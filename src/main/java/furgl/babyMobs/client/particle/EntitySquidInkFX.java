package furgl.babyMobs.client.particle;

import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntitySquidInkFX extends EntityCustomParticle
{

	public EntitySquidInkFX(World world, double x, double y, double z, double motionX, double motionY, double zSpeed)
	{
		super(world, x, y, z, motionX, motionY, zSpeed);
		this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D);
		this.motionX *= 0.10000000149011612D;
        this.motionY *= 0.10000000149011612D;
        this.motionZ *= 0.10000000149011612D;
        this.motionX += motionX;
        this.motionY += motionY;
        this.motionZ += zSpeed;
		this.particleScale *= 0.75F;
		//this.noClip = false;
		this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		this.particleMaxAge = this.maxAge;
	}

	/**Used by EntitySpawner*/
	public EntitySquidInkFX(World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator)
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
		super.onUpdate();

		//EntitySmokeFX
		this.setParticleTextureIndex(7 - this.ticksExisted * 8 / this.maxAge);

		if (this.posY == this.prevPosY)
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
		}
	}
}
