package gov.nasa.generator.examples;

public class B {
	
	public Double a;
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
