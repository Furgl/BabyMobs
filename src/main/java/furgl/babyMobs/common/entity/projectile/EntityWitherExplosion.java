package furgl.babyMobs.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityWitherExplosion extends EntityThrowable
{
	public EntityWitherExplosion(World world)
	{
		super(world);
	}

	public EntityWitherExplosion(World world, EntityLivingBase entitylivingbase) 
	{
		super(world, entitylivingbase);
		this.noClip = false;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.ticksExisted > 50)
			this.setDead();

		this.motionY -= 0.03D;
		this.move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9990000128746033D;
		this.motionY *= 0.9990000128746033D;
		this.motionZ *= 0.9990000128746033D;
		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

		if (this.world.isRemote)
		{
			for (int i=0; i<2; i++)
				this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY+0.5D, this.posZ, 0, 0, 0, new int[0]);
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!this.world.isRemote)
		{
			if (!player.capabilities.isCreativeMode)
			{
				player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 40));
				this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_WITHER_SHOOT, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.1F + 1.8F, false);
				this.setDead();
			}
		}
	}

	@Override
	public float getGravityVelocity()
	{
		return 0;
	}

	@Override
	protected void onImpact(RayTraceResult mop) {}
	
	
}
