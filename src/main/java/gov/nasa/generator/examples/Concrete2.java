package gov.nasa.generator.examples;

public class Concrete2 extends Abstract {

	public Integer a;

	public Concrete2(Integer aa) {
		 a = aa;
	}
	
	@Override
	public String toString(){
		return "C2:{ a: "+ a +"}";
	}
}
