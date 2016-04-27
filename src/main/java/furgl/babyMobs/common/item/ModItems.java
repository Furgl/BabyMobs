package furgl.babyMobs.common.item;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.item.projectile.ItemCaveSpiderVenom;
import furgl.babyMobs.common.item.projectile.ItemCreeperExplosion;
import furgl.babyMobs.common.item.projectile.ItemInvisible;
import furgl.babyMobs.common.item.spawnEgg.ItemBabySpawnEgg;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModItems 
{
	public static Item baby_spider_egg;
	public static Item baby_skeleton_egg;
	public static Item baby_creeper_egg;
	public static Item baby_wither_skeleton_egg;
	public static Item baby_enderman_egg;
	public static Item baby_blaze_egg;
	public static Item baby_witch_egg;
	public static Item baby_guardian_egg;
	public static Item baby_squid_egg;
	public static Item baby_cave_spider_egg;
	public static Item baby_zombie_egg;
	public static Item baby_pig_zombie_egg;
	public static Item baby_ghast_egg;
	public static Item baby_snowman_egg;
	public static Item baby_iron_golem_egg;
	public static Item baby_wither_egg;

	public static Item invisible;
	public static Item cave_spider_venom;
	public static Item creeper_explosion;

	public static void init() 
	{
		baby_spider_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babySpider"), "baby_spider_egg");
		baby_skeleton_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babySkeleton"), "baby_skeleton_egg");
		baby_creeper_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyCreeper"), "baby_creeper_egg");
		baby_wither_skeleton_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyWitherSkeleton"), "baby_wither_skeleton_egg");
		baby_enderman_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyEnderman"), "baby_enderman_egg");
		baby_blaze_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyBlaze"), "baby_blaze_egg");
		baby_witch_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyWitch"), "baby_witch_egg");
		baby_guardian_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyGuardian"), "baby_guardian_egg");
		baby_squid_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babySquid"), "baby_squid_egg");
		baby_cave_spider_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyCaveSpider"), "baby_cave_spider_egg");
		baby_zombie_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyZombie"), "baby_zombie_egg");
		baby_pig_zombie_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyPigZombie"), "baby_pig_zombie_egg");
		baby_ghast_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyGhast"), "baby_ghast_egg");
		baby_snowman_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babySnowman"), "baby_snowman_egg");
		baby_iron_golem_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyIronGolem"), "baby_iron_golem_egg");
		baby_wither_egg = registerItemWithTab(new ItemBabySpawnEgg("babymobs.babyWither"), "baby_wither_egg");

		invisible = registerItemWithoutTab(new ItemInvisible(), "invisible");
		cave_spider_venom = registerItemWithoutTab(new ItemCaveSpiderVenom(), "cave_spider_venom");
		creeper_explosion = registerItemWithoutTab(new ItemCreeperExplosion(), "creeper_explosion");
	}

	public static void registerRenders()
	{
		registerRender(baby_spider_egg);
		registerRender(baby_skeleton_egg);
		registerRender(baby_creeper_egg);
		registerRender(baby_wither_skeleton_egg);
		registerRender(baby_enderman_egg);
		registerRender(baby_blaze_egg);
		registerRender(baby_witch_egg);
		registerRender(baby_guardian_egg);
		registerRender(baby_squid_egg);
		registerRender(baby_cave_spider_egg);
		registerRender(baby_zombie_egg);
		registerRender(baby_pig_zombie_egg);
		registerRender(baby_ghast_egg);
		registerRender(baby_snowman_egg);
		registerRender(baby_iron_golem_egg);
		registerRender(baby_wither_egg);

		registerRender(invisible);
		registerRender(cave_spider_venom);
		registerRender(creeper_explosion);
	}

	public static Item registerItemWithTab(final Item item, final String unlocalizedName) {
		item.setUnlocalizedName(unlocalizedName);
		item.setCreativeTab(BabyMobs.tab);
		GameRegistry.registerItem(item, unlocalizedName);
		return item;
	}

	public static Item registerItemWithoutTab(final Item item, final String unlocalizedName) {
		item.setUnlocalizedName(unlocalizedName);
		GameRegistry.registerItem(item, unlocalizedName);
		return item;
	}

	public static void registerRender(Item item)
	{	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("babymobs:" + item.getUnlocalizedName().substring(5), "inventory"));
	}
}

