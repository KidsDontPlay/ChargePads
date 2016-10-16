package mrriegel.chargepads;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Loader;

public class ConfigHandler {

	public static Configuration config;

	public static boolean RF, TESLA, FE, showEnergy, healAnimals, healMonsters;

	public static void refreshConfig(File file) {
		config = new Configuration(file);
		config.load();

		RF = config.getBoolean("rf", "Energy", true, "Enable RF.");
		TESLA = config.getBoolean("tesla", "Energy", Loader.isModLoaded("tesla"), "Enable Tesla.") && Loader.isModLoaded("tesla");
		FE = config.getBoolean("fe", "Energy", true, "Enable Forge Energy.");
		showEnergy = config.getBoolean("showEnergy", Configuration.CATEGORY_CLIENT, !Loader.isModLoaded("theoneprobe") || true, "Shows energy in machines while sneaking.");
		healAnimals = config.getBoolean("healAnimals", Configuration.CATEGORY_GENERAL, true, "Heal animals too.");
		healMonsters = config.getBoolean("healMonsters", Configuration.CATEGORY_GENERAL, false, "Heal monsters too.");

		if (config.hasChanged()) {
			config.save();
		}
	}
}
