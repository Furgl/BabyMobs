package furgl.babyMobs.common.event;

import net.minecraft.command.server.CommandSummon;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SummonCommandEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(CommandEvent event)
	{
		if (event.command.getClass() == CommandSummon.class && event.parameters[0].equals("babymobs.babyDragon"))
		{
			event.sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "A Baby Dragon can only be summoned by placing a Dragon Egg."));
			event.setCanceled(true);
		}
	}
}
