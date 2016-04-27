package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class EntityBabyOcelot extends EntityTameable
{
    public EntityBabyOcelot(World p_i1688_1_)
    {
        super(p_i1688_1_);
        this.setSize(0.9F, 0.9F);
        this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.tasks.addTask(1, new EntityAIBabyFollowParent(this, 1.1D, this.isAIEnabled()));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, 0, true));
		
        this.tasks.addTask(1, new EntityAISwimming(this));    
        this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
        this.tasks.addTask(8, new EntityAIOcelotAttack(this));
        this.tasks.addTask(10, new EntityAIWander(this, 0.8D));
        this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
        this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, 750, false));
    }
    
  //TODO added
    @Override
	public void onUpdate()
    {
    	if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
        {
            this.setDead();
        }
    	if (this.ticksExisted == 1)
    	{
    		this.setTamed(true);
    		this.setTameSkin(1);
    		this.setGrowingAge(-2000000);
    	}
    	if (this.worldObj.isRemote && this.rand.nextInt(2) == 0)
    	{
    		this.worldObj.spawnParticle("smoke", this.posX+(this.rand.nextDouble()-0.5D), this.posY+(this.rand.nextDouble()-0.5D), this.posZ+(this.rand.nextDouble()-0.5D), 0, 0, 0);
    	}
    	if (this.motionX == 0 && this.motionZ == 0 && this.getAttackTarget() instanceof EntityPlayer)
    	{
    		this.getMoveHelper().setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, 1.0D);
    	}    	
    	super.onUpdate();
    }
    
    @Override
	public boolean isPotionApplicable(PotionEffect effect)
    {
    	int i = effect.getPotionID();
    	
        if (i == Potion.poison.id || i == Potion.moveSlowdown.id)
        {
            return false;
        }
    	return true;	
    }
    //end

    @Override
	protected void entityInit()
    {
        super.entityInit();
        this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
    }

    /**
     * main AI tick function, replaces updateEntityActionState
     */
    @Override
	public void updateAITick()
    {
        if (this.getMoveHelper().isUpdating())
        {
            double d0 = this.getMoveHelper().getSpeed();

            if (d0 == 0.6D)
            {
                this.setSneaking(true);
                this.setSprinting(false);
            }
            else if (d0 == 1.33D)
            {
                this.setSneaking(false);
                this.setSprinting(true);
            }
            else
            {
                this.setSneaking(false);
                this.setSprinting(false);
            }
        }
        else
        {
            this.setSneaking(false);
            this.setSprinting(false);
        }
    }

    /**
     * Determines if an entity can be despawned, used on idle far away entities
     */
    @Override
	protected boolean canDespawn()
    {
        return true;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     */
    @Override
	public boolean isAIEnabled()
    {
        return true;
    }

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.3D);
    }

    /**
     * Called when the mob is falling. Calculates and applies fall damage.
     */
    @Override
	protected void fall(float distance) {}

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("CatType", this.getTameSkin());
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        this.setTameSkin(tagCompund.getInteger("CatType"));
    }

    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
	protected String getLivingSound()
    {
        return this.isTamed() ? (this.isInLove() ? "mob.cat.purr" : (this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow")) : null;
    }

    /**
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        return "mob.cat.hitt";
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        return "mob.cat.hitt";
    }

    /**
     * Returns the volume for the sounds this mob makes.
     */
    @Override
	protected float getSoundVolume()
    {
        return 0.4F;
    }

    @Override
	protected Item getDropItem()
    {
        return Items.leather;
    }

    @Override
	public boolean attackEntityAsMob(Entity p_70652_1_)
    {
        return p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
	public boolean attackEntityFrom(DamageSource source, float amount)
    {
        if (this.isEntityInvulnerable())
        {
            return false;
        }
        else if (source.isMagicDamage())
        {
        	return false; //TODO prevent potion damage
        }
        else
        {
            this.aiSit.setSitting(false);
            return super.attackEntityFrom(source, amount);
        }
    }

    /**
     * Drop 0-2 items of this living's type
     */
    @Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_) {}

    @Override
	public EntityBabyOcelot createChild(EntityAgeable p_90011_1_)
    {
        EntityBabyOcelot entityocelot = new EntityBabyOcelot(this.worldObj);

        if (this.isTamed())
        {
            entityocelot.func_152115_b(this.func_152113_b());
            entityocelot.setTamed(true);
            entityocelot.setTameSkin(this.getTameSkin());
        }

        return entityocelot;
    }

    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
	public boolean isBreedingItem(ItemStack p_70877_1_)
    {
        return p_70877_1_ != null && p_70877_1_.getItem() == Items.fish;
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    @Override
	public boolean canMateWith(EntityAnimal p_70878_1_)
    {
        if (p_70878_1_ == this)
        {
            return false;
        }
        else if (!this.isTamed())
        {
            return false;
        }
        else if (!(p_70878_1_ instanceof EntityBabyOcelot))
        {
            return false;
        }
        else
        {
            EntityBabyOcelot entityocelot = (EntityBabyOcelot)p_70878_1_;
            return !entityocelot.isTamed() ? false : this.isInLove() && entityocelot.isInLove();
        }
    }

    public int getTameSkin()
    {
        return this.dataWatcher.getWatchableObjectByte(18);
    }

    public void setTameSkin(int p_70912_1_)
    {
        this.dataWatcher.updateObject(18, Byte.valueOf((byte)p_70912_1_));
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
	public boolean getCanSpawnHere()
    {
        if (this.worldObj.rand.nextInt(3) == 0)
        {
            return false;
        }
        else
        {
            if (this.worldObj.checkNoEntityCollision(this.boundingBox) && this.worldObj.getCollidingBoundingBoxes(this, this.boundingBox).isEmpty() && !this.worldObj.isAnyLiquid(this.boundingBox))
            {
                int i = MathHelper.floor_double(this.posX);
                int j = MathHelper.floor_double(this.boundingBox.minY);
                int k = MathHelper.floor_double(this.posZ);

                if (j < 63)
                {
                    return false;
                }

                Block block = this.worldObj.getBlock(i, j - 1, k);

                if (block == Blocks.grass || block.isLeaves(worldObj, i, j - 1, k))
                {
                    return true;
                }
            }

            return false;
        }
    }

    /**
     * Gets the name of this command sender (usually username, but possibly "Rcon")
     */
    @Override
	public String getCommandSenderName()
    {
        return this.hasCustomNameTag() ? this.getCustomNameTag() : (this.isTamed() ? StatCollector.translateToLocal("entity.Cat.name") : super.getCommandSenderName());
    }

    @Override
	public IEntityLivingData onSpawnWithEgg(IEntityLivingData p_110161_1_)
    {
        p_110161_1_ = super.onSpawnWithEgg(p_110161_1_);

        /*if (this.worldObj.rand.nextInt(7) == 0)
        {
            for (int i = 0; i < 2; ++i)
            {
                EntityBabyOcelot entityocelot = new EntityBabyOcelot(this.worldObj);
                entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entityocelot.setGrowingAge(-24000);
                this.worldObj.spawnEntityInWorld(entityocelot);
            }
        }*/

        return p_110161_1_;
    }
}