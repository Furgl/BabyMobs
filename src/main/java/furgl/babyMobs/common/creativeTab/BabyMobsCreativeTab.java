package furgl.babyMobs.common.creativeTab;

import java.util.ArrayList;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BabyMobsCreativeTab extends CreativeTabs 
{
	public ArrayList<ItemStack> eggs = new ArrayList<ItemStack>();
	
	public BabyMobsCreativeTab(String label) 
	{
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Items.SPAWN_EGG);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void displayAllRelevantItems(NonNullList<ItemStack> list)
    {
		super.displayAllRelevantItems(list);
		list.addAll(eggs);
    }
}

