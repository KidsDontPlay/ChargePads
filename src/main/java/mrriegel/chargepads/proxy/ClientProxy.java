package mrriegel.chargepads.proxy;

import mrriegel.chargepads.ChargePads;
import mrriegel.chargepads.init.ModBlocks;
import mrriegel.limelib.gui.GuiDrawer;
import mrriegel.limelib.util.Utils;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.energy.CapabilityEnergy;
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
		TileEntity tile = mc.theWorld.getTileEntity(mc.objectMouseOver.getBlockPos());
		if (GuiScreen.isShiftKeyDown() && event.getType() == ElementType.TEXT && tile != null && (tile instanceof IEnergyHandler || tile.hasCapability(CapabilityEnergy.ENERGY, null) || tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null))) {
			GuiDrawer drawer = new GuiDrawer(0, 0, 0, 0, 0);
			int energy = 0, max = 0;
			if (tile instanceof IEnergyHandler) {
				energy = ((IEnergyHandler) tile).getEnergyStored(null);
				max = ((IEnergyHandler) tile).getMaxEnergyStored(null);
			} else if (tile.hasCapability(CapabilityEnergy.ENERGY, null)) {
				energy = tile.getCapability(CapabilityEnergy.ENERGY, null).getEnergyStored();
				max = tile.getCapability(CapabilityEnergy.ENERGY, null).getMaxEnergyStored();
			} else if (tile.hasCapability(TeslaCapabilities.CAPABILITY_HOLDER, null)) {
				energy = (int) tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null).getStoredPower() % Integer.MAX_VALUE;
				max = (int) tile.getCapability(TeslaCapabilities.CAPABILITY_HOLDER, null).getCapacity() % Integer.MAX_VALUE;
			}
			ScaledResolution sr = event.getResolution();
			int diff = 15, lenght = 80;
			String text = energy + " / " + Utils.formatNumber(max) + " RF";
			mc.fontRendererObj.drawString(text, (sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(text)) / 2, (sr.getScaledHeight() - diff - mc.fontRendererObj.FONT_HEIGHT) / 2, 0xffff00, true);
			drawer.drawEnergyBarH((sr.getScaledWidth() - lenght) / 2, (sr.getScaledHeight() - -(diff + 5) - 8) / 2, lenght, (float)energy / (float)max);
		}
	}

	public static final CreativeTabs tab = new CreativeTabs(ChargePads.MODID) {

		@Override
		public Item getTabIconItem() {
			return Item.getItemFromBlock(ModBlocks.ENERGYPAD1);
		}

		public String getTranslatedTabLabel() {
			return ChargePads.MODNAME;
		}
	};

}
