/**
 * 
 */
package gov.nasa.generator.examples;



/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 
 */
public abstract class AbstractClass{

	//Ignored by generation since it's private (you must set it in the constructor of subclasses)
	private String name;

	
	protected AbstractClass(String name) {
		this.name=name;
	}
	
}
