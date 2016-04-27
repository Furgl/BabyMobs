package furgl.babyMobs.common;

import java.lang.reflect.Field;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import furgl.babyMobs.client.gui.Achievements;
import furgl.babyMobs.common.block.ModBlocks;
import furgl.babyMobs.common.config.Config;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.event.BabyReplaceEvent;
import furgl.babyMobs.common.event.BlockPatternEvent;
import furgl.babyMobs.common.event.BreakSpeedEvent;
import furgl.babyMobs.common.event.ConfigChangeEvent;
import furgl.babyMobs.common.event.OnUpdateEvent;
import furgl.babyMobs.common.event.SpawnDragonEvent;
import furgl.babyMobs.common.event.SummonCommandEvent;
import furgl.babyMobs.common.item.ModItems;
import furgl.babyMobs.common.item.spawnEgg.BabySpawnEgg;
import furgl.babyMobs.creativetab.BabyMobsCreativeTab;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.MinecraftForge;

@Mod(modid = BabyMobs.MODID, name = BabyMobs.MODNAME, version = BabyMobs.VERSION, guiFactory = "furgl.babyMobs.client.gui.BabyMobsGuiFactory")
public class BabyMobs
{
    public static final String MODID = "babymobs";
    public static final String MODNAME = "Baby Mobs";
    public static final String VERSION = "1.4";
	@Mod.Instance("babymobs")
	public static BabyMobs instance;
	@SidedProxy(clientSide = "furgl.babyMobs.client.ClientProxy", serverSide = "furgl.babyMobs.common.CommonProxy")
	public static CommonProxy proxy;
	public static final BabyMobsCreativeTab tab = new BabyMobsCreativeTab("tabBabyMobs");

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Config.init(event.getSuggestedConfigurationFile());		
		ModEntities.registerEntities();
		ModItems.init();
		ModBlocks.init();
		Achievements.init();
		registerDispenserBehaviors();
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		registerEventListeners();
		proxy.registerRenders();
		proxy.registerAchievements();
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
		FMLCommonHandler.instance().bus().register(new ConfigChangeEvent());
		FMLCommonHandler.instance().bus().register(new OnUpdateEvent());
	}
	
	public void registerDispenserBehaviors()
	{
		Field[] fields = ModItems.class.getDeclaredFields();
		for (int i=0; i<fields.length; i++)
		{
			try 
			{
				if (fields[i].get(new Object()) instanceof BabySpawnEgg)
				{
					BlockDispenser.dispenseBehaviorRegistry.putObject(fields[i].get(ModItems.class), new BehaviorDefaultDispenseItem()
					{
						/**
						 * Dispense the specified stack, play the dispense sound and spawn particles.
						 */
						public ItemStack dispenseStack(IBlockSource source, ItemStack stack)
						{
							EnumFacing enumfacing = BlockDispenser.func_149937_b(source.getBlockMetadata());
							double d0 = source.getX() + (double)enumfacing.getFrontOffsetX();
							double d1 = (double)((float)source.getYInt() + 0.2F);
							double d2 = source.getZ() + (double)enumfacing.getFrontOffsetZ();
							Entity entity = BabySpawnEgg.spawnCreature(source.getWorld(), ((BabySpawnEgg) stack.getItem()).entityName, d0, d1, d2);

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
