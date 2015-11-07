package gov.nasa.generator.examples;

import gov.nasa.generator.annotations.Generate;

public class B {
	
	@Generate(min = "0", max="2", step = "0.5")
	public Double a;
	@Generate(min = "0", max="1", step = "1")
	public Integer b;

	
	public B(Double aa, Integer bb) {
	 a = aa;
	 b = bb;
	}
	
	@Override
	public String toString(){
		return "B:{ a: "+ a +", b: " + b +"}";
	}

}
