/**
 * 
 */
package gov.nasa.generator.examples;


/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public class Encapsulator extends AbstractClassWithRecursiveSubclasses {

	public Encapsulator(AbstractClass trajectoryType) {
		super(trajectoryType);
	}


	
	
	@Override
	public String toString(){
		return trajectoryType.toString();
	}

}
