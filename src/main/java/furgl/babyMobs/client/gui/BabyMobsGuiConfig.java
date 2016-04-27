package furgl.babyMobs.client.gui;

import java.util.ArrayList;
import java.util.List;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.config.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class BabyMobsGuiConfig extends GuiConfig 
{
	public BabyMobsGuiConfig(GuiScreen parent) 
	{
		super(parent, getConfigElements(), BabyMobs.MODID, false, false, "Baby Mobs Configuration");
	}

	private static List<IConfigElement> getConfigElements() {
        final List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(Config.config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        list.add(new ConfigElement(Config.config.getCategory("spawn rates".toLowerCase())));
        return list;
    }
}