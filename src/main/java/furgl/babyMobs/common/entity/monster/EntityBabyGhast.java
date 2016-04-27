package furgl.babyMobs.common.entity.monster;

import java.util.Random;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.projectile.EntityGhastFireball;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabyGhast extends EntityFlying implements IMob
{
	/** The explosion radius of spawned fireballs. */
	private int explosionStrength = 1;

	public EntityBabyGhast(World worldIn)
	{
		super(worldIn);
		this.setSize(1.4F, 1.4F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));

		this.isImmuneToFire = true;
		this.experienceValue = 5;
		this.moveHelper = new EntityBabyGhast.GhastMoveHelper();
		this.tasks.addTask(5, new EntityBabyGhast.AIRandomFly());
		this.tasks.addTask(7, new EntityBabyGhast.AILookAround());
		this.tasks.addTask(7, new EntityBabyGhast.AIFireballAttack());
		this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
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
		return new ItemStack(ModItems.baby_ghast_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@SideOnly(Side.CLIENT)
	public boolean func_110182_bF()
	{
		return this.dataWatcher.getWatchableObjectByte(16) != 0;
	}

	public void func_175454_a(boolean p_175454_1_)
	{
		this.dataWatcher.updateObject(16, Byte.valueOf((byte)(p_175454_1_ ? 1 : 0)));
	}

	public int func_175453_cd()
	{
		return this.explosionStrength;
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		super.onUpdate();

		if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else if ("fireball".equals(source.getDamageType()) && source.getEntity() instanceof EntityPlayer)
		{
			super.attackEntityFrom(source, 1000.0F);
			((EntityPlayer)source.getEntity()).triggerAchievement(AchievementList.ghast);
			return true;
		}
		else
		{
			return super.attackEntityFrom(source, amount);
		}
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(100.0D);
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.ghast.moan";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.ghast.scream";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.ghast.death";
	}

	@Override
	protected Item getDropItem()
	{
		return Items.gunpowder;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(2) + this.rand.nextInt(1 + p_70628_2_);
		int k;

		for (k = 0; k < j; ++k)
		{
			this.dropItem(Items.ghast_tear, 1);
		}

		j = this.rand.nextInt(3) + this.rand.nextInt(1 + p_70628_2_);

		for (k = 0; k < j; ++k)
		{
			this.dropItem(Items.gunpowder, 1);
		}
	}

	/**
	 * Returns the volume for the sounds this mob makes.
	 */
	@Override
	protected float getSoundVolume()
	{
		return 10.0F;
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere()
	{
		return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL;
	}

	/**
	 * Will return how many at most can spawn in a chunk at once.
	 */
	@Override
	public int getMaxSpawnedInChunk()
	{
		return 1;
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("ExplosionPower", this.explosionStrength);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);

		if (tagCompund.hasKey("ExplosionPower", 99))
		{
			this.explosionStrength = tagCompund.getInteger("ExplosionPower");
		}
	}

	@Override
	public float getEyeHeight()
	{
		return 1.3F;
	}

	class AIFireballAttack extends EntityAIBase
	{
		private EntityBabyGhast field_179470_b = EntityBabyGhast.this;
		public int field_179471_a;

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			return this.field_179470_b.getAttackTarget() != null;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void startExecuting()
		{
			this.field_179471_a = 0;
		}

		/**
		 * Resets the task
		 */
		@Override
		public void resetTask()
		{
			this.field_179470_b.func_175454_a(false);
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			EntityLivingBase entitylivingbase = this.field_179470_b.getAttackTarget();
			double d0 = 64.0D;

			if (entitylivingbase.getDistanceSqToEntity(this.field_179470_b) < d0 * d0 && this.field_179470_b.canEntityBeSeen(entitylivingbase))
			{
				World world = this.field_179470_b.worldObj;
				++this.field_179471_a;

				if (this.field_179471_a == 10)
				{
					world.playAuxSFXAtEntity((EntityPlayer)null, 1007, new BlockPos(this.field_179470_b), 0);
				}

				if (this.field_179471_a == 20)
				{
					double d1 = 4.0D;
					Vec3 vec3 = this.field_179470_b.getLook(1.0F);
					double d2 = entitylivingbase.posX - (this.field_179470_b.posX + vec3.xCoord * d1);
					double d3 = entitylivingbase.getEntityBoundingBox().minY + entitylivingbase.height / 2.0F - (0.5D + this.field_179470_b.posY + this.field_179470_b.height / 2.0F);
					double d4 = entitylivingbase.posZ - (this.field_179470_b.posZ + vec3.zCoord * d1);
					world.playAuxSFXAtEntity((EntityPlayer)null, 1008, new BlockPos(this.field_179470_b), 0);
					//TODO EntityGhastFireball (only entity is replaced from orig)
					if (Config.useSpecialAbilities)
					{
						EntityGhastFireball fireball = new EntityGhastFireball(world, this.field_179470_b, d2, d3, d4);
						fireball.explosionPower = this.field_179470_b.func_175453_cd();
						fireball.posX = this.field_179470_b.posX + vec3.xCoord * d1;
						fireball.posY = this.field_179470_b.posY + this.field_179470_b.height / 2.0F + 0.5D;
						fireball.posZ = this.field_179470_b.posZ + vec3.zCoord * d1;
						world.spawnEntityInWorld(fireball);
					}
					else
					{
						EntityLargeFireball fireball = new EntityLargeFireball(world, this.field_179470_b, d2, d3, d4);
						fireball.explosionPower = this.field_179470_b.func_175453_cd();
						fireball.posX = this.field_179470_b.posX + vec3.xCoord * d1;
						fireball.posY = this.field_179470_b.posY + this.field_179470_b.height / 2.0F + 0.5D;
						fireball.posZ = this.field_179470_b.posZ + vec3.zCoord * d1;
						world.spawnEntityInWorld(fireball);
					}
					//end   

					this.field_179471_a = -40;
				}
			}
			else if (this.field_179471_a > 0)
			{
				--this.field_179471_a;
			}

			this.field_179470_b.func_175454_a(this.field_179471_a > 10);
		}
	}

	class AILookAround extends EntityAIBase
	{
		private EntityBabyGhast field_179472_a = EntityBabyGhast.this;

		public AILookAround()
		{
			this.setMutexBits(2);
		}

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
			if (this.field_179472_a.getAttackTarget() == null)
			{
				this.field_179472_a.renderYawOffset = this.field_179472_a.rotationYaw = -((float)Math.atan2(this.field_179472_a.motionX, this.field_179472_a.motionZ)) * 180.0F / (float)Math.PI;
			}
			else
			{
				EntityLivingBase entitylivingbase = this.field_179472_a.getAttackTarget();
				double d0 = 64.0D;

				if (entitylivingbase.getDistanceSqToEntity(this.field_179472_a) < d0 * d0)
				{
					double d1 = entitylivingbase.posX - this.field_179472_a.posX;
					double d2 = entitylivingbase.posZ - this.field_179472_a.posZ;
					this.field_179472_a.renderYawOffset = this.field_179472_a.rotationYaw = -((float)Math.atan2(d1, d2)) * 180.0F / (float)Math.PI;
				}
			}
		}
	}

	class AIRandomFly extends EntityAIBase
	{
		private EntityBabyGhast field_179454_a = EntityBabyGhast.this;

		public AIRandomFly()
		{
			this.setMutexBits(1);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			EntityMoveHelper entitymovehelper = this.field_179454_a.getMoveHelper();

			if (!entitymovehelper.isUpdating())
			{
				return true;
			}
			else
			{
				double d0 = entitymovehelper.func_179917_d() - this.field_179454_a.posX;
				double d1 = entitymovehelper.func_179919_e() - this.field_179454_a.posY;
				double d2 = entitymovehelper.func_179918_f() - this.field_179454_a.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				return d3 < 1.0D || d3 > 3600.0D;
			}
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean continueExecuting()
		{
			return false;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void startExecuting()
		{
			Random random = this.field_179454_a.getRNG();
			double d0 = this.field_179454_a.posX + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d1 = this.field_179454_a.posY + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			double d2 = this.field_179454_a.posZ + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
			this.field_179454_a.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
		}
	}

	class GhastMoveHelper extends EntityMoveHelper
	{
		private EntityBabyGhast field_179927_g = EntityBabyGhast.this;
		private int field_179928_h;

		public GhastMoveHelper()
		{
			super(EntityBabyGhast.this);
		}

		@Override
		public void onUpdateMoveHelper()
		{
			if (this.update)
			{
				double d0 = this.posX - this.field_179927_g.posX;
				double d1 = this.posY - this.field_179927_g.posY;
				double d2 = this.posZ - this.field_179927_g.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;

				if (this.field_179928_h-- <= 0)
				{
					this.field_179928_h += this.field_179927_g.getRNG().nextInt(5) + 2;
					d3 = MathHelper.sqrt_double(d3);

					if (this.func_179926_b(this.posX, this.posY, this.posZ, d3))
					{
						this.field_179927_g.motionX += d0 / d3 * 0.1D;
						this.field_179927_g.motionY += d1 / d3 * 0.1D;
						this.field_179927_g.motionZ += d2 / d3 * 0.1D;
					}
					else
					{
						this.update = false;
					}
				}
			}
		}

		private boolean func_179926_b(double p_179926_1_, double p_179926_3_, double p_179926_5_, double p_179926_7_)
		{
			double d4 = (p_179926_1_ - this.field_179927_g.posX) / p_179926_7_;
			double d5 = (p_179926_3_ - this.field_179927_g.posY) / p_179926_7_;
			double d6 = (p_179926_5_ - this.field_179927_g.posZ) / p_179926_7_;
			AxisAlignedBB axisalignedbb = this.field_179927_g.getEntityBoundingBox();

			for (int i = 1; i < p_179926_7_; ++i)
			{
				axisalignedbb = axisalignedbb.offset(d4, d5, d6);

				if (!this.field_179927_g.worldObj.getCollidingBoundingBoxes(this.field_179927_g, axisalignedbb).isEmpty())
				{
					return false;
				}
			}

			return true;
		}
	}
}