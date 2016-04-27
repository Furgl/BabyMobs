package furgl.babyMobs.common.event;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;

public class ConfigChangeEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onConfigChanged(final ConfigChangedEvent.OnConfigChangedEvent event) 
	{
		if (event.modID.equals(BabyMobs.MODID)) 
			Config.syncConfig();
	}
}
