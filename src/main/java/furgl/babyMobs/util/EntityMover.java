package furgl.babyMobs.util;

import furgl.babyMobs.client.particle.EntityCustomFX;
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
	
	/**Moves Entity or EntityFX with custom movement*/
	public static int updateMovement(EntityCustomFX entity, EntitySpawner spawner, int heightIterator, int entityIterator)
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
					entity.setXSpeed((spawner.radius * Math.cos(2*Math.PI*(entityIterator)/Math.max(spawner.numEntities, 10)) + spawner.origin.xCoord - entity.getX()) * 0.3D);
					entity.setYSpeed(0.0D);  
					entity.setZSpeed((spawner.radius * Math.sin(2*Math.PI*(entityIterator)/Math.max(spawner.numEntities, 10)) + spawner.origin.zCoord - entity.getZ()) * 0.3D);
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			case "sphere":
				if (entity.ticksExisted % spawner.updateTime == 0 || entity.ticksExisted == 1)
				{
					entityIterator++;
					entity.setXSpeed(((Math.cos((float) (2*Math.PI*heightIterator/Math.max(spawner.numEntities, 10))) * spawner.radius * Math.cos(2*Math.PI*(entityIterator)/spawner.numEntities) + spawner.origin.xCoord) - entity.getX()) * 0.3D);
					entity.setYSpeed(0.0F);
					entity.setZSpeed(((Math.cos((float) (2*Math.PI*heightIterator/Math.max(spawner.numEntities, 10))) * spawner.radius * Math.sin(2*Math.PI*(entityIterator)/spawner.numEntities) + spawner.origin.zCoord) - entity.getZ()) * 0.3D);
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			case "line": 
				if (entity.ticksExisted == 1)
				{
					entity.setXSpeed(- spawner.length * Math.sin(Math.PI * spawner.yaw / 180)   / 200);
					entity.setYSpeed(- spawner.length * Math.sin(Math.PI * spawner.pitch / 180) / 200);
					entity.setZSpeed(  spawner.length * Math.cos(Math.PI * spawner.yaw / 180)   / 200);
					EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
				}
				break;
			}
			break;
		case "in/out":
			if (entity.ticksExisted == 1)
			{
				entity.setXSpeed((spawner.origin.xCoord - entity.getX()) / 20);
				entity.setYSpeed((spawner.origin.yCoord - entity.getY()) / 20);
				entity.setZSpeed((spawner.origin.zCoord - entity.getZ()) / 20);
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		case "random":
			if (entity.ticksExisted == 1)
			{
				entity.setXSpeed(spawner.rand.nextDouble()-0.5D);
				entity.setYSpeed(spawner.rand.nextDouble()-0.5D);
				entity.setZSpeed(spawner.rand.nextDouble()-0.5D);
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		case "target":
			if (spawner.homing || entity.ticksExisted == 1)
			{
				entity.setXSpeed((spawner.target.posX - entity.getX()) / 20);
				entity.setYSpeed(((spawner.target.posY + spawner.target.height/2) - entity.getY()) / 20);
				entity.setZSpeed((spawner.target.posZ - entity.getZ()) / 20);
				EntityMover.updateEntity(entity, spawner, heightIterator, entityIterator);
			}
			break;
		}
		entity.moveEntity(entity.getXSpeed(), entity.getYSpeed(), entity.getZSpeed());	
		return entityIterator;
	}
	
	/**Normalizes speed according to EntitySpawner.movementSpeed*/
	private static void updateEntity(EntityCustomFX entity, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		double norm = Math.sqrt(Math.pow(entity.getXSpeed(), 2) + Math.pow(entity.getYSpeed(), 2) + Math.pow(entity.getZSpeed(), 2));
		if (spawner.shape.equals("circle") || spawner.shape.equals("sphere"))
		{
			entity.setXSpeed(entity.getXSpeed() * spawner.movementSpeed);
			entity.setYSpeed(entity.getYSpeed() * spawner.movementSpeed);
			entity.setZSpeed(entity.getZSpeed() * spawner.movementSpeed);
		}
		else
		{
			entity.setXSpeed(entity.getXSpeed() * spawner.movementSpeed / norm);
			entity.setYSpeed(entity.getYSpeed() * spawner.movementSpeed / norm);
			entity.setZSpeed(entity.getZSpeed() * spawner.movementSpeed / norm);
		}
	}
}
