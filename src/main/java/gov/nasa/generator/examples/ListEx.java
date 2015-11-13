package gov.nasa.generator.examples;

import java.util.List;

public class ListEx {
	
	public List<A> a;
	public ListEx(List<A> a) {
		this.a=a;
	
	}
	
	@Override
	public String toString(){
		String ret ="ListEx: { a: [";
		for (A a2 : a) {
			ret += a2.toString();
		}
		ret += " ] }";
		return ret;
				
	}

}
