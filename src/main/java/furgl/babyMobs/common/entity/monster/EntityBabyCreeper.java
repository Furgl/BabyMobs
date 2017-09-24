package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityCreeperExplosion;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

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
//		if (!this.world.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
//			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
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
		return ModEntities.getSpawnEgg(this.getClass());
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
				int ignitedTime = ReflectionHelper.getPrivateValue(EntityCreeper.class, this, 4); //timeSinceIgnited
				int fuse = ReflectionHelper.getPrivateValue(EntityCreeper.class, this, 5); //fuseTime
				
				if (rand.nextInt(20)==0 && this.world.isRemote)
				{
					for (int i=0; i<ignitedTime/5+1; i++)
						this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX, this.posY, this.posZ, 0, 0, 0, new int[0]);
				}
				else if(this.world.isRemote)
				{
					for (int i=0; i<ignitedTime/8; i++)
						this.world.spawnParticle(EnumParticleTypes.LAVA, this.posX, this.posY, this.posZ, 0, 0, 0, new int[0]);
				}
				if (ignitedTime >= fuse-1 && !this.world.isRemote)
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
		if (!this.world.isRemote)
		{
			int ignitedTime = ReflectionHelper.getPrivateValue(EntityCreeper.class, this, 4); //timeSinceIgnited
			boolean flag = this.world.getGameRules().getBoolean("mobGriefing");
			float f = this.getPowered() ? 2.0F : 1.0F;
			this.world.createExplosion(this, this.posX, this.posY, this.posZ, this.explosionRadius * f, flag);
			
			//TODO creeperexplosion
			EntityPlayer player = this.world.getClosestPlayerToEntity(this, 10D);
			if (player != null)
				//player.addStat(Achievements.achievementBoomBaby);
			for (int i=0; i<ignitedTime/2; i++)
			{
				EntityCreeperExplosion explosion = new EntityCreeperExplosion(this.world, this);
				explosion.motionX = (rand.nextDouble()-0.5D)*rand.nextDouble();
				explosion.motionY = rand.nextDouble()*0.7D;
				explosion.motionZ = (rand.nextDouble()-0.5D)*rand.nextDouble();
				explosion.setPosition(this.posX, this.posY, this.posZ);
				this.world.spawnEntity(explosion);
			}
			//end
			
			this.setDead();
		}
	}
}
