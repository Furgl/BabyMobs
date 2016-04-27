package furgl.babyMobs.common.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityBlazeFlamethrower;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySmallFireball;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyBlaze extends EntityMob
{
	/** Random offset used in floating behavior */
	private float heightOffset = 0.5F;
	/** ticks until heightOffset is randomized */
	private int heightOffsetUpdateTime;
	private int field_70846_g;

	private EntityAIBabyHurtByTarget hurtAi = new EntityAIBabyHurtByTarget(this, true, new Class[0]);
	private EntityAIBabyFollowParent followAi = new EntityAIBabyFollowParent(this, 1.1D, this.isAIEnabled());

	public EntityBabyBlaze(World world)
	{
		super(world);
		this.experienceValue = 25;
		this.setSize(0.6F, 1.2F);

		this.isImmuneToFire = true;
	}

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

	//TODO sound and middle click
	@Override
	protected boolean func_146066_aG()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_blaze_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}
	//end

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(3D);//changed
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, new Byte((byte)0));
		this.dataWatcher.addObject(17, 0);//added
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return "mob.blaze.breathe";
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.blaze.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.blaze.death";
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float p_70070_1_)
	{
		return 15728880;
	}

	/**
	 * Gets how bright this entity is.
	 */
	@Override
	public float getBrightness(float p_70013_1_)
	{
		return 1.0F;
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
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
		//TODO flamethrower
		if (Config.useSpecialAbilities) 
		{
			if (!this.worldObj.isRemote)
			{
				if (this.entityToAttack instanceof EntityPlayer && this.canEntityBeSeen(this.entityToAttack) && !(this.entityToAttack instanceof FakePlayer) && !(((EntityPlayer)this.entityToAttack).capabilities.isCreativeMode))
				{
					Entity entity = this.entityToAttack;
					double d0 = this.getDistanceSqToEntity(entity);

					if (d0 < 40.0D)
						this.dataWatcher.updateObject(17, 1);
					else
						this.dataWatcher.updateObject(17, 0);
				}
				else
				{
					this.entityToAttack = null;
					this.dataWatcher.updateObject(17, 0);
				}
			}
			if (this.dataWatcher.getWatchableObjectInt(17) == 1 && this.getHealth() > 0)
			{
				Entity entity = this.entityToAttack;
				if (entity instanceof EntityPlayer && !(entity instanceof FakePlayer))
				{
					this.getLookHelper().setLookPosition(entity.posX, entity.posY+entity.getEyeHeight()/2, entity.posZ, 100F, 100F);
					if (this.posY > entity.posY+2)
						this.motionY = -1D;
					else if (this.posY < entity.posY-2)
						this.motionY = 1D;
					else
						this.motionY *= 0.1D;
				}
				if (this.ticksExisted % 10 == 0)
					this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.ghast.fireball", 1.0F, this.rand.nextFloat() * 0.2F + 0.0F);
				Vec3 vec = Vec3.createVectorHelper(this.posX, 0.5 + this.posY, this.posZ);

				if (this.ticksExisted % 10 == 0 && entity instanceof EntityPlayer && !(entity instanceof FakePlayer))
				{
					EntitySpawner entitySpawner = new EntitySpawner(EntityBlazeFlamethrower.class, worldObj, vec, 1);
					entitySpawner.setShapeLine(0.5D, this.rotationPitch, this.rotationYawHead);
					entitySpawner.setMovementFollowShape(0.2D);
					entitySpawner.setDelay(8);
					entitySpawner.setRandVar(0.2D);
					entitySpawner.run();
				}
				BabyMobs.proxy.spawnEntityBlazeFlamethrowerFX(this, vec);
			}
		} 
		//end 

		if (!this.worldObj.isRemote)
		{
			if (this.isWet())
			{
				this.attackEntityFrom(DamageSource.drown, 1.0F);
			}

			--this.heightOffsetUpdateTime;

			if (this.heightOffsetUpdateTime <= 0)
			{
				this.heightOffsetUpdateTime = 100;
				this.heightOffset = 0.5F + (float)this.rand.nextGaussian() * 3.0F;
			}

			if (this.getEntityToAttack() != null && this.getEntityToAttack().posY + this.getEntityToAttack().getEyeHeight() > this.posY + this.getEyeHeight() + this.heightOffset)
			{
				this.motionY += (0.30000001192092896D - this.motionY) * 0.30000001192092896D;
			}
		}

		if (this.rand.nextInt(24) == 0)
		{
			this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.fire", 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F);
		}

		if (!this.onGround && this.motionY < 0.0D)
		{
			this.motionY *= 0.6D;
		}

		for (int i = 0; i < 2; ++i)
		{
			this.worldObj.spawnParticle("smoke", this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, 0.0D, 0.0D, 0.0D);
		}

		super.onLivingUpdate();
	}

	/**
	 * Basic mob attack. Default to touch of death in EntityCreature. Overridden by each mob to define their attack.
	 */
	@Override
	protected void attackEntity(Entity entity, float par2)
	{
		if (this.attackTime <= 0 && par2 < 2.0F && entity.boundingBox.maxY > this.boundingBox.minY && entity.boundingBox.minY < this.boundingBox.maxY)
		{
			this.attackTime = 20;
			this.attackEntityAsMob(entity);
		}
		else if (par2 < 30.0F)
		{
			double d0 = entity.posX - this.posX;
			double d1 = entity.boundingBox.minY + entity.height / 2.0F - (this.posY + this.height / 2.0F);
			double d2 = entity.posZ - this.posZ;

			if (this.attackTime == 0)
			{
				++this.field_70846_g;

				if (this.field_70846_g == 1)
				{
					this.attackTime = 60;
					this.func_70844_e(true);
				}
				else if (this.field_70846_g <= 4)
				{
					this.attackTime = 6;
				}
				else
				{
					this.attackTime = 100;
					this.field_70846_g = 0;
					this.func_70844_e(false);
				}

				if (this.field_70846_g > 1)
				{
					float f1 = MathHelper.sqrt_float(par2) * 0.5F;
					this.worldObj.playAuxSFXAtEntity((EntityPlayer)null, 1009, (int)this.posX, (int)this.posY, (int)this.posZ, 0);

					for (int i = 0; i < 1; ++i)
					{
						EntitySmallFireball entitysmallfireball = new EntitySmallFireball(this.worldObj, this, d0 + this.rand.nextGaussian() * f1, d1, d2 + this.rand.nextGaussian() * f1);
						entitysmallfireball.posY = this.posY + this.height / 2.0F + 0.5D;
						this.worldObj.spawnEntityInWorld(entitysmallfireball);
					}
				}
			}

			this.rotationYaw = (float)(Math.atan2(d2, d0) * 180.0D / Math.PI) - 90.0F;
			this.hasAttacked = true;
		}
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float distance) {}

	@Override
	protected Item getDropItem()
	{
		return Items.blaze_rod;
	}

	/**
	 * Returns true if the entity is on fire. Used by render to add the fire effect on rendering.
	 */
	@Override
	public boolean isBurning()
	{
		return this.func_70845_n();
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		if (p_70628_1_)
		{
			int j = this.rand.nextInt(2 + p_70628_2_);

			for (int k = 0; k < j; ++k)
			{
				this.dropItem(Items.blaze_rod, 1);
			}
		}
	}

	public boolean func_70845_n()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void func_70844_e(boolean p_70844_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (p_70844_1_)
		{
			b0 = (byte)(b0 | 1);
		}
		else
		{
			b0 &= -2;
		}

		this.dataWatcher.updateObject(16, Byte.valueOf(b0));
	}

	/**
	 * Checks to make sure the light is not too bright where the mob is spawning
	 */
	@Override
	protected boolean isValidLightLevel()
	{
		return true;
	}
}