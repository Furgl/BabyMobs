package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.util.EntityMover;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityWitherSkeletonSmoke extends EntityThrowable
{
	private boolean spawnedBySpawner;
	private EntitySpawner spawner;
	private int heightIterator;
	private int entityIterator;
	private int maxAge;
	
	public EntityWitherSkeletonSmoke(World world)
	{
		super(world);
		this.noClip = false;
		this.maxAge = 50;
	}

	public EntityWitherSkeletonSmoke(World worldObj, EntityLivingBase entitylivingbase, int particlesPerTick) 
	{
		super(worldObj, entitylivingbase);
		this.noClip = false;
		this.maxAge = 50;
		if (!this.worldObj.isRemote)
			this.setParticlesPerTick(particlesPerTick);
	}

	/**Used by EntitySpawner*/
	public EntityWitherSkeletonSmoke(World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		super(world, x, y, z);
		this.spawnedBySpawner = true;
		this.spawner = spawner;
		this.heightIterator = heightIterator;
		this.entityIterator = entityIterator;
		this.noClip = false;
		this.maxAge = 50;
		if (!this.worldObj.isRemote)
			this.setParticlesPerTick(1);
	}

	@Override
	public void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(20, Integer.valueOf(0));		
	}

	private int getParticlesPerTick()
	{
		return this.dataWatcher.getWatchableObjectInt(20);
	}

	private void setParticlesPerTick(int particlesPerTick)
	{
		this.dataWatcher.updateObject(20, particlesPerTick);;
	}

	@Override
	public void onUpdate()
	{
		if (this.ticksExisted > this.maxAge)
			this.setDead();
		if (this.spawnedBySpawner)
			this.entityIterator = EntityMover.updateMovement(this, this.spawner, this.heightIterator, this.entityIterator);

		if (this.worldObj.isRemote)
		{
			for (int i=0; i<this.getParticlesPerTick(); i++)
				BabyMobs.proxy.spawnEntitySquidInkFX(this);
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!this.worldObj.isRemote)
		{
			if (!player.capabilities.isCreativeMode)
			{
				player.addPotionEffect(new PotionEffect(Potion.blindness.id, 60, 1));
			}
		}
	}

	@Override
	public float getGravityVelocity()
	{
		return 0;
	}

	@Override
	protected void onImpact(MovingObjectPosition mop) {}
}
