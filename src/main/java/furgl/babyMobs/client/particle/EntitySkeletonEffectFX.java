package furgl.babyMobs.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import furgl.babyMobs.util.EntityMover;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntitySkeletonEffectFX extends EntityFX
{
	private boolean spawnedBySpawner;
	private EntitySpawner spawner;
	private int heightIterator;
	private int entityIterator;
	private int maxAge;
	private Entity entity;

	public EntitySkeletonEffectFX(World world, EntityBabySkeleton entity, float red, float green, float blue)
	{
		this(world, entity.posX, entity.posY+0.7F, entity.posZ, entity.motionX, entity.motionY, entity.motionZ, red, green, blue);
		this.entity = entity;
	}

	public EntitySkeletonEffectFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ, float red, float green, float blue)
	{
		super(world, x, y, z, motionX, motionY, motionZ);
		this.motionX = motionX;
		this.motionY = motionY;
		this.motionZ = motionZ;
		this.particleRed = red;
		this.particleGreen = green;
		this.particleBlue = blue;
		this.particleTextureIndexX = 0;
		this.particleTextureIndexY = 9;
		this.maxAge = 99999999;
		this.particleScale = 1.7F;
	}

	/**Used by EntitySpawner*/
	public EntitySkeletonEffectFX(World world, double x, double y, double z, float red, float green, float blue, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		this(world, x, y, z, 0, 0, 0, red, green, blue);
		this.spawnedBySpawner = true;
		this.spawner = spawner;
		this.heightIterator = heightIterator;
		this.entityIterator = entityIterator;
	}

	@Override
	public void onUpdate()
	{
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;

		if (this.entity != null)
		{
			if (this.entity.isDead)
				this.setDead();
			this.moveEntity(this.entity.motionX, this.entity.motionY, this.entity.motionZ);
			this.setPosition(this.entity.posX, this.entity.posY+this.entity.height+this.height/*this.entity.posY+0.7F*/, this.entity.posZ);
			if (this.ticksExisted % 2 == 0)
				this.particleTextureIndexX = this.rand.nextInt(8);
		}

		this.ticksExisted++;
		if (this.ticksExisted > this.maxAge)
			this.setDead();
		if (this.spawnedBySpawner)
			this.entityIterator = EntityMover.updateMovement(this, this.spawner, this.heightIterator, this.entityIterator);
	}

	public float getGravityVelocity()
	{
		return 0;
	}
}
