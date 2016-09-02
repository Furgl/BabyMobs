package furgl.babyMobs.common.entity.ai;

import java.util.List;
import java.util.Random;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.HorseType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntityAIUndeadHorseMate extends EntityAIMate
{
	private EntityHorse theAnimal;
	private World theWorld;
	private EntityAnimal targetMate;
	/** Delay preventing a baby from spawning immediately when two mate-able animals find each other. */
	int spawnBabyDelay;
    /** The speed the creature moves at during mating behavior. */
    double moveSpeed;

	public EntityAIUndeadHorseMate(EntityAnimal animal, double speedIn) 
	{
		super(animal, speedIn);
		this.theAnimal = (EntityHorse) animal;
		this.theWorld = animal.worldObj;
        this.moveSpeed = speedIn;
	}
	
	@Override
	public boolean continueExecuting()
    {
        return this.targetMate.isEntityAlive() && this.targetMate.isInLove() && this.spawnBabyDelay < 60;
    }

	@Override
	public boolean shouldExecute()
	{
		if (!this.theAnimal.isInLove())
		{
			return false;
		}
		else
		{
			this.targetMate = this.getNearbyMate();
			return this.targetMate != null;
		}
	}

	@Override
	public void resetTask()
	{
		this.targetMate = null;
		this.spawnBabyDelay = 0;
	}

	@Override
	public void updateTask()
	{
		this.theAnimal.getLookHelper().setLookPositionWithEntity(this.targetMate, 10.0F, (float)this.theAnimal.getVerticalFaceSpeed());
		this.theAnimal.getNavigator().tryMoveToEntityLiving(this.targetMate, this.moveSpeed);
		++this.spawnBabyDelay;

		if (this.spawnBabyDelay >= 60 && this.theAnimal.getDistanceSqToEntity(this.targetMate) < 9.0D)
		{
			this.spawnBaby();
		}
	}

	private EntityAnimal getNearbyMate()
	{
		List<EntityAnimal> list = this.theWorld.<EntityAnimal>getEntitiesWithinAABB(this.theAnimal.getClass(), this.theAnimal.getEntityBoundingBox().expandXyz(8.0D));
		double d0 = Double.MAX_VALUE;
		EntityAnimal entityanimal = null;

		for (EntityAnimal entityanimal1 : list)
		{
			if (this./*theAnimal.*/canMateWith(entityanimal1) && this.theAnimal.getDistanceSqToEntity(entityanimal1) < d0)
			{
				entityanimal = entityanimal1;
				d0 = this.theAnimal.getDistanceSqToEntity(entityanimal1);
			}
		}

		return entityanimal;
	}

	private boolean canMateWith(EntityAnimal otherAnimal) 
	{
		if (otherAnimal == this.theAnimal)
		{
			return false;
		}
		else if (otherAnimal.getClass() != this.theAnimal.getClass())
		{
			return false;
		}
		else
		{
			EntityHorse entityhorse = (EntityHorse)otherAnimal;

			if (this./*theAnimal.*/canMate(theAnimal) && this/*entityhorse*/.canMate(entityhorse))
			{
				HorseType horsearmortype = this.theAnimal.getType();
				HorseType horsearmortype1 = entityhorse.getType();
				return horsearmortype == horsearmortype1 || horsearmortype == HorseType.HORSE && horsearmortype1 == HorseType.DONKEY || horsearmortype == HorseType.DONKEY && horsearmortype1 == HorseType.HORSE;
			}
			else
			{
				return false;
			}
		}
	}

	private boolean canMate(EntityHorse horse)
	{
		return !horse.isBeingRidden() && !horse.isRiding() && horse.isTame() && horse.isAdultHorse()/* && horse.getType().canMate()*/ && horse.getHealth() >= horse.getMaxHealth() && horse.isInLove();
	}
	
	/**
     * Spawns a baby animal of the same type.
     */
    private void spawnBaby()
    {
        EntityAgeable entityageable = this.theAnimal.createChild(this.targetMate);

        if (entityageable != null)
        {
            EntityPlayer entityplayer = this.theAnimal.getPlayerInLove();

            if (entityplayer == null && this.targetMate.getPlayerInLove() != null)
            {
                entityplayer = this.targetMate.getPlayerInLove();
            }

            if (entityplayer != null)
            {
                entityplayer.addStat(StatList.ANIMALS_BRED);

                /*if (this.theAnimal instanceof EntityCow)
                {
                    entityplayer.addStat(AchievementList.breedCow);
                }*/
            }

            this.theAnimal.setGrowingAge(6000);
            this.targetMate.setGrowingAge(6000);
            this.theAnimal.resetInLove();
            this.targetMate.resetInLove();
            entityageable.setGrowingAge(-24000);
            entityageable.setLocationAndAngles(this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, 0.0F, 0.0F);
            this.theWorld.spawnEntityInWorld(entityageable);
            Random random = this.theAnimal.getRNG();

            for (int i = 0; i < 7; ++i)
            {
                double d0 = random.nextGaussian() * 0.02D;
                double d1 = random.nextGaussian() * 0.02D;
                double d2 = random.nextGaussian() * 0.02D;
                double d3 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
                double d4 = 0.5D + random.nextDouble() * (double)this.theAnimal.height;
                double d5 = random.nextDouble() * (double)this.theAnimal.width * 2.0D - (double)this.theAnimal.width;
                this.theWorld.spawnParticle(EnumParticleTypes.HEART, this.theAnimal.posX + d3, this.theAnimal.posY + d4, this.theAnimal.posZ + d5, d0, d1, d2, new int[0]);
            }

            if (this.theWorld.getGameRules().getBoolean("doMobLoot"))
            {
                this.theWorld.spawnEntityInWorld(new EntityXPOrb(this.theWorld, this.theAnimal.posX, this.theAnimal.posY, this.theAnimal.posZ, random.nextInt(7) + 1));
            }
        }
    }
}
