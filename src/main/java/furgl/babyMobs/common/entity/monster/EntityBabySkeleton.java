package furgl.babyMobs.common.entity.monster;

import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntitySkeletonArrow;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabySkeleton extends EntityMob implements IRangedAttackMob
{
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);

	public int effectType;
	public PotionEffect potionEffect;
	public double red;
	public double green;
	public double blue;

	public EntityBabySkeleton(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIRestrictSun(this));
		this.tasks.addTask(2, this.field_175455_a);
		this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, new Predicate()
		{
			public boolean func_179945_a(Entity p_179945_1_)
			{
				return p_179945_1_ instanceof EntityWolf;
			}
			@Override
			public boolean apply(Object p_apply_1_)
			{
				return this.func_179945_a((Entity)p_apply_1_);
			}
		}, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));

		if (worldIn != null && !worldIn.isRemote)
		{
			this.setCombatTask();
		}
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).triggerAchievement(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
		
		if (cause.getSourceOfDamage() instanceof EntityArrow && cause.getEntity() instanceof EntityPlayer)
		{
			EntityPlayer entityplayer = (EntityPlayer)cause.getEntity();
			double d0 = entityplayer.posX - this.posX;
			double d1 = entityplayer.posZ - this.posZ;

			if (d0 * d0 + d1 * d1 >= 2500.0D)
			{
				entityplayer.triggerAchievement(AchievementList.snipeSkeleton);
			}
		}
		else if (cause.getEntity() instanceof EntityCreeper && ((EntityCreeper)cause.getEntity()).getPowered() && ((EntityCreeper)cause.getEntity()).isAIEnabled())
		{
			((EntityCreeper)cause.getEntity()).func_175493_co();
			this.entityDropItem(new ItemStack(Items.skull, 1, this.getSkeletonType() == 1 ? 1 : 0), 0.0F);
		}
    }
	
	@Override
	protected boolean func_146066_aG()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_skeleton_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	//TODO when attacked give effect
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (Config.useSpecialAbilities) 
		{
			if (!this.worldObj.isRemote && this.potionEffect != null && source.getEntity() instanceof EntityPlayer && !(source.getEntity() instanceof FakePlayer) && !(source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode))
			{
				if (this.effectType == 1)
					this.potionEffect = new PotionEffect(Potion.poison.id, 50, 0);
				else if (this.effectType == 2)
					this.potionEffect = new PotionEffect(Potion.blindness.id, 50, 0);
				else if (this.effectType == 3)
					this.potionEffect = new PotionEffect(Potion.wither.id, 50, 0);
				else if (this.effectType == 4)
					this.potionEffect = new PotionEffect(Potion.confusion.id, 100, 1);
				else if (this.effectType == 5)
					this.potionEffect = new PotionEffect(Potion.moveSlowdown.id, 50, 0);
				((EntityPlayer)source.getEntity()).addPotionEffect(this.potionEffect);
			}
		}
		return super.attackEntityFrom(source, amount);
	}
	//end

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(13, new Byte((byte)0));
		this.dataWatcher.addObject(20, Integer.valueOf(0));//potionEffect
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.skeleton.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.skeleton.hurt";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.skeleton.death";
	}

	@Override
	protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
	{
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
	}

	@Override
	public boolean attackEntityAsMob(Entity entity)
	{
		if (super.attackEntityAsMob(entity))
		{
			if (!this.worldObj.isRemote && this.potionEffect != null && entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode))
			{
				if (this.effectType == 1)
					this.potionEffect = new PotionEffect(Potion.poison.id, 50, 0);
				else if (this.effectType == 2)
					this.potionEffect = new PotionEffect(Potion.blindness.id, 50, 0);
				else if (this.effectType == 3)
					this.potionEffect = new PotionEffect(Potion.wither.id, 50, 0);
				else if (this.effectType == 4)
					this.potionEffect = new PotionEffect(Potion.confusion.id, 100, 1);
				else if (this.effectType == 5)
					this.potionEffect = new PotionEffect(Potion.moveSlowdown.id, 50, 0);
				((EntityLivingBase)entity).addPotionEffect(this.potionEffect);
			}
			return true;
		}
		else
		{
			return false;
		}
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
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		//TODO effects
		if (Config.useSpecialAbilities)
		{
			if (!this.worldObj.isRemote && this.ticksExisted == 1)
			{
				if (!this.getEntityData().hasKey("effectType"))
				{
					this.getEntityData().setInteger("effectType", this.rand.nextInt(5)+1);
					this.effectType = this.getEntityData().getInteger("effectType");
					this.dataWatcher.updateObject(20, effectType);
				}
				else
				{
					this.effectType = this.getEntityData().getInteger("effectType");
					this.dataWatcher.updateObject(20, effectType);
				}
			}
			if (this.potionEffect == null && (this.effectType = this.dataWatcher.getWatchableObjectInt(20)) > 0)
			{
				if (this.effectType == 1)
					this.potionEffect = new PotionEffect(Potion.poison.id, 50, 0);
				else if (this.effectType == 2)
					this.potionEffect = new PotionEffect(Potion.blindness.id, 50, 0);
				else if (this.effectType == 3)
					this.potionEffect = new PotionEffect(Potion.wither.id, 50, 0);
				else if (this.effectType == 4)
					this.potionEffect = new PotionEffect(Potion.confusion.id, 100, 1);
				else if (this.effectType == 5)
					this.potionEffect = new PotionEffect(Potion.moveSlowdown.id, 50, 0);
				Map map = Maps.newHashMap();
				map.put(1, this.potionEffect);
				int i = PotionHelper.calcPotionLiquidColor(map.values());
				this.red = (i >> 16 & 255) / 255.0D;
				this.green = (i >> 8 & 255) / 255.0D;
				this.blue = (i >> 0 & 255) / 255.0D;
			}
			if (this.worldObj.isRemote)
			{
				if (this.ticksExisted == 3)
				{
					BabyMobs.proxy.spawnEntitySkeletonEffectFX(this.worldObj, this, (float) this.red, (float) this.green, (float) this.blue);
				}
				this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, this.red, this.green, this.blue, new int[0]);
			}
		}
		//end

		super.onLivingUpdate();
	}

	/**
	 * Handles updating while being ridden by an entity
	 */
	@Override
	public void updateRidden()
	{
		super.updateRidden();

		if (this.ridingEntity instanceof EntityCreature)
		{
			EntityCreature entitycreature = (EntityCreature)this.ridingEntity;
			this.renderYawOffset = entitycreature.renderYawOffset;
		}
	}

	@Override
	protected Item getDropItem()
	{
		return Items.arrow;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j;
		int k;

		if (this.getSkeletonType() == 1)
		{
			j = this.rand.nextInt(3 + p_70628_2_) - 1;

			for (k = 0; k < j; ++k)
			{
				this.dropItem(Items.coal, 1);
			}
		}
		else
		{
			j = this.rand.nextInt(3 + p_70628_2_);

			for (k = 0; k < j; ++k)
			{
				this.dropItem(Items.arrow, 1);
			}
		}

		j = this.rand.nextInt(3 + p_70628_2_);

		for (k = 0; k < j; ++k)
		{
			this.dropItem(Items.bone, 1);
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	@Override
	protected void addRandomArmor()
	{
		if (this.getSkeletonType() == 1)
		{
			this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
		}
	}

	@Override
	protected void func_180481_a(DifficultyInstance p_180481_1_)
	{
		super.func_180481_a(p_180481_1_);
		this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	}

	@Override
	public IEntityLivingData func_180482_a(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
	{
		 p_180482_2_ = super.func_180482_a(p_180482_1_, p_180482_2_);

	        if (this.worldObj.provider instanceof WorldProviderHell && this.getRNG().nextInt(5) > 0)
	        {
	            /*this.tasks.addTask(4, this.aiAttackOnCollide);
	            this.setSkeletonType(1);
	            this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
	            this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);*/
	        }
	        else
	        {
	            this.tasks.addTask(4, this.aiArrowAttack);
	            this.func_180481_a(p_180482_1_);
	            this.func_180483_b(p_180482_1_);
	        }

	        this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * p_180482_1_.getClampedAdditionalDifficulty());

	        /*if (this.getEquipmentInSlot(4) == null)
	        {
	            Calendar calendar = this.worldObj.getCurrentDate();

	            if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
	            {
	                this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
	                this.equipmentDropChances[4] = 0.0F;
	            }
	        }*/

	        return p_180482_2_;
	}

	/**
	 * sets this entity's combat AI.
	 */
	public void setCombatTask()
	{
		this.tasks.removeTask(this.aiAttackOnCollide);
		this.tasks.removeTask(this.aiArrowAttack);
		ItemStack itemstack = this.getHeldItem();

		if (itemstack != null && itemstack.getItem() == Items.bow)
		{
			this.tasks.addTask(4, this.aiArrowAttack);
		}
		else
		{
			this.tasks.addTask(4, this.aiAttackOnCollide);
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
	{
		//TODO changed to EntitySkeletonArrow from EntityArrow
		if (Config.useSpecialAbilities)
		{
			EntitySkeletonArrow entityarrow = new EntitySkeletonArrow(this.worldObj, this, p_82196_1_, 1.6F, 14 - this.worldObj.getDifficulty().getDifficultyId() * 4);
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
			int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
			entityarrow.setDamage(p_82196_2_ * 2.0F + this.rand.nextGaussian() * 0.25D + this.worldObj.getDifficulty().getDifficultyId() * 0.11F);
			if (i > 0)
			{
				entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
			}

			if (j > 0)
			{
				entityarrow.setKnockbackStrength(j);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1)
			{
				entityarrow.setFire(100);
			}

			this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.worldObj.spawnEntityInWorld(entityarrow);
		}
		else
		{
			EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F, 14 - this.worldObj.getDifficulty().getDifficultyId() * 4);
			int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
			int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
			entityarrow.setDamage(p_82196_2_ * 2.0F + this.rand.nextGaussian() * 0.25D + this.worldObj.getDifficulty().getDifficultyId() * 0.11F);
			if (i > 0)
			{
				entityarrow.setDamage(entityarrow.getDamage() + i * 0.5D + 0.5D);
			}

			if (j > 0)
			{
				entityarrow.setKnockbackStrength(j);
			}

			if (EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, this.getHeldItem()) > 0 || this.getSkeletonType() == 1)
			{
				entityarrow.setFire(100);
			}

			this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.worldObj.spawnEntityInWorld(entityarrow);
		}
		//end
	}

	/**
	 * Return this skeleton's type.
	 */
	public int getSkeletonType()
	{
		return this.dataWatcher.getWatchableObjectByte(13);
	}

	/**
	 * Set this skeleton's type.
	 */
	public void setSkeletonType(int p_82201_1_)
	{
		this.dataWatcher.updateObject(13, Byte.valueOf((byte)p_82201_1_));
		this.isImmuneToFire = p_82201_1_ == 1;

		if (p_82201_1_ == 1)
		{
			this.setSize(0.72F, 2.535F);
		}
		else
		{
			this.setSize(0.6F, 1.95F);
		}
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);

		if (tagCompund.hasKey("SkeletonType", 99))
		{
			byte b0 = tagCompund.getByte("SkeletonType");
			this.setSkeletonType(b0);
		}

		this.setCombatTask();
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setByte("SkeletonType", (byte)this.getSkeletonType());
	}

	/**
	 * Sets the held item, or an armor slot. Slot 0 is held item. Slot 1-4 is armor. Params: Item, slot
	 */
	@Override
	public void setCurrentItemOrArmor(int slotIn, ItemStack stack)
	{
		super.setCurrentItemOrArmor(slotIn, stack);

		if (!this.worldObj.isRemote && slotIn == 0)
		{
			this.setCombatTask();
		}
	}

	/**
	 * Returns the Y Offset of this entity.
	 */
	@Override
	public double getYOffset()
	{
		return super.getYOffset() - 0.5D;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.9F;
	}
}


