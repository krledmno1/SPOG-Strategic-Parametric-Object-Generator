package gov.nasa.generator.generators;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import gov.nasa.generator.configurations.InputConf;

public class AbstractClassGenerator<T> extends AbstractGenerator<T> {

	public static class AbstractClassBuilder<T> extends AbstractGenerator.Build<T>{

		public AbstractClassBuilder(Class<T> c, GenerationStrategy<T> s) {
			super(c, s);
		}

		@Override
		public AbstractGenerator<T> instance() throws ParseException, GenerationException {
			return new AbstractClassGenerator<T>(this);
		}
		
	}
	public static <T> AbstractClassBuilder<T> builder(Class<?> c, GenerationStrategy<T> s){
		 return new AbstractClassGenerator.AbstractClassBuilder<T>((Class<T>)c, s);
	}
	
	
	List<AbstractGenerator<T>> generators = new ArrayList<AbstractGenerator<T>>();
	Deque<AbstractGenerator<T>> workingGenerators = new ArrayDeque<AbstractGenerator<T>>();
	AbstractGenerator<T> currentGenerator;
	T current;
	
	protected AbstractClassGenerator(gov.nasa.generator.generators.AbstractGenerator.Build<T> b) throws ParseException, GenerationException {
		super(b);
		
		
		//TODO: Differensiate on the generic type: if primitive, list...
		String name = clazz.getPackage().getName();
		Reflections reflections = new Reflections(name);
		
		//get all subclasses
		Set<Class<? extends T>> allClasses = 
		      reflections.getSubTypesOf(clazz);
		
		boolean isRecursiveHierarchy = false;
		for (Class<? extends T> sub : allClasses) {
			if(isRecursive(sub)){
				isRecursiveHierarchy = true;
			}
		}
		
		if(isRecursiveHierarchy){
			for (Class<? extends T> sub : allClasses) {
				
				if(isRecursive(sub)){
					
						
						for(int i=depth-1;i>0;i--){
							generators.add(ClassGenerator.builder(sub, strategy)
									 .input(input)
									 .depth(i)
									 .length(length)
									 .topLvl(false)
									 .instance());
						}
				}
				else{
					if(topLvl || depth==1){
						generators.add(ClassGenerator.builder(sub, strategy)
							 .input(input)
							 .depth(depth)
							 .length(length)
							 .topLvl(topLvl)
							 .instance());
					}
					
				}
			}
		}
		else{
			for (Class<? extends T> sub : allClasses) {
				generators.add(ClassGenerator.builder(sub, strategy)
						 .input(input)
						 .depth(depth)
						 .length(length)
						 .topLvl(topLvl)
						 .instance());
			}
		}
		
		
		if(generators.size()>0){
			reset();
		}
		else{
			throw new GenerationException("No non-recursive sub-classes");
		}
	}
	
	private boolean isRecursive(Class<? extends T> sub) {

		Field [] fieldList = sub.getDeclaredFields();
		
		for (Field field : fieldList) {
			if(field.getType().equals(clazz)){
				return true;
			}
		}
		return false;
	}

	@Override
	public T generate() throws ParseException, GenerationException{
		return strategy.generate(this);
	}
	@Override
	public boolean hasNext(){
		return strategy.hasNext(this);
	}
	@Override
	void reset() throws ParseException, GenerationException{
		strategy.reset(this);
	}
	@Override
	T peek(){
		return strategy.peek(this);
	}
	@Override
	void next() throws ParseException, GenerationException{
		strategy.next(this);
	}
	@Override
	boolean isLast() {
		return strategy.isLast(this);
	}
	@Override
	protected AbstractGenerator<T> cloneGenerator() throws ParseException, GenerationException {
		return builder(clazz, strategy)
				.input(input)
				.depth(depth)
				.length(length)
				.topLvl(topLvl)
				.instance();
	}

	@Override
	protected void visit(InputConf config) {
		for (AbstractGenerator<T> abstractGenerator : generators) {
			abstractGenerator.checkAndVisit(config);
		}
	}



}
