package mrriegel.chargepads.tile;

import java.util.Random;

import mrriegel.chargepads.ConfigHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;

public class TileHealthPad extends TilePad {

	@Override
	protected boolean chargeEntities() {
		boolean charged = false;
		for (Entity e : getEntities(false)) {
			if (charged || energy.getEnergyStored() == 0)
				break;
			if (!e.isEntityAlive() || !canHeal(e))
				continue;
			if (healEntity((EntityLivingBase) e)) {
				charged = true;
			}
		}
		return charged;
	}

	private boolean canHeal(Entity entity) {
		if (!(entity instanceof EntityLivingBase))
			return false;
		if (entity instanceof IAnimals && ConfigHandler.healAnimals)
			return true;
		if (entity instanceof IMob && ConfigHandler.healMonsters)
			return true;
		return entity instanceof EntityPlayer;
	}

	private static final int RFPERHEAL = 2000;

	private boolean healEntity(EntityLivingBase entity) {
		if (entity.getMaxHealth() > entity.getHealth()) {
			float need = Math.min(entity.getMaxHealth() - entity.getHealth(), healAmount());
			if (need > 0F) {
				this.energy.extractEnergy((int) (RFPERHEAL * need), false);
				entity.setHealth(entity.getHealth() + need);
				if (entity instanceof EntityPlayer && getTier() > 2 && new Random().nextInt(200) == 0) {
					((EntityPlayer) entity).getFoodStats().setFoodLevel(((EntityPlayer) entity).getFoodStats().getFoodLevel() + 1);
				}
				return true;
			}
		}
		return false;
	}

	private float healAmount() {
		return Math.min((float) this.energy.getEnergyStored() / (float) RFPERHEAL, (float) Math.pow(3, getTier()) * 0.005F);
	}

	public static class T1 extends TileHealthPad {
		public T1() {
			energy = getBaseEnergy(1);
		}
	}

	public static class T2 extends TileHealthPad {
		public T2() {
			energy = getBaseEnergy(2);
		}
	}

	public static class T3 extends TileHealthPad {
		public T3() {
			energy = getBaseEnergy(3);
		}
	}

}
