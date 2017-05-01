package mrriegel.chargepads;

import java.io.File;

import mrriegel.limelib.LimeLib;
import net.minecraftforge.common.config.Configuration;

public class ConfigHandler {

	public static Configuration config;

	public static boolean RF, TESLA, FE, healAnimals, healMonsters;

	public static void refreshConfig(File file) {
		config = new Configuration(file);
		config.load();

		RF = config.getBoolean("rf", "Energy", true, "Enable RF.");
		TESLA = config.getBoolean("tesla", "Energy", LimeLib.teslaLoaded, "Enable Tesla.") && LimeLib.teslaLoaded;
		FE = config.getBoolean("fe", "Energy", true, "Enable Forge Energy.");
		healAnimals = config.getBoolean("healAnimals", Configuration.CATEGORY_GENERAL, true, "Heal animals too.");
		healMonsters = config.getBoolean("healMonsters", Configuration.CATEGORY_GENERAL, false, "Heal monsters too.");

		if (config.hasChanged()) {
			config.save();
		}
	}
}
