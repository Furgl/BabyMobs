package furgl.babyMobs.common.entity.ai;

import java.util.Iterator;
import java.util.List;

import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyCaveSpider;
import furgl.babyMobs.common.entity.monster.EntityBabyCreeper;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import furgl.babyMobs.common.entity.monster.EntityBabyGhast;
import furgl.babyMobs.common.entity.monster.EntityBabyGuardian;
import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityBabyOcelot;
import furgl.babyMobs.common.entity.monster.EntityBabyPigZombie;
import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabySnowman;
import furgl.babyMobs.common.entity.monster.EntityBabySpider;
import furgl.babyMobs.common.entity.monster.EntityBabySquid;
import furgl.babyMobs.common.entity.monster.EntityBabyWitch;
import furgl.babyMobs.common.entity.monster.EntityBabyWitherSkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabyZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntitySquid;

public class EntityAIBabyFollowParent<T extends Entity> extends EntityAIBase
{
	/** The child that is following its parent. */
	private EntityLiving child;
	private EntityLiving parent;
	private double field1;
	private int field2;

	public EntityAIBabyFollowParent(EntityLiving entityliving, double par2)
	{
		this.child = entityliving;
		this.field1 = par2;
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		if (!(this.child.getAttackTarget() == null))
			return false;
        List<EntityLiving> list = this.child.worldObj.getEntitiesWithinAABB(EntityLiving.class, this.child.getEntityBoundingBox().expand(8.0D, 4.0D, 8.0D));
		EntityLiving entityliving = null;
		double d0 = Double.MAX_VALUE;
		Iterator<?> iterator = list.iterator();

		while (iterator.hasNext())
		{
			EntityLiving entityliving1 = (EntityLiving)iterator.next();
			double d1 = this.child.getDistanceSqToEntity(entityliving1);

			if (d1 <= d0 && this.getParent(this.child) == entityliving1.getClass())
			{
				d0 = d1;
				entityliving = entityliving1;
			}
		}

		if (entityliving == null)
		{
			return false;
		}
		else if (d0 < 9.0D)
		{
			return false;
		}
		else
		{
			this.parent = entityliving;
			return true;
		}
	}

	private Class getParent(EntityLiving taskOwner) {
		if (taskOwner.getClass() == EntityBabyCreeper.class)
		{
			return EntityCreeper.class;
		}
		else if (taskOwner.getClass() == EntityBabySkeleton.class || taskOwner.getClass() == EntityBabyWitherSkeleton.class)
		{
			return EntitySkeleton.class;
		}
		else if (taskOwner.getClass() == EntityBabySpider.class)
		{
			return EntitySpider.class;
		}
		else if (taskOwner.getClass() == EntityBabyEnderman.class)
		{
			return EntityEnderman.class;
		}
		else if (taskOwner.getClass() == EntityBabyBlaze.class)
		{
			return EntityBlaze.class;
		}
		else if (taskOwner.getClass() == EntityBabyWitch.class)
		{
			return EntityWitch.class;
		}
		else if (taskOwner.getClass() == EntityBabyGuardian.class)
		{
			return EntityGuardian.class;
		}
		else if (taskOwner.getClass() == EntityBabySquid.class)
		{
			return EntitySquid.class;
		}
		else if (taskOwner.getClass() == EntityBabyCaveSpider.class)
		{
			return EntityCaveSpider.class;
		}
		else if (taskOwner.getClass() == EntityBabyGhast.class)
		{
			return EntityGhast.class;
		}
		else if (taskOwner.getClass() == EntityBabySnowman.class)
		{
			return EntitySnowman.class;
		}
		else if (taskOwner.getClass() == EntityBabyIronGolem.class)
		{
			return EntityIronGolem.class;
		}
		else if (taskOwner.getClass() == EntityBabyOcelot.class)
		{
			return EntityBabyWitch.class;
		}
		else if (taskOwner.getClass() == EntityBabyPigZombie.class)
		{
			return EntityPigZombie.class;
		}
		else if (taskOwner.getClass() == EntityBabyZombie.class)
		{
			return EntityZombie.class;
		}
		return null;
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		if (!this.parent.isEntityAlive())
		{
			return false;
		}
		else
		{
			double d0 = this.child.getDistanceSqToEntity(this.parent);
			return d0 >= 9.0D && d0 <= 256.0D;
		}
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.field2 = 0;
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		this.parent = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		if (--this.field2 <= 0)
		{
			this.field2 = 10;
			if (this.child instanceof EntityBabyGhast)
			{
				((EntityBabyGhast) this.child).getMoveHelper().setMoveTo(this.parent.posX, this.parent.posY, this.parent.posZ, this.child.getAIMoveSpeed());
			}
			else
				this.child.getNavigator().tryMoveToEntityLiving(this.parent, this.field1);
		}
	}
}