package gov.nasa.generator.generators;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import gov.nasa.generator.generators.NumberGenerator.TypeWrapper;


public class NumberGenerator <T extends Number> extends AbstractGenerator<T> {

	//Builder + static factory method
	public static class NumberBuilder<T extends Number> extends AbstractGenerator.Build<T>{

		//required params
		private TypeWrapper<T> min;
		private TypeWrapper<T> max;
		private TypeWrapper<T> step;

		
		public NumberBuilder(Class<T> c, GenerationStrategy<T> s, TypeWrapper<T> min, TypeWrapper<T> max, TypeWrapper<T> step) {
			super(c, s);
			this.min=min;
			this.max=max;
			this.step=step;
		}
		

		@Override
		public AbstractGenerator<T> instance() throws ParseException, GenerationException {
			return new NumberGenerator<T>(this);
		}
		
	}
	public static <T extends Number> NumberBuilder<T> builder(Class<?> c, GenerationStrategy<?> s, TypeWrapper<T> min, TypeWrapper<T> max, TypeWrapper<T> step){
		 return new NumberGenerator.NumberBuilder<T>((Class<T>)c, (GenerationStrategy<T>)s, min, max,step);
	}
	
	//TypeWrapper (to aextend to support more types)
	public static interface TypeWrapper<K extends Number> {

		public TypeWrapper<K> plus(TypeWrapper<K> that);
		public TypeWrapper<K> minus(TypeWrapper<K> that);
		public TypeWrapper<K> mult(TypeWrapper<K> that);
		public TypeWrapper<K> div(TypeWrapper<K> that);

		public boolean lt(TypeWrapper<K> that);
		public boolean le(TypeWrapper<K> that);
		public boolean gt(TypeWrapper<K> that);
		public boolean ge(TypeWrapper<K> that);
		public boolean eq(TypeWrapper<K> that);
		public boolean ne(TypeWrapper<K> that);
		public TypeWrapper<K> cloneValue();
		public K getValue();
		public TypeWrapper<K> mult(IntWrapper intWrapper);
		
		public static boolean supports(String type){
			Set<String> supported = new HashSet<String>();
			supported.add(Integer.class.getSimpleName().toUpperCase());
			supported.add(Double.class.getSimpleName().toUpperCase());
			
			return supported.contains(type.toUpperCase());
		}
		public static <K extends Number> TypeWrapper<K> extractValue(String value, String type) throws ParseException{
			type = type.toUpperCase();
			switch(type){
				case "DOUBLE":{
					return (TypeWrapper<K>) DoubleWrapper.extractValue(value);
				}
				case "INTEGER":{
					return (TypeWrapper<K>) IntWrapper.extractValue(value);
				}
				default:{
					throw new ParseException("Type " + type + " not supported", 0);
				}
			}
		}
		
	}
	public static class IntWrapper implements TypeWrapper<Integer>{
		private Integer value;
		public IntWrapper(Integer v) {
			value=v;
		}
		public static IntWrapper extractValue(String value) throws ParseException  {
			return new IntWrapper(Integer.parseInt(value));
		}
		@Override
		public Integer getValue() {
			return new Integer(value);
		}
		@Override
		public TypeWrapper<Integer> plus(TypeWrapper<Integer> that) {
			return new IntWrapper(value+that.getValue());
		}
		@Override
		public TypeWrapper<Integer> minus(TypeWrapper<Integer> that) {
			return new IntWrapper(value-that.getValue());
		}
		@Override
		public TypeWrapper<Integer> mult(TypeWrapper<Integer> that) {
			return new IntWrapper(value*that.getValue());
		}
		public TypeWrapper<Integer> mult(IntWrapper that) {
			return new IntWrapper(value*that.getValue());
		}
		@Override
		public TypeWrapper<Integer> div(TypeWrapper<Integer> that) {
			return new IntWrapper(value/that.getValue());
		}
		@Override
		public boolean lt(TypeWrapper<Integer> that) {
			return value<that.getValue();
		}
		@Override
		public boolean le(TypeWrapper<Integer> that) {
			return value<=that.getValue();
		}
		@Override
		public boolean gt(TypeWrapper<Integer> that) {
			return value>that.getValue();
		}
		@Override
		public boolean ge(TypeWrapper<Integer> that) {
			return value>=that.getValue();
		}
		@Override
		public boolean eq(TypeWrapper<Integer> that) {
			return value.equals(that.getValue());
		}
		@Override
		public boolean ne(TypeWrapper<Integer> that) {
			return !value.equals(that.getValue());
		}
		@Override
		public TypeWrapper<Integer> cloneValue() {
			return new IntWrapper(value);
		}
		
		
	}
	public static class DoubleWrapper implements TypeWrapper<Double>{
		
