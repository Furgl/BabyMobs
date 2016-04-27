package furgl.babyMobs.common.event;

import furgl.babyMobs.common.block.BlockDisappearingWeb;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreakSpeedEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerEvent.BreakSpeed event)
	{
		if (event.state.getBlock() instanceof BlockDisappearingWeb)
		{
			if (event.entityPlayer.getHeldItem() != null && (event.entityPlayer.getHeldItem().getItem() instanceof ItemSword || event.entityPlayer.getHeldItem().getItem() instanceof ItemShears))
				event.newSpeed = event.originalSpeed*35;
		}
	}
}
