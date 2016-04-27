package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntitySquidInk extends EntityWitherSkeletonSmoke
{
	public EntitySquidInk(World world)
	{
		super(world);
	}

	public EntitySquidInk(World worldObj, EntityLivingBase entitylivingbase, int particlesPerTick) 
	{
		super(worldObj, entitylivingbase, particlesPerTick);
	}

	/**Used by EntitySpawner*/
	public EntitySquidInk(World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		super(world, x, y, z, spawner, heightIterator, entityIterator);
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!this.worldObj.isRemote)
		{
			if (!player.capabilities.isCreativeMode)
			{
				player.triggerAchievement(Achievements.achievementICantSee);
				player.addPotionEffect(new PotionEffect(Potion.blindness.id, 60, 1));
			}
		}
	}
}
