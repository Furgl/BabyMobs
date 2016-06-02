package furgl.babyMobs.common.event;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.entity.ai.EntityAIZombieRiders;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.server.management.PlayerManager;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;

public class ZombieTrapEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(TickEvent.WorldTickEvent event) {
		if (event.phase == Phase.END && !event.world.isRemote && event.world.isRaining() && event.world.isThundering()) {
			WorldServer worldServer = event.world.getMinecraftServer().worldServerForDimension(event.world.provider.getDimension());
			PlayerManager thePlayerManager = (PlayerManager) BabyMobs.reflect(WorldServer.class, "thePlayerManager", worldServer);
			for (Iterator<Chunk> iterator = worldServer.getPersistentChunkIterable(thePlayerManager.getChunkIterator()) ; iterator.hasNext() ; )
			{
				Chunk chunk = (Chunk)iterator.next();
				int j = chunk.xPosition * 16;
				int k = chunk.zPosition * 16;
				if (event.world.provider.canDoLightning(chunk) && event.world.rand.nextInt(100000) == 0)
				{
					int l = event.world.rand.nextInt() >> 2;
					BlockPos blockpos = this.adjustPosToNearbyEntity(new BlockPos(j + (l & 15), 0, k + (l >> 8 & 15)), worldServer);

					if (worldServer.isRainingAt(blockpos))
					{
						DifficultyInstance difficultyinstance = worldServer.getDifficultyForLocation(blockpos);

						if (event.world.rand.nextDouble() < (double)difficultyinstance.getAdditionalDifficulty() * 0.05D)
						{
							EntityHorse entityhorse = new EntityHorse(event.world);
							entityhorse.setType(HorseArmorType.ZOMBIE);
							entityhorse.setSkeletonTrap(true);
							EntityAISkeletonRiders skeletonTrapAI = (EntityAISkeletonRiders) BabyMobs.reflect(EntityHorse.class, "skeletonTrapAI", entityhorse);
							entityhorse.tasks.removeTask(skeletonTrapAI);
							entityhorse.tasks.addTask(1, new EntityAIZombieRiders(entityhorse));
							entityhorse.setGrowingAge(0);
							entityhorse.setPosition((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());	                			
							event.world.spawnEntityInWorld(entityhorse);
							//System.out.println(blockpos);
							worldServer.addWeatherEffect(new EntityLightningBolt(worldServer, (double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ(), true));
						}
					}
				}
			}
		}
	}
	
	private BlockPos adjustPosToNearbyEntity(BlockPos pos, final WorldServer worldServer)
    {
        BlockPos blockpos = worldServer.getPrecipitationHeight(pos);
        AxisAlignedBB axisalignedbb = (new AxisAlignedBB(blockpos, new BlockPos(blockpos.getX(), worldServer.getHeight(), blockpos.getZ()))).expandXyz(3.0D);
        List<EntityLivingBase> list = worldServer.getEntitiesWithinAABB(EntityLivingBase.class, axisalignedbb, new Predicate<EntityLivingBase>()
        {
            public boolean apply(EntityLivingBase p_apply_1_)
            {
                return p_apply_1_ != null && p_apply_1_.isEntityAlive() && worldServer.canSeeSky(p_apply_1_.getPosition());
            }
        });

        if (!list.isEmpty())
        {
            return ((EntityLivingBase)list.get(worldServer.rand.nextInt(list.size()))).getPosition();
        }
        else
        {
            if (blockpos.getY() == -1)
            {
                blockpos = blockpos.up(2);
            }

            return blockpos;
        }
    }
}
