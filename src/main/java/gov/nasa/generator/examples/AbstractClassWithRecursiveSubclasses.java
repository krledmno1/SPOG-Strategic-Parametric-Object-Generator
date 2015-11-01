/**
 * 
 */
package gov.nasa.generator.examples;

/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public abstract class AbstractClassWithRecursiveSubclasses {
	
	
	protected AbstractClass trajectoryType;
	
	protected AbstractClassWithRecursiveSubclasses(AbstractClass trajectoryType) {
		this.trajectoryType=trajectoryType;
	}
	
	
	
}
