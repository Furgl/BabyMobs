package furgl.babyMobs.common.event;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIUndeadHorseMate;
import furgl.babyMobs.common.entity.ai.EntityAIZombieRiders;
import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyCaveSpider;
import furgl.babyMobs.common.entity.monster.EntityBabyCreeper;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import furgl.babyMobs.common.entity.monster.EntityBabyGhast;
import furgl.babyMobs.common.entity.monster.EntityBabyGuardian;
import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityBabyPigZombie;
import furgl.babyMobs.common.entity.monster.EntityBabyShulker;
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
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenEnd;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BabyReplaceEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(LivingEvent.LivingUpdateEvent event)
	{
		if (event.getEntity() instanceof EntityHorse && !((EntityHorse)event.getEntity()).isTame() && (((EntityHorse)event.getEntity()).getType() == HorseArmorType.ZOMBIE || ((EntityHorse)event.getEntity()).getType() == HorseArmorType.SKELETON))
			((EntityHorse)event.getEntity()).setHorseTamed(true);
		if (!event.getEntity().worldObj.isRemote && event.getEntity().ticksExisted == 1 && event.getEntity() instanceof EntityHorse && (((EntityHorse)event.getEntity()).getType() == HorseArmorType.ZOMBIE || ((EntityHorse)event.getEntity()).getType() == HorseArmorType.SKELETON))
		{
			EntityHorse horse = (EntityHorse) event.getEntity();
			boolean hasZombieAI = false;
			boolean hasUndeadHorseMateAI = false;
			ArrayList<EntityAIBase> list = new ArrayList<EntityAIBase>();
			for (EntityAITaskEntry task : horse.tasks.taskEntries) {
				if (horse.isSkeletonTrap() && ((EntityHorse)event.getEntity()).getType() == HorseArmorType.ZOMBIE)
				{
					if (task.action.getClass().getSimpleName().equals("EntityAISkeletonRiders"))
					{
						//EntityAISkeletonRiders skeletonTrapAI = (EntityAISkeletonRiders) BabyMobs.reflect(EntityHorse.class, "skeletonTrapAI", horse);
						//horse.tasks.removeTask(task.action);
						list.add(task.action);
					}
					else if (task.action.getClass().getSimpleName().equals("EntityAIZombieRiders"))
						hasZombieAI = true;
				}
				if (task.action.getClass().getSimpleName().equals("EntityAIMate"))
				{
					//EntityAISkeletonRiders skeletonTrapAI = (EntityAISkeletonRiders) BabyMobs.reflect(EntityHorse.class, "skeletonTrapAI", horse);
					//horse.tasks.removeTask(task.action);
					list.add(task.action);
				}
				else if (task.action.getClass().getSimpleName().equals("EntityAIZombieRiders"))
					hasUndeadHorseMateAI = true;
			}
			for (EntityAIBase task : list)
				horse.tasks.removeTask(task);
			if (!hasZombieAI && horse.isSkeletonTrap() && ((EntityHorse)event.getEntity()).getType() == HorseArmorType.ZOMBIE)
				horse.tasks.addTask(1, new EntityAIZombieRiders(horse));
			if (!hasUndeadHorseMateAI)
				horse.tasks.addTask(1, new EntityAIUndeadHorseMate(horse, 1.0D));
		}
		if (!event.getEntity().worldObj.isRemote && event.getEntity().ticksExisted == 1 && !event.getEntity().getEntityData().hasKey("Baby Mobs: Checked")) //if server side & newly spawned
		{
			event.getEntity().getEntityData().setBoolean("Baby Mobs: Checked", true);
			Random rand = new Random();
			if (event.getEntity().getClass() == EntityZombie.class)
			{
				if (((EntityZombie) event.getEntity()).isChild())
					((EntityZombie) event.getEntity()).setChild(false); //prevent baby zombies spawning naturally
				if (rand.nextInt(101) <= Config.babyZombieRate || event.getEntity().getRidingEntity() instanceof EntityHorse && ((EntityHorse)event.getEntity().getRidingEntity()).isChild())
				{
					Entity entity = this.spawnEntity(EntityBabyZombie.class, event.getEntity());
					event.getEntity().setDead();
					if (!(entity.getRidingEntity() instanceof EntityHorse)) {
						EntityLiving entityToSpawn = new EntityZombieChicken(entity.worldObj);
						entityToSpawn.setLocationAndAngles(entity.posX, entity.posY, entity.posZ,  entity.rotationYaw, 0.0F);
						entityToSpawn.onInitialSpawn(entity.worldObj.getDifficultyForLocation(entity.getPosition()), (IEntityLivingData)null);
						((EntityZombieChicken) entityToSpawn).setChickenJockey(true);
						entity.worldObj.spawnEntityInWorld(entityToSpawn);
						entity.startRiding(entityToSpawn);
						entityToSpawn.playLivingSound();
					}
					else 
						((EntityHorse) event.getEntity().getRidingEntity()).setGrowingAge(-24000);
				}
			}
			else if (event.getEntity().getClass() == EntityBabyZombie.class)
			{
				EntityLiving entityToSpawn = new EntityZombieChicken(event.getEntity().worldObj);
				entityToSpawn.setLocationAndAngles(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ,  event.getEntity().rotationYaw, 0.0F);
				entityToSpawn.onInitialSpawn(event.getEntity().worldObj.getDifficultyForLocation(event.getEntity().getPosition()), (IEntityLivingData)null);
				((EntityZombieChicken) entityToSpawn).setChickenJockey(true);
				event.getEntity().worldObj.spawnEntityInWorld(entityToSpawn);
				event.getEntity().startRiding(entityToSpawn);
				entityToSpawn.playLivingSound();
			}
			else if (event.getEntity().getClass() == EntityPigZombie.class)
			{
				if (((EntityPigZombie) event.getEntity()).isChild())
					((EntityPigZombie) event.getEntity()).setChild(false); //prevent baby zombie pigmen spawning naturally
				if (rand.nextInt(101) <= Config.babyPigZombieRate)
				{
					Entity entity = this.spawnEntity(EntityBabyPigZombie.class, event.getEntity());
					event.getEntity().setDead();
					EntityLiving entityToSpawn = (EntityLiving) EntityList.createEntityByName("babymobs.zombiePig", entity.worldObj);
					entityToSpawn.setLocationAndAngles(entity.posX, entity.posY, entity.posZ,  MathHelper.wrapAngleTo180_float(entity.worldObj.rand.nextFloat() * 360.0F), 0.5F);
					entity.worldObj.spawnEntityInWorld(entityToSpawn);
					entity.startRiding(entityToSpawn);
					entityToSpawn.playLivingSound();
				}
			}
			else if (event.getEntity().getClass() == EntityBabyPigZombie.class)
			{
				EntityLiving entityToSpawn = (EntityLiving) EntityList.createEntityByName("babymobs.zombiePig", event.getEntity().worldObj);
				entityToSpawn.setLocationAndAngles(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ,  MathHelper.wrapAngleTo180_float(event.getEntity().worldObj.rand.nextFloat() * 360.0F), 0.5F);
				event.getEntity().worldObj.spawnEntityInWorld(entityToSpawn);
				event.getEntity().startRiding(entityToSpawn);
				entityToSpawn.playLivingSound();
			}
			else if (event.getEntity().getClass() == EntityHorse.class && ((EntityHorse)event.getEntity()).getType() == HorseArmorType.SKELETON)
			{
				((EntityHorse)event.getEntity()).setHorseTamed(true);
				Iterator it = event.getEntity().getRecursivePassengers().iterator();
				Entity entity = it.hasNext() ? (Entity) it.next() : null;
				if (rand.nextInt(101) <= Config.babySkeletonHorseRate || entity instanceof EntitySkeleton && ((EntitySkeleton) entity).isChild())
				{
					EntityHorse horse = (EntityHorse) this.spawnEntity(EntityHorse.class, event.getEntity());
					horse.setType(HorseArmorType.SKELETON);
					horse.setGrowingAge(-24000);
					horse.setSkeletonTrap(((EntityHorse)event.getEntity()).isSkeletonTrap());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityHorse.class && ((EntityHorse)event.getEntity()).getType() == HorseArmorType.ZOMBIE)
			{
				((EntityHorse)event.getEntity()).setHorseTamed(true);
				Iterator it = event.getEntity().getRecursivePassengers().iterator();
				Entity entity = it.hasNext() ? (Entity) it.next() : null;
				if (rand.nextInt(101) <= Config.babyZombieHorseRate || entity instanceof EntityZombie && ((EntityZombie) entity).isChild())
				{
					EntityHorse horse = (EntityHorse) this.spawnEntity(EntityHorse.class, event.getEntity());
					horse.setType(HorseArmorType.ZOMBIE);
					horse.setGrowingAge(-24000);
					horse.setSkeletonTrap(((EntityHorse)event.getEntity()).isSkeletonTrap());
					if (horse.isSkeletonTrap())
					{
						EntityAISkeletonRiders skeletonTrapAI = (EntityAISkeletonRiders) BabyMobs.reflect(EntityHorse.class, "skeletonTrapAI", horse);
						horse.tasks.removeTask(skeletonTrapAI);
						horse.tasks.addTask(1, new EntityAIZombieRiders(horse));
					}
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntitySpider.class)
			{
				if (rand.nextInt(101) <= Config.babySpiderRate)
				{
					this.spawnEntity(EntityBabySpider.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityShulker.class)
			{
				if (rand.nextInt(101) <= Config.babyShulkerRate)
				{
					this.spawnEntity(EntityBabyShulker.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntitySkeleton.class && ((EntitySkeleton) event.getEntity()).getSkeletonType() == 0)
			{
				if (rand.nextInt(101) <= Config.babySkeletonRate || event.getEntity().getRidingEntity() instanceof EntityHorse && ((EntityHorse)event.getEntity().getRidingEntity()).isChild())
				{
					this.spawnEntity(EntityBabySkeleton.class, event.getEntity());
					event.getEntity().setDead();
					if (event.getEntity().getRidingEntity() instanceof EntityHorse) 
						((EntityHorse) event.getEntity().getRidingEntity()).setGrowingAge(-29000);
				}
			}
			else if (event.getEntity().getClass() == EntityCreeper.class)
			{
				if (rand.nextInt(101) <= Config.babyCreeperRate)
				{
					this.spawnEntity(EntityBabyCreeper.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntitySkeleton.class && ((EntitySkeleton) event.getEntity()).getSkeletonType() == 1)
			{
				if (rand.nextInt(101) <= Config.babyWitherSkeletonRate)
				{
					this.spawnEntity(EntityBabyWitherSkeleton.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityEnderman.class)
			{
				if (event.getEntity().worldObj.getBiomeGenForCoords(event.getEntity().getPosition()) instanceof BiomeGenEnd && rand.nextInt(101) <= Config.babyEndermanEndRate)
				{
					this.spawnEntity(EntityBabyEnderman.class, event.getEntity());
					event.getEntity().setDead();
				}
				else if (!(event.getEntity().worldObj.getBiomeGenForCoords(event.getEntity().getPosition()) instanceof BiomeGenEnd) && rand.nextInt(101) <= Config.babyEndermanRate)
				{
					this.spawnEntity(EntityBabyEnderman.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityBlaze.class)
			{
				if (rand.nextInt(101) <= Config.babyBlazeRate)
				{
					this.spawnEntity(EntityBabyBlaze.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityWitch.class)
			{
				if (rand.nextInt(101) <= Config.babyWitchRate)
				{
					this.spawnEntity(EntityBabyWitch.class, event.getEntity());
					EntityLiving entityToSpawn = (EntityLiving) EntityList.createEntityByName("babymobs.babyOcelot", event.getEntity().worldObj);
					entityToSpawn.setLocationAndAngles(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ,  MathHelper.wrapAngleTo180_float(event.getEntity().worldObj.rand.nextFloat() * 360.0F), 0.5F);
					event.getEntity().worldObj.spawnEntityInWorld(entityToSpawn);
					entityToSpawn.playLivingSound();
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityBabyWitch.class)
			{
				EntityLiving entityToSpawn = (EntityLiving) EntityList.createEntityByName("babymobs.babyOcelot", event.getEntity().worldObj);
				entityToSpawn.setLocationAndAngles(event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ,  MathHelper.wrapAngleTo180_float(event.getEntity().worldObj.rand.nextFloat() * 360.0F), 0.5F);
				event.getEntity().worldObj.spawnEntityInWorld(entityToSpawn);
				entityToSpawn.playLivingSound();
			}
			else if (event.getEntity().getClass() == EntityGuardian.class)
			{
				if (rand.nextInt(101) <= Config.babyGuardianRate)
				{
					this.spawnEntity(EntityBabyGuardian.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntitySquid.class)
			{
				if (rand.nextInt(101) <= Config.babySquidRate)
				{
					this.spawnEntity(EntityBabySquid.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityCaveSpider.class)
			{
				if (rand.nextInt(101) <= Config.babyCaveSpiderRate)
				{
					this.spawnEntity(EntityBabyCaveSpider.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityGhast.class)
			{
				if (rand.nextInt(101) <= Config.babyGhastRate)
				{
					this.spawnEntity(EntityBabyGhast.class, event.getEntity());
					event.getEntity().setDead();
				}
			}
			else if (event.getEntity().getClass() == EntityIronGolem.class  && (!((EntityIronGolem) event.getEntity()).isPlayerCreated()))
			{
				if (rand.nextInt(101) <= Config.babyIronGolemRate)
				{
					this.spawnEntity(EntityBabyIronGolem.class, event.getEntity());
					event.getEntity().setDead();
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
			nbt.setLong("UUIDLeast", nbt.getLong("UUIDLeast")+1L);
			entityToSpawn.readFromNBT(nbt);
			entityToSpawn.setCustomNameTag(originalEntity.getCustomNameTag());
			world.spawnEntityInWorld(entityToSpawn);
		} 
		catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException| NoSuchMethodException | SecurityException e) 
		{
			e.printStackTrace();
		}

		//Copy data from riders/ridden
		if (originalEntity.isBeingRidden() && !originalEntity.getRecursivePassengers().isEmpty())
		{
			Iterator it = originalEntity.getRecursivePassengers().iterator();
			Entity entity = (Entity) it.next();
			if (entity != riderToIgnore) {
				Entity riddenByEntity = this.spawnEntity(entity.getClass(), entity, originalEntity);
				riddenByEntity.startRiding(entityToSpawn);
				entity.setDead();
			}
		}
		if (originalEntity.getRidingEntity() != null && originalEntity.getRidingEntity() != riderToIgnore)
		{
			Entity ridingEntity = this.spawnEntity(originalEntity.getRidingEntity().getClass(), originalEntity.getRidingEntity(), originalEntity);
			entityToSpawn.startRiding(ridingEntity);
			originalEntity.getRidingEntity().setDead();
		}
		if (entityToSpawn instanceof EntityLiving)
			((EntityLiving) entityToSpawn).playLivingSound();
		return entityToSpawn;
	}
}
