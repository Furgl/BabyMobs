package furgl.babyMobs.common.event;

import furgl.babyMobs.common.entity.ai.EntityAIZombieRiders;
import net.minecraft.block.material.Material;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class ZombieTrapEvent 
{
	EntityLightningBolt bolt;

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(EntityEvent.EntityConstructing event) {
		if (event.getEntity() instanceof EntityLightningBolt && !event.getEntity().worldObj.isRemote && bolt == null) {
			bolt = (EntityLightningBolt) event.getEntity();
		}
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(TickEvent.WorldTickEvent event) {
		if (bolt != null && !event.world.isRemote && bolt.getPosition() != new BlockPos(0, 0, 0)) {
            DifficultyInstance difficultyinstance = event.world.getDifficultyForLocation(bolt.getPosition());
			if (!(boolean)(ReflectionHelper.getPrivateValue(EntityLightningBolt.class, bolt, 3)) && event.world.rand.nextDouble() < (double)difficultyinstance.getAdditionalDifficulty() * 0.05D)
			{
				BlockPos blockpos = bolt.getPosition();
				EntityHorse entityhorse = new EntityHorse(event.world);
				entityhorse.setType(HorseArmorType.ZOMBIE);
				entityhorse.setSkeletonTrap(true);
				EntityAISkeletonRiders skeletonTrapAI = (EntityAISkeletonRiders) ReflectionHelper.getPrivateValue(EntityHorse.class, entityhorse, 12);
				entityhorse.tasks.removeTask(skeletonTrapAI);
				entityhorse.tasks.addTask(1, new EntityAIZombieRiders(entityhorse));
				entityhorse.setGrowingAge(0);
				entityhorse.setPosition((double)blockpos.getX(), (double)blockpos.getY(), (double)blockpos.getZ());	                			
				event.world.spawnEntityInWorld(entityhorse);
				//System.out.println(blockpos);
				//extinguish fire because can't turn on effectonly (done in constructor)
				if (event.world.getBlockState(blockpos).getMaterial() == Material.fire)
	                event.world.setBlockState(blockpos, Blocks.air.getDefaultState());
			}
			bolt = null;
		}
	}
}
