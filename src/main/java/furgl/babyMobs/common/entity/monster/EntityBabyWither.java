package furgl.babyMobs.common.entity.monster;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.projectile.EntityWitherWitherSkull;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.boss.IBossDisplayData;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabyWither extends EntityMob implements IBossDisplayData, IRangedAttackMob
{
	private float[] field_82220_d = new float[2];
	private float[] field_82221_e = new float[2];
	private float[] field_82217_f = new float[2];
	private float[] field_82218_g = new float[2];
	private int[] field_82223_h = new int[2];
	private int[] field_82224_i = new int[2];
	/** Time before the Wither tries to break blocks */
	private int blockBreakCounter;
	/** Selector used to determine the entities a wither boss should attack. */
	private static final Predicate attackEntitySelector = new Predicate()
	{
		public boolean func_180027_a(Entity p_180027_1_)
		{
			return p_180027_1_ instanceof EntityLivingBase && ((EntityLivingBase)p_180027_1_).getCreatureAttribute() != EnumCreatureAttribute.UNDEAD;
		}
		@Override
		public boolean apply(Object p_apply_1_)
		{
			return this.func_180027_a((Entity)p_apply_1_);
		}
	};
	public EntityBabyWither(World worldIn)
	{
		super(worldIn);
		this.setHealth(this.getMaxHealth());
		this.setSize(0.6F, 1.7F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);

		this.isImmuneToFire = true;
		((PathNavigateGround)this.getNavigator()).setCanSwim(true);
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 40, 20.0F));
		this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(7, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, false, attackEntitySelector));
		this.experienceValue = 50;
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
		return new ItemStack(ModItems.baby_wither_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(21, new Integer(0)); //orig 17
		this.dataWatcher.addObject(22, new Integer(0)); //orig 18
		this.dataWatcher.addObject(23, new Integer(0)); //orig 19
		this.dataWatcher.addObject(24, new Integer(0)); //orig 20
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("Invul", this.getInvulTime());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.setInvulTime(tagCompund.getInteger("Invul"));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.wither.idle";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.wither.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.wither.death";
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		this.motionY *= 0.6000000238418579D;
		double d1;
		double d3;
		double d5;

		if (!this.worldObj.isRemote && this.getWatchedTargetId(0) > 0)
		{
			Entity entity = this.worldObj.getEntityByID(this.getWatchedTargetId(0));

			if (entity != null)
			{	
				if (this.posY < entity.posY || !this.isArmored() && this.posY < entity.posY + 5.0D)
				{
					if (this.motionY < 0.0D)
					{
						this.motionY = 0.0D;
					}

					this.motionY += (0.5D - this.motionY) * 0.6000000238418579D;
				}

				double d0 = entity.posX - this.posX;
				d1 = entity.posZ - this.posZ;
				d3 = d0 * d0 + d1 * d1;

				if (d3 > 9.0D)
				{
					d5 = MathHelper.sqrt_double(d3);
					this.motionX += (d0 / d5 * 0.5D - this.motionX) * 0.6000000238418579D;
					this.motionZ += (d1 / d5 * 0.5D - this.motionZ) * 0.6000000238418579D;
				}
			}
		}

		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 0.05000000074505806D)
		{
			this.rotationYaw = (float)Math.atan2(this.motionZ, this.motionX) * (180F / (float)Math.PI) - 90.0F;
		}

		super.onLivingUpdate();
		int i;

		for (i = 0; i < 2; ++i)
		{
			this.field_82218_g[i] = this.field_82221_e[i];
			this.field_82217_f[i] = this.field_82220_d[i];
		}

		int j;

		for (i = 0; i < 2; ++i)
		{
			j = this.getWatchedTargetId(i + 1);
			Entity entity1 = null;

			if (j > 0)
			{
				entity1 = this.worldObj.getEntityByID(j);
			}

			if (entity1 != null)
			{
				d1 = this.func_82214_u(i + 1);
				d3 = this.func_82208_v(i + 1);
				d5 = this.func_82213_w(i + 1);
				double d6 = entity1.posX - d1;
				double d7 = entity1.posY + entity1.getEyeHeight() - d3;
				double d8 = entity1.posZ - d5;
				double d9 = MathHelper.sqrt_double(d6 * d6 + d8 * d8);
				float f = (float)(Math.atan2(d8, d6) * 180.0D / Math.PI) - 90.0F;
				float f1 = (float)(-(Math.atan2(d7, d9) * 180.0D / Math.PI));
				this.field_82220_d[i] = this.func_82204_b(this.field_82220_d[i], f1, 40.0F);
				this.field_82221_e[i] = this.func_82204_b(this.field_82221_e[i], f, 10.0F);
			}
			else
			{
				this.field_82221_e[i] = this.func_82204_b(this.field_82221_e[i], this.renderYawOffset, 10.0F);
			}
		}

		boolean flag = this.isArmored();

		double d10 = this.func_82214_u(0);
		double d2 = this.func_82208_v(0)-1.7D;
		double d4 = this.func_82213_w(0);
		this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, d10 + this.rand.nextGaussian() * 0.3D, d2 + this.rand.nextGaussian() * 0.3D, d4 + this.rand.nextGaussian() * 0.3D, 0.0D, 0.0D, 0.0D, new int[0]);

		if (flag && this.worldObj.rand.nextInt(4) == 0)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, d10 + this.rand.nextGaussian() * 0.3D, d2 + this.rand.nextGaussian() * 0.3D, d4 + this.rand.nextGaussian() * 0.3D, 0.7D, 0.7D, 0.5D, new int[0]);
		}

		if (this.getInvulTime() > 0)
			this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + this.rand.nextGaussian() * 1.0D, this.posY + this.rand.nextFloat() * 3.3F, this.posZ + this.rand.nextGaussian() * 1.0D, 0.7D, 0.7D, 0.9D, new int[0]);
	}

	@Override
	protected void updateAITasks()
	{
		int i;

		if (this.getInvulTime() > 0)
		{
			i = this.getInvulTime() - 1;

			if (i <= 0)
			{
				this.worldObj.newExplosion(this, this.posX, this.posY + this.getEyeHeight(), this.posZ, 7.0F, false, this.worldObj.getGameRules().getBoolean("mobGriefing"));
				this.worldObj.playBroadcastSound(1013, new BlockPos(this), 0);
			}

			this.setInvulTime(i);

			if (this.ticksExisted % 10 == 0)
			{
				this.heal(10.0F);
			}
		}
		else
		{
			super.updateAITasks();
			int i1;

			for (i = 1; i < 3; ++i)
			{
				if (this.ticksExisted >= this.field_82223_h[i - 1])
				{
					this.field_82223_h[i - 1] = this.ticksExisted + 10 + this.rand.nextInt(10);

					if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL || this.worldObj.getDifficulty() == EnumDifficulty.HARD)
					{
						int k2 = i - 1;
						int l2 = this.field_82224_i[i - 1];
						this.field_82224_i[k2] = this.field_82224_i[i - 1] + 1;

						if (l2 > 15)
						{
							float f = 10.0F;
							float f1 = 5.0F;
							double d0 = MathHelper.getRandomDoubleInRange(this.rand, this.posX - f, this.posX + f);
							double d1 = MathHelper.getRandomDoubleInRange(this.rand, this.posY - f1, this.posY + f1);
							double d2 = MathHelper.getRandomDoubleInRange(this.rand, this.posZ - f, this.posZ + f);
							this.launchWitherSkullToCoords(i + 1, d0, d1, d2, true, null);
							this.field_82224_i[i - 1] = 0;
						}
					}

					i1 = this.getWatchedTargetId(i);

					if (i1 > 0)
					{
						Entity entity = this.worldObj.getEntityByID(i1);

						if (entity != null && entity.isEntityAlive() && this.getDistanceSqToEntity(entity) <= 900.0D && this.canEntityBeSeen(entity))
						{
							this.launchWitherSkullToEntity(i + 1, (EntityLivingBase)entity);
							this.field_82223_h[i - 1] = this.ticksExisted + 40 + this.rand.nextInt(20);
							this.field_82224_i[i - 1] = 0;
						}
						else
						{
							this.func_82211_c(i, 0);
						}
					}
					else
					{
                        List<EntityLivingBase> list = this.worldObj.<EntityLivingBase>getEntitiesWithinAABB(EntityLivingBase.class, this.getEntityBoundingBox().expand(20.0D, 8.0D, 20.0D), Predicates.<EntityLivingBase>and(attackEntitySelector, EntitySelectors.NOT_SPECTATING));

						for (int k1 = 0; k1 < 10 && !list.isEmpty(); ++k1)
						{
							EntityLivingBase entitylivingbase = (EntityLivingBase)list.get(this.rand.nextInt(list.size()));

							if (entitylivingbase != this && entitylivingbase.isEntityAlive() && this.canEntityBeSeen(entitylivingbase))
							{
								if (entitylivingbase instanceof EntityPlayer)
								{
									if (!((EntityPlayer)entitylivingbase).capabilities.disableDamage)
									{
										this.func_82211_c(i, entitylivingbase.getEntityId());
									}
								}
								else
								{
									this.func_82211_c(i, entitylivingbase.getEntityId());
								}

								break;
							}

							list.remove(entitylivingbase);
						}
					}
				}
			}

			if (this.getAttackTarget() != null)
			{
				this.func_82211_c(0, this.getAttackTarget().getEntityId());
			}
			else
			{
				this.func_82211_c(0, 0);
			}

			if (this.blockBreakCounter > 0)
			{
				--this.blockBreakCounter;

				if (this.blockBreakCounter == 0 && this.worldObj.getGameRules().getBoolean("mobGriefing"))
				{
					i = MathHelper.floor_double(this.posY);
					i1 = MathHelper.floor_double(this.posX);
					int j1 = MathHelper.floor_double(this.posZ);
					boolean flag = false;

					for (int l1 = -1; l1 <= 1; ++l1)
					{
						for (int i2 = -1; i2 <= 1; ++i2)
						{
							for (int j = 0; j <= 3; ++j)
							{
								int j2 = i1 + l1;
								int k = i + j;
								int l = j1 + i2;
								Block block = this.worldObj.getBlockState(new BlockPos(j2, k, l)).getBlock();

								if (!block.isAir(worldObj, new BlockPos(j2, k, l)) && block.canEntityDestroy(worldObj, new BlockPos(j2, k, l), this))
								{
									flag = this.worldObj.destroyBlock(new BlockPos(j2, k, l), true) || flag;
								}
							}
						}
					}

					if (flag)
					{
						this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1012, new BlockPos(this), 0);
					}
				}
			}

			if (this.ticksExisted % 20 == 0)
			{
				this.heal(1.0F);
			}
		}
	}

	public void func_82206_m()
	{
		this.setInvulTime(220);
		this.setHealth(this.getMaxHealth() / 3.0F);
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	@Override
	public void setInWeb() {}

	/**
	 * Returns the current armor value as determined by a call to InventoryPlayer.getTotalArmorValue
	 */
	@Override
	public int getTotalArmorValue()
	{
		return 4;
	}

	private double func_82214_u(int p_82214_1_)
	{
		if (p_82214_1_ <= 0)
		{
			return this.posX;
		}
		else
		{
			float f = (this.renderYawOffset + 180 * (p_82214_1_ - 1)) / 180.0F * (float)Math.PI;
			float f1 = MathHelper.cos(f);
			return this.posX + f1 * 1.3D;
		}
	}

	private double func_82208_v(int p_82208_1_)
	{
		return p_82208_1_ <= 0 ? this.posY + 3.0D : this.posY + 2.2D;
	}

	private double func_82213_w(int p_82213_1_)
	{
		if (p_82213_1_ <= 0)
		{
			return this.posZ;
		}
		else
		{
			float f = (this.renderYawOffset + 180 * (p_82213_1_ - 1)) / 180.0F * (float)Math.PI;
			float f1 = MathHelper.sin(f);
			return this.posZ + f1 * 1.3D;
		}
	}

	private float func_82204_b(float p_82204_1_, float p_82204_2_, float p_82204_3_)
	{
		float f3 = MathHelper.wrapAngleTo180_float(p_82204_2_ - p_82204_1_);

		if (f3 > p_82204_3_)
		{
			f3 = p_82204_3_;
		}

		if (f3 < -p_82204_3_)
		{
			f3 = -p_82204_3_;
		}

		return p_82204_1_ + f3;
	}

	private void launchWitherSkullToEntity(int par1, EntityLivingBase entitylivingbase)
	{
		this.launchWitherSkullToCoords(par1, entitylivingbase.posX, entitylivingbase.posY + entitylivingbase.getEyeHeight() * 0.5D, entitylivingbase.posZ, par1 == 0 && this.rand.nextFloat() < 0.001F, entitylivingbase);
	}

	/**
	 * Launches a Wither skull toward (par2, par4, par6)
	 */
	private void launchWitherSkullToCoords(int par1, double par2, double par3, double par4, boolean par5, EntityLivingBase entitylivingbase)
	{
		this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1014, new BlockPos(this), 0);
		double d3 = this.func_82214_u(par1);
		double d4 = this.func_82208_v(par1);
		double d5 = this.func_82213_w(par1);
		double d6 = par2 - d3;
		double d7 = par3 - d4;
		double d8 = par4 - d5;
		//TODO entityWitherWitherSkull
		if (Config.useSpecialAbilities)
		{
			EntityWitherWitherSkull entitywitherwitherskull = new EntityWitherWitherSkull(this.worldObj, this, d6, d7, d8, entitylivingbase);
			if (par5)
			{
				entitywitherwitherskull.setInvulnerable(true);
			}

			entitywitherwitherskull.posY = d4;
			entitywitherwitherskull.posX = d3;
			entitywitherwitherskull.posZ = d5;
			this.worldObj.spawnEntityInWorld(entitywitherwitherskull);
		}
		else
		{
			EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.worldObj, this, d6, d7, d8);
			if (par5)
			{
				entitywitherskull.setInvulnerable(true);
			}

			entitywitherskull.posY = d4;
			entitywitherskull.posX = d3;
			entitywitherskull.posZ = d5;
			this.worldObj.spawnEntityInWorld(entitywitherskull);
		}
		//end
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
	{
		this.launchWitherSkullToEntity(0, p_82196_1_);
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
		else if (source != DamageSource.drown && !(source.getEntity() instanceof EntityBabyWither))
		{
			if (this.getInvulTime() > 0 && source != DamageSource.outOfWorld)
			{
				return false;
			}
			else
			{
				Entity entity;

				if (this.isArmored())
				{
					entity = source.getSourceOfDamage();

					//TODO reflect arrows when armored
					if (Config.useSpecialAbilities)
					{
						if (entity instanceof EntityArrow && ((EntityArrow) entity).shootingEntity != null)
						{
							EntityArrow arrow = (EntityArrow) entity;
							EntityArrow newArrow = new EntityArrow(worldObj, this, (EntityLivingBase) arrow.shootingEntity, 1.6F, 0.0F);
							newArrow.copyLocationAndAnglesFrom(arrow);
							newArrow.setDamage(arrow.getDamage());
							double d0 = arrow.shootingEntity.posX - newArrow.posX;
							double d1 = arrow.shootingEntity.getEntityBoundingBox().minY + newArrow.shootingEntity.height / 3.0F - newArrow.posY;
							double d2 = arrow.shootingEntity.posZ - newArrow.posZ;
							double d3 = MathHelper.sqrt_double(d0 * d0 + d2 * d2);
							float f4 = (float)(d3 * 0.20000000298023224D);
							newArrow.setThrowableHeading(d0, d1 + f4, d2, 1.6F, 14 - this.worldObj.getDifficulty().getDifficultyId() * 4);
							if (!this.worldObj.isRemote)
							{
								this.worldObj.spawnEntityInWorld(newArrow);
								arrow.setDead();
							}
							return false;
						}
					}
					//end
				}

				entity = source.getEntity();

				if (entity != null && !(entity instanceof EntityPlayer) && entity instanceof EntityLivingBase && ((EntityLivingBase)entity).getCreatureAttribute() == this.getCreatureAttribute())
				{
					return false;
				}
				else
				{
					if (this.blockBreakCounter <= 0)
					{
						this.blockBreakCounter = 20;
					}

					for (int i = 0; i < this.field_82224_i.length; ++i)
					{
						this.field_82224_i[i] += 3;
					}

					return super.attackEntityFrom(source, amount);
				}
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		//TODO drops changed
		if (this.worldObj.rand.nextInt(3) == 0)
		{
			EntityItem entityitem = this.dropItem(Items.nether_star, 1);

			if (entityitem != null)
			{
				entityitem.setNoDespawn();
			}
		}
		else 
		{
			Iterator iterator = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();

			while (iterator.hasNext())
			{
				EntityPlayer entityplayer = (EntityPlayer)iterator.next();
				entityplayer.triggerAchievement(Achievements.achievementBetterLuckNextTime);
			}
			this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
		}
		//end

		if (!this.worldObj.isRemote)
		{
			Iterator iterator = this.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();

			while (iterator.hasNext())
			{
				EntityPlayer entityplayer = (EntityPlayer)iterator.next();
				entityplayer.triggerAchievement(AchievementList.killWither);
			}
		}
	}

	/**
	 * Makes the entity despawn if requirements are reached
	 */
	@Override
	protected void despawnEntity()
	{
		this.entityAge = 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 15728880;
	}

	@Override
	public void fall(float distance, float damageMultiplier) {}

	/**
	 * adds a PotionEffect to the entity
	 */
	@Override
	public void addPotionEffect(PotionEffect p_70690_1_) {}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(300.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.6000000238418579D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(40.0D);
	}

	@SideOnly(Side.CLIENT)
	public float func_82207_a(int p_82207_1_)
	{
		return this.field_82221_e[p_82207_1_];
	}

	@SideOnly(Side.CLIENT)
	public float func_82210_r(int p_82210_1_)
	{
		return this.field_82220_d[p_82210_1_];
	}

	public int getInvulTime()
	{
		return this.dataWatcher.getWatchableObjectInt(24); //orig 20
	}

	public void setInvulTime(int p_82215_1_)
	{
		this.dataWatcher.updateObject(24, Integer.valueOf(p_82215_1_)); //orig 20
	}

	/**
	 * Returns the target entity ID if present, or -1 if not @param par1 The target offset, should be from 0-2
	 */
	public int getWatchedTargetId(int p_82203_1_)
	{
		return this.dataWatcher.getWatchableObjectInt(21 + p_82203_1_);
	}

	public void func_82211_c(int p_82211_1_, int p_82211_2_)
	{
		this.dataWatcher.updateObject(21 + p_82211_1_, Integer.valueOf(p_82211_2_));
	}

	/**
	 * Returns whether the wither is armored with its boss armor or not by checking whether its health is below half of
	 * its maximum.
	 */
	public boolean isArmored()
	{
		return this.getHealth() <= this.getMaxHealth() / 2.0F;
	}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.UNDEAD;
	}

	/**
	 * Called when a player mounts an entity. e.g. mounts a pig, mounts a boat.
	 */
	@Override
	public void mountEntity(Entity entityIn)
	{
		this.ridingEntity = null;
	}
}