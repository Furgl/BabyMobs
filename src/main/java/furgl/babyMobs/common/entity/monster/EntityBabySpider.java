package furgl.babyMobs.common.entity.monster;

import java.util.Random;

import furgl.babyMobs.common.block.ModBlocks;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityCaveSpiderVenom;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabySpider extends EntityMob
{
	protected boolean spitting = false;
	protected int spittingCounter = 0;
	private EntityAIBabyHurtByTarget hurtAi = new EntityAIBabyHurtByTarget(this, true, new Class[0]);
	private EntityAIBabyFollowParent followAi = new EntityAIBabyFollowParent(this, 1.1D, this.isAIEnabled());

	public EntityBabySpider(World p_i1743_1_)
	{
		super(p_i1743_1_);
		this.setSize(0.8F, 0.5F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
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
		return new ItemStack(ModItems.baby_spider_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}
	//end

	//TODO hurtAi
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source.getEntity() instanceof EntityPlayer)
		{
			this.setRevengeTarget((EntityLivingBase) source.getEntity());
			if (this.hurtAi.shouldExecute())
				this.hurtAi.startExecuting();
		}
		return super.attackEntityFrom(source, amount);
	}
	//end

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
		this.dataWatcher.addObject(17, 0);//added
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		//TODO followAI
		if (this.getAttackTarget() == null && this.followAi.shouldExecute())
		{
			if (this.followAi.parent != null && this.rand.nextInt(10) == 0)
			{
				this.followAi.startExecuting();
				this.followAi.updateTask();
			}
			else
				this.followAi.resetTask();
		}
		//end
		//TODO venom spitting attack for baby cave spiders and jockey check
		if (!this.getEntityData().hasKey("JockeyCheck") && this.ticksExisted == 1 && !this.worldObj.isRemote && this.riddenByEntity == null && this.getClass() == EntityBabySpider.class)
		{
			this.getEntityData().setBoolean("JockeyCheck", true);
			if(this.worldObj.rand.nextInt(100) <= Config.babySpiderJockeyRate)
			{
				EntityBabySkeleton entityBabySkeleton = new EntityBabySkeleton(this.worldObj);
				entityBabySkeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				entityBabySkeleton.onSpawnWithEgg((IEntityLivingData)null);
				this.worldObj.spawnEntityInWorld(entityBabySkeleton);
				entityBabySkeleton.mountEntity(this);
			}
		}
		if (Config.useSpecialAbilities)
		{
			if (!this.worldObj.isRemote)
			{
				if (this.entityToAttack instanceof EntityLivingBase && this instanceof EntityBabyCaveSpider && !this.spitting && this.getHealth() > 0)
				{
					EntityLivingBase entitylivingbase = (EntityLivingBase) this.entityToAttack;
					double d0 = this.getDistanceSqToEntity(entitylivingbase);

					if (d0 > 10.0D)
						this.dataWatcher.updateObject(17, 1);
					else
						this.dataWatcher.updateObject(17, 0);
				}
				else
					this.dataWatcher.updateObject(17, 0);
			}

			if (this.dataWatcher.getWatchableObjectInt(17) == 1 && this.entityToAttack instanceof EntityPlayer && !(this.entityToAttack instanceof FakePlayer) && !(((EntityPlayer) this.entityToAttack).capabilities.isCreativeMode))
			{
				this.spitting = true;
				this.spittingCounter = 40;

				EntityLivingBase entitylivingbase = (EntityLivingBase)this.entityToAttack;
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
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(3D);//changed
	}

	/**
	 * Finds the closest player within 16 blocks to attack, or null if this Entity isn't interested in attacking
	 * (Animals, BabySpiders at day, peaceful PigZombies).
	 */
	@Override
	protected Entity findPlayerToAttack()
	{
		float f = this.getBrightness(1.0F);

		if (f < 0.5F)
		{
			double d0 = 16.0D;
			return this.worldObj.getClosestVulnerablePlayerToEntity(this, d0);
		}
		else
		{
			return null;
		}
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
	protected void func_145780_a(int p_145780_1_, int p_145780_2_, int p_145780_3_, Block p_145780_4_)
	{
		this.playSound("mob.spider.step", 0.15F, 1.0F);
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
	 */
	@Override
	protected void attackEntity(Entity target, float p_70785_2_)
	{
		float f1 = this.getBrightness(1.0F);

		//TODO spawn web
		if (Config.useSpecialAbilities && target instanceof EntityPlayer)
		{
			if(this.worldObj.rand.nextInt(150) == 0 && this.worldObj.isAirBlock((int)target.posX, (int)target.posY, (int)target.posZ))
			{
				this.worldObj.setBlock((int)target.posX, (int)target.posY, (int)target.posZ, ModBlocks.disappearingWeb);
			}
		}
		//end

		if (f1 > 0.5F && this.rand.nextInt(100) == 0)
		{
			this.entityToAttack = null;
		}
		else
		{
			if (p_70785_2_ > 2.0F && p_70785_2_ < 6.0F && this.rand.nextInt(10) == 0)
			{
				if (this.onGround)
				{
					double d0 = target.posX - this.posX;
					double d1 = target.posZ - this.posZ;
					float f2 = MathHelper.sqrt_double(d0 * d0 + d1 * d1);
					this.motionX = d0 / f2 * 0.5D * 0.800000011920929D + this.motionX * 0.20000000298023224D;
					this.motionZ = d1 / f2 * 0.5D * 0.800000011920929D + this.motionZ * 0.20000000298023224D;
					this.motionY = 0.4000000059604645D;
				}
			}
			else
			{
				super.attackEntity(target, p_70785_2_);
			}
		}
	}

	@Override
	protected Item getDropItem()
	{
		return Items.string;
	}

	/**
	 * Drop 0-2 items of this living's type. @param par1 - Whether this entity has recently been hit by a player. @param
	 * par2 - Level of Looting used to kill this mob.
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
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	/**
	 * Updates the WatchableObject (Byte) created in entityInit(), setting it to 0x01 if par1 is true or 0x00 if it is
	 * false.
	 */
	public void setBesideClimbableBlock(boolean p_70839_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (p_70839_1_)
		{
			b0 = (byte)(b0 | 1);
		}
		else
		{
			b0 &= -2;
		}

		this.dataWatcher.updateObject(16, Byte.valueOf(b0));
	}

	@Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
	{
		Object p_110161_1_1 = super.onSpawnWithEgg(p_110161_1_);

		if (p_110161_1_1 == null)
		{
			p_110161_1_1 = new EntityBabySpider.GroupData();

			if (this.worldObj.difficultySetting == EnumDifficulty.HARD && this.worldObj.rand.nextFloat() < 0.1F * this.worldObj.func_147462_b(this.posX, this.posY, this.posZ))
			{
				((EntityBabySpider.GroupData)p_110161_1_1).func_111104_a(this.worldObj.rand);
			}
		}

		if (p_110161_1_1 instanceof EntityBabySpider.GroupData)
		{
			int i = ((EntityBabySpider.GroupData)p_110161_1_1).field_111105_a;

			if (i > 0 && Potion.potionTypes[i] != null)
			{
				this.addPotionEffect(new PotionEffect(i, Integer.MAX_VALUE));
			}
		}

		return (IEntityLivingData)p_110161_1_1;
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