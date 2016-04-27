package furgl.babyMobs.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityWitherWitherSkull extends EntityWitherSkull
{

	public EntityWitherWitherSkull(World world)
	{
		super(world);
	}

	public EntityWitherWitherSkull(World world, EntityLivingBase entitylivingbase, double accelX, double accelY, double accelZ) 
	{
		super(world, entitylivingbase, accelX, accelY, accelZ);
	}

	@Override
	public void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(20, Integer.valueOf(0));		
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.motionX == 0 && this.motionY == 0 && this.motionZ == 0)
			this.setDead();
	}

	@Override
	protected void onImpact(MovingObjectPosition mop)
	{
		super.onImpact(mop);
		if (!this.worldObj.isRemote && this.shootingEntity != null)
		{
			for (int i=0; i<7; i++)
			{
				EntityWitherExplosion explosion = new EntityWitherExplosion(this.worldObj, this.shootingEntity);
				explosion.motionX = (rand.nextDouble()-0.5D)*rand.nextDouble()*0.4D;
				explosion.motionY = rand.nextDouble()*0.4D;
				explosion.motionZ = (rand.nextDouble()-0.5D)*rand.nextDouble()*0.4D;
				explosion.setPosition(this.posX, this.posY, this.posZ);
				this.worldObj.spawnEntityInWorld(explosion);
			}
		}
	}
}
