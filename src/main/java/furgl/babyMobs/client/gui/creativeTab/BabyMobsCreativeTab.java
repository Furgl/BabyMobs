package furgl.babyMobs.client.gui.creativeTab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BabyMobsCreativeTab extends CreativeTabs 
{
	public BabyMobsCreativeTab(String label) 
	{
		super(label);
	}

	@Override
	public Item getTabIconItem() {
		return Items.SPAWN_EGG;
	}
	
}

