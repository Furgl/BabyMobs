package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.projectile.EntitySquidInk;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabySquid extends EntityWaterMob
{
	public float squidPitch;
	public float prevSquidPitch;
	public float squidYaw;
	public float prevSquidYaw;
	/** appears to be rotation in radians; we already have pitch & yaw, so this completes the triumvirate. */
	public float squidRotation;
	/** previous squidRotation in radians */
	public float prevSquidRotation;
	/** angle of the tentacles in radians */
	public float tentacleAngle;
	/** the last calculated angle of the tentacles in radians */
	public float lastTentacleAngle;
	private float randomMotionSpeed;
	/** change in squidRotation in radians. */
	private float rotationVelocity;
	private float field_70871_bB;
	private float randomMotionVecX;
	private float randomMotionVecY;
	private float randomMotionVecZ;
	public EntityBabySquid(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);

		this.rand.setSeed(1 + this.getEntityId());
		this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
		this.tasks.addTask(0, new EntityBabySquid.AIMoveRandom());
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).triggerAchievement(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }

	@Override
	protected boolean canDropLoot()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_squid_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
	}

	@Override
	public float getEyeHeight()
	{
		return this.height * 0.5F;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return null;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return null;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return null;
	}

	//TODO squid ink
	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (Config.useSpecialAbilities)
		{
			if (this.isInWater() && source.getEntity() instanceof EntityLivingBase && this.getHealth() > 0)
			{
				Vec3 vec = new Vec3(this.posX, this.posY, this.posZ);
				EntitySpawner entitySpawner = new EntitySpawner(EntitySquidInk.class, this.worldObj, vec, 5);
				entitySpawner.setShapeSphere(0);
				entitySpawner.setMovementInOut(-1D);
				entitySpawner.setRandVar(0.5D);
				entitySpawner.run();

				this.randomMotionSpeed = 2.0F;
				this.squidRotation = (float) (Math.PI-1);
				this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.slime.attack", 1.0F, this.rand.nextFloat() * 0.4F + 8F);
			}
		}
		return super.attackEntityFrom(source, amount);
	}
	//end

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume()
	{
		return 0.4F;
	}

	@Override
	protected Item getDropItem()
	{
		return null;
	}

	/**
	 * returns if this entity triggers Block.onEntityWalking on the blocks they walk on. used for spiders and wolves to
	 * prevent them from trampling crops
	 */
	@Override
	protected boolean canTriggerWalking()
	{
		return false;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(3 + p_70628_2_) + 1;

		for (int k = 0; k < j; ++k)
		{
			this.entityDropItem(new ItemStack(Items.dye, 1, EnumDyeColor.BLACK.getDyeDamage()), 0.0F);
		}
	}

	/**
	 * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
	 * true)
	 */
	@Override
	public boolean isInWater()
	{
		return this.worldObj.handleMaterialAcceleration(this.getEntityBoundingBox().expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.prevSquidPitch = this.squidPitch;
		this.prevSquidYaw = this.squidYaw;
		this.prevSquidRotation = this.squidRotation;
		this.lastTentacleAngle = this.tentacleAngle;
		this.squidRotation += this.rotationVelocity;

		if (this.squidRotation > (Math.PI * 2D))
		{
			if (this.worldObj.isRemote)
			{
				this.squidRotation = ((float)Math.PI * 2F);
			}
			else
			{
				this.squidRotation = (float)(this.squidRotation - (Math.PI * 2D));

				if (this.rand.nextInt(10) == 0)
				{
					this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
				}

				this.worldObj.setEntityState(this, (byte)19);
			}
		}

		if (this.inWater)
		{
			float f;

			if (this.squidRotation < (float)Math.PI)
			{
				f = this.squidRotation / (float)Math.PI;
				this.tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;

				if (f > 0.75D)
				{
					this.randomMotionSpeed = 1.0F;
					this.field_70871_bB = 1.0F;
				}
				else
				{
					this.field_70871_bB *= 0.8F;
				}
			}
			else
			{
				this.tentacleAngle = 0.0F;
				this.randomMotionSpeed *= 0.9F;
				this.field_70871_bB *= 0.99F;
			}

			if (!this.worldObj.isRemote)
			{
				this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
				this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
				this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
			}

			f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
			this.renderYawOffset += (-((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float)Math.PI - this.renderYawOffset) * 0.1F;
			this.rotationYaw = this.renderYawOffset;
			this.squidYaw = (float)(this.squidYaw + Math.PI * this.field_70871_bB * 1.5D);
			this.squidPitch += (-((float)Math.atan2(f, this.motionY)) * 180.0F / (float)Math.PI - this.squidPitch) * 0.1F;
		}
		else
		{
			this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float)Math.PI * 0.25F;

			if (!this.worldObj.isRemote)
			{
				this.motionX = 0.0D;
				this.motionY -= 0.08D;
				this.motionY *= 0.9800000190734863D;
				this.motionZ = 0.0D;
			}

			this.squidPitch = (float)(this.squidPitch + (-90.0F - this.squidPitch) * 0.02D);
		}
	}

	/**
	 * Moves the entity based on the specified heading.  Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
	{
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere()
	{
		return this.posY > 45.0D && this.posY < 63.0D && super.getCanSpawnHere();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte p_70103_1_)
	{
		if (p_70103_1_ == 19)
		{
			this.squidRotation = 0.0F;
		}
		else
		{
			super.handleStatusUpdate(p_70103_1_);
		}
	}

	public void func_175568_b(float p_175568_1_, float p_175568_2_, float p_175568_3_)
	{
		this.randomMotionVecX = p_175568_1_;
		this.randomMotionVecY = p_175568_2_;
		this.randomMotionVecZ = p_175568_3_;
	}

	public boolean func_175567_n()
	{
		return this.randomMotionVecX != 0.0F || this.randomMotionVecY != 0.0F || this.randomMotionVecZ != 0.0F;
	}

	class AIMoveRandom extends EntityAIBase
	{
		private EntityBabySquid field_179476_a = EntityBabySquid.this;
		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			return true;
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			int i = this.field_179476_a.getAge();

			if (i > 100)
			{
				this.field_179476_a.func_175568_b(0.0F, 0.0F, 0.0F);
			}
			else if (this.field_179476_a.getRNG().nextInt(50) == 0 || !this.field_179476_a.inWater || !this.field_179476_a.func_175567_n())
			{
				float f = this.field_179476_a.getRNG().nextFloat() * (float)Math.PI * 2.0F;
				float f1 = MathHelper.cos(f) * 0.2F;
				float f2 = -0.1F + this.field_179476_a.getRNG().nextFloat() * 0.2F;
				float f3 = MathHelper.sin(f) * 0.2F;
				this.field_179476_a.func_175568_b(f1, f2, f3);
			}
		}
	}
}