package mrriegel.chargepads.block;

import static net.minecraft.block.BlockDirectional.FACING;

import java.util.List;
import java.util.Random;

import mrriegel.chargepads.ChargePads;
import mrriegel.chargepads.proxy.CommonProxy;
import mrriegel.chargepads.tile.TilePad;
import mrriegel.limelib.block.CommonBlockContainer;
import mrriegel.limelib.helper.NBTStackHelper;
import mrriegel.limelib.item.CommonItemBlock;
import net.minecraft.block.BlockPistonBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public abstract class BlockPad<T extends TilePad> extends CommonBlockContainer<T> {
	public static final PropertyBool CHARGE = PropertyBool.create("charge");

	public BlockPad(String name) {
		super(Material.IRON, name);
		this.setHardness(3F);
		this.setResistance(30F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(CHARGE, false));
		this.setCreativeTab(CommonProxy.tab);
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
		TileEntity te = world instanceof ChunkCache ? ((ChunkCache) world).getTileEntity(pos, Chunk.EnumCreateEntityType.CHECK) : world.getTileEntity(pos);
		boolean charge = false;
		if (te instanceof TilePad) {
			charge = ((TilePad) te).isActive();
		}
		return state.withProperty(CHARGE, charge);
	}

	@Override
	public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
		super.randomDisplayTick(stateIn, worldIn, pos, rand);
		if (stateIn.getValue(CHARGE)) {
			for (int i = 0; i < getTier() * 3; i++) {
				Vec3d o = new Vec3d(pos.getX() + .5 + ((worldIn.rand.nextDouble() - .5) / 1.1), pos.getY() + .5 + ((worldIn.rand.nextDouble() - .5) / 1.1), pos.getZ() + .5 + ((worldIn.rand.nextDouble() - .5) / 1.1));
				BlockPos nei = pos.offset(stateIn.getValue(FACING));
				Vec3d v = new Vec3d(nei.getX() - pos.getX(), nei.getY() - pos.getY(), nei.getZ() - pos.getZ());
				v = v.scale(.07);
				ChargePads.proxy.spawnParticle(o.xCoord, o.yCoord, o.zCoord, v.xCoord, v.yCoord, v.zCoord, getRegistryName().toString());
			}
		}
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
		super.addInformation(stack, playerIn, tooltip, advanced);
		if (NBTStackHelper.getBoolean(stack, "idatakeeper")) {
			int energy = NBTStackHelper.getInt(stack, "energY");
			String text = energy + " RF";
			tooltip.add(text);
		}
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
		return new CommonItemBlock(this) {
			@Override
			public EnumRarity getRarity(ItemStack stack) {
				return EnumRarity.values()[getTier() - 1];
			}
		};
	}

}
