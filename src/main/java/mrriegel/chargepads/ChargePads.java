package mrriegel.chargepads;

import mrriegel.chargepads.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = ChargePads.MODID, name = ChargePads.MODNAME, version = ChargePads.VERSION, dependencies = "required-after:limelib@[1.5.4,)")
public class ChargePads {
	public static final String MODID = "chargepads";
	public static final String VERSION = "1.0.3";
	public static final String MODNAME = "Charge Pads";

	@Instance(ChargePads.MODID)
	public static ChargePads instance;

	@SidedProxy(clientSide = "mrriegel.chargepads.proxy.ClientProxy", serverSide = "mrriegel.chargepads.proxy.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}
}
