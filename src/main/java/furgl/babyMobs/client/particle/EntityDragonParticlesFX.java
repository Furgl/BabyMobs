package furgl.babyMobs.client.particle;

import furgl.babyMobs.util.EntityMover;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityDragonParticlesFX extends EntityFX
{
	private boolean spawnedBySpawner;
	private EntitySpawner spawner;
	private int heightIterator;
	private int entityIterator;
	private int maxAge;

	public EntityDragonParticlesFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
	{
		super(world, x, y, z, motionX, motionY, motionZ);
		float f = this.rand.nextFloat() * 0.6F + 0.4F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f;
        this.particleGreen *= 0.3F;
        this.particleRed *= 0.9F;
		this.motionX *= 0.10000000149011612D;
		this.motionY *= 0.10000000149011612D;
		this.motionZ *= 0.10000000149011612D;
		this.motionX += motionX;
		this.motionY += motionY;
		this.motionZ += motionZ;
		this.particleScale *= 0.75F;
		this.noClip = true;
		this.maxAge = 40;
	}

	/**Used by EntitySpawner*/
	public EntityDragonParticlesFX(World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator)
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

		//EntitySmokeFX
		this.setParticleTextureIndex(7 - this.ticksExisted * 8 / this.maxAge);

		this.motionY = 0.05D;
		/*if (this.posY == this.prevPosY)
		{
			this.motionX *= 1.1D;
			this.motionZ *= 1.1D;
		}

		this.motionX *= 0.9599999785423279D;
		this.motionY *= 0.9599999785423279D;
		this.motionZ *= 0.9599999785423279D;

		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}*/
	}
}
