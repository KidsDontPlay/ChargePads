package mrriegel.chargepads.init;

import mrriegel.chargepads.block.BlockEnergyPad;
import mrriegel.chargepads.block.BlockHealthPad;
import mrriegel.limelib.block.CommonBlock;
import mrriegel.limelib.helper.NBTStackHelper;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ModBlocks {

	public static final CommonBlock ENERGYPAD1 = new BlockEnergyPad("energypad_1");
	public static final CommonBlock ENERGYPAD2 = new BlockEnergyPad("energypad_2");
	public static final CommonBlock ENERGYPAD3 = new BlockEnergyPad("energypad_3");
	public static final CommonBlock HEALTHPAD1 = new BlockHealthPad("healthpad_1");
	public static final CommonBlock HEALTHPAD2 = new BlockHealthPad("healthpad_2");
	public static final CommonBlock HEALTHPAD3 = new BlockHealthPad("healthpad_3");

	public static void init() {
		ENERGYPAD1.registerBlock();
		ENERGYPAD2.registerBlock();
		ENERGYPAD3.registerBlock();
		HEALTHPAD1.registerBlock();
		HEALTHPAD2.registerBlock();
		HEALTHPAD3.registerBlock();

		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENERGYPAD1), "isi", "qMq", 'i', "ingotIron", 's', Blocks.STONE_PRESSURE_PLATE, 'q', "gemQuartz", 'M', Blocks.REDSTONE_BLOCK));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENERGYPAD2), "isi", "qMq", 'i', "ingotGold", 's', Blocks.STONE_PRESSURE_PLATE, 'q', "gemQuartz", 'M', ENERGYPAD1));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ENERGYPAD3), "isi", "qMq", 'i', "gemDiamond", 's', Blocks.STONE_PRESSURE_PLATE, 'q', "gemQuartz", 'M', ENERGYPAD2));
		ItemStack potion = new ItemStack(Items.POTIONITEM);
		NBTStackHelper.setString(potion, "Potion", "minecraft:strong_regeneration");
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HEALTHPAD1), "isi", "qMq", 'i', "ingotIron", 's', Blocks.STONE_PRESSURE_PLATE, 'q', "gemQuartz", 'M', potion));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HEALTHPAD2), "isi", "qMq", 'i', "ingotGold", 's', Blocks.STONE_PRESSURE_PLATE, 'q', "gemQuartz", 'M', HEALTHPAD1));
		GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(HEALTHPAD3), "isi", "qMq", 'i', "gemDiamond", 's', Blocks.STONE_PRESSURE_PLATE, 'q', "gemQuartz", 'M', HEALTHPAD2));
	}

	public static void initClient() {
		ENERGYPAD1.initModel();
		ENERGYPAD2.initModel();
		ENERGYPAD3.initModel();
		HEALTHPAD1.initModel();
		HEALTHPAD2.initModel();
		HEALTHPAD3.initModel();
	}
}
