package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.projectile.EntitySnowmanSnowball;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIArrowAttack;
import net.minecraft.entity.ai.EntityAILookIdle;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.monster.EntityGolem;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBabySnowman extends EntityGolem implements IRangedAttackMob
{

	public EntityBabySnowman(World p_i1692_1_)
	{
		super(p_i1692_1_);
		this.setSize(0.35F, 0.95F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D, this.isAIEnabled()));
		this.getNavigator().setAvoidsWater(false);

		this.tasks.addTask(1, new EntityAIArrowAttack(this, 1.25D, 20, 10.0F));
		this.tasks.addTask(2, new EntityAIWander(this, 1.0D));
		this.tasks.addTask(3, new EntityAIWatchClosest(this, EntityPlayer.class, 6.0F));
		this.tasks.addTask(4, new EntityAILookIdle(this));
		this.targetTasks.addTask(1, new EntityAINearestAttackableTarget(this, EntityLiving.class, 0, true, false, IMob.mobSelector));
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
		return new ItemStack(ModItems.baby_snowman_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}
	//end

	/**
	 * Returns true if the newer Entity AI code should be run
	 */
	@Override
	public boolean isAIEnabled()
	{
		return true;
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(4.0D);
		this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
	}

	/**
	 * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
	 * use this to react to sunlight and start to burn.
	 */
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		int i = MathHelper.floor_double(this.posX);
		int j = MathHelper.floor_double(this.posY);
		int k = MathHelper.floor_double(this.posZ);

		if (this.isWet())
		{
			this.attackEntityFrom(DamageSource.drown, 1.0F);
		}

		if (this.worldObj.getBiomeGenForCoords(i, k).getFloatTemperature(i, j, k) > 1.0F)
		{
			this.attackEntityFrom(DamageSource.onFire, 1.0F);
		}

		for (int l = 0; l < 4; ++l)
		{
			i = MathHelper.floor_double(this.posX + (l % 2 * 2 - 1) * 0.25F);
			j = MathHelper.floor_double(this.posY);
			k = MathHelper.floor_double(this.posZ + (l / 2 % 2 * 2 - 1) * 0.25F);

			//TODO ice
			if (Config.useSpecialAbilities)
			{
				if (this.worldObj.getBlock(i, j-1, k).getMaterial() == Material.water/* && this.worldObj.getBiomeGenForCoords(i, k).getFloatTemperature(i, j-1, k) < 0.8F*/ && this.getHealth() > 0)
				{
					this.worldObj.setBlock(i,j-1,k, Blocks.ice);
				}
			}
			//end

			if (this.worldObj.getBlock(i, j, k).getMaterial() == Material.air/* && this.worldObj.getBiomeGenForCoords(i, k).getFloatTemperature(i, j, k) < 0.8F*/ && Blocks.snow_layer.canPlaceBlockAt(this.worldObj, i, j, k) && this.getHealth() > 0)
			{
				this.worldObj.setBlock(i, j, k, Blocks.snow_layer);
			}
		}
	}

	@Override
	protected Item getDropItem()
	{
		return Items.snowball;
	}

	/**
	 * Drop 0-2 items of this living's type
	 */
	@Override
	protected void dropFewItems(boolean p_70628_1_, int p_70628_2_)
	{
		int j = this.rand.nextInt(16);

		for (int k = 0; k < j; ++k)
		{
			this.dropItem(Items.snowball, 1);
		}
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
     * Returns the sound this mob makes when it is hurt.
     */
    @Override
	protected String getHurtSound()
    {
        return null;
    }

    /**
     * Returns the sound this mob makes on death.
     */
    @Override
	protected String getDeathSound()
    {
        return null;
    }
	
	/**
	 * Attack the specified entity using a ranged attack.
	 */
	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
	{
		//TODO slowness snowball (only the entity is replaced in orig)
		if (Config.useSpecialAbilities)
		{
			EntitySnowmanSnowball entitysnowmansnowball = new EntitySnowmanSnowball(this.worldObj, this);
			double d0 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D;
			double d1 = p_82196_1_.posX - this.posX;
			double d2 = d0 - entitysnowmansnowball.posY;
			double d3 = p_82196_1_.posZ - this.posZ;
			float f1 = MathHelper.sqrt_double(d1 * d1 + d3 * d3) * 0.2F;
			entitysnowmansnowball.setThrowableHeading(d1, d2 + f1, d3, 1.6F, 12.0F);
			this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.worldObj.spawnEntityInWorld(entitysnowmansnowball);
		}
		else
		{
			EntitySnowball entitysnowball = new EntitySnowball(this.worldObj, this);
			double d0 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D;
			double d1 = p_82196_1_.posX - this.posX;
			double d2 = d0 - entitysnowball.posY;
			double d3 = p_82196_1_.posZ - this.posZ;
			float f1 = MathHelper.sqrt_double(d1 * d1 + d3 * d3) * 0.2F;
			entitysnowball.setThrowableHeading(d1, d2 + f1, d3, 1.6F, 12.0F);
			this.playSound("random.bow", 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.worldObj.spawnEntityInWorld(entitysnowball);
		}
		//end
	}
}