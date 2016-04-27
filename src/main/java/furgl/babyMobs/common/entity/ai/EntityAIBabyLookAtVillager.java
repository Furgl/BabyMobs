package furgl.babyMobs.common.entity.ai;

import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;

public class EntityAIBabyLookAtVillager extends EntityAIBase
{
    private EntityBabyIronGolem theGolem;
    private EntityVillager theVillager;
    private int lookTime;

    public EntityAIBabyLookAtVillager(EntityBabyIronGolem p_i1643_1_)
    {
        this.theGolem = p_i1643_1_;
        this.setMutexBits(3);
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
	public boolean shouldExecute()
    {
        if (!this.theGolem.worldObj.isDaytime())
        {
            return false;
        }
        else if (this.theGolem.getRNG().nextInt(8000) != 0)
        {
            return false;
        }
        else
        {
            this.theVillager = (EntityVillager)this.theGolem.worldObj.findNearestEntityWithinAABB(EntityVillager.class, this.theGolem.boundingBox.expand(6.0D, 2.0D, 6.0D), this.theGolem);
            return this.theVillager != null;
        }
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
	public boolean continueExecuting()
    {
        return this.lookTime > 0;
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
	public void startExecuting()
    {
        this.lookTime = 400;
        this.theGolem.setHoldingRose(true);
    }

    /**
     * Resets the task
     */
    @Override
	public void resetTask()
    {
        this.theGolem.setHoldingRose(false);
        this.theVillager = null;
    }

    /**
     * Updates the task
     */
    @Override
	public void updateTask()
    {
        this.theGolem.getLookHelper().setLookPositionWithEntity(this.theVillager, 30.0F, 30.0F);
        --this.lookTime;
    }
}