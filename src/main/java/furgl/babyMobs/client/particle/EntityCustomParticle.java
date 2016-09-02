package furgl.babyMobs.client.particle;

import furgl.babyMobs.util.EntityMover;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.client.particle.Particle;
import net.minecraft.world.World;

public class EntityCustomParticle extends Particle
{
	protected boolean spawnedBySpawner;
	protected EntitySpawner spawner;
	protected int heightIterator;
	protected int entityIterator;
	protected int maxAge;
	public int ticksExisted;

	protected EntityCustomParticle(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
		super(world, posX, posY, posZ, motionX, motionY, motionZ);
		
		this.ticksExisted = 0;
	}
	
	@Override
	public void onUpdate() {
		this.prevPosX = this.posX;
		this.prevPosY = this.posY;
		this.prevPosZ = this.posZ;
		this.ticksExisted++;
		if (this.ticksExisted > this.particleMaxAge)
			this.setExpired();
		
		if (this.spawnedBySpawner)
			this.entityIterator = EntityMover.updateMovement(this, this.spawner, this.heightIterator, this.entityIterator);
		else
			this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
	
	public void setMotionX(double speed) {
		motionX = speed;
	}
	
	public void setMotionY(double speed) {
		motionY = speed;
	}
	
	public void setMotionZ(double speed) {
		motionZ = speed;
	}
	
	public double getMotionX() {
		return motionX;
	}
	
	public double getMotionY() {
		return motionY;
	}
	
	public double getMotionZ() {
		return motionZ;
	}
	
	public double getX() {
		return posX;
	}
	
	public double getY() {
		return posY;
	}
	
	public double getZ() {
		return posZ;
	}
}
