package furgl.babyMobs.common.entity.monster;

import java.util.Collections;
import java.util.List;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBabyZombie extends EntityZombie
{
	private Sorter theNearestAttackableTargetSorter;

	public EntityBabyZombie(World worldIn) 
	{
		super(worldIn);
		this.setChild(true);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(this);
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
//		if (!this.world.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
//			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return ModEntities.getSpawnEgg(this.getClass());
	}

	@Override
	public double getYOffset()
	{
		return super.getYOffset() + 0.1D;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (Config.useSpecialAbilities && this.ticksExisted % 50 == 0 && !this.isRiding())
		{
			double d0 = 35.0D;
			List list = this.world.getEntitiesWithinAABB(EntityChicken.class, this.getEntityBoundingBox().grow(d0, 4.0D, d0), null);
			Collections.sort(list, this.theNearestAttackableTargetSorter);
			if (!list.isEmpty())
			{
				for (int i=0; i<list.size(); i++)
				{
					if (list.get(i).getClass().equals(EntityChicken.class))
					{
						this.setAttackTarget((EntityLivingBase) list.get(i));
						break;
					}
				}
			}
		}
		
		if (!this.isRiding() && this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityChicken)
		{
			if (this.getAttackTarget().getClass().equals(EntityZombieChicken.class))
				this.setAttackTarget(null);
			else if (this.getAttackTarget().getClass().equals(EntityChicken.class))
			{
				this.setLastAttackedEntity(this.getAttackTarget());
				this.getMoveHelper().setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, 1.0D);
				if (this.getDistanceToEntity(this.getAttackTarget()) < 1.0F)
					this.attackEntityAsMob(this.getAttackTarget());
			}
		}
	}

	@Override
	public void onKillEntity(EntityLivingBase entityLivingIn)
	{
		super.onKillEntity(entityLivingIn);
		if (entityLivingIn instanceof EntityChicken)
		{
			EntityZombieChicken entityzombiechicken = new EntityZombieChicken(this.world);
			entityzombiechicken.copyLocationAndAnglesFrom(entityLivingIn);
			this.world.removeEntity(entityLivingIn);
			((EntityChicken)entityLivingIn).setGrowingAge(-2000000);
			entityzombiechicken.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entityzombiechicken)), (IEntityLivingData)null);
			this.world.spawnEntity(entityzombiechicken);
			this.startRiding(entityzombiechicken, true);
			entityzombiechicken.playLivingSound();
		}
	}
}