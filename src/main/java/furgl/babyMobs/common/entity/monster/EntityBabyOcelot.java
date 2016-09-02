package furgl.babyMobs.common.entity.monster;

import com.google.common.base.Predicate;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAILeapAtTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIOcelotAttack;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAITargetNonTamed;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyOcelot extends EntityOcelot
{
	public EntityBabyOcelot(World worldIn)
	{
		super(worldIn);
		this.setSize(0.9F, 0.9F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.tasks.addTask(1, new EntityAIBabyFollowParent(this, 1.1D));
		this.targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityPlayer.class, true));
		this.setTameSkin(1);
	}	

	@Override
	protected void initEntityAI()
	{
		this.tasks.addTask(1, new EntityAISwimming(this));
		this.tasks.addTask(7, new EntityAILeapAtTarget(this, 0.3F));
		this.tasks.addTask(8, new EntityAIOcelotAttack(this));
		this.tasks.addTask(10, new EntityAIWander(this, 0.8D));
		this.tasks.addTask(11, new EntityAIWatchClosest(this, EntityPlayer.class, 10.0F));
		this.targetTasks.addTask(1, new EntityAITargetNonTamed(this, EntityChicken.class, false, (Predicate)null));
	}

	@Override
	public void onDeath(DamageSource cause) //first achievement
	{
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
	}

	//TODO added
	@Override
	public void onUpdate()
	{
		if (!this.worldObj.isRemote && this.worldObj.getDifficulty() == EnumDifficulty.PEACEFUL)
		{
			this.setDead();
		}
		if (this.ticksExisted == 1)
		{
			this.setTamed(true);
			this.setTameSkin(1);
			this.setGrowingAge(-2000000);
		}
		if (this.worldObj.isRemote && this.rand.nextInt(2) == 0)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, this.posX+(this.rand.nextDouble()-0.5D), this.posY+(this.rand.nextDouble()-0.5D), this.posZ+(this.rand.nextDouble()-0.5D), 0, 0, 0, 0);
		}
		if (this.motionX == 0 && this.motionZ == 0 && this.getAttackTarget() instanceof EntityPlayer)
		{
			this.getMoveHelper().setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, 1.0D);
		}
		super.onUpdate();
	}

	@Override
	public boolean isPotionApplicable(PotionEffect effect)
	{   	
		if (effect.getPotion() == MobEffects.POISON || effect.getPotion() == MobEffects.SLOWNESS)
			return false;
		return true;	
	}
	//end

	/**
	 * Called when the entity is attacked.
	 */
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (source.isMagicDamage())
			return false; //TODO prevent potion damage
		else
			return super.attackEntityFrom(source, amount);
	}

	@Override
	protected boolean canDespawn()
	{
		return true;
	}

	@Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance difficulty, IEntityLivingData livingData)
	{
		return livingData;
	}
}