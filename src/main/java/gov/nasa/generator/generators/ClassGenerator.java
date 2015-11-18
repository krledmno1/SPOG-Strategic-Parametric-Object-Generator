package gov.nasa.generator.generators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import gov.nasa.generator.configurations.InputConf;


/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 * This class implements AbstractGenerator interface 
 * for class user objects. User must instantiate 
 * ClassGenerator to generate objects of his/her
 * custom class T. T is assumed to be annotated with 
 * Generate annotations
 * 
 * Class requirements:
 * 
 * 1. It implements the AbstractGenerator interface 
 * as simple calls to the abstract strategy and
 * passing itself as parameter. Each generation 
 * strategy must implement  AbstractGenerator interface 
 * for each parameterized by the Generator type.  
 * 
 * 2. It is responsible for analyzing all non private 
 * fields of the class T and creating and keeping an 
 * appropriate generator for every field.
 * 
 * 3. It is responsible for initializing parameters
 * min, max and step for every field from different sources.
 * Default sources are Generate annotations, but user can specify
 * custom sources (csv, commons conf, ...).
 * 
 * 
 *
 * @param <T> - Class type to be generated (assumes it has been annotated with Generate)
 */
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
		
		//get the FIRST constructor
		Constructor<T>[] constructors = (Constructor<T>[]) clazz.getConstructors();
		if(constructors.length==0){
			noConstructor();
		}
		
		//all declared fields
		fieldList = getAllFields();
		
		//generable fields

		//get the constructor and assign generators
		List<Class> paramList = new ArrayList<Class>();
		
		for (Field field : fieldList) {
			//assign generators
			AbstractGenerator<?> gen = assignGenerators(field);
			if(gen!=null){
				generators.put(field, gen);
			}
			else{
				throw new GenerationException("Unsupported "
						+ "java construct: we currently support "
						+ "abstract, concrete classes, lists, "
						+ "Integers and Doubles");
			}
			
			//get parameter list for the constructor
			paramList.add(field.getType());
				
			
		}
		Class [] params = new Class[paramList.size()];
		int i = 0;
		for (Class cs : paramList) {
			params[i]=cs;
			i++;
		}
		try {
			//redundant
			constructor = (Constructor<T>) clazz.getConstructor(params);
		} catch (NoSuchMethodException | SecurityException e) {
			invalidConstructor(params);
		}

		//reset
		reset();

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
				.input(input)
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

	
	@Override
	protected void visit(InputConf config) {
		
		for (Field field : fieldList) {
			if(isPrimitive(field)){
					config.writeMin(generateFieldKey(field), "");
					config.writeMax(generateFieldKey(field), "");
					config.writeStep(generateFieldKey(field), "");
			}
			else{
				generators.get(field).checkAndVisit(config);
			}
		}
		
	}


	

}
