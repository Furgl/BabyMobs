package furgl.babyMobs.common.entity.ai;

import java.util.List;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;

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
    public final Predicate field_179509_a = new Predicate()
    {
        public boolean func_180419_a(Entity p_180419_1_)
        {
            return p_180419_1_.isEntityAlive() && EntityAIBabyAvoidEntity.this.theEntity.getEntitySenses().canSee(p_180419_1_);
        }
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
    private Predicate field_179510_i;

    public EntityAIBabyAvoidEntity(EntityCreature p_i45890_1_, Predicate p_i45890_2_, float p_i45890_3_, double p_i45890_4_, double p_i45890_6_)
    {
        this.theEntity = p_i45890_1_;
        this.field_179510_i = p_i45890_2_;
        this.field_179508_f = p_i45890_3_;
        this.farSpeed = p_i45890_4_;
        this.nearSpeed = p_i45890_6_;
        this.entityPathNavigate = p_i45890_1_.getNavigator();
        this.setMutexBits(1);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List list = this.theEntity.worldObj.func_175674_a(this.theEntity, this.theEntity.getEntityBoundingBox().expand((double)this.field_179508_f, 3.0D, (double)this.field_179508_f), Predicates.and(new Predicate[] {IEntitySelector.NOT_SPECTATING, this.field_179509_a, this.field_179510_i}));

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
            else if (this.closestLivingEntity.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < this.closestLivingEntity.getDistanceSqToEntity(this.theEntity))
            {
                return false;
            }
            else
            {
                this.entityPathEntity = this.entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord);
                return this.entityPathEntity == null ? false : this.entityPathEntity.isDestinationSame(vec3);
            }
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean continueExecuting()
    {
        return !this.entityPathNavigate.noPath();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.entityPathNavigate.setPath(this.entityPathEntity, this.farSpeed);
    }

    /**
     * Resets the task
     */
    public void resetTask()
    {
        this.closestLivingEntity = null;
    }

    /**
     * Updates the task
     */
    public void updateTask()
    {
        if (this.theEntity.getDistanceSqToEntity(this.closestLivingEntity) < 49.0D)
        {
            this.theEntity.getNavigator().setSpeed(this.nearSpeed);
        }
        else
        {
            this.theEntity.getNavigator().setSpeed(this.farSpeed);
        }
    }
}