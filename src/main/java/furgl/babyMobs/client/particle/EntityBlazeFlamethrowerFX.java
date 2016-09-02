package furgl.babyMobs.client.particle;

import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntityBlazeFlamethrowerFX extends EntityCustomParticle
{
	public EntityBlazeFlamethrowerFX(World world, double x, double y, double z, double motionX, double motionY, double motionZ)
	{
		super(world, x, y, z, motionX, motionY, motionZ);
		this.particleRed = this.particleGreen = this.particleBlue = 1.0F;
		this.particleTextureIndexX = 0;
		this.particleTextureIndexY = 3;
		this.maxAge = 10;
		this.particleMaxAge = 10;
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
}
