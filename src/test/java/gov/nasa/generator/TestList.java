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
import gov.nasa.generator.generators.GenerationException;
import gov.nasa.generator.generators.GenerationStrategy;
import gov.nasa.generator.generators.ListGenerator;

public class TestList {

	@Test
	public void testSimpleObjectList() {
		try {
			GenerationStrategy<B> strategy= new CartesianStrategy<B>();
			AbstractGenerator<B> generator = ListGenerator.builder(B.class, strategy).length(3).instance();
			
			int count=0;
			while(generator.hasNext()){
				List<B> object = (List<B>) generator.generate();
//				System.out.println(object);
				count++;
			}
			
			assertEquals("Number of lists with simple objects", 5*2+10*10+10*10*10, count);

			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );

		}
		
	}
	
	
	
	@Test
	public void testNestedObjectList() {
		try {
			GenerationStrategy<C> strategy= new CartesianStrategy<C>();
			AbstractGenerator<C> generator = ListGenerator.builder(C.class, strategy).length(3).instance();
			
			int count=0;
			while(generator.hasNext()){
				List<C> object = (List<C>) generator.generate();
//				System.out.println(object);
				count++;
			}
			
			assertEquals("Number of lists with simple objects", 5+5*5+5*5*5, count);

			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
	        assertTrue( "Exception!",  false );

		}
		
	}

}
