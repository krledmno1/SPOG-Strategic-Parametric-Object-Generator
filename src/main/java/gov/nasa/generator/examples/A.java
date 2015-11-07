package gov.nasa.generator.examples;

import gov.nasa.generator.annotations.Generate;

public class A {
	
	@Generate(min = "0", max="2", step = "0.5")
	public Double a;

	
	public A(Double aa) {
	 a = aa;
	}
	
	@Override
	public String toString(){
		return "A:{ a: "+ a +"}";
	}

}
