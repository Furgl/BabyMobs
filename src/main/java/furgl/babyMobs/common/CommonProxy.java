package furgl.babyMobs.common;


import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyDragon;
import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import furgl.babyMobs.common.entity.projectile.EntityGhastFireball;
import furgl.babyMobs.common.entity.projectile.EntityWitherSkeletonSmoke;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.stats.Achievement;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.AchievementPage;

public class CommonProxy 
{	
	public void preInit() {}

	public void init() {
		registerAchievements();
	}

	public void postInit() {}
		
	private void registerAchievements() {
		AchievementPage.registerAchievementPage(new AchievementPage("Baby Mobs", (Achievement[]) Achievements.achievements.toArray(new Achievement[Achievements.achievements.size()])));
		
		for (int i=0; i<Achievements.achievements.size(); i++)
			Achievements.achievements.get(i).registerStat();
	}
	
	public Class getParticleClass() {return null;}
	
	public void spawnEntitySpawner(Class entityClass, World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator) { }

	public void spawnEntitySkeletonEffectFX(World world, EntityBabySkeleton entityBabySkeleton, float red, float green, float blue) { }
	
	public void spawnEntityBlazeFlamethrowerFX(EntityBabyBlaze entityBabyBlaze, Vec3d vec) { }
	
	public void spawnEntityBlazeFlamethrowerFX(EntityGhastFireball entityGhastFireball) { }

	public void spawnEntitySquidInkFX(EntityWitherSkeletonSmoke entitySquidInk) { }

	public void spawnEntityDragonParticlesFX(EntityBabyDragon entityBabyDragon) { }

	public void spawnEntitySquidInkFX(World world, double x, double y, double z, int motionX, int motionY, int motionZ) { }
}
