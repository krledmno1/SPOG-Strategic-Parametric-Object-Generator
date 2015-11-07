/**
 * 
 */
package gov.nasa.generator.examples;

import java.util.ArrayList;
import java.util.List;

import gov.nasa.generator.annotations.Generate;


/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public class Con1 extends AbstractClass {

	
	@Generate(min = "0", max="4", step = "1")
	private Double a1;
	
	@Generate(min = "0", max="3", step = "1")
	private Double a2;
	
	@Generate(min = "0", max="2", step = "1")
	private Double a3;
	
	@Generate(min = "0", max="1", step = "1")
	private Double a4;

	@Generate(min = "0", max="0", step = "1")
	private Double a5;

	public Con1(Double a1,
				 Double a2,
				 Double a3,
				 Double a4,
				 Double a5) {
		super("Con1");
		
		this.a1 = a1;
		this.a2 = a2;
		this.a3 = a3;
		this.a4 = a4;
		this.a5 = a5;

	}

	
	
	@Override
	public String toString(){
		return "Con1: a1=" + a1 + 
					 " a2=" + a2 + 
					 " a3=" + a3 + 
					 " a4=" + a4 + 
					 " a5=" + a5;
	}

}
