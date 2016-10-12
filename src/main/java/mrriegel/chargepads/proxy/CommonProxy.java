package mrriegel.chargepads.proxy;

import mrriegel.chargepads.ChargePads;
import mrriegel.chargepads.ConfigHandler;
import mrriegel.chargepads.init.ModBlocks;
import mrriegel.limelib.helper.IProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

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

	public void spawnParticle(double x, double y, double z, double dx, double dy, double dz, String name) {
	}

	public static final CreativeTabs tab = new CreativeTabs(ChargePads.MODID) {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(ModBlocks.ENERGYPAD1);
		}

		@Override
		public String getTranslatedTabLabel() {
			return ChargePads.MODNAME;
		}
	};

}
