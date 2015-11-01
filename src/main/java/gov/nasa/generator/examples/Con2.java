/**
 * 
 */
package gov.nasa.generator.examples;

/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public class Con2 extends AbstractClass {

	private Double a1;
	private Double a2;

	public Con2(Double a1, Double a2) {
		super("Con2");
		this.a1=a1;
		this.a2=a2;
	}

	
	
	@Override
	public String toString(){
		return "Con2: a1=" + a1 + " a2:" + a2;
	}

}
