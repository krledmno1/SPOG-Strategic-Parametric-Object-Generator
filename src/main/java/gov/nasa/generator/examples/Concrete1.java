package gov.nasa.generator.examples;

public class Concrete1 extends Abstract {

	public Double a;

	public Concrete1(Double aa) {
		 a = aa;
	}
	
	@Override
	public String toString(){
		return "C1:{ a: "+ a +"}";
	}
}
