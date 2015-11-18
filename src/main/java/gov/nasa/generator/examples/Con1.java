/**
 * 
 */
package gov.nasa.generator.examples;

import java.util.ArrayList;
import java.util.List;



/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public class Con1 extends AbstractClass {

	
	
	private Double a1;
	
	private Double a2;

	private Double a3;

	public Con1( Double a3,
				 Double a4,
				 Double a5) {
		super("Con1");
		
		this.a1 = a3;
		this.a2 = a4;
		this.a3 = a5;

	}

	
	
	@Override
	public String toString(){
		return "Con1: a1=" + a1 + 
					 " a2=" + a2 + 
					 " a3=" + a3;
	}

}
