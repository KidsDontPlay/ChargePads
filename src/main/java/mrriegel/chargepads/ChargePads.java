package mrriegel.chargepads;

import mrriegel.limelib.helper.IProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod(modid = ChargePads.MODID, name = ChargePads.MODNAME, version = ChargePads.VERSION, dependencies = "required-after:LimeLib@[1.0.1,)")
public class ChargePads {
	public static final String MODID = "chargepads";
	public static final String VERSION = "1.0.0";
	public static final String MODNAME = "Charge Pads";

	@Instance(ChargePads.MODID)
	public static ChargePads instance;

	@SidedProxy(clientSide = "mrriegel.chargepads.proxy.ClientProxy", serverSide = "mrriegel.chargepads.proxy.CommonProxy")
	public static IProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		proxy.preInit(event);
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		proxy.init(event);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.postInit(event);
	}

	@SubscribeEvent
	public void jump(LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer && !event.getEntityLiving().worldObj.isRemote) {
//			System.out.println("TEs:");
			for (TileEntity t : event.getEntityLiving().worldObj.tickableTileEntities) {
//				System.out.println("   " + t.toString());
			}
			//			System.out.println(event.getEntityLiving().worldObj.getTileEntity(new BlockPos(event.getEntityLiving()).down()));
		}
	}
}
