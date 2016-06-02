package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityBlazeFlamethrower;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyBlaze extends EntityBlaze
{
    private static final DataParameter<Byte> FLAMETHROWER = EntityDataManager.<Byte>createKey(EntityBabyBlaze.class, DataSerializers.BYTE);
	
	public EntityBabyBlaze(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
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
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(ModItems.baby_blaze_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.7F;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.register(FLAMETHROWER, Byte.valueOf((byte)0));//added
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		//TODO flamethrower
		if (Config.useSpecialAbilities) 
		{
			if (!this.worldObj.isRemote)
			{
				if (this.getAttackTarget() instanceof EntityPlayer && this.canEntityBeSeen(this.getAttackTarget()) && !(this.getAttackTarget() instanceof FakePlayer) && !(((EntityPlayer)this.getAttackTarget()).capabilities.isCreativeMode))
				{
					EntityLivingBase entitylivingbase = this.getAttackTarget();
					double d0 = this.getDistanceSqToEntity(entitylivingbase);

					if (d0 < 40.0D)
						this.dataWatcher.set(FLAMETHROWER, (byte)1);
					else
						this.dataWatcher.set(FLAMETHROWER, (byte)0);
				}
				else
					this.dataWatcher.set(FLAMETHROWER, (byte)0);
			}
			if (this.dataWatcher.get(FLAMETHROWER) == 1 && this.getHealth() > 0)
			{
				EntityLivingBase entitylivingbase = this.getAttackTarget();
				if (entitylivingbase instanceof EntityPlayer && !(entitylivingbase instanceof FakePlayer))
				{
					this.getLookHelper().setLookPosition(entitylivingbase.posX, entitylivingbase.posY+entitylivingbase.getEyeHeight()/2, entitylivingbase.posZ, 100F, 100F);
					if (this.posY > entitylivingbase.posY+2)
						this.motionY = -1D;
					else if (this.posY < entitylivingbase.posY-2)
						this.motionY = 1D;
					else
						this.motionY *= 0.1D;
				}
				if (this.ticksExisted % 10 == 0)
					this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.entity_ghast_shoot, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.2F + 0.0F, false);
				Vec3d vec = new Vec3d(this.posX, 0.5 + this.posY, this.posZ);

				if (this.ticksExisted % 10 == 0 && entitylivingbase instanceof EntityPlayer && !(entitylivingbase instanceof FakePlayer))
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

		super.onLivingUpdate();
	}
}