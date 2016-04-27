package furgl.babyMobs.common.entity.monster;

import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.UUID;

import com.google.common.base.Predicate;
import com.google.common.collect.Sets;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.EntityDamageSourceIndirect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyEnderman extends EntityMob
{
	private static final UUID attackingSpeedBoostModifierUUID = UUID.fromString("020E0DFB-87AE-4653-9556-831010E291A0");
	private static final AttributeModifier attackingSpeedBoostModifier = (new AttributeModifier(attackingSpeedBoostModifierUUID, "Attacking speed boost", 0.15000000596046448D, 0)).setSaved(false);
	private static final Set<Block> carriableBlocks = Sets.newIdentityHashSet();
	private boolean isAggressive;

	private int laserDelay = 0;

	public EntityBabyEnderman(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.6F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(1, new EntityAIBabyFollowParent(this, 1.1D));

		this.stepHeight = 1.0F;
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(7, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(0, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.tasks.addTask(10, new EntityBabyEnderman.AIPlaceBlock());
		this.tasks.addTask(11, new EntityBabyEnderman.AITakeBlock());
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityBabyEnderman.AIFindPlayer());
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityEndermite.class, 10, true, false, new Predicate<Object>()
		{
			public boolean func_179948_a(EntityEndermite p_179948_1_)
			{
				return p_179948_1_.isSpawnedByPlayer();
			}
			@Override
			public boolean apply(Object p_apply_1_)
			{
				return this.func_179948_a((EntityEndermite)p_apply_1_);
			}
		}));
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
	public boolean isChild()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_enderman_egg);
	}

	//TODO used in beam
	public EntityPlayer getTargetedEntity()
	{
		if (this.dataWatcher.getWatchableObjectInt(19) == 0)
			return null;
		else if (this.worldObj.isRemote)
		{
			Entity entity = this.worldObj.getEntityByID(this.dataWatcher.getWatchableObjectInt(19));
			//if fake player or creative mode
			if (entity instanceof FakePlayer || (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode))
				return null;
			if (entity instanceof EntityPlayer)
				return (EntityPlayer)entity;
			else
				return null;
		}
		else
		{
			//if fake player or creative mode
			if (this.getAttackTarget() instanceof FakePlayer || (this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer) this.getAttackTarget()).capabilities.isCreativeMode))
				return null;
			return (this.getAttackTarget() instanceof EntityPlayer) ? (EntityPlayer) this.getAttackTarget() : null;
		}
	}
	//end

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(40.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(7.0D);
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(64.0D);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, new Short((short)0));
		this.dataWatcher.addObject(17, new Byte((byte)0));
		this.dataWatcher.addObject(18, new Byte((byte)0));

		this.dataWatcher.addObject(19, Integer.valueOf(0));//entityId (if 0, then no beam)
		this.dataWatcher.addObject(31, Integer.valueOf(0));//laserDelay
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		IBlockState iblockstate = this.func_175489_ck();
		tagCompound.setShort("carried", (short)Block.getIdFromBlock(iblockstate.getBlock()));
		tagCompound.setShort("carriedData", (short)iblockstate.getBlock().getMetaFromState(iblockstate));
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		IBlockState iblockstate;

		if (tagCompund.hasKey("carried", 8))
		{
			iblockstate = Block.getBlockFromName(tagCompund.getString("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 65535);
		}
		else
		{
			iblockstate = Block.getBlockById(tagCompund.getShort("carried")).getStateFromMeta(tagCompund.getShort("carriedData") & 65535);
		}

		this.func_175490_a(iblockstate);
	}

	/**
	 * Checks to see if this enderman should be attacking this player
	 */
	private boolean shouldAttackPlayer(EntityPlayer p_70821_1_)
	{
		ItemStack itemstack = p_70821_1_.inventory.armorInventory[3];

		if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.pumpkin))
		{
			return false;
		}
		else
		{
			Vec3 vec3 = p_70821_1_.getLook(1.0F).normalize();
			Vec3 vec31 = new Vec3(this.posX - p_70821_1_.posX, this.getEntityBoundingBox().minY + this.height / 2.0F - (p_70821_1_.posY + p_70821_1_.getEyeHeight()), this.posZ - p_70821_1_.posZ);
			double d0 = vec31.lengthVector();
			vec31 = vec31.normalize();
			double d1 = vec3.dotProduct(vec31);
			return d1 > 1.0D - 0.025D / d0 ? p_70821_1_.canEntityBeSeen(this) : false;
		}
	}

	@Override
	public float getEyeHeight()
	{
		return 1.3F;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		if (this.worldObj.isRemote)
		{
			for (int i = 0; i < 2; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height - 0.25D, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, (this.rand.nextDouble() - 0.5D) * 2.0D, -this.rand.nextDouble(), (this.rand.nextDouble() - 0.5D) * 2.0D, new int[0]);
			}
		}

		//TODO beam
		if (Config.useSpecialAbilities)
		{
			if (!this.worldObj.isRemote)
			{
				if (this.dataWatcher.getWatchableObjectInt(31) == 0 && laserDelay == 0)
				{
					if (this.getAttackTarget() instanceof EntityPlayer && this.dataWatcher.getWatchableObjectInt(19) != 0) 
					{
						this.getAttackTarget().attackEntityFrom(DamageSource.magic, 1F);
					}
					if (this.getAttackTarget() instanceof EntityPlayer && this.shouldAttackPlayer((EntityPlayer) this.getAttackTarget()) == true && this.canEntityBeSeen(this.getAttackTarget()) && this.getHealth() > 0)
					{
						this.dataWatcher.updateObject(19, Integer.valueOf(this.getAttackTarget().getEntityId()));
						laserDelay = 40;
						this.dataWatcher.updateObject(31, Integer.valueOf(laserDelay));
					}
					else
					{
						this.dataWatcher.updateObject(19, Integer.valueOf(0));					
					}		
				}
				else
				{
					if (laserDelay > 0 && laserDelay % 5 == 0 && this.getAttackTarget() != null) 
					{
						if (this.getAttackTarget() instanceof EntityPlayer && !(this.getAttackTarget() instanceof FakePlayer))
							((EntityPlayer)this.getAttackTarget()).triggerAchievement(Achievements.achievementLaserTag);
						this.getAttackTarget().attackEntityFrom(DamageSource.magic, 1F);
					}
					laserDelay--;
					this.dataWatcher.updateObject(31, Integer.valueOf(laserDelay));
				}
			}
		}
		//end
		this.isJumping = false;
		super.onLivingUpdate();
		//TODO stop moving,look, and make sound when shooting beam
		if (Config.useSpecialAbilities)
		{
			if (this.dataWatcher.getWatchableObjectInt(19) != 0)
			{
				if (this.getHealth() == 0 || (!this.worldObj.isRemote && this.getAttackTarget() instanceof EntityPlayer && !(this.getAttackTarget() instanceof FakePlayer) && !this.canEntityBeSeen(this.getAttackTarget())))
				{
					this.dataWatcher.updateObject(31, 0);
					laserDelay = 0;
				}
				this.motionX = 0;
				this.motionZ = 0;
				this.setAIMoveSpeed(0);
				this.moveEntity(0, 0, 0);
				if (this.getTargetedEntity() instanceof EntityPlayer && !(this.getTargetedEntity() instanceof FakePlayer))
				{
					this.getLookHelper().setLookPositionWithEntity(this.getTargetedEntity(), 90.0F, 90.0F);
					this.getLookHelper().onUpdateLook();
					if (this.rand.nextInt(41) >= this.dataWatcher.getWatchableObjectInt(31))
						this.getTargetedEntity().playSound("mob.spider.death", 1F, 0.0F);
				}
			}
		}
		//end
	}

	@Override
	protected void updateAITasks()
	{
		if (this.isWet())
		{
			this.attackEntityFrom(DamageSource.drown, 1.0F);
		}

		if (this.isScreaming() && !this.isAggressive && this.rand.nextInt(100) == 0)
		{
			this.setScreaming(false);
		}

		if (this.worldObj.isDaytime())
		{
			float f = this.getBrightness(1.0F);

			if (f > 0.5F && this.worldObj.canSeeSky(new BlockPos(this)) && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F)
			{
				this.setAttackTarget((EntityLivingBase)null);
				this.setScreaming(false);
				this.isAggressive = false;
				this.teleportRandomly();
			}
		}

		super.updateAITasks();
	}

	/**
	 * Teleport the enderman to a random nearby position
	 */
	public boolean teleportRandomly()
	{
		double d0 = this.posX + (this.rand.nextDouble() - 0.5D) * 64.0D;
		double d1 = this.posY + (this.rand.nextInt(64) - 32);
		double d2 = this.posZ + (this.rand.nextDouble() - 0.5D) * 64.0D;
		return this.teleportTo(d0, d1, d2);
	}

	/**
	 * Teleport the enderman to another entity
	 */
	public boolean teleportToEntity(Entity p_70816_1_)
	{
		Vec3 vec3 = new Vec3(this.posX - p_70816_1_.posX, this.getEntityBoundingBox().minY + this.height / 2.0F - p_70816_1_.posY + p_70816_1_.getEyeHeight(), this.posZ - p_70816_1_.posZ);
		vec3 = vec3.normalize();
		double d0 = 16.0D;
		double d1 = this.posX + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.xCoord * d0;
		double d2 = this.posY + (this.rand.nextInt(16) - 8) - vec3.yCoord * d0;
		double d3 = this.posZ + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3.zCoord * d0;
		return this.teleportTo(d1, d2, d3);
	}

	/**
	 * Teleport the enderman
	 */
	protected boolean teleportTo(double p_70825_1_, double p_70825_3_, double p_70825_5_)
	{
		if (this.dataWatcher.getWatchableObjectInt(19) == 0)//TODO added
		{
			net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, p_70825_1_, p_70825_3_, p_70825_5_, 0);
			if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
			double d3 = this.posX;
			double d4 = this.posY;
			double d5 = this.posZ;
			this.posX = event.targetX;
			this.posY = event.targetY;
			this.posZ = event.targetZ;
			boolean flag = false;
			BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);

			if (this.worldObj.isBlockLoaded(blockpos))
			{
				boolean flag1 = false;

				while (!flag1 && blockpos.getY() > 0)
				{
					BlockPos blockpos1 = blockpos.down();
					Block block = this.worldObj.getBlockState(blockpos1).getBlock();

					if (block.getMaterial().blocksMovement())
					{
						flag1 = true;
					}
					else
					{
						--this.posY;
						blockpos = blockpos1;
					}
				}

				if (flag1)
				{
					super.setPositionAndUpdate(this.posX, this.posY, this.posZ);

					if (this.worldObj.getCollidingBoundingBoxes(this, this.getEntityBoundingBox()).isEmpty() && !this.worldObj.isAnyLiquid(this.getEntityBoundingBox()))
					{
						flag = true;
					}
				}
			}

			if (!flag)
			{
				this.setPosition(d3, d4, d5);
				return false;
			}
			else
			{
				short short1 = 128;

				for (int i = 0; i < short1; ++i)
				{
					double d9 = i / (short1 - 1.0D);
					float f = (this.rand.nextFloat() - 0.5F) * 0.2F;
					float f1 = (this.rand.nextFloat() - 0.5F) * 0.2F;
					float f2 = (this.rand.nextFloat() - 0.5F) * 0.2F;
					double d6 = d3 + (this.posX - d3) * d9 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
					double d7 = d4 + (this.posY - d4) * d9 + this.rand.nextDouble() * this.height;
					double d8 = d5 + (this.posZ - d5) * d9 + (this.rand.nextDouble() - 0.5D) * this.width * 2.0D;
					this.worldObj.spawnParticle(EnumParticleTypes.PORTAL, d6, d7, d8, f, f1, f2, new int[0]);
				}

				this.worldObj.playSoundEffect(d3, d4, d5, "mob.endermen.portal", 1.0F, 1.0F);
				this.playSound("mob.endermen.portal", 1.0F, 1.0F);
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return this.isScreaming() ? "mob.endermen.scream" : "mob.endermen.idle";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.endermen.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.endermen.death";
	}

	@Override
	protected Item getDropItem()
	{
		return Items.ender_pearl;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		Item item = this.getDropItem();

		if (item != null)
		{
			int j = this.rand.nextInt(2 + p_70628_2_);

			for (int k = 0; k < j; ++k)
			{
				this.dropItem(item, 1);
			}
		}
	}

	public void func_175490_a(IBlockState p_175490_1_)
	{
		this.dataWatcher.updateObject(16, Short.valueOf((short)(Block.getStateId(p_175490_1_) & 65535)));
	}

	public IBlockState func_175489_ck()
	{
		return Block.getStateById(this.dataWatcher.getWatchableObjectShort(16) & 65535);
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
		else
		{
			if (source.getEntity() == null || !(source.getEntity() instanceof EntityEndermite))
			{
				if (!this.worldObj.isRemote)
				{
					this.setScreaming(true);
				}

				if (source instanceof EntityDamageSource && source.getEntity() instanceof EntityPlayer)
				{
					if (source.getEntity() instanceof EntityPlayerMP && ((EntityPlayerMP)source.getEntity()).theItemInWorldManager.isCreative())
					{
						this.setScreaming(false);
					}
					else
					{
						this.isAggressive = true;
					}
				}

				if (source instanceof EntityDamageSourceIndirect)
				{
					this.isAggressive = false;

					for (int i = 0; i < 64; ++i)
					{
						if (this.teleportRandomly())
						{
							return true;
						}
					}

					return false;
				}
			}

			boolean flag = super.attackEntityFrom(source, amount);

			if (source.isUnblockable() && this.rand.nextInt(10) != 0)
			{
				this.teleportRandomly();
			}

			return flag;
		}
	}

	public boolean isScreaming()
	{
		return this.dataWatcher.getWatchableObjectByte(18) > 0;
	}

	public void setScreaming(boolean p_70819_1_)
	{
		this.dataWatcher.updateObject(18, Byte.valueOf((byte)(p_70819_1_ ? 1 : 0)));
	}

	static
	{
		carriableBlocks.add(Blocks.grass);
		carriableBlocks.add(Blocks.dirt);
		carriableBlocks.add(Blocks.sand);
		carriableBlocks.add(Blocks.gravel);
		carriableBlocks.add(Blocks.yellow_flower);
		carriableBlocks.add(Blocks.red_flower);
		carriableBlocks.add(Blocks.brown_mushroom);
		carriableBlocks.add(Blocks.red_mushroom);
		carriableBlocks.add(Blocks.tnt);
		carriableBlocks.add(Blocks.cactus);
		carriableBlocks.add(Blocks.clay);
		carriableBlocks.add(Blocks.pumpkin);
		carriableBlocks.add(Blocks.melon_block);
		carriableBlocks.add(Blocks.mycelium);
	}

	class AIFindPlayer extends EntityAINearestAttackableTarget
	{
		private EntityPlayer entityPlayer;
		private int field_179450_h;
		private int field_179451_i;
		private EntityBabyEnderman entityBabyEnderman = EntityBabyEnderman.this;

		public AIFindPlayer()
		{
			super(EntityBabyEnderman.this, EntityPlayer.class, true);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		@SuppressWarnings("unchecked")
		public boolean shouldExecute()
		{
			double d0 = this.getTargetDistance();
			List<?> list = this.taskOwner.worldObj.getEntitiesWithinAABB(EntityPlayer.class, this.taskOwner.getEntityBoundingBox().expand(d0, 4.0D, d0), this.targetEntitySelector);
			Collections.sort(list, this.theNearestAttackableTargetSorter);

			if (list.isEmpty())
			{
				return false;
			}
			else
			{
				this.entityPlayer = (EntityPlayer)list.get(0);
				return true;
			}
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		@Override
		public void startExecuting()
		{
			this.field_179450_h = 5;
			this.field_179451_i = 0;
		}

		/**
		 * Resets the task
		 */
		@Override
		public void resetTask()
		{
			this.entityBabyEnderman.setScreaming(false);
			this.entityPlayer = null;
			IAttributeInstance iattributeinstance = this.entityBabyEnderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
			iattributeinstance.removeModifier(EntityBabyEnderman.attackingSpeedBoostModifier);
			super.resetTask();
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean continueExecuting()
		{
			if (this.entityPlayer != null)
			{
				if (!this.entityBabyEnderman.shouldAttackPlayer(this.entityPlayer))
				{
					return false;
				}
				else
				{
					this.entityBabyEnderman.isAggressive = true;
					this.entityBabyEnderman.faceEntity(this.entityPlayer, 10.0F, 10.0F);
					return true;
				}
			}
			else
			{
				return super.continueExecuting();
			}
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			if (this.entityPlayer != null)
			{
				if (--this.field_179450_h <= 0)
				{
					this.targetEntity = this.entityPlayer;
					this.entityPlayer = null;
					super.startExecuting();
					this.entityBabyEnderman.playSound("mob.endermen.stare", 1.0F, 1.0F);
					this.entityBabyEnderman.setScreaming(true);
					IAttributeInstance iattributeinstance = this.entityBabyEnderman.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
					iattributeinstance.applyModifier(EntityBabyEnderman.attackingSpeedBoostModifier);
				}
			}
			else
			{
				if (this.targetEntity != null)
				{
					if (this.targetEntity instanceof EntityPlayer && this.entityBabyEnderman.shouldAttackPlayer((EntityPlayer)this.targetEntity))
					{
						if (this.targetEntity.getDistanceSqToEntity(this.entityBabyEnderman) < 16.0D)
						{
							this.entityBabyEnderman.teleportRandomly();
						}

						this.field_179451_i = 0;
					}
					else if (this.targetEntity.getDistanceSqToEntity(this.entityBabyEnderman) > 256.0D && this.field_179451_i++ >= 30 && this.entityBabyEnderman.teleportToEntity(this.targetEntity))
					{
						this.field_179451_i = 0;
					}
				}

				super.updateTask();
			}
		}
	}

	class AIPlaceBlock extends EntityAIBase
	{
		private EntityBabyEnderman field_179475_a = EntityBabyEnderman.this;
		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			return !this.field_179475_a.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") ? false : (this.field_179475_a.func_175489_ck().getBlock().getMaterial() == Material.air ? false : this.field_179475_a.getRNG().nextInt(2000) == 0);
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			Random random = this.field_179475_a.getRNG();
			World world = this.field_179475_a.worldObj;
			int i = MathHelper.floor_double(this.field_179475_a.posX - 1.0D + random.nextDouble() * 2.0D);
			int j = MathHelper.floor_double(this.field_179475_a.posY + random.nextDouble() * 2.0D);
			int k = MathHelper.floor_double(this.field_179475_a.posZ - 1.0D + random.nextDouble() * 2.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			Block block = world.getBlockState(blockpos).getBlock();
			Block block1 = world.getBlockState(blockpos.down()).getBlock();

			if (this.func_179474_a(world, blockpos, this.field_179475_a.func_175489_ck().getBlock(), block, block1))
			{
				world.setBlockState(blockpos, this.field_179475_a.func_175489_ck(), 3);
				this.field_179475_a.func_175490_a(Blocks.air.getDefaultState());
			}
		}

		private boolean func_179474_a(World worldIn, BlockPos p_179474_2_, Block p_179474_3_, Block p_179474_4_, Block p_179474_5_)
		{
			return !p_179474_3_.canPlaceBlockAt(worldIn, p_179474_2_) ? false : (p_179474_4_.getMaterial() != Material.air ? false : (p_179474_5_.getMaterial() == Material.air ? false : p_179474_5_.isFullCube()));
		}
	}

	class AITakeBlock extends EntityAIBase
	{
		private EntityBabyEnderman field_179473_a = EntityBabyEnderman.this;
		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			return !this.field_179473_a.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing") ? false : (this.field_179473_a.func_175489_ck().getBlock().getMaterial() != Material.air ? false : this.field_179473_a.getRNG().nextInt(20) == 0);
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			Random random = this.field_179473_a.getRNG();
			World world = this.field_179473_a.worldObj;
			int i = MathHelper.floor_double(this.field_179473_a.posX - 2.0D + random.nextDouble() * 4.0D);
			int j = MathHelper.floor_double(this.field_179473_a.posY + random.nextDouble() * 3.0D);
			int k = MathHelper.floor_double(this.field_179473_a.posZ - 2.0D + random.nextDouble() * 4.0D);
			BlockPos blockpos = new BlockPos(i, j, k);
			IBlockState iblockstate = world.getBlockState(blockpos);
			Block block = iblockstate.getBlock();

			if (EntityBabyEnderman.carriableBlocks.contains(block))
			{
				this.field_179473_a.func_175490_a(iblockstate);
				world.setBlockState(blockpos, Blocks.air.getDefaultState());
			}
		}
	}

	/*===================================== Forge Start ==============================*/
	public static void setCarriable(Block block, boolean canCarry)
	{
		if (canCarry) carriableBlocks.add(block);
		else          carriableBlocks.remove(block);
	}
	public static boolean getCarriable(Block block)
	{
		return carriableBlocks.contains(block);
	}
	/*===================================== Forge End ==============================*/
}