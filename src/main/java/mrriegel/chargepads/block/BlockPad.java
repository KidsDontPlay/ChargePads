package mrriegel.chargepads.block;

import static net.minecraft.block.BlockDispenser.FACING;
import mrriegel.chargepads.proxy.ClientProxy;
import mrriegel.chargepads.tile.TilePad;
import mrriegel.limelib.block.CommonBlockContainer;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockPad<T extends TilePad> extends CommonBlockContainer<T> {
	public static final PropertyBool CHARGE = PropertyBool.create("charge");

	public BlockPad(String name) {
		super(Material.IRON, name);
		this.setHardness(4F);
		this.setResistance(30F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CHARGE, false));
		this.setCreativeTab(ClientProxy.tab);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return this.getDefaultState().withProperty(FACING, BlockPistonBase.getFacingFromEntity(pos, placer)).withProperty(CHARGE, false);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(FACING, EnumFacing.values()[meta % 6]).withProperty(CHARGE, meta > 5);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(FACING).ordinal() + (state.getValue(CHARGE) ? 6 : 0);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		boolean charge = false;
		if (te instanceof TilePad) {
			charge = ((TilePad) te).isActive();
		}
		return state.withProperty(CHARGE, charge);
	}

	@Override
	public IBlockState withRotation(IBlockState state, Rotation rot) {
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] { FACING, CHARGE });
	}

	public final int getTier() {
		return Integer.valueOf(getRegistryName().toString().substring(getRegistryName().toString().length() - 1));
	}

	@Override
	protected ItemBlock getItemBlock() {
		return (ItemBlock) new ItemBlock(this) {
			@Override
			public EnumRarity getRarity(ItemStack stack) {
				return EnumRarity.values()[getTier() - 1];
			}
		}.setRegistryName(getRegistryName());
	}

}
