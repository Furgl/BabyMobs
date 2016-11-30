package furgl.babyMobs.common.event;

import java.lang.reflect.Method;

import net.minecraft.block.Block;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class InteractHorseEvent 
{
	@SubscribeEvent(priority=EventPriority.NORMAL, receiveCanceled=true)
	public void onEvent(EntityInteract event) {
		if (/*!event.getTarget().worldObj.isRemote && */event.getTarget() instanceof EntityHorse && ((EntityHorse)event.getTarget()).getType().isUndead() && event.getItemStack() != null) 
		{
			ItemStack stack = event.getItemStack();
			EntityHorse horse = (EntityHorse) event.getTarget();
			boolean flag = false;
			float f = 0.0F;
			int i = 0;
			int j = 0;

			if (stack.getItem() == Items.WHEAT)
			{
				f = 2.0F;
				i = 20;
				j = 3;
			}
			else if (stack.getItem() == Items.SUGAR)
			{
				f = 1.0F;
				i = 30;
				j = 3;
			}
			else if (Block.getBlockFromItem(stack.getItem()) == Blocks.HAY_BLOCK)
			{
				f = 20.0F;
				i = 180;
			}
			else if (stack.getItem() == Items.APPLE)
			{
				f = 3.0F;
				i = 60;
				j = 3;
			}
			else if (stack.getItem() == Items.GOLDEN_CARROT)
			{
				f = 4.0F;
				i = 60;
				j = 5;

				if (horse.isTame() && (horse.getGrowingAge() == 0 || horse.getGrowingAge() == 1))
				{
					flag = true;
					horse.setInLove(event.getEntityPlayer());
				}
			}
			else if (stack.getItem() == Items.GOLDEN_APPLE)
			{
				f = 10.0F;
				i = 240;
				j = 10;

				if (horse.isTame() && horse.getGrowingAge() == 0 && !horse.isInLove())
				{
					flag = true;
					horse.setInLove(event.getEntityPlayer());
				}
			}

			if (horse.getHealth() < horse.getMaxHealth() && f > 0.0F)
			{
				horse.heal(f);
				flag = true;
			}

			if (!horse.isAdultHorse() && i > 0)
			{
				if (!horse.worldObj.isRemote)
				{
					horse.addGrowth(i);
				}

				flag = true;
			}

			if (j > 0 && (flag || !horse.isTame()) && horse.getTemper() < horse.getMaxTemper())
			{
				flag = true;

				if (!horse.worldObj.isRemote)
				{
					horse.increaseTemper(j);
				}
			}

			if (flag)
			{
				try {//not sure what use this whole method has, so w/e
					Method method = horse.getClass().getDeclaredMethod("func_110266_cB");
					method.setAccessible(true);
					method.invoke(horse);
				}
				catch (Exception e)
				{
				}
				event.setCanceled(true);
				horse.setHorseTamed(false);
				if (!event.getEntityPlayer().capabilities.isCreativeMode)
                    --stack.stackSize;
			}
		}
	}
}
