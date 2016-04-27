package furgl.babyMobs.common.entity.monster;

import com.google.common.base.Predicate;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyAvoidEntity;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityWitherSkeletonSmoke;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
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
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyWitherSkeleton extends EntityMob implements IRangedAttackMob
{
	private EntityAIArrowAttack aiArrowAttack = new EntityAIArrowAttack(this, 1.0D, 20, 60, 15.0F);
	private EntityAIAttackOnCollide aiAttackOnCollide = new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.2D, false);

	public EntityBabyWitherSkeleton(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.setSkeletonType(1);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.maxHurtResistantTime = 300;
		this.getNavigator().setAvoidsWater(true);

		this.tasks.addTask(2, new EntityAISwimming(this));
		this.tasks.addTask(1, new EntityAIBabyAvoidEntity(this, new Predicate()
		{
			public boolean func_179945_a(Entity p_179945_1_)
			{
				return p_179945_1_ instanceof EntityWolf || p_179945_1_ instanceof EntityPlayer || p_179945_1_ instanceof EntityArrow;
			}
			@Override
			public boolean apply(Object p_apply_1_)
			{
				return this.func_179945_a((Entity)p_apply_1_);
			}
		}, 12.0F, 1.2D, 1.6D)); //orig 6.0F, 1.0D, 1.2D
		this.tasks.addTask(4, new EntityAIWander(this, 1.2D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
		if (worldIn != null && !worldIn.isRemote)
		{
			this.setCombatTask();
		}
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
		return new ItemStack(ModItems.baby_wither_skeleton_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}
	//end

	//TODO on attack 
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (Config.useSpecialAbilities)
		{
			EntityPlayer player = null;
			if (source.getSourceOfDamage() instanceof EntityPlayer)
				player = (EntityPlayer) source.getSourceOfDamage();
			else if (source.getSourceOfDamage() instanceof EntityArrow && ((EntityArrow) source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer)
				player = (EntityPlayer) ((EntityArrow) source.getSourceOfDamage()).shootingEntity;
			if (player != null && !(player instanceof FakePlayer) && this.getHealth() > 0 && this.hurtResistantTime < 25)
			{
				player.triggerAchievement(Achievements.achievementItsMine);
				if (!this.worldObj.isRemote && !player.capabilities.isCreativeMode)
				{
					player.addPotionEffect(new PotionEffect(Potion.wither.id, 100));
					player.addPotionEffect(new PotionEffect(Potion.confusion.id, 140, 3));
					player.addPotionEffect(new PotionEffect(Potion.blindness.id, 20, 3));
					player.knockBack(player, 0.0F, this.posX-player.posX, this.posZ-player.posZ);
					Vec3 vec = Vec3.createVectorHelper(this.posX, this.posY+0.5D, this.posZ);
					EntitySpawner entitySpawner = new EntitySpawner(EntityWitherSkeletonSmoke.class, this.worldObj, vec, 5);
					entitySpawner.setShapeSphere(0);
					entitySpawner.setMovementInOut(-1D);
					entitySpawner.setRandVar(0.3D);
					entitySpawner.run();
				}
				else
				{
					player.playSound("mob.wither.hurt", 1.0F, rand.nextFloat()*0.2F + 1.8F);
					for (int i=0; i<100; i++)
					{
						BabyMobs.proxy.spawnEntitySquidInkFX(worldObj,this.posX+(rand.nextDouble()-0.5D), this.posY+rand.nextDouble()*1.1D, this.posZ+(rand.nextDouble()-0.5D), 0, 0, 0);
						this.worldObj.spawnParticle("largesmoke", this.posX+(rand.nextDouble()-0.5D), this.posY+rand.nextDouble()*1.1D, this.posZ+(rand.nextDouble()-0.5D), 0, 0, 0);
					}
				}
				if (!this.worldObj.isRemote)
					this.addPotionEffect(new PotionEffect(Potion.invisibility.id, 50));

				PathNavigate entityPathNavigate = this.getNavigator();
				Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this, 16, 7, Vec3.createVectorHelper(player.posX, player.posY, player.posZ));
				if (vec3 != null)
				{
					while (player.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < player.getDistanceSqToEntity(this))
					{
						vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this, 16, 7, Vec3.createVectorHelper(player.posX, player.posY, player.posZ));
					}
					entityPathNavigate.setPath(entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord), 1.2D);
				}
			}
		}
		return super.attackEntityFrom(source, amount);
	}
	//end

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(13, new Byte((byte)0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled()
	{
		return true;
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

	protected void playStepSound(int x, int y, int z, Block blockIn)
	{
		this.playSound("mob.skeleton.step", 0.15F, 1.0F);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		if (super.attackEntityAsMob(p_70652_1_))
		{
			if (this.getSkeletonType() == 1 && p_70652_1_ instanceof EntityLivingBase)
			{
				((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(Potion.wither.id, 200));
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
		if (this.getHeldItem() != null)
			this.setCurrentItemOrArmor(0, null);
		if (this.worldObj.isDaytime() && !this.worldObj.isRemote)
		{
			float f = this.getBrightness(1.0F);

			if (f > 0.5F && this.rand.nextFloat() * 30.0F < (f - 0.4F) * 2.0F && this.worldObj.canBlockSeeTheSky(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ)))
			{
				boolean flag = true;
				ItemStack itemstack = this.getEquipmentInSlot(4);

				if (itemstack != null)
				{
					if (itemstack.isItemStackDamageable())
					{
						itemstack.setItemDamage(itemstack.getItemDamageForDisplay() + this.rand.nextInt(2));

						if (itemstack.getItemDamageForDisplay() >= itemstack.getMaxDamage())
						{
							this.renderBrokenItemStack(itemstack);
							this.setCurrentItemOrArmor(4, (ItemStack)null);
						}
					}

					flag = false;
				}

				if (flag)
				{
					this.setFire(8);
				}
			}
		}

		if (this.worldObj.isRemote && this.getSkeletonType() == 1)
		{
			//this.setSize(0.72F, 2.34F);
		}

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

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource cause)
	{
		super.onDeath(cause);
		//TODO skull drop
		if (cause.getEntity() instanceof EntityPlayer)
		{
			int chance = 70 + 10*EnchantmentHelper.getLootingModifier((EntityLivingBase) cause.getEntity());
			if (this.rand.nextInt(100) <= chance)
			{
				this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
			}
		}
		//end

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

	@Override
	protected void dropRareDrop(int p_70600_1_)
	{
		if (this.getSkeletonType() == 1)
		{
			this.entityDropItem(new ItemStack(Items.skull, 1, 1), 0.0F);
		}
	}

	/**
	 * Makes entity wear random armor based on difficulty
	 */
	@Override
	protected void addRandomArmor()
	{
		super.addRandomArmor();
		this.setCurrentItemOrArmor(0, new ItemStack(Items.bow));
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
	{
		p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);

		if (this.worldObj.provider instanceof WorldProviderHell && this.getRNG().nextInt(5) > 0)
		{
			this.tasks.addTask(4, this.aiAttackOnCollide);
			this.setSkeletonType(1);
			this.setCurrentItemOrArmor(0, new ItemStack(Items.stone_sword));
			this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(4.0D);
		}
		else
		{
			/*this.tasks.addTask(4, this.aiArrowAttack);
             this.addRandomArmor();
             this.enchantEquipment();*/
		}

		this.setCanPickUpLoot(this.rand.nextFloat() < 0.55F * this.worldObj.func_147462_b(this.posX, this.posY, this.posZ));

		/* if (this.getEquipmentInSlot(4) == null)
         {
             Calendar calendar = this.worldObj.getCurrentDate();

             if (calendar.get(2) + 1 == 10 && calendar.get(5) == 31 && this.rand.nextFloat() < 0.25F)
             {
                 this.setCurrentItemOrArmor(4, new ItemStack(this.rand.nextFloat() < 0.1F ? Blocks.lit_pumpkin : Blocks.pumpkin));
                 this.equipmentDropChances[4] = 0.0F;
             }
         }*/

		return p_110161_1_;
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
		EntityArrow entityarrow = new EntityArrow(this.worldObj, this, p_82196_1_, 1.6F, 14 - this.worldObj.difficultySetting.getDifficultyId() * 4);
		int i = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, this.getHeldItem());
		int j = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, this.getHeldItem());
		entityarrow.setDamage(p_82196_2_ * 2.0F + this.rand.nextGaussian() * 0.25D + this.worldObj.difficultySetting.getDifficultyId() * 0.11F);

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
			//this.setSize(0.72F, 2.34F);
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
	public void setCurrentItemOrArmor(int slotIn, ItemStack itemStackIn)
	{
		super.setCurrentItemOrArmor(slotIn, itemStackIn);

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
}