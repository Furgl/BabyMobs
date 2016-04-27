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
	public static Achievement achievementWhyAreTheySoStrong;
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
		achievementWhyAreTheySoStrong = new Achievement("achievement.whyAreTheySoStrong", "whyAreTheySoStrong", 0, 0, Items.spawn_egg, null);
		achievementItsGettingHotInHere = new Achievement("achievement.itsGettingHotInHere", "itsGettingHotInHere", -1, 2, Items.blaze_powder, achievementWhyAreTheySoStrong);
		achievementLaserTag = new Achievement("achievement.laserTag", "laserTag", 2, 1, Items.ender_eye, achievementWhyAreTheySoStrong);
		achievementItsMine = new Achievement("achievement.itsMine", "itsMine", -3, 2, new ItemStack(Items.skull, 1, 1), achievementItsGettingHotInHere);
		achievementICantSee = new Achievement("achievement.iCantSee", "iCantSee", 2, -1, Items.dye, achievementWhyAreTheySoStrong);
		achievementBoomBaby = new Achievement("achievement.boomBaby", "boomBaby", -1, -2, Item.getItemFromBlock(Blocks.tnt), achievementWhyAreTheySoStrong);
		achievementAFlowerForMe = new Achievement("achievement.aFlowerForMe", "aFlowerForMe", -3, 0, Item.getItemFromBlock(Blocks.red_flower), achievementWhyAreTheySoStrong);
		achievementBetterLuckNextTime = new Achievement("achievement.betterLuckNextTime", "betterLuckNextTime", -3, 4, Items.nether_star, achievementItsMine);

		achievements.add(achievementWhyAreTheySoStrong);
		achievements.add(achievementItsGettingHotInHere);
		achievements.add(achievementLaserTag);
		achievements.add(achievementItsMine);
		achievements.add(achievementICantSee);
		achievements.add(achievementBoomBaby);
		achievements.add(achievementAFlowerForMe);
		achievements.add(achievementBetterLuckNextTime);

	}
}
