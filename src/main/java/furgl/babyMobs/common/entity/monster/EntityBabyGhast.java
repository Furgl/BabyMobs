package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.projectile.EntityGhastFireball;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityLargeFireball;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
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
		return new ItemStack(ModItems.baby_ghast_egg);
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
				World world = this.parentEntity.worldObj;
				++this.attackTimer;

				if (this.attackTimer == 10)
				{
					world.playAuxSFXAtEntity((EntityPlayer)null, 1015, new BlockPos(this.parentEntity), 0);
				}

				if (this.attackTimer == 20)
				{
					double d1 = 4.0D;
					Vec3d vec3d = this.parentEntity.getLook(1.0F);
					double d2 = entitylivingbase.posX - (this.parentEntity.posX + vec3d.xCoord * d1);
					double d3 = entitylivingbase.getEntityBoundingBox().minY + (double)(entitylivingbase.height / 2.0F) - (0.5D + this.parentEntity.posY + (double)(this.parentEntity.height / 2.0F));
					double d4 = entitylivingbase.posZ - (this.parentEntity.posZ + vec3d.zCoord * d1);
					world.playAuxSFXAtEntity((EntityPlayer)null, 1016, new BlockPos(this.parentEntity), 0);
					//TODO EntityGhastFireball (only entity is replaced from orig)
					if (Config.useSpecialAbilities)
					{
						EntityGhastFireball fireball = new EntityGhastFireball(world, this.parentEntity, d2, d3, d4);
						fireball.explosionPower = this.parentEntity.getFireballStrength();
						fireball.posX = this.parentEntity.posX + vec3d.xCoord * d1;
						fireball.posY = this.parentEntity.posY + this.parentEntity.height / 2.0F + 0.5D;
						fireball.posZ = this.parentEntity.posZ + vec3d.zCoord * d1;
						world.spawnEntityInWorld(fireball);
					}
					else
					{
						EntityLargeFireball fireball = new EntityLargeFireball(world, this.parentEntity, d2, d3, d4);
						fireball.explosionPower = this.parentEntity.getFireballStrength();
						fireball.posX = this.parentEntity.posX + vec3d.xCoord * d1;
						fireball.posY = this.parentEntity.posY + this.parentEntity.height / 2.0F + 0.5D;
						fireball.posZ = this.parentEntity.posZ + vec3d.zCoord * d1;
						world.spawnEntityInWorld(fireball);
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
}