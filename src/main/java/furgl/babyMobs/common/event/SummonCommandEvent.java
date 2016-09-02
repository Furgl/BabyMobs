package furgl.babyMobs.common.event;

import net.minecraft.command.server.CommandSummon;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class SummonCommandEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(CommandEvent event)
	{
		if (event.getCommand().getClass() == CommandSummon.class && event.getParameters()[0].equals("babymobs.babyDragon"))
		{
			TextComponentString component = new TextComponentString("A Baby Dragon can only be summoned by placing a Dragon Egg.");
			component.getStyle().setColor(TextFormatting.RED);
			event.getSender().addChatMessage(component);
			event.setCanceled(true);
		}
	}
}
