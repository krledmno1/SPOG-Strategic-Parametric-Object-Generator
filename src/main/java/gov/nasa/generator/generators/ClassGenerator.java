package gov.nasa.generator.generators;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
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
		//read file
		String csvPath=path+clazz.getSimpleName()+".csv";
		List<Map<Field,TypeWrapper<Number>>> bounds = read(csvPath);
		
		fieldList = clazz.getDeclaredFields();
		
		//TODO: check if all the fields where declared in the csvfile and vv
		
		//get the constructor and assign generators
		Class [] params = new Class[fieldList.length];
		int i=0;
		for (Field field : fieldList) {
			//assign generators
			assignGenerators(field,bounds);
			
			//get parameter list for the constructor
			params[i]=field.getType();
			i++;
		}
		try {
			constructor = (Constructor<T>) clazz.getConstructor(params);
		} catch (NoSuchMethodException | SecurityException e) {
			throw new ParseException("Class needs to declare a constructor with all the fields as parameters", 0);
		}

		//reset
		reset();

	}
	
	
	
	
	
	//TODO: change its  return type to AbstractGenerator and move it to AbstractGenerator
	//potentially can be reused.
	private void assignGenerators(Field field, 
								  List<Map<Field, TypeWrapper<Number>>> bounds) 
										  throws ParseException, GenerationException {
		
		TypeWrapper<Number> min = bounds.get(0).get(field);
		TypeWrapper<Number> max = bounds.get(1).get(field); 
		TypeWrapper<Number> step = bounds.get(2).get(field);
		
		if(min!=null && max!=null && step!=null){
			//Primitive types
			generators.put(field,
						   NumberGenerator.builder(field.getType(),
												   strategy, 
												   min, max, 
												   step).depth(depth)
						   								.length(length)
						   								.path(path)
						   								.instance());
			return;
		}
		if(Collection.class.isAssignableFrom(field.getType())){
			
			ParameterizedType pt = (ParameterizedType) field.getGenericType();
			Class<?> genericClass = (Class<?>) pt.getActualTypeArguments()[0];
			
			generators.put(field, ListGenerator.builder(genericClass,
														strategy)
														.depth(depth)
														.length(length)
														.path(path)
														.instance());
			return;
		}
		
		if(field.getType().isInterface()){
			//TODO: interfaces
			return;
		}
		
		if(Modifier.isAbstract(field.getType().getModifiers())){
			//TODO: abstract classes
			return;
		}
		
		//concrete classes
		generators.put(field, ClassGenerator.builder(field.getType(),
													strategy)
													.depth(depth)
													.length(length)
													.path(path)
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
				.path(path)
				.instance();
	}

	
	
	
	
	/**
	 * This method reads the CSV file and parses MIN, MAX, and STEP values
	 * It checks if the csv file has the right format:
	 * <field name>, <field type>, <value range>
	 * where 
	 * <field name> MUST match the field name of T
	 * <field type> MUST match the field type of <field name> of T
	 * and <value range> is of the form:
	 * [num_1 to num_2 step num_3] 
	 * or
	 * [num_1 , num_2]
	 * where num_i is a number and
	 * num_1 is min value for the parameter
	 * num_2 is max value for the parameter
	 * num_3 is the step increase value for the parameter (default 1)
	 * @param <K>
	 * 
	 *
	 * @throws ParseException in the following cases:
	 * 1. if csv is not the right format
	 * 2. if <field name> does not exists in T
	 * 3. if <field type> is not the type of the <field name> in T
	 * 4. if csv file cannot be found
	 *
	 */
	private <K extends Number> List<Map<Field,TypeWrapper<K>>> read(String pathToCSV) throws ParseException{
		try {
			Map<Field,TypeWrapper<K>> min=new HashMap<Field, TypeWrapper<K>>();
			Map<Field,TypeWrapper<K>> max=new HashMap<Field, TypeWrapper<K>>();
			Map<Field,TypeWrapper<K>> step=new HashMap<Field, TypeWrapper<K>>();
			
			
			CSVReader reader = new CSVReader(new FileReader(pathToCSV));
			String [] nextLine;
			int line = 1;
			while ((nextLine = reader.readNext()) != null) {
				//check if there are 3 columns in each csv row
				if(nextLine.length==3){
					String name = nextLine[0];
					String type = nextLine[1];
					String values = nextLine[2];
					
					//check if name exists 
					try {
						Field field = clazz.getDeclaredField(name);
						//check if the type is correct
						if(field.getType().getSimpleName().toUpperCase().equals(type.toUpperCase())){
							//extract bounds
							TypeWrapper<K>[] bounds = TypeWrapper.extractBounds(values,type);
							//put them into the HashMaps
							min.put(field, bounds[0]);
							max.put(field, bounds[1]);
							step.put(field, bounds[2]);
						}
						else{
					   		reader.close();
					   		throw new ParseException("Incorrect type: " + type + " expected: " + field.getType().getSimpleName(), line);						
						}
						
					} catch (NoSuchFieldException | SecurityException e) {
						reader.close();
						throw new ParseException("No such filed: " + name + " in " + clazz.getName(), line);
					} 
				}
				else{
					reader.close();
					throw new ParseException("Lines shoud contain 3 members", line);
				}
			    line++;
			}
			reader.close();
			List<Map<Field,TypeWrapper<K>>> list = new ArrayList<Map<Field,TypeWrapper<K>>>(3);
			list.add(min);
			list.add(max);
			list.add(step);
			return list;
			
		} catch (IOException e1) {
			throw new ParseException("File could not be found or read: "+ pathToCSV, 0);
		}
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
