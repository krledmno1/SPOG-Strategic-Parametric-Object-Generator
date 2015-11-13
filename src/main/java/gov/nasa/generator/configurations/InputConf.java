/**
 * 
 */
package gov.nasa.generator.configurations;

/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public interface InputConf {

	public String readMin(String key);
	public String readMax(String key);
	public String readStep(String key);	
	public void writeMin(String key, String value);
	public void writeMax(String key, String value);
	public void writeStep(String key, String value);

}
