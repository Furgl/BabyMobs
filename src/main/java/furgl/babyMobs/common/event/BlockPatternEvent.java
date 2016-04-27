package furgl.babyMobs.common.event;

import java.util.Iterator;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityBabySnowman;
import furgl.babyMobs.common.entity.monster.EntityBabyWither;
import net.minecraft.block.Block;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockSkull;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.event.world.BlockEvent;

public class BlockPatternEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(BlockEvent.PlaceEvent event)
	{				
		//TODO baby wither
		if (event.block instanceof BlockSkull && event.world.getTileEntity(event.x, event.y, event.z) instanceof TileEntitySkull && event.block instanceof BlockSkull && !this.shouldNormalWitherSpawn(event)) 
		{
			TileEntitySkull skullTile = (TileEntitySkull) event.world.getTileEntity(event.x, event.y, event.z);
			BlockSkull skullBlock = (BlockSkull) event.block;
			if (skullTile.func_145904_a() == 1 && event.y >= 2 && event.world.difficultySetting != EnumDifficulty.PEACEFUL && !event.world.isRemote) 
			{
				EntityBabyWither entitybabywither;
				Iterator iterator;
				EntityPlayer entityplayer;
				int i1;

				if (event.world.getBlock(event.x, event.y - 1, event.z) == Blocks.soul_sand && this.something(event.world, event.x, event.y, event.z, 1, skullBlock)) 
				{
					event.world.setBlockMetadataWithNotify(event.x, event.y, event.z, 8, 2);
					event.world.setBlock(event.x, event.y, event.z, Block.getBlockById(0), 0, 2);
					event.world.setBlock(event.x, event.y-1, event.z, Block.getBlockById(0), 0, 2);
					if (!event.world.isRemote) 
					{
						entitybabywither = new EntityBabyWither(event.world);
						entitybabywither.setLocationAndAngles(event.x + 0.5D, event.y - 0.5D, event.z + 0.5D, 90.0F, 0.0F);
						entitybabywither.renderYawOffset = 90.0F;
						entitybabywither.func_82206_m();

						if (!event.world.isRemote) 
						{
							iterator = event.world.getEntitiesWithinAABB(EntityPlayer.class,
									entitybabywither.boundingBox.expand(50.0D, 50.0D, 50.0D)).iterator();

							while (iterator.hasNext()) {
								entityplayer = (EntityPlayer) iterator.next();
								entityplayer.triggerAchievement(AchievementList.field_150963_I);
							}
						}

						event.world.spawnEntityInWorld(entitybabywither);
					}

					for (i1 = 0; i1 < 120; ++i1) 
					{
						event.world.spawnParticle("snowballpoof", event.x + event.world.rand.nextDouble(),
								event.y - 2 + event.world.rand.nextDouble() * 3.9D,
								event.z + 1 + event.world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
					}

					event.world.notifyBlockChange(event.x, event.y, event.z, Block.getBlockById(0));
					event.world.notifyBlockChange(event.x, event.y - 1, event.z, Block.getBlockById(0));
					return;
				}
			} 
		}
		//TODO baby snowman
		else if (event.block instanceof BlockPumpkin && event.world.getBlock(event.x, event.y - 1, event.z) == Blocks.snow && event.world.getBlock(event.x, event.y - 2, event.z) != Blocks.snow)
		{
			if (!event.world.isRemote)
			{
				event.world.setBlock(event.x, event.y, event.z, Block.getBlockById(0), 0, 2);
				event.world.setBlock(event.x, event.y - 1, event.z, Block.getBlockById(0), 0, 2);
				EntityBabySnowman entitybabysnowman = new EntityBabySnowman(event.world);
				entitybabysnowman.setLocationAndAngles(event.x + 0.5D, event.y - 0.5D, event.z + 0.5D, 0.0F, 0.0F);
				event.world.spawnEntityInWorld(entitybabysnowman);
				event.world.notifyBlockChange(event.x, event.y, event.z, Block.getBlockById(0));
				event.world.notifyBlockChange(event.x, event.y - 1, event.z, Block.getBlockById(0));
			}

			for (int i1 = 0; i1 < 120; ++i1)
			{
				event.world.spawnParticle("snowshovel", event.x + event.world.rand.nextDouble(), event.y - 2 + event.world.rand.nextDouble() * 2.5D, event.z + event.world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
		}
		//TODO baby iron golem
		else if (event.block instanceof BlockPumpkin && event.world.getBlock(event.x, event.y - 1, event.z) == Blocks.iron_block && event.world.getBlock(event.x, event.y - 2, event.z) != Blocks.iron_block)
		{
			event.world.setBlock(event.x, event.y, event.z, Block.getBlockById(0), 0, 2);
			event.world.setBlock(event.x, event.y - 1, event.z, Block.getBlockById(0), 0, 2);
			EntityBabyIronGolem entitybabyirongolem = new EntityBabyIronGolem(event.world);
			entitybabyirongolem.setPlayerCreated(true);
			entitybabyirongolem.setLocationAndAngles(event.x + 0.5D, event.y - 0.5D, event.z + 0.5D, 0.0F, 0.0F);
			event.world.spawnEntityInWorld(entitybabyirongolem);
			for (int l = 0; l < 120; ++l)
			{
				event.world.spawnParticle("snowballpoof", event.x + event.world.rand.nextDouble(), event.y - 2 + event.world.rand.nextDouble() * 3.9D, event.z + event.world.rand.nextDouble(), 0.0D, 0.0D, 0.0D);
			}
			event.world.notifyBlockChange(event.x, event.y, event.z, Block.getBlockById(0));
			event.world.notifyBlockChange(event.x, event.y - 1, event.z, Block.getBlockById(0));
		}
	}

	public boolean something(World world, int x, int y, int z, int par5, BlockSkull skull)
	{
		if (world.getBlock(x, y, z) != skull)
		{
			return false;
		}
		else
		{
			TileEntity tileentity = world.getTileEntity(x, y, z);
			return tileentity != null && tileentity instanceof TileEntitySkull ? ((TileEntitySkull)tileentity).func_145904_a() == par5 : false;
		}
	}
	
	public boolean shouldNormalWitherSpawn(BlockEvent.PlaceEvent event)
	{
		for (int x=event.x-1; x<=event.x+1; x++)
		{
			for (int z=event.z-1; z<=event.z+1; z++)
			{
				if (event.world.getBlock(x, event.y-1, z) == Blocks.soul_sand && !(event.x == x && event.z == z))
					return true;
			}
		}
		return false;
	}
}
