package furgl.babyMobs.common.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;

import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.Vec3;

public class EntityAIBabyAvoidEntity extends EntityAIBase
{
	public final IEntitySelector field_98218_a = new IEntitySelector()
	{
		/**
		 * Return whether the specified entity is applicable to this filter.
		 */
		@Override
		public boolean isEntityApplicable(Entity p_82704_1_)
		{
			return p_82704_1_.isEntityAlive() && EntityAIBabyAvoidEntity.this.theEntity.getEntitySenses().canSee(p_82704_1_);
		}
	};
	/** The entity we are attached to */
	protected EntityCreature theEntity;
	private double farSpeed;
	private double nearSpeed;
	protected Entity closestLivingEntity;
	private float field_179508_f;
	/** The PathEntity of our entity */
	private PathEntity entityPathEntity;
	/** The PathNavigate of our entity */
	private PathNavigate entityPathNavigate;
	
	public EntityAIBabyAvoidEntity(EntityCreature entity, Predicate predicate2, float fieldf, double FarSpeed, double NearSpeed)
	{
		this.theEntity = entity;
		this.field_179508_f = fieldf;
		this.farSpeed = FarSpeed;
		this.nearSpeed = NearSpeed;
		this.entityPathNavigate = entity.getNavigator();
		this.setMutexBits(1);
	}

	/**
	 * Returns whether the EntityAIBase should begin execution.
	 */
	@Override
	public boolean shouldExecute()
	{
		List list = this.theEntity.worldObj.selectEntitiesWithinAABB(this.theEntity.getClass(), this.theEntity.boundingBox.expand(this.field_179508_f, 3.0D, this.field_179508_f), this.field_98218_a);

		if (list.isEmpty())
		{
			return false;
		}
		else
		{
			this.closestLivingEntity = (Entity)list.get(0);
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, Vec3.createVectorHelper(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

			if (vec3 == null)
			{
				return false;
			}
			while (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity))
			{
				vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, Vec3.createVectorHelper(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
			}
			this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
			return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(vec3);
		}
	}

	/**
	 * Returns whether an in-progress EntityAIBase should continue executing
	 */
	@Override
	public boolean continueExecuting()
	{
		return !this.entityPathNavigate.noPath();
	}

	/**
	 * Execute a one shot task or start executing a continuous task
	 */
	@Override
	public void startExecuting()
	{
		this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
	}

	/**
	 * Resets the task
	 */
	@Override
	public void resetTask()
	{
		this.closestLivingEntity = null;
	}

	/**
	 * Updates the task
	 */
	@Override
	public void updateTask()
	{
		if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 75.0D) //orig 49.0D
		{
			this.theEntity.getNavigator().setSpeed(this.nearSpeed);
		}
		else
		{
			this.theEntity.getNavigator().setSpeed(this.farSpeed);
		}
	}
}