package mrriegel.chargepads.tile;

import java.util.List;

import mrriegel.chargepads.ConfigHandler;
import mrriegel.chargepads.block.BlockPad;
import mrriegel.limelib.helper.NBTHelper;
import mrriegel.limelib.helper.NBTStackHelper;
import mrriegel.limelib.tile.CommonTile;
import mrriegel.limelib.tile.IDataKeeper;
import mrriegel.limelib.util.EnergyStorageExt;
import net.darkhax.tesla.api.ITeslaConsumer;
import net.darkhax.tesla.capability.TeslaCapabilities;
import net.minecraft.block.BlockDispenser;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.EnergyStorage;
import cofh.api.energy.IEnergyReceiver;

public abstract class TilePad extends CommonTile implements ITickable, IDataKeeper, IEnergyReceiver {

	protected EnergyStorageExt energy;
	protected boolean active = false, markedForSync = false;

	public EnumFacing getFacing() {
		return world.getBlockState(pos).getValue(BlockDispenser.FACING);
	}

	public boolean isActive() {
		return active;
	}

	public int getTier() {
		try {
			return ((BlockPad<?>) getBlockType()).getTier();
		} catch (Exception e) {
			return 1;
		}
	}

	public EnergyStorageExt getBaseEnergy(int tier) {
		return new EnergyStorageExt((int) Math.pow(8, tier) * 5000, (int) Math.pow(8, tier) * 1000);
	}

	protected abstract boolean chargeEntities();

	private List<Entity> entityList;

	public List<Entity> getEntities(boolean force) {
		if (entityList == null || world.getTotalWorldTime() % 10 == 0 || force)
			return entityList = world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.offset(getFacing())).expand(getFacing().getAxis() == Axis.X ? 1 : 0, getFacing().getAxis() == Axis.Y ? 1 : 0, getFacing().getAxis() == Axis.Z ? 1 : 0));
		else
			return entityList;
	}

	@Override
	public void update() {
		if (onClient())
			return;
		boolean before = active;
		if (!world.isBlockPowered(pos)) {
			active = chargeEntities();
			if (active) {
				markedForSync = true;
			}
		} else
			active = false;
		if (markedForSync && world.getTotalWorldTime() % 5 == 0) {
			markedForSync = false;
			sync();
		}
		if (active != before) {
			world.setBlockState(pos, world.getBlockState(pos).withProperty(BlockPad.CHARGE, active), 2);
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		energy.setEnergyStored(NBTHelper.getInt(compound, "energy"));
		active = compound.getBoolean("ACTIVE");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTHelper.setInt(compound, "energy", energy.getEnergyStored());
		compound.setBoolean("ACTIVE", active);
		return super.writeToNBT(compound);
	}

	@Override
	public void writeToStack(ItemStack stack) {
		NBTStackHelper.setInt(stack, "energY", energy.getEnergyStored());
	}

	@Override
	public void readFromStack(ItemStack stack) {
		energy.setEnergyStored(NBTStackHelper.getInt(stack, "energY"));
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return (ConfigHandler.FE && capability == CapabilityEnergy.ENERGY) || (ConfigHandler.TESLA && capability == TeslaCapabilities.CAPABILITY_CONSUMER) || super.hasCapability(capability, facing);
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (ConfigHandler.FE && capability == CapabilityEnergy.ENERGY)
			return (T) energy;
		if (ConfigHandler.TESLA && capability == TeslaCapabilities.CAPABILITY_CONSUMER)
			return (T) new TeslaConsumer(energy);
		return super.getCapability(capability, facing);
	}

	@Override
	public int receiveEnergy(EnumFacing from, int maxReceive, boolean simulate) {
		markedForSync = true;
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

	public static class TeslaConsumer implements ITeslaConsumer {
		private EnergyStorage energy;

		public TeslaConsumer(EnergyStorage energy) {
			this.energy = energy;
		}

		@Override
		public long givePower(long power, boolean simulated) {
			return energy.receiveEnergy((int) power, simulated);
		}
	}

}
