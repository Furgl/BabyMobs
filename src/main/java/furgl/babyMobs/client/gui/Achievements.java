package furgl.babyMobs.client.gui;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

public class Achievements 
{
	public static Achievement achievementItsGettingHotInHere;
	public static Achievement achievementLaserTag;
	public static Achievement achievementItsMine;
	public static Achievement achievementICantSee;
	public static Achievement achievementBoomBaby;
	public static Achievement achievementAFlowerForMe;
	public static Achievement achievementBetterLuckNextTime;

	public static List<Achievement> achievements = new ArrayList<Achievement>();
	
	public static void init()
	{
		achievementItsGettingHotInHere = new Achievement("achievement.itsGettingHotInHere", "itsGettingHotInHere", 0, 0, Items.blaze_powder, null);
		achievementLaserTag = new Achievement("achievement.laserTag", "laserTag", 0, 1, Items.ender_eye, null);
		achievementItsMine = new Achievement("achievement.itsMine", "itsMine", 0, 2, new ItemStack(Items.skull, 1, 1), null);
		achievementICantSee = new Achievement("achievement.iCantSee", "iCantSee", 1, 0, Items.dye, null);
		achievementBoomBaby = new Achievement("achievement.boomBaby", "boomBaby", 1, 1, Item.getItemFromBlock(Blocks.tnt), null);
		achievementAFlowerForMe = new Achievement("achievement.aFlowerForMe", "aFlowerForMe", 1, 2, Item.getItemFromBlock(Blocks.red_flower), null);
		achievementBetterLuckNextTime = new Achievement("achievement.betterLuckNextTime", "betterLuckNextTime", 2, 0, Items.nether_star, null);

		achievements.add(achievementItsGettingHotInHere);
		achievements.add(achievementLaserTag);
		achievements.add(achievementItsMine);
		achievements.add(achievementICantSee);
		achievements.add(achievementBoomBaby);
		achievements.add(achievementAFlowerForMe);
		achievements.add(achievementBetterLuckNextTime);
	}
}
