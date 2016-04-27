package furgl.babyMobs.common.event;

import furgl.babyMobs.common.entity.monster.EntityBabyDragon;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.util.Vec3;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SpawnDragonEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(BlockEvent.NeighborNotifyEvent event)
	{
		if (event.state.getBlock() instanceof BlockDragonEgg && !event.world.isRemote)
		{
			EntityBabyDragon dragon = new EntityBabyDragon(event.world, new Vec3(event.pos.getX(), event.pos.getY(), event.pos.getZ()));
			dragon.setPosition(event.pos.getX(), event.pos.getY(), event.pos.getZ());
			event.world.spawnEntityInWorld(dragon);
		}
	}
}
