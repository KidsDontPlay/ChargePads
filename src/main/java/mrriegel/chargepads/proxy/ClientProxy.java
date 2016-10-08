package mrriegel.chargepads.proxy;

import mrriegel.limelib.gui.GuiDrawer;
import mrriegel.limelib.helper.ColorHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.client.event.RenderGameOverlayEvent.Post;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ClientProxy extends CommonProxy {

	@Override
	public void init(FMLInitializationEvent event) {
		super.init(event);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void overLay(RenderGameOverlayEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		if (event instanceof Post && event.getType() == ElementType.TEXT && mc.theWorld.getTileEntity(mc.objectMouseOver.getBlockPos()) != null) {
			GuiDrawer drawer = new GuiDrawer(0, 0, 0, 0, 0);
			// drawer.drawColoredRectangle(0, 0, 490, 420,
			// ColorHelper.getRGB(0xff0000, 50));
			ScaledResolution sr = event.getResolution();
			int diff = 15, lenght = 80	;
			String text = "Daffne";
			mc.fontRendererObj.drawString(text, (sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(text)) / 2, (sr.getScaledHeight() - diff - mc.fontRendererObj.FONT_HEIGHT) / 2, 0xffff00, true);
			drawer.drawEnergyBarH((sr.getScaledWidth() - lenght) / 2, (sr.getScaledHeight() - -(diff+5) - 8) / 2, lenght, .87f);
		}
	}

}
