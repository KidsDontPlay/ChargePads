package mrriegel.chargepads.init;

import mrriegel.chargepads.block.BlockEnergyPad;
import mrriegel.limelib.block.CommonBlock;

public class ModBlocks {

	public static final CommonBlock ENERGYPAD1 = new BlockEnergyPad("energypad_1");
	public static final CommonBlock ENERGYPAD2 = new BlockEnergyPad("energypad_2");
	public static final CommonBlock ENERGYPAD3 = new BlockEnergyPad("energypad_3");

	public static void init() {
		ENERGYPAD1.registerBlock();
		ENERGYPAD2.registerBlock();
		ENERGYPAD3.registerBlock();
	}

	public static void initClient() {
		ENERGYPAD1.initModel();
		ENERGYPAD2.initModel();
		ENERGYPAD3.initModel();
	}
}
