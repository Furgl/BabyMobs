package furgl.babyMobs.common.entity.ai;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAISkeletonRiders;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseArmorType;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DifficultyInstance;

public class EntityAIZombieRiders extends EntityAISkeletonRiders
{
	private final EntityHorse horse;

	public EntityAIZombieRiders(EntityHorse horseIn) 
	{
		super(horseIn);
		this.horse = horseIn;
	}

	@Override
	public void updateTask()
	{
		DifficultyInstance difficultyinstance = this.horse.worldObj.getDifficultyForLocation(new BlockPos(this.horse));
		this.horse.setSkeletonTrap(false);
		this.horse.tasks.removeTask(this);
		this.horse.setType(HorseArmorType.ZOMBIE);
		this.horse.setHorseTamed(true);
		this.horse.setGrowingAge(0);
		this.horse.worldObj.addWeatherEffect(new EntityLightningBolt(this.horse.worldObj, this.horse.posX, this.horse.posY, this.horse.posZ, true));
		EntityZombie entityzombie = this.setupZombie(difficultyinstance, this.horse);
		entityzombie.startRiding(this.horse);

		for (int i = 0; i < 3; ++i)
		{
			EntityHorse entityhorse = this.setupHorse(difficultyinstance);
			EntityZombie entityzombie1 = this.setupZombie(difficultyinstance, entityhorse);
			entityzombie1.startRiding(entityhorse);
			entityhorse.addVelocity(this.horse.getRNG().nextGaussian() * 0.5D, 0.0D, this.horse.getRNG().nextGaussian() * 0.5D);
		}
	}

	private EntityHorse setupHorse(DifficultyInstance difficultyinstance)
	{
		EntityHorse entityhorse = new EntityHorse(this.horse.worldObj);
		entityhorse.onInitialSpawn(difficultyinstance, (IEntityLivingData)null);
		entityhorse.setPosition(this.horse.posX, this.horse.posY, this.horse.posZ);
		entityhorse.hurtResistantTime = 60;
		entityhorse.enablePersistence();
		entityhorse.setType(HorseArmorType.ZOMBIE);
		entityhorse.setSkeletonTrap(false);
		entityhorse.setHorseTamed(true);
		entityhorse.setGrowingAge(0);
		entityhorse.worldObj.spawnEntityInWorld(entityhorse);
		return entityhorse;
	}

	private EntityZombie setupZombie(DifficultyInstance difficultyinstance, EntityHorse horse) 
	{
		EntityZombie entityzombie = new EntityZombie(horse.worldObj);
		entityzombie.onInitialSpawn(difficultyinstance, (IEntityLivingData)null);
		entityzombie.setPosition(horse.posX, horse.posY, horse.posZ);
		entityzombie.hurtResistantTime = 60;
		entityzombie.enablePersistence();

		if (entityzombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null)
			entityzombie.setItemStackToSlot(EntityEquipmentSlot.HEAD, new ItemStack(Items.iron_helmet));

		if (entityzombie.getHeldItemMainhand() != null)
			EnchantmentHelper.addRandomEnchantment(entityzombie.getRNG(), entityzombie.getHeldItemMainhand(), (int)(5.0F + difficultyinstance.getClampedAdditionalDifficulty() * (float)entityzombie.getRNG().nextInt(18)), false);
		EnchantmentHelper.addRandomEnchantment(entityzombie.getRNG(), entityzombie.getItemStackFromSlot(EntityEquipmentSlot.HEAD), (int)(5.0F + difficultyinstance.getClampedAdditionalDifficulty() * (float)entityzombie.getRNG().nextInt(18)), false);
		entityzombie.worldObj.spawnEntityInWorld(entityzombie);
		return entityzombie;
	}
}
