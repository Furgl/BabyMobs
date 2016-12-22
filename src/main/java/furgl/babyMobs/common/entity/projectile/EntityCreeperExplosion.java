package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.common.BabyMobs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityCreeperExplosion extends EntityThrowable
{
	public EntityCreeperExplosion(World world)
	{
		super(world);
	}

	public EntityCreeperExplosion(World worldObj, EntityLivingBase entitylivingbase) 
	{
		super(worldObj, entitylivingbase);
		this.noClip = false;
		this.setFire(100);
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.ticksExisted > 50)
			this.setDead();

		this.motionY -= 0.03D;
		this.moveEntity(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
		this.motionX *= 0.9990000128746033D;
		this.motionY *= 0.9990000128746033D;
		this.motionZ *= 0.9990000128746033D;
		if (this.onGround)
		{
			this.motionX *= 0.699999988079071D;
			this.motionZ *= 0.699999988079071D;
		}

		if (this.worldObj.isRemote)
		{
			for (int i=0; i<2; i++)
			{
				BabyMobs.proxy.spawnEntitySquidInkFX(worldObj, this.posX, this.posY+0.3D, this.posZ, 0, 0, 0);
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX, this.posY+0.5D, this.posZ, 0, 0, 0, new int[0]);
			}
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!this.worldObj.isRemote)
		{
			if (!player.isImmuneToFire() && !player.isBurning() && player.attackEntityFrom(DamageSource.onFire, 1.0F))
			{
				player.setFire(3);
				this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ITEM_FLINTANDSTEEL_USE, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.4F + 0.8F, false);
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
