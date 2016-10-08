package mrriegel.chargepads.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import mrriegel.chargepads.ConfigHandler;
import mrriegel.chargepads.init.ModBlocks;
import mrriegel.limelib.helper.IProxy;

public class CommonProxy implements IProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		ConfigHandler.refreshConfig(event.getSuggestedConfigurationFile());
		ModBlocks.init();
	}

	@Override
	public void init(FMLInitializationEvent event) {

	}

	@Override
	public void postInit(FMLPostInitializationEvent event) {

	}

}
