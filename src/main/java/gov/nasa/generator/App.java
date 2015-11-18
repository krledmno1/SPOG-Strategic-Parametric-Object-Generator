package gov.nasa.generator;

import java.text.ParseException;


import gov.nasa.generator.configurations.*;
import gov.nasa.generator.examples.*;
import gov.nasa.generator.generators.*;
import gov.nasa.generator.generators.NumberGenerator.*;

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
				GenerationStrategy<A> strategy= new CartesianStrategy<A>();
				AbstractGenerator<A> generator = 
						ClassGenerator.builder(A.class, 
								strategy)
						.input(new CommonsInput("resources/params.properties"))
						.instance();
				
				while(generator.hasNext())
					System.out.println(generator.generate());
			
				
				
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}

	}
	
	
	private static void simpleTwoFieldObjectExample(){
		try {
				GenerationStrategy<B> strategy= new CartesianStrategy<B>();
				AbstractGenerator<B> generator = 
						ClassGenerator.builder(B.class, 
								strategy)
						.input(new CommonsInput("resources/params.properties"))
						.instance();
				
				while(generator.hasNext())
					System.out.println(generator.generate());
				
				
				generator.writeConfigTemplate(new CommonsInput("bla.properties"));
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}

	}
	
	private static void simpleIgnoreFieldObjectExample(){
		try {
				GenerationStrategy<D> strategy= new CartesianStrategy<D>();
				AbstractGenerator<D> generator = 
						ClassGenerator.builder(D.class,
								strategy)
						.input(new CommonsInput("resources/params.properties"))
						.instance();
				
				while(generator.hasNext())
					System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}

	}

	private static void nestedObjectExample(){
		try {
				GenerationStrategy<C> strategy= new CartesianStrategy<C>();
				AbstractGenerator<C> generator = 
						ClassGenerator.builder(C.class, 
								strategy)
						.input(new CommonsInput("resources/params.properties"))
						.instance();
				
				while(generator.hasNext())
					System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
		
	}
	
	
	
	private static void simpleObjectListExample(){
		try {
			GenerationStrategy<B> strategy= new CartesianStrategy<B>();
			AbstractGenerator<B> generator = 
					ListGenerator.builder(B.class, 
							strategy)
					.input(new CommonsInput("resources/params.properties"))
					.length(3).instance();
			
			while(generator.hasNext())
				System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}

	
	
	private static void nestedObjectListExample(){
		try {
			GenerationStrategy<C> strategy= new CartesianStrategy<C>();
			AbstractGenerator<C> generator = 
					ListGenerator.builder(C.class, 
							strategy)
					.input(new CommonsInput("resources/params.properties"))
					.length(3).instance();
			
			while(generator.hasNext())
				System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void abstractClassExample(){
		try {
			GenerationStrategy<AbstractClass> strategy= new CartesianStrategy<AbstractClass>();
			AbstractGenerator<AbstractClass> generator = 
					AbstractClassGenerator.builder(AbstractClass.class, 
							strategy)
					.input(new CommonsInput("resources/params.properties"))
					.depth(3).instance();
			
			while(generator.hasNext())
				System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}
	
	private static void abstractRecursiveClassExample(){
		try {
			GenerationStrategy<Abstract> strategy= new CartesianStrategy<Abstract>();
			AbstractGenerator<Abstract> generator = 
					AbstractClassGenerator.builder(Abstract.class,
							strategy)
					.input(new CommonsInput("resources/params.properties"))
					.depth(3).instance();
			
			while(generator.hasNext())
				System.out.println(generator.generate());
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}
	
	private static void abstractClassEncapsulatedExamlple(){
		try {
			
			CartesianStrategy<Encapsulator> strategy = new CartesianStrategy<Encapsulator>();

			AbstractGenerator<Encapsulator> generator =  
					ClassGenerator.builder(Encapsulator.class, 
							strategy)
					.input(new CommonsInput("resources/params.properties"))
					.instance();
			
			while(generator.hasNext()){
				System.out.println(generator.generate());
			}

		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}
	
	
	private static void abstractRecursiveClassEncapsulatedExamlple(){
		try {
			
			CartesianStrategy<CompositeObject> strategy = new CartesianStrategy<CompositeObject>();

			AbstractGenerator<CompositeObject> generator =  
					ClassGenerator.builder(CompositeObject.class, 
							strategy)
					.input(new CommonsInput("resources/params.properties"))
					.depth(2)
					.instance();
			
			int i = 1;
			while(generator.hasNext()){
				System.out.println(i);
				System.out.println(generator.generate());
				i++;
			}
			

		} catch (ParseException | GenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
	private static void simpleObjectWithList(){
		try {
			
			CartesianStrategy<ListEx> strategy = new CartesianStrategy<ListEx>();

			AbstractGenerator<ListEx> generator =  
					ClassGenerator.builder(ListEx.class, 
							strategy)
					.input(new CommonsInput("resources/params.properties"))
					.instance();
			
			int i = 1;
			while(generator.hasNext()){
				System.out.println(i);
				System.out.println(generator.generate());
				i++;
			}

		} catch (ParseException | GenerationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void generateTemplate() {
		GenerationStrategy<A> strategy= new CartesianStrategy<A>();
		try {
			AbstractGenerator<A> generator = 
					ClassGenerator.builder(A.class, 
							strategy)
					.instance();
			
			generator.writeConfigTemplate();
			
		} catch (ParseException | GenerationException e) {
			e.printStackTrace();
		}
	}
	
    public static void main( String[] args )
    {
    	
//    	integerExample();
//    	doubleExample();
//    	simpleObjectExample();
//    	simpleTwoFieldObjectExample();
//    	simpleIgnoreFieldObjectExample();
//    	nestedObjectExample();
//    	simpleObjectListExample();
//    	nestedObjectListExample();
//    	abstractClassExample();
//   	abstractRecursiveClassExample();
//    	abstractClassEncapsulatedExamlple();
//    	abstractRecursiveClassEncapsulatedExamlple();
//    	simpleObjectWithList();
//    	generateTemplate();
    	
    }

	
}
