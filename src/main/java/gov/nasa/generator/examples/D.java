package gov.nasa.generator.examples;


public class D {

	public Double a;
	public Integer b;

	
	public D(Integer bb) {
	 a = 4.0;
	 b = bb;
	}
	
	@Override
	public String toString(){
		return "D:{ a: "+ a +", b: " + b +"}";
	}
	
}
