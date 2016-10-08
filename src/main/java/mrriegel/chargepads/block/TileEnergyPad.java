package mrriegel.chargepads.block;

import mrriegel.chargepads.ConfigHandler;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;
import cofh.api.energy.IEnergyContainerItem;

public class TileEnergyPad extends TilePad {

	@Override
	public void update() {
		chargeEntities();
	}

	@Override
	public int getTier() {
		return 0;
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
			if (e instanceof EntityItem) {
				EntityItem ei = (EntityItem) e;
				if (ei.getEntityItem() != null && ei.getEntityItem().getItem() != null) {
					ItemStack stack = ei.getEntityItem();
					charged = chargeItem(stack);
					ei.setEntityItemStack(stack);
				}
			} else if (e instanceof EntityPlayer) {
				PlayerInvWrapper inv = new PlayerInvWrapper(((EntityPlayer) e).inventory);
				for (int i = 0; i < inv.getSlots(); i++) {
					if (inv.getStackInSlot(i) != null && (charged = chargeItem(inv.getStackInSlot(i))))
						break;
				}
			}
		}
	}

	private boolean chargeItem(ItemStack stack) {
		Item item = stack.getItem();
		if (item instanceof IEnergyContainerItem && ConfigHandler.RF) {
			int receive = ((IEnergyContainerItem) item).receiveEnergy(stack, Math.min(this.energy.getEnergyStored(), (int) Math.pow(10, getTier()) * 5), false);
			if (receive > 0) {
				this.energy.extractEnergy(receive, false);
				return true;
			}
		} else if (stack.hasCapability(CapabilityEnergy.ENERGY, null) && ConfigHandler.FE) {
			IEnergyStorage storage = stack.getCapability(CapabilityEnergy.ENERGY, null);
			int receive = storage.receiveEnergy(Math.min(this.energy.getEnergyStored(), (int) Math.pow(10, getTier()) * 5), false);
			if (receive > 0) {
				this.energy.extractEnergy(receive, false);
				return true;
			}
		} else if (stack.hasCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null) && ConfigHandler.TESLA) {
			ITeslaConsumer consumer = stack.getCapability(TeslaCapabilities.CAPABILITY_CONSUMER, null);
			long receive = consumer.givePower(Math.min(this.energy.getEnergyStored(), (int) Math.pow(10, getTier()) * 5), false);
			if (receive > 0) {
				this.energy.extractEnergy((int) receive, false);
				return true;
			}
		}
		return false;
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		return energy.receiveEnergy(maxReceive, simulate);
	}

	@Override
	public int getEnergyStored(EnumFacing from) {
		return energy.getEnergyStored();
	}

	@Override
	public int getMaxEnergyStored(EnumFacing from) {
		return energy.getMaxEnergyStored();
	}

	@Override
	public boolean canConnectEnergy(EnumFacing from) {
		return ConfigHandler.RF;
	}

}
