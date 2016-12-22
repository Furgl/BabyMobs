package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabySpider extends EntitySpider
{
	public EntityBabySpider(World worldIn)
	{
		super(worldIn);
		this.setSize(0.8F, 0.5F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
	}	

	@Override
	public void onDeath(DamageSource cause) //first achievement
	{
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
	}

	@Override
	protected boolean canDropLoot()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return ModEntities.getSpawnEgg(this.getClass());
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.30F;
	}
	
	public double getMountedYOffset()
    {
        return (double)(this.height * 0.5F);
    }

	/**
	 * Called to update the entity's position/logic.
	 */
	@Override
	public void onUpdate()
	{
		//TODO jockey check

		if (!this.getEntityData().hasKey("JockeyCheck") && this.ticksExisted == 1 && !this.worldObj.isRemote && !this.isBeingRidden() && this.getClass() == EntityBabySpider.class)
		{
			this.getEntityData().setBoolean("JockeyCheck", true);
			if(this.worldObj.rand.nextInt(100) <= Config.babySpiderJockeyRate)
			{
				EntityBabySkeleton entityBabySkeleton = new EntityBabySkeleton(this.worldObj);
				entityBabySkeleton.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
				entityBabySkeleton.onInitialSpawn(this.worldObj.getDifficultyForLocation(new BlockPos(this)), (IEntityLivingData)null);
				this.worldObj.spawnEntityInWorld(entityBabySkeleton);
				entityBabySkeleton.startRiding(this, true);
			}
		}
		//end

		super.onUpdate();
	}
}