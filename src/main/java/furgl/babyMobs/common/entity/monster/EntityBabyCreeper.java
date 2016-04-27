package furgl.babyMobs.common.entity.monster;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyCreeperSwell;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.projectile.EntityCreeperExplosion;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBabyCreeper extends EntityMob
{
	/**
	 * Time when this creeper was last in an active state (Messed up code here, probably causes creeper animation to go
	 * weird)
	 */
	private int lastActiveTime;
	/** The amount of time since the creeper was close enough to the player to ignite */
	private int timeSinceIgnited;
	private int fuseTime = 30;
	/** Explosion radius for this creeper. */
	private int explosionRadius = 3;
	public EntityBabyCreeper(World p_i1733_1_)
	{
		super(p_i1733_1_);
		this.setSize(0.6F, 1.2F);

		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D, this.isAIEnabled()));
		
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIBabyCreeperSwell(this));
		this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityOcelot.class, 6.0F, 1.0D, 1.2D));
		this.tasks.addTask(4, new EntityAIAttackOnCollide(this, 1.0D, false));
		this.tasks.addTask(5, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
		this.tasks.addTask(6, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
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
		return new ItemStack(ModItems.baby_creeper_egg);
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
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
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
	 * The maximum height from where the entity is alowed to jump (used in pathfinder)
	 */
	public int getMaxFallHeight()
	{
		return this.getAttackTarget() == null ? 3 : 3 + (int)(this.getHealth() - 1.0F);
	}

	/**
	 * Called when the mob is falling. Calculates and applies fall damage.
	 */
	@Override
	protected void fall(float distance)
	{
		super.fall(distance);
		this.timeSinceIgnited = (int)(this.timeSinceIgnited + distance * 1.5F);

		if (this.timeSinceIgnited > this.fuseTime - 5)
		{
			this.timeSinceIgnited = this.fuseTime - 5;
		}
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte) - 1));
		this.dataWatcher.addObject(17, Byte.valueOf((byte)0));
		this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);

		if (this.dataWatcher.getWatchableObjectByte(17) == 1)
		{
			tagCompound.setBoolean("powered", true);
		}

		tagCompound.setShort("Fuse", (short)this.fuseTime);
		tagCompound.setByte("ExplosionRadius", (byte)this.explosionRadius);
		tagCompound.setBoolean("ignited", this.func_146078_ca());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.dataWatcher.updateObject(17, Byte.valueOf((byte)(tagCompund.getBoolean("powered") ? 1 : 0)));

		if (tagCompund.hasKey("Fuse", 99))
		{
			this.fuseTime = tagCompund.getShort("Fuse");
		}

		if (tagCompund.hasKey("ExplosionRadius", 99))
		{
			this.explosionRadius = tagCompund.getByte("ExplosionRadius");
		}

		if (tagCompund.getBoolean("ignited"))
		{
			this.func_146079_cb();
		}
	}

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		if (this.isEntityAlive())
		{
			//TODO sparks and explosion
			if (Config.useSpecialAbilities)
			{
				if (rand.nextInt(20)==0 && this.worldObj.isRemote)
				{
					for (int i=0; i<this.timeSinceIgnited/5+1; i++)
						this.worldObj.spawnParticle("lava", this.posX, this.posY, this.posZ, 0, 0, 0);
				}
				else if(this.worldObj.isRemote)
				{
					for (int i=0; i<this.timeSinceIgnited/8; i++)
						this.worldObj.spawnParticle("lava", this.posX, this.posY, this.posZ, 0, 0, 0);
				}
				if (this.timeSinceIgnited >= this.fuseTime && !this.worldObj.isRemote)
				{
					this.func_146077_cc();
				}
			}
			//end 
			this.lastActiveTime = this.timeSinceIgnited;

			if (this.func_146078_ca())
			{
				this.setCreeperState(1);
			}

			int i = this.getCreeperState();

			if (i > 0 && this.timeSinceIgnited == 0)
			{
				this.playSound("creeper.primed", 1.0F, 0.5F);
			}

			this.timeSinceIgnited += i;

			if (this.timeSinceIgnited < 0)
			{
				this.timeSinceIgnited = 0;
			}

			if (this.timeSinceIgnited >= this.fuseTime)
			{
				this.timeSinceIgnited = this.fuseTime;
				this.func_146077_cc();
			}
		}

		super.onUpdate();
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.creeper.say";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.creeper.death";
	}

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_)
	{
		super.onDeath(p_70645_1_);

		if (p_70645_1_.getEntity() instanceof EntitySkeleton)
		{
			int i = Item.getIdFromItem(Items.record_13);
			int j = Item.getIdFromItem(Items.record_wait);
			int k = i + this.rand.nextInt(j - i + 1);
			this.dropItem(Item.getItemById(k), 1);
		}
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		return true;
	}

	/**
	 * Returns true if the creeper is powered by a lightning bolt.
	 */
	public boolean getPowered()
	{
		return this.dataWatcher.getWatchableObjectByte(17) == 1;
	}

	/**
	 * Params: (Float)Render tick. Returns the intensity of the creeper's flash when it is ignited.
	 */
	@SideOnly(Side.CLIENT)
	public float getCreeperFlashIntensity(float p_70831_1_)
	{
		return (this.lastActiveTime + (this.timeSinceIgnited - this.lastActiveTime) * p_70831_1_) / (this.fuseTime - 2);
	}

	@Override
	protected Item getDropItem()
	{
		return Items.gunpowder;
	}

	/**
	 * Returns the current state of creeper, -1 is idle, 1 is 'in fuse'
	 */
	public int getCreeperState()
	{
		return this.dataWatcher.getWatchableObjectByte(16);
	}

	/**
	 * Sets the state of creeper, -1 to idle and 1 to be 'in fuse'
	 */
	public void setCreeperState(int p_70829_1_)
	{
		this.dataWatcher.updateObject(16, Byte.valueOf((byte)p_70829_1_));
	}

	/**
	 * Called when a lightning bolt hits the entity.
	 */
	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt)
	{
		super.onStruckByLightning(lightningBolt);
		this.dataWatcher.updateObject(17, Byte.valueOf((byte)1));
	}

	/**
	 * Called when a player interacts with a mob. e.g. gets milk from a cow, gets into the saddle on a pig.
	 */
	@Override
	protected boolean interact(EntityPlayer p_70085_1_)
	{
		ItemStack itemstack = p_70085_1_.inventory.getCurrentItem();

		if (itemstack != null && itemstack.getItem() == Items.flint_and_steel)
		{
			this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "fire.ignite", 1.0F, this.rand.nextFloat() * 0.4F + 0.8F);
			p_70085_1_.swingItem();

			if (!this.worldObj.isRemote)
			{
				this.func_146079_cb();
				itemstack.damageItem(1, p_70085_1_);
				return true;
			}
		}

		return super.interact(p_70085_1_);
	}

	private void func_146077_cc()
	{
		if (!this.worldObj.isRemote)
		{
			boolean flag = this.worldObj.getGameRules().getGameRuleBooleanValue("mobGriefing");

			if (this.getPowered())
			{
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * 2, flag);
			}
			else
			{
				this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius, flag);
			}

			//TODO creeperexplosion
			EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 10D);
			if (player != null)
				player.triggerAchievement(Achievements.achievementBoomBaby);
			for (int i=0; i<this.timeSinceIgnited/2; i++)
			{
				EntityCreeperExplosion explosion = new EntityCreeperExplosion(this.worldObj, this);
				explosion.motionX = (rand.nextDouble()-0.5D)*rand.nextDouble();
				explosion.motionY = rand.nextDouble()*0.7D;
				explosion.motionZ = (rand.nextDouble()-0.5D)*rand.nextDouble();
				explosion.setPosition(this.posX, this.posY, this.posZ);
				this.worldObj.spawnEntityInWorld(explosion);
			}
			//end
			
			this.setDead();
		}
	}

	public boolean func_146078_ca()
	{
		return this.dataWatcher.getWatchableObjectByte(18) != 0;
	}

	public void func_146079_cb()
	{
		this.dataWatcher.updateObject(18, Byte.valueOf((byte)1));
	}
}