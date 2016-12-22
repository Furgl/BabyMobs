package furgl.babyMobs.common.event;

import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityZombieChicken;
import furgl.babyMobs.common.entity.monster.EntityZombiePig;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SetAttackTargetEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(LivingSetAttackTargetEvent event) {
		if (!event.getEntity().worldObj.isRemote && event.getEntityLiving() instanceof EntityBabyIronGolem && 
				(event.getTarget() instanceof EntityZombieVillager && ((EntityZombieVillager)event.getTarget()).isConverting() ||
						event.getTarget() instanceof EntityZombiePig && ((EntityZombiePig)event.getTarget()).isConverting() ||
						event.getTarget() instanceof EntityZombieChicken && ((EntityZombieChicken)event.getTarget()).isConverting())) {
				((EntityBabyIronGolem)event.getEntityLiving()).setAttackTarget(null);
		}
	}
}
