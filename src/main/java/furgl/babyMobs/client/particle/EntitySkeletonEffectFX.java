package furgl.babyMobs.client.particle;

import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class EntitySkeletonEffectFX extends EntityCustomParticle
{
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
		this.particleMaxAge = this.maxAge;
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
		super.onUpdate();

		if (this.entity != null)
		{
			if (this.entity.isDead)
				this.setExpired();
			this.move(this.entity.motionX, this.entity.motionY, this.entity.motionZ);
			this.setPosition(this.entity.posX, this.entity.posY+this.entity.height+this.height, this.entity.posZ);
			if (this.ticksExisted % 2 == 0)
				this.particleTextureIndexX = this.rand.nextInt(8);
		}
	}
}
