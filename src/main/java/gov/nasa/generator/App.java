package gov.nasa.generator;

import java.text.ParseException;

import gov.nasa.generator.examples.Abstract;
import gov.nasa.generator.examples.B;
import gov.nasa.generator.examples.C;
import gov.nasa.generator.generators.AbstractClassGenerator;
import gov.nasa.generator.generators.AbstractGenerator;
import gov.nasa.generator.generators.CartesianStrategy;
import gov.nasa.generator.generators.ClassGenerator;
import gov.nasa.generator.generators.GenerationException;
import gov.nasa.generator.generators.GenerationStrategy;
import gov.nasa.generator.generators.ListGenerator;
import gov.nasa.generator.generators.NumberGenerator;
import gov.nasa.generator.generators.NumberGenerator.DoubleWrapper;
import gov.nasa.generator.generators.NumberGenerator.IntWrapper;

/**
 * Hello world!
 *
 */
public class App 
{
	private static void integerExample(){
		try {			
	    		IntWrapper min = new IntWrapper (1);
	    		IntWrapper max = new IntWrapper(10);
	    		IntWrapper step = new IntWrapper (4);
	    		
	    		GenerationStrategy<Integer> strategy= new CartesianStrategy<Integer>();
	    		
	    		AbstractGenerator<Integer> generator = NumberGenerator.builder(
	    														Integer.class, strategy,
																min, max, step)
																.instance();
	    		
	    		while(generator.hasNext())
	    			System.out.println(generator.generate());
    		
    		} catch (ParseException | GenerationException e) {
    			e.printStackTrace();
    		}
	}
	
	private static void doubleExample(){
		try {
    		
	    		DoubleWrapper min = new DoubleWrapper(0.0);
	    		DoubleWrapper max = new DoubleWrapper(2.0);
	    		DoubleWrapper step = new DoubleWrapper(0.5);
	    		
	    		GenerationStrategy<Double> strategy= new CartesianStrategy<Double>();
	    		
	    		AbstractGenerator<Double> generator = NumberGenerator.builder(
															Double.class, strategy,
															min, max, step)
	    													.instance();
    		
    		while(generator.hasNext())
    			System.out.println(generator.generate());
    		
    		} catch (ParseException | GenerationException e) {
    			e.printStackTrace();
    		}
	}
	
	
	private static void simpleObjectExample(){
		try {
				GenerationStrategy<B> strategy= new CartesianStrategy<B>();
				AbstractGenerator<B> generator = ClassGenerator.builder(B.class, strategy).instance();
				
				while(generator.hasNext())
					System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}

	}

	private static void nestedObjectExample(){
		try {
				GenerationStrategy<C> strategy= new CartesianStrategy<C>();
				AbstractGenerator<C> generator = ClassGenerator.builder(C.class, strategy).instance();
				
				while(generator.hasNext())
					System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private static void simpleobjectListExample(){
		try {
			GenerationStrategy<B> strategy= new CartesianStrategy<B>();
			AbstractGenerator<B> generator = ListGenerator.builder(B.class, strategy).length(3).instance();
			
			while(generator.hasNext())
				System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}

	
	
	private static void nestedObjectListExample(){
		try {
			GenerationStrategy<C> strategy= new CartesianStrategy<C>();
			AbstractGenerator<C> generator = ListGenerator.builder(C.class, strategy).length(3).instance();
			
			while(generator.hasNext())
				System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void abstractClassListExample(){
		try {
			GenerationStrategy<Abstract> strategy= new CartesianStrategy<Abstract>();
			AbstractGenerator<Abstract> generator = AbstractClassGenerator.builder(Abstract.class, strategy).instance();
			
			while(generator.hasNext())
				System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}


	
    public static void main( String[] args )
    {
    	
//    	integerExample();
//    	doubleExample();
//    	simpleObjectExample();
//    	nestedObjectExample();
//    	simpleobjectListExample();
//    	nestedObjectListExample();
    	abstractClassListExample();
    		
    }
}
