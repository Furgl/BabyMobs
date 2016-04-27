package furgl.babyMobs.creativetab;

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
		return Items.spawn_egg;
	}
	
}

