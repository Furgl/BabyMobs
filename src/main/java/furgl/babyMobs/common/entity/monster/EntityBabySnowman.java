package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.entity.projectile.EntitySnowmanSnowball;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntitySnowball;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabySnowman extends EntitySnowman
{
	public EntityBabySnowman(World worldIn)
	{
		super(worldIn);
		this.setSize(0.35F, 0.95F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		((PathNavigateGround)this.getNavigator()).setCanSwim(true);
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.world.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }
	
	@Override
	protected boolean canDropLoot()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return ModEntities.getSpawnEgg(this.getClass());
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public void onLivingUpdate()
	{
		if (!this.world.isRemote)
		{
			int i, j, k;
			for (int l = 0; l < 4; ++l)
			{
				i = MathHelper.floor(this.posX + (l % 2 * 2 - 1) * 0.25F);
				j = MathHelper.floor(this.posY);
				k = MathHelper.floor(this.posZ + (l / 2 % 2 * 2 - 1) * 0.25F);

				//TODO ice
				if (Config.useSpecialAbilities)
				{
					if (this.world.getBlockState(new BlockPos(i, j-1, k)).getMaterial() == Material.WATER && this.getHealth() > 0)
						this.world.setBlockState(new BlockPos(i,j-1,k), Blocks.FROSTED_ICE.getDefaultState());
				}
				//end
				
				if (this.world.getBlockState(new BlockPos(i, j, k)).getMaterial() == Material.AIR/* && this.world.getBiomeGenForCoords(new BlockPos(i, 0, k)).getFloatTemperature(new BlockPos(i, j, k)) < 0.8F */&& Blocks.SNOW_LAYER.canPlaceBlockAt(this.world, new BlockPos(i, j, k)) && this.getHealth() > 0)
				{
					this.world.setBlockState(new BlockPos(i, j, k), Blocks.SNOW_LAYER.getDefaultState());
				}
			}
		}
		
		super.onLivingUpdate();
	}

	@Override
	public void attackEntityWithRangedAttack(EntityLivingBase p_82196_1_, float p_82196_2_)
	{
		//TODO slowness snowball (only the entity is replaced in orig)
		if (Config.useSpecialAbilities)
		{
			EntitySnowmanSnowball entitysnowmansnowball = new EntitySnowmanSnowball(this.world, this);
			double d0 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D;
			double d1 = p_82196_1_.posX - this.posX;
			double d2 = d0 - entitysnowmansnowball.posY;
			double d3 = p_82196_1_.posZ - this.posZ;
			float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
			entitysnowmansnowball.setThrowableHeading(d1, d2 + f1, d3, 1.6F, 12.0F);
			this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.spawnEntity(entitysnowmansnowball);
		}
		else
		{
			EntitySnowball entitysnowball = new EntitySnowball(this.world, this);
			double d0 = p_82196_1_.posY + p_82196_1_.getEyeHeight() - 1.100000023841858D;
			double d1 = p_82196_1_.posX - this.posX;
			double d2 = d0 - entitysnowball.posY;
			double d3 = p_82196_1_.posZ - this.posZ;
			float f1 = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
			entitysnowball.setThrowableHeading(d1, d2 + f1, d3, 1.6F, 12.0F);
			this.playSound(SoundEvents.ENTITY_SNOWMAN_SHOOT, 1.0F, 1.0F / (this.getRNG().nextFloat() * 0.4F + 0.8F));
			this.world.spawnEntity(entitysnowball);
		}
		//end
	}

	@Override
	public float getEyeHeight()
	{
		return 0.85F;
	}
}