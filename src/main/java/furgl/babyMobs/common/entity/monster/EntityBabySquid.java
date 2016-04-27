package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.projectile.EntitySquidInk;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityWaterMob;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityBabySquid extends EntityWaterMob
{
    public float squidPitch;
    public float prevSquidPitch;
    public float squidYaw;
    public float prevSquidYaw;
    /** appears to be rotation in radians; we already have pitch & yaw, so this completes the triumvirate. */
    public float squidRotation;
    /** previous squidRotation in radians */
    public float prevSquidRotation;
    /** angle of the tentacles in radians */
    public float tentacleAngle;
    /** the last calculated angle of the tentacles in radians */
    public float lastTentacleAngle;
    private float randomMotionSpeed;
    /** change in squidRotation in radians. */
    private float rotationVelocity;
    private float field_70871_bB;
    private float randomMotionVecX;
    private float randomMotionVecY;
    private float randomMotionVecZ;
    
    public EntityBabySquid(World p_i1693_1_)
    {
        super(p_i1693_1_);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		
        this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
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
		return new ItemStack(ModItems.baby_squid_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}
	//end
    
  //TODO squid ink
  	/**
  	 * Called when the entity is attacked.
  	 */
  	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
  	{
  		if (Config.useSpecialAbilities)
  		{
  			if (this.isInWater() && source.getEntity() instanceof EntityLivingBase && this.getHealth() > 0)
  			{
  				Vec3 vec = Vec3.createVectorHelper(this.posX, this.posY, this.posZ);
  				EntitySpawner entitySpawner = new EntitySpawner(EntitySquidInk.class, this.worldObj, vec, 5);
  				entitySpawner.setShapeSphere(0);
  				entitySpawner.setMovementInOut(-1D);
  				entitySpawner.setRandVar(0.5D);
  				entitySpawner.run();

  				this.randomMotionSpeed = 2.0F;
  				this.squidRotation = (float) (Math.PI-1);
  				this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.slime.attack", 1.0F, this.rand.nextFloat() * 0.4F + 8F);
  			}
  		}
  		return super.attackEntityFrom(source, amount);
  	}
  	//end

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(10.0D);
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
        return Item.getItemById(0);
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

    /**
     * Drop 0-2 items of this living's type
     */
    @Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
    {
        int j = this.rand.nextInt(3 + p_70628_2_) + 1;

        for (int k = 0; k < j; ++k)
        {
            this.entityDropItem(new ItemStack(Items.dye, 1, 0), 0.0F);
        }
    }

    /**
     * Checks if this entity is inside water (if inWater field is true as a result of handleWaterMovement() returning
     * true)
     */
    @Override
	public boolean isInWater()
    {
        return this.worldObj.handleMaterialAcceleration(this.boundingBox.expand(0.0D, -0.6000000238418579D, 0.0D), Material.water, this);
    }

    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
	public void onLivingUpdate()
    {
        super.onLivingUpdate();
        this.prevSquidPitch = this.squidPitch;
        this.prevSquidYaw = this.squidYaw;
        this.prevSquidRotation = this.squidRotation;
        this.lastTentacleAngle = this.tentacleAngle;
        this.squidRotation += this.rotationVelocity;

        if (this.squidRotation > ((float)Math.PI * 2F))
        {
            this.squidRotation -= ((float)Math.PI * 2F);

            if (this.rand.nextInt(10) == 0)
            {
                this.rotationVelocity = 1.0F / (this.rand.nextFloat() + 1.0F) * 0.2F;
            }
        }

        if (this.isInWater())
        {
            float f;

            if (this.squidRotation < (float)Math.PI)
            {
                f = this.squidRotation / (float)Math.PI;
                this.tentacleAngle = MathHelper.sin(f * f * (float)Math.PI) * (float)Math.PI * 0.25F;

                if (f > 0.75D)
                {
                    this.randomMotionSpeed = 1.0F;
                    this.field_70871_bB = 1.0F;
                }
                else
                {
                    this.field_70871_bB *= 0.8F;
                }
            }
            else
            {
                this.tentacleAngle = 0.0F;
                this.randomMotionSpeed *= 0.9F;
                this.field_70871_bB *= 0.99F;
            }

            if (!this.worldObj.isRemote)
            {
                this.motionX = this.randomMotionVecX * this.randomMotionSpeed;
                this.motionY = this.randomMotionVecY * this.randomMotionSpeed;
                this.motionZ = this.randomMotionVecZ * this.randomMotionSpeed;
            }

            f = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.renderYawOffset += (-((float)Math.atan2(this.motionX, this.motionZ)) * 180.0F / (float)Math.PI - this.renderYawOffset) * 0.1F;
            this.rotationYaw = this.renderYawOffset;
            this.squidYaw += (float)Math.PI * this.field_70871_bB * 1.5F;
            this.squidPitch += (-((float)Math.atan2(f, this.motionY)) * 180.0F / (float)Math.PI - this.squidPitch) * 0.1F;
        }
        else
        {
            this.tentacleAngle = MathHelper.abs(MathHelper.sin(this.squidRotation)) * (float)Math.PI * 0.25F;

            if (!this.worldObj.isRemote)
            {
                this.motionX = 0.0D;
                this.motionY -= 0.08D;
                this.motionY *= 0.9800000190734863D;
                this.motionZ = 0.0D;
            }

            this.squidPitch = (float)(this.squidPitch + (-90.0F - this.squidPitch) * 0.02D);
        }
    }

    /**
     * Moves the entity based on the specified heading.  Args: strafe, forward
     */
    @Override
	public void moveEntityWithHeading(float p_70612_1_, float p_70612_2_)
    {
        this.moveEntity(this.motionX, this.motionY, this.motionZ);
    }

    @Override
	protected void updateEntityActionState()
    {
        ++this.entityAge;

        if (this.entityAge > 100)
        {
            this.randomMotionVecX = this.randomMotionVecY = this.randomMotionVecZ = 0.0F;
        }
        else if (this.rand.nextInt(50) == 0 || !this.inWater || this.randomMotionVecX == 0.0F && this.randomMotionVecY == 0.0F && this.randomMotionVecZ == 0.0F)
        {
            float f = this.rand.nextFloat() * (float)Math.PI * 2.0F;
            this.randomMotionVecX = MathHelper.cos(f) * 0.2F;
            this.randomMotionVecY = -0.1F + this.rand.nextFloat() * 0.2F;
            this.randomMotionVecZ = MathHelper.sin(f) * 0.2F;
        }

        this.despawnEntity();
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
	public boolean getCanSpawnHere()
    {
        return this.posY > 45.0D && this.posY < 63.0D && super.getCanSpawnHere();
    }
}