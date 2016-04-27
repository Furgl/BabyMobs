package furgl.babyMobs.common.event;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import furgl.babyMobs.util.EntitySpawner;

public class OnUpdateEvent 
{
	private static List addOnClientUpdateList = new ArrayList();
	private static List removeOnClientUpdateList = new ArrayList();
	private static List addOnServerUpdateList = new ArrayList();
	private static List removeOnServerUpdateList = new ArrayList();

	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(TickEvent.ClientTickEvent event)
	{
		for (int i=0; i<removeOnClientUpdateList.size(); i++)
		{
			addOnClientUpdateList.remove(removeOnClientUpdateList.get(i));
			removeOnClientUpdateList.remove(i);
		}
		for (int i=0; i<addOnClientUpdateList.size(); i++)
		{
			if (addOnClientUpdateList.get(i) != null)
				((EntitySpawner) addOnClientUpdateList.get(i)).onUpdate();
		}
	}
	
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(TickEvent.ServerTickEvent event)
	{
		for (int i=0; i<removeOnServerUpdateList.size(); i++)
		{
			addOnServerUpdateList.remove(removeOnServerUpdateList.get(i));
			removeOnServerUpdateList.remove(i);
		}
		for (int i=0; i<addOnServerUpdateList.size(); i++)
		{
			if (addOnServerUpdateList.get(i) != null)
				((EntitySpawner) addOnServerUpdateList.get(i)).onUpdate();
		}
	}
	
	/**Add spawner to list of things to update*/
	public static void addOnClientUpdate(EntitySpawner spawner)
	{
		addOnClientUpdateList.add(spawner);
	}
	
	/**Add spawner to list of things to update*/
	public static void addOnServerUpdate(EntitySpawner spawner)
	{
		addOnServerUpdateList.add(spawner);
	}

	/**Remove from list of things to update at next update*/
	public static void removeOnClientUpdate(EntitySpawner spawner)
	{
		removeOnClientUpdateList.add(spawner);
	}
	
	/**Remove from list of things to update at next update*/
	public static void removeOnServerUpdate(EntitySpawner spawner)
	{
		removeOnServerUpdateList.add(spawner);
	}
}
