package furgl.babyMobs.common.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.Vec3;

public class EntityAIBabyAvoidEntity<T extends Entity> extends EntityAIBase
{
    private Class<T> field_181064_i;
	public final Predicate<?> predicate1 = new Predicate<Object>()
	
	{
		public boolean func_180419_a(Entity p_180419_1_)
		{
			return p_180419_1_.isEntityAlive() && EntityAIBabyAvoidEntity.this.theEntity.getEntitySenses().canSee(p_180419_1_);
		}
		@Override
		public boolean apply(Object p_apply_1_)
		{
			return this.func_180419_a((Entity)p_apply_1_);
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
	private Predicate<?> field_179510_i;

	public EntityAIBabyAvoidEntity(EntityCreature entity, Predicate<?> predicate2, float fieldf, double FarSpeed, double NearSpeed)
	{
		this.theEntity = entity;
		this.field_179510_i = predicate2;
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
		field_181064_i = (Class<T>) this.theEntity.getClass();
		@SuppressWarnings("unchecked")
		List<T> list = this.theEntity.worldObj.<T>getEntitiesWithinAABB(field_181064_i, this.theEntity.getEntityBoundingBox().expand(this.field_179508_f, 3.0D, this.field_179508_f), Predicates.and(new Predicate[] {EntitySelectors.NOT_SPECTATING, this.predicate1, this.field_179510_i}));

		if (list.isEmpty())
		{
			return false;
		}
		else
		{
			this.closestLivingEntity = (Entity)list.get(0);
			Vec3 vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));

			if (vec3 == null)
			{
				return false;
			}
			while (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity))
			{
				vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this.theEntity, 16, 7, new Vec3(this.closestLivingEntity.posX, this.closestLivingEntity.posY, this.closestLivingEntity.posZ));
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