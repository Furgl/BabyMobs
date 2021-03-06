package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.projectile.EntitySquidInk;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class EntityBabySquid extends EntitySquid
{
	public EntityBabySquid(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
//		if (!this.world.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
//			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
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

	//TODO squid ink
	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (Config.useSpecialAbilities)
		{
			if (this.isInWater() && source.getTrueSource() instanceof EntityLivingBase && this.getHealth() > 0)
			{
				Vec3d vec = new Vec3d(this.posX, this.posY, this.posZ);
				EntitySpawner entitySpawner = new EntitySpawner(EntitySquidInk.class, this.world, vec, 5);
				entitySpawner.setShapeSphere(0);
				entitySpawner.setMovementInOut(-1D);
				entitySpawner.setRandVar(0.5D);
				entitySpawner.run();
				
				ReflectionHelper.setPrivateValue(EntitySquid.class, this, 2.0F, 8); //randomMotionSpeed

				this.squidRotation = (float) (Math.PI-1);
				this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_SLIME_ATTACK, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 8F, false);
			}
		}
		return super.attackEntityFrom(source, amount);
	}
	//end
}