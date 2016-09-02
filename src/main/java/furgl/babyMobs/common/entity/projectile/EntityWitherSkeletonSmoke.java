package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.util.EntityMover;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.MobEffects;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWitherSkeletonSmoke extends EntityThrowable
{
    private static final DataParameter<Integer> PARTICLES_PER_TICK = EntityDataManager.<Integer>createKey(EntityTippedArrow.class, DataSerializers.VARINT);
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
		this.dataManager.register(PARTICLES_PER_TICK, Integer.valueOf(0));		
	}

	private int getParticlesPerTick()
	{
		return this.dataManager.get(PARTICLES_PER_TICK);
	}

	private void setParticlesPerTick(int particlesPerTick)
	{
		this.dataManager.set(PARTICLES_PER_TICK, particlesPerTick);;
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
				player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60, 1));
			}
		}
	}

	@Override
	public float getGravityVelocity()
	{
		return 0;
	}

	@Override
	protected void onImpact(RayTraceResult mop) {}
}
