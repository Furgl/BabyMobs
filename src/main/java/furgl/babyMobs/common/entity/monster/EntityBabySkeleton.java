package furgl.babyMobs.common.entity.monster;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import com.google.common.collect.Sets;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.SkeletonType;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabySkeleton extends EntitySkeleton
{
	private int effectType;
	private PotionEffect potionEffect;
	private double red = -1;
	private double green = -1;
	private double blue = -1;
	private static final DataParameter<Integer> POTION_EFFECT = EntityDataManager.<Integer>createKey(EntityBabySkeleton.class, DataSerializers.VARINT);

	public EntityBabySkeleton(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
	}	

	@Override
	public void onDeath(DamageSource cause) //first achievement
	{
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		if (!this.worldObj.isRemote)
		{
			EntityTippedArrow arrow = new EntityTippedArrow(worldObj);
			arrow.addEffect(this.getPotionEffect());
			ItemStack stack = new ItemStack(Items.TIPPED_ARROW, this.worldObj.rand.nextInt(3)+1);
			Set<PotionEffect> customPotionEffects = Sets.<PotionEffect>newHashSet();
			PotionEffect effect = this.getPotionEffect();
			customPotionEffects.add(new PotionEffect(effect.getPotion(), effect.getDuration()*3, effect.getAmplifier()));
			PotionUtils.addPotionToItemStack(stack, PotionTypes.EMPTY);
			PotionUtils.appendEffects(stack, customPotionEffects);
			this.entityDropItem(stack, 0);
		}
		super.onDeath(cause);
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(ModItems.baby_skeleton_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	//TODO when attacked give effect
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (Config.useSpecialAbilities) 
		{
			if (!this.worldObj.isRemote && source.getEntity() instanceof EntityPlayer && !(source.getEntity() instanceof FakePlayer) && !(source.getEntity() instanceof EntityPlayer && ((EntityPlayer)source.getEntity()).capabilities.isCreativeMode))
				((EntityPlayer)source.getEntity()).addPotionEffect(this.getPotionEffect());
		}
		return super.attackEntityFrom(source, amount);
	}
	//end

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataManager.register(POTION_EFFECT, Integer.valueOf(0));//potionEffect
	}

	@Override
	public boolean attackEntityAsMob(Entity entity)
	{
		if (super.attackEntityAsMob(entity))
		{
			if (!this.worldObj.isRemote && entity instanceof EntityLivingBase && !(entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode))
				((EntityLivingBase)entity).addPotionEffect(this.getPotionEffect());
			return true;
		}
		else
			return false;
	}

	public PotionEffect getPotionEffect() {
		this.effectType = this.dataManager.get(POTION_EFFECT);
		if (this.effectType == 0) 
			this.setEffectType();
		if (this.potionEffect == null) {
			if (this.effectType == 1)
				this.potionEffect = new PotionEffect(MobEffects.POISON, 50, 0);
			else if (this.effectType == 2)
				this.potionEffect = new PotionEffect(MobEffects.BLINDNESS, 50, 0);
			else if (this.effectType == 3)
				this.potionEffect = new PotionEffect(MobEffects.WITHER, 50, 0);
			else if (this.effectType == 4)
				this.potionEffect = new PotionEffect(MobEffects.NAUSEA, 100, 1);
			else if (this.effectType == 5)
				this.potionEffect = new PotionEffect(MobEffects.SLOWNESS, 50, 0);
		}
		return this.potionEffect;
	}

	public void setEffectType() { 
		if (this.dataManager.get(POTION_EFFECT).intValue() == 0 && !this.worldObj.isRemote)
		{
			this.effectType = this.rand.nextInt(5)+1;
			this.dataManager.set(POTION_EFFECT, effectType);
		}
		else if (this.dataManager.get(POTION_EFFECT).intValue() != 0)
			this.effectType = this.dataManager.get(POTION_EFFECT).intValue();
	}

	public void setColors() {
		Collection<PotionEffect> map = new ArrayList<PotionEffect>();
		map.add(this.getPotionEffect());
		int i = PotionUtils.getPotionColorFromEffectList(map);
		this.red = (i >> 16 & 255) / 255.0D;
		this.green = (i >> 8 & 255) / 255.0D;
		this.blue = (i >> 0 & 255) / 255.0D;
	}

	@Override
	public void onLivingUpdate()
	{
		//TODO effects
		if (Config.useSpecialAbilities)
		{
			if (this.effectType == 0)
				this.setEffectType();
			if (this.worldObj.isRemote && this.effectType != 0 && this.red == -1)
				this.setColors();
			if (this.worldObj.isRemote && this.red != -1)
			{
				if (this.ticksExisted == 3)
					BabyMobs.proxy.spawnEntitySkeletonEffectFX(this.worldObj, this, (float) this.red, (float) this.green, (float) this.blue);
				this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, this.red, this.green, this.blue, new int[0]);
			}
		}
		//end

		super.onLivingUpdate();

		if (this.worldObj.isRemote)
			this.setSize(0.6F, 1.2F);
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase target, float p_82196_2_)
	{
		//TODO changed to EntitySkeletonArrow from EntityArrow
		EntityTippedArrow entityarrow = new EntityTippedArrow(this.worldObj, this);
		if (Config.useSpecialAbilities && !this.worldObj.isRemote)
			entityarrow.addEffect(this.getPotionEffect());
		double d0 = target.posX - this.posX;
		double d1 = target.getEntityBoundingBox().minY + (double)(target.height / 3.0F) - entityarrow.posY;
		double d2 = target.posZ - this.posZ;
		double d3 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2);
		entityarrow.setThrowableHeading(d0, d1 + d3 * 0.20000000298023224D, d2, 1.6F, (float)(14 - this.worldObj.getDifficulty().getDifficultyId() * 4));
		int i = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.POWER, this);
		int j = EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.PUNCH, this);
		entityarrow.setDamage((double)(p_82196_2_ * 2.0F) + this.rand.nextGaussian() * 0.25D + (double)((float)this.worldObj.getDifficulty().getDifficultyId() * 0.11F));
		if (i > 0)
			entityarrow.setDamage(entityarrow.getDamage() + (double)i * 0.5D + 0.5D);
		if (j > 0)
			entityarrow.setKnockbackStrength(j);
		if (EnchantmentHelper.getMaxEnchantmentLevel(Enchantments.FLAME, this) > 0 || this.func_189771_df() == SkeletonType.WITHER)
			entityarrow.setFire(100);
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
		this.worldObj.spawnEntityInWorld(entityarrow);
		//end
	}

	@Override
	public double getYOffset()
	{
		return super.getYOffset() + (this.getRidingEntity() instanceof EntityHorse ? 0.7D : 0.2D);
	}

	@Override
	public float getEyeHeight()
	{
		return 0.9F;
	}
}