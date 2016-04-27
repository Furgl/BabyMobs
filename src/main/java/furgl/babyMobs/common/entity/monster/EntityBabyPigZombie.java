package furgl.babyMobs.common.entity.monster;

import java.util.Collections;
import java.util.List;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyPigZombie extends EntityPigZombie
{
	private Sorter theNearestAttackableTargetSorter;

	public EntityBabyPigZombie(World worldIn)
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
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).triggerAchievement(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_pig_zombie_egg);
	}

	@Override
	public double getYOffset()
	{
		return super.getYOffset() + 0.4D;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		//Sync anger with zombie pig
		if (!this.worldObj.isRemote)
		{
			if (!this.isAngry() && this.ridingEntity instanceof EntityZombiePig &&((EntityZombiePig)this.ridingEntity).getAttackTarget() != null)
			{
				EntityLivingBase target = ((EntityZombiePig) this.ridingEntity).getAttackTarget();
				if ((((EntityZombiePig)this.ridingEntity).getAttackTarget() != null) && target != null && target instanceof EntityPlayer && !(target instanceof FakePlayer))
				{
					this.attackEntityFrom(DamageSource.causePlayerDamage((EntityPlayer) target), 0);
				}
			}
		}

		//Target pigs when not riding
		if (Config.useSpecialAbilities && this.ticksExisted % 50 == 0 && !this.isRiding())
		{
			double d0 = 35.0D;
			List list = this.worldObj.getEntitiesWithinAABB(EntityPig.class, this.getEntityBoundingBox().expand(d0, 4.0D, d0), null);
			Collections.sort(list, this.theNearestAttackableTargetSorter);
			if (!list.isEmpty())
			{
				for (int i=0; i<list.size(); i++)
				{
					if (list.get(i).getClass().equals(EntityPig.class))
					{
						this.setAttackTarget((EntityLivingBase) list.get(i));
						break;
					}
				}
			}
		}

		if (!this.isRiding() && this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityPig)
		{
			if (this.getAttackTarget().getClass().equals(EntityZombiePig.class))
				this.setAttackTarget(null);
			else if (this.getAttackTarget().getClass().equals(EntityPig.class))
			{
				this.setLastAttacker(this.getAttackTarget());
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
		if (entityLivingIn instanceof EntityPig)
		{
			EntityZombiePig entityzombiepig = new EntityZombiePig(this.worldObj);
			entityzombiepig.copyLocationAndAnglesFrom(entityLivingIn);
			this.worldObj.removeEntity(entityLivingIn);
			((EntityPig)entityLivingIn).setGrowingAge(-2000000);
			entityzombiepig.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(entityzombiepig)), (IEntityLivingData)null);
			this.worldObj.spawnEntityInWorld(entityzombiepig);
			this.mountEntity(entityzombiepig);
			entityzombiepig.playLivingSound();
		}
	}
}