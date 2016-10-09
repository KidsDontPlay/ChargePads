package mrriegel.chargepads.block;

import mrriegel.chargepads.tile.TileEnergyPad;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockEnergyPad extends BlockPad<TileEnergyPad> {

	public BlockEnergyPad(String name) {
		super(name);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		if (getTier() == 1)
			return new TileEnergyPad.T1();
		else if (getTier() == 2)
			return new TileEnergyPad.T2();
		else if (getTier() == 3)
			return new TileEnergyPad.T3();
		return null;
	}

	@Override
	protected Class<? extends TileEnergyPad> getTile() {
		if (getTier() == 1)
			return TileEnergyPad.T1.class;
		else if (getTier() == 2)
			return TileEnergyPad.T2.class;
		else if (getTier() == 3)
			return TileEnergyPad.T3.class;
		return null;
	}

}
