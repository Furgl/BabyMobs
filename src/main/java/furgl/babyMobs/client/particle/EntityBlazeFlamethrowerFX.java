package furgl.babyMobs.client.particle;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.util.EntityMover;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

@SideOnly(Side.CLIENT)
public class EntityBlazeFlamethrowerFX extends EntityFX
{
	private boolean spawnedBySpawner;
	private EntitySpawner spawner;
	private int heightIterator;
	private int entityIterator;
	private int maxAge;

	public EntityBlazeFlamethrowerFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
	{
		super(world, x, y, z, motionX, motionY, motionZ);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleTextureIndexX = 0;
		this.particleTextureIndexY = 3;
		this.maxAge = 30;
	}

	/**Used by EntitySpawner*/
	public EntityBlazeFlamethrowerFX(World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		this(world, x, y, z, 0, 0, 0);
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
		this.ticksExisted++;
		if (this.ticksExisted > this.maxAge)
			this.setDead();
		if (this.spawnedBySpawner)
			this.entityIterator = EntityMover.updateMovement(this, this.spawner, this.heightIterator, this.entityIterator);
		else
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
	
	public float getGravityVelocity()
	{
		return 0;
	}
}
