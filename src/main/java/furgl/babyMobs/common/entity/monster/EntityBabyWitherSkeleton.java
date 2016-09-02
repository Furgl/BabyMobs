package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityWitherSkeletonSmoke;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIFleeSun;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIRestrictSun;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyWitherSkeleton extends EntitySkeleton
{
	public EntityBabyWitherSkeleton(World worldIn)
	{
		super(worldIn);
		this.setSize(0.6F, 1.2F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.setSkeletonType(1);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.maxHurtResistantTime = 50;
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
	}	
	
	@Override
	protected void initEntityAI()
    {
        this.tasks.addTask(1, new EntityAISwimming(this));
        this.tasks.addTask(2, new EntityAIRestrictSun(this));
        this.tasks.addTask(3, new EntityAIFleeSun(this, 1.0D));
        this.tasks.addTask(3, new EntityAIAvoidEntity(this, EntityWolf.class, 6.0F, 1.0D, 1.2D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityArrow.class, 12.0F, 1.2D, 1.6D));
        this.tasks.addTask(1, new EntityAIAvoidEntity(this, EntityPlayer.class, 12.0F, 1.2D, 1.6D));
        this.tasks.addTask(5, new EntityAIWander(this, 1.0D));
        this.tasks.addTask(6, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.tasks.addTask(6, new EntityAILookIdle(this));
    }

	@Override
	public void onDeath(DamageSource cause) //first achievement
	{
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);

		//TODO skull drop
		if (cause.getEntity() instanceof EntityPlayer)
		{
			int chance = 60 + 10*EnchantmentHelper.getLootingModifier((EntityLivingBase) cause.getEntity());
			if (this.rand.nextInt(100) <= chance)
			{
				this.entityDropItem(new ItemStack(Items.SKULL, 1, 1), 0.0F);
			}
		}
		//end
	}

	@Override
	protected boolean canDropLoot()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return new ItemStack(ModItems.baby_wither_skeleton_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		
		if (this.worldObj.isRemote)
			this.setSize(0.6F, 1.5F);
	}
	
	protected void setEquipmentBasedOnDifficulty(DifficultyInstance difficulty)
    {
        super.setEquipmentBasedOnDifficulty(difficulty);
        this.setHeldItem(EnumHand.MAIN_HAND, null);
    }
	
	//TODO on attack 
	@Override
	public boolean attackEntityFrom(DamageSource source, float amount)
	{
		if (Config.useSpecialAbilities)
		{
			EntityPlayer player = null;
			if (source.getSourceOfDamage() instanceof EntityPlayer)
				player = (EntityPlayer) source.getSourceOfDamage();
			else if (source.getSourceOfDamage() instanceof EntityArrow && ((EntityArrow) source.getSourceOfDamage()).shootingEntity instanceof EntityPlayer)
				player = (EntityPlayer) ((EntityArrow) source.getSourceOfDamage()).shootingEntity;
			if (player != null && !(player instanceof FakePlayer) && this.getHealth() > 0 && this.hurtResistantTime < 25)
			{
				player.addStat(Achievements.achievementItsMine);
				if (!this.worldObj.isRemote && !player.capabilities.isCreativeMode)
				{
					player.addPotionEffect(new PotionEffect(MobEffects.WITHER, 100));
					player.addPotionEffect(new PotionEffect(MobEffects.NAUSEA, 140, 3));
					player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 20, 3));
					player.knockBack(player, 0.0F, this.posX-player.posX, this.posZ-player.posZ);
					Vec3d vec = new Vec3d(this.posX, this.posY+0.5D, this.posZ);
					EntitySpawner entitySpawner = new EntitySpawner(EntityWitherSkeletonSmoke.class, this.worldObj, vec, 5);
					entitySpawner.setShapeSphere(0);
					entitySpawner.setMovementInOut(-1D);
					entitySpawner.setRandVar(0.3D);
					entitySpawner.run();
				}
				else
				{
					player.playSound(SoundEvents.ENTITY_WITHER_HURT, 1.0F, rand.nextFloat()*0.2F + 1.8F);
					for (int i=0; i<100; i++)
					{
						BabyMobs.proxy.spawnEntitySquidInkFX(worldObj,this.posX+(rand.nextDouble()-0.5D), this.posY+rand.nextDouble()*1.1D, this.posZ+(rand.nextDouble()-0.5D), 0, 0, 0);
						this.worldObj.spawnParticle(EnumParticleTypes.SMOKE_LARGE, this.posX+(rand.nextDouble()-0.5D), this.posY+rand.nextDouble()*1.1D, this.posZ+(rand.nextDouble()-0.5D), 0, 0, 0, 0);
					}
				}
				if (!this.worldObj.isRemote)
					this.addPotionEffect(new PotionEffect(MobEffects.INVISIBILITY, 50));

				PathNavigate entityPathNavigate = this.getNavigator();
				Vec3d vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this, 16, 7, new Vec3d(player.posX, player.posY, player.posZ));
				if (vec3 != null)
				{
					while (player.getDistanceSq(vec3.xCoord, vec3.yCoord, vec3.zCoord) < player.getDistanceSqToEntity(this))
					{
						vec3 = RandomPositionGenerator.findRandomTargetBlockAwayFrom(this, 16, 7, new Vec3d(player.posX, player.posY, player.posZ));
					}
					entityPathNavigate.setPath(entityPathNavigate.getPathToXYZ(vec3.xCoord, vec3.yCoord, vec3.zCoord), 1.2D);
				}
			}
		}
		return super.attackEntityFrom(source, amount);
	}
	//end

	@Override
	public double getYOffset()
	{
		return super.getYOffset() - 0.5D;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.9F;
	}
}


