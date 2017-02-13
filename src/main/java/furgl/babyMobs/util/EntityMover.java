package furgl.babyMobs.util;

import furgl.babyMobs.client.particle.EntityCustomParticle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;

/**Moves Entity's or Particle's with custom movement.
 * Requires the Entity or Particle to have been spawned with EntitySpawner*/
public class EntityMover 
{
	/**Moves Entity or Particle with custom movement*/
	public static int updateMovement(Entity entity, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		switch(spawner.movement)
		{
		case "followShape":
			switch(spawner.shape)
			{
			case "circle":
				if (entity.ticksExisted % spawner.updateTime == 0 || entity.ticksExisted == 1)
				{
					entityIterator++;
					entity.motionX = (spawner.radius * Math.cos(2*Math.PI*(entityIterator)/Math.max(spawner.numEntities, 10)) + spawner.origin.xCoord - entity.posX) * 0.3D;
					entity.motionY = 0.0D;  
					entity.motionZ = (spawner.radius * Math.sin(2*Math.PI*(entityIterator)/Math.max(spawner.numEntities, 10)) + spawner.origin.zCoord - entity.posZ) * 0.3D;
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			case "sphere":
				if (entity.ticksExisted % spawner.updateTime == 0 || entity.ticksExisted == 1)
				{
					entityIterator++;
					entity.motionX = ((Math.cos((float) (2*Math.PI*heightIterator/Math.max(spawner.numEntities, 10))) * spawner.radius * Math.cos(2*Math.PI*(entityIterator)/spawner.numEntities) + spawner.origin.xCoord) - entity.posX) * 0.3D;
					entity.motionY = 0.0F;
					entity.motionZ = ((Math.cos((float) (2*Math.PI*heightIterator/Math.max(spawner.numEntities, 10))) * spawner.radius * Math.sin(2*Math.PI*(entityIterator)/spawner.numEntities) + spawner.origin.zCoord) - entity.posZ) * 0.3D;
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			case "line": 
				if (entity.ticksExisted == 1)
				{
					entity.motionX = - spawner.length * Math.sin(Math.PI * spawner.yaw / 180)   / 200;
					entity.motionY = - spawner.length * Math.sin(Math.PI * spawner.pitch / 180) / 200;
					entity.motionZ =   spawner.length * Math.cos(Math.PI * spawner.yaw / 180)   / 200;
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			}
			break;
		case "in/out":
			if (entity.ticksExisted == 1)
			{
				entity.motionX = (spawner.origin.xCoord - entity.posX) / 20;
				entity.motionY = (spawner.origin.yCoord - entity.posY) / 20;
				entity.motionZ = (spawner.origin.zCoord - entity.posZ) / 20;
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		case "random":
			if (entity.ticksExisted == 1)
			{
				entity.motionX = spawner.rand.nextDouble()-0.5D;
				entity.motionY = spawner.rand.nextDouble()-0.5D;
				entity.motionZ = spawner.rand.nextDouble()-0.5D;
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		case "target":
			if (spawner.homing || entity.ticksExisted == 1)
			{
				entity.motionX = (spawner.target.posX - entity.posX) / 20;
				entity.motionY = ((spawner.target.posY + spawner.target.height/2) - entity.posY) / 20;
				entity.motionZ = (spawner.target.posZ- entity.posZ) / 20;
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		}
		entity.move(MoverType.SELF, entity.motionX, entity.motionY, entity.motionZ);	
		return entityIterator;
	}
	
	/**Normalizes speed according to EntitySpawner.movementSpeed*/
	private static void updateEntity(Entity entity, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		double norm = Math.sqrt(Math.pow(entity.motionX, 2) + Math.pow(entity.motionY, 2) + Math.pow(entity.motionZ, 2));
		if (spawner.shape.equals("circle") || spawner.shape.equals("sphere"))
		{
			entity.motionX *= spawner.movementSpeed;
			entity.motionY *= spawner.movementSpeed;
			entity.motionZ *= spawner.movementSpeed;
		}
		else
		{
			entity.motionX *= spawner.movementSpeed / norm;
			entity.motionY *= spawner.movementSpeed / norm;
			entity.motionZ *= spawner.movementSpeed / norm;
		}
	}
	
	/**Moves Entity or Particle with custom movement*/
	public static int updateMovement(EntityCustomParticle entity, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		switch(spawner.movement)
		{
		case "followShape":
			switch(spawner.shape)
			{
			case "circle":
				if (entity.ticksExisted % spawner.updateTime == 0 || entity.ticksExisted == 1)
				{
					entityIterator++;
					entity.setMotionX((spawner.radius * Math.cos(2*Math.PI*(entityIterator)/Math.max(spawner.numEntities, 10)) + spawner.origin.xCoord - entity.getX()) * 0.3D);
					entity.setMotionY(0.0D);  
					entity.setMotionZ((spawner.radius * Math.sin(2*Math.PI*(entityIterator)/Math.max(spawner.numEntities, 10)) + spawner.origin.zCoord - entity.getZ()) * 0.3D);
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			case "sphere":
				if (entity.ticksExisted % spawner.updateTime == 0 || entity.ticksExisted == 1)
				{
					entityIterator++;
					entity.setMotionX(((Math.cos((float) (2*Math.PI*heightIterator/Math.max(spawner.numEntities, 10))) * spawner.radius * Math.cos(2*Math.PI*(entityIterator)/spawner.numEntities) + spawner.origin.xCoord) - entity.getX()) * 0.3D);
					entity.setMotionY(0.0F);
					entity.setMotionZ(((Math.cos((float) (2*Math.PI*heightIterator/Math.max(spawner.numEntities, 10))) * spawner.radius * Math.sin(2*Math.PI*(entityIterator)/spawner.numEntities) + spawner.origin.zCoord) - entity.getZ()) * 0.3D);
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			case "line": 
				if (entity.ticksExisted == 1)
				{
					entity.setMotionX(- spawner.length * Math.sin(Math.PI * spawner.yaw / 180)   / 200);
					entity.setMotionY(- spawner.length * Math.sin(Math.PI * spawner.pitch / 180) / 200);
					entity.setMotionZ(  spawner.length * Math.cos(Math.PI * spawner.yaw / 180)   / 200);
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			}
			break;
		case "in/out":
			if (entity.ticksExisted == 1)
			{
				entity.setMotionX((spawner.origin.xCoord - entity.getX()) / 20);
				entity.setMotionY((spawner.origin.yCoord - entity.getY()) / 20);
				entity.setMotionZ((spawner.origin.zCoord - entity.getZ()) / 20);
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		case "random":
			if (entity.ticksExisted == 1)
			{
				entity.setMotionX(spawner.rand.nextDouble()-0.5D);
				entity.setMotionY(spawner.rand.nextDouble()-0.5D);
				entity.setMotionZ(spawner.rand.nextDouble()-0.5D);
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		case "target":
			if (spawner.homing || entity.ticksExisted == 1)
			{
				entity.setMotionX((spawner.target.posX - entity.getX()) / 20);
				entity.setMotionY(((spawner.target.posY + spawner.target.height/2) - entity.getY()) / 20);
				entity.setMotionZ((spawner.target.posZ - entity.getZ()) / 20);
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		}
		entity.move(entity.getMotionX(), entity.getMotionY(), entity.getMotionZ());	
		return entityIterator;
	}
	
	/**Normalizes speed according to EntitySpawner.movementSpeed*/
	private static void updateEntity(EntityCustomParticle entity, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		double norm = Math.sqrt(Math.pow(entity.getMotionX(), 2) + Math.pow(entity.getMotionY(), 2) + Math.pow(entity.getMotionZ(), 2));
		if (spawner.shape.equals("circle") || spawner.shape.equals("sphere"))
		{
			entity.setMotionX(entity.getMotionX() * spawner.movementSpeed);
			entity.setMotionY(entity.getMotionY() * spawner.movementSpeed);
			entity.setMotionZ(entity.getMotionZ() * spawner.movementSpeed);
		}
		else
		{
			entity.setMotionX(entity.getMotionX() * spawner.movementSpeed / norm);
			entity.setMotionY(entity.getMotionY() * spawner.movementSpeed / norm);
			entity.setMotionZ(entity.getMotionZ() * spawner.movementSpeed / norm);
		}
	}
}
