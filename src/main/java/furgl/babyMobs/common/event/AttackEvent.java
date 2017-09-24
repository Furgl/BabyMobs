package furgl.babyMobs.common.event;

import java.lang.reflect.Method;
import java.util.UUID;

import furgl.babyMobs.common.block.ModBlocks;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.monster.EntityBabyCaveSpider;
import furgl.babyMobs.common.entity.monster.EntityBabySpider;
import furgl.babyMobs.common.entity.monster.EntityZombieChicken;
import furgl.babyMobs.common.entity.monster.EntityZombiePig;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.EntityZombieVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public class AttackEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(LivingAttackEvent event) {//spawn disappearing web
		if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityBabySpider || event.getSource().getTrueSource() instanceof EntityBabyCaveSpider) {
			if (Config.useSpecialAbilities && event.getEntityLiving() instanceof EntityPlayer)
			{
				BlockPos pos = new BlockPos(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ);
				if (event.getEntityLiving().world.rand.nextInt(5) == 0 && event.getEntityLiving().world.isAirBlock(pos) && event.getEntityLiving().getDistanceSqToEntity(event.getSource().getTrueSource()) < 80D)
					event.getEntityLiving().world.setBlockState(pos, ModBlocks.DISAPPEARING_WEB.getDefaultState());
			}
		}//convert Zombie
		else if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityIronGolem && event.getEntityLiving() instanceof EntityZombie) {
			EntityZombie zombie = (EntityZombie) event.getEntityLiving();
			if (Config.useSpecialAbilities && zombie instanceof EntityZombieVillager && !((EntityZombieVillager) zombie).isConverting()) {
				zombie.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 0));
				try {
					Method method = ReflectionHelper.findMethod(EntityZombieVillager.class, "func_191991_a", null, UUID.class, int.class);//EntityZombie.class.getDeclaredMethod("startConversion", int.class);
					method.setAccessible(true);
					method.invoke(zombie, zombie.getPersistentID(), 200);
				} catch(Exception e) {
					e.printStackTrace();
				}
				zombie.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 9999, 9, false, false));
				zombie.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER)));
				event.setCanceled(true);
			}
		}//convert ZombiePig
		else if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityIronGolem && event.getEntityLiving() instanceof EntityZombiePig) {
			EntityZombiePig pig = (EntityZombiePig) event.getEntityLiving();
			if (Config.useSpecialAbilities && !pig.isConverting()) {
				pig.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 0));
				pig.startConversion(200);
				pig.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 9999, 9, false, false));
				pig.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER)));
				event.setCanceled(true);
			}
		}//convert ZombieChicken
		else if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityIronGolem && event.getEntityLiving() instanceof EntityZombieChicken) {
			EntityZombieChicken chicken = (EntityZombieChicken) event.getEntityLiving();
			if (Config.useSpecialAbilities && !chicken.isConverting()) {
				chicken.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 0));
				chicken.startConversion(200);
				chicken.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 9999, 9, false, false));
				chicken.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER)));
				event.setCanceled(true);
			}
		}
		else if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityZombieVillager && ((EntityZombieVillager)event.getSource().getTrueSource()).isConverting())
			event.setCanceled(true);
		else if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityZombieChicken && ((EntityZombieChicken)event.getSource().getTrueSource()).isConverting())
			event.setCanceled(true);
		else if (!event.getEntity().world.isRemote && event.getSource().getTrueSource() instanceof EntityZombiePig && ((EntityZombiePig)event.getSource().getTrueSource()).isConverting())
			event.setCanceled(true);
	}
}
