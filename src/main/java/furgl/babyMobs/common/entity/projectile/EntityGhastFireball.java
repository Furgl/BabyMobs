package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.common.BabyMobs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityGhastFireball extends EntityLargeFireball
{
	public EntityGhastFireball(World world)
	{
		super(world);
	}

	public EntityGhastFireball(World world, EntityLivingBase entitylivingbase, double accelX, double accelY, double accelZ) 
	{
		super(world, entitylivingbase, accelX, accelY, accelZ);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.world.isRemote)
		{
			for (int i=0; i<2; i++)
			{
				BabyMobs.proxy.spawnEntityBlazeFlamethrowerFX(this);
			}
			if (this.motionX == 0 && this.motionY == 0 && this.motionZ == 0)
				this.setDead();
		}
	}

	@Override
	protected void onImpact(RayTraceResult mop)
	{
		super.onImpact(mop);
		if (!this.world.isRemote)
		{
			for (int i=0; i<15; i++)
			{
				EntityCreeperExplosion explosion = new EntityCreeperExplosion(this.world, this.shootingEntity);
				explosion.motionX = (rand.nextDouble()-0.5D)*rand.nextDouble();
				explosion.motionY = rand.nextDouble()*0.7D;
				explosion.motionZ = (rand.nextDouble()-0.5D)*rand.nextDouble();
				explosion.setPosition(this.posX, this.posY, this.posZ);
				this.world.spawnEntity(explosion);
			}
		}
	}

}
