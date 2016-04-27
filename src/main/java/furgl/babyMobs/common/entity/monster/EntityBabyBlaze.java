package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityBlazeFlamethrower;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabyBlaze extends EntityMob
{
	/** Random offset used in floating behaviour */
	private float heightOffset = 0.5F;
	/** ticks until heightOffset is randomized */
	private int heightOffsetUpdateTime;

	public EntityBabyBlaze(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));

		this.isImmuneToFire = true;
		this.tasks.addTask(4, new EntityBabyBlaze.AIFireballAttack());
		this.tasks.addTask(5, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
	}
	
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).triggerAchievement(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }

	@Override
	protected boolean func_146066_aG()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_blaze_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.7F;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.23000000417232513D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(48.0D);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
		this.dataWatcher.addObject(17, 0);//added
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.blaze.breathe";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.blaze.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.blaze.death";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 15728880;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_)
	{
		return 1.0F;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{

		//TODO flamethrower
		if (Config.useSpecialAbilities) 
		{
			if (!this.worldObj.isRemote)
			{
				if (this.getAttackTarget() instanceof EntityPlayer && this.canEntityBeSeen(this.getAttackTarget()) && !(this.getAttackTarget() instanceof FakePlayer) && !(((EntityPlayer)this.getAttackTarget()).capabilities.isCreativeMode))
				{
					EntityLivingBase entitylivingbase = this.getAttackTarget();
					double d0 = this.getDistanceSqToEntity(entitylivingbase);

					if (d0 < 40.0D)
						this.dataWatcher.updateObject(17, 1);
					else
						this.dataWatcher.updateObject(17, 0);
				}
				else
					this.dataWatcher.updateObject(17, 0);
			}
			if (this.dataWatcher.getWatchableObjectInt(17) == 1 && this.getHealth() > 0)
			{
				EntityLivingBase entitylivingbase = this.getAttackTarget();
				if (entitylivingbase instanceof EntityPlayer && !(entitylivingbase instanceof FakePlayer))
				{
					this.getLookHelper().setLookPosition(entitylivingbase.posX, entitylivingbase.posY+entitylivingbase.getEyeHeight()/2, entitylivingbase.posZ, 100F, 100F);
					if (this.posY > entitylivingbase.posY+2)
						this.motionY = -1D;
					else if (this.posY < entitylivingbase.posY-2)
						this.motionY = 1D;
					else
						this.motionY *= 0.1D;
				}
				if (this.ticksExisted % 10 == 0)
					this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.ghast.fireball", 1.0F, this.rand.nextFloat() * 0.2F + 0.0F);
				Vec3 vec = new Vec3(this.posX, 0.5 + this.posY, this.posZ);

				if (this.ticksExisted % 10 == 0 && entitylivingbase instanceof EntityPlayer && !(entitylivingbase instanceof FakePlayer))
				{
					EntitySpawner entitySpawner = new EntitySpawner(EntityBlazeFlamethrower.class, worldObj, vec, 1);
					entitySpawner.setShapeLine(0.5D, this.rotationPitch, this.rotationYawHead);
					entitySpawner.setMovementFollowShape(0.2D);
					entitySpawner.setDelay(8);
					entitySpawner.setRandVar(0.2D);
					entitySpawner.run();
				}
				BabyMobs.proxy.spawnEntityBlazeFlamethrowerFX(this, vec);
			}
		} 
		//end 

		if (!this.onGround && this.motionY < 0.0D)
		{
			this.motionY *= 0.6D;
		}

		if (this.worldObj.isRemote)
		{
			if (this.rand.nextInt(24) == 0 && !this.isSilent())
			{
				this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
			}

			for (int i = 0; i < 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}

		super.onLivingUpdate();
	}

	@Override
	protected void updateAITasks()
	{
		if (this.isWet())
		{
			this.attackEntityFrom(DamageSource.drown, 1.0F);
		}

		--this.heightOffsetUpdateTime;

		if (this.heightOffsetUpdateTime <= 0)
		{
			this.heightOffsetUpdateTime = 100;
			this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
		}

		EntityLivingBase entitylivingbase = this.getAttackTarget();

		if (entitylivingbase != null && entitylivingbase.posY + entitylivingbase.getEyeHeight() > this.posY + this.getEyeHeight() + this.heightOffset)
		{
			this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
			this.isAirBorne = true;
		}

		super.updateAITasks();
	}

	@Override
	public void fall(float distance, float damageMultiplier) 
	{

	}

	@Override
	protected Item getDropItem()
	{
		return Items.blaze_rod;
	}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
	 */
	@Override
	public boolean isBurning()
	{
		return this.func_70845_n();
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		if (p_70628_1_)
		{
			int j = this.rand.nextInt(2 + p_70628_2_);

			for (int k = 0; k < j; ++k)
			{
				this.dropItem(Items.blaze_rod, 1);
			}
		}
	}

	public boolean func_70845_n()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void func_70844_e(boolean p_70844_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (p_70844_1_)
		{
			b0 = (byte)(b0 | 1);
		}
		else
		{
			b0 &= -2;
		}

		this.dataWatcher.updateObject(16, Byte.valueOf(b0));
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	@Override
	protected boolean isValidLightLevel()
	{
		return true;
	}

	class AIFireballAttack extends EntityAIBase
	{
		private EntityBabyBlaze entityBabyBlaze = EntityBabyBlaze.this;
		private int field_179467_b;
		private int field_179468_c;
		public AIFireballAttack()
		{
			this.setMutexBits(3);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			EntityLivingBase entitylivingbase = this.entityBabyBlaze.getAttackTarget();
			return entitylivingbase != null && entitylivingbase.isEntityAlive();
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void startExecuting()
		{
			this.field_179467_b = 0;
		}

		/**
		 * Resets the task
		 */
		@Override
		public void resetTask()
		{
			this.entityBabyBlaze.func_70844_e(false);
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			--this.field_179468_c;
			EntityLivingBase entitylivingbase = this.entityBabyBlaze.getAttackTarget();
			double d0 = this.entityBabyBlaze.getDistanceSqToEntity(entitylivingbase);

			if (!Config.useSpecialAbilities)
			{
				if (d0 < 4.0D)
				{
					if (this.field_179468_c <= 0)
					{
						this.field_179468_c = 20;
						this.entityBabyBlaze.attackEntityAsMob(entitylivingbase);
					}

					this.entityBabyBlaze.getMoveHelper().setMoveTo(entitylivingbase.posX, entitylivingbase.posY, entitylivingbase.posZ, 1.0D);
				}
			}
			if ((d0 < 256.0D && d0 > 40D && Config.useSpecialAbilities) || (d0 < 256.0D && !Config.useSpecialAbilities)) 
			{
				double d1 = entitylivingbase.posX - this.entityBabyBlaze.posX;
				double d2 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 2.0F - (this.entityBabyBlaze.posY + this.entityBabyBlaze.height / 2.0F);
				double d3 = entitylivingbase.posZ - this.entityBabyBlaze.posZ;

				if (this.field_179468_c <= 0)
				{
					++this.field_179467_b;

					if (this.field_179467_b == 1)
					{
						this.field_179468_c = 60;
						this.entityBabyBlaze.func_70844_e(true);
					}
					else if (this.field_179467_b <= 4)
					{
						this.field_179468_c = 6;
					}
					else
					{
						this.field_179468_c = 100;
						this.field_179467_b = 0;
						this.entityBabyBlaze.func_70844_e(false);
					}

					if (this.field_179467_b > 1)
					{
						float f = MathHelper.sqrt_float(MathHelper.sqrt_double(d0)) * 0.5F;
						this.entityBabyBlaze.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, new BlockPos((int)this.entityBabyBlaze.posX, (int)this.entityBabyBlaze.posY, (int)this.entityBabyBlaze.posZ), 0);

						for (int i = 0; i < 1; ++i)
						{
							EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.entityBabyBlaze.worldObj, this.entityBabyBlaze, d1 + this.entityBabyBlaze.getRNG().nextGaussian() * f, d2, d3 + this.entityBabyBlaze.getRNG().nextGaussian() * f);
							entitysmallfireball.posY = this.entityBabyBlaze.posY + this.entityBabyBlaze.height / 2.0F + 0.5D;
							this.entityBabyBlaze.worldObj.spawnEntityInWorld(entitysmallfireball);
						}
					}
				}

				this.entityBabyBlaze.getLookHelper().setLookPositionWithEntity(entitylivingbase, 10.0F, 10.0F);
			}

			super.updateTask();
		}
	}
}