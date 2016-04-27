package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.BabyMobs;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityMultiPart;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.monster.IMob;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityBabyDragon extends EntityLiving implements /*IBossDisplayData, */IEntityMultiPart, IMob
{
	public double targetX;
	public double targetY;
	public double targetZ;
	/** Ring buffer array for the last 64 Y-positions and yaw rotations. Used to calculate offsets for the animations. */
	public double[][] ringBuffer = new double[64][3];
	/** Index into the ring buffer. Incremented once per tick and restarts at 0 once it reaches the end of the buffer. */
	public int ringBufferIndex = -1;
	/** An array containing all body parts of this dragon */
	public EntityDragonPart[] dragonPartArray;
	/** The head bounding box of a dragon */
	public EntityDragonPart dragonPartHead;
	/** The body bounding box of a dragon */
	public EntityDragonPart dragonPartBody;
	public EntityDragonPart dragonPartTail1;
	public EntityDragonPart dragonPartTail2;
	public EntityDragonPart dragonPartTail3;
	public EntityDragonPart dragonPartWing1;
	public EntityDragonPart dragonPartWing2;
	/** Animation time at previous tick. */
	public float prevAnimTime;
	/** Animation time, used to control the speed of the animation cycles (wings flapping, jaw opening, etc.) */
	public float animTime;
	/** Force selecting a new flight target at next tick if set to true. */
	public boolean forceNewTarget;
	/** Activated if the dragon is flying though obsidian, white stone or bedrock. Slows movement and animation speed. */
	public boolean slowed;
	private BlockPos target;
	public int deathTicks;

	public Vec3 dragonEgg;
	private boolean hasDragonEgg;

	public EntityBabyDragon(World worldIn)
	{
		super(worldIn);
		this.setSize(0.2F, 0.2F);
		this.targetY = 80.0D;
		if (!this.worldObj.isRemote)
			this.hasDragonEgg = false;
		this.renderDistanceWeight = 5D;

		this.dragonPartArray = new EntityDragonPart[] {this.dragonPartHead = new EntityDragonPart(this, "head", 6.0F, 6.0F), this.dragonPartBody = new EntityDragonPart(this, "body", 8.0F, 8.0F), this.dragonPartTail1 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail2 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartTail3 = new EntityDragonPart(this, "tail", 4.0F, 4.0F), this.dragonPartWing1 = new EntityDragonPart(this, "wing", 4.0F, 4.0F), this.dragonPartWing2 = new EntityDragonPart(this, "wing", 4.0F, 4.0F)};
		this.setHealth(this.getMaxHealth());
		this.noClip = true;
		this.isImmuneToFire = true;
		this.ignoreFrustumCheck = true;
	}

	public EntityBabyDragon(World world, Vec3 dragonEgg)
	{
		this(world);
		this.dragonEgg = dragonEgg;
		this.hasDragonEgg = true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(Item.getItemFromBlock(Blocks.dragon_egg));
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
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(150.0D);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(29, Integer.valueOf(0)); //dragonEgg x
		this.dataWatcher.addObject(30, Integer.valueOf(0)); //dragonEgg y
		this.dataWatcher.addObject(31, Integer.valueOf(0)); //dragonEgg z
	}

	/**
	 * Returns a double[3] array with movement offsets, used to calculate trailing tail/neck positions. [0] = yaw
	 * offset, [1] = y offset, [2] = unused, always 0. Parameters: buffer index offset, partial ticks.
	 */
	public double[] getMovementOffsets(int p_70974_1_, float p_70974_2_)
	{
		if (this.getHealth() <= 0.0F)
		{
			p_70974_2_ = 0.0F;
		}

		p_70974_2_ = 1.0F - p_70974_2_;
		int j = this.ringBufferIndex - p_70974_1_ * 1 & 63;
		int k = this.ringBufferIndex - p_70974_1_ * 1 - 1 & 63;
		double[] adouble = new double[3];
		double d0 = this.ringBuffer[j][0];
		double d1 = MathHelper.wrapAngleTo180_double(this.ringBuffer[k][0] - d0);
		adouble[0] = d0 + d1 * p_70974_2_;
		d0 = this.ringBuffer[j][1];
		d1 = this.ringBuffer[k][1] - d0;
		adouble[1] = d0 + d1 * p_70974_2_;
		adouble[2] = this.ringBuffer[j][2] + (this.ringBuffer[k][2] - this.ringBuffer[j][2]) * p_70974_2_;
		return adouble;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{    	
		//TODO spawning
		if (!this.worldObj.isRemote && this.ticksExisted == 1)
		{
			if (!this.getEntityData().hasKey("dragonEgg"))
			{
				if (this.hasDragonEgg) //server and just spawned by egg
				{
					int[] array = new int[3];
					array[0] = (int) this.dragonEgg.xCoord;
					array[1] = (int) this.dragonEgg.yCoord;
					array[2] = (int) this.dragonEgg.zCoord;
					this.getEntityData().setIntArray("dragonEgg", array);
					this.dataWatcher.updateObject(29, (int) dragonEgg.xCoord);
					this.dataWatcher.updateObject(30, (int) dragonEgg.yCoord);
					this.dataWatcher.updateObject(31, (int) dragonEgg.zCoord);
				}
			}
			else //server and already spawned with egg
			{
				this.dragonEgg = new Vec3(this.getEntityData().getIntArray("dragonEgg")[0], this.getEntityData().getIntArray("dragonEgg")[1], this.getEntityData().getIntArray("dragonEgg")[2]);
				this.dataWatcher.updateObject(29, (int) dragonEgg.xCoord);
				this.dataWatcher.updateObject(30, (int) dragonEgg.yCoord);
				this.dataWatcher.updateObject(31, (int) dragonEgg.zCoord);
				this.hasDragonEgg = true;
			}
		}

		if (this.worldObj.isRemote && this.ticksExisted == 2)
		{
			this.dragonEgg = new Vec3(this.dataWatcher.getWatchableObjectInt(29), this.dataWatcher.getWatchableObjectInt(30), this.dataWatcher.getWatchableObjectInt(31));
			if (!(this.worldObj.getBlockState(new BlockPos(this.dragonEgg)).getBlock() instanceof BlockDragonEgg))
				this.hasDragonEgg = false;
			else
				this.hasDragonEgg = true;
		}

		if (!this.hasDragonEgg && this.ticksExisted > 2 || !this.worldObj.isRemote && this.hasDragonEgg && !(this.worldObj.getBlockState(new BlockPos(this.dragonEgg)).getBlock() instanceof BlockDragonEgg))
			this.setDead();

		if (this.hasDragonEgg)
		{
			if (this.ticksExisted == 3)
				this.targetY = this.dragonEgg.yCoord + 2D;
			else if (this.ticksExisted % 40 == 0)
				this.target = new BlockPos(this.dragonEgg.addVector(rand.nextInt(5)-2, rand.nextDouble()*3, rand.nextInt(5)-2));

			if (this.worldObj.isRemote)
			{
				if (this.ticksExisted % 10 == 0)
				{
					BabyMobs.proxy.spawnEntityDragonParticlesFX(this);
				}
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.dragonEgg.xCoord+rand.nextDouble(), this.dragonEgg.yCoord+rand.nextDouble(), this.dragonEgg.zCoord+rand.nextDouble(), 0,0,0, new int[0]);
			}
		}
		//end

		float f;
		float f1;

		if (this.worldObj.isRemote)
		{
			f = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
			f1 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

			if (f1 <= -0.3F && f >= -0.3F && !this.isSilent())
			{
				this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.enderdragon.wings", this.getSoundVolume(), 0.8F + this.rand.nextFloat() * 0.3F, false);
			}
		}

		this.prevAnimTime = this.animTime;
		float f2;

		if (this.getHealth() <= 0.0F)
		{
			f = (this.rand.nextFloat() - 0.5F) * 8.0F;
			f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
			f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			this.worldObj.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		else
		{
			f = 0.2F / (MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
			f *= (float)Math.pow(2.0D, this.motionY);

			if (this.slowed)
			{
				this.animTime += f * 0.5F;
			}
			else
			{
				this.animTime += f;
			}

			this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);

			if (this.ringBufferIndex < 0)
			{
				for (int i = 0; i < this.ringBuffer.length; ++i)
				{
					this.ringBuffer[i][0] = this.rotationYaw;
					this.ringBuffer[i][1] = this.posY;
				}
			}

			if (++this.ringBufferIndex == this.ringBuffer.length)
			{
				this.ringBufferIndex = 0;
			}

			this.ringBuffer[this.ringBufferIndex][0] = this.rotationYaw;
			this.ringBuffer[this.ringBufferIndex][1] = this.posY;
			double d0;
			double d1;
			double d2;
			double d10;
			float f12;

			if (this.worldObj.isRemote)
			{
				if (this.newPosRotationIncrements > 0)
				{
					d10 = this.posX + (this.newPosX - this.posX) / this.newPosRotationIncrements;
					d0 = this.posY + (this.newPosY - this.posY) / this.newPosRotationIncrements;
					d1 = this.posZ + (this.newPosZ - this.posZ) / this.newPosRotationIncrements;
					d2 = MathHelper.wrapAngleTo180_double(this.newRotationYaw - this.rotationYaw);
					this.rotationYaw = (float)(this.rotationYaw + d2 / this.newPosRotationIncrements);
					this.rotationPitch = (float)(this.rotationPitch + (this.newRotationPitch - this.rotationPitch) / this.newPosRotationIncrements);
					--this.newPosRotationIncrements;
					this.setPosition(d10, d0, d1);
					this.setRotation(this.rotationYaw, this.rotationPitch);
				}
			}
			else
			{
				d10 = this.targetX - this.posX;
				d0 = this.targetY - this.posY;
				d1 = this.targetZ - this.posZ;
				d2 = d10 * d10 + d0 * d0 + d1 * d1;
				double d8;

				if (this.target != null)
				{
					this.targetX = this.target.getX();
					this.targetZ = this.target.getZ();
					double d3 = this.targetX - this.posX;
					double d5 = this.targetZ - this.posZ;
					double d7 = Math.sqrt(d3 * d3 + d5 * d5);
					d8 = 0.4000000059604645D + d7 / 80.0D - 1.0D;

					if (d8 > 10.0D)
					{
						d8 = 10.0D;
					}

					this.targetY = this.target.getY()+2D + d8; //TODO target height
				}
				else
				{
					this.targetX += this.rand.nextGaussian() * 2.0D;
					this.targetZ += this.rand.nextGaussian() * 2.0D;
				}

				if (this.forceNewTarget || d2 < 100.0D || d2 > 22500.0D || this.isCollidedHorizontally || this.isCollidedVertically)
				{
					this.setNewTarget();
				}

				d0 /= MathHelper.sqrt_double(d10 * d10 + d1 * d1);
				f12 = 0.6F;
				d0 = MathHelper.clamp_double(d0, (-f12), f12);
				this.motionY += d0 * 0.10000000149011612D;
				this.rotationYaw = MathHelper.wrapAngleTo180_float(this.rotationYaw);
				double d4 = 180.0D - Math.atan2(d10, d1) * 180.0D / Math.PI;
				double d6 = MathHelper.wrapAngleTo180_double(d4 - this.rotationYaw);

				if (d6 > 50.0D)
				{
					d6 = 50.0D;
				}

				if (d6 < -50.0D)
				{
					d6 = -50.0D;
				}

				Vec3 vec3 = (new Vec3(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ)).normalize();
				d8 = (-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F));
				Vec3 vec31 = (new Vec3(MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, d8)).normalize();
				float f5 = ((float)vec31.dotProduct(vec3) + 0.5F) / 1.5F;

				if (f5 < 0.0F)
				{
					f5 = 0.0F;
				}

				this.randomYawVelocity *= 0.8F;
				float f6 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
				double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;

				if (d9 > 40.0D)
				{
					d9 = 40.0D;
				}

				this.randomYawVelocity = (float)(this.randomYawVelocity + d6 * (0.699999988079071D / d9 / f6));
				this.rotationYaw += this.randomYawVelocity * 0.1F;
				float f7 = (float)(2.0D / (d9 + 1.0D));
				float f8 = 0.06F;
				this.moveFlying(0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));

				this.moveEntity(this.motionX * 0.2D, this.motionY * 0.2D, this.motionZ * 0.2D); //TODO slow

				Vec3 vec32 = (new Vec3(this.motionX, this.motionY, this.motionZ)).normalize();
				float f9 = ((float)vec32.dotProduct(vec31) + 1.0F) / 2.0F;
				f9 = 0.8F + 0.15F * f9;
				this.motionX *= f9;
				this.motionZ *= f9;
				this.motionY *= 0.9100000262260437D;
			}

			this.renderYawOffset = this.rotationYaw;
			this.dragonPartHead.width = this.dragonPartHead.height = 3.0F;
			this.dragonPartTail1.width = this.dragonPartTail1.height = 2.0F;
			this.dragonPartTail2.width = this.dragonPartTail2.height = 2.0F;
			this.dragonPartTail3.width = this.dragonPartTail3.height = 2.0F;
			this.dragonPartBody.height = 3.0F;
			this.dragonPartBody.width = 5.0F;
			this.dragonPartWing1.height = 2.0F;
			this.dragonPartWing1.width = 4.0F;
			this.dragonPartWing2.height = 3.0F;
			this.dragonPartWing2.width = 4.0F;
			f1 = (float)(this.getMovementOffsets(5, 1.0F)[1] - this.getMovementOffsets(10, 1.0F)[1]) * 10.0F / 180.0F * (float)Math.PI;
			f2 = MathHelper.cos(f1);
			float f10 = -MathHelper.sin(f1);
			float f3 = this.rotationYaw * (float)Math.PI / 180.0F;
			float f11 = MathHelper.sin(f3);
			float f4 = MathHelper.cos(f3);
			this.dragonPartBody.onUpdate();
			this.dragonPartBody.setLocationAndAngles(this.posX + f11 * 0.5F, this.posY, this.posZ - f4 * 0.5F, 0.0F, 0.0F);
			this.dragonPartWing1.onUpdate();
			this.dragonPartWing1.setLocationAndAngles(this.posX + f4 * 4.5F, this.posY + 2.0D, this.posZ + f11 * 4.5F, 0.0F, 0.0F);
			this.dragonPartWing2.onUpdate();
			this.dragonPartWing2.setLocationAndAngles(this.posX - f4 * 4.5F, this.posY + 2.0D, this.posZ - f11 * 4.5F, 0.0F, 0.0F);

			double[] adouble1 = this.getMovementOffsets(5, 1.0F);
			double[] adouble = this.getMovementOffsets(0, 1.0F);
			f12 = MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
			float f13 = MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F - this.randomYawVelocity * 0.01F);
			this.dragonPartHead.onUpdate();
			this.dragonPartHead.setLocationAndAngles(this.posX + f12 * 5.5F * f2, this.posY + (adouble[1] - adouble1[1]) * 1.0D + f10 * 5.5F, this.posZ - f13 * 5.5F * f2, 0.0F, 0.0F);

			for (int j = 0; j < 3; ++j)
			{
				EntityDragonPart entitydragonpart = null;

				if (j == 0)
				{
					entitydragonpart = this.dragonPartTail1;
				}

				if (j == 1)
				{
					entitydragonpart = this.dragonPartTail2;
				}

				if (j == 2)
				{
					entitydragonpart = this.dragonPartTail3;
				}

				double[] adouble2 = this.getMovementOffsets(12 + j * 2, 1.0F);
				float f14 = this.rotationYaw * (float)Math.PI / 180.0F + this.simplifyAngle(adouble2[0] - adouble1[0]) * (float)Math.PI / 180.0F * 1.0F;
				float f15 = MathHelper.sin(f14);
				float f16 = MathHelper.cos(f14);
				float f17 = 1.5F;
				float f18 = (j + 1) * 2.0F;
				entitydragonpart.onUpdate();
				entitydragonpart.setLocationAndAngles(this.posX - (f11 * f17 + f15 * f18) * f2, this.posY + (adouble2[1] - adouble1[1]) * 1.0D - (f18 + f17) * f10 + 1.5D, this.posZ + (f4 * f17 + f16 * f18) * f2, 0.0F, 0.0F);
			}
		}
	}


	/**
	 * Sets a new target for the flight AI. It can be a random coordinate or a nearby player.
	 */
	private void setNewTarget()
	{

	}

	/**
	 * Simplifies the value of a number by adding/subtracting 180 to the point that the number is between -180 and 180.
	 */
	private float simplifyAngle(double p_70973_1_)
	{
		return (float)MathHelper.wrapAngleTo180_double(p_70973_1_);
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		return false;
	}

	/**
	 * Called by the /kill command.
	 */
	@Override
	public void onKillCommand()//TODO onKill
	{
		if (!this.hasDragonEgg)
			this.setDead();
		return;
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	@Override
	protected void despawnEntity() {}

	/**
	 * Return the Entity parts making up this Entity (currently only for dragons)
	 */
	@Override
	public Entity[] getParts()
	{
		return this.dragonPartArray;
	}

	/**
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	@Override
	public World getWorld()
	{
		return this.worldObj;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.enderdragon.growl";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.enderdragon.hit";
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume()
	{
		return 1.5F;
	}

	@Override
	public boolean attackEntityFromPart(EntityDragonPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_) 
	{
		return false;
	}
}