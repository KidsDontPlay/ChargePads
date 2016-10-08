package mrriegel.chargepads.tile;

import mrriegel.chargepads.ConfigHandler;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import cofh.api.energy.IEnergyContainerItem;

public class TileEnergyPad extends TilePad {

	public TileEnergyPad(int tier) {
		super(tier);
	}

	@Override
	public Pad getPad() {
		return Pad.ENERGY;
	}

	@Override
	protected void chargeEntities() {
		boolean charged = false;
		for (Entity e : getEntities()) {
			if (charged || !e.isEntityAlive() || energy.getEnergyStored() == 0)
				break;
			if (chargeProvider(e)) {
				charged = true;
			} else if (e instanceof EntityItem) {
				EntityItem ei = (EntityItem) e;
				if (ei.getEntityItem() != null && ei.getEntityItem().getItem() != null) {
					ItemStack stack = ei.getEntityItem();
					charged = chargeProvider(stack);
					ei.setEntityItemStack(stack);
				}
			} else if (e instanceof EntityPlayer) {
				PlayerInvWrapper inv = new PlayerInvWrapper(((EntityPlayer) e).inventory);
				for (int i = 0; i < inv.getSlots(); i++) {
					if (inv.getStackInSlot(i) != null && (charged = chargeProvider(inv.getStackInSlot(i))))
						break;
				}
			}
		}
	}

	private boolean chargeProvider(ICapabilityProvider provider) {
		if (ConfigHandler.RF && provider instanceof ItemStack && ((ItemStack) provider).getItem() instanceof IEnergyContainerItem) {
			int receive = ((IEnergyContainerItem) ((ItemStack) provider).getItem()).receiveEnergy((ItemStack) provider, Math.min(this.energy.getEnergyStored(), (int) Math.pow(8, getTier()) * 5), false);
			if (receive > 0) {
				this.energy.extractEnergy(receive, false);
				return true;
			}
		} else if (ConfigHandler.FE && provider.hasCapability(CapabilityEnergy.ENERGY, null)) {
			IEnergyStorage storage = provider.getCapability(CapabilityEnergy.ENERGY, null);
			int receive = storage.receiveEnergy(Math.min(this.energy.getEnergyStored(), (int) Math.pow(8, getTier()) * 5), false);
			if (receive > 0) {
				this.energy.extractEnergy(receive, false);
				return true;
			}
		} else if (ConfigHandler.TESLA && provider.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null)) {
			ITeslaConsumer consumer = provider.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null);
			long receive = consumer.givePower(Math.min(this.energy.getEnergyStored(), (int) Math.pow(8, getTier()) * 5), false);
			if (receive > 0) {
				this.energy.extractEnergy((int) receive, false);
				return true;
			}
		}
		return false;
	}

}
