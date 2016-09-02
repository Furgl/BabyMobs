package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyEnderman extends EntityEnderman
{
    private static final DataParameter<Float> ENTITY_ID = EntityDataManager.<Float>createKey(EntityEnderman.class, DataSerializers.FLOAT);
    private static final DataParameter<Float> LASER_DELAY = EntityDataManager.<Float>createKey(EntityEnderman.class, DataSerializers.FLOAT);
	private int laserDelay = 0;

	public EntityBabyEnderman(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.6F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(1, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }

	@Override
	protected boolean canDropLoot()
	{
		return true;
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(ModItems.baby_enderman_egg);
	}

	//TODO used in beam
	public EntityPlayer getTargetedEntity()
	{
		if (this.dataManager.get(EntityBabyEnderman.ENTITY_ID) == 0)
			return null;
		else if (this.worldObj.isRemote)
		{
			Entity entity = this.worldObj.getEntityByID(Math.round(this.dataManager.get(EntityBabyEnderman.ENTITY_ID)));
			//if fake player or creative mode
			if (entity instanceof FakePlayer || (entity instanceof EntityPlayer && ((EntityPlayer) entity).capabilities.isCreativeMode))
				return null;
			if (entity instanceof EntityPlayer)
			{
				//this.dataManager.set(SCREAMING, Boolean.valueOf(true));
				return (EntityPlayer)entity;
			}
			else
				return null;
		}
		else
		{
			//if fake player or creative mode
			if (this.getAttackTarget() instanceof FakePlayer || (this.getAttackTarget() instanceof EntityPlayer && ((EntityPlayer) this.getAttackTarget()).capabilities.isCreativeMode))
				return null;
			//this.dataManager.set(SCREAMING, Boolean.valueOf(true));
			return (this.getAttackTarget() instanceof EntityPlayer) ? (EntityPlayer) this.getAttackTarget() : null;
		}
	}
	//end

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(ENTITY_ID, Float.valueOf(0));//entityId (if 0, then no beam)
		this.dataManager.register(LASER_DELAY, Float.valueOf(0));//laserDelay
	}

	@Override
	public float getEyeHeight()
	{
		return 1.3F;
	}
	
	/**
     * Checks to see if this enderman should be attacking this player
     */
    private boolean shouldAttackPlayer(EntityPlayer player)
    {
        ItemStack itemstack = player.inventory.armorInventory[3];

        if (itemstack != null && itemstack.getItem() == Item.getItemFromBlock(Blocks.PUMPKIN))
        {
            return false;
        }
        else
        {
            Vec3d vec3d = player.getLook(1.0F).normalize();
            Vec3d vec3d1 = new Vec3d(this.posX - player.posX, this.getEntityBoundingBox().minY + (double)(this.height / 2.0F) - (player.posY + (double)player.getEyeHeight()), this.posZ - player.posZ);
            double d0 = vec3d1.lengthVector();
            vec3d1 = vec3d1.normalize();
            double d1 = vec3d.dotProduct(vec3d1);
            return d1 > 1.0D - 0.025D / d0 ? player.canEntityBeSeen(this) : false;
        }
    }

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		//TODO beam
		if (Config.useSpecialAbilities)
		{
			if (!this.worldObj.isRemote)
			{
				if (this.dataManager.get(EntityBabyEnderman.LASER_DELAY) == 0 && laserDelay == 0)
				{
					if (this.getAttackTarget() instanceof EntityPlayer && this.dataManager.get(EntityBabyEnderman.ENTITY_ID) != 0) 
					{
						this.getAttackTarget().attackEntityFrom(DamageSource.magic, 1F);
					}
					if (this.getAttackTarget() instanceof EntityPlayer && this.shouldAttackPlayer((EntityPlayer) this.getAttackTarget()) == true && this.canEntityBeSeen(this.getAttackTarget()) && this.getHealth() > 0)
					{
						this.dataManager.set(EntityBabyEnderman.ENTITY_ID, Float.valueOf(this.getAttackTarget().getEntityId()));
						laserDelay = 40;
						this.dataManager.set(EntityBabyEnderman.LASER_DELAY, Float.valueOf(laserDelay));
					}
					else
					{
						this.dataManager.set(EntityBabyEnderman.ENTITY_ID, Float.valueOf(0));					
					}		
				}
				else
				{
					if (laserDelay > 0 && laserDelay % 5 == 0 && this.getAttackTarget() != null) 
					{
						if (this.getAttackTarget() instanceof EntityPlayer && !(this.getAttackTarget() instanceof FakePlayer))
							((EntityPlayer)this.getAttackTarget()).addStat(Achievements.achievementLaserTag);
						this.getAttackTarget().attackEntityFrom(DamageSource.magic, 1F);
					}
					laserDelay--;
					this.dataManager.set(EntityBabyEnderman.LASER_DELAY, Float.valueOf(laserDelay));
				}
			}
		}
		//end
		super.onLivingUpdate();
		//TODO stop moving,look, and make sound when shooting beam
		if (Config.useSpecialAbilities)
		{
			if (this.dataManager.get(EntityBabyEnderman.ENTITY_ID) != 0)
			{
				if (this.getHealth() == 0 || (!this.worldObj.isRemote && this.getAttackTarget() instanceof EntityPlayer && !(this.getAttackTarget() instanceof FakePlayer) && !this.canEntityBeSeen(this.getAttackTarget())))
				{
					this.dataManager.set(EntityBabyEnderman.LASER_DELAY, 0f);
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
					if (this.rand.nextInt(41) >= this.dataManager.get(EntityBabyEnderman.LASER_DELAY))
						this.getTargetedEntity().playSound(SoundEvents.ENTITY_SPIDER_DEATH, 1F, 0.0F);
				}
			}
		}
		//end
	}
}