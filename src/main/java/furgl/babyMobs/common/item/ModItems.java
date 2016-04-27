package furgl.babyMobs.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.item.projectile.ItemCaveSpiderVenom;
import furgl.babyMobs.common.item.projectile.ItemCreeperExplosion;
import furgl.babyMobs.common.item.projectile.ItemInvisible;
import furgl.babyMobs.common.item.spawnEgg.BabySpawnEgg;
import net.minecraft.item.Item;

public class ModItems 
{
	public static Item baby_spider_egg;
	public static Item baby_skeleton_egg;
	public static Item baby_creeper_egg;
	public static Item baby_wither_skeleton_egg;
	public static Item baby_enderman_egg;
	public static Item baby_blaze_egg;
	public static Item baby_witch_egg;
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
		baby_spider_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babySpider"), "baby_spider_egg");
		baby_skeleton_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babySkeleton"), "baby_skeleton_egg");
		baby_creeper_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyCreeper"), "baby_creeper_egg");
		baby_wither_skeleton_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyWitherSkeleton"), "baby_wither_skeleton_egg");
		baby_enderman_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyEnderman"), "baby_enderman_egg");
		baby_blaze_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyBlaze"), "baby_blaze_egg");
		baby_witch_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyWitch"), "baby_witch_egg");
		baby_squid_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babySquid"), "baby_squid_egg");
		baby_cave_spider_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyCaveSpider"), "baby_cave_spider_egg");
		baby_zombie_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyZombie"), "baby_zombie_egg");
		baby_pig_zombie_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyPigZombie"), "baby_pig_zombie_egg");
		baby_ghast_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyGhast"), "baby_ghast_egg");
		baby_snowman_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babySnowman"), "baby_snowman_egg");
		baby_iron_golem_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyIronGolem"), "baby_iron_golem_egg");
		baby_wither_egg = registerItemWithTab(new BabySpawnEgg("babymobs.babyWither"), "baby_wither_egg");

		invisible = registerItemWithoutTab(new ItemInvisible(), "invisible");
		cave_spider_venom = registerItemWithoutTab(new ItemCaveSpiderVenom(), "cave_spider_venom");
		creeper_explosion = registerItemWithoutTab(new ItemCreeperExplosion(), "creeper_explosion");
	}

    public static Item registerItemWithTab(final Item item, final String unlocalizedName) {
        item.setUnlocalizedName(unlocalizedName);
        item.setCreativeTab(BabyMobs.tab);
        item.setTextureName("babymobs:" + item.getUnlocalizedName().substring(5));
        GameRegistry.registerItem(item, unlocalizedName);
        return item;
    }
    
    public static Item registerItemWithoutTab(final Item item, final String unlocalizedName) {
        item.setUnlocalizedName(unlocalizedName);
        item.setTextureName("babymobs:" + item.getUnlocalizedName().substring(5));
        GameRegistry.registerItem(item, unlocalizedName);
        return item;
    }
}

