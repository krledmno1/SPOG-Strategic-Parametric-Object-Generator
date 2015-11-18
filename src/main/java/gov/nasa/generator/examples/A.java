package gov.nasa.generator.examples;


public class A {
	
	public Double a;
	
	public A(Double a) {
	 this.a = a;
	}
	
	@Override
	public String toString(){
		return "A:{ a: "+ a +"}";
	}

}
