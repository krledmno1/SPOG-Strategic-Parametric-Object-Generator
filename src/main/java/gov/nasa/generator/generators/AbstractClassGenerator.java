package gov.nasa.generator.generators;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

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
	public static <T> AbstractClassBuilder<T> builder(Class<T> c, GenerationStrategy<T> s){
		 return new AbstractClassGenerator.AbstractClassBuilder<T>(c, s);
	}
	
	
	List<AbstractGenerator<T>> generators = new ArrayList<AbstractGenerator<T>>();
	T current;
	
	protected AbstractClassGenerator(gov.nasa.generator.generators.AbstractGenerator.Build<T> b) throws ParseException, GenerationException {
		super(b);
		String name = clazz.getPackage().getName();
		
		Reflections reflections = new Reflections(name);

		Set<Class<? extends T>> allClasses = 
		      reflections.getSubTypesOf(clazz);
		
		for (Class<? extends T> sub : allClasses) {
			generators.add(ClassGenerator.builder(sub, strategy)
										 .depth(depth)
										 .length(length)
										 .path(path)
										 .instance());
		}
		
		reset();
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
				.depth(depth)
				.length(length)
				.path(path)
				.instance();
	}

}
