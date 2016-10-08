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
		return new TileEnergyPad(getTier());
	}

	@Override
	protected Class<? extends TileEnergyPad> getTile() {
		return TileEnergyPad.class;
	}

}
