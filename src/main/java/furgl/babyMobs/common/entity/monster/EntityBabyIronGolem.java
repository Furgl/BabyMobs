package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockGrass;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class EntityBabyIronGolem extends EntityIronGolem
{
	public EntityBabyIronGolem(World worldIn)
	{
		super(worldIn);
		this.setSize(0.7F, 1.45F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D));
		this.setHoldingRose(true);
	}	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
//		if (!this.world.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
//			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);

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
	public float getEyeHeight()
	{
		return 1.3F;
	}
	
	@Override
	 protected void collideWithEntity(Entity entityIn)
    {
        if (entityIn instanceof EntityZombiePig || entityIn instanceof EntityZombieChicken)
            this.setAttackTarget((EntityLivingBase)entityIn);

        super.collideWithEntity(entityIn);
    }
	
	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		this.setHoldingRose(true);
		//TODO plant roses
		if (this.ticksExisted % 50 == 0)
		{
			//EntityPlayer player = this.world.getClosestPlayerToEntity(this, 10D);
			//if (player != null)
				//player.addStat(Achievements.achievementAFlowerForMe);
		}
		if (Config.useSpecialAbilities)
			if (!this.world.isRemote && this.rand.nextInt(1000) == 0 && this.world.isAirBlock(new BlockPos(this)) && (this.world.getBlockState(new BlockPos(this).down()).getBlock() instanceof BlockGrass || this.world.getBlockState(new BlockPos(this).down()).getBlock() instanceof BlockDirt))
				this.world.setBlockState(new BlockPos(this), Blocks.RED_FLOWER.getDefaultState());
		//end    
	}
}