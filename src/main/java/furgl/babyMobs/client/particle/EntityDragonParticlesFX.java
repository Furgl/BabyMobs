package furgl.babyMobs.client.particle;

import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityDragonParticlesFX extends EntityCustomFX
{
	public EntityDragonParticlesFX(World world, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed)
	{
		super(world, x, y, z, xSpeed, ySpeed, zSpeed);
		float f = this.rand.nextFloat() * 0.6F + 0.4F;
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F * f;
        this.particleGreen *= 0.3F;
        this.particleRed *= 0.9F;
		this.xSpeed *= 0.10000000149011612D;
		this.ySpeed *= 0.10000000149011612D;
		this.zSpeed *= 0.10000000149011612D;
		this.xSpeed += xSpeed;
		this.ySpeed += ySpeed;
		this.zSpeed += zSpeed;
		this.particleScale *= 0.75F;
		//this.noClip = true;
		this.maxAge = 40;
		this.particleMaxAge = this.maxAge;
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
		super.onUpdate();

		this.setParticleTextureIndex(7 - this.ticksExisted * 8 / this.maxAge);

		this.ySpeed = 0.05D;
	}
}
