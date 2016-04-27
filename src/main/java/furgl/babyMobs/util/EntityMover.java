package furgl.babyMobs.util;

import net.minecraft.entity.Entity;

/**Moves Entity's or EntityFX's with custom movement.
 * Requires the Entity or EntityFX to have been spawned with EntitySpawner*/
public class EntityMover 
{
	/**Moves Entity or EntityFX with custom movement*/
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
		entity.moveEntity(entity.motionX, entity.motionY, entity.motionZ);	
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
}
