package gov.nasa.generator.generators;

import java.text.ParseException;

public abstract class AbstractGenerator<T> {

	//required
	protected Class<T> clazz;
	protected GenerationStrategy<T> strategy;
	
	//optional with default values
	protected String path;
	protected int depth; 
	protected int length;
	
	//internal use only
	boolean hasNext;

	
	public abstract static class Build<T>{
		
		//required
		private Class<T> clazz;
		private GenerationStrategy<T> strategy;
		
		//optional with default values
		private String path="";
		private int depth=2; 
		private int length=2;
		
		public Build(Class<T> c, GenerationStrategy<T> s){
			clazz=c;
			strategy=s;
		}
		
		public Build<T> path(String p){
			path=p;
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
		
		abstract public AbstractGenerator<T> instance() throws ParseException, GenerationException;
		
	}
	
	protected AbstractGenerator(Build<T> b){
		clazz=b.clazz;
		strategy=b.strategy;
		path=b.path;
		depth=b.depth;
		length=b.length;
	}
	
	
	public abstract Object generate() throws ParseException, GenerationException;

	public abstract boolean hasNext();

	abstract void reset() throws ParseException, GenerationException;
		
	abstract Object peek();
	
	abstract void next() throws ParseException, GenerationException;
	
	abstract boolean isLast();
	
	protected abstract AbstractGenerator<T> cloneGenerator() throws ParseException, GenerationException;

	
}
