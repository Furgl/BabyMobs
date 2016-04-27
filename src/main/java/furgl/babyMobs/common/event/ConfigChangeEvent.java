package furgl.babyMobs.common.event;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigChangeEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) 
	{
		if (event.modID.equals(BabyMobs.MODID)) 
			Config.syncConfig();
	}
}
