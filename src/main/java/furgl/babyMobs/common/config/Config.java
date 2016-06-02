package furgl.babyMobs.common.config;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class Config 
{
	public static Configuration config;
	public static int babyZombieRate;
	public static int babyPigZombieRate;
	public static int babySpiderRate;
	public static int babySpiderJockeyRate;
	public static int babySkeletonRate;
	public static int babyCreeperRate;
	public static int babyWitherSkeletonRate;
	public static int babyEndermanRate;
	public static int babyEndermanEndRate;
	public static int babyBlazeRate;
	public static int babyWitchRate;
	public static int babyGuardianRate;
	public static int babySquidRate;
	public static int babyCaveSpiderRate;
	public static int babyGhastRate;
	public static int babyIronGolemRate;
	public static int babyShulkerRate;
	public static int babySkeletonHorseRate;
	public static int babyZombieHorseRate;
	public static boolean useSpecialAbilities;

	public static void init(final File file)
	{
		Config.config = new Configuration(file);
		Config.config.addCustomCategoryComment("spawn rates", "Percentage of adult monsters that will be converted to babies when they spawn");
		Config.config.load();
		Config.syncConfig();
		Config.config.save();
	}
	
	public static void syncConfig() 
	{
		Property useSpecialAbilitiesProp = Config.config.get(Configuration.CATEGORY_GENERAL, "Use Special Abilities", true);
		useSpecialAbilitiesProp.setComment("Do baby mobs use their special abilities?");
		Config.useSpecialAbilities = useSpecialAbilitiesProp.getBoolean(true);
		Property babyZombieRateProp = Config.config.get("spawn rates", "Baby Zombie", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyZombieRate = babyZombieRateProp.getInt();
		Property babyPigZombieRateProp = Config.config.get("spawn rates", "Baby Pig Zombie", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyPigZombieRate = babyPigZombieRateProp.getInt();
		Property babySpiderRateProp = Config.config.get("spawn rates", "Baby Spider", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babySpiderRate = babySpiderRateProp.getInt();
		Property babySpiderJockeyRateProp = Config.config.get("spawn rates", "Baby Spider Jockey", 10, "% of Baby Spiders that will spawn as this", 0, 100);
		Config.babySpiderJockeyRate = babySpiderJockeyRateProp.getInt();
		Property babySkeletonRateProp = Config.config.get("spawn rates", "Baby Skeleton", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babySkeletonRate = babySkeletonRateProp.getInt();
		Property babyCreeperRateProp = Config.config.get("spawn rates", "Baby Creeper", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyCreeperRate = babyCreeperRateProp.getInt();
		Property babyWitherSkeletonRateProp = Config.config.get("spawn rates", "Baby Wither Skeleton", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyWitherSkeletonRate = babyWitherSkeletonRateProp.getInt();
		Property babyEndermanRateProp = Config.config.get("spawn rates", "Baby Enderman in Overworld", 25, "% of adult mobs that will spawn as babies in the Overworld", 0, 100);
		Config.babyEndermanRate = babyEndermanRateProp.getInt();
		Property babyEndermanEndRateProp = Config.config.get("spawn rates", "Baby Enderman in End", 5, "% of adult mobs that will spawn as babies in the End", 0, 100);
		Config.babyEndermanEndRate = babyEndermanEndRateProp.getInt();
		Property babyBlazeRateProp = Config.config.get("spawn rates", "Baby Blaze", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyBlazeRate = babyBlazeRateProp.getInt();
		Property babyGuardianRateProp = Config.config.get("spawn rates", "Baby Guardian", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyGuardianRate = babyGuardianRateProp.getInt();
		Property babySquidRateProp = Config.config.get("spawn rates", "Baby Squid", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babySquidRate = babySquidRateProp.getInt();
		Property babyCaveSpiderRateProp = Config.config.get("spawn rates", "Baby Cave Spider", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyCaveSpiderRate = babyCaveSpiderRateProp.getInt();
		Property babyGhastRateProp = Config.config.get("spawn rates", "Baby Ghast", 50, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyGhastRate = babyGhastRateProp.getInt();
		Property babyIronGolemRateProp = Config.config.get("spawn rates", "Baby Iron Golem", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyIronGolemRate = babyIronGolemRateProp.getInt();
		Property babyWitchRateProp = Config.config.get("spawn rates", "Baby Witch", 50, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyWitchRate = babyWitchRateProp.getInt();
		Property babyShulkerRateProp = Config.config.get("spawn rates", "Baby Shulker", 50, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyShulkerRate = babyShulkerRateProp.getInt();
		Property babySkeletonHorseRateProp = Config.config.get("spawn rates", "Baby Skeleton Horse", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babySkeletonHorseRate = babySkeletonHorseRateProp.getInt();
		Property babyZombieHorseRateProp = Config.config.get("spawn rates", "Baby Zombie Horse", 25, "% of adult mobs that will spawn as babies", 0, 100);
		Config.babyZombieHorseRate = babyZombieHorseRateProp.getInt();
		Config.config.save();
	}
}
