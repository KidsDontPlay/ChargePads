package mrriegel.chargepads;

import net.minecraftforge.energy.EnergyStorage;

public class EnergyStorageExt extends EnergyStorage {
	public EnergyStorageExt(int capacity) {
		super(capacity);
	}

	public EnergyStorageExt(int capacity, int maxTransfer) {
		super(capacity, maxTransfer);
	}

	public EnergyStorageExt(int capacity, int maxReceive, int maxExtract) {
		super(capacity, maxReceive, maxExtract);
	}

	public void setEnergyStored(int energy) {
		this.energy = energy;
		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}

	public void modifyEnergyStored(int energy) {
		this.energy += energy;
		if (this.energy > capacity) {
			this.energy = capacity;
		} else if (this.energy < 0) {
			this.energy = 0;
		}
	}
}
