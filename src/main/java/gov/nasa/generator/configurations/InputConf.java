/**
 * 
 */
package gov.nasa.generator.configurations;

import gov.nasa.generator.generators.GenerationException;

/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public interface InputConf {

	public boolean exists(String key);
	public String readMin(String key);
	public String readMax(String key);
	public String readStep(String key);	
	public void writeMin(String key, String value);
	public void writeMax(String key, String value);
	public void writeStep(String key, String value);

}
