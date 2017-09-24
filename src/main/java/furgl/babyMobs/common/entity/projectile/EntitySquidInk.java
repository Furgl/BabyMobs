package furgl.babyMobs.common.entity.projectile;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.util.EntitySpawner;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

public class EntitySquidInk extends EntityWitherSkeletonSmoke
{
	public EntitySquidInk(World world)
	{
		super(world);
	}

	public EntitySquidInk(World world, EntityLivingBase entitylivingbase, int particlesPerTick) 
	{
		super(world, entitylivingbase, particlesPerTick);
	}

	/**Used by EntitySpawner*/
	public EntitySquidInk(World world, double x, double y, double z, EntitySpawner spawner, int heightIterator, int entityIterator)
	{
		super(world, x, y, z, spawner, heightIterator, entityIterator);
	}

	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (!this.world.isRemote)
		{
			if (!player.capabilities.isCreativeMode)
			{
				player.addStat(Achievements.achievementICantSee);
				player.addPotionEffect(new PotionEffect(MobEffects.BLINDNESS, 60, 1));
			}
		}
	}
}
