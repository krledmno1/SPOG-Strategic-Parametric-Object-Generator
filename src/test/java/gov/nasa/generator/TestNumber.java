package gov.nasa.generator;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import gov.nasa.generator.generators.AbstractGenerator;
import gov.nasa.generator.generators.CartesianStrategy;
import gov.nasa.generator.generators.GenerationException;
import gov.nasa.generator.generators.GenerationStrategy;
import gov.nasa.generator.generators.NumberGenerator;
import gov.nasa.generator.generators.NumberGenerator.DoubleWrapper;
import gov.nasa.generator.generators.NumberGenerator.IntWrapper;
import junit.framework.TestCase;

public class TestNumber extends TestCase {
	
	
    public void testIntegerSize()
    {
    	try{
    		
    	IntWrapper min = new IntWrapper (1);
		IntWrapper max = new IntWrapper(10);
		IntWrapper step = new IntWrapper (4);
		
		GenerationStrategy<Integer> strategy= new CartesianStrategy<Integer>();
		
		AbstractGenerator<Integer> generator = NumberGenerator.builder(
														Integer.class, strategy,
														min, max, step)
														.instance();
		
		List<Integer> list = new ArrayList<Integer>();
		while(generator.hasNext()){
			Integer obj = (Integer) generator.generate();
			System.out.println(obj);
			list.add(obj);
		}
		
		assertEquals("Number of generatod objects", 3, list.size());
	
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );
	
		}
    	
    }
    
    
    public void testDoubleSize()
    {
    	try{
    		
    		DoubleWrapper min = new DoubleWrapper(0.0);
    		DoubleWrapper max = new DoubleWrapper(2.0);
    		DoubleWrapper step = new DoubleWrapper(0.5);
		
		GenerationStrategy<Double> strategy= new CartesianStrategy<Double>();
		
		AbstractGenerator<Double> generator = NumberGenerator.builder(
														Double.class, strategy,
														min, max, step).path("resources/")
														.instance();
		
		List<Double> list = new ArrayList<Double>();
		while(generator.hasNext()){
			Double obj = (Double) generator.generate();
			System.out.println(obj);
			list.add(obj);
		}
		
		assertEquals("Number of generatod objects", 5, list.size());
	
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );
	
		}
    	
    }
    

}
