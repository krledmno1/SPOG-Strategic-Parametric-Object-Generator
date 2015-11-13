package gov.nasa.generator.generators;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;


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


	

}
