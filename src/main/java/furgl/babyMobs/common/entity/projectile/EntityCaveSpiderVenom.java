package furgl.babyMobs.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityCaveSpiderVenom extends EntityThrowable
{
	public EntityCaveSpiderVenom(World world)
	{
		super(world);
	}

	public EntityCaveSpiderVenom(World world, EntityLivingBase entitylivingbase) 
	{
		super(world, entitylivingbase);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (this.ticksExisted == 1)
			this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_GENERIC_DRINK, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.1F + 1.7F, false);
		
		if (this.world.isRemote)
		{
			for (int i=0; i<2; i++)
				this.world.spawnParticle(EnumParticleTypes.CRIT_MAGIC, this.posX+(this.rand.nextDouble()-0.5D), this.posY+(this.rand.nextDouble()-0.5D), this.posZ+(this.rand.nextDouble()-0.5D), this.motionX, this.motionY, this.motionZ, new int[0]);
		}
	}

	@Override
	protected void onImpact(RayTraceResult mop)
	{
		if (!this.world.isRemote)
		{
			if (mop.entityHit != null)
			{
				mop.entityHit.attackEntityFrom(DamageSource.causeThrownDamage(this, this.getThrower()), 1F);
				if (mop.entityHit instanceof EntityPlayer)
				{
					((EntityPlayer) mop.entityHit).addPotionEffect(new PotionEffect(MobEffects.POISON, 60, 0));
				}
			}
			this.setDead();
		}
	}

}
