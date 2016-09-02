package furgl.babyMobs.common;

import java.lang.reflect.Field;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.client.gui.creativeTab.BabyMobsCreativeTab;
import furgl.babyMobs.common.block.ModBlocks;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.event.AttackEvent;
import furgl.babyMobs.common.event.BabyReplaceEvent;
import furgl.babyMobs.common.event.BlockPatternEvent;
import furgl.babyMobs.common.event.BreakSpeedEvent;
import furgl.babyMobs.common.event.ConfigChangeEvent;
import furgl.babyMobs.common.event.InteractHorseEvent;
import furgl.babyMobs.common.event.OnUpdateEvent;
import furgl.babyMobs.common.event.SetAttackTargetEvent;
import furgl.babyMobs.common.event.SpawnDragonEvent;
import furgl.babyMobs.common.event.SummonCommandEvent;
import furgl.babyMobs.common.event.ZombieTrapEvent;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.common.item.spawnEgg.ItemBabySpawnEgg;
import furgl.babyMobs.common.packet.PacketMotionY;
import furgl.babyMobs.common.packet.PacketVolatileLevitation;
import furgl.babyMobs.common.potion.ModPotions;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
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

@Mod(modid = BabyMobs.MODID, name = BabyMobs.MODNAME, version = BabyMobs.VERSION, guiFactory = "furgl.babyMobs.client.gui.config.BabyMobsGuiFactory")
public class BabyMobs
{
	public static final String MODID = "babymobs";
	public static final String MODNAME = "Baby Mobs";
	public static final String VERSION = "1.5";
	@Mod.Instance("babymobs")
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
		registerDispenserBehaviors();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		registerEventListeners();
		registerCraftingRecipes();
		proxy.registerAchievements();
		proxy.registerRenders();
		ModPotions.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{

	}

	public void registerEventListeners()
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
		MinecraftForge.EVENT_BUS.register(new ZombieTrapEvent());
		MinecraftForge.EVENT_BUS.register(new InteractHorseEvent());
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

	//copied from bootstrap
	public void registerDispenserBehaviors()
	{
		Field[] fields = ModItems.class.getDeclaredFields();
		for (int i=0; i<fields.length; i++)
		{
			try 
			{
				if (fields[i].get(new Object()) instanceof ItemBabySpawnEgg)
				{
					BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject((Item) fields[i].get(ModItems.class), new BehaviorDefaultDispenseItem()
					{
						/**
						 * Dispense the specified stack, play the dispense sound and spawn particles.
						 */
						public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
						{
							EnumFacing enumfacing = (EnumFacing)source.func_189992_e().getValue(BlockDispenser.FACING);
							double d0 = source.getX() + (double)enumfacing.getFrontOffsetX();
							double d1 = (double)((float)source.getBlockPos().getY() + 0.2F);
							double d2 = source.getZ() + (double)enumfacing.getFrontOffsetZ();
							Entity entity = ItemBabySpawnEgg.spawnCreature(source.getWorld(), ((ItemBabySpawnEgg) stack.getItem()).entityName, d0, d1, d2);

							if (entity instanceof EntityLivingBase && stack.hasDisplayName())
							{
								((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
							}

							stack.splitStack(1);
							return stack;
						}
					});
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
			}
		}
	}
}
