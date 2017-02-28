package mrriegel.chargepads.proxy;

import mrriegel.chargepads.init.ModBlocks;
import mrriegel.limelib.helper.ParticleHelper;
import mrriegel.limelib.particle.CommonParticle;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		super.preInit(event);
		ModBlocks.initClient();
	}

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void spawnParticle(double x, double y, double z, double dx, double dy, double dz, String name) {
		int color = 0;
		if (name.contains("energy"))
			color = 0xff0000;
		else if (name.contains("health"))
			color = 0x8a2be2;
		ParticleHelper.renderParticle(new CommonParticle(x, y, z, dx, dy, dz).setNoClip(true).setColor(color, 0).setScale(.5f).setMaxAge2(20).setFlouncing(0.009));
	}

}
