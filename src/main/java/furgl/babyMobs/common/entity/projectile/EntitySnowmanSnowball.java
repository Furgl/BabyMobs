package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntitySnowmanSnowball extends EntityThrowable
{
	public EntitySnowmanSnowball(World world)
	{
		super(world);
	}

	public EntitySnowmanSnowball(World worldObj, EntityLivingBase entitylivingbase) 
	{
		super(worldObj, entitylivingbase);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.worldObj.isRemote)
		{
			for (int i=0; i<2; i++)
				this.worldObj.spawnParticle(EnumParticleTypes.SNOW_SHOVEL, this.posX+(this.rand.nextDouble()-0.5D), this.posY+(this.rand.nextDouble()-0.5D), this.posZ+(this.rand.nextDouble()-0.5D), 0, 0.1D, 0, new int[0]);
		}
	}

	@Override
	protected void onImpact(RayTraceResult mop)
	{
		if (!this.worldObj.isRemote)
		{
			if (mop.entityHit != null && mop.entityHit instanceof EntityLiving)
			{
				byte b0 = 0;
				if (mop.entityHit instanceof EntityBlaze || mop.entityHit instanceof EntityBabyBlaze)
					b0 = 3;
				mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), b0);
				((EntityLiving) mop.entityHit).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 60, 0));
			}
			this.setDead();
		}
	}

}
