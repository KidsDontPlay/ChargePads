package mrriegel.chargepads.tile;

import java.util.List;

import mrriegel.chargepads.ConfigHandler;
import mrriegel.limelib.helper.EnergyHelper;
import mrriegel.limelib.helper.EnergyHelper.Energy;
import mrriegel.limelib.helper.InvHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import com.google.common.collect.Lists;

public class TileEnergyPad extends TilePad {

	@Override
	protected boolean chargeEntities() {
		boolean charged = false;
		for (Entity e : getEntities(false)) {
			if (charged || energy.getEnergyStored() == 0)
				break;
			if (!e.isEntityAlive())
				continue;
			if (chargeProvider(e)) {
				charged = true;
			} else if (e instanceof EntityItem) {
				EntityItem ei = (EntityItem) e;
				if (!ei.getEntityItem().isEmpty()) {
					ItemStack stack = ei.getEntityItem();
					charged = chargeProvider(stack);
					ei.setEntityItemStack(stack);
				}
			} else if (e instanceof EntityPlayer) {
				PlayerInvWrapper inv = new PlayerInvWrapper(((EntityPlayer) e).inventory);
				for (int i = 0; i < inv.getSlots(); i++) {
					if (!inv.getStackInSlot(i).isEmpty() && chargeProvider(inv.getStackInSlot(i))) {
						//						if (worldObj.rand.nextBoolean()&&false)
						//							((EntityPlayer) e).openContainer.detectAndSendChanges();
						charged = true;
						break;
					}
				}
			}
		}
		if (!charged) {
			IItemHandler inv = InvHelper.getItemHandler(world.getTileEntity(pos.offset(getFacing())), null);
			if (inv != null)
				for (int i = 0; i < inv.getSlots(); i++) {
					if (!inv.getStackInSlot(i).isEmpty() && chargeProvider(inv.getStackInSlot(i))) {
						charged = true;
						break;
					}
				}
		}
		return charged;
	}

	private boolean chargeProvider(ICapabilityProvider provider) {
		List<Energy> lis = Lists.newArrayList();
		if (ConfigHandler.RF)
			lis.add(Energy.RF);
		if (ConfigHandler.FE)
			lis.add(Energy.FORGE);
		if (ConfigHandler.TESLA)
			lis.add(Energy.TESLA);
		if (EnergyHelper.isEnergyContainer(provider, null, lis.toArray(new Energy[lis.size()])) == null)
			return false;
		int receive = (int) EnergyHelper.receiveEnergy(provider, null, chargeAmount(), false);
		if (receive > 0) {
			this.energy.extractEnergy(receive, false);
			return true;
		}
		return false;
	}

	private int chargeAmount() {
		return Math.min(this.energy.getEnergyStored(), (int) Math.pow(8, getTier()) * 5);
	}

	public static class T1 extends TileEnergyPad {
		public T1() {
			energy = getBaseEnergy(1);
		}
	}

	public static class T2 extends TileEnergyPad {
		public T2() {
			energy = getBaseEnergy(2);
		}
	}

	public static class T3 extends TileEnergyPad {
		public T3() {
			energy = getBaseEnergy(3);
		}
	}

}
