package furgl.babyMobs.common.entity.monster;

import com.google.common.base.Predicate;
import com.mojang.authlib.GameProfile;

import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyDefendVillage;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyLookAtVillager;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackOnCollide;
import net.minecraft.entity.ai.EntityAIHurtByTarget;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAIMoveThroughVillage;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIMoveTowardsTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBabyIronGolem extends EntityGolem
{
	/** deincrements, and a distance-to-home check is done at 0 */
	private int homeCheckTimer;
	Village villageObj;
	private int attackTimer;
	private int holdRoseTick;
	public EntityBabyIronGolem(World worldIn)
	{
		super(worldIn);
		this.setSize(0.7F, 1.45F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.setHoldingRose(true);

		((PathNavigateGround)this.getNavigator()).func_179690_a(true);
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
		this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(5, new EntityAIBabyLookAtVillager(this));
		this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIBabyDefendVillage(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false, new Class[0]));
		this.targetTasks.addTask(3, new EntityBabyIronGolem.AINearestAttackableTargetNonCreeper(this, EntityLiving.class, 10, false, true, IMob.field_175450_e));
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).triggerAchievement(Achievements.achievementWhyAreTheySoStrong);
		
		if (!this.isPlayerCreated() && this.attackingPlayer != null && this.villageObj != null)
		{
			this.villageObj.setReputationForPlayer(this.attackingPlayer.getName(), -5);
		}

		super.onDeath(cause);
    }
	
	@Override
	protected boolean func_146066_aG()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_iron_golem_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public float getEyeHeight()
	{
		return 1.3F;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	@Override
	protected void updateAITasks()
	{
		if (--this.homeCheckTimer <= 0)
		{
			this.homeCheckTimer = 70 + this.rand.nextInt(50);
			this.villageObj = this.worldObj.getVillageCollection().getNearestVillage(new BlockPos(this), 32);

			if (this.villageObj == null)
			{
				this.detachHome();
			}
			else
			{
				BlockPos blockpos = this.villageObj.getCenter();
				this.func_175449_a(blockpos, (int)(this.villageObj.getVillageRadius() * 0.6F));
			}
		}

		super.updateAITasks();
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(50.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.25D);
	}

	/**
	 * Decrements the entity's air supply when underwater
	 */
	@Override
	protected int decreaseAirSupply(int p_70682_1_)
	{
		return p_70682_1_;
	}

	@Override
	protected void collideWithEntity(Entity p_82167_1_)
	{
		if (p_82167_1_ instanceof IMob && this.getRNG().nextInt(20) == 0)
		{
			this.setAttackTarget((EntityLivingBase)p_82167_1_);
		}

		super.collideWithEntity(p_82167_1_);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		//TODO plant roses
		if (this.ticksExisted % 50 == 0)
		{
			EntityPlayer player = this.worldObj.getClosestPlayerToEntity(this, 10D);
			if (player != null)
				player.triggerAchievement(Achievements.achievementAFlowerForMe);
		}
		if (Config.useSpecialAbilities)
		{
			if (!this.worldObj.isRemote && this.rand.nextInt(10000) == 0 && this.worldObj.isAirBlock(new BlockPos(this)) && this.worldObj.getBlockState(new BlockPos(this).down()) == Blocks.grass.getDefaultState())
			{
				this.worldObj.setBlockState(new BlockPos(this), Blocks.red_flower.getDefaultState());
			}
		}
		//end      
		if (this.attackTimer > 0)
		{
			--this.attackTimer;
		}

		if (this.holdRoseTick > 0)
		{
			//--this.holdRoseTick;
		}

		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0)
		{
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY - 0.20000000298023224D);
			int k = MathHelper.floor_double(this.posZ);
			IBlockState iblockstate = this.worldObj.getBlockState(new BlockPos(i, j, k));
			Block block = iblockstate.getBlock();

			if (block.getMaterial() != Material.air)
			{
				this.worldObj.spawnParticle(EnumParticleTypes.BLOCK_CRACK, this.posX + (this.rand.nextFloat() - 0.5D) * this.width, this.getEntityBoundingBox().minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D, new int[] {Block.getStateId(iblockstate)});
			}
		}
	}

	/**
	 * Returns true if this entity can attack entities of the specified class.
	 */
	@Override
	public boolean canAttackClass(Class p_70686_1_)
	{
		return this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(p_70686_1_) ? false : super.canAttackClass(p_70686_1_);
	}

	/**
	 * (abstract) Protected helper method to write subclass entity data to NBT.
	 */
	@Override
	public void writeEntityToNBT(NBTTagCompound tagCompound)
	{
		super.writeEntityToNBT(tagCompound);
		tagCompound.setBoolean("PlayerCreated", this.isPlayerCreated());
	}

	/**
	 * (abstract) Protected helper method to read subclass entity data from NBT.
	 */
	@Override
	public void readEntityFromNBT(NBTTagCompound tagCompund)
	{
		super.readEntityFromNBT(tagCompund);
		this.setPlayerCreated(tagCompund.getBoolean("PlayerCreated"));
	}

	@Override
	public boolean attackEntityAsMob(Entity p_70652_1_)
	{
		this.attackTimer = 10;
		this.worldObj.setEntityState(this, (byte)4);
		boolean flag = p_70652_1_.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + this.rand.nextInt(15));

		if (flag)
		{
			p_70652_1_.motionY += 0.4000000059604645D;
			this.func_174815_a(this, p_70652_1_);
		}

		this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		return flag;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleHealthUpdate(byte p_70103_1_)
	{
		if (p_70103_1_ == 4)
		{
			this.attackTimer = 10;
			this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
		}
		else if (p_70103_1_ == 11)
		{
			this.holdRoseTick = 400;
		}
		else
		{
			super.handleHealthUpdate(p_70103_1_);
		}
	}

	public Village getVillage()
	{
		return this.villageObj;
	}

	@SideOnly(Side.CLIENT)
	public int getAttackTimer()
	{
		return this.attackTimer;
	}

	public void setHoldingRose(boolean p_70851_1_)
	{
		this.holdRoseTick =  p_70851_1_ ? 400 : 0;
		//this.worldObj.setEntityState(this, (byte)11);
	}

	/**
	 * Returns the sound this mob makes when it is hurt.
	 */
	@Override
	protected String getHurtSound()
	{
		return "mob.irongolem.hit";
	}

	/**
	 * Returns the sound this mob makes on death.
	 */
	@Override
	protected String getDeathSound()
	{
		return "mob.irongolem.death";
	}

	@Override
	protected void playStepSound(BlockPos p_180429_1_, Block p_180429_2_)
	{
		this.playSound("mob.irongolem.walk", 1.0F, 1.0F);
	}
	
    /**
     * Returns the sound this mob makes while it's alive.
     */
    @Override
	protected String getLivingSound()
    {
        return null;
    }

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(3);
		int k;

		for (k = 0; k < j; ++k)
		{
			this.dropItemWithOffset(Item.getItemFromBlock(Blocks.red_flower), 1, BlockFlower.EnumFlowerType.POPPY.getMeta());
		}

		k = 3 + this.rand.nextInt(3);

		for (int l = 0; l < k; ++l)
		{
			this.dropItem(Items.iron_ingot, 1);
		}
	}

	public int getHoldRoseTick()
	{
		return this.holdRoseTick;
	}

	public boolean isPlayerCreated()
	{
		return (this.dataWatcher.getWatchableObjectByte(16) & 1) != 0;
	}

	public void setPlayerCreated(boolean p_70849_1_)
	{
		byte b0 = this.dataWatcher.getWatchableObjectByte(16);

		if (p_70849_1_)
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 | 1)));
		}
		else
		{
			this.dataWatcher.updateObject(16, Byte.valueOf((byte)(b0 & -2)));
		}
	}

	static class AINearestAttackableTargetNonCreeper extends EntityAINearestAttackableTarget
	{
		public AINearestAttackableTargetNonCreeper(final EntityCreature creature, Class p_i45858_2_, int p_i45858_3_, boolean p_i45858_4_, boolean p_i45858_5_, final Predicate predicate)
		{
			super(creature, p_i45858_2_, p_i45858_3_, p_i45858_4_, p_i45858_5_, predicate);
			this.targetEntitySelector = new Predicate()
			{
				public boolean func_180096_a(EntityLivingBase entitylivingbase)
				{
					if (predicate != null && !predicate.apply(entitylivingbase))
					{
						return false;
					}
					else if (entitylivingbase instanceof EntityCreeper)
					{
						return false;
					}
					//TODO change zombie villagers
					else if (Config.useSpecialAbilities && entitylivingbase instanceof EntityZombie && ((EntityZombie)entitylivingbase).isVillager())
					{
						if (!((EntityZombie)entitylivingbase).isConverting())
						{
							entitylivingbase.addPotionEffect(new PotionEffect(Potion.weakness.id, 100, 0));
							FakePlayer player = new FakePlayer((WorldServer) entitylivingbase.worldObj, new GameProfile(entitylivingbase.getUniqueID(), ""));
							player.setCurrentItemOrArmor(0, new ItemStack(Items.golden_apple));
							((EntityZombie)entitylivingbase).interact(player);
							entitylivingbase.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 9999, 9, false, false));
							entitylivingbase.setCurrentItemOrArmor(0, new ItemStack(Item.getItemFromBlock(Blocks.red_flower)));
						}
						return false;
					}
					//end
					else
					{
						if (entitylivingbase instanceof EntityPlayer)
						{
							double d0 = AINearestAttackableTargetNonCreeper.this.getTargetDistance();

							if (entitylivingbase.isSneaking())
							{
								d0 *= 0.800000011920929D;
							}

							if (entitylivingbase.isInvisible())
							{
								float f = ((EntityPlayer)entitylivingbase).getArmorVisibility();

								if (f < 0.1F)
								{
									f = 0.1F;
								}

								d0 *= 0.7F * f;
							}

							if (entitylivingbase.getDistanceToEntity(creature) > d0)
							{
								return false;
							}
						}

						return AINearestAttackableTargetNonCreeper.this.isSuitableTarget(entitylivingbase, false);
					}
				}
				@Override
				public boolean apply(Object p_apply_1_)
				{
					return this.func_180096_a((EntityLivingBase)p_apply_1_);
				}
			};
		}
	}
}