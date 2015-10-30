package gov.nasa.generator.generators;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ListGenerator<T> extends AbstractGenerator<T> {

	
	public static class ListBuilder<T> extends AbstractGenerator.Build<T>{
		
		public ListBuilder(Class<T> c, GenerationStrategy<T> s) {
			super(c, s);
		}

		@Override
		public AbstractGenerator<T> instance() throws ParseException, GenerationException {
			return new ListGenerator<T>(this);
		}
		
	}
	public static <T> ListBuilder<T> builder(Class<?> c, GenerationStrategy<T> s){
		 return new ListGenerator.ListBuilder<T>((Class<T>)c, s);
	}
	
	
	//derrived fields
	AbstractGenerator<T> generator;
	List<AbstractGenerator> generators = new ArrayList<AbstractGenerator>(length);
	List<T> current;
	
	protected ListGenerator(ListBuilder<T> b) throws ParseException, GenerationException {
		super(b);
		//TODO: check the type of the generic class... primitive, abstract...? 
		//(for now we just assume is a concrete class)
		//list might need to be extended to consider min, max and step to support numbers
		generator =  ClassGenerator.builder(clazz,
											strategy)
											.depth(depth)
											.length(length)
											.path(path)
											.topLvl(topLvl)
											.instance();
		reset();
	}
	
	
	
	public List<T> generate() throws ParseException, GenerationException{
		return strategy.generate(this);
	}
	public boolean hasNext(){
		return strategy.hasNext(this);
	}
	void reset() throws ParseException, GenerationException{
		strategy.reset(this);
	}
	List<T> peek(){
		return strategy.peek(this);
	}
	void next()throws ParseException, GenerationException{
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
				.topLvl(topLvl)
				.instance();
	}
	
}
