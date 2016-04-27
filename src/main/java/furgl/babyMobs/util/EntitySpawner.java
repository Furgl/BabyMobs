package furgl.babyMobs.util;

import java.lang.reflect.InvocationTargetException;
import java.util.Random;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.event.OnUpdateEvent;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntitySpawner
{
	//Class variables
	private boolean isRunning = false;
	private int totalDelay;
	private Class entityClass;
	private Entity entity;
	//Essential variables - always specified via constructor
	protected World world;
	protected Vec3 origin;
	protected int numEntities;
	protected double x;
	protected double y;
	protected double z;
	protected Random rand = new Random();

	//Shape variables - always specified via setShape<Shape>() methods
	protected String shape = "none";
	protected double radius;
	protected double length; 
	protected int entityIterator;
	protected int heightIterator;
	protected float pitch;
	protected float yaw;

	//Movement variables - always specified via setMovement<Movement>() methods
	protected String movement = "none";
	protected double movementSpeed;
	protected Entity target;
	protected boolean homing;
	
	//Default variables - have setters
	protected int delay = 0;	
	protected double randVar = 0.0D;
	protected int numRuns = 1;
	protected int consecRuns = 1;
	protected int updateTime = 10;

	/**Spawns custom Entity's or EntityFX's with custom shape/movement*/
	public EntitySpawner(Class entityClass, World world, Vec3 origin, int numEntities)
	{
		this.entityClass = entityClass;
		this.world = world;
		this.origin = origin;
		this.numEntities = numEntities;
		this.x = origin.xCoord;
		this.y = origin.yCoord;
		this.z = origin.zCoord;
	}

	/**Can have movements: "followShape" or "in/out"*/
	public void setShapeCircle(double radius)
	{
		this.radius = radius;
		this.shape = "circle";
	}

	/**Can have movements: "followShape"  or "in/out"*/
	public void setShapeSphere(double radius)
	{
		this.radius = radius;
		this.shape = "sphere";
	}

	/**Can have movements: "followShape"*/
	public void setShapeLine(double length, float pitch, float yaw)
	{
		this.length = length;
		this.pitch = pitch;
		this.yaw = yaw;
		this.shape = "line";
	}
	
	 /**Particle moves with its shape*/
	public void setMovementFollowShape(double movementSpeed)
	{
		this.movementSpeed = movementSpeed;
		this.movement = "followShape";
	}

	  /**Particle moves in/out of its shape (can't be used with line)
	  * 
	  * @param movementSpeed Positive = in, negative = out*/
	public void setMovementInOut(double movementSpeed)
	{
		this.movementSpeed = movementSpeed;
		this.movement = "in/out";
	}

	   /**Particle moves in random direction*/
	public void setMovementRandom(double movementSpeed)
	{
		this.movementSpeed = movementSpeed;
		this.movement = "random";
	}

	    /**Particle targets entity*/
	public void setMovementTarget(double movementSpeed, Entity target, boolean homing)
	{
		this.movementSpeed = movementSpeed;
		this.target = target;
		this.homing = homing;
		this.movement = "target";
	}

	/**In ticks*/
	public void setDelay(int delay)
	{
		this.delay = delay;
	}

	/**Random variation in spawning location of particles*/
	public void setRandVar(double randVar) 
	{
		this.randVar = randVar;
	}

	/**Number of runs AT THE SAME TIME*/
	public void setNumRuns(int numRuns)
	{
		this.numRuns = numRuns;
	}

	/**Number of CONSECUTIVE runs*/
	public void setConsecRuns(int consecRuns)
	{
		this.consecRuns = consecRuns;
	}

	 /**Ticks between each update - decrease for faster movement/smaller circles and spheres*/
	public void setUpdateTick(int updateTime)
	{
		this.updateTime = updateTime;
	}

	/**Starts particle action after all parameters are specified*/
	public void run()
	{	
		if (this.entityClass.getSuperclass() == BabyMobs.proxy.getEntityFXClass() && this.world.isRemote)
			OnUpdateEvent.addOnClientUpdate(this);
		else if (this.entityClass.getSuperclass() != BabyMobs.proxy.getEntityFXClass() && !this.world.isRemote)
			OnUpdateEvent.addOnServerUpdate(this);
		this.isRunning = true;
		this.totalDelay = this.delay * this.numEntities * this.consecRuns;
		this.onUpdate();
	}

	/**Called once per tick*/
	public void onUpdate()
	{
		if (isRunning)
		{
			for (int runIterator=0; runIterator < numRuns; runIterator++)
			{
				for (int entityIterator=0; entityIterator < numEntities * this.consecRuns; entityIterator++)
				{
					if (totalDelay - delay * entityIterator == 0)
					{
						switch(this.shape)
						{
						case "circle":
							x = radius * MathHelper.cos((float) (2*Math.PI*entityIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.xCoord;
							y = ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.yCoord;
							z = radius * MathHelper.sin((float) (2*Math.PI*entityIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.zCoord;	
							break;
						case "sphere":
							for (int heightIterator=0; heightIterator < numEntities; heightIterator++)
							{
								x = MathHelper.cos((float) (2*Math.PI*heightIterator/numEntities)) * radius * MathHelper.cos((float) (2*Math.PI*entityIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.xCoord;
								y = radius * MathHelper.sin((float) (2*Math.PI*heightIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.yCoord;
								z = MathHelper.cos((float) (2*Math.PI*heightIterator/numEntities)) * radius * MathHelper.sin((float) (2*Math.PI*entityIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.zCoord;	
								this.spawnEntity(heightIterator, entityIterator);
							}
							break;
						case "line":
							int entityIterator1 = entityIterator;
							while (entityIterator1 >= numEntities)
								entityIterator1 -= numEntities;
							entityIterator1 = Math.abs(numEntities - entityIterator1);
							x = origin.xCoord + (((((double) rand.nextInt(20) - 10) / 10) * randVar) * entityIterator1/5) + (length * entityIterator1 / numEntities) * Math.cos(yaw*Math.PI/180 + Math.PI/2);
							y = origin.yCoord + (((((double) rand.nextInt(20) - 10) / 10) * randVar) * entityIterator1/5) - (length * entityIterator1 / numEntities) * Math.sin(pitch*Math.PI/180);
							z = origin.zCoord + (((((double) rand.nextInt(20) - 10) / 10) * randVar) * entityIterator1/5) + (length * entityIterator1 / numEntities) * Math.sin(yaw*Math.PI/180 + Math.PI/2);
							break;
						default:
							x = origin.xCoord;
							y = origin.yCoord;
							z = origin.zCoord;
							break;
						}
						if (!this.shape.equals("sphere"))
							this.spawnEntity(heightIterator, entityIterator);
					}
				}
			}
			totalDelay--;
			if (totalDelay < 0)
			{
				isRunning = false;
				if (this.entityClass.getSuperclass() == BabyMobs.proxy.getEntityFXClass() && this.world.isRemote)
					OnUpdateEvent.removeOnClientUpdate(this);
				else if (this.entityClass.getSuperclass() != BabyMobs.proxy.getEntityFXClass() && !this.world.isRemote)
					OnUpdateEvent.removeOnServerUpdate(this);
			}
		}
	}

	public void spawnEntity(int heightIterator, int entityIterator)
	{
		this.heightIterator = heightIterator;
		this.entityIterator = entityIterator;
		if (this.entityClass.getSuperclass() == BabyMobs.proxy.getEntityFXClass() && this.world.isRemote)
		{
			BabyMobs.proxy.spawnEntitySpawner(this.entityClass, this.world, this.x, this.y, this.z, this, this.heightIterator, this.entityIterator);
		}
		else if (this.entityClass.getSuperclass() != BabyMobs.proxy.getEntityFXClass() && !this.world.isRemote)
		{
			try 
			{
				entity = (Entity) this.entityClass.getConstructor(World.class, double.class, double.class, double.class, EntitySpawner.class, int.class, int.class).newInstance(this.world, this.x, this.y, this.z, this, this.heightIterator, this.entityIterator);
				entity.setPosition(x, y, z);
				world.spawnEntityInWorld(entity);
			} 
			catch (InstantiationException | IllegalAccessException | IllegalArgumentException| InvocationTargetException | NoSuchMethodException | SecurityException e) 
			{
				System.out.println("ERROR: ENTITY " + this.entityClass + " MISSING SPAWNER CONSTRUCTOR");
				e.printStackTrace();
			}
		}
	}

}
