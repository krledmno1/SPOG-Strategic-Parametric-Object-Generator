package gov.nasa.generator;

import static org.junit.Assert.*;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gov.nasa.generator.examples.B;
import gov.nasa.generator.generators.AbstractGenerator;
import gov.nasa.generator.generators.CartesianStrategy;
import gov.nasa.generator.generators.ClassGenerator;
import gov.nasa.generator.generators.GenerationException;
import gov.nasa.generator.generators.GenerationStrategy;

public class TestList {

	@Test
	public void testSimpleObject() {
		try {
			GenerationStrategy<B> strategy= new CartesianStrategy<B>();
			AbstractGenerator<B> generator = ClassGenerator.builder(B.class, strategy).path("resources/").instance();
			
			List<B> list = new ArrayList<B>();
			while(generator.hasNext()){
				B object = (B) generator.generate();
				System.out.println(object);
				list.add(object);
			}
			
			assertEquals("Number of simple object", 10, list.size());
		
			} catch (ParseException | GenerationException e) {
				e.printStackTrace();
			}
	}

}
