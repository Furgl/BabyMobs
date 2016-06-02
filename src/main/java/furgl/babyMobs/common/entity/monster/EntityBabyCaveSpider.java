package furgl.babyMobs.common.entity.monster;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.projectile.EntityCaveSpiderVenom;
import furgl.babyMobs.common.item.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCaveSpider;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyCaveSpider extends EntityCaveSpider
{
	private static final DataParameter<Byte> SPITTING = EntityDataManager.<Byte>createKey(EntityBabyCaveSpider.class, DataSerializers.BYTE);
	protected boolean spitting = false;
	protected int spittingCounter = 0;

	public EntityBabyCaveSpider(World worldIn)
	{
		super(worldIn);
		this.setSize(0.7F, 0.5F);
	}	

	@Override
	public void onDeath(DamageSource cause) //first achievement
	{
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
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
		return new ItemStack(ModItems.baby_cave_spider_egg);
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.30F;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.register(SPITTING, (byte)0);//added
	}

	@Override
	public void onUpdate()
	{
		//TODO venom spitting attack
		if (Config.useSpecialAbilities)
		{
			if (!this.worldObj.isRemote)
			{
				if (this.getAttackTarget() != null && this instanceof EntityBabyCaveSpider && !this.spitting && this.getHealth() > 0)
				{
					EntityLivingBase entitylivingbase = this.getAttackTarget();
					double d0 = this.getDistanceSqToEntity(entitylivingbase);

					if (d0 > 10.0D)
						this.dataWatcher.set(SPITTING, (byte)1);
					else
						this.dataWatcher.set(SPITTING, (byte)0);
				}
				else
					this.dataWatcher.set(SPITTING, (byte)0);
			}

			if (this.dataWatcher.get(SPITTING) == 1 && this.getAttackTarget() instanceof EntityPlayer && !(this.getAttackTarget() instanceof FakePlayer) && !(((EntityPlayer) this.getAttackTarget()).capabilities.isCreativeMode))
			{
				this.spitting = true;
				this.spittingCounter = 40;

				EntityLivingBase entitylivingbase = this.getAttackTarget();
				if (entitylivingbase != null && !this.worldObj.isRemote)
				{
					this.getLookHelper().setLookPosition(entitylivingbase.posX, entitylivingbase.posY+entitylivingbase.getEyeHeight()/2, entitylivingbase.posZ, 5F, 5F);			
					EntityCaveSpiderVenom venom = new EntityCaveSpiderVenom(this.worldObj, this);
					entitylivingbase.getEyeHeight();
					double d1 = entitylivingbase.posX - this.posX;
					double d3 = entitylivingbase.posZ - this.posZ;
					MathHelper.sqrt_double(d1 * d1 + d3 * d3);
					double x = entitylivingbase.posX - this.posX;
					double y = entitylivingbase.posY - this.posY;
					double z = entitylivingbase.posZ - this.posZ;
					venom.setThrowableHeading(x, y+2, z, 1.6F, 5.0F);
					//not working this.worldObj.playSound(this.posX + 0.5D, this.posY + 0.5D, this.posZ + 0.5D, SoundEvents.entity_generic_drink, this.getSoundCategory(), 1.0F, this.rand.nextFloat() * 0.1F + 1.7F, false);					
					this.worldObj.spawnEntityInWorld(venom);
				}
			}

			if (this.spitting)
			{
				this.spittingCounter--;
				this.addPotionEffect(new PotionEffect(MobEffects.moveSlowdown, 2, 9));
				if (this.spittingCounter == 0)
					this.spitting = false;
			}
		}
		//end

		super.onUpdate();
	}
}