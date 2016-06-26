package furgl.babyMobs.common.entity.monster;

import java.util.List;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityZombiePig extends EntityPig
{
	boolean isAngry;
	
	public EntityZombiePig(World world)
	{
		super(world);
		this.isImmuneToFire = true;
		this.tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, 0, true));
		List list = this.tasks.taskEntries;
		list.remove(6);
		list.remove(5);
		list.remove(4);
		list.remove(3);
		list.remove(2);
		list.remove(1);
	}

	@Override
	protected float getSoundPitch()
	{
		return this.rand.nextFloat() + 0.2F;
	}

	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source.getEntity() instanceof EntityPlayer && !(source.getEntity() instanceof FakePlayer) && !this.isAngry)
		{
			EntityPlayer entityplayer = (EntityPlayer) source.getEntity();
			this.setRevengeTarget(entityplayer);
			this.attackingPlayer = entityplayer;
		}
		return super.attackEntityFrom(source, amount);
	}

	
	@Override
    protected boolean canDespawn()
    {
        return true;
    }

	@Override
	public void onUpdate()
	{
		//sync anger with rider
		if (!this.worldObj.isRemote)
		{
			if (this.getAttackTarget() == null && this.riddenByEntity instanceof EntityBabyPigZombie && ((EntityBabyPigZombie)this.riddenByEntity).getAttackTarget() != null)
			{
				EntityLivingBase target = ((EntityBabyPigZombie) this.riddenByEntity).getAttackTarget();
				if (((EntityBabyPigZombie)this.riddenByEntity).getAttackTarget() != null && target != null && target instanceof EntityPlayer && !(target instanceof FakePlayer))
				{
					this.setRevengeTarget(target);
					this.attackingPlayer = (EntityPlayer) target;
				}
			}
		}

		if (!this.worldObj.isRemote && this.worldObj.difficultySetting == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}
		if (this.ticksExisted == 1)
		{
			this.setGrowingAge(-2000000);
		}
		super.onUpdate();
	}

	@Override
	public boolean isBreedingItem(ItemStack stack)
	{
		return false;
	}

	@Override
	public boolean canBeSteered()
	{
		return false;
	}

	@Override
	public boolean interact(EntityPlayer player)
	{
		return false;
	}

	@Override
	protected Item getDropItem()
	{
		return Items.rotten_flesh;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);
		for (int k = 0; k < j; ++k)
		{
			this.dropItem(Items.rotten_flesh, 1);
		}
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.followRange).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.attackDamage).setBaseValue(3.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_) //copied so pig can attack
	{
		float f = (float)this.getEntityAttribute(SharedMonsterAttributes.attackDamage).getAttributeValue();
		int i = 0;

		if (p_70652_1_ instanceof EntityLivingBase)
		{
			f += EnchantmentHelper.func_152377_a(this.getHeldItem(), ((EntityLivingBase)p_70652_1_).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this, null);
		}

		boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), f);

		if (flag)
		{
			if (i > 0)
			{
				p_70652_1_.addVelocity((double)(-MathHelper.sin(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F), 0.1D, (double)(MathHelper.cos(this.rotationYaw * (float)Math.PI / 180.0F) * (float)i * 0.5F));
				this.motionX *= 0.6D;
				this.motionZ *= 0.6D;
			}

			int j = EnchantmentHelper.getFireAspectModifier(this);

			if (j > 0)
			{
				p_70652_1_.setFire(j * 4);
			}

			//this.func_174815_a(this, p_70652_1_);
		}

		return flag;
	}
}
