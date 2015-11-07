package gov.nasa.generator.examples;

import gov.nasa.generator.annotations.Generate;

public class Concrete2 extends Abstract {

	@Generate(min = "0", max="1", step = "1")
	public Integer a;
	
	public Abstract b;

	public Concrete2(Integer aa, Abstract bb) {
		 a = aa;
		 b = bb;
	}
	
	@Override
	public String toString(){
		return "C2:{ a: "+ a +" { b: "+ b.toString() +" }}";
	}
}
