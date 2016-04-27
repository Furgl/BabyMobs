package furgl.babyMobs.common.entity.monster;

import java.util.Iterator;
import java.util.List;

import com.google.common.base.Predicate;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFishFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.S2BPacketChangeGameState;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateSwimmer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.WeightedRandomFishable;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabyGuardian extends EntityMob
{
	//Custom variables
	private boolean longerSpikes = false;
	private int spikesCounter = 0;

	private float field_175482_b;
	private float field_175484_c;
	private float field_175483_bk;
	private float field_175485_bl;
	private float field_175486_bm;
	private EntityLivingBase field_175478_bn;
	private int field_175479_bo;
	private boolean field_175480_bp;
	private EntityAIWander wander;

	public EntityBabyGuardian(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 0.5F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));

		this.tasks.addTask(4, new EntityBabyGuardian.AIGuardianAttack());
		EntityAIMoveTowardsRestriction entityaimovetowardsrestriction;
		this.tasks.addTask(5, entityaimovetowardsrestriction = new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(7, this.wander = new EntityAIWander(this, 1.0D, 80));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAIWatchClosest(this, EntityBabyGuardian.class, 12.0F, 0.01F));
		this.tasks.addTask(9, new EntityAILookIdle(this));
		this.wander.setMutexBits(3);
		entityaimovetowardsrestriction.setMutexBits(3);
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLivingBase.class, 10, true, false, new EntityBabyGuardian.GuardianTargetSelector()));
		this.moveHelper = new EntityBabyGuardian.GuardianMoveHelper();
		this.field_175484_c = this.field_175482_b = this.rand.nextFloat();
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
		return new ItemStack(ModItems.baby_guardian_egg);
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
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.5D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(30.0D);
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.func_175467_a(tagCompund.getBoolean("Elder"));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setBoolean("Elder", this.isElder());
	}

	@Override
	protected PathNavigate func_175447_b(World worldIn)
	{
		return new PathNavigateSwimmer(this, worldIn);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, Integer.valueOf(0));
		this.dataWatcher.addObject(17, Integer.valueOf(0));
	}

	private boolean func_175468_a(int p_175468_1_)
	{
		return (this.dataWatcher.getWatchableObjectInt(16) & p_175468_1_) != 0;
	}

	private void func_175473_a(int p_175473_1_, boolean p_175473_2_)
	{
		int j = this.dataWatcher.getWatchableObjectInt(16);

		if (p_175473_2_)
		{
			this.dataWatcher.updateObject(16, Integer.valueOf(j | p_175473_1_));
		}
		else
		{
			this.dataWatcher.updateObject(16, Integer.valueOf(j & ~p_175473_1_));
		}
	}

	public boolean func_175472_n()
	{
		return this.func_175468_a(2);
	}

	private void func_175476_l(boolean p_175476_1_)
	{
		this.func_175473_a(2, p_175476_1_);
	}

	public int func_175464_ck()
	{
		return this.isElder() ? 60 : 80;
	}

	public boolean isElder()
	{
		return this.func_175468_a(4);
	}

	public void func_175467_a(boolean p_175467_1_)
	{
		this.func_175473_a(4, p_175467_1_);

		if (p_175467_1_)
		{
			this.setSize(1.9975F, 1.9975F);
			this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
			this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(8.0D);
			this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(80.0D);
			this.enablePersistence();
			this.wander.func_179479_b(400);
		}
	}

	@SideOnly(Side.CLIENT)
	public void func_175465_cm()
	{
		this.func_175467_a(true);
		this.field_175486_bm = this.field_175485_bl = 1.0F;
	}

	private void func_175463_b(int p_175463_1_)
	{
		this.dataWatcher.updateObject(17, Integer.valueOf(p_175463_1_));
	}

	public boolean func_175474_cn()
	{
		return this.dataWatcher.getWatchableObjectInt(17) != 0;
	}

	public EntityLivingBase getTargetedEntity()
	{
		if (!this.func_175474_cn())
		{
			return null;
		}
		else if (this.worldObj.isRemote)
		{
			if (this.field_175478_bn != null)
			{
				return this.field_175478_bn;
			}
			else
			{
				Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(17));

				if (entity instanceof EntityLivingBase)
				{
					this.field_175478_bn = (EntityLivingBase)entity;
					return this.field_175478_bn;
				}
				else
				{
					return null;
				}
			}
		}
		else
		{
			return this.getAttackTarget();
		}
	}

	@Override
	public void func_145781_i(int p_145781_1_)
	{
		super.func_145781_i(p_145781_1_);

		if (p_145781_1_ == 16)
		{
			if (this.isElder() && this.width < 1.0F)
			{
				this.setSize(1.9975F, 1.9975F);
			}
		}
		else if (p_145781_1_ == 17)
		{
			this.field_175479_bo = 0;
			this.field_175478_bn = null;
		}
	}

	/**
	 * Get number of ticks, at least during which the living entity will be silent.
	 */
	@Override
	public int getTalkInterval()
	{
		return 160;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return !this.isInWater() ? "mob.guardian.land.idle" : (this.isElder() ? "mob.guardian.elder.idle" : "mob.guardian.idle");
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		if (this.longerSpikes)//TODO spike sound
			return "mob.blaze.hit";
		else
			return !this.isInWater() ? "mob.guardian.land.hit" : (this.isElder() ? "mob.guardian.elder.hit" : "mob.guardian.hit");
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return !this.isInWater() ? "mob.guardian.land.death" : (this.isElder() ? "mob.guardian.elder.death" : "mob.guardian.death");
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

	@Override
	public float getEyeHeight()
	{
		return this.height * 0.5F;
	}

	@Override
	public float func_180484_a(BlockPos p_180484_1_)
	{
		return this.worldObj.getBlockState(p_180484_1_).getBlock().getMaterial() == Material.water ? 10.0F + this.worldObj.getLightBrightness(p_180484_1_) - 0.5F : super.func_180484_a(p_180484_1_);
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
				this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.blaze.hit", 1.0F, this.rand.nextFloat() * 0.4F + 8F);
		}
	}
	//end

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		//TODO longerSpikes counter
		if (this.longerSpikes)
		{
			this.spikesCounter--;
			if (this.spikesCounter == 0)
			{
				this.longerSpikes = false;
				this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.blaze.hit", 1.0F, this.rand.nextFloat() * 0.4F + 8F);
			}
			if (this.attackingPlayer instanceof EntityPlayer && !(this.attackingPlayer instanceof FakePlayer))
			{
				if (this.inWater)
					this.getNavigator().tryMoveToEntityLiving(this.attackingPlayer, 2D);
				else
				{
					this.faceEntity(this.attackingPlayer, 100, 100);
					Vec3 vec = this.attackingPlayer.getPositionVector().subtract(this.getPositionVector()).normalize();
					this.motionX = vec.xCoord/4;
					this.motionZ = vec.zCoord/4;
				}
			}
		}
		//end

		if (this.worldObj.isRemote)
		{
			this.field_175484_c = this.field_175482_b;

			if (!this.isInWater())
			{
				this.field_175483_bk = 2.0F;

				if (this.motionY > 0.0D && this.field_175480_bp && !this.isSilent())
				{
					this.worldObj.playSound(this.posX, this.posY, this.posZ, "mob.guardian.flop", 1.0F, 1.0F, false);
				}

				this.field_175480_bp = this.motionY < 0.0D && this.worldObj.isBlockNormalCube((new BlockPos(this)).down(), false);
			}
			else if (this.func_175472_n())
			{
				if (this.field_175483_bk < 0.5F)
				{
					this.field_175483_bk = 4.0F;
				}
				else
				{
					this.field_175483_bk += (0.5F - this.field_175483_bk) * 0.1F;
				}
			}
			else
			{
				this.field_175483_bk += (0.125F - this.field_175483_bk) * 0.2F;
			}

			this.field_175482_b += this.field_175483_bk;
			this.field_175486_bm = this.field_175485_bl;

			if (!this.isInWater())
			{
				this.field_175485_bl = this.rand.nextFloat();
			}
			else if (this.func_175472_n())
			{
				this.field_175485_bl += (0.0F - this.field_175485_bl) * 0.25F;
			}
			else
			{
				this.field_175485_bl += (1.0F - this.field_175485_bl) * 0.06F;
			}

			if (this.func_175472_n() && this.isInWater())
			{
				Vec3 vec3 = this.getLook(0.0F);

				for (int i = 0; i < 2; ++i)
				{
					this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + (this.rand.nextDouble() - 0.5D) * this.width - vec3.xCoord * 1.5D, this.posY + this.rand.nextDouble() * this.height - vec3.yCoord * 1.5D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width - vec3.zCoord * 1.5D, 0.0D, 0.0D, 0.0D, new int[0]);
				}
			}

			if (this.func_175474_cn())
			{
				if (this.field_175479_bo < this.func_175464_ck())
				{
					++this.field_175479_bo;
				}

				EntityLivingBase entitylivingbase = this.getTargetedEntity();

				if (entitylivingbase != null)
				{
					this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);
					this.getLookHelper().onUpdateLook();
					double d5 = this.func_175477_p(0.0F);
					double d0 = entitylivingbase.posX - this.posX;
					double d1 = entitylivingbase.posY + entitylivingbase.height * 0.5F - (this.posY + this.getEyeHeight());
					double d2 = entitylivingbase.posZ - this.posZ;
					double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
					d0 /= d3;
					d1 /= d3;
					d2 /= d3;
					double d4 = this.rand.nextDouble();

					while (d4 < d3)
					{
						d4 += 1.8D - d5 + this.rand.nextDouble() * (1.7D - d5);
						this.worldObj.spawnParticle(EnumParticleTypes.WATER_BUBBLE, this.posX + d0 * d4, this.posY + d1 * d4 + this.getEyeHeight(), this.posZ + d2 * d4, 0.0D, 0.0D, 0.0D, new int[0]);
					}
				}
			}
		}

		if (this.inWater)
		{
			this.setAir(300);
		}
		else if (this.onGround)
		{
			this.motionY += 0.5D;
			if (!this.longerSpikes)
			{
				this.motionX += (this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
				this.motionZ += (this.rand.nextFloat() * 2.0F - 1.0F) * 0.4F;
				this.rotationYaw = this.rand.nextFloat() * 360.0F;
			}
			this.onGround = false;
			this.isAirBorne = true;
		}

		if (this.func_175474_cn())
		{
			this.rotationYaw = this.rotationYawHead;
		}

		super.onLivingUpdate();
	}

	@SideOnly(Side.CLIENT)
	public float func_175471_a(float p_175471_1_)
	{
		return this.field_175484_c + (this.field_175482_b - this.field_175484_c) * p_175471_1_;
	}

	@SideOnly(Side.CLIENT)
	public float func_175469_o(float p_175469_1_)
	{
		return this.field_175486_bm + (this.field_175485_bl - this.field_175486_bm) * p_175469_1_;
	}

	public float func_175477_p(float p_175477_1_)
	{
		return (this.field_175479_bo + p_175477_1_) / this.func_175464_ck();
	}

	@Override
	protected void updateAITasks()
	{
		super.updateAITasks();

		if (this.isElder())
		{
			if ((this.ticksExisted + this.getEntityId()) % 1200 == 0)
			{
				Potion potion = Potion.digSlowdown;
				List list = this.worldObj.getPlayers(EntityPlayerMP.class, new Predicate()
				{
					public boolean func_179913_a(EntityPlayerMP p_179913_1_)
					{
						return EntityBabyGuardian.this.getDistanceSqToEntity(p_179913_1_) < 2500.0D && p_179913_1_.theItemInWorldManager.func_180239_c();
					}
					@Override
					public boolean apply(Object p_apply_1_)
					{
						return this.func_179913_a((EntityPlayerMP)p_apply_1_);
					}
				});
				Iterator iterator = list.iterator();

				while (iterator.hasNext())
				{
					EntityPlayerMP entityplayermp = (EntityPlayerMP)iterator.next();

					if (!entityplayermp.isPotionActive(potion) || entityplayermp.getActivePotionEffect(potion).getAmplifier() < 2 || entityplayermp.getActivePotionEffect(potion).getDuration() < 1200)
					{
						entityplayermp.playerNetServerHandler.sendPacket(new S2BPacketChangeGameState(10, 0.0F));
						entityplayermp.addPotionEffect(new PotionEffect(potion.id, 6000, 2));
					}
				}
			}

			if (!this.hasHome())
			{
				this.func_175449_a(new BlockPos(this), 16);
			}
		}
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(3) + this.rand.nextInt(p_70628_2_ + 1);

		if (j > 0)
		{
			this.entityDropItem(new ItemStack(Items.prismarine_shard, j, 0), 1.0F);
		}

		if (this.rand.nextInt(3 + p_70628_2_) > 1)
		{
			this.entityDropItem(new ItemStack(Items.fish, 1, ItemFishFood.FishType.COD.getMetadata()), 1.0F);
		}
		else if (this.rand.nextInt(3 + p_70628_2_) > 1)
		{
			this.entityDropItem(new ItemStack(Items.prismarine_crystals, 1, 0), 1.0F);
		}

		if (p_70628_1_ && this.isElder())
		{
			this.entityDropItem(new ItemStack(Blocks.sponge, 1, 1), 1.0F);
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	@Override
	protected void addRandomArmor()
	{
		ItemStack itemstack = ((WeightedRandomFishable)WeightedRandom.getRandomItem(this.rand, EntityFishHook.func_174855_j())).getItemStack(this.rand);
		this.entityDropItem(itemstack, 1.0F);
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	@Override
	protected boolean isValidLightLevel()
	{
		return true;
	}

	/**
	 * Whether or not the current entity is in lava
	 */
	@Override
	public boolean handleLavaMovement()
	{
		return this.worldObj.checkNoEntityCollision(this.getEntityBoundingBox(), this) && this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty();
	}

	/**
	 * Checks if the entity's current position is a valid location to spawn this entity.
	 */
	@Override
	public boolean getCanSpawnHere()
	{
		return (this.rand.nextInt(20) == 0 || !this.worldObj.canBlockSeeSky(new BlockPos(this))) && super.getCanSpawnHere();
	}

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		//TODO render spikes longer on attack and play sound
		if (Config.useSpecialAbilities)
		{
			if (source.getSourceOfDamage() instanceof EntityLivingBase)
			{
				if (!this.longerSpikes)
					this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.blaze.hit", 1.0F, this.rand.nextFloat() * 0.4F + 8F);
				this.longerSpikes = true;
				this.spikesCounter = 50;
			}
		}
		//end 

		if (!this.func_175472_n() && !source.isMagicDamage() && source.getSourceOfDamage() instanceof EntityLivingBase)
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase)source.getSourceOfDamage();

			if (!source.isExplosion())
			{
				entitylivingbase.attackEntityFrom(DamageSource.causeThornsDamage(this), 2.0F);
				//entitylivingbase.playSound("damage.thorns", 0.5F, 1.0F);
			}
		}

		this.wander.func_179480_f();
		return super.attackEntityFrom(source, amount);
	}

	/**
	 * The speed it takes to move the entityliving's rotationPitch through the faceEntity method. This is only currently
	 * use in wolves.
	 */
	@Override
	public int getVerticalFaceSpeed()
	{
		return 180;
	}

	/**
	 * Moves the entity based on the specified heading.  Args: strafe, forward
	 */
	@Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
	{
		if (this.isServerWorld())
		{
			if (this.isInWater())
			{
				this.moveFlying(p_70612_1_, p_70612_2_, 0.1F);
				this.moveEntity(this.motionX, this.motionY, this.motionZ);
				this.motionX *= 0.8999999761581421D;
				this.motionY *= 0.8999999761581421D;
				this.motionZ *= 0.8999999761581421D;

				if (!this.func_175472_n() && this.getAttackTarget() == null)
				{
					this.motionY -= 0.005D;
				}
			}
			else
			{
				super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
			}
		}
		else
		{
			super.moveEntityWithHeading(p_70612_1_, p_70612_2_);
		}
	}

	class AIGuardianAttack extends EntityAIBase
	{
		private EntityBabyGuardian guardian = EntityBabyGuardian.this;
		private int counter;
		public AIGuardianAttack()
		{
			this.setMutexBits(3);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			if (this.guardian.longerSpikes) //added
				return false;
			EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
			return entitylivingbase != null && entitylivingbase.isEntityAlive();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean continueExecuting()
		{
			return super.continueExecuting() && (this.guardian.isElder() || this.guardian.getDistanceSqToEntity(this.guardian.getAttackTarget()) > 9.0D);
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void startExecuting()
		{
			this.counter = -10;
			this.guardian.getNavigator().clearPathEntity();
			this.guardian.getLookHelper().setLookPositionWithEntity(this.guardian.getAttackTarget(), 90.0F, 90.0F);
			this.guardian.isAirBorne = true;
		}

		/**
		 * Resets the task
		 */
		@Override
		public void resetTask()
		{
			this.guardian.func_175463_b(0);
			this.guardian.setAttackTarget((EntityLivingBase)null);
			this.guardian.wander.func_179480_f();
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			EntityLivingBase entitylivingbase = this.guardian.getAttackTarget();
			this.guardian.getNavigator().clearPathEntity();
			this.guardian.getLookHelper().setLookPositionWithEntity(entitylivingbase, 90.0F, 90.0F);

			if (!this.guardian.canEntityBeSeen(entitylivingbase))
			{
				this.guardian.setAttackTarget((EntityLivingBase)null);
			}
			else
			{
				++this.counter;

				if (this.counter == 0)
				{
					this.guardian.func_175463_b(this.guardian.getAttackTarget().getEntityId());
					this.guardian.worldObj.setEntityState(this.guardian, (byte)22);//orig 21
				}
				else if (this.counter >= this.guardian.func_175464_ck())
				{
					float f = 1.0F;

					if (this.guardian.worldObj.getDifficulty() == EnumDifficulty.HARD)
					{
						f += 2.0F;
					}

					if (this.guardian.isElder())
					{
						f += 2.0F;
					}

					entitylivingbase.attackEntityFrom(DamageSource.causeIndirectMagicDamage(this.guardian, this.guardian), f);
					entitylivingbase.attackEntityFrom(DamageSource.causeMobDamage(this.guardian), (float)this.guardian.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue());
					this.guardian.setAttackTarget((EntityLivingBase)null);
				}
				super.updateTask();
			}
		}
	}

	class GuardianMoveHelper extends EntityMoveHelper
	{
		private EntityBabyGuardian field_179930_g = EntityBabyGuardian.this;
		public GuardianMoveHelper()
		{
			super(EntityBabyGuardian.this);
		}

		@Override
		public void onUpdateMoveHelper()
		{
			if (this.update && !this.field_179930_g.getNavigator().noPath())
			{
				double d0 = this.posX - this.field_179930_g.posX;
				double d1 = this.posY - this.field_179930_g.posY;
				double d2 = this.posZ - this.field_179930_g.posZ;
				double d3 = d0 * d0 + d1 * d1 + d2 * d2;
				d3 = MathHelper.sqrt_double(d3);
				d1 /= d3;
				float f = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
				this.field_179930_g.rotationYaw = this.limitAngle(this.field_179930_g.rotationYaw, f, 30.0F);
				this.field_179930_g.renderYawOffset = this.field_179930_g.rotationYaw;
				float f1 = (float)(this.speed * this.field_179930_g.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
				this.field_179930_g.setAIMoveSpeed(this.field_179930_g.getAIMoveSpeed() + (f1 - this.field_179930_g.getAIMoveSpeed()) * 0.125F);
				double d4 = Math.sin((this.field_179930_g.ticksExisted + this.field_179930_g.getEntityId()) * 0.5D) * 0.05D;
				double d5 = Math.cos(this.field_179930_g.rotationYaw * (float)Math.PI / 180.0F);
				double d6 = Math.sin(this.field_179930_g.rotationYaw * (float)Math.PI / 180.0F);
				this.field_179930_g.motionX += d4 * d5;
				this.field_179930_g.motionZ += d4 * d6;
				d4 = Math.sin((this.field_179930_g.ticksExisted + this.field_179930_g.getEntityId()) * 0.75D) * 0.05D;
				this.field_179930_g.motionY += d4 * (d6 + d5) * 0.25D;
				this.field_179930_g.motionY += this.field_179930_g.getAIMoveSpeed() * d1 * 0.1D;
				EntityLookHelper entitylookhelper = this.field_179930_g.getLookHelper();
				double d7 = this.field_179930_g.posX + d0 / d3 * 2.0D;
				double d8 = this.field_179930_g.getEyeHeight() + this.field_179930_g.posY + d1 / d3 * 1.0D;
				double d9 = this.field_179930_g.posZ + d2 / d3 * 2.0D;
				double d10 = entitylookhelper.func_180423_e();
				double d11 = entitylookhelper.func_180422_f();
				double d12 = entitylookhelper.func_180421_g();

				if (!entitylookhelper.func_180424_b())
				{
					d10 = d7;
					d11 = d8;
					d12 = d9;
				}

				this.field_179930_g.getLookHelper().setLookPosition(d10 + (d7 - d10) * 0.125D, d11 + (d8 - d11) * 0.125D, d12 + (d9 - d12) * 0.125D, 10.0F, 40.0F);
				this.field_179930_g.func_175476_l(true);
			}
			else
			{
				this.field_179930_g.setAIMoveSpeed(0.0F);
				this.field_179930_g.func_175476_l(false);
			}
		}
	}

	class GuardianTargetSelector implements Predicate
	{
		private EntityBabyGuardian field_179916_a = EntityBabyGuardian.this;
		public boolean func_179915_a(EntityLivingBase p_179915_1_)
		{
			return (p_179915_1_ instanceof EntityPlayer || p_179915_1_ instanceof EntitySquid|| p_179915_1_ instanceof EntityBabySquid) && p_179915_1_.getDistanceSqToEntity(this.field_179916_a) > 9.0D;
		}

		@Override
		public boolean apply(Object p_apply_1_)
		{
			return this.func_179915_a((EntityLivingBase)p_apply_1_);
		}
	}
}