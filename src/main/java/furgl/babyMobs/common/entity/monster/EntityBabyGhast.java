package furgl.babyMobs.common.entity.monster;

import java.util.Random;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.projectile.EntityGhastFireball;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFindEntityNearestPlayer;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyGhast extends EntityGhast
{
	public EntityBabyGhast(World worldIn)
	{
		super(worldIn);
		this.setSize(1.4F, 1.4F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
	}	

	@Override
    protected void initEntityAI()
    {
        this.tasks.addTask(5, new EntityBabyGhast.AIRandomFly(this));
        this.tasks.addTask(7, new EntityBabyGhast.AILookAround(this));
        this.tasks.addTask(7, new EntityBabyGhast.AIFireballAttack(this));
        this.targetTasks.addTask(1, new EntityAIFindEntityNearestPlayer(this));
    }

	@Override
	public void onDeath(DamageSource cause) //first achievement
	{
		if (!this.world.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
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
		return 1.0F;
	}

	static class AIFireballAttack extends EntityAIBase
	{
		private EntityGhast parentEntity;
		public int attackTimer;

		public AIFireballAttack(EntityGhast ghast)
		{
			this.parentEntity = ghast;
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute()
		{
			return this.parentEntity.getAttackTarget() != null;
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting()
		{
			this.attackTimer = 0;
		}

		/**
		 * Resets the task
		 */
		public void resetTask()
		{
			this.parentEntity.setAttacking(false);
		}

		/**
		 * Updates the task
		 */
		@Override
		public void updateTask()
		{
			EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
			double d0 = 64.0D;

			if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < d0 * d0 && this.parentEntity.canEntityBeSeen(entitylivingbase))
			{
				World world = this.parentEntity.world;
				++this.attackTimer;

				if (this.attackTimer == 10)
				{
					world.playEvent((EntityPlayer)null, 1015, new BlockPos(this.parentEntity), 0);
				}

				if (this.attackTimer == 20)
				{
					double d1 = 4.0D;
					Vec3d vec3d = this.parentEntity.getLook(1.0F);
					double d2 = entitylivingbase.posX - (this.parentEntity.posX + vec3d.xCoord * d1);
					double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + this.parentEntity.posY + (double)(this.parentEntity.height / 2.0F));
					double d4 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3d.zCoord * d1);
					world.playEvent((EntityPlayer)null, 1016, new BlockPos(this.parentEntity), 0);
					//TODO EntityGhastFireball (only entity is replaced from orig)
					if (Config.useSpecialAbilities)
					{
						EntityGhastFireball fireball = new EntityGhastFireball(world, this.parentEntity, d2, d3, d4);
						fireball.explosionPower = this.parentEntity.getFireballStrength();
						fireball.posX = this.parentEntity.posX + vec3d.xCoord * d1;
						fireball.posY = this.parentEntity.posY + this.parentEntity.height / 2.0F + 0.5D;
						fireball.posZ = this.parentEntity.posZ + vec3d.zCoord * d1;
						world.spawnEntity(fireball);
					}
					else
					{
						EntityLargeFireball fireball = new EntityLargeFireball(world, this.parentEntity, d2, d3, d4);
						fireball.explosionPower = this.parentEntity.getFireballStrength();
						fireball.posX = this.parentEntity.posX + vec3d.xCoord * d1;
						fireball.posY = this.parentEntity.posY + this.parentEntity.height / 2.0F + 0.5D;
						fireball.posZ = this.parentEntity.posZ + vec3d.zCoord * d1;
						world.spawnEntity(fireball);
					}
					//end   

					this.attackTimer = -40;
				}
			}
			else if (this.attackTimer > 0)
			{
				--this.attackTimer;
			}

			this.parentEntity.setAttacking(this.attackTimer > 10);
		}
	}
	static class AILookAround extends EntityAIBase
    {
        private final EntityGhast parentEntity;

        public AILookAround(EntityGhast ghast)
        {
            this.parentEntity = ghast;
            this.setMutexBits(2);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            return true;
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            if (this.parentEntity.getAttackTarget() == null)
            {
                this.parentEntity.rotationYaw = -((float)MathHelper.atan2(this.parentEntity.motionX, this.parentEntity.motionZ)) * (180F / (float)Math.PI);
                this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
            }
            else
            {
                EntityLivingBase entitylivingbase = this.parentEntity.getAttackTarget();
                if (entitylivingbase.getDistanceSqToEntity(this.parentEntity) < 4096.0D)
                {
                    double d1 = entitylivingbase.posX - this.parentEntity.posX;
                    double d2 = entitylivingbase.posZ - this.parentEntity.posZ;
                    this.parentEntity.rotationYaw = -((float)MathHelper.atan2(d1, d2)) * (180F / (float)Math.PI);
                    this.parentEntity.renderYawOffset = this.parentEntity.rotationYaw;
                }
            }
        }
    }

static class AIRandomFly extends EntityAIBase
    {
        private final EntityGhast parentEntity;

        public AIRandomFly(EntityGhast ghast)
        {
            this.parentEntity = ghast;
            this.setMutexBits(1);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityMoveHelper entitymovehelper = this.parentEntity.getMoveHelper();

            if (!entitymovehelper.isUpdating())
            {
                return true;
            }
            else
            {
                double d0 = entitymovehelper.getX() - this.parentEntity.posX;
                double d1 = entitymovehelper.getY() - this.parentEntity.posY;
                double d2 = entitymovehelper.getZ() - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;
                return d3 < 1.0D || d3 > 3600.0D;
            }
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean continueExecuting()
        {
            return false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            Random random = this.parentEntity.getRNG();
            double d0 = this.parentEntity.posX + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d1 = this.parentEntity.posY + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            double d2 = this.parentEntity.posZ + (double)((random.nextFloat() * 2.0F - 1.0F) * 16.0F);
            this.parentEntity.getMoveHelper().setMoveTo(d0, d1, d2, 1.0D);
        }
    }

static class GhastMoveHelper extends EntityMoveHelper
    {
        private final EntityGhast parentEntity;
        private int courseChangeCooldown;

        public GhastMoveHelper(EntityGhast ghast)
        {
            super(ghast);
            this.parentEntity = ghast;
        }

        public void onUpdateMoveHelper()
        {
            if (this.action == EntityMoveHelper.Action.MOVE_TO)
            {
                double d0 = this.posX - this.parentEntity.posX;
                double d1 = this.posY - this.parentEntity.posY;
                double d2 = this.posZ - this.parentEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (this.courseChangeCooldown-- <= 0)
                {
                    this.courseChangeCooldown += this.parentEntity.getRNG().nextInt(5) + 2;
                    d3 = (double)MathHelper.sqrt(d3);

                    if (this.isNotColliding(this.posX, this.posY, this.posZ, d3))
                    {
                        this.parentEntity.motionX += d0 / d3 * 0.1D;
                        this.parentEntity.motionY += d1 / d3 * 0.1D;
                        this.parentEntity.motionZ += d2 / d3 * 0.1D;
                    }
                    else
                    {
                        this.action = EntityMoveHelper.Action.WAIT;
                    }
                }
            }
        }

        /**
         * Checks if entity bounding box is not colliding with terrain
         */
        private boolean isNotColliding(double x, double y, double z, double p_179926_7_)
        {
            double d0 = (x - this.parentEntity.posX) / p_179926_7_;
            double d1 = (y - this.parentEntity.posY) / p_179926_7_;
            double d2 = (z - this.parentEntity.posZ) / p_179926_7_;
            AxisAlignedBB axisalignedbb = this.parentEntity.getEntityBoundingBox();

            for (int i = 1; (double)i < p_179926_7_; ++i)
            {
                axisalignedbb = axisalignedbb.offset(d0, d1, d2);

                if (!this.parentEntity.world.getCollisionBoxes(this.parentEntity, axisalignedbb).isEmpty())
                {
                    return false;
                }
            }

            return true;
        }
    }
}