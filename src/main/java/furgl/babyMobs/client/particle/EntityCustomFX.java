package furgl.babyMobs.client.particle;

import furgl.babyMobs.util.EntityMover;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.world.World;

public class EntityCustomFX extends EntityFX
{
	protected boolean spawnedBySpawner;
	protected EntitySpawner spawner;
	protected int heightIterator;
	protected int entityIterator;
	protected int maxAge;
	public int ticksExisted;

	protected EntityCustomFX(World world, double posX, double posY, double posZ, double motionX, double motionY, double motionZ) {
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
			this.moveEntity(this.xSpeed, this.ySpeed, this.zSpeed);
	}
	
	public void setXSpeed(double speed) {
		xSpeed = speed;
	}
	
	public void setYSpeed(double speed) {
		ySpeed = speed;
	}
	
	public void setZSpeed(double speed) {
		zSpeed = speed;
	}
	
	public double getXSpeed() {
		return xSpeed;
	}
	
	public double getYSpeed() {
		return ySpeed;
	}
	
	public double getZSpeed() {
		return zSpeed;
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
