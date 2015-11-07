package gov.nasa.generator;

import static org.junit.Assert.*;

import java.text.ParseException;


import org.junit.Test;

import gov.nasa.generator.examples.Abstract;
import gov.nasa.generator.examples.CompositeObject;
import gov.nasa.generator.examples.Encapsulator;
import gov.nasa.generator.generators.AbstractClassGenerator;
import gov.nasa.generator.generators.AbstractGenerator;
import gov.nasa.generator.generators.CartesianStrategy;
import gov.nasa.generator.generators.ClassGenerator;
import gov.nasa.generator.generators.GenerationException;
import gov.nasa.generator.generators.GenerationStrategy;

public class TestAbstractClass {

	@Test
	public void testRecursiveAbstractHirearchy() {
		try {
			GenerationStrategy<Abstract> strategy= new CartesianStrategy<Abstract>();
			AbstractGenerator<Abstract> generator = AbstractClassGenerator.builder(Abstract.class, strategy).depth(3).instance();
			
			
			int count=0;
			while(generator.hasNext()){
				Abstract object = (Abstract) generator.generate();
				//System.out.println(object);
				count++;			
			}
			
			assertEquals("Number of generated objects", 2+2*2+2*2*2, count);
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );
		}
	}
	
	
	@Test
	public void testEncapsulatedAbstractHirearchy() {
		try {
			GenerationStrategy<Encapsulator> strategy= new CartesianStrategy<Encapsulator>();
			AbstractGenerator<Encapsulator> generator = ClassGenerator.builder(Encapsulator.class, strategy).instance();
			
			int count=0;
			while(generator.hasNext()){
				Encapsulator object = (Encapsulator) generator.generate();
				//System.out.println(object);
				count++;
			}
			
			assertEquals("Number of generated objects", 1+3*2+3*2*1, count);
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );

		}
	}
	
	
	@Test
	public void testRecursiveEncapsulatedAbstractHirearchy() {
		try {
			CartesianStrategy<CompositeObject> strategy = new CartesianStrategy<CompositeObject>();

			AbstractGenerator<CompositeObject> generator =  
					ClassGenerator.builder(CompositeObject.class, strategy)
					.depth(1)
					.instance();
			
			int count = 0;
			while(generator.hasNext()){
				CompositeObject object = (CompositeObject) generator.generate();
				//System.out.println(object);
				count ++;
			}
			
			assertEquals("Number of generated objects", new Double(Math.pow(13, 2)).intValue(), count);
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );

		}
	}
	
	
	@Test
	public void testRecursiveEncapsulatedAbstractHirearchyDepthTwo() {
		try {
			CartesianStrategy<CompositeObject> strategy = new CartesianStrategy<CompositeObject>();

			AbstractGenerator<CompositeObject> generator =  
					ClassGenerator.builder(CompositeObject.class, strategy)
					.depth(2)
					.instance();
			
			int count = 0;
			while(generator.hasNext()){
				CompositeObject object = (CompositeObject) generator.generate();
				//System.out.println(object);
				count ++;
			}
			
			assertEquals("Number of generated objects", new Double(Math.pow(13, 2)+Math.pow(13, 3)).intValue(), count);
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );

		}
	}

}
