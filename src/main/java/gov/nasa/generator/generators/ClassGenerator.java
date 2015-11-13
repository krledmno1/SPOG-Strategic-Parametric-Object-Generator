package gov.nasa.generator.generators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import gov.nasa.generator.configurations.InputConf;


/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 * This class implements AbstractGenerator interface 
 * for class user objects. User must instantiate 
 * ClassGenerator to generate objects of his/her
 * custom class T. T is assumed to be annotated with 
 * @Generate annotations
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
 * Default sources are @Generate annotations, but user can specify
 * custom sources (csv, commons conf, ...).
 * 
 * 
 *
 * @param <T> - Class type to be generated (assumes it has been annotated with @Generate)
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
		
		fieldList = getAllFields();

		//get the constructor and assign generators
		Class [] params = new Class[fieldList.length];
		int i=0;
		for (Field field : fieldList) {
			//assign generators
			AbstractGenerator<?> gen = assignGenerators(field);
			if(gen!=null){
				generators.put(field, gen);
			}
			
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

	
	@Override
	protected void visit(InputConf config) {
		
		for (Field field : fieldList) {
			if(isPrimitive(field)){
				if(config!=null){
					config.writeMin(generateFieldKey(field), "");
					config.writeMax(generateFieldKey(field), "");
					config.writeStep(generateFieldKey(field), "");
				}
				else{
					System.out.println(generateFieldKey(field)+".min"+"=");
					System.out.println(generateFieldKey(field)+".max"+"=");
					System.out.println(generateFieldKey(field)+".step"+"=");
				}
			}
			else{
				generators.get(field).checkAndVisit(config);
			}
		}
		
	}


	

}
