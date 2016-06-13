package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityBabyGuardian extends EntityGuardian
{
	private boolean longerSpikes = false;
	private int spikesCounter = 0;

	public EntityBabyGuardian(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 0.5F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }
	
	public boolean hasTargetedEntity()
    {
		DataParameter<Integer> dataParam = (DataParameter<Integer>) ReflectionHelper.getPrivateValue(EntityGuardian.class, this, 1);
        return this.dataWatcher.get(dataParam) != 0;
    }

	@Override
	protected boolean canDropLoot()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(ModItems.baby_guardian_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected SoundEvent getHurtSound()
	{
		if (this.longerSpikes)//TODO spike sound
			return SoundEvents.entity_blaze_hurt;
		else
	        return this.isInWater() ? SoundEvents.entity_guardian_hurt : SoundEvents.entity_guardian_hurt_land;
	}

	//TODO longer spikes getter
	public boolean longerSpikes()
	{
		return longerSpikes;
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer entityIn)
	{
		if (this.longerSpikes)
		{
			if (entityIn.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F))
				this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.entity_blaze_hurt, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 8F, false);
		}
	}
	//end

	@Override
	public void onLivingUpdate()
	{
		//TODO longerSpikes counter
		if (this.longerSpikes)
		{
			this.setAttackTarget(null);
			this.spikesCounter--;
			if (this.spikesCounter == 0)
			{
				this.longerSpikes = false;
				this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.entity_blaze_hurt, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 8F, false);
			}
			if (this.attackingPlayer instanceof EntityPlayer && !(this.attackingPlayer instanceof FakePlayer))
			{
				if (this.inWater)
					this.getNavigator().tryMoveToEntityLiving(this.attackingPlayer, 2D);
				else
				{
					this.faceEntity(this.attackingPlayer, 100, 100);
					Vec3d vec = this.attackingPlayer.getPositionVector().subtract(this.getPositionVector()).normalize();
					this.motionX = vec.xCoord/4;
					this.motionZ = vec.zCoord/4;
				}
			}
		}
		//end

		super.onLivingUpdate();
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		//TODO render spikes longer on attack and play sound
		if (Config.useSpecialAbilities)
		{
			if (source.getSourceOfDamage() instanceof EntityLivingBase)
			{
				if (!this.longerSpikes)
					this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.entity_blaze_hurt, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 8F, false);
				this.longerSpikes = true;
				this.spikesCounter = 50;
			}
		}
		//end 

		return super.attackEntityFrom(source, amount);
	}
}