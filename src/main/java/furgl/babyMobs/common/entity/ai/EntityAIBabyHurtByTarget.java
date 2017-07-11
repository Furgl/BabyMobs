package furgl.babyMobs.common.entity.ai;

import java.util.Iterator;
import java.util.List;

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
import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabySnowman;
import furgl.babyMobs.common.entity.monster.EntityBabySpider;
import furgl.babyMobs.common.entity.monster.EntityBabySquid;
import furgl.babyMobs.common.entity.monster.EntityBabyWitch;
import furgl.babyMobs.common.entity.monster.EntityBabyWither;
import furgl.babyMobs.common.entity.monster.EntityBabyWitherSkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabyZombie;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAITarget;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityWither;
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
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;

public class EntityAIBabyHurtByTarget extends EntityAITarget {

	private boolean entityCallsForHelp;
	/** Store the previous revengeTimer value */
	private int revengeTimerOld;
	private final Class[] field_179447_c;
	private EntityLivingBase target;
	public EntityAIBabyHurtByTarget(EntityCreature entityCreature, boolean bool, Class<?> ... className)
	{
		super(entityCreature, false);
		this.entityCallsForHelp = bool;
		this.field_179447_c = className;
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		int i = this.taskOwner.getRevengeTimer();
		return i != this.revengeTimerOld && this.isSuitableTarget(this.taskOwner.getAttackTarget(), false);
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		if (!(this.taskOwner instanceof EntityBabyWitherSkeleton))
		{
			target = this.taskOwner.getAttackTarget();
			this.taskOwner.setAttackTarget(target);
		}
		this.revengeTimerOld = this.taskOwner.getRevengeTimer();

		if (this.entityCallsForHelp)
		{
			double d0 = this.getTargetDistance();
			if (this.taskOwner.getClass() == EntityBabyEnderman.class)
				d0 = 10.0D;
			List<?> list = this.taskOwner.world.getEntitiesWithinAABB(EntityCreature.class, (new AxisAlignedBB(this.taskOwner.posX, this.taskOwner.posY, this.taskOwner.posZ, this.taskOwner.posX + 1.0D, this.taskOwner.posY + 1.0D, this.taskOwner.posZ + 1.0D)).expand(d0, 10.0D, d0));
			Iterator<?> iterator = list.iterator();

			while (iterator.hasNext())
			{
				EntityCreature entitycreature = (EntityCreature)iterator.next();

				if (this.taskOwner != entitycreature && entitycreature.getClass() == this.getParent(this.taskOwner) && !entitycreature.isOnSameTeam(this.taskOwner.getAttackTarget()))
				{
					boolean flag = false;
					Class[] aclass = this.field_179447_c;
					int i = aclass.length;

					for (int j = 0; j < i; ++j)
					{
						Class<?> oclass = aclass[j];

						if (entitycreature.getClass() == oclass)
						{
							flag = true;
							break;
						}
					}

					if (!flag)
					{
						this.func_179446_a(entitycreature, this.taskOwner.getAttackTarget());
					}
				}
			}
		}

		super.startExecuting();
	}

	private Class<?> getParent(EntityLiving taskOwner) {
		if (taskOwner.getClass() == EntityBabyCreeper.class)
		{
			return EntityCreeper.class;
		}
		else if (taskOwner.getClass() == EntityBabySkeleton.class)
		{
			return EntitySkeleton.class;
		}
		else if (taskOwner.getClass() == EntityBabyWitherSkeleton.class)
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
		else if (taskOwner.getClass() == EntityBabyGuardian.class)
		{
			return EntityGuardian.class;
		}
		else if (taskOwner.getClass() == EntityBabyOcelot.class)
		{
			return EntityOcelot.class;
		}
		else if (taskOwner.getClass() == EntityBabyDragon.class)
		{
			return EntityDragon.class;
		}
		else if (taskOwner.getClass() == EntityBabyPigZombie.class)
		{
			return EntityPigZombie.class;
		}
		else if (taskOwner.getClass() == EntityBabyWither.class)
		{
			return EntityWither.class;
		}
		else if (taskOwner.getClass() == EntityBabyZombie.class)
		{
			return EntityZombie.class;
		}
		return null;
	}

	protected void func_179446_a(EntityCreature entityCreature, EntityLivingBase target)
	{
		if (entityCreature.getClass() == EntityEnderman.class)
			entityCreature.attackEntityFrom(DamageSource.causeMobDamage(target), 0);
		else
			entityCreature.setAttackTarget(target);
	}

}
