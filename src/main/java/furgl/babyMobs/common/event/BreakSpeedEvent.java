package furgl.babyMobs.common.event;

import furgl.babyMobs.common.block.BlockDisappearingWeb;
import net.minecraft.item.ItemShears;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumHand;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BreakSpeedEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(PlayerEvent.BreakSpeed event)
	{
		if (event.getState().getBlock() instanceof BlockDisappearingWeb)
		{
			if (event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND) != null && (event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword || event.getEntityPlayer().getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemShears))
				event.setNewSpeed(event.getOriginalSpeed()*35);
		}
	}
}
