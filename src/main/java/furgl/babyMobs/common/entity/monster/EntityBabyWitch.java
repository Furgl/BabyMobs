package furgl.babyMobs.common.entity.monster;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabyWitch extends EntityMob implements IRangedAttackMob
{
	private static final UUID field_110184_bp = UUID.fromString("5CD17E52-A79A-43D3-A529-90FDE04B181E");
	private static final AttributeModifier field_110185_bq = (new AttributeModifier(field_110184_bp, "Drinking speed penalty", -0.25D, 0)).setSaved(false);
	/** List of items a witch should drop on death. */
	private static final Item[] witchDrops = new Item[] {Items.glowstone_dust, Items.sugar, Items.redstone, Items.spider_eye, Items.glass_bottle, Items.gunpowder, Items.stick, Items.stick};
	/**
	 * Timer used as interval for a witch's attack, decremented every tick if aggressive and when reaches zero the witch
	 * will throw a potion at the target entity.
	 */
	private int witchAttackTimer;
	public EntityBabyWitch(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));

		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIArrowAttack(this, 1.0D, 60, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		//this.tasks.addTask(2, this.field_175455_a); ??
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(3, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
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
		return new ItemStack(ModItems.baby_witch_egg);
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
		this.getDataWatcher().addObject(21, Byte.valueOf((byte)0));
	}

	/**
	 * Returns the sound this mob makes while it's alive.
	 */
	@Override
	protected String getLivingSound()
	{
		return null;
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return null;
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return null;
	}

	/**
	 * Set whether this witch is aggressive at an entity.
	 */
	public void setAggressive(boolean p_82197_1_)
	{
		this.getDataWatcher().updateObject(21, Byte.valueOf((byte)(p_82197_1_ ? 1 : 0)));
	}

	/**
	 * Return whether this witch is aggressive at an entity.
	 */
	public boolean getAggressive()
	{
		return this.getDataWatcher().getWatchableObjectByte(21) == 1;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(26.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		if (!this.worldObj.isRemote)
		{
			if (this.getAggressive())
			{
				if (this.witchAttackTimer-- <= 0)
				{
					this.setAggressive(false);
					ItemStack itemstack = this.getHeldItem();
					this.setCurrentItemOrArmor(0, (ItemStack)null);

					if (itemstack != null && itemstack.getItem() == Items.potionitem)
					{
						List list = Items.potionitem.getEffects(itemstack);

						if (list != null)
						{
							Iterator iterator = list.iterator();

							while (iterator.hasNext())
							{
								PotionEffect potioneffect = (PotionEffect)iterator.next();
								this.addPotionEffect(new PotionEffect(potioneffect));
							}
						}
					}

					this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).removeModifier(field_110185_bq);
				}
			}
			else
			{
				short short1 = -1;

				if (this.rand.nextFloat() < 0.15F && this.isInsideOfMaterial(Material.water) && !this.isPotionActive(Potion.waterBreathing))
				{
					short1 = 8237;
				}
				else if (this.rand.nextFloat() < 0.15F && this.isBurning() && !this.isPotionActive(Potion.fireResistance))
				{
					short1 = 16307;
				}
				else if (this.rand.nextFloat() < 0.05F && this.getHealth() < this.getMaxHealth())
				{
					short1 = 16341;
				}
				else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D)
				{
					short1 = 16274;
				}
				else if (this.rand.nextFloat() < 0.25F && this.getAttackTarget() != null && !this.isPotionActive(Potion.moveSpeed) && this.getAttackTarget().getDistanceSqToEntity(this) > 121.0D)
				{
					short1 = 16274;
				}

				if (short1 > -1)
				{
					this.setCurrentItemOrArmor(0, new ItemStack(Items.potionitem, 1, short1));
					this.witchAttackTimer = this.getHeldItem().getMaxItemUseDuration();
					this.setAggressive(true);
					IAttributeInstance iattributeinstance = this.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
					iattributeinstance.removeModifier(field_110185_bq);
					iattributeinstance.applyModifier(field_110185_bq);
				}
			}

			if (this.rand.nextFloat() < 7.5E-4F)
			{
				this.worldObj.setEntityState(this, (byte)15);
			}
		}

		super.onLivingUpdate();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleStatusUpdate(byte p_70103_1_)
	{
		if (p_70103_1_ == 15)
		{
			for (int i = 0; i < this.rand.nextInt(35) + 10; ++i)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.SPELL_WITCH, this.posX + this.rand.nextGaussian() * 0.12999999523162842D, this.getEntityBoundingBox().maxY + 0.5D + this.rand.nextGaussian() * 0.12999999523162842D, this.posZ + this.rand.nextGaussian() * 0.12999999523162842D, 0.0D, 0.0D, 0.0D, new int[0]);
			}
		}
		else
		{
			super.handleStatusUpdate(p_70103_1_);
		}
	}

	/**
	 * Reduces damage, depending on potions
	 */
	@Override
	protected float applyPotionDamageCalculations(DamageSource p_70672_1_, float p_70672_2_)
	{
		p_70672_2_ = super.applyPotionDamageCalculations(p_70672_1_, p_70672_2_);

		if (p_70672_1_.getEntity() == this)
		{
			p_70672_2_ = 0.0F;
		}

		if (p_70672_1_.isMagicDamage())
		{
			p_70672_2_ = (float)(p_70672_2_ * 0.15D);
		}

		return p_70672_2_;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(3) + 1;

		for (int k = 0; k < j; ++k)
		{
			int l = this.rand.nextInt(3);
			Item item = witchDrops[this.rand.nextInt(witchDrops.length)];

			if (p_70628_2_ > 0)
			{
				l += this.rand.nextInt(p_70628_2_ + 1);
			}

			for (int i1 = 0; i1 < l; ++i1)
			{
				this.dropItem(item, 1);
			}
		}
	}

	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase entitylivingbase, float p_82196_2_)
	{
		if (!this.getAggressive())
		{
			EntityPotion entitypotion = new EntityPotion(this.worldObj, this, 32732);
			double d0 = entitylivingbase.posY + entitylivingbase.getEyeHeight() - 1.100000023841858D;
			entitypotion.rotationPitch -= -20.0F;
			double d1 = entitylivingbase.posX + entitylivingbase.motionX - this.posX;
			double d2 = d0 - this.posY;
			double d3 = entitylivingbase.posZ + entitylivingbase.motionZ - this.posZ;
			float f1 = MathHelper.sqrt_double(d1 * d1 + d3 * d3);

			if (f1 >= 8.0F && !entitylivingbase.isPotionActive(Potion.moveSlowdown))
			{
				entitypotion.setPotionDamage(32698);
			}
			else if (entitylivingbase.getHealth() >= 8.0F && !entitylivingbase.isPotionActive(Potion.poison))
			{
				entitypotion.setPotionDamage(32660);
			}
			else if (f1 <= 3.0F && !entitylivingbase.isPotionActive(Potion.weakness) && this.rand.nextFloat() < 0.25F)
			{
				entitypotion.setPotionDamage(32696);
			}

			entitypotion.setThrowableHeading(d1, d2 + f1 * 0.2F, d3, 0.75F, 8.0F);
			this.worldObj.spawnEntityInWorld(entitypotion);
		}
	}

	@Override
	public float getEyeHeight()
	{
		return 0.9F;
	}
}