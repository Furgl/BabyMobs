package furgl.babyMobs.common;


import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyDragon;
import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import furgl.babyMobs.common.entity.projectile.EntityGhastFireball;
import furgl.babyMobs.common.entity.projectile.EntityWitherSkeletonSmoke;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class CommonProxy 
{	
	public void registerRenders() { }
	
	public void registerAchievements() { }
	
	public Class getEntityFXClass() {return null;}
	
	public void spawnEntitySpawner(Class entityClass, World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator) { }

	public void spawnEntitySkeletonEffectFX(World world, EntityBabySkeleton entityBabySkeleton, float red, float green, float blue) { }
	
	public void spawnEntityBlazeFlamethrowerFX(EntityBabyBlaze entityBabyBlaze, Vec3d vec) { }
	
	public void spawnEntityBlazeFlamethrowerFX(EntityGhastFireball entityGhastFireball) { }

	public void spawnEntitySquidInkFX(EntityWitherSkeletonSmoke entitySquidInk) { }

	public void spawnEntityDragonParticlesFX(EntityBabyDragon entityBabyDragon) { }

	public void spawnEntitySquidInkFX(World worldObj, double x, double y, double z, int motionX, int motionY, int motionZ) { }
}
