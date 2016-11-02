package therogue.storehouse.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {
	
	public static void init(File configFile){
		Configuration configuration = new Configuration(configFile);
		try {
			
			//Load the configuration file
			configuration.load();
			
			//Read Values from the configuration file
			//Example: Value = configuration.get("Catagory under", "Property to find", default value, "Comment on the property").getTypethepropertyshouldbe(default value);
			
			
			
		} catch (Exception e) {
			
			//Log the exception
			
		} finally {
			
			//Save the configuration file
			configuration.save();
			
		}
	}
	
}