		private static double DELTA=0.000001;
		private Double value;
		public DoubleWrapper(Double v) {
			value=v;
		}
		public static DoubleWrapper extractValue(String value) throws ParseException  {
			return new DoubleWrapper(Double.parseDouble(value));
		}
		@Override
		public Double getValue() {
			return new Double(value);
		}
		@Override
		public TypeWrapper<Double> plus(TypeWrapper<Double> that) {
			return new DoubleWrapper(value+that.getValue());
		}
		@Override
		public TypeWrapper<Double> minus(TypeWrapper<Double> that) {
			return new DoubleWrapper(value-that.getValue());
		}
		@Override
		public TypeWrapper<Double> mult(TypeWrapper<Double> that) {
			return new DoubleWrapper(value*that.getValue());
		}
		public TypeWrapper<Double> mult(IntWrapper that) {
			return new DoubleWrapper(value*that.getValue());
		}
		@Override
		public TypeWrapper<Double> div(TypeWrapper<Double> that) {
			return new DoubleWrapper(value/that.getValue());
		}
		@Override
		public boolean lt(TypeWrapper<Double> that) {
			return value<that.getValue();
		}
		@Override
		public boolean le(TypeWrapper<Double> that) {
			return value<=that.getValue();
		}
		@Override
		public boolean gt(TypeWrapper<Double> that) {
			return value>that.getValue();
		}
		@Override
		public boolean ge(TypeWrapper<Double> that) {
			return value>=that.getValue();
		}
		@Override
		public boolean eq(TypeWrapper<Double> that) {
			return Math.abs(value-that.getValue())<DELTA;
		}
		@Override
		public boolean ne(TypeWrapper<Double> that) {
			return !eq(that);
		}
		@Override
		public TypeWrapper<Double> cloneValue() {
			return new DoubleWrapper(value);
		}
		
	}
	
	
	TypeWrapper<T> begin;
	TypeWrapper<T> curr;
	TypeWrapper<T> min;
	TypeWrapper<T> max;
	TypeWrapper<T> step;
	
	
	protected NumberGenerator(NumberBuilder<T> b) throws ParseException, GenerationException {
		super(b);
		min=b.min;
		max=b.max;
		step=b.step;
		Double bound = Double.parseDouble(max.minus(min).div(step).getValue().toString());
		int intbound = bound.intValue();
		
		if(intbound>0){
		    int randomNum = ThreadLocalRandom.current().nextInt(bound.intValue());
			begin=min.plus(step.mult(new IntWrapper(randomNum)));
		}
		else{
			begin = min.cloneValue();
		}
		reset();
	}
	
	@Override
	public T generate() throws ParseException, GenerationException{
		return strategy.generate((NumberGenerator<Number>)this);
	}
	@Override
	public boolean hasNext(){
		return strategy.hasNext((NumberGenerator<Number>)this);
	}
	@Override
	void reset() throws ParseException, GenerationException{
		strategy.reset((NumberGenerator<Number>) this);
	}
	@Override
	T peek(){
		return strategy.peek((NumberGenerator<Number>)this);
	}
	@Override
	void next() throws ParseException, GenerationException{
		strategy.next((NumberGenerator<Number>)this);
	}
	@Override
	boolean isLast() {
		return strategy.isLast((NumberGenerator<Number>)this);
	}
	@Override
	protected AbstractGenerator<T> cloneGenerator() throws ParseException, GenerationException {
		return builder(clazz, strategy,min,max,step)
				.depth(depth)
				.length(length)
				.topLvl(topLvl)
				.instance();
	}

	

}
