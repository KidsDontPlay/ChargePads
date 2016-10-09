package mrriegel.chargepads.block;

import mrriegel.limelib.block.CommonBlockContainer;
import mrriegel.limelib.tile.CommonTile;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.world.World;

public class BlockJust extends CommonBlockContainer {

	public BlockJust() {
		super(Material.WOOD, "just");
		this.setCreativeTab(CreativeTabs.REDSTONE);
	}

	public static class TileJust extends CommonTile implements ITickable {

		@Override
		public void update() {
			if (!worldObj.isRemote && worldObj.getTotalWorldTime() % 30 == 0)
				System.out.println("tick");
		}

		protected void chargeEntities() {

		}

	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileJust();
	}

	@Override
	protected Class<? extends CommonTile> getTile() {
		return TileJust.class;
	}

}
