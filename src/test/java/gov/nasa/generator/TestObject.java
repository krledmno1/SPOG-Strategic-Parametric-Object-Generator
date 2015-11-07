package gov.nasa.generator;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gov.nasa.generator.examples.B;
import gov.nasa.generator.examples.C;
import gov.nasa.generator.generators.AbstractGenerator;
import gov.nasa.generator.generators.CartesianStrategy;
import gov.nasa.generator.generators.ClassGenerator;
import gov.nasa.generator.generators.GenerationException;
import gov.nasa.generator.generators.GenerationStrategy;

public class TestObject {

	@Test
	public void testSimpleObject() {
		try {
			GenerationStrategy<B> strategy= new CartesianStrategy<B>();
			AbstractGenerator<B> generator = ClassGenerator.builder(B.class, strategy).instance();
			
			int count=0;
			while(generator.hasNext()){
				B object = (B) generator.generate();
//				System.out.println(object);
				count++;
			}
			
			assertEquals("Number of simple objects", 10, count);
		
			} catch (ParseException | GenerationException e) {
		        assertTrue( "Exception!",  false );
			}
	}
	
	
	@Test
	public void testNestedObject() {	
				try {
					GenerationStrategy<C> strategy= new CartesianStrategy<C>();
					AbstractGenerator<C> generator = ClassGenerator.builder(C.class, strategy).instance();
					
					int count=0;
					while(generator.hasNext()){
						C object = (C) generator.generate();
//						System.out.println(object);
						count++;
					}
					
					assertEquals("Number of nested objects", 5, count);

				
			} catch (ParseException | GenerationException e) {
		        assertTrue( "Exception!",  false );
			}
	}

}
