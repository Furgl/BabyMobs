package furgl.babyMobs.client.gui;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

public class BabyMobsGuiConfig extends GuiConfig 
{
	public BabyMobsGuiConfig(GuiScreen parent) 
	{
		super(parent, getConfigElements(), BabyMobs.MODID, false, false, "Baby Mobs Configuration");
	}

	private static List getConfigElements() {
        final List list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(Config.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        list.add(new ConfigElement(Config.config.getCategory("spawn rates".toLowerCase())));
        return list;
    }
}