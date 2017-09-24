package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.projectile.EntityWitherWitherSkull;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.entity.projectile.EntityWitherSkull;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBabyWither extends EntityWither
{
	public EntityBabyWither(World worldIn)
	{
		super(worldIn);
		this.setHealth(this.getMaxHealth());
		this.setSize(0.6F, 1.7F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));	
	}	

	@Override
	public void onDeath(DamageSource cause) //first achievement
	{
		/*if (!this.world.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
//			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);*/
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

	private void launchWitherSkullToEntity(int par1, EntityLivingBase entitylivingbase)
	{
		this.launchWitherSkullToCoords(par1, entitylivingbase.posX, entitylivingbase.posY + entitylivingbase.getEyeHeight() * 0.5D, entitylivingbase.posZ, par1 == 0 && this.rand.nextFloat() < 0.001F);
	}

	private void launchWitherSkullToCoords(int par1, double x, double y, double z, boolean invulnerable)
	{
		this.world.playEvent((EntityPlayer)null, 1014, new BlockPos(this), 0);
		double d3 = this.func_82214_u(par1);
		double d4 = this.func_82208_v(par1);
		double d5 = this.func_82213_w(par1);
		double d6 = x - d3;
		double d7 = y - d4;
		double d8 = z - d5;
		//TODO entityWitherWitherSkull
		if (Config.useSpecialAbilities)
		{
			EntityWitherWitherSkull entitywitherwitherskull = new EntityWitherWitherSkull(this.world, this, d6, d7, d8);
			if (invulnerable)
				entitywitherwitherskull.setInvulnerable(true);

			entitywitherwitherskull.posY = this.posY;//d4;
			entitywitherwitherskull.posX = this.posX;//d3;
			entitywitherwitherskull.posZ = this.posZ;//d5;
			this.world.spawnEntity(entitywitherwitherskull);
		}
		else
		{
			EntityWitherSkull entitywitherskull = new EntityWitherSkull(this.world, this, d6, d7, d8);
			if (invulnerable)
				entitywitherskull.setInvulnerable(true);

			entitywitherskull.posY = this.posY;//d4;
			entitywitherskull.posX = this.posX;//d3;
			entitywitherskull.posZ = this.posZ;//d5;
			this.world.spawnEntity(entitywitherskull);
		}
		//end
	}

	private double func_82214_u(int p_82214_1_)
	{
		if (p_82214_1_ <= 0)
		{
			return this.posX;
		}
		else
		{
			float f = (this.renderYawOffset + (float)(180 * (p_82214_1_ - 1))) * 0.017453292F;
			float f1 = MathHelper.cos(f);
			return this.posX + (double)f1 * 1.3D;
		}
	}

	private double func_82208_v(int p_82208_1_)
	{
		return this.posY - 2.0D;
	}

	private double func_82213_w(int p_82213_1_)
	{
		if (p_82213_1_ <= 0)
		{
			return this.posZ;
		}
		else
		{
			float f = (this.renderYawOffset + (float)(180 * (p_82213_1_ - 1))) * 0.017453292F;
			float f1 = MathHelper.sin(f);
			return this.posZ + (double)f1 * 1.3D;
		}
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
	{
		this.launchWitherSkullToEntity(0, p_82196_1_);
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (this.isEntityInvulnerable(source))
		{
			return false;
		}
		else if (source != DamageSource.DROWN && !(source.getTrueSource() instanceof EntityBabyWither))
		{
			if (this.getInvulTime() > 0 && source != DamageSource.OUT_OF_WORLD)
			{
				return false;
			}
			else
			{
				Entity entity;

				if (this.isArmored())
				{
					entity = source.getTrueSource();

					//TODO reflect arrows when armored
					if (Config.useSpecialAbilities)
					{
						if (entity instanceof EntityArrow && ((EntityArrow) entity).shootingEntity != null)
						{
							EntityArrow arrow = (EntityArrow) entity;
							EntityArrow newArrow = new EntityTippedArrow(world, (EntityLivingBase) arrow.shootingEntity);
							newArrow.copyLocationAndAnglesFrom(arrow);
							newArrow.setDamage(arrow.getDamage());
							double d0 = arrow.shootingEntity.posX - newArrow.posX;
							double d1 = arrow.shootingEntity.getEntityBoundingBox().minY + newArrow.shootingEntity.height / 3.0F - newArrow.posY;
							double d2 = arrow.shootingEntity.posZ - newArrow.posZ;
							double d3 = MathHelper.sqrt(d0 * d0 + d2 * d2);
							float f4 = (float)(d3 * 0.20000000298023224D);
							newArrow.setThrowableHeading(d0, d1 + f4, d2, 1.6F, 14 - this.world.getDifficulty().getDifficultyId() * 4);
							if (!this.world.isRemote)
							{
								this.world.spawnEntity(newArrow);
								arrow.setDead();
							}
							return false;
						}
					}
					//end
				}
				return super.attackEntityFrom(source, amount);
			}
		}
		else
		{
			return false;
		}
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		//TODO drops changed
		if (this.world.rand.nextInt(3) == 0)
		{
			EntityItem entityitem = this.dropItem(Items.NETHER_STAR, 1);

			if (entityitem != null)
			{
				entityitem.setNoDespawn();
			}
		}
		else 
		{
/*			Iterator iterator = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();

			while (iterator.hasNext())
			{
				EntityPlayer entityplayer = (EntityPlayer)iterator.next();
				entity//player.addStat(Achievements.achievementBetterLuckNextTime);
			}*/
			this.entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
		}
		//end

		/*if (!this.world.isRemote)
		{
			Iterator iterator = this.world.getEntitiesWithinAABB(EntityPlayer.class, this.getEntityBoundingBox().expand(50.0D, 100.0D, 50.0D)).iterator();

			while (iterator.hasNext())
			{
				EntityPlayer entityplayer = (EntityPlayer)iterator.next();
				entity//player.addStat(AchievementList.KILL_WITHER);
			}
		}*/
	}
}