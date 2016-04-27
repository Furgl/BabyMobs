package furgl.babyMobs.common.entity.monster;

import java.util.Random;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.block.ModBlocks;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.ai.EntityAIBabyLeapAtTarget;
import furgl.babyMobs.common.entity.projectile.EntityCaveSpiderVenom;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNavigateClimber;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabySpider extends EntityMob
{
	protected boolean spitting = false;
	protected int spittingCounter = 0;

	public EntityBabySpider(World worldIn)
	{
		super(worldIn);
		this.setSize(0.8F, 0.5F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, this.field_175455_a);
		this.tasks.addTask(3, new EntityAIBabyLeapAtTarget(this, 0.4F));
		this.tasks.addTask(4, new EntityBabySpider.AISpiderAttack(EntityPlayer.class));
		this.tasks.addTask(4, new EntityBabySpider.AISpiderAttack(EntityIronGolem.class));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(2, new EntityBabySpider.AISpiderTarget(EntityPlayer.class));
		this.targetTasks.addTask(3, new EntityBabySpider.AISpiderTarget(EntityIronGolem.class));
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
		return new ItemStack(ModItems.baby_spider_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.30F;
	}


	@Override
	protected PathNavigate func_175447_b(World worldIn)
	{
		return new PathNavigateClimber(this, worldIn);
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(19, new Byte((byte)0));
		this.dataWatcher.addObject(17, 0);//added
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		//TODO venom spitting attack for baby cave spiders and jockey check

		if (!this.getEntityData().hasKey("JockeyCheck") && this.ticksExisted == 1 && !this.worldObj.isRemote && this.riddenByEntity == null && this.getClass() == EntityBabySpider.class)
		{
			this.getEntityData().setBoolean("JockeyCheck", true);
			if(this.worldObj.rand.nextInt(100) <= Config.babySpiderJockeyRate)
			{
				EntityBabySkeleton entityBabySkeleton = new EntityBabySkeleton(this.worldObj);
				entityBabySkeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				entityBabySkeleton.func_180482_a(this.worldObj.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
				this.worldObj.spawnEntityInWorld(entityBabySkeleton);
				entityBabySkeleton.mountEntity(this);
			}
		}
		if (Config.useSpecialAbilities)
		{
			if (!this.worldObj.isRemote)
			{
				if (this.getAttackTarget() != null && this instanceof EntityBabyCaveSpider && !this.spitting && this.getHealth() > 0)
				{
					EntityLivingBase entitylivingbase = this.getAttackTarget();
					double d0 = this.getDistanceSqToEntity(entitylivingbase);

					if (d0 > 10.0D)
						this.dataWatcher.updateObject(17, 1);
					else
						this.dataWatcher.updateObject(17, 0);
				}
				else
					this.dataWatcher.updateObject(17, 0);
			}

			if (this.dataWatcher.getWatchableObjectInt(17) == 1 && this.getAttackTarget() instanceof EntityPlayer && !(this.getAttackTarget() instanceof FakePlayer) && !(((EntityPlayer) this.getAttackTarget()).capabilities.isCreativeMode))
			{
				this.spitting = true;
				this.spittingCounter = 40;

				EntityLivingBase entitylivingbase = this.getAttackTarget();
				if (entitylivingbase != null && !this.worldObj.isRemote)
				{
					this.getLookHelper().setLookPosition(entitylivingbase.posX, entitylivingbase.posY+entitylivingbase.getEyeHeight()/2, entitylivingbase.posZ, 5F, 5F);			
					EntityCaveSpiderVenom venom = new EntityCaveSpiderVenom(this.worldObj, this);
					entitylivingbase.getEyeHeight();
					double d1 = entitylivingbase.posX - this.posX;
					double d3 = entitylivingbase.posZ - this.posZ;
					MathHelper.sqrt_double(d1 * d1 + d3 * d3);
					double x = entitylivingbase.posX - this.posX;
					double y = entitylivingbase.posY - this.posY;
					double z = entitylivingbase.posZ - this.posZ;
					venom.setThrowableHeading(x, y+2, z, 1.6F, 5.0F);
					this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "random.drink", 1.0F, this.rand.nextFloat() * 0.1F + 1.7F);
					this.worldObj.spawnEntityInWorld(venom);
				}
			}

			if (this.spitting)
			{
				this.spittingCounter--;
				this.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 2, 9));
				if (this.spittingCounter == 0)
					this.spitting = false;
			}
		}
		//end

		super.onUpdate();

		if (!this.worldObj.isRemote)
		{
			this.setBesideClimbableBlock(this.isCollidedHorizontally);
		}
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(16.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.30000001192092896D);
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.spider.say";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.spider.say";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.spider.death";
	}

	@Override
	protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
	{
		this.playSound("mob.spider.step", 0.15F, 1.0F);
	}

	@Override
	protected Item getDropItem()
	{
		return Items.string;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		super.dropFewItems(p_70628_1_, p_70628_2_);

		if (p_70628_1_ && (this.rand.nextInt(3) == 0 || this.rand.nextInt(1 + p_70628_2_) > 0))
		{
			this.dropItem(Items.spider_eye, 1);
		}
	}

	/**
	 * returns true if this entity is by a ladder, false otherwise
	 */
	@Override
	public boolean isOnLadder()
	{
		return this.isBesideClimbableBlock();
	}

	/**
	 * Sets the Entity inside a web block.
	 */
	@Override
	public void setInWeb() {}

	/**
	 * Get this Entity's EnumCreatureAttribute
	 */
	@Override
	public EnumCreatureAttribute getCreatureAttribute()
	{
		return EnumCreatureAttribute.ARTHROPOD;
	}

	@Override
	public boolean isPotionApplicable(PotionEffect p_70687_1_)
	{
		return p_70687_1_.getPotionID() == Potion.poison.id ? false : super.isPotionApplicable(p_70687_1_);
	}

	/**
	 * Returns true if the WatchableObject (Byte) is 0x01 otherwise returns false. The WatchableObject is updated using
	 * setBesideClimableBlock.
	 */
	public boolean isBesideClimbableBlock()
	{
		return (this.dataWatcher.getWatchableObjectByte(19) & 1) != 0;
	}

	/**
	 * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
	 * false.
	 */
	public void setBesideClimbableBlock(boolean p_70839_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(19);

		if (p_70839_1_)
		{
			b0 = (byte)(b0 | 1);
		}
		else
		{
			b0 &= -2;
		}

		this.dataWatcher.updateObject(19, Byte.valueOf(b0));
	}

	@Override
	public IEntityLivingData func_180482_a(DifficultyInstance difficulty, IEntityLivingData iEntityLivingData)
	{
		Object obj = super.func_180482_a(difficulty, iEntityLivingData);

		if (obj == null)
		{
			obj = new EntityBabySpider.GroupData();

			if (this.worldObj.getDifficulty() == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * difficulty.getClampedAdditionalDifficulty())
			{
				((EntityBabySpider.GroupData)obj).func_111104_a(this.worldObj.rand);
			}
		}

		if (obj instanceof EntityBabySpider.GroupData)
		{
			int i = ((EntityBabySpider.GroupData)obj).field_111105_a;

			if (i > 0 && Potion.potionTypes[i] != null)
			{
				this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
			}
		}

		return (IEntityLivingData)obj;
	}

	@Override
	public double getMountedYOffset()
	{
		return 0.65D;
	}

	class AISpiderAttack extends EntityAIAttackOnCollide
	{
		private Class classTarget;
		@SuppressWarnings("unused")
		private boolean canPenalize;

		public AISpiderAttack(Class p_i45819_2_)
		{
			super(EntityBabySpider.this, p_i45819_2_, 1.0D, true);
			canPenalize = classTarget == null || !net.minecraft.entity.player.EntityPlayer.class.isAssignableFrom(classTarget);
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		@Override
		public boolean continueExecuting()
		{
			float f = this.attacker.getBrightness(1.0F);

			//TODO spawn web
			if (Config.useSpecialAbilities && this.attacker.getAttackTarget() instanceof EntityPlayer)
			{
				BlockPos pos = new BlockPos(this.attacker.getAttackTarget().posX, this.attacker.getAttackTarget().posY, this.attacker.getAttackTarget().posZ);
				if (this.attacker.worldObj.rand.nextInt(150) == 0 && this.attacker.worldObj.isAirBlock(pos) && this.attacker.getDistanceSqToEntity(this.attacker.getAttackTarget()) < 80D)
					this.attacker.worldObj.setBlockState(pos, ModBlocks.disappearingWeb.getDefaultState());
			}
			//end

			if (f >= 0.5F && this.attacker.getRNG().nextInt(100) == 0)
			{
				this.attacker.setAttackTarget((EntityLivingBase)null);
				return false;
			}
			else
			{
				return super.continueExecuting();
			}
		}

		@Override
		protected double func_179512_a(EntityLivingBase p_179512_1_)
		{
			return 4.0F + p_179512_1_.width;
		}
	}

	class AISpiderTarget extends EntityAINearestAttackableTarget
	{
		public AISpiderTarget(Class p_i45818_2_)
		{
			super(EntityBabySpider.this, p_i45818_2_, true);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		@Override
		public boolean shouldExecute()
		{
			float f = this.taskOwner.getBrightness(1.0F);
			return f >= 0.5F ? false : super.shouldExecute();
		}
	}

	public static class GroupData implements IEntityLivingData
	{
		public int field_111105_a;
		public void func_111104_a(Random p_111104_1_)
		{
			int i = p_111104_1_.nextInt(5);

			if (i <= 1)
			{
				this.field_111105_a = Potion.moveSpeed.id;
			}
			else if (i <= 2)
			{
				this.field_111105_a = Potion.damageBoost.id;
			}
			else if (i <= 3)
			{
				this.field_111105_a = Potion.regeneration.id;
			}
			else if (i <= 4)
			{
				this.field_111105_a = Potion.invisibility.id;
			}
		}
	}

}


