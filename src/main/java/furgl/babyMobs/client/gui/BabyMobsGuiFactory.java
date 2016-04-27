package furgl.babyMobs.client.gui;

import java.util.Set;

import cpw.mods.fml.client.IModGuiFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

public class BabyMobsGuiFactory implements IModGuiFactory 
{
    @Override
	public void initialize(Minecraft minecraftInstance) 
    {
 
    }
 
    @Override
	public Class<? extends GuiScreen> mainConfigGuiClass() 
    {
        return BabyMobsGuiConfig.class;
    }
 
    @Override
	public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() 
    {
        return null;
    }
 
    @Override
	public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) 
    {
        return null;
    }
}