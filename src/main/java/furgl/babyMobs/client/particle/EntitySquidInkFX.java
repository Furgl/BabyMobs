package furgl.babyMobs.client.particle;

import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntitySquidInkFX extends EntityCustomFX
{

	public EntitySquidInkFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		this.particleRed = this.particleGreen = this.particleBlue = (float)(Math.random() * 0.30000001192092896D);
		this.xSpeed *= 0.10000000149011612D;
        this.ySpeed *= 0.10000000149011612D;
        this.zSpeed *= 0.10000000149011612D;
        this.xSpeed += xSpeed;
        this.ySpeed += ySpeed;
        this.zSpeed += zSpeed;
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
			this.xSpeed *= 1.1D;
			this.zSpeed *= 1.1D;
		}

		this.xSpeed *= 0.9599999785423279D;
		this.ySpeed *= 0.9599999785423279D;
		this.zSpeed *= 0.9599999785423279D;

		if (this.isCollided) //changed from onGround
		{
			this.xSpeed *= 0.699999988079071D;
			this.zSpeed *= 0.699999988079071D;
		}
	}
}
