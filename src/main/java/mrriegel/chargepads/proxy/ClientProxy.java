package mrriegel.chargepads.proxy;

import mrriegel.chargepads.ConfigHandler;
import mrriegel.chargepads.init.ModBlocks;
import mrriegel.limelib.gui.GuiDrawer;
import mrriegel.limelib.helper.ParticleHelper;
import mrriegel.limelib.particle.CommonParticle;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import cofh.api.energy.IEnergyHandler;

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

	@SubscribeEvent
	public void overLay(Post event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.objectMouseOver.getBlockPos() == null || mc.theWorld.getTileEntity(mc.objectMouseOver.getBlockPos()) == null)
			return;
		TileEntity tile = mc.theWorld.getTileEntity(mc.objectMouseOver.getBlockPos());
		if (ConfigHandler.showEnergy && GuiScreen.isShiftKeyDown() && event.getType() == ElementType.TEXT && (tile instanceof IEnergyHandler || tile.hasCapability(CapabilityEnergy.ENERGY, null) || (Loader.isModLoaded("tesla") && tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null)))) {
			GuiDrawer drawer = new GuiDrawer(0, 0, 0, 0, 0);
			int energy = 0, max = 0;
			if (tile instanceof IEnergyHandler) {
				try {
					energy = ((IEnergyHandler) tile).getEnergyStored(null);
					max = ((IEnergyHandler) tile).getMaxEnergyStored(null);
				} catch (Exception e) {
					return;
				}
			} else if (tile.hasCapability(CapabilityEnergy.ENERGY, null)) {
				energy = tile.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
				max = tile.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
			} else if (Loader.isModLoaded("tesla") && tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null)) {
				energy = (int) tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null).getStoredPower() % Integer.MAX_VALUE;
				max = (int) tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null).getCapacity() % Integer.MAX_VALUE;
			}
			ScaledResolution sr = event.getResolution();
			String text = energy + "/" + max + " RF";
			int lenght = 80/*mc.fontRendererObj.getStringWidth(text)*/;
			mc.fontRendererObj.drawString(text, (sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(text)) / 2, (sr.getScaledHeight() - 15 - mc.fontRendererObj.FONT_HEIGHT) / 2, 0xffff00, true);
			boolean before = mc.fontRendererObj.getUnicodeFlag();
			mc.fontRendererObj.setUnicodeFlag(true);
			String config = "Can be disabled in Charge Pads config.";
			mc.fontRendererObj.drawString(config, (sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(config)) / 2, (sr.getScaledHeight() + 40 - mc.fontRendererObj.FONT_HEIGHT) / 2, 0x40ffff00, true);
			mc.fontRendererObj.setUnicodeFlag(before);
			drawer.drawEnergyBarH((sr.getScaledWidth() - lenght) / 2, (sr.getScaledHeight() + 20 - 8) / 2, lenght, (float) energy / (float) max);
		}
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
