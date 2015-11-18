/**
 * 
 */
package gov.nasa.generator.configurations;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.util.Properties;

import org.apache.commons.configuration.*;

/**
 * @author Srdan Krstic (srdan.krstic@polimi.it)
 *
 */
public class CommonsInput implements InputConf {

	private PropertiesConfiguration config;
	
	public CommonsInput(String path) throws ParseException {
		
		try {
			config = new PropertiesConfiguration(path);
			config.setAutoSave(true);
			
		} catch (ConfigurationException e) {
			try {
				
				Properties prop = new Properties();
				
				OutputStream output = new FileOutputStream(path);
				prop.store(output, null);
				config = new PropertiesConfiguration(path);
				config.setAutoSave(true);
				
			} catch (ConfigurationException | IOException e1) {
				throw new ParseException("File does not exist and cannot be created: " + path, 0);
			}
		}

		
	}
	
	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.Configuration#readMin(java.lang.String)
	 */
	@Override
	public String readMin(String key) {
		return config.getString(key+".min");
	}

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.Configuration#readMax(java.lang.String)
	 */
	@Override
	public String readMax(String key) {
		return config.getString(key+".max");
	}

	/* (non-Javadoc)
	 * @see gov.nasa.generator.configurations.Configuration#readStep(java.lang.String)
	 */
	@Override
	public String readStep(String key) {
		return config.getString(key+".step");
	}

	@Override
	public void writeMin(String key, String value) {
		config.setProperty(key+".min", value);
	}

	@Override
	public void writeMax(String key, String value) {
		config.setProperty(key+".max", value);
	}

	@Override
	public void writeStep(String key, String value) {
		config.setProperty(key+".step", value);
	}

	@Override
	public boolean exists(String key) {
		return config.containsKey(key+".min");
	}

	

}
