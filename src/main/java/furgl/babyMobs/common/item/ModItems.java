package furgl.babyMobs.common.item;

import java.util.ArrayList;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.item.projectile.ItemCaveSpiderVenom;
import furgl.babyMobs.common.item.projectile.ItemCreeperExplosion;
import furgl.babyMobs.common.item.projectile.ItemInvisible;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModItems 
{
	public static Item INVISIBLE  = new ItemInvisible();
	public static Item CAVE_SPIDER_VENOM = new ItemCaveSpiderVenom();
	public static Item CREEPER_EXPLOSION = new ItemCreeperExplosion();
	public static Item GOLDEN_BREAD = new ItemGoldenBread(10, 1.2F, false);

	public static ArrayList<Item> allItems = new ArrayList<Item>();

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerItems(final RegistryEvent.Register<Item> event) {
			register(event.getRegistry(), INVISIBLE, "invisible", false);
			register(event.getRegistry(), CAVE_SPIDER_VENOM, "cave_spider_venom", false);
			register(event.getRegistry(), CREEPER_EXPLOSION, "creeper_explosion", false);
			register(event.getRegistry(), GOLDEN_BREAD, "golden_bread", true);
		}

		private static void register(IForgeRegistry<Item> registry, Item item, String itemName, boolean addToTab) {
			allItems.add(item);
			item.setRegistryName(BabyMobs.MODID, itemName);
			item.setUnlocalizedName(item.getRegistryName().getResourcePath());
			if (addToTab)
				item.setCreativeTab(BabyMobs.tab);
			registry.register(item);
		}

	}

	public static void registerRenders() {
		for (Item item : allItems)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register
			(item, 0, new ModelResourceLocation(BabyMobs.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}