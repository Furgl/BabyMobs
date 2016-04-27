package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.util.EntityMover;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBlazeFlamethrower extends EntityThrowable
{
	private boolean spawnedBySpawner;
	private EntitySpawner spawner;
	private int heightIterator;
	private int entityIterator;
	private int maxAge;

	public EntityBlazeFlamethrower(World world)
	{
		super(world);
		this.noClip = true;
		this.maxAge = 20;
	}
	
	public EntityBlazeFlamethrower(World world, EntityLivingBase entitylivingbase) 
	{
		super(world, entitylivingbase);
		this.noClip = true;
		this.maxAge = 20;
	}

	/**Used by EntitySpawner*/
	public EntityBlazeFlamethrower(World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		super(world, x, y, z);
		this.spawnedBySpawner = true;
		this.spawner = spawner;
		this.heightIterator = heightIterator;
		this.entityIterator = entityIterator;
		this.noClip = true;
		this.maxAge = 20;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.ticksExisted > this.maxAge)
			this.setDead();
		if (this.spawnedBySpawner)
			this.entityIterator = EntityMover.updateMovement(this, this.spawner, this.heightIterator, this.entityIterator);
	}

	@Override
	protected void onImpact(MovingObjectPosition mop)
	{
		if (!this.worldObj.isRemote)
		{
			if (mop.entityHit instanceof EntityPlayer && !((EntityPlayer)mop.entityHit).isImmuneToFire() && ((EntityPlayer)mop.entityHit).attackEntityFrom(DamageSource.onFire, 1.0F))
			{
				((EntityPlayer)mop.entityHit).triggerAchievement(Achievements.achievementItsGettingHotInHere);
				((EntityPlayer)mop.entityHit).setFire(3);
			}
		}
	}
	
	@Override
	public float getGravityVelocity()
	{
		return 0;
	}
}
