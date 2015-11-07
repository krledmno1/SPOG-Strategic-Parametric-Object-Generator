package gov.nasa.generator.generators;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import gov.nasa.generator.annotations.Generate;
import gov.nasa.generator.generators.NumberGenerator.TypeWrapper;

public class ClassGenerator<T> extends AbstractGenerator<T> {

	public static class ClassBuilder<T> extends AbstractGenerator.Build<T>{

		public ClassBuilder(Class<T> c, GenerationStrategy<T> s) {
			super(c, s);
		}

		@Override
		public AbstractGenerator<T> instance() throws ParseException, GenerationException {
			return new ClassGenerator<T>(this);
		}
		
	}
	public static <T> ClassBuilder<T> builder(Class<?> c, GenerationStrategy<T> s){
		 return new ClassGenerator.ClassBuilder<T>((Class<T>)c, s);
	}
	
	Map<Field,AbstractGenerator> generators = new HashMap<Field,AbstractGenerator>();
	T current;
	Field [] fieldList;
	private Constructor <T> constructor;
	
	protected ClassGenerator(gov.nasa.generator.generators.AbstractGenerator.Build<T> b) throws ParseException, GenerationException {
		super(b);
		
		//get all the declared fields in the class and all non private fields
		//in all superclasses		
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
		fieldList = fields.toArray(new Field[0]);
		
		
		//TODO: check if all the fields where declared in the csvfile and vv
		
		//get the constructor and assign generators
		Class [] params = new Class[fieldList.length];
		int i=0;
		for (Field field : fieldList) {
			//assign generators
			assignGenerators(field);
			
			//get parameter list for the constructor
			params[i]=field.getType();
			i++;
		}
		try {
			constructor = (Constructor<T>) clazz.getConstructor(params);
		} catch (NoSuchMethodException | SecurityException e) {
			String msg = "Class "+ clazz.getSimpleName() +" needs to declare "
					+ "a PUBLIC constructor with all the fields as parameters"
					+ "For instance:\n"
					+ "public "+clazz.getSimpleName()+"(";
					int p = 1;
					for (Class c : params) {
						msg+= c.getSimpleName()+ " p"+p + " ";
						p++;
					}
					msg+=")";
			
			throw new ParseException(msg, 0);
		}

		//reset
		reset();

	}
	
	
	
	
	
	private boolean generable(Field field) {
		Generate generate = null;
		for(Annotation annotation : field.getAnnotations()) {
			if(annotation.annotationType() == Generate.class) {
				generate=(Generate)annotation;
			}

		}
		return generate!=null;
	}

	private boolean isPrimitive(Field field) {
		//TODO: what if user inherits Number
		return Number.class.isAssignableFrom(field.getType());
	}



	//TODO: change its  return type to AbstractGenerator and move it to AbstractGenerator
	//potentially can be reused.
	private <K extends Number> void assignGenerators(Field field) 
										  throws ParseException, GenerationException {
		
		Generate generate = null;
		for(Annotation annotation : field.getAnnotations()) {
			if(annotation.annotationType() == Generate.class) {
				generate=(Generate)annotation;
			}

		}

		
		if(generate!=null){
			//Primitive types (we assume that only primitive types are (possibly) annotated)
			String type = field.getType().getSimpleName().toUpperCase();
			TypeWrapper<K> min = TypeWrapper.extractValue(generate.min(), type);
			TypeWrapper<K> max = TypeWrapper.extractValue(generate.max(), type);
			TypeWrapper<K> step = TypeWrapper.extractValue(generate.step(), type);
			
			generators.put(field,
						   NumberGenerator.builder(field.getType(),
												   strategy, 
												   min, max, 
												   step).depth(depth)
						   								.length(length)
						   								.instance());
			return;
		}
		if(isPrimitive(field)){
			//Skip unannotated primitive fields
			return;
		}
		if(Collection.class.isAssignableFrom(field.getType())){
			//lists
			
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			Class<?> genericClass = (Class<?>) pt.getActualTypeArguments()[0];
			
			generators.put(field, ListGenerator.builder(genericClass,
														strategy)
														.depth(depth)
														.length(length)
														.topLvl(topLvl)
														.instance());
			return;
		}
		
		if(field.getType().isInterface()){
			//TODO: interfaces
			return;
		}
		
		if(Modifier.isAbstract(field.getType().getModifiers())){
			//abstract classes
			generators.put(field, AbstractClassGenerator.builder(field.getType(),
					strategy)
					.depth(depth)
					.length(length)
					.topLvl(topLvl)
					.instance());
			
			return;
		}
		
		//concrete classes
		generators.put(field, ClassGenerator.builder(field.getType(),
													strategy)
													.depth(depth)
													.length(length)
													.topLvl(topLvl)
													.instance());
		return;
		
		
	}

	





	//implementation of abstract methods
	public T generate() throws ParseException, GenerationException{
		return strategy.generate(this);
	}
	public boolean hasNext(){
		return strategy.hasNext(this);
	}
	void reset() throws ParseException, GenerationException{
		strategy.reset(this);
	}
	T peek(){
		return strategy.peek(this);
	}
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
				.depth(depth)
				.length(length)
				.topLvl(topLvl)
				.instance();
	}


	public T construct(Object[] values) throws GenerationException {
			try {
				return (T) constructor.newInstance(values);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				throw new GenerationException("Cannot instantiate type " + clazz);
			}
		
	}







	
	
	
	

}
