package furgl.babyMobs.common.entity;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyCaveSpider;
import furgl.babyMobs.common.entity.monster.EntityBabyCreeper;
import furgl.babyMobs.common.entity.monster.EntityBabyDragon;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import furgl.babyMobs.common.entity.monster.EntityBabyGhast;
import furgl.babyMobs.common.entity.monster.EntityBabyGuardian;
import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityBabyOcelot;
import furgl.babyMobs.common.entity.monster.EntityBabyPigZombie;
import furgl.babyMobs.common.entity.monster.EntityBabyShulker;
import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabySnowman;
import furgl.babyMobs.common.entity.monster.EntityBabySpider;
import furgl.babyMobs.common.entity.monster.EntityBabySquid;
import furgl.babyMobs.common.entity.monster.EntityBabyWitch;
import furgl.babyMobs.common.entity.monster.EntityBabyWither;
import furgl.babyMobs.common.entity.monster.EntityBabyWitherSkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabyZombie;
import furgl.babyMobs.common.entity.monster.EntityZombieChicken;
import furgl.babyMobs.common.entity.monster.EntityZombiePig;
import furgl.babyMobs.common.entity.projectile.EntityBabyShulkerBullet;
import furgl.babyMobs.common.entity.projectile.EntityBlazeFlamethrower;
import furgl.babyMobs.common.entity.projectile.EntityCaveSpiderVenom;
import furgl.babyMobs.common.entity.projectile.EntityCreeperExplosion;
import furgl.babyMobs.common.entity.projectile.EntitySnowmanSnowball;
import furgl.babyMobs.common.entity.projectile.EntitySquidInk;
import furgl.babyMobs.common.entity.projectile.EntityWitherExplosion;
import furgl.babyMobs.common.entity.projectile.EntityWitherSkeletonSmoke;
import furgl.babyMobs.common.entity.projectile.EntityWitherWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class ModEntities 
{
	private static int id;
	
    public static void registerEntities() 
    {	//primary and secondary colors copied from EntityList
    	registerEntityWithSpawnEgg(EntityBabySpider.class, 3419431, 11013646);
    	registerEntityWithSpawnEgg(EntityBabySkeleton.class, 12698049, 4802889);
    	registerEntityWithSpawnEgg(EntityBabyCreeper.class, 894731, 0);
    	registerEntityWithSpawnEgg(EntityBabyWitherSkeleton.class, 1315860, 4672845);
    	registerEntityWithSpawnEgg(EntityBabyEnderman.class, 1447446, 0);
    	registerEntityWithSpawnEgg(EntityBabyBlaze.class, 16167425, 16775294);
    	registerEntityWithSpawnEgg(EntityBabyWitch.class, 3407872, 5349438);
    	registerEntityWithSpawnEgg(EntityBabyGuardian.class, 5931634, 15826224);
    	registerEntityWithSpawnEgg(EntityBabySquid.class, 2243405, 7375001);
    	registerEntityWithSpawnEgg(EntityBabyCaveSpider.class, 803406, 11013646);
    	registerEntityWithSpawnEgg(EntityBabyZombie.class, 44975, 7969893);
    	registerEntityWithSpawnEgg(EntityBabyPigZombie.class, 15373203, 5009705);
    	registerEntityWithSpawnEgg(EntityBabyGhast.class, 16382457, 12369084);
    	registerEntityWithSpawnEgg(EntityBabySnowman.class, 16382457, 15826224);
    	registerEntityWithSpawnEgg(EntityBabyIronGolem.class, 16382457, 7969893);
    	registerEntityWithSpawnEgg(EntityBabyWither.class, 0, 12369084);
    	registerEntityWithSpawnEgg(EntityBabyShulker.class, 9725844, 5060690);
    	
    	registerEntity(EntityBabyDragon.class);
    	registerEntity(EntityBabyOcelot.class);
    	registerEntity(EntityZombieChicken.class);
    	registerEntity(EntityZombiePig.class);
    	
    	registerEntity(EntityCaveSpiderVenom.class);
    	registerEntity(EntitySnowmanSnowball.class);
    	registerEntity(EntityBlazeFlamethrower.class);
    	registerEntity(EntityCreeperExplosion.class);
    	registerEntity(EntityWitherSkeletonSmoke.class);
    	registerEntity(EntitySquidInk.class);
    	registerEntity(EntityWitherWitherSkull.class);
    	registerEntity(EntityWitherExplosion.class);
    	registerEntity(EntityBabyShulkerBullet.class);
    }
    
    /**Registers entity to unlocalizedName based on entity class (i.e. EntityBabySpider = babySpider)*/
    private static void registerEntity(Class clazz) {
    	String unlocalizedName = clazz.getSimpleName().replace("Entity", "").toLowerCase();   	
    	ResourceLocation registryName = new ResourceLocation(BabyMobs.MODID, unlocalizedName);
        EntityRegistry.registerModEntity(registryName, clazz, unlocalizedName, id++, BabyMobs.instance, 64, 3, true);
    }
    
    /**Registers entity to unlocalizedName based on entity class (i.e. EntityBabySpider = babySpider) and gives it a spawn egg*/
    private static void registerEntityWithSpawnEgg(Class clazz, int primary, int secondary) {
    	String unlocalizedName = clazz.getSimpleName().replace("Entity", "").toLowerCase();   	
    	ResourceLocation registryName = new ResourceLocation(BabyMobs.MODID, unlocalizedName);
    	EntityRegistry.registerModEntity(registryName, clazz, unlocalizedName, id++, BabyMobs.instance, 64, 3, true, primary, secondary);
    	BabyMobs.tab.eggs.add(getSpawnEgg(clazz));
    }

    /**Get spawn egg for given baby mob entity class*/
	public static ItemStack getSpawnEgg(Class clazz) {
		ItemStack stack = new ItemStack(Items.SPAWN_EGG);
		NBTTagCompound nbt = new NBTTagCompound();    	
		String unlocalizedName = clazz.getSimpleName().replace("Entity", "").toLowerCase();   	
		nbt.setString("id", BabyMobs.MODID+":"+unlocalizedName);
		NBTTagCompound nbt2 = new NBTTagCompound();
		nbt2.setTag("EntityTag", nbt);
		stack.setTagCompound(nbt2);
		return stack;
	}
}
