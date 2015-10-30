package gov.nasa.generator.generators;

import java.text.ParseException;
import java.util.List;

abstract public class GenerationStrategy<T> {
	
	
	//NumberGenerator
	abstract T generate(NumberGenerator<Number> generator) throws ParseException, GenerationException;
	abstract boolean hasNext(NumberGenerator<Number> abstractGenerator);
	abstract void reset(NumberGenerator<Number> abstractGenerator) throws ParseException, GenerationException;
	abstract T peek(NumberGenerator<Number> abstractGenerator);
	abstract void next(NumberGenerator<Number> abstractGenerator) throws ParseException, GenerationException;
	abstract boolean isLast(NumberGenerator<Number> numberGenerator);
	
	//AbstractClassGenerator
	abstract T generate(AbstractClassGenerator<T> abstractClassGenerator) throws ParseException, GenerationException;
	abstract boolean hasNext(AbstractClassGenerator<T> abstractClassGenerator);
	abstract void reset(AbstractClassGenerator<T> abstractClassGenerator) throws ParseException, GenerationException;
	abstract T peek(AbstractClassGenerator<T> abstractClassGenerator);
	abstract void next(AbstractClassGenerator<T> abstractClassGenerator) throws ParseException, GenerationException;
	abstract boolean isLast(AbstractClassGenerator<T> abstractClassGenerator);
	
	//ClassGenerator
	abstract T generate(ClassGenerator<T> classGenerator) throws ParseException, GenerationException;
	abstract boolean hasNext(ClassGenerator<T> classGenerator);
	abstract void reset(ClassGenerator<T> classGenerator) throws ParseException, GenerationException;
	abstract T peek(ClassGenerator<T> classGenerator);
	abstract void next(ClassGenerator<T> classGenerator) throws ParseException, GenerationException;
	abstract boolean isLast(ClassGenerator<T> classGenerator);
	
	//ListGenerator
	abstract List<T> generate(ListGenerator<T> listGenerator) throws ParseException, GenerationException;
	abstract boolean hasNext(ListGenerator<T> listGenerator);
	abstract void reset(ListGenerator<T> listGenerator) throws ParseException, GenerationException;
	abstract List<T> peek(ListGenerator<T> listGenerator);
	abstract void next(ListGenerator<T> listGenerator) throws ParseException, GenerationException;
	abstract boolean isLast(ListGenerator<T> listGenerator);
	
	
	
	
	
	
}
