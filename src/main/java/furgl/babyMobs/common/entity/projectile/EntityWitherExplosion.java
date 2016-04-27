package furgl.babyMobs.common.entity.projectile;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityWitherExplosion extends EntityThrowable
{
	public EntityWitherExplosion(World world)
	{
		super(world);
	}

	public EntityWitherExplosion(World worldObj, EntityLivingBase entitylivingbase) 
	{
		super(worldObj, entitylivingbase);
		this.noClip = false;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (this.ticksExisted > 50)
			this.setDead();

		this.motionY -= 0.03D;
		this.moveEntity(this.motionX, this.motionY, this.motionZ);
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
				this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY+0.5D, this.posZ, 0, 0, 0, new int[0]);
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!this.worldObj.isRemote)
		{
			if (!player.capabilities.isCreativeMode)
			{
				player.addPotionEffect(new PotionEffect(Potion.wither.id, 40));
				this.worldObj.playSoundEffect(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, "mob.wither.shoot", 1.0F, this.rand.nextFloat() * 0.1F + 1.8F);
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
	protected void onImpact(MovingObjectPosition mop) {}
	
	
}
