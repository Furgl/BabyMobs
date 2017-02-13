package furgl.babyMobs.common.item;

import java.util.ArrayList;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.item.projectile.ItemCaveSpiderVenom;
import furgl.babyMobs.common.item.projectile.ItemCreeperExplosion;
import furgl.babyMobs.common.item.projectile.ItemInvisible;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems 
{
	public static Item invisible;
	public static Item cave_spider_venom;
	public static Item creeper_explosion;

	public static Item golden_bread;

	public static ArrayList<Item> allItems;

	public static void init() {
		allItems = new ArrayList<Item>();
		
		invisible = registerItem(new ItemInvisible(), "invisible", false);
		cave_spider_venom = registerItem(new ItemCaveSpiderVenom(), "cave_spider_venom", false);
		creeper_explosion = registerItem(new ItemCreeperExplosion(), "creeper_explosion", false);

		golden_bread = registerItem(new ItemGoldenBread(10, 1.2F, false), "golden_bread", true);
	}

	public static void registerRenders() {
		for (Item item : allItems)
			registerRender(item);
	}

	private static Item registerItem(final Item item, final String unlocalizedName, boolean addToTab) {
		allItems.add(item);
		item.setUnlocalizedName(unlocalizedName);
		if (addToTab)
			item.setCreativeTab(BabyMobs.tab);
		GameRegistry.register(item.setRegistryName(unlocalizedName));
		return item;
	}

	private static void registerRender(Item item) {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(BabyMobs.MODID+":" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}