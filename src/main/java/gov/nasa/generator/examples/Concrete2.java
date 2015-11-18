package gov.nasa.generator.examples;


public class Concrete2 extends Abstract {

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
