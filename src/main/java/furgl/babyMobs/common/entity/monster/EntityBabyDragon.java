package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.BabyMobs;
import net.minecraft.block.BlockDragonEgg;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabyDragon extends EntityDragon
{
	public static final DataParameter<BlockPos> DRAGON_BLOCK_POS = EntityDataManager.<BlockPos>createKey(EntityBabyDragon.class, DataSerializers.BLOCK_POS);
	public Vec3d dragonEgg;
	private boolean hasDragonEgg;
	public double targetX;
	public double targetY;
	public double targetZ;
	private BlockPos target;
	private boolean forceNewTarget;

	public EntityBabyDragon(World worldIn)
	{
		super(worldIn);
		this.setSize(0.2F, 0.2F);
		if (!this.world.isRemote)
			this.hasDragonEgg = false;
	}

	public EntityBabyDragon(World world, Vec3d dragonEgg)
	{
		this(world);
		this.dragonEgg = dragonEgg;
		this.hasDragonEgg = true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(Item.getItemFromBlock(Blocks.DRAGON_EGG));
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public boolean isNonBoss()
	{
		return true;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(DRAGON_BLOCK_POS, new BlockPos(0,0,0)); //dragonEgg block pos
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean isInRangeToRenderDist(double distance)
	{
		double d0 = this.getEntityBoundingBox().getAverageEdgeLength();

		if (Double.isNaN(d0))
		{
			d0 = 1.0D;
		}

		d0 = d0 * 64.0D * 5D;
		return distance < d0 * d0;
	}

	@Override
	public void onLivingUpdate()
	{   
		//TODO spawning
		if (!this.world.isRemote && this.ticksExisted == 1)
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
					this.dataManager.set(DRAGON_BLOCK_POS, new BlockPos(dragonEgg));
				}
			}
			else //server and already spawned with egg
			{
				this.dragonEgg = new Vec3d(this.getEntityData().getIntArray("dragonEgg")[0], this.getEntityData().getIntArray("dragonEgg")[1], this.getEntityData().getIntArray("dragonEgg")[2]);
				this.dataManager.set(DRAGON_BLOCK_POS, new BlockPos(dragonEgg));
				this.hasDragonEgg = true;
			}
		}

		if (this.world.isRemote && this.ticksExisted == 2)
		{
			this.dragonEgg = new Vec3d(this.dataManager.get(DRAGON_BLOCK_POS));
			if (!(this.world.getBlockState(new BlockPos(this.dragonEgg)).getBlock() instanceof BlockDragonEgg))
				this.hasDragonEgg = false;
			else
				this.hasDragonEgg = true;
		}

		if (!this.hasDragonEgg && this.ticksExisted > 2 || !this.world.isRemote && this.hasDragonEgg && !(this.world.getBlockState(new BlockPos(this.dragonEgg)).getBlock() instanceof BlockDragonEgg))
			this.setDead();

		if (this.hasDragonEgg)
		{
			if (this.ticksExisted == 3)
				this.targetY = this.dragonEgg.yCoord + 2D;
			else if (this.ticksExisted % 40 == 0)
				this.target = new BlockPos(this.dragonEgg.addVector(rand.nextInt(5)-2, rand.nextDouble()*3, rand.nextInt(5)-2));

			if (this.world.isRemote)
			{
				if (this.ticksExisted % 10 == 0)
				{
					BabyMobs.proxy.spawnEntityDragonParticlesFX(this);
				}
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.dragonEgg.xCoord+rand.nextDouble(), this.dragonEgg.yCoord+rand.nextDouble(), this.dragonEgg.zCoord+rand.nextDouble(), 0,0,0, new int[0]);
			}
		}
		//end

		float f;
		float f1;

		if (this.world.isRemote)
		{
			f = MathHelper.cos(this.animTime * (float)Math.PI * 2.0F);
			f1 = MathHelper.cos(this.prevAnimTime * (float)Math.PI * 2.0F);

			if (f1 <= -0.3F && f >= -0.3F && !this.isSilent())
			{
				this.world.playSound(this.posX, this.posY, this.posZ, SoundEvents.ENTITY_ENDERDRAGON_FLAP, this.getSoundCategory(), this.getSoundVolume(), 0.8F + this.rand.nextFloat() * 0.3F, false);
			}
		}

		this.prevAnimTime = this.animTime;
		float f2;

		if (this.getHealth() <= 0.0F)
		{
			f = (this.rand.nextFloat() - 0.5F) * 8.0F;
			f1 = (this.rand.nextFloat() - 0.5F) * 4.0F;
			f2 = (this.rand.nextFloat() - 0.5F) * 8.0F;
			this.world.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX + f, this.posY + 2.0D + f1, this.posZ + f2, 0.0D, 0.0D, 0.0D, new int[0]);
		}
		else
		{
			f = 0.2F / (MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 10.0F + 1.0F);
			f *= (float)Math.pow(2.0D, this.motionY);

			if (this.slowed)
			{
				this.animTime += f * 0.5F;
			}
			else
			{
				this.animTime += f;
			}

			this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);

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

			if (this.world.isRemote)
			{
				if (this.newPosRotationIncrements > 0)
				{
					d10 = this.posX + (this.interpTargetX - this.posX) / this.newPosRotationIncrements;
					d0 = this.posY + (this.interpTargetY - this.posY) / this.newPosRotationIncrements;
					d1 = this.posZ + (this.interpTargetZ - this.posZ) / this.newPosRotationIncrements;
					d2 = MathHelper.wrapDegrees(this.interpTargetYaw - this.rotationYaw);
					this.rotationYaw = (float)(this.rotationYaw + d2 / this.newPosRotationIncrements);
					this.rotationPitch = (float)(this.rotationPitch + (this.interpTargetPitch - this.rotationPitch) / this.newPosRotationIncrements);
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
					//this.setNewTarget();
				}

				d0 /= MathHelper.sqrt(d10 * d10 + d1 * d1);
				f12 = 0.6F;
				d0 = MathHelper.clamp(d0, (-f12), f12);
				this.motionY += d0 * 0.10000000149011612D;
				this.rotationYaw = MathHelper.wrapDegrees(this.rotationYaw);
				double d4 = 180.0D - Math.atan2(d10, d1) * 180.0D / Math.PI;
				double d6 = MathHelper.wrapDegrees(d4 - this.rotationYaw);//?! was double d6 = MathHelper.wrapAngleTo180_double(d4 - this.rotationYaw);

				if (d6 > 50.0D)
				{
					d6 = 50.0D;
				}

				if (d6 < -50.0D)
				{
					d6 = -50.0D;
				}

				Vec3d vec3 = (new Vec3d(this.targetX - this.posX, this.targetY - this.posY, this.targetZ - this.posZ)).normalize();
				d8 = (-MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F));
				Vec3d vec31 = (new Vec3d(MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F), this.motionY, d8)).normalize();
				float f5 = ((float)vec31.dotProduct(vec3) + 0.5F) / 1.5F;

				if (f5 < 0.0F)
				{
					f5 = 0.0F;
				}

				this.randomYawVelocity *= 0.8F;
				float f6 = MathHelper.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0F + 1.0F;
				double d9 = Math.sqrt(this.motionX * this.motionX + this.motionZ * this.motionZ) * 1.0D + 1.0D;

				if (d9 > 40.0D)
				{
					d9 = 40.0D;
				}

				this.randomYawVelocity = (float)(this.randomYawVelocity + d6 * (0.699999988079071D / d9 / f6));
				this.rotationYaw += this.randomYawVelocity * 0.1F;
				float f7 = (float)(2.0D / (d9 + 1.0D));
				float f8 = 0.06F;
				this.moveRelative(0.0F, -1.0F, f8 * (f5 * f7 + (1.0F - f7)));

				this.move(MoverType.SELF, this.motionX * 0.2D, this.motionY * 0.2D, this.motionZ * 0.2D); //TODO slow

				Vec3d vec32 = (new Vec3d(this.motionX, this.motionY, this.motionZ)).normalize();
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
				float f14 = this.rotationYaw * (float)Math.PI / 180.0F + (float)MathHelper.wrapDegrees(adouble2[0] - adouble1[0]) * (float)Math.PI / 180.0F * 1.0F;
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
	 * Returns true if other Entities should be prevented from moving through this Entity.
	 */
	@Override
	public boolean canBeCollidedWith()
	{
		return false;
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume()
	{
		return 0.1F;
	}

	@Override
	public boolean attackEntityFromPart(EntityDragonPart p_70965_1_, DamageSource p_70965_2_, float p_70965_3_) 
	{
		return false;
	}
}