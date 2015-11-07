package gov.nasa.generator.examples;

import gov.nasa.generator.annotations.Generate;

public class Concrete1 extends Abstract {

	@Generate(min = "0", max="1", step = "1")
	public Double a;

	public Concrete1(Double aa) {
		 a = aa;
	}
	
	@Override
	public String toString(){
		return "C1:{ a: "+ a +"}";
	}
}
