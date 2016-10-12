package mrriegel.chargepads.block;

import mrriegel.chargepads.tile.TileHealthPad;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockHealthPad extends BlockPad<TileHealthPad> {

	public BlockHealthPad(String name) {
		super(name);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		if (getTier() == 1)
			return new TileHealthPad.T1();
		else if (getTier() == 2)
			return new TileHealthPad.T2();
		else if (getTier() == 3)
			return new TileHealthPad.T3();
		return null;
	}

	@Override
	protected Class<? extends TileHealthPad> getTile() {
		if (getTier() == 1)
			return TileHealthPad.T1.class;
		else if (getTier() == 2)
			return TileHealthPad.T2.class;
		else if (getTier() == 3)
			return TileHealthPad.T3.class;
		return null;
	}

}
