package furgl.babyMobs.common.event;

import furgl.babyMobs.common.entity.monster.EntityBabyDragon;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnDragonEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(BlockEvent.NeighborNotifyEvent event)
	{
		if (event.getState().getBlock() instanceof BlockDragonEgg && !event.getWorld().isRemote)
		{
			EntityBabyDragon dragon = new EntityBabyDragon(event.getWorld(), new Vec3d(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ()));
			dragon.setPosition(event.getPos().getX(), event.getPos().getY(), event.getPos().getZ());
			event.getWorld().spawnEntity(dragon);
		}
	}
}
