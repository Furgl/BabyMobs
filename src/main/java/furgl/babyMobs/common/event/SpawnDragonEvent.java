package furgl.babyMobs.common.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import furgl.babyMobs.common.entity.monster.EntityBabyDragon;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.entity.item.EntityFallingBlock;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent;

public class SpawnDragonEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(EntityJoinWorldEvent event)
	{
		if (event.entity instanceof EntityFallingBlock && ((EntityFallingBlock)event.entity).func_145805_f() instanceof BlockDragonEgg && !event.entity.worldObj.isRemote)
		{
			int y = (int) event.entity.posY-1;
			while (event.entity.worldObj.isAirBlock((int) event.entity.posX, y, (int) event.entity.posZ) && y>0)
			{
				y--;
			}
			EntityBabyDragon dragon = new EntityBabyDragon(event.entity.worldObj, Vec3.createVectorHelper((int)event.entity.posX, y+1, (int)event.entity.posZ-1));
			dragon.setPosition(event.entity.posX, event.entity.posY, event.entity.posZ);
			event.entity.worldObj.spawnEntityInWorld(dragon);
		}
	}

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(BlockEvent.PlaceEvent event)
	{
		if (event.block instanceof BlockDragonEgg && !event.world.isRemote)
		{
			EntityBabyDragon dragon = new EntityBabyDragon(event.world, Vec3.createVectorHelper(event.x, event.y, event.z));
			dragon.setPosition(event.x, event.y, event.z);
			event.world.spawnEntityInWorld(dragon);
		}
	}
}
