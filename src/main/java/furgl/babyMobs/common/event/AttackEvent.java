package furgl.babyMobs.common.event;

import java.lang.reflect.Method;

import furgl.babyMobs.common.block.ModBlocks;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.monster.EntityBabyCaveSpider;
import furgl.babyMobs.common.entity.monster.EntityBabySpider;
import furgl.babyMobs.common.entity.monster.EntityZombieChicken;
import furgl.babyMobs.common.entity.monster.EntityZombiePig;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityZombie;
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
		if (!event.getEntity().worldObj.isRemote && event.getSource().getEntity() instanceof EntityBabySpider || event.getSource().getEntity() instanceof EntityBabyCaveSpider) {
			if (Config.useSpecialAbilities && event.getEntityLiving() instanceof EntityPlayer)
			{
				BlockPos pos = new BlockPos(event.getEntityLiving().posX, event.getEntityLiving().posY, event.getEntityLiving().posZ);
				if (event.getEntityLiving().worldObj.rand.nextInt(5) == 0 && event.getEntityLiving().worldObj.isAirBlock(pos) && event.getEntityLiving().getDistanceSqToEntity(event.getSource().getEntity()) < 80D)
					event.getEntityLiving().worldObj.setBlockState(pos, ModBlocks.disappearingWeb.getDefaultState());
			}
		}//convert Zombie
		else if (!event.getEntity().worldObj.isRemote && event.getSource().getEntity() instanceof EntityIronGolem && event.getEntityLiving() instanceof EntityZombie) {
			EntityZombie zombie = (EntityZombie) event.getEntityLiving();
			if (Config.useSpecialAbilities && zombie.isVillager() && !zombie.isConverting()) {
				zombie.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 0));
				try {
					Method method = ReflectionHelper.findMethod(EntityZombie.class, zombie, new String[] {"startConversion", "func_82228_a"}, int.class);//EntityZombie.class.getDeclaredMethod("startConversion", int.class);
					method.setAccessible(true);
					method.invoke(zombie, 200);
				} catch(Exception e) {
					e.printStackTrace();
				}
				zombie.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 9999, 9, false, false));
				zombie.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER)));
				event.setCanceled(true);
			}
		}//convert ZombiePig
		else if (!event.getEntity().worldObj.isRemote && event.getSource().getEntity() instanceof EntityIronGolem && event.getEntityLiving() instanceof EntityZombiePig) {
			EntityZombiePig pig = (EntityZombiePig) event.getEntityLiving();
			if (Config.useSpecialAbilities && !pig.isConverting()) {
				pig.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 0));
				pig.startConversion(200);
				pig.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 9999, 9, false, false));
				pig.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER)));
				event.setCanceled(true);
			}
		}//convert ZombieChicken
		else if (!event.getEntity().worldObj.isRemote && event.getSource().getEntity() instanceof EntityIronGolem && event.getEntityLiving() instanceof EntityZombieChicken) {
			EntityZombieChicken chicken = (EntityZombieChicken) event.getEntityLiving();
			if (Config.useSpecialAbilities && !chicken.isConverting()) {
				chicken.addPotionEffect(new PotionEffect(MobEffects.WEAKNESS, 100, 0));
				chicken.startConversion(200);
				chicken.addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 9999, 9, false, false));
				chicken.setHeldItem(EnumHand.MAIN_HAND, new ItemStack(Item.getItemFromBlock(Blocks.RED_FLOWER)));
				event.setCanceled(true);
			}
		}
		else if (!event.getEntity().worldObj.isRemote && event.getSource().getEntity() instanceof EntityZombie && ((EntityZombie)event.getSource().getEntity()).isConverting())
			event.setCanceled(true);
		else if (!event.getEntity().worldObj.isRemote && event.getSource().getEntity() instanceof EntityZombieChicken && ((EntityZombieChicken)event.getSource().getEntity()).isConverting())
			event.setCanceled(true);
		else if (!event.getEntity().worldObj.isRemote && event.getSource().getEntity() instanceof EntityZombiePig && ((EntityZombiePig)event.getSource().getEntity()).isConverting())
			event.setCanceled(true);
	}
}
