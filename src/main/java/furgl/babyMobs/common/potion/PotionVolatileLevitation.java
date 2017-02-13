package furgl.babyMobs.common.potion;

import java.util.Random;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.packet.PacketMotionY;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PotionVolatileLevitation extends Potion 
{
	private Random rand = new Random();
	private int level;

	public PotionVolatileLevitation(boolean isBadEffectIn, int liquidColorIn) {
		super(isBadEffectIn, liquidColorIn);
	}

	public Potion setIconIndex(int par1, int par2) {
		super.setIconIndex(par1, par2);
		return this;
	}

	@Override
	public boolean isReady(int p_76397_1_, int p_76397_2_)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderInventoryEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc) 
	{ 
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(BabyMobs.MODID+":textures/effects/volatile_levitation.png"));
		Minecraft.getMinecraft().currentScreen.drawTexturedModalRect(x+6, y+8, 0, 0, 18, 18);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void renderHUDEffect(int x, int y, PotionEffect effect, net.minecraft.client.Minecraft mc, float alpha) 
	{ 
		Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(BabyMobs.MODID+":textures/effects/volatile_levitation.png"));
		Minecraft.getMinecraft().ingameGUI.drawTexturedModalRect(x+3, y+4, 0, 0, 18, 18);
	}

	@Override
	public void performEffect(EntityLivingBase entityLivingBaseIn, int amplifier)
	{
		if (entityLivingBaseIn.ticksExisted % 10 == 0) {
			if (rand.nextInt(2) == 0) 
				level = rand.nextInt(6) + 2;
			else {
				if (level > 0)
					entityLivingBaseIn.fallDistance = 0.0F; //negate op fall damage
				level = -1; //fall
			}
		}
		if (level > 0) {
			if (entityLivingBaseIn instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entityLivingBaseIn;
				if (!player.isElytraFlying() && !player.isInWater() && !player.isInLava() && !player.capabilities.isFlying && player.canPassengerSteer())
				{
					player.motionY += (0.05D * (double)(level + amplifier) - player.motionY) * 0.8D;
					if (player instanceof EntityPlayerMP)
						BabyMobs.network.sendTo(new PacketMotionY(player.motionY), (EntityPlayerMP) player);
				}
			}
			else
				entityLivingBaseIn.motionY += (0.05D * (double)(level + amplifier) - entityLivingBaseIn.motionY) * 0.8D;
		}
	}
}