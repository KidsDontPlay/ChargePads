package mrriegel.chargepads;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class ConfigHandler {

	public static Configuration config;

	public static boolean RF, TESLA, FE, showEnergy;

	public static void refreshConfig(File file) {
		config = new Configuration(file);
		config.load();

		RF = config.getBoolean("rf", "Energy", true, "Enable RF.");
		TESLA = config.getBoolean("tesla", "Energy", Loader.isModLoaded("tesla"), "Enable Tesla.") && Loader.isModLoaded("tesla");
		FE = config.getBoolean("fe", "Energy", true, "Enable Forge Energy.");
		showEnergy = config.getBoolean("showEnergy", config.CATEGORY_CLIENT, !Loader.isModLoaded("theoneprobe"), "Shows energy in machines while sneaking");

		if (config.hasChanged()) {
			config.save();
		}
	}
}
