package furgl.babyMobs.common.event;

import java.util.Iterator;

import com.google.common.base.Predicate;

import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityBabySnowman;
import furgl.babyMobs.common.entity.monster.EntityBabyWither;
import net.minecraft.block.state.BlockWorldState;
import net.minecraft.block.state.pattern.BlockPattern;
import net.minecraft.block.state.pattern.BlockStateHelper;
import net.minecraft.block.state.pattern.FactoryBlockPattern;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.stats.AchievementList;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.EnumDifficulty;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockPatternEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(BlockEvent.PlaceEvent event)
	{				
		final Predicate IS_WITHER_SKELETON = new Predicate()
		{
			public boolean apply(BlockWorldState state)
			{
				return state.getBlockState().getBlock() == Blocks.skull && state.getTileEntity() instanceof TileEntitySkull && ((TileEntitySkull)state.getTileEntity()).getSkullType() == 1;
			}
			@Override
			public boolean apply(Object p_apply_1_)
			{
				return this.apply((BlockWorldState)p_apply_1_);
			}
		};

		BlockPattern.PatternHelper patternhelper;
		BlockPattern babySnowmanPattern = FactoryBlockPattern.start().aisle(new String[] {"^", "#"}).where('^', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.pumpkin))).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.snow))).build();
		BlockPattern babyIronGolemPattern = FactoryBlockPattern.start().aisle(new String[] {"^", "#"}).where('^', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.pumpkin))).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.iron_block))).build();
		BlockPattern babyWitherPattern = FactoryBlockPattern.start().aisle(new String[] {"^", "#"}).where('^', IS_WITHER_SKELETON).where('#', BlockWorldState.hasState(BlockStateHelper.forBlock(Blocks.soul_sand))).build();

		if (event.pos.getY() >= 2 && event.world.getDifficulty() != EnumDifficulty.PEACEFUL && !event.world.isRemote)
		{
			if ((patternhelper = babyWitherPattern.match(event.world, event.pos)) != null && event.world.getBlockState(event.pos.north().down()).getBlock() != Blocks.soul_sand && event.world.getBlockState(event.pos.south().down()).getBlock() != Blocks.soul_sand && event.world.getBlockState(event.pos.east().down()).getBlock() != Blocks.soul_sand && event.world.getBlockState(event.pos.west().down()).getBlock() != Blocks.soul_sand)
			{
				for (int i = 0; i < 3; ++i)
				{
					patternhelper.translateOffset(i, 0, 0);
				}

				for (int i = 0; i < babyWitherPattern.getPalmLength(); ++i)
				{
					for (int j = 0; j < babyWitherPattern.getThumbLength(); ++j)
					{
						BlockWorldState blockworldstate1 = patternhelper.translateOffset(i, j, 0);
						event.world.setBlockState(blockworldstate1.getPos(), Blocks.air.getDefaultState(), 2);
					}
				}

				BlockPos blockpos1 = patternhelper.translateOffset(1, 0, 0).getPos();
				EntityBabyWither EntityBabyWither = new EntityBabyWither(event.world);
				BlockPos blockpos2 = patternhelper.translateOffset(1, 2, 0).getPos();
				EntityBabyWither.setLocationAndAngles(blockpos2.getX() - 0.5D, blockpos2.getY() + 1.55D, blockpos2.getZ() + 0.5D, patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F, 0.0F);
				EntityBabyWither.renderYawOffset = patternhelper.getFinger().getAxis() == EnumFacing.Axis.X ? 0.0F : 90.0F;
				EntityBabyWither.func_82206_m();
				Iterator iterator = event.world.getEntitiesWithinAABB(EntityPlayer.class, EntityBabyWither.getEntityBoundingBox().expand(50.0D, 50.0D, 50.0D)).iterator();

				while (iterator.hasNext())
				{
					EntityPlayer entityplayer = (EntityPlayer)iterator.next();
					entityplayer.triggerAchievement(AchievementList.spawnWither);
				}

				event.world.spawnEntityInWorld(EntityBabyWither);
				for (int k = 0; k < 120; ++k)
				{
					event.world.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos1.getX() + event.world.rand.nextDouble(), blockpos1.getY() - 2 + event.world.rand.nextDouble() * 3.9D, blockpos1.getZ() + event.world.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
				}

				for (int k = 0; k < babyWitherPattern.getPalmLength(); ++k)
				{
					for (int l = 0; l < babyWitherPattern.getThumbLength(); ++l)
					{
						BlockWorldState blockworldstate2 = patternhelper.translateOffset(k, l, 0);
						event.world.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
					}
				}
			}
		}
		if ((patternhelper = babySnowmanPattern.match(event.world, event.pos)) != null)
		{
			for (int i = 0; i < babySnowmanPattern.getThumbLength(); ++i)
			{
				BlockWorldState blockworldstate = patternhelper.translateOffset(0, i, 0);
				event.world.setBlockState(blockworldstate.getPos(), Blocks.air.getDefaultState(), 2);
			}

			EntityBabySnowman EntityBabySnowman = new EntityBabySnowman(event.world);
			BlockPos blockpos2 = patternhelper.translateOffset(0, 2, 0).getPos();
			EntityBabySnowman.setLocationAndAngles(blockpos2.getX() + 0.5D, blockpos2.getY() + 1.05D, blockpos2.getZ() + 0.5D, 0.0F, 0.0F);
			event.world.spawnEntityInWorld(EntityBabySnowman);

			for (int j = 0; j < 120; ++j)
			{
				event.world.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, blockpos2.getX() + event.world.rand.nextDouble(), blockpos2.getY() + event.world.rand.nextDouble() * 2.5D, blockpos2.getZ() + event.world.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
			}

			for (int j = 0; j < babySnowmanPattern.getThumbLength(); ++j)
			{
				BlockWorldState blockworldstate1 = patternhelper.translateOffset(0, j, 0);
				event.world.notifyNeighborsRespectDebug(blockworldstate1.getPos(), Blocks.air);
			}
		}
		else if ((patternhelper = babyIronGolemPattern.match(event.world, event.pos)) != null)
		{
			for (int i = 0; i < babyIronGolemPattern.getPalmLength(); ++i)
			{
				for (int k = 0; k < babyIronGolemPattern.getThumbLength(); ++k)
				{
					event.world.setBlockState(patternhelper.translateOffset(i, k, 0).getPos(), Blocks.air.getDefaultState(), 2);
				}
			}

			BlockPos blockpos1 = patternhelper.translateOffset(1, 2, 0).getPos();
			EntityBabyIronGolem EntityBabyIronGolem = new EntityBabyIronGolem(event.world);
			EntityBabyIronGolem.setPlayerCreated(true);
			EntityBabyIronGolem.setLocationAndAngles(blockpos1.getX() - 0.5D, blockpos1.getY() + 1.05D, blockpos1.getZ() + 0.5D, 0.0F, 0.0F);
			event.world.spawnEntityInWorld(EntityBabyIronGolem);

			for (int j = 0; j < 120; ++j)
			{
				event.world.spawnParticle(EnumParticleTypes.SNOWBALL, blockpos1.getX() + event.world.rand.nextDouble(), blockpos1.getY() + event.world.rand.nextDouble() * 3.9D, blockpos1.getZ() + event.world.rand.nextDouble(), 0.0D, 0.0D, 0.0D, new int[0]);
			}

			for (int j = 0; j < babyIronGolemPattern.getPalmLength(); ++j)
			{
				for (int l = 0; l < babyIronGolemPattern.getThumbLength(); ++l)
				{
					BlockWorldState blockworldstate2 = patternhelper.translateOffset(j, l, 0);
					event.world.notifyNeighborsRespectDebug(blockworldstate2.getPos(), Blocks.air);
				}
			}
		}
	}
}
