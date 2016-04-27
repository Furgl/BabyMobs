package furgl.babyMobs.common.block;

import java.util.Random;

import net.minecraft.block.BlockWeb;
import net.minecraft.item.Item;
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
	public void updateTick(World world, int x, int y, int z, Random random)
	{
		if (!world.isRemote)
		{
			world.setBlockToAir(x, y, z);
		}
	}

	/**
	 * Get the Item that this Block should drop when harvested.
	 *  
	 * @param fortune the level of the Fortune enchantment on the player's tool
	 */
	@Override
	public Item getItemDropped(int meta, Random rand, int fortune)
	{
		return null;
	}

	@Override
	protected boolean canSilkHarvest()
	{
		return false;
	}
}
