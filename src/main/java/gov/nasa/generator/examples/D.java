package gov.nasa.generator.examples;

import gov.nasa.generator.annotations.Generate;

public class D {

	public Double a;
	@Generate(min = "1", max="3", step = "1")
	public Integer b;

	
	public D( Integer bb) {
	 a = 4.0;
	 b = bb;
	}
	
	@Override
	public String toString(){
		return "D:{ a: "+ a +", b: " + b +"}";
	}
	
}
