package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.packet.PacketVolatileLevitation;
import furgl.babyMobs.common.potion.ModPotions;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityShulkerBullet;
import net.minecraft.init.SoundEvents;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabyShulkerBullet extends EntityShulkerBullet 
{
	private EntityLivingBase entityLivingBase;

	public EntityBabyShulkerBullet(World worldIn) {
		super(worldIn);
	}

	public EntityBabyShulkerBullet(World worldIn, EntityLivingBase entitylivingbase, Entity entity, Axis axis) {
		super(worldIn, entitylivingbase, entity, axis);
		this.entityLivingBase = entitylivingbase;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getBrightnessForRender(float partialTicks)
	{
		return 5000;
	}

	@Override
	public float getBrightness(float partialTicks)
	{
		return 0.0F;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		this.motionX += (this.rand.nextDouble() - 0.5D)*0.1D;
		this.motionY += (this.rand.nextDouble() - 0.4D)*0.1D;
		this.motionZ += (this.rand.nextDouble() - 0.5D)*0.1D;
		this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, (this.rand.nextDouble() - 0.5D)*0.1D, (this.rand.nextDouble() - 0.5D)*0.1D, (this.rand.nextDouble() - 0.5D)*0.1D, null);
		this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX, this.posY, this.posZ, (this.rand.nextDouble() - 0.5D)*0.1D, (this.rand.nextDouble() - 0.5D)*0.1D, (this.rand.nextDouble() - 0.5D)*0.1D, null);
	}

	@Override
	protected void func_184567_a(RayTraceResult result)
	{
		if (result.entityHit == null)
		{
			((WorldServer)this.worldObj).spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, this.posX, this.posY, this.posZ, 2, 0.2D, 0.2D, 0.2D, 0.0D, new int[0]);
			this.playSound(SoundEvents.entity_shulker_bullet_hit, 1.0F, 1.0F);
		}
		else
		{
			boolean flag = result.entityHit.attackEntityFrom(DamageSource.causeIndirectDamage(this, this.entityLivingBase).setProjectile(), 4.0F);

			if (flag)
			{
				this.applyEnchantments(this.entityLivingBase, result.entityHit);

				if (result.entityHit instanceof EntityLivingBase)
				{
					((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(ModPotions.volatileLevitationPotion, 150));
					if (result.entityHit instanceof EntityPlayerMP)
						BabyMobs.network.sendTo(new PacketVolatileLevitation(150), (EntityPlayerMP)result.entityHit);
					//((EntityLivingBase)result.entityHit).addPotionEffect(new PotionEffect(MobEffects.levitation, 200, 255));
				}
			}
		}

		this.setDead();
	}
}
