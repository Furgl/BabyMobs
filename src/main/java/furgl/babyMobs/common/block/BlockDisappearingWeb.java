package furgl.babyMobs.common.block;

import java.util.Random;

import net.minecraft.block.BlockWeb;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockDisappearingWeb extends BlockWeb
{
	public BlockDisappearingWeb()
	{
		super();
		this.setCreativeTab(null);
		this.setTickRandomly(true);
	}
	
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		if (!world.isRemote)
		{
			world.setBlockToAir(pos);
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 *  
	 * @param fortune the level of the Fortune enchantment on the player's tool
	 */
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune)
	{
		return null;
	}

	@Override
	protected boolean canSilkHarvest()
	{
		return false;
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand)
	{
		if (!worldIn.isRemote)
		{
			//worldIn.setBlockToAir(pos);
		}
	}
}
