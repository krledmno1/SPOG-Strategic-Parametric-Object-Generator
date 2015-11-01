/**
 * 
 */
package gov.nasa.generator.examples;


/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public class CompositeObject extends AbstractClassWithRecursiveSubclasses {

	
	protected AbstractClassWithRecursiveSubclasses previous; 
	

	public CompositeObject(AbstractClass trajectoryType, AbstractClassWithRecursiveSubclasses previous) {
		super(trajectoryType);
		this.previous=previous;
	}
	
	

	
	
	@Override
	public String toString(){
		return previous.toString()+ " \n" + trajectoryType.toString();
	}







}
