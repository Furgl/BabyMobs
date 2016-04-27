package furgl.babyMobs.client;

import java.lang.reflect.InvocationTargetException;

import cpw.mods.fml.client.registry.RenderingRegistry;
import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.client.model.ModelBabyOcelot;
import furgl.babyMobs.client.model.ModelBabySquid;
import furgl.babyMobs.client.model.ModelZombieChicken;
import furgl.babyMobs.client.model.ModelZombiePig;
import furgl.babyMobs.client.particle.EntityBlazeFlamethrowerFX;
import furgl.babyMobs.client.particle.EntityDragonParticlesFX;
import furgl.babyMobs.client.particle.EntitySkeletonEffectFX;
import furgl.babyMobs.client.particle.EntitySquidInkFX;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyBlaze;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyCaveSpider;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyCreeper;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyDragon;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyEnderman;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyGhast;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyIronGolem;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyOcelot;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyPigZombie;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabySkeleton;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabySnowman;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabySpider;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabySquid;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyWitch;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyWither;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyWitherSkeleton;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyZombie;
import furgl.babyMobs.client.renderer.entity.mob.RenderZombieChicken;
import furgl.babyMobs.client.renderer.entity.mob.RenderZombiePig;
import furgl.babyMobs.common.CommonProxy;
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
import furgl.babyMobs.common.entity.projectile.EntityWitherSkeletonSmoke;
import furgl.babyMobs.common.entity.projectile.EntityWitherExplosion;
import furgl.babyMobs.common.entity.projectile.EntityWitherWitherSkull;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.tileentity.RenderWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.stats.Achievement;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.AchievementPage;

public class ClientProxy extends CommonProxy
{	
	@Override
	public void registerRenders()
	{
		registerEntityRenders();
	}

	@Override
	public void registerAchievements()
	{
		AchievementPage.registerAchievementPage(new AchievementPage("Baby Mobs", (Achievement[]) Achievements.achievements.toArray(new Achievement[Achievements.achievements.size()])));
		
		for (int i=0; i<Achievements.achievements.size(); i++)
			Achievements.achievements.get(i).registerStat();
	}
	
	@Override
	public Class getEntityFXClass()
	{
		return EntityFX.class;
	}
	
	@Override
	public void spawnEntitySpawner(Class entityClass, World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator) 
	{ 
		try 
		{
			EntityFX particle = (EntityFX) entityClass.getConstructor(World.class, double.class, double.class, double.class, EntitySpawner.class, int.class, int.class).newInstance(world, x, y, z, spawner, heightIterator, entityIterator);
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException | NoSuchMethodException | SecurityException e) 
		{
			System.out.println("ERROR: ENTITYFX " + entityClass + " MISSING SPAWNER CONSTRUCTOR");
			e.printStackTrace();
		}
	}
	
	@Override
	public void spawnEntitySkeletonEffectFX(World world, EntityBabySkeleton entityBabySkeleton, float red, float green, float blue) 
	{
		EntitySkeletonEffectFX effect = new EntitySkeletonEffectFX(world, entityBabySkeleton, red, green, blue);
		Minecraft.getMinecraft().effectRenderer.addEffect(effect);
	}
	
	@Override
	public void spawnEntityBlazeFlamethrowerFX(EntityBabyBlaze entityBabyBlaze, Vec3 vec) 
	{ 
		EntitySpawner entityFXSpawner = new EntitySpawner(EntityBlazeFlamethrowerFX.class, entityBabyBlaze.worldObj, vec, 8);
		entityFXSpawner.setMovementFollowShape(0.1D);
		entityFXSpawner.setShapeLine(5D, entityBabyBlaze.rotationPitch, entityBabyBlaze.rotationYawHead);
		entityFXSpawner.setRandVar(0.2D);
		entityFXSpawner.run();
	}
	
