package furgl.babyMobs.common.entity.projectile;

import java.util.Map;

import com.google.common.collect.Maps;

import furgl.babyMobs.common.entity.monster.EntityBabySkeleton;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

public class EntitySkeletonArrow extends EntityArrow
{
	private double red;
	private double green;
	private double blue;
	private PotionEffect potionEffect;
	private int effectType;

	public EntitySkeletonArrow(World world)
	{
		super(world);
	}

	public EntitySkeletonArrow(World world, EntityBabySkeleton shooter, EntityLivingBase target, float velocity, float inaccuracy) 
	{
		super(world, shooter, target, velocity, inaccuracy);
		this.effectType = shooter.effectType;
	}

	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(20, Integer.valueOf(0));//effectType
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();
		if (!this.worldObj.isRemote && this.ticksExisted == 1)
		{
			if (!this.getEntityData().hasKey("effectType"))
			{
				this.getEntityData().setInteger("effectType", this.effectType);
				this.effectType = this.getEntityData().getInteger("effectType");
				this.dataWatcher.updateObject(20, effectType);
			}
			else
			{
				this.effectType = this.getEntityData().getInteger("effectType");
				this.dataWatcher.updateObject(20, effectType);
			}
		}
		if (this.potionEffect == null && (this.effectType = this.dataWatcher.getWatchableObjectInt(20)) > 0)
		{
			if (this.effectType == 1)
				this.potionEffect = new PotionEffect(Potion.poison.id, 50, 0);
			else if (this.effectType == 2)
				this.potionEffect = new PotionEffect(Potion.blindness.id, 50, 0);
			else if (this.effectType == 3)
				this.potionEffect = new PotionEffect(Potion.wither.id, 50, 0);
			else if (this.effectType == 4)
				this.potionEffect = new PotionEffect(Potion.confusion.id, 100, 1);
			else if (this.effectType == 5)
				this.potionEffect = new PotionEffect(Potion.moveSlowdown.id, 50, 0);
			Map map = Maps.newHashMap();
			map.put(1, this.potionEffect);
			int i = PotionHelper.calcPotionLiquidColor(map.values());
			this.red = (i >> 16 & 255) / 255.0D;
			this.green = (i >> 8 & 255) / 255.0D;
			this.blue = (i >> 0 & 255) / 255.0D;
		}
		if (this.worldObj.isRemote)
		{
			this.worldObj.spawnParticle(EnumParticleTypes.SPELL_MOB, this.posX + (this.rand.nextDouble() - 0.5D) * this.width, this.posY + this.rand.nextDouble() * this.height, this.posZ + (this.rand.nextDouble() - 0.5D) * this.width, this.red, this.green, this.blue, new int[0]);
		}
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!this.worldObj.isRemote && !player.capabilities.isCreativeMode)
		{
			if (this.effectType == 1)
				this.potionEffect = new PotionEffect(Potion.poison.id, 50, 0);
			else if (this.effectType == 2)
				this.potionEffect = new PotionEffect(Potion.blindness.id, 50, 0);
			else if (this.effectType == 3)
				this.potionEffect = new PotionEffect(Potion.wither.id, 50, 0);
			else if (this.effectType == 4)
				this.potionEffect = new PotionEffect(Potion.confusion.id, 100, 1);
			else if (this.effectType == 5)
				this.potionEffect = new PotionEffect(Potion.moveSlowdown.id, 50, 0);
			else
				System.out.println("effectType == 0?!");
			player.addPotionEffect(this.potionEffect);
			NBTTagCompound nbt = new NBTTagCompound();
			this.writeEntityToNBT(nbt);
			if (nbt.getByte("inGround") == 1)				
				this.setDead();
		}
		super.onCollideWithPlayer(player);
	}
}
