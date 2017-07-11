package furgl.babyMobs.client;

import furgl.babyMobs.client.particle.EntityBlazeFlamethrowerFX;
import furgl.babyMobs.client.particle.EntityCustomParticle;
import furgl.babyMobs.client.particle.EntityDragonParticlesFX;
import furgl.babyMobs.client.particle.EntitySkeletonEffectFX;
import furgl.babyMobs.client.particle.EntitySquidInkFX;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyBlaze;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyCaveSpider;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyCreeper;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyDragon;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyEnderman;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyGhast;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyGuardian;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyIronGolem;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyOcelot;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyShulker;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabySkeleton;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabySnowMan;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabySpider;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabySquid;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyWitch;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyWither;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyWitherSkeleton;
import furgl.babyMobs.client.renderer.entity.mob.RenderZombieChicken;
import furgl.babyMobs.client.renderer.entity.mob.RenderZombiePig;
import furgl.babyMobs.common.CommonProxy;
import furgl.babyMobs.common.block.ModBlocks;
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
import furgl.babyMobs.common.entity.projectile.EntityGhastFireball;
import furgl.babyMobs.common.entity.projectile.EntitySnowmanSnowball;
import furgl.babyMobs.common.entity.projectile.EntityWitherExplosion;
import furgl.babyMobs.common.entity.projectile.EntityWitherSkeletonSmoke;
import furgl.babyMobs.common.entity.projectile.EntityWitherWitherSkull;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.entity.RenderFireball;
import net.minecraft.client.renderer.entity.RenderPigZombie;
import net.minecraft.client.renderer.entity.RenderShulkerBullet;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraft.client.renderer.entity.RenderWitherSkull;
import net.minecraft.client.renderer.entity.RenderZombie;
import net.minecraft.init.Items;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{	
	@Override
	public void preInit() {
		super.preInit();
	}
	
	@Override
	public void init() {
		super.init();
		registerEntityRenders();
		ModItems.registerRenders();
		ModBlocks.registerRenders();
	}
	
	@Override
	public void postInit() {
		super.postInit();
	}
	
	@Override
	public Class getParticleClass()
	{
		return EntityCustomParticle.class;
	}
	
	@Override
	public void spawnEntitySpawner(Class entityClass, World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator) 
	{ 
		try 
		{
			Particle particle = (Particle) entityClass.getConstructor(World.class, double.class, double.class, double.class, EntitySpawner.class, int.class, int.class).newInstance(world, x, y, z, spawner, heightIterator, entityIterator);
			Minecraft.getMinecraft().effectRenderer.addEffect(particle);
		} 
		catch (Exception e) 
		{
			System.out.println("ERROR: ENTITYFX " + entityClass + " MISSING SPAWNER CONSTRUCTOR");
			e.printStackTrace();
		}
	}
	
	@Override
	public void spawnEntitySkeletonEffectFX(World world, EntityBabySkeleton entityBabySkeleton, float red, float green, float blue) 
	{
		Particle effect = new EntitySkeletonEffectFX(world, entityBabySkeleton, red, green, blue);
		Minecraft.getMinecraft().effectRenderer.addEffect(effect);
	}
	
	@Override
	public void spawnEntityBlazeFlamethrowerFX(EntityBabyBlaze entityBabyBlaze, Vec3d vec) 
	{ 
		EntitySpawner entityFXSpawner = new EntitySpawner(EntityBlazeFlamethrowerFX.class, entityBabyBlaze.world, vec, 8);
		entityFXSpawner.setMovementFollowShape(0.1D);
		entityFXSpawner.setShapeLine(5D, entityBabyBlaze.rotationPitch, entityBabyBlaze.rotationYawHead);
		entityFXSpawner.setRandVar(0.2D);
		entityFXSpawner.run();
	}
	
	@Override
	public void spawnEntityBlazeFlamethrowerFX(EntityGhastFireball entityGhastFireball) 
	{ 
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntityBlazeFlamethrowerFX(entityGhastFireball.world, entityGhastFireball.posX+(entityGhastFireball.world.rand.nextDouble()-0.5D), entityGhastFireball.posY+(entityGhastFireball.world.rand.nextDouble()-0.5D), entityGhastFireball.posZ+(entityGhastFireball.world.rand.nextDouble()-0.5D), entityGhastFireball.motionX, entityGhastFireball.motionY, entityGhastFireball.motionZ));
	}
	
	@Override
	public void spawnEntitySquidInkFX(EntityWitherSkeletonSmoke entitySquidInk) 
	{ 
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySquidInkFX(entitySquidInk.world, entitySquidInk.posX + (entitySquidInk.world.rand.nextDouble()-0.5D)*0.5, entitySquidInk.posY + (entitySquidInk.world.rand.nextDouble()-0.5D)*0.5, entitySquidInk.posZ + (entitySquidInk.world.rand.nextDouble()-0.5D)*0.5, entitySquidInk.motionX/2, entitySquidInk.motionY/2, entitySquidInk.motionZ/2));
	}
	
	@Override
	public void spawnEntitySquidInkFX(World world, double x, double y, double z, int motionX, int motionY, int motionZ)
	{
		Minecraft.getMinecraft().effectRenderer.addEffect(new EntitySquidInkFX(world, x, y, z, motionX, motionY, motionZ));
	}
	
	@Override
	public void spawnEntityDragonParticlesFX(EntityBabyDragon entityBabyDragon) 
	{ 
		EntitySpawner spawner = new EntitySpawner(EntityDragonParticlesFX.class, entityBabyDragon.world, new Vec3d(entityBabyDragon.dragonEgg.x+0.5D, entityBabyDragon.dragonEgg.y, entityBabyDragon.dragonEgg.z+0.5D), 10);
		spawner.setShapeCircle(0.8D);
		spawner.setMovementFollowShape(0.1D);
		spawner.setUpdateTick(10);
		spawner.run();
	}

	private void registerEntityRenders() 
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySpider.class, renderManager -> new RenderBabySpider(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySkeleton.class, renderManager -> new RenderBabySkeleton(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyCreeper.class, renderManager -> new RenderBabyCreeper(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyWitherSkeleton.class, renderManager -> new RenderBabyWitherSkeleton(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyEnderman.class, renderManager -> new RenderBabyEnderman(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyBlaze.class, renderManager -> new RenderBabyBlaze(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyWitch.class, renderManager -> new RenderBabyWitch(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyGuardian.class, renderManager -> new RenderBabyGuardian(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySquid.class, renderManager -> new RenderBabySquid(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyCaveSpider.class, renderManager -> new RenderBabyCaveSpider(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyZombie.class, renderManager -> new RenderZombie(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyPigZombie.class, renderManager -> new RenderPigZombie(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyGhast.class, renderManager -> new RenderBabyGhast(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabySnowman.class, renderManager -> new RenderBabySnowMan(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyIronGolem.class, renderManager -> new RenderBabyIronGolem(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyWither.class, renderManager -> new RenderBabyWither(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyDragon.class, renderManager -> new RenderBabyDragon(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyOcelot.class, renderManager -> new RenderBabyOcelot(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombieChicken.class, renderManager -> new RenderZombieChicken(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityZombiePig.class, renderManager -> new RenderZombiePig(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyShulker.class, renderManager -> new RenderBabyShulker(renderManager));
		
		RenderingRegistry.registerEntityRenderingHandler(EntityCaveSpiderVenom.class, renderManager -> new RenderSnowball(renderManager, ModItems.CAVE_SPIDER_VENOM, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntitySnowmanSnowball.class, renderManager -> new RenderSnowball(renderManager, Items.SNOWBALL, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBlazeFlamethrower.class, renderManager -> new RenderSnowball(renderManager, ModItems.INVISIBLE, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityCreeperExplosion.class, renderManager -> new RenderSnowball(renderManager, ModItems.CREEPER_EXPLOSION, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityWitherSkeletonSmoke.class, renderManager -> new RenderSnowball(renderManager, ModItems.INVISIBLE, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityGhastFireball.class, renderManager -> new RenderFireball(renderManager, 2.0F));
		RenderingRegistry.registerEntityRenderingHandler(EntityWitherWitherSkull.class, renderManager -> new RenderWitherSkull(renderManager));
		RenderingRegistry.registerEntityRenderingHandler(EntityWitherExplosion.class, renderManager -> new RenderSnowball(renderManager, ModItems.INVISIBLE, Minecraft.getMinecraft().getRenderItem()));
		RenderingRegistry.registerEntityRenderingHandler(EntityBabyShulkerBullet.class, renderManager -> new RenderShulkerBullet(renderManager));
	}
}
