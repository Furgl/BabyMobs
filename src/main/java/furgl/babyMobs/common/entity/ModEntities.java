package furgl.babyMobs.common.entity;

import cpw.mods.fml.common.registry.EntityRegistry;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyCaveSpider;
import furgl.babyMobs.common.entity.monster.EntityBabyCreeper;
import furgl.babyMobs.common.entity.monster.EntityBabyDragon;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import furgl.babyMobs.common.entity.monster.EntityBabyGhast;
import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityBabyOcelot;
import furgl.babyMobs.common.entity.monster.EntityBabyPigZombie;
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
import furgl.babyMobs.common.entity.projectile.EntityBlazeFlamethrower;
import furgl.babyMobs.common.entity.projectile.EntityCaveSpiderVenom;
import furgl.babyMobs.common.entity.projectile.EntityCreeperExplosion;
import furgl.babyMobs.common.entity.projectile.EntityGhastFireball;
import furgl.babyMobs.common.entity.projectile.EntitySkeletonArrow;
import furgl.babyMobs.common.entity.projectile.EntitySnowmanSnowball;
import furgl.babyMobs.common.entity.projectile.EntitySquidInk;
import furgl.babyMobs.common.entity.projectile.EntityWitherExplosion;
import furgl.babyMobs.common.entity.projectile.EntityWitherSkeletonSmoke;
import furgl.babyMobs.common.entity.projectile.EntityWitherWitherSkull;

public class ModEntities 
{
    public static void registerEntities() 
    {
        int ID = 0;
        EntityRegistry.registerModEntity(EntityBabySpider.class, "babySpider", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabySkeleton.class, "babySkeleton", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyCreeper.class, "babyCreeper", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyWitherSkeleton.class, "babyWitherSkeleton", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyEnderman.class, "babyEnderman", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyBlaze.class, "babyBlaze", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyWitch.class, "babyWitch", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabySquid.class, "babySquid", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyCaveSpider.class, "babyCaveSpider", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyZombie.class, "babyZombie", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyPigZombie.class, "babyPigZombie", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyGhast.class, "babyGhast", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabySnowman.class, "babySnowman", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyIronGolem.class, "babyIronGolem", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyWither.class, "babyWither", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyDragon.class, "babyDragon", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBabyOcelot.class, "babyOcelot", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityZombieChicken.class, "zombieChicken", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityZombiePig.class, "zombiePig", ID++, BabyMobs.instance, 64, 3, true);

		EntityRegistry.registerModEntity(EntityCaveSpiderVenom.class, "caveSpiderVenom", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntitySnowmanSnowball.class, "snowmanSnowball", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityBlazeFlamethrower.class, "blazeFlamethrower", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityCreeperExplosion.class, "creeperExplosion", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityWitherSkeletonSmoke.class, "witherSkeletonSmoke", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntitySquidInk.class, "squidInk", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityGhastFireball.class, "ghastFireball", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityWitherWitherSkull.class, "witherWitherSkull", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntitySkeletonArrow.class, "skeletonArrow", ID++, BabyMobs.instance, 64, 3, true);
		EntityRegistry.registerModEntity(EntityWitherExplosion.class, "witherExplosion", ID++, BabyMobs.instance, 64, 3, true);
    }
}
