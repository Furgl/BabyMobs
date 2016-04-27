package furgl.babyMobs.common.event;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import furgl.babyMobs.common.block.BlockDisappearingWeb;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSword;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class BreakSpeedEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerEvent.BreakSpeed event)
	{
		if (event.block instanceof BlockDisappearingWeb)
		{
			if (event.entityPlayer.getHeldItem() != null && (event.entityPlayer.getHeldItem().getItem() instanceof ItemSword || event.entityPlayer.getHeldItem().getItem() instanceof ItemShears))
				event.newSpeed = event.originalSpeed*25;
		}
	}
}
