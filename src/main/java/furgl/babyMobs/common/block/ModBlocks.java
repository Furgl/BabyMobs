package furgl.babyMobs.common.block;

import cpw.mods.fml.common.registry.GameRegistry;
import furgl.babyMobs.common.BabyMobs;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class ModBlocks 
{
	public static Block disappearingWeb;

	public static void init()
	{
		disappearingWeb = registerBlockWithoutTab(new BlockDisappearingWeb().setLightOpacity(1).setHardness(4.0F), "disappearingWeb");
		Blocks.dragon_egg.setCreativeTab(BabyMobs.tab);
	}

	public static Block registerBlockWithTab(final Block block, final String unlocalizedName) {
		block.setBlockName(unlocalizedName);
		block.setCreativeTab(BabyMobs.tab);
		block.setBlockTextureName("babymobs:" + block.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(block, unlocalizedName);
		return block;
	}

	public static Block registerBlockWithoutTab(final Block block, final String unlocalizedName) {
		block.setBlockName(unlocalizedName);
		block.setBlockTextureName("babymobs:" + block.getUnlocalizedName().substring(5));
		GameRegistry.registerBlock(block, unlocalizedName);
		return block;
	}
}
