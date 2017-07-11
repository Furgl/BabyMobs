package furgl.babyMobs.common.block;

import java.util.ArrayList;

import furgl.babyMobs.common.BabyMobs;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class ModBlocks 
{
	public static final Block DISAPPEARING_WEB = new BlockDisappearingWeb().setLightOpacity(1).setHardness(4.0F);

	public static ArrayList<Block> allBlocks = new ArrayList<Block>();

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {

		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			register(event.getRegistry(), DISAPPEARING_WEB, "disappearingWeb", false);
			Blocks.DRAGON_EGG.setCreativeTab(BabyMobs.tab);
		}

		private static void register(IForgeRegistry<Block> registry, Block block, String blockName, boolean addToTab) {
			allBlocks.add(block);
			block.setRegistryName(BabyMobs.MODID, blockName);
			block.setUnlocalizedName(block.getRegistryName().toString());
			if (addToTab)
				block.setCreativeTab(BabyMobs.tab);
			registry.register(block);
		}

	}

	public static void registerRenders() {
		for (Block block : allBlocks)
			registerRender(block);
	}

	public static void registerRender(Block block) {	
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register
		(Item.getItemFromBlock(block), 0, new ModelResourceLocation(BabyMobs.MODID + ":" + 
		block.getUnlocalizedName().substring(5), "inventory"));
	}
}
