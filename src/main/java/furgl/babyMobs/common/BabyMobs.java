package furgl.babyMobs.common;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.block.ModBlocks;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.creativeTab.BabyMobsCreativeTab;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.event.AttackEvent;
import furgl.babyMobs.common.event.BabyReplaceEvent;
import furgl.babyMobs.common.event.BlockPatternEvent;
import furgl.babyMobs.common.event.BreakSpeedEvent;
import furgl.babyMobs.common.event.ConfigChangeEvent;
import furgl.babyMobs.common.event.OnUpdateEvent;
import furgl.babyMobs.common.event.SetAttackTargetEvent;
import furgl.babyMobs.common.event.SpawnDragonEvent;
import furgl.babyMobs.common.event.SummonCommandEvent;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.common.packet.PacketMotionY;
import furgl.babyMobs.common.packet.PacketVolatileLevitation;
import furgl.babyMobs.common.potion.ModPotions;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BabyMobs.MODID, name = BabyMobs.MODNAME, version = BabyMobs.VERSION, guiFactory = "furgl.babyMobs.client.gui.config.BabyMobsGuiFactory", updateJSON = "https://raw.githubusercontent.com/Furgl/BabyMobs/1.11.2/update.json")
public class BabyMobs
{
	public static final String MODID = "babymobs";
	public static final String MODNAME = "Baby Mobs";
	public static final String VERSION = "1.5.4";
	@Mod.Instance(MODID)
	public static BabyMobs instance;
	@SidedProxy(clientSide = "furgl.babyMobs.client.ClientProxy", serverSide = "furgl.babyMobs.common.CommonProxy")
	public static CommonProxy proxy;
	public static final BabyMobsCreativeTab tab = new BabyMobsCreativeTab("tabBabyMobs");
	public static SimpleNetworkWrapper network;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		network = NetworkRegistry.INSTANCE.newSimpleChannel("babyMobsChannel");
		registerPackets();
		Config.init(event.getSuggestedConfigurationFile());		
		ModEntities.registerEntities();
		ModItems.init();
		ModBlocks.init();
		Achievements.init();
		ModPotions.preInit();
		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		registerEventListeners();
		registerCraftingRecipes();
		ModPotions.init();
		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		proxy.postInit();
	}

	private void registerEventListeners()
	{
		MinecraftForge.EVENT_BUS.register(new BabyReplaceEvent());
		MinecraftForge.EVENT_BUS.register(new SpawnDragonEvent());
		MinecraftForge.EVENT_BUS.register(new BlockPatternEvent());
		MinecraftForge.EVENT_BUS.register(new BreakSpeedEvent());
		MinecraftForge.EVENT_BUS.register(new SummonCommandEvent());
		MinecraftForge.EVENT_BUS.register(new ConfigChangeEvent());
		MinecraftForge.EVENT_BUS.register(new OnUpdateEvent());
		MinecraftForge.EVENT_BUS.register(new AttackEvent());
		MinecraftForge.EVENT_BUS.register(new SetAttackTargetEvent());
	}

	private void registerPackets()
	{
		int id = 0;
		network.registerMessage(PacketVolatileLevitation.Handler.class, PacketVolatileLevitation.class, id++, Side.CLIENT);
		network.registerMessage(PacketMotionY.Handler.class, PacketMotionY.class, id++, Side.CLIENT);
	}

	private void registerCraftingRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(ModItems.golden_bread), "NNN", "NBN", "NNN", 'N', Items.GOLD_NUGGET, 'B', Items.BREAD);
	}
}
