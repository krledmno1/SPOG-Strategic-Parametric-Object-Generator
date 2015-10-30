package gov.nasa.generator.generators;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import com.rits.cloning.Cloner;



public class CartesianStrategy<T> extends GenerationStrategy<T> {

	//NumberGenerator
	@Override
	T generate(NumberGenerator<Number> g) {
		T object = peek(g);
		next(g);
		return object;
	}
	@Override
	boolean hasNext(NumberGenerator<Number> g) {
		return g.hasNext;
	}
	@Override
	void reset(NumberGenerator<Number> g) {
		g.hasNext=g.min.le(g.max);
		g.curr=g.begin;
		
	}
	@Override
	T peek(NumberGenerator<Number> g) {
		Cloner cloner = new Cloner();
		T object = (T) cloner.deepClone(g.curr.getValue());
		return object;
	}
	@Override
	void next(NumberGenerator<Number> g) {
		if(g.curr.plus(g.step).gt(g.max)){
			g.curr=g.min;
		}
		else{
			g.curr=g.curr.plus(g.step);
		}
		if(g.curr.eq(g.begin)){
			g.hasNext=false;
		}
			
	}
	@Override
	boolean isLast(NumberGenerator<Number> g) {
		if(g.curr.plus(g.step).gt(g.max)){
			return g.min.eq(g.begin);
		}
		else{
			return g.curr.plus(g.step).eq(g.begin); 
		}
	}
	
	
	
	//ClassGenerator	
	@Override
	T generate(ClassGenerator<T> g) throws ParseException, GenerationException {
		T object = peek(g);
		next(g);
		return object;
	}
	@Override
	boolean hasNext(ClassGenerator<T> g) {
		return g.hasNext;
	}
	@Override
	void reset(ClassGenerator<T> g) throws ParseException, GenerationException {

		//reset subgenerators and materialize current element
		Object [] values = new Object[g.fieldList.length];
		int i=0;
		for(Field f : g.fieldList){
			g.generators.get(f).reset();
			values[i]=g.generators.get(f).peek();
			i++;
		}
		g.current=g.construct(values);
		
		//update hasNext
		boolean hasNext = false;
		for (AbstractGenerator<T> subgen: g.generators.values()) {
			hasNext=hasNext||subgen.hasNext();
		}
		g.hasNext=hasNext;
		
	}
	@Override
	T peek(ClassGenerator<T> g) {
		Cloner cloner = new Cloner();
		T object = (T) cloner.deepClone(g.current);
		return object;
	}
	@Override
	void next(ClassGenerator<T> g) throws GenerationException, ParseException {
		int i = 0;
		while(i<g.fieldList.length && g.generators.get(g.fieldList[i]).isLast()){
			g.generators.get(g.fieldList[i]).reset();
			i++;
		}
		if(i<g.fieldList.length){
			//increase 
			g.generators.get(g.fieldList[i]).next();
			//set next current
			Object [] values = new Object[g.fieldList.length];
			int j=0;
			for(Field f : g.fieldList){
				values[j]=g.generators.get(f).peek();
				j++;
			}
			g.current=g.construct(values);
		}
		else{
			g.hasNext=false;
		}
			
	}
	@Override
	boolean isLast(ClassGenerator<T> g) {
		boolean isLast = false;
		for (AbstractGenerator<T> subgen: g.generators.values()) {
			isLast=isLast||subgen.isLast();
		}
		return isLast;
	}

	
	
	//ListGenerator
	@Override
	List<T> generate(ListGenerator<T> g) throws ParseException, GenerationException {
		List<T> object = peek(g);
		next(g);
		return object;
	}
	@Override
	boolean hasNext(ListGenerator<T> g) {
		return g.hasNext;
	}
	@Override
	void reset(ListGenerator<T> g) throws ParseException, GenerationException {
		//reset all the generators
		g.generators=new ArrayList<AbstractGenerator>(g.length);
		g.generators.add(g.generator.cloneGenerator());
		
		//set current element
		g.current= new ArrayList<T>(g.length);
		g.current.add((T) g.generators.get(0).peek());
		
		//set hasNext
		g.hasNext=g.generators.get(0).hasNext()&&g.length>1;
	}
	@Override
	List<T> peek(ListGenerator<T> g) {
		Cloner cloner = new Cloner();
		List<T> object = (List<T>) cloner.deepClone(g.current);
		return object;
	}
	@Override
	void next(ListGenerator<T> g) throws ParseException, GenerationException {
		int i = 0;
		while(i<g.generators.size() && g.generators.get(i).isLast()){
			g.generators.get(i).reset();
			g.current.set(i, (T) g.generators.get(i).peek());
			i++;
		}
		if(i<g.generators.size()){
			//increase
			g.generators.get(i).next();
			//set next current
			g.current.set(i, (T) g.generators.get(i).peek());
		}
		else{
			
			if(g.generators.size()<g.length){
				//reset all existing generators
				for (AbstractGenerator<?> subgen : g.generators) {
					subgen.reset();
				}
				//add a new one
				g.generators.add(g.generator.cloneGenerator());
				//rebuild current element
				g.current= new ArrayList<T>(g.length);
				for (AbstractGenerator<?> subgen : g.generators) {
					g.current.add((T)subgen.peek());
				}
			}
			else{
				g.hasNext=false;
			}
		}
	}
	@Override
	boolean isLast(ListGenerator<T> g) {
		boolean isLast = g.generators.size()==g.length;
		if(isLast){
			for (AbstractGenerator<T> gen : g.generators) {
				isLast = isLast && gen.isLast();
			}
		}
		return isLast;
	}

	
	//AbstractClassGenerator
	@Override
	T generate(AbstractClassGenerator<T> g) throws ParseException, GenerationException {
		T object = peek(g);
		next(g);
		return object;
	}
	@Override
	boolean hasNext(AbstractClassGenerator<T> g) {
		return g.hasNext;
	}
	@Override
	void reset(AbstractClassGenerator<T> g) throws ParseException, GenerationException {
		for (AbstractGenerator<T> classGenerator : g.generators) {
			classGenerator.reset();
		}
		g.current = (T) g.generators.get(0).peek();

		g.hasNext=g.generators.size()>0;
	}
	@Override
	T peek(AbstractClassGenerator<T> g) {
		Cloner cloner = new Cloner();
		T object = (T) cloner.deepClone(g.current);
		return object;
	}
	@Override
	void next(AbstractClassGenerator<T> g) throws ParseException, GenerationException {
		int i=0;
		while(i<g.generators.size() && g.generators.get(i).isLast()){
			i++;
		}
		if(i<g.generators.size()){
			g.generators.get(i).next();
			g.current = (T) g.generators.get(i).peek();
		}else{
			g.hasNext=false;
		}
	}
	@Override
	boolean isLast(AbstractClassGenerator<T> g) {
		return g.generators.get(g.generators.size()-1).isLast();
	}

}
