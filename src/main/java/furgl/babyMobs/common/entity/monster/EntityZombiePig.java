package furgl.babyMobs.common.entity.monster;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityPigZombie;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityZombiePig extends EntityPig
{
	private static final DataParameter<Boolean> CONVERTING = EntityDataManager.<Boolean>createKey(EntityZombiePig.class, DataSerializers.BOOLEAN);
	boolean isAngry;
	private int conversionTime;

	public EntityZombiePig(World world)
	{
		super(world);
		this.isImmuneToFire = true;
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
		this.targetTasks.addTask(1, new EntityAIHurtByTarget(this, true, new Class[] {EntityPigZombie.class}));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityIronGolem.class, true));
	}

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(6, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
	}

	//TODO convert
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.getDataManager().register(CONVERTING, Boolean.valueOf(false));
	}
	
	public boolean isConverting()
    {
        return ((Boolean)this.getDataManager().get(CONVERTING)).booleanValue();
    }

	public void startConversion(int ticks)
	{
		this.conversionTime = ticks;
		this.getDataManager().set(CONVERTING, Boolean.valueOf(true));
		this.removePotionEffect(MobEffects.WEAKNESS);
		this.addPotionEffect(new PotionEffect(MobEffects.STRENGTH, ticks, Math.min(this.world.getDifficulty().getDifficultyId() - 1, 0)));
		this.world.setEntityState(this, (byte)16);
	}

	protected void convert()
	{
		EntityPig entitypig = new EntityPig(this.world);
		entitypig.copyLocationAndAnglesFrom(this);
		entitypig.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(entitypig)), (IEntityLivingData)null);

		this.world.removeEntity(this);
		entitypig.setNoAI(this.isAIDisabled());
		
        entitypig.setGrowingAge(-24000);

		if (this.hasCustomName())
		{
			entitypig.setCustomNameTag(this.getCustomNameTag());
			entitypig.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
		}

		this.world.spawnEntity(entitypig);
		entitypig.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 200, 0));
		this.world.playEvent((EntityPlayer)null, 1027, new BlockPos((int)this.posX, (int)this.posY, (int)this.posZ), 0);
	}

	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		if (stack != null && stack.getItem() == Items.GOLDEN_CARROT && stack.getMetadata() == 0 && this.isPotionActive(MobEffects.WEAKNESS)) {
			if (!player.capabilities.isCreativeMode)
				stack.shrink(1);
			if (!this.world.isRemote)
				this.startConversion(this.rand.nextInt(1000) + 200);
			return true;
		}
		else
			return false;
	}

	@Override
	protected boolean canDespawn()
	{
		return !this.isConverting();
	}

	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setInteger("ConversionTime", this.isConverting() ? this.conversionTime : -1);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
    {
        super.readEntityFromNBT(tagCompund);
        if (tagCompund.hasKey("ConversionTime", 99) && tagCompund.getInteger("ConversionTime") > -1)
            this.startConversion(tagCompund.getInteger("ConversionTime"));
    }
	
	@Override
	@SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 16)
            if (!this.isSilent())
                this.world.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.ENTITY_ZOMBIE_VILLAGER_CURE, this.getSoundCategory(), 1.0F + this.rand.nextFloat(), this.rand.nextFloat() * 0.7F + 0.3F, false);
        else
            super.handleStatusUpdate(id);
        else
        	super.handleStatusUpdate(id);
    }
	//end

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
			this.recentlyHit = this.getRevengeTimer();
		}
		return super.attackEntityFrom(source, amount);
	}

	@Override
	public void onUpdate()
	{
		if (!this.world.isRemote && this.getDataManager().get(CONVERTING).booleanValue())
		{
			this.conversionTime -= 1;
			if (this.conversionTime <= 0)
				this.convert();
		}

		if (this.isBeingRidden())
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.25D);
		else
			this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);

		//sync anger with rider
		if (!this.world.isRemote)
		{
			if (this.getAttackTarget() == null && this.getLowestRidingEntity() instanceof EntityBabyPigZombie && ((EntityBabyPigZombie)this.getLowestRidingEntity()).isAngry())
			{
				EntityLivingBase target = ((EntityBabyPigZombie) this.getLowestRidingEntity()).getAttackTarget();
				if (((EntityBabyPigZombie)this.getLowestRidingEntity()).isAngry() && target != null && target instanceof EntityPlayer && !(target instanceof FakePlayer))
				{
					this.setRevengeTarget(target);
					this.attackingPlayer = (EntityPlayer) target;
					this.recentlyHit = this.getRevengeTimer();
				}
			}
			else if (this.getAttackTarget() instanceof EntityPlayer && !(this.getAttackTarget() instanceof FakePlayer) && ((EntityPlayer) this.getAttackTarget()).capabilities.isCreativeMode)
				this.setAttackTarget(null);
		}

		if (!this.world.isRemote && this.world.getDifficulty() == EnumDifficulty.PEACEFUL)
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
	protected Item getDropItem()
	{
		return Items.ROTTEN_FLESH;
	}

	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(3) + 1 + this.rand.nextInt(1 + p_70628_2_);
		for (int k = 0; k < j; ++k)
		{
			this.dropItem(Items.ROTTEN_FLESH, 1);
		}
	}

	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt) {}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(35.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.getAttributeMap().registerAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(3.0D);
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		float f = (float)this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getAttributeValue();
		int i = 0;

		if (p_70652_1_ instanceof EntityLivingBase)
		{
			f += EnchantmentHelper.getModifierForCreature(this.getHeldItem(EnumHand.MAIN_HAND), ((EntityLivingBase)p_70652_1_).getCreatureAttribute());
			i += EnchantmentHelper.getKnockbackModifier(this);
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

			this.applyEnchantments(this, p_70652_1_);
		}

		return flag;
	}
}