	@Override
	public void spawnEntityBlazeFlamethrowerFX(EntityGhastFireball entityGhastFireball) 
	{ 
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntityBlazeFlamethrowerFX(entityGhastFireball.worldObj, entityGhastFireball.posX+(entityGhastFireball.worldObj.rand.nextDouble()-0.5D), entityGhastFireball.posY+(entityGhastFireball.worldObj.rand.nextDouble()-0.5D), entityGhastFireball.posZ+(entityGhastFireball.worldObj.rand.nextDouble()-0.5D), entityGhastFireball.motionX, entityGhastFireball.motionY, entityGhastFireball.motionZ));
	}
	
	@Override
	public void spawnEntitySquidInkFX(EntityWitherSkeletonSmoke entitySquidInk) 
	{ 
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySquidInkFX(entitySquidInk.worldObj, entitySquidInk.posX + (entitySquidInk.worldObj.rand.nextDouble()-0.5D)*0.5, entitySquidInk.posY + (entitySquidInk.worldObj.rand.nextDouble()-0.5D)*0.5, entitySquidInk.posZ + (entitySquidInk.worldObj.rand.nextDouble()-0.5D)*0.5, entitySquidInk.motionX/2, entitySquidInk.motionY/2, entitySquidInk.motionZ/2));
	}
	
	@Override
	public void spawnEntitySquidInkFX(World worldObj, double x, double y, double z, int motionX, int motionY, int motionZ)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySquidInkFX(worldObj, x, y, z, motionX, motionY, motionZ));
	}
	
	@Override
	public void spawnEntityDragonParticlesFX(EntityBabyDragon entityBabyDragon) 
	{ 
		EntitySpawner spawner = new EntitySpawner(EntityDragonParticlesFX.class, entityBabyDragon.worldObj, Vec3.createVectorHelper(entityBabyDragon.dragonEgg.xCoord+0.5D, entityBabyDragon.dragonEgg.yCoord, entityBabyDragon.dragonEgg.zCoord+0.5D), 10);
		spawner.setShapeCircle(0.8D);
		spawner.setMovementFollowShape(0.1D);
		spawner.setUpdateTick(10);
		spawner.run();
	}

	private void registerEntityRenders() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySpider.class, new RenderBabySpider());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySkeleton.class, new RenderBabySkeleton());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyCreeper.class, new RenderBabyCreeper());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyWitherSkeleton.class, new RenderBabyWitherSkeleton());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyEnderman.class, new RenderBabyEnderman());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyBlaze.class, new RenderBabyBlaze());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyWitch.class, new RenderBabyWitch());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySquid.class, new RenderBabySquid(new ModelBabySquid(), 0.4F));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyCaveSpider.class, new RenderBabyCaveSpider());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyZombie.class, new RenderBabyZombie());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyPigZombie.class, new RenderBabyPigZombie());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyGhast.class, new RenderBabyGhast());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySnowman.class, new RenderBabySnowman());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyIronGolem.class, new RenderBabyIronGolem());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyWither.class, new RenderBabyWither());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyDragon.class, new RenderBabyDragon());
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyOcelot.class, new RenderBabyOcelot(new ModelBabyOcelot(), 0.2F));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieChicken.class, new RenderZombieChicken(new ModelZombieChicken(), 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombiePig.class, new RenderZombiePig(new ModelZombiePig(), new ModelZombiePig(0.5F), 0));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityCaveSpiderVenom.class, new RenderSnowball(ModItems.cave_spider_venom, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowmanSnowball.class, new RenderSnowball(Items.snowball, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeFlamethrower.class, new RenderSnowball(ModItems.invisible, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityCreeperExplosion.class, new RenderSnowball(ModItems.creeper_explosion, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityWitherSkeletonSmoke.class, new RenderSnowball(ModItems.invisible, 0));
		RenderingRegistry.registerEntityRenderingHandler(EntityGhastFireball.class, new RenderFireball(2.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityWitherWitherSkull.class, new RenderWitherSkull());
		RenderingRegistry.registerEntityRenderingHandler(EntitySkeletonArrow.class, new RenderArrow());
		RenderingRegistry.registerEntityRenderingHandler(EntityWitherExplosion.class, new RenderSnowball(ModItems.invisible, 0));
	}
}
