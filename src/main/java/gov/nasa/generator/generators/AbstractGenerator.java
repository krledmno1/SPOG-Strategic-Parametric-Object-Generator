package gov.nasa.generator.generators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

import gov.nasa.generator.annotations.Generate;
import gov.nasa.generator.generators.NumberGenerator.TypeWrapper;

public abstract class AbstractGenerator<T> {

	//required
	protected Class<T> clazz;
	protected GenerationStrategy<T> strategy;
	
	//optional with default values
	protected int depth; 
	protected int length;
	protected boolean topLvl;
	
	//internal use only
	boolean hasNext;

	
	public abstract static class Build<T>{
		
		//required
		private Class<T> clazz;
		private GenerationStrategy<T> strategy;
		
		//optional with default values
		private int depth=2; 
		private int length=2;
		private boolean topLvl=true;

		
		public Build(Class<T> c, GenerationStrategy<T> s){
			clazz=c;
			strategy=s;
		}
		
		public Build<T> depth(int d){
			depth=d;
			return this;
		}
		
		public Build<T> length(int l){
			length=l;
			return this;
		}
		public Build<T> topLvl(boolean t){
			topLvl=t;
			return this;
		}
		
		abstract public AbstractGenerator<T> instance() throws ParseException, GenerationException;
		
	}
	
	protected AbstractGenerator(Build<T> b){
		
		clazz=b.clazz;
		strategy=b.strategy;
		depth=b.depth;
		length=b.length;
		topLvl=b.topLvl;
		
	}
	
	
	public abstract Object generate() throws ParseException, GenerationException;

	public abstract boolean hasNext();

	abstract void reset() throws ParseException, GenerationException;
		
	abstract Object peek();
	
	abstract void next() throws ParseException, GenerationException;
	
	abstract boolean isLast();
	
	protected abstract AbstractGenerator<T> cloneGenerator() throws ParseException, GenerationException;
		
	
	protected Field[] getAllFields() {
		//get all the declared fields in the class and all non private fields
		//in all superclasses
		//TODO: this will change when we annotate also non-primitive types
		Class<?> current = clazz;
		Deque<Field> fields = new ArrayDeque<Field>();
		for (Field field : current.getDeclaredFields()) {
			if(generable(field) || !isPrimitive(field)){
				fields.add(field);
			}
		}
		current = current.getSuperclass();		
		List<Field> currentfields = new ArrayList<Field>();
		while(current.getSuperclass()!=null){ // we don't want to process Object.class
			for (Field field : current.getDeclaredFields()) {
				if(!Modifier.isPrivate(field.getModifiers()) && (generable(field)|| !isPrimitive(field))){
					currentfields.add(field);
				}
			}
			for(int i = currentfields.size()-1;i>=0;i--){
				fields.addFirst(currentfields.get(i));
			}
			currentfields.clear();			
			current = current.getSuperclass();
		}
		return fields.toArray(new Field[0]);
	}
	
	
	protected boolean generable(Field field) {
		Generate generate = null;
		for(Annotation annotation : field.getAnnotations()) {
			if(annotation.annotationType() == Generate.class) {
				generate=(Generate)annotation;
			}

		}
		return generate!=null;
	}

	protected boolean isPrimitive(Field field) {
		//TODO: what if user inherits Number
		return Number.class.isAssignableFrom(field.getType());
	}

	
	/**
	 * The method assigns a generator to a field depending on its type
	 * @param field - field of the clazz
	 * @return AbstractGenerator associated to the field
	 * @throws ParseException - thrown if the annotations are not valid strings
	 * @throws GenerationException - thrown if the class does not have a valid structure 
	 * (a public constructor with all the parameters that are annotated for generation)
	 */
	protected <K extends Number> AbstractGenerator<?> assignGenerators(Field field) 
										  throws ParseException, GenerationException {
		
		Generate generate = null;
		for(Annotation annotation : field.getAnnotations()) {
			if(annotation.annotationType() == Generate.class) {
				generate=(Generate)annotation;
			}

		}

		
		if(generate!=null){
			//Primitive types (we assume that only primitive types are (possibly) annotated)
			//TODO: this will change when we annotate also non-primitive types
			String type = field.getType().getSimpleName().toUpperCase();
			TypeWrapper<K> min = TypeWrapper.extractValue(generate.min(), type);
			TypeWrapper<K> max = TypeWrapper.extractValue(generate.max(), type);
			TypeWrapper<K> step = TypeWrapper.extractValue(generate.step(), type);
			
			return NumberGenerator.builder(field.getType(),
					   strategy, 
					   min, max, 
					   step).depth(depth)
							.length(length)
							.instance();
			
		}
		if(isPrimitive(field)){
			//Skip unannotated primitive fields
			return null;
		}
		if(Collection.class.isAssignableFrom(field.getType())){
			//lists
			
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			Class<?> genericClass = (Class<?>) pt.getActualTypeArguments()[0];
						
			return ListGenerator.builder(genericClass,
					strategy)
					.depth(depth)
					.length(length)
					.topLvl(topLvl)
					.instance();
			
		}
		
		if(field.getType().isInterface()){
			//TODO: interfaces
			return null;
		}
		
		if(Modifier.isAbstract(field.getType().getModifiers())){
			//abstract classes
			
			return AbstractClassGenerator.builder(field.getType(),
					strategy)
					.depth(depth)
					.length(length)
					.topLvl(topLvl)
					.instance();
			
		}
		
		
		return ClassGenerator.builder(field.getType(),
				strategy)
				.depth(depth)
				.length(length)
				.topLvl(topLvl)
				.instance();
		
		
	}


}
