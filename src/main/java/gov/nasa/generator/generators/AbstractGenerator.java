package gov.nasa.generator.generators;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import gov.nasa.generator.configurations.DefaultInput;
import gov.nasa.generator.configurations.InputConf;
import gov.nasa.generator.generators.NumberGenerator.TypeWrapper;

public abstract class AbstractGenerator<T> {

	//required
	protected Class<T> clazz;
	protected GenerationStrategy<T> strategy;

	
	//optional with default values
	protected InputConf input;
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
		private InputConf input=new DefaultInput();
		private int depth=2; 
		private int length=2;
		private boolean topLvl=true;

		
		public Build(Class<T> c, GenerationStrategy<T> s){
			clazz=c;
			strategy=s;
		}
			
		public Build<T> input(InputConf i){
			input=i;
			return this;
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
	
	protected AbstractGenerator(Build<T> b) throws GenerationException{
		
		clazz=b.clazz;
		strategy=b.strategy;
		input=b.input;
		depth=b.depth;
		length=b.length;
		topLvl=b.topLvl;
		
		if(clazz == null || strategy == null || input==null){
			throw new GenerationException("Generator must have non-null target class, strategy and input");
		}
		
	}
	
	
	public abstract Object generate() throws ParseException, GenerationException;

	public abstract boolean hasNext();

	abstract void reset() throws ParseException, GenerationException;
		
	abstract Object peek();
	
	abstract void next() throws ParseException, GenerationException;
	
	abstract boolean isLast();
	
	protected abstract AbstractGenerator<T> cloneGenerator() throws ParseException, GenerationException;
		
	/**
	 * Returns true if field should be considered in generation
	 * 
	 * @param field - field to check
	 * @return true if if the field is associated with a generator
	 */
	protected boolean generable(Field field) {
//		String key = generateFieldKey(field);
//		boolean ret = input.exists(key); 
		return  !isPrimitive(field) || input.exists(generateFieldKey(field));
	}

	/**
	 * Analyzes the class T and all its superclasses and returns a list of 
	 * fields that T has declared or inherited in order of declaration 
	 * starting from the most general superclass until class T
	 * Object class is not considered.
	 * 
	 * @return sorted list of fields
	 */
	protected Field[] getAllFields() {
		//get all the declared fields in the class and all non private fields
		//in all superclasses
		//TODO: this will change when we annotate also non-primitive types
		Class<?> current = clazz;
		Deque<Field> fields = new ArrayDeque<Field>();
		for (Field field : current.getDeclaredFields()) {
			if(generable(field)){
				fields.add(field);
			}
		}
		current = current.getSuperclass();		
		List<Field> currentfields = new ArrayList<Field>();
		while(current.getSuperclass()!=null){ // we don't want to process Object.class
			for (Field field : current.getDeclaredFields()) {
				if(!Modifier.isPrivate(field.getModifiers()) && generable(field)){
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
	
	
	protected void noConstructor() throws GenerationException {
		String msg = "Class "+ clazz.getSimpleName() +" must declare at least one constructor\n";
		throw new GenerationException(msg);
	}
	
	
	protected void invalidConstructor(Class[] params) throws GenerationException {
		String msg = "Class "+ clazz.getSimpleName() +" needs to declare an appropriate public constructor\n" +	
		 "containing all the fields indended for generation as parameters" +
		 "For instance:\n" +
		 "public "+clazz.getSimpleName()+"(";
		int p = 1;
		for (Class c : params) {
			msg+= c.getSimpleName()+ " p"+p + " ";
			p++;
		}
		msg+=")";
		throw new GenerationException(msg);
		
	}
	

	/**
	 * 
	 * Returns true if the field is primitive
	 * Currently primitive fields can be of type Double or Integer 
	 * 
	 * @param field - field to check
	 * @return true if field is primitive
	 */
	protected boolean isPrimitive(Field field) {
		//TODO: what if user inherits Number
		return Number.class.isAssignableFrom(field.getType());
	}

	
	/**
	 * The method assigns a generator to a field depending on its type
	 * A precondition is that this method is called only for generable fields
	 * and class invariant is input != null
	 * 
	 * @param field - field of the clazz
	 * @param <K> - primitive type parameter 
	 * @return AbstractGenerator associated to the field
	 * @throws ParseException - thrown if the annotations are not valid strings
	 * @throws GenerationException - thrown if the class does not have a valid structure 
	 * (a public constructor with all the parameters that are annotated for generation)
	 */
	protected <K extends Number> AbstractGenerator<?> assignGenerators(Field field) 
										  throws ParseException, GenerationException {
		
		//read from the configuration
		if(isPrimitive(field)){
			//Primitive types (we assume that only primitive types are (possibly) annotated)
			//TODO: this will change when we annotate also non-primitive types
			String type = field.getType().getSimpleName().toUpperCase();
			
			String key = generateFieldKey(field);
			
			TypeWrapper<K> min = TypeWrapper.extractValue(input.readMin(key), type);
			TypeWrapper<K> max = TypeWrapper.extractValue(input.readMax(key), type);
			TypeWrapper<K> step = TypeWrapper.extractValue(input.readStep(key), type);
			
			return NumberGenerator.builder(field.getType(),
					   strategy, 
					   min, max, 
					   step).depth(depth)
							.length(length)
							.instance();
			
		}
		
		if(Collection.class.isAssignableFrom(field.getType())){
			//lists
			
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			Class<?> genericClass = (Class<?>) pt.getActualTypeArguments()[0];
						
			return ListGenerator.builder(genericClass,
					strategy)
					.input(input)
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
					.input(input)
					.depth(depth)
					.length(length)
					.topLvl(topLvl)
					.instance();
			
		}
		
		//we assume a concrete class if none from above
		return ClassGenerator.builder(field.getType(),
				strategy)
				.input(input)
				.depth(depth)
				.length(length)
				.topLvl(topLvl)
				.instance();
		
		
	}


	/**
	 * Generated a unique string key that identifies a field
	 * 
	 * @param field - field for which we want to generate key
	 * @return unique string key
	 */
	protected String generateFieldKey(Field field) {
		return clazz.getName()+"."+field.getName();
	}

	

	
	
	/**
	 * Write default configuration template
	 * on the standard output
	 */
	public void writeConfigTemplate(){
		writeConfigTemplate(input);
	}


	/**
	 * Write configuration template using the 
	 * config as a method. The format depends on the
	 * Input config class.
	 * 
	 * @param config - Input config object, defines 
	 * format and write method for the template
	 *   
	 */
	public void writeConfigTemplate(InputConf config){
		visited = null;
		checkAndVisit(config);
		
	}
	
	private static Set<Class<?>> visited = null;

	/**
	 * Initialized a static data structure used to make sure 
	 * that every class is visited only once
	 * 
	 * @param config - config object defines format and write method
	 */
	protected void checkAndVisit(InputConf config){
		if(visited==null){
			visited = new HashSet<Class<?>>();
		}
		
		if(!visited.contains(clazz)){
			visited.add(clazz);
			visit(config);
		}
		
	}


	/**
	 * Visits a particular Generator
	 * without checking if it was visited before
	 * 
	 * @param config - config object defines format and write method
	 */
	abstract protected void visit(InputConf config);

}
