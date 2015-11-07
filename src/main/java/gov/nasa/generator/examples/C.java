package gov.nasa.generator.examples;

public class C {

	
	public A a;
	public C(A a){
		this.a=a;
	}
	
	@Override
	public String toString(){
		return "C:{ " + a.toString() +"}";
	}

}
