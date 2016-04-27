package furgl.babyMobs.common.event;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyCaveSpider;
import furgl.babyMobs.common.entity.monster.EntityBabyCreeper;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import furgl.babyMobs.common.entity.monster.EntityBabyGhast;
import furgl.babyMobs.common.entity.monster.EntityBabyGuardian;
import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityBabyPigZombie;
import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabySpider;
import furgl.babyMobs.common.entity.monster.EntityBabySquid;
import furgl.babyMobs.common.entity.monster.EntityBabyWitch;
import furgl.babyMobs.common.entity.monster.EntityBabyWitherSkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabyZombie;
import furgl.babyMobs.common.entity.monster.EntityZombieChicken;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BabyReplaceEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(LivingEvent.LivingUpdateEvent event)
	{
		if (!event.entity.worldObj.isRemote && event.entity.ticksExisted == 1 && !event.entity.getEntityData().hasKey("Checked")) //if server side & newly spawned
		{
			event.entity.getEntityData().setBoolean("Checked", true);
			Random rand = new Random();
			if (event.entity.getClass() == EntityZombie.class)
			{
				if (((EntityZombie) event.entity).isChild())
					((EntityZombie) event.entity).setChild(false); //prevent baby zombies spawning naturally
				if (rand.nextInt(101) <= Config.babyZombieRate)
				{
					Entity entity = this.spawnEntity(EntityBabyZombie.class, event.entity);
					event.entity.setDead();
					EntityLiving entityToSpawn = new EntityZombieChicken(entity.worldObj);
					entityToSpawn.setLocationAndAngles(entity.posX, entity.posY, entity.posZ,  entity.rotationYaw, 0.0F);
					entityToSpawn.func_180482_a(entity.worldObj.getDifficultyForLocation(entity.getPosition()), (IEntityLivingData)null);
					((EntityZombieChicken) entityToSpawn).setChickenJockey(true);
					entity.worldObj.spawnEntityInWorld(entityToSpawn);
					entity.mountEntity(entityToSpawn);
					entityToSpawn.playLivingSound();
				}
			}
			else if (event.entity.getClass() == EntityBabyZombie.class)
			{
				EntityLiving entityToSpawn = new EntityZombieChicken(event.entity.worldObj);
				entityToSpawn.setLocationAndAngles(event.entity.posX, event.entity.posY, event.entity.posZ,  event.entity.rotationYaw, 0.0F);
				entityToSpawn.func_180482_a(event.entity.worldObj.getDifficultyForLocation(event.entity.getPosition()), (IEntityLivingData)null);
				((EntityZombieChicken) entityToSpawn).setChickenJockey(true);
				event.entity.worldObj.spawnEntityInWorld(entityToSpawn);
				event.entity.mountEntity(entityToSpawn);
				entityToSpawn.playLivingSound();
			}
			else if (event.entity.getClass() == EntityPigZombie.class)
			{
				if (((EntityPigZombie) event.entity).isChild())
					((EntityPigZombie) event.entity).setChild(false); //prevent baby zombie pigmen spawning naturally
				if (rand.nextInt(101) <= Config.babyPigZombieRate)
				{
					Entity entity = this.spawnEntity(EntityBabyPigZombie.class, event.entity);
					event.entity.setDead();
					EntityLiving entityToSpawn = (EntityLiving) EntityList.createEntityByName("babymobs.zombiePig", entity.worldObj);
					entityToSpawn.setLocationAndAngles(entity.posX, entity.posY, entity.posZ,  MathHelper.wrapAngleTo180_float(entity.worldObj.rand.nextFloat() * 360.0F), 0.5F);
					entity.worldObj.spawnEntityInWorld(entityToSpawn);
					entity.mountEntity(entityToSpawn);
					entityToSpawn.playLivingSound();
				}
			}
			else if (event.entity.getClass() == EntityBabyPigZombie.class)
			{
				EntityLiving entityToSpawn = (EntityLiving) EntityList.createEntityByName("babymobs.zombiePig", event.entity.worldObj);
				entityToSpawn.setLocationAndAngles(event.entity.posX, event.entity.posY, event.entity.posZ,  MathHelper.wrapAngleTo180_float(event.entity.worldObj.rand.nextFloat() * 360.0F), 0.5F);
				event.entity.worldObj.spawnEntityInWorld(entityToSpawn);
				event.entity.mountEntity(entityToSpawn);
				entityToSpawn.playLivingSound();
			}
			else if (event.entity.getClass() == EntitySpider.class)
			{

				if (rand.nextInt(101) <= Config.babySpiderRate)
				{
					this.spawnEntity(EntityBabySpider.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntitySkeleton.class && ((EntitySkeleton) event.entity).getSkeletonType() == 0)
			{

				if (rand.nextInt(101) <= Config.babySkeletonRate)
				{
					this.spawnEntity(EntityBabySkeleton.class, event.entity);
					event.entity.setDead();
				}

			}
			else if (event.entity.getClass() == EntityCreeper.class)
			{

				if (rand.nextInt(101) <= Config.babyCreeperRate)
				{
					this.spawnEntity(EntityBabyCreeper.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntitySkeleton.class && ((EntitySkeleton) event.entity).getSkeletonType() == 1)
			{

				if (rand.nextInt(101) <= Config.babyWitherSkeletonRate)
				{
					this.spawnEntity(EntityBabyWitherSkeleton.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntityEnderman.class)
			{
				if (event.entity.worldObj.getBiomeGenForCoords(event.entity.getPosition()) == BiomeGenBase.sky && rand.nextInt(101) <= Config.babyEndermanEndRate)
				{
					this.spawnEntity(EntityBabyEnderman.class, event.entity);
					event.entity.setDead();
				}
				else if (!(event.entity.worldObj.getBiomeGenForCoords(event.entity.getPosition()) == BiomeGenBase.sky) && rand.nextInt(101) <= Config.babyEndermanRate)
				{
					this.spawnEntity(EntityBabyEnderman.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntityBlaze.class)
			{

				if (rand.nextInt(101) <= Config.babyBlazeRate)
				{
					this.spawnEntity(EntityBabyBlaze.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntityWitch.class)
			{

				if (rand.nextInt(101) <= Config.babyWitchRate)
				{
					this.spawnEntity(EntityBabyWitch.class, event.entity);
					EntityLiving entityToSpawn = (EntityLiving) EntityList.createEntityByName("babymobs.babyOcelot", event.entity.worldObj);
					entityToSpawn.setLocationAndAngles(event.entity.posX, event.entity.posY, event.entity.posZ,  MathHelper.wrapAngleTo180_float(event.entity.worldObj.rand.nextFloat() * 360.0F), 0.5F);
					event.entity.worldObj.spawnEntityInWorld(entityToSpawn);
					entityToSpawn.playLivingSound();
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntityBabyWitch.class)
			{
				EntityLiving entityToSpawn = (EntityLiving) EntityList.createEntityByName("babymobs.babyOcelot", event.entity.worldObj);
				entityToSpawn.setLocationAndAngles(event.entity.posX, event.entity.posY, event.entity.posZ,  MathHelper.wrapAngleTo180_float(event.entity.worldObj.rand.nextFloat() * 360.0F), 0.5F);
				event.entity.worldObj.spawnEntityInWorld(entityToSpawn);
				entityToSpawn.playLivingSound();
			}
			else if (event.entity.getClass() == EntityGuardian.class)
			{

				if (rand.nextInt(101) <= Config.babyGuardianRate)
				{
					this.spawnEntity(EntityBabyGuardian.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntitySquid.class)
			{

				if (rand.nextInt(101) <= Config.babySquidRate)
				{
					this.spawnEntity(EntityBabySquid.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntityCaveSpider.class)
			{

				if (rand.nextInt(101) <= Config.babyCaveSpiderRate)
				{
					this.spawnEntity(EntityBabyCaveSpider.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntityGhast.class)
			{

				if (rand.nextInt(101) <= Config.babyGhastRate)
				{
					this.spawnEntity(EntityBabyGhast.class, event.entity);
					event.entity.setDead();
				}
			}
			else if (event.entity.getClass() == EntityIronGolem.class  && (!((EntityIronGolem) event.entity).isPlayerCreated()))
			{

				if (rand.nextInt(101) <= Config.babyIronGolemRate)
				{
					this.spawnEntity(EntityBabyIronGolem.class, event.entity);
					event.entity.setDead();
				}
			}
		}
	}

	/**
	 * 
	 * @param entityToSpawnClass Class of entity to spawn.
	 * @param originalEntity entityToSpawn copies from this entity.
	 */
	private Entity spawnEntity(Class entityToSpawnClass, Entity originalEntity)
	{
		return this.spawnEntity(entityToSpawnClass, originalEntity, null);
	}

	private Entity spawnEntity(Class entityToSpawnClass, Entity originalEntity, Entity riderToIgnore)
	{
		World world = originalEntity.worldObj;
		Entity entityToSpawn = null;
		try 
		{
			entityToSpawn = (Entity) entityToSpawnClass.getConstructor(World.class).newInstance(world);
			NBTTagCompound nbt = new NBTTagCompound();
			originalEntity.writeToNBT(nbt);
			nbt.setLong("UUIDLeast", nbt.getLong("UUIDLeast")+1l);
			entityToSpawn.readFromNBT(nbt);
			entityToSpawn.setCustomNameTag(originalEntity.getCustomNameTag());
			world.spawnEntityInWorld(entityToSpawn);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException| NoSuchMethodException | SecurityException e) 
		{
			e.printStackTrace();
		}

		//Copy data from riders/ridden
		if (originalEntity.riddenByEntity != null && originalEntity.riddenByEntity != riderToIgnore)
		{
			Entity riddenByEntity = this.spawnEntity(originalEntity.riddenByEntity.getClass(), originalEntity.riddenByEntity, originalEntity);
			riddenByEntity.mountEntity(entityToSpawn);
			originalEntity.riddenByEntity.setDead();
		}
		if (originalEntity.ridingEntity != null && originalEntity.ridingEntity != riderToIgnore)
		{
			Entity ridingEntity = this.spawnEntity(originalEntity.ridingEntity.getClass(), originalEntity.ridingEntity, originalEntity);
			entityToSpawn.mountEntity(ridingEntity);
			originalEntity.ridingEntity.setDead();
		}
		if (entityToSpawn instanceof EntityLiving)
			((EntityLiving) entityToSpawn).playLivingSound();
		return entityToSpawn;
	}
}
