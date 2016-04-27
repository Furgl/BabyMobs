package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyCaveSpider extends EntityBabySpider
{

    public EntityBabyCaveSpider(World worldIn)
    {
        super(worldIn);
        this.setSize(0.7F, 0.5F);
    }	
    
	@Override
	public void onDeath(DamageSource cause) //first achievement
    {
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).triggerAchievement(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
    }
    
    @Override
	public ItemStack getPickedResult(MovingObjectPosition target)
	{
		return new ItemStack(ModItems.baby_cave_spider_egg);
	}

    @Override
	protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(12.0D);
    }

    @Override
	public boolean attackEntityAsMob(Entity p_70652_1_)
    {
        if (super.attackEntityAsMob(p_70652_1_))
        {
            if (p_70652_1_ instanceof EntityLivingBase)
            {
                byte b0 = 0;

                if (this.worldObj.getDifficulty() == EnumDifficulty.NORMAL)
                {
                    b0 = 7;
                }
                else if (this.worldObj.getDifficulty() == EnumDifficulty.HARD)
                {
                    b0 = 15;
                }

                if (b0 > 0)
                {
                    ((EntityLivingBase)p_70652_1_).addPotionEffect(new PotionEffect(Potion.poison.id, b0 * 20, 0));
                }
            }

            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
	public IEntityLivingData onInitialSpawn(DifficultyInstance p_180482_1_, IEntityLivingData p_180482_2_)
    {
        return p_180482_2_;
    }

    @Override
	public float getEyeHeight()
    {
        return 0.45F;
    }
}