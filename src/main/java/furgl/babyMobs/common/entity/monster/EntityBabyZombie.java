package furgl.babyMobs.common.entity.monster;

import java.util.Collections;
import java.util.List;

import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ai.EntityAIBabyFollowParent;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget.Sorter;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class EntityBabyZombie extends EntityZombie
{
	private Sorter theNearestAttackableTargetSorter;

	public EntityBabyZombie(World worldIn) {
		super(worldIn);
		this.setChild(true);
		this.tasks.addTask(5, new EntityAIBabyFollowParent(this, 1.1D, this.isAIEnabled()));
		this.theNearestAttackableTargetSorter = new EntityAINearestAttackableTarget.Sorter(this);
	}
	
	@Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_zombie_egg);
	}
	
	@Override
	public double getYOffset()
	{
		return super.getYOffset() + 0.1D;
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		
		if (Config.useSpecialAbilities && this.ticksExisted % 50 == 0 && !this.isRiding())
		{
			double d0 = 35.0D;
			List list = this.worldObj.getEntitiesWithinAABB(EntityChicken.class, this.boundingBox.expand(d0, 4.0D, d0));
			Collections.sort(list, this.theNearestAttackableTargetSorter);
			if (!list.isEmpty())
			{
				for (int i=0; i<list.size(); i++)
				{
					if (list.get(i).getClass().equals(EntityChicken.class))
					{
						this.setAttackTarget((EntityLivingBase) list.get(i));
						break;
					}
				}
			}
		}
		
		if (!this.isRiding() && this.getAttackTarget() != null && this.getAttackTarget() instanceof EntityChicken)
		{
			if (this.getAttackTarget().getClass().equals(EntityZombieChicken.class))
				this.setAttackTarget(null);
			else if (this.getAttackTarget().getClass().equals(EntityChicken.class))
			{
				this.setLastAttacker(this.getAttackTarget());
				this.getMoveHelper().setMoveTo(this.getAttackTarget().posX, this.getAttackTarget().posY, this.getAttackTarget().posZ, 1.0D);
				if (this.getDistanceToEntity(this.getAttackTarget()) < 1.0F)
					this.attackEntityAsMob(this.getAttackTarget());
			}
		}
	}

	@Override
	public void onKillEntity(EntityLivingBase entityLivingIn)
	{
		super.onKillEntity(entityLivingIn);
		if (entityLivingIn instanceof EntityChicken)
		{
			EntityZombieChicken entityzombiechicken = new EntityZombieChicken(this.worldObj);
			entityzombiechicken.copyLocationAndAnglesFrom(entityLivingIn);
			this.worldObj.removeEntity(entityLivingIn);
			((EntityChicken)entityLivingIn).setGrowingAge(-2000000);
			entityzombiechicken.onSpawnWithEgg((IEntityLivingData)null);
			this.worldObj.spawnEntityInWorld(entityzombiechicken);
			this.mountEntity(entityzombiechicken);
			entityzombiechicken.playLivingSound();
		}
	}
}
