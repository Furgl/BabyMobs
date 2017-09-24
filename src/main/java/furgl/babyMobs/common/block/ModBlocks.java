package furgl.babyMobs.common.block;

import furgl.babyMobs.common.BabyMobs;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks 
{
	public static Block disappearingWeb;

	public static void init()
	{
		disappearingWeb = registerBlockWithoutTab(new BlockDisappearingWeb().setLightOpacity(1).setHardness(4.0F), "disappearingWeb");
		Blocks.DRAGON_EGG.setCreativeTab(BabyMobs.tab);
	}

	public static void registerRenders() 
	{
		registerRender(disappearingWeb);
	}

	public static Block registerBlockWithTab(final Block block, final String unlocalizedName) {
		block.setUnlocalizedName(unlocalizedName);
		block.setCreativeTab(BabyMobs.tab);
		GameRegistry.register(block.setRegistryName(unlocalizedName));
		return block;
	}

	public static Block registerBlockWithoutTab(final Block block, final String unlocalizedName) {
		block.setUnlocalizedName(unlocalizedName);
		GameRegistry.register(block.setRegistryName(unlocalizedName));
		return block;
	}

	public static void registerRender(Block block)
	{	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(block), 0, new ModelResourceLocation("babymobs:" + block.getUnlocalizedName().substring(5), "inventory"));
	}
}
