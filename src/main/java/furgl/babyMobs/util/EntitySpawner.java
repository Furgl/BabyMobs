package furgl.babyMobs.util;

import java.util.Random;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.event.OnUpdateEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntitySpawner
{
	//Class variables
	private boolean isRunning = false;
	private int totalDelay;
	private Class entityClass;
	private Entity entity;
	private Particle entityFX;
	//Essential variables - always specified via constructor
	protected World world;
	protected Vec3d origin;
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

	/**Spawns custom Entity's or Particle's with custom shape/movement*/
	public EntitySpawner(Class entityClass, World world, Vec3d origin, int numEntities)
	{
		this.entityClass = entityClass;
		this.world = world;
		this.origin = origin;
		this.numEntities = numEntities;
		this.x = origin.x;
		this.y = origin.y;
		this.z = origin.z;
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
		if (this.entityClass.getSuperclass() == BabyMobs.proxy.getParticleClass() && this.world.isRemote)
			OnUpdateEvent.addOnClientUpdate(this);
		else if (this.entityClass.getSuperclass() != BabyMobs.proxy.getParticleClass() && !this.world.isRemote)
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
							x = radius * MathHelper.cos((float) (2*Math.PI*entityIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.x;
							y = ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.y;
							z = radius * MathHelper.sin((float) (2*Math.PI*entityIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.z;	
							break;
						case "sphere":
							for (int heightIterator=0; heightIterator < numEntities; heightIterator++)
							{
								x = MathHelper.cos((float) (2*Math.PI*heightIterator/numEntities)) * radius * MathHelper.cos((float) (2*Math.PI*entityIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.x;
								y = radius * MathHelper.sin((float) (2*Math.PI*heightIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.y;
								z = MathHelper.cos((float) (2*Math.PI*heightIterator/numEntities)) * radius * MathHelper.sin((float) (2*Math.PI*entityIterator/numEntities)) + ((((double) rand.nextInt(20) - 10) / 10) * randVar) + origin.z;	
								this.spawnEntity(heightIterator, entityIterator);
							}
							break;
						case "line":
							int entityIterator1 = entityIterator;
							while (entityIterator1 >= numEntities)
								entityIterator1 -= numEntities;
							entityIterator1 = Math.abs(numEntities - entityIterator1);
							x = origin.x + (((((double) rand.nextInt(20) - 10) / 10) * randVar) * entityIterator1/5) + (length * entityIterator1 / numEntities) * Math.cos(yaw*Math.PI/180 + Math.PI/2);
							y = origin.y + (((((double) rand.nextInt(20) - 10) / 10) * randVar) * entityIterator1/5) - (length * entityIterator1 / numEntities) * Math.sin(pitch*Math.PI/180);
							z = origin.z + (((((double) rand.nextInt(20) - 10) / 10) * randVar) * entityIterator1/5) + (length * entityIterator1 / numEntities) * Math.sin(yaw*Math.PI/180 + Math.PI/2);
							break;
						default:
							x = origin.x;
							y = origin.y;
							z = origin.z;
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
				if (this.entityClass.getSuperclass() == BabyMobs.proxy.getParticleClass() && this.world.isRemote)
					OnUpdateEvent.removeOnClientUpdate(this);
				else if (this.entityClass.getSuperclass() != BabyMobs.proxy.getParticleClass() && !this.world.isRemote)
					OnUpdateEvent.removeOnServerUpdate(this);
			}
		}
	}

	public void spawnEntity(int heightIterator, int entityIterator)
	{
		this.heightIterator = heightIterator;
		this.entityIterator = entityIterator;
		if (this.entityClass.getSuperclass() == BabyMobs.proxy.getParticleClass() && this.world.isRemote)
		{
			BabyMobs.proxy.spawnEntitySpawner(this.entityClass, this.world, this.x, this.y, this.z, this, this.heightIterator, this.entityIterator);
		}
		else if (this.entityClass.getSuperclass() != BabyMobs.proxy.getParticleClass() && !this.world.isRemote)
		{
			try 
			{
				Object object = this.entityClass.getConstructor(World.class, double.class, double.class, double.class, EntitySpawner.class, int.class, int.class).newInstance(this.world, this.x, this.y, this.z, this, this.heightIterator, this.entityIterator);
				if (object instanceof Entity) {
				entity = (Entity) object;
				entity.setPosition(x, y, z);
				world.spawnEntity(entity);
				}
				else {
					entityFX = (Particle) object;
					entityFX.setPosition(x, y, z);
					Minecraft.getMinecraft().effectRenderer.addEffect(entityFX);
				}
			} 
			catch (Exception e) 
			{
				System.out.println("ERROR: ENTITY " + this.entityClass + " MISSING SPAWNER CONSTRUCTOR");
				e.printStackTrace();
			}
		}
	}

}
