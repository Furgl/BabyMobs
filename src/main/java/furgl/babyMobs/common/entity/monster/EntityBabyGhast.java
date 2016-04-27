package furgl.babyMobs.common.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.projectile.EntityGhastFireball;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityFlying;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityBabyGhast extends EntityFlying implements IMob
{
	public int courseChangeCooldown;
	public double waypointX;
	public double waypointY;
	public double waypointZ;
	private Entity targetedEntity;
	/** Cooldown time between target loss and new target aquirement. */
	private int aggroCooldown;
	public int prevAttackCounter;
	public int attackCounter;
	/** The explosion radius of spawned fireballs. */
	private int explosionStrength = 1;
	
	private EntityAIBabyFollowParent followAi = new EntityAIBabyFollowParent(this, 1.1D, this.isAIEnabled());

	public EntityBabyGhast(World p_i1735_1_)
	{
		super(p_i1735_1_);
		this.setSize(1.4F, 1.4F);
		this.experienceValue = 12;
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);

		this.isImmuneToFire = true;
	}
	
	//TODO sound and middle click
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
	//end

	@SideOnly(Side.CLIENT)
	public boolean func_110182_bF()
	{
		return this.dataWatcher.getWatchableObjectByte(16) != 0;
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable())
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
	}

	@Override
	protected void updateEntityActionState()
	{
		//TODO followAI
		if (this.getAttackTarget() == null && this.followAi.shouldExecute())
		{
			if (this.followAi.parent != null && this.rand.nextInt(5) == 0)
			{
				this.followAi.startExecuting();
				this.followAi.updateTask();
			}
			else
				this.followAi.resetTask();
		}
		//end
		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}

		this.despawnEntity();
		this.prevAttackCounter = this.attackCounter;
		double d0 = this.waypointX - this.posX;
		double d1 = this.waypointY - this.posY;
		double d2 = this.waypointZ - this.posZ;
		double d3 = d0 * d0 + d1 * d1 + d2 * d2;

		if (d3 < 1.0D || d3 > 3600.0D)
		{
			this.waypointX = this.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			this.waypointY = this.posY + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
			this.waypointZ = this.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
		}

		if (this.courseChangeCooldown-- <= 0)
		{
			this.courseChangeCooldown += this.rand.nextInt(5) + 2;
			d3 = MathHelper.sqrt_double(d3);

			if (this.isCourseTraversable(this.waypointX, this.waypointY, this.waypointZ, d3))
			{
				this.motionX += d0 / d3 * 0.1D;
				this.motionY += d1 / d3 * 0.1D;
				this.motionZ += d2 / d3 * 0.1D;
			}
			else
			{
				this.waypointX = this.posX;
				this.waypointY = this.posY;
				this.waypointZ = this.posZ;
			}
		}

		if (this.targetedEntity != null && this.targetedEntity.isDead)
		{
			this.targetedEntity = null;
		}

		if (this.targetedEntity == null || this.aggroCooldown-- <= 0)
		{
			this.targetedEntity = this.worldObj.getClosestVulnerablePlayerToEntity(this, 100.0D);

			if (this.targetedEntity != null)
			{
				this.aggroCooldown = 20;
			}
		}

		double d4 = 64.0D;

		if (this.targetedEntity != null && this.targetedEntity.getDistanceSqToEntity(this) < d4 * d4)
		{
			double d5 = this.targetedEntity.posX - this.posX;
            double d6 = this.targetedEntity.boundingBox.minY + this.targetedEntity.height / 2.0F - (this.posY + this.height / 2.0F)-3.0F;
			double d7 = this.targetedEntity.posZ - this.posZ;
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(d5, d7)) * 180.0F / (float)Math.PI;

			if (this.canEntityBeSeen(this.targetedEntity))
			{
				if (this.attackCounter == 10)
				{
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1007, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
				}

				++this.attackCounter;

				if (this.attackCounter == 20)
				{
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1008, (int)this.posX, (int)this.posY, (int)this.posZ, 0);
					//TODO EntityGhastFireball (only entity is replaced from orig)
					Vec3 vec3 = this.getLook(1.0F);
					 double d8 = 4.0D;
					if (Config.useSpecialAbilities)
					{
						EntityGhastFireball fireball = new EntityGhastFireball(this.worldObj, this, d5, d6, d7);
						fireball.field_92057_e = this.explosionStrength;
						fireball.posX = this.posX + vec3.xCoord * d8;
	                    fireball.posY = this.posY + this.height / 2.0F - 1.0D;
	                    fireball.posZ = this.posZ + vec3.zCoord * d8;
						this.worldObj.spawnEntityInWorld(fireball);
					}
					else
					{
						EntityLargeFireball fireball = new EntityLargeFireball(this.worldObj, this, d5, d6, d7);
						fireball.field_92057_e = this.explosionStrength;
						fireball.posX = this.posX + vec3.xCoord * d8;
	                    fireball.posY = this.posY + this.height / 2.0F - 1.0D;
	                    fireball.posZ = this.posZ + vec3.zCoord * d8;
						this.worldObj.spawnEntityInWorld(fireball);
					}
					//end
					this.attackCounter = -40;
				}
			}
			else if (this.attackCounter > 0)
			{
				--this.attackCounter;
			}
		}
		else
		{
			this.renderYawOffset = this.rotationYaw = -((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float)Math.PI;

			if (this.attackCounter > 0)
			{
				--this.attackCounter;
			}
		}

		if (!this.worldObj.isRemote)
		{
			byte b1 = this.dataWatcher.getWatchableObjectByte(16);
			byte b0 = (byte)(this.attackCounter > 10 ? 1 : 0);

			if (b1 != b0)
			{
				this.dataWatcher.updateObject(16, Byte.valueOf(b0));
			}
		}
	}

	/**
	 * True if the ghast has an unobstructed line of travel to the waypoint.
	 */
	private boolean isCourseTraversable(double p_70790_1_, double p_70790_3_, double p_70790_5_, double p_70790_7_)
	{
		double d4 = (this.waypointX - this.posX) / p_70790_7_;
		double d5 = (this.waypointY - this.posY) / p_70790_7_;
		double d6 = (this.waypointZ - this.posZ) / p_70790_7_;
		AxisAlignedBB axisalignedbb = this.boundingBox.copy();

		for (int i = 1; i < p_70790_7_; ++i)
		{
			axisalignedbb.offset(d4, d5, d6);

			if (!this.worldObj.getCollidingBoundingBoxes(this, axisalignedbb).isEmpty())
			{
				return false;
			}
		}

		return true;
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
		return this.rand.nextInt(20) == 0 && super.getCanSpawnHere() && this.worldObj.difficultySetting != EnumDifficulty.PEACEFUL;
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
}