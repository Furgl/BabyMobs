package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityCreeperExplosion;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyCreeper extends EntityCreeper
{
    private int explosionRadius = 3;
	
	public EntityBabyCreeper(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);

		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(2, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }

	// //TODO sound and middle click
    @Override
	protected boolean canDropLoot()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
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
	public float getEyeHeight()
	{
		return 0.9F;
	}	

	@Override
	public void onUpdate()
	{
		//TODO sparks and explosion
		if (this.isEntityAlive())
		{
			if (Config.useSpecialAbilities)
			{
				int ignitedTime = (int) BabyMobs.reflect(EntityCreeper.class, "timeSinceIgnited", this);
				int fuse = (int) BabyMobs.reflect(EntityCreeper.class, "fuseTime", this);
				
				if (rand.nextInt(20)==0 && this.worldObj.isRemote)
				{
					for (int i=0; i<ignitedTime/5+1; i++)
						this.worldObj.spawnParticle(EnumParticleTypes.LAVA, this.posX, this.posY, this.posZ, 0, 0, 0, new int[0]);
				}
				else if(this.worldObj.isRemote)
				{
					for (int i=0; i<ignitedTime/8; i++)
						this.worldObj.spawnParticle(EnumParticleTypes.LAVA, this.posX, this.posY, this.posZ, 0, 0, 0, new int[0]);
				}
				if (ignitedTime >= fuse-1 && !this.worldObj.isRemote)
					this.explode();
			}
		}
		//end 
		
		super.onUpdate();
	}

	/**
	 * Creates an explosion as determined by this creeper's power and explosion radius.
	 */
	private void explode()
	{
		if (!this.worldObj.isRemote)
		{
			int ignitedTime = (int) BabyMobs.reflect(EntityCreeper.class, "timeSinceIgnited", this);
			boolean flag = this.worldObj.getGameRules().getBoolean("mobGriefing");
			float f = this.getPowered() ? 2.0F : 1.0F;
			this.worldObj.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * f, flag);
			
			//TODO creeperexplosion
			EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 10D);
			if (player != null)
				player.addStat(Achievements.achievementBoomBaby);
			for (int i=0; i<ignitedTime/2; i++)
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
}
