package mrriegel.chargepads.block;

import mrriegel.limelib.helper.NBTStackHelper;
import mrriegel.limelib.tile.CommonTile;
import mrriegel.limelib.tile.IDataKeeper;
import net.minecraft.block.BlockDispenser;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import cofh.api.energy.EnergyStorage;

public abstract class TilePad extends CommonTile implements ITickable, IDataKeeper {

	public enum Pad {
		CHARGE, HEALTH;
	}

//	protected Pad pad;
	protected EnergyStorage energy = new EnergyStorage((int) Math.pow(10, getTier()) * 5000, (int) Math.pow(10, getTier()) * 1000);

	public EnumFacing getFacing() {
		return worldObj.getBlockState(pos).getValue(BlockDispenser.FACING);
	}

	public abstract int getTier();
	
	public abstract Pad getPad();

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
//		if (compound.hasKey("pad"))
//			pad = Pad.values()[compound.getInteger("pad")];
		energy.readFromNBT(compound.getCompoundTag("energy"));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
//		if (pad != null)
//			compound.setInteger("pad", pad.ordinal());
		NBTTagCompound n = new NBTTagCompound();
		energy.writeToNBT(n);
		compound.setTag("energy", n);
		return super.writeToNBT(compound);
	}

	@Override
	public void writeToStack(ItemStack stack) {
		if (!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		NBTTagCompound n = new NBTTagCompound();
		energy.writeToNBT(n);
		stack.getTagCompound().setTag("energy", n);
	}

	@Override
	public void readFromStack(ItemStack stack) {
		energy.readFromNBT(stack.getTagCompound().getCompoundTag("energy"));
	}

}
