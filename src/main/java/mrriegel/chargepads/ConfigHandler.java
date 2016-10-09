package mrriegel.chargepads;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class ConfigHandler {

	public static Configuration config;

	public static boolean RF, TESLA, FE;

	public static void refreshConfig(File file) {
		config = new Configuration(file);
		config.load();

		RF = config.getBoolean("rf", "Energy", true, "Enable RF.");
		TESLA = config.getBoolean("tesla", "Energy", Loader.isModLoaded("tesla"), "Enable Tesla.") && Loader.isModLoaded("tesla");
		FE = config.getBoolean("fe", "Energy", true, "Enable Forge Energy.");

		if (config.hasChanged()) {
			config.save();
		}
	}
}
