package furgl.babyMobs.common.entity.monster;

import com.mojang.authlib.GameProfile;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyDefendVillage;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.ai.EntityAIBabyLookAtVillager;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
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
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyIronGolem extends EntityGolem
{
	/** deincrements, and a distance-to-home check is done at 0 */
	private int homeCheckTimer;
	Village villageObj;
	private int attackTimer;
	private int holdRoseTick;

	public EntityBabyIronGolem(World p_i1694_1_)
	{
		super(p_i1694_1_);
		this.setSize(0.7F, 1.45F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D, this.isAIEnabled()));
		this.setHoldingRose(true);

		this.getNavigator().setAvoidsWater(true);
		this.tasks.addTask(1, new EntityAIAttackOnCollide(this, 1.0D, true));
		this.tasks.addTask(2, new EntityAIMoveTowardsTarget(this, 0.9D, 32.0F));
		this.tasks.addTask(3, new EntityAIMoveThroughVillage(this, 0.6D, true));
		this.tasks.addTask(4, new EntityAIMoveTowardsRestriction(this, 1.0D));
		this.tasks.addTask(5, new EntityAIBabyLookAtVillager(this));
		this.tasks.addTask(6, new EntityAIWander(this, 0.6D));
		this.tasks.addTask(7, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(8, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAIBabyDefendVillage(this));
		this.targetTasks.addTask(2, new EntityAIHurtByTarget(this, false));
		this.targetTasks.addTask(3, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, false, true, IMob.mobSelector));
	}
	
	//TODO sound and middle click
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
	//end

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(16, Byte.valueOf((byte)0));
	}

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	/**
	 * main AI tick function, replaces updateEntityActionState
	 */
	@Override
	protected void updateAITick()
	{
		if (--this.homeCheckTimer <= 0)
		{
			this.homeCheckTimer = 70 + this.rand.nextInt(50);
			this.villageObj = this.worldObj.villageCollectionObj.findNearestVillage(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY), MathHelper.floor_double(this.posZ), 32);

			if (this.villageObj == null)
			{
				this.detachHome();
			}
			else
			{
				ChunkCoordinates chunkcoordinates = this.villageObj.getCenter();
				this.setHomeArea(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, (int)(this.villageObj.getVillageRadius() * 0.6F));
			}
		}

		super.updateAITick();
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
			if (!this.worldObj.isRemote && this.rand.nextInt(10000) == 0 && this.worldObj.isAirBlock(this.serverPosX, this.serverPosY, this.serverPosZ) && this.worldObj.getBlock(this.serverPosX, this.serverPosY, this.serverPosZ) == Blocks.grass)
			{
				this.worldObj.setBlock(this.serverPosX, this.serverPosY, this.serverPosZ, Blocks.red_flower);
			}
		}
		//end  
		if (this.attackTimer > 0)
		{
			--this.attackTimer;
		}

		if (this.holdRoseTick > 0)
		{
			//	--this.holdRoseTick; TODO removed
		}

		if (this.motionX * this.motionX + this.motionZ * this.motionZ > 2.500000277905201E-7D && this.rand.nextInt(5) == 0)
		{
			int i = MathHelper.floor_double(this.posX);
			int j = MathHelper.floor_double(this.posY - 0.20000000298023224D - this.yOffset);
			int k = MathHelper.floor_double(this.posZ);
			Block block = this.worldObj.getBlock(i, j, k);

			if (block.getMaterial() != Material.air)
			{
				this.worldObj.spawnParticle("blockcrack_" + Block.getIdFromBlock(block) + "_" + this.worldObj.getBlockMetadata(i, j, k), this.posX + (this.rand.nextFloat() - 0.5D) * this.width, this.boundingBox.minY + 0.1D, this.posZ + (this.rand.nextFloat() - 0.5D) * this.width, 4.0D * (this.rand.nextFloat() - 0.5D), 0.5D, (this.rand.nextFloat() - 0.5D) * 4.0D);
			}
		}
	}

	/**
	 * Returns true if this entity can attack entities of the specified class.
	 */
	@Override
	public boolean canAttackClass(Class clazz)
	{
		return this.isPlayerCreated() && EntityPlayer.class.isAssignableFrom(clazz) ? false : super.canAttackClass(clazz);
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
	public boolean attackEntityAsMob(Entity entity)
	{
		this.attackTimer = 10;
		//TODO change zombie villagers
		if (Config.useSpecialAbilities && entity instanceof EntityZombie && ((EntityZombie)entity).isVillager())
		{
			EntityLivingBase entitylivingbase = (EntityLivingBase) entity;
			if (!((EntityZombie)entitylivingbase).isConverting())
			{
				entitylivingbase.addPotionEffect(new PotionEffect(Potion.weakness.id, 100, 0));
				FakePlayer player = new FakePlayer((WorldServer) entitylivingbase.worldObj, new GameProfile(entitylivingbase.getUniqueID(), ""));
				player.setCurrentItemOrArmor(0, new ItemStack(Items.golden_apple));
				((EntityZombie)entitylivingbase).interact(player);
				entitylivingbase.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, 9999, 9, false));
				entitylivingbase.setCurrentItemOrArmor(0, new ItemStack(Item.getItemFromBlock(Blocks.red_flower)));
			}
			this.setAttackTarget(null);this.numTicksToChaseTarget=0;
			return false;
		}
		//end
		else
		{
			this.worldObj.setEntityState(this, (byte)4);
			boolean flag = entity.attackEntityFrom(DamageSource.causeMobDamage(this), 7 + this.rand.nextInt(15));

			if (flag)
			{
				entity.motionY += 0.4000000059604645D;
			}

			this.playSound("mob.irongolem.throw", 1.0F, 1.0F);
			return flag;
		}
		
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
		this.holdRoseTick = p_70851_1_ ? 400 : 0;
		//this.worldObj.setEntityState(this, (byte)11); TODO removed
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

	protected void playStepSound(int x, int y, int z, Block blockIn)
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
			this.func_145778_a(Item.getItemFromBlock(Blocks.red_flower), 1, 0.0F);
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

	/**
	 * Called when the mob's health reaches 0.
	 */
	@Override
	public void onDeath(DamageSource p_70645_1_)
	{
		if (!this.isPlayerCreated() && this.attackingPlayer != null && this.villageObj != null)
		{
			this.villageObj.setReputationForPlayer(this.attackingPlayer.getCommandSenderName(), -5);
		}

		super.onDeath(p_70645_1_);
	}
}