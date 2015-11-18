/**
 * 
 */
package gov.nasa.generator.configurations;

import gov.nasa.generator.generators.GenerationException;

/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public class DefaultInput implements InputConf {

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.InputConf#exists(java.lang.String)
	 */
	@Override
	public boolean exists(String key) {
		return true;
	}

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.InputConf#readMin(java.lang.String)
	 */
	@Override
	public String readMin(String key) {
		return "1";
	}

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.InputConf#readMax(java.lang.String)
	 */
	@Override
	public String readMax(String key) {
		return "1";
	}

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.InputConf#readStep(java.lang.String)
	 */
	@Override
	public String readStep(String key) {
		return "1";
	}

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.InputConf#writeMin(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeMin(String key, String value) {
		System.out.println(key+".min="+value);
	}

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.InputConf#writeMax(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeMax(String key, String value) {
		System.out.println(key+".max="+value);
	}

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.InputConf#writeStep(java.lang.String, java.lang.String)
	 */
	@Override
	public void writeStep(String key, String value) {
		System.out.println(key+".step="+value);
	}

}
