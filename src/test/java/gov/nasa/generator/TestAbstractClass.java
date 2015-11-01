package gov.nasa.generator;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


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
			AbstractGenerator<Abstract> generator = AbstractClassGenerator.builder(Abstract.class, strategy).depth(3).path("resources/").instance();
			
			
			List<Abstract> list = new ArrayList<Abstract>();
			while(generator.hasNext()){
				Abstract object = (Abstract) generator.generate();
				//System.out.println(object);
				list.add(object);
			}
			
			assertEquals("Number of generated objects", 2+2*2+2*2*2, list.size());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );
		}
	}
	
	
	@Test
	public void testEncapsulatedAbstractHirearchy() {
		try {
			GenerationStrategy<Encapsulator> strategy= new CartesianStrategy<Encapsulator>();
			AbstractGenerator<Encapsulator> generator = ClassGenerator.builder(Encapsulator.class, strategy).path("resources/").instance();
			
			
			List<Encapsulator> list = new ArrayList<Encapsulator>();
			while(generator.hasNext()){
				Encapsulator object = (Encapsulator) generator.generate();
				//System.out.println(object);
				list.add(object);
			}
			
			assertEquals("Number of generated objects", 1+3*2+5*4*3*2*1, list.size());
			
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
					.path("resources/hierarchy/")
					.depth(3)
					.instance();
			
			List<CompositeObject> list = new ArrayList<CompositeObject>();
			while(generator.hasNext()){
				CompositeObject object = (CompositeObject) generator.generate();
				//System.out.println(object);
				list.add(object);
			}
			
			assertEquals("Number of generated objects", new Double(Math.pow(3, 4)+Math.pow(3, 3)+Math.pow(3, 2)).intValue(), list.size());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );

		}
	}

}
