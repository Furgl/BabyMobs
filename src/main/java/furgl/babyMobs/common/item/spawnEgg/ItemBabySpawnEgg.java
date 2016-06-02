package furgl.babyMobs.common.item.spawnEgg;

import java.util.UUID;

import net.minecraft.block.BlockFence;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.MobSpawnerBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class ItemBabySpawnEgg extends Item 
{
	public String entityName;

	public ItemBabySpawnEgg(String entityName)
	{
		this.entityName = entityName;
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 *  
	 * @param pos The block being right-clicked
	 * @param side The side being right-clicked
	 */
	@Override
	public EnumActionResult onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if (worldIn.isRemote)
		{
			return EnumActionResult.SUCCESS;
		}
		else if (!playerIn.canPlayerEdit(pos.offset(facing), facing, stack))
		{
			return EnumActionResult.FAIL;
		}
		else
		{
			IBlockState iblockstate = worldIn.getBlockState(pos);

			if (iblockstate.getBlock() == Blocks.mob_spawner)
			{
				TileEntity tileentity = worldIn.getTileEntity(pos);

				if (tileentity instanceof TileEntityMobSpawner)
				{
					MobSpawnerBaseLogic mobspawnerbaselogic = ((TileEntityMobSpawner)tileentity).getSpawnerBaseLogic();
					mobspawnerbaselogic.setEntityName(entityName);
					tileentity.markDirty();
					worldIn.notifyBlockUpdate(pos, iblockstate, iblockstate, 3);

					if (!playerIn.capabilities.isCreativeMode)
					{
						--stack.stackSize;
					}

					return EnumActionResult.SUCCESS;
				}
			}

			pos = pos.offset(facing);
			double d0 = 0.0D;

			if (facing == EnumFacing.UP && iblockstate.getBlock() instanceof BlockFence) //Forge: Fix Vanilla bug comparing state instead of block
			{
				d0 = 0.5D;
			}

			Entity entity = spawnCreature(worldIn, this.entityName, (double)pos.getX() + 0.5D, (double)pos.getY() + d0, (double)pos.getZ() + 0.5D);

			if (entity != null)
			{
				if (entity instanceof EntityLivingBase && stack.hasDisplayName())
				{
					entity.setCustomNameTag(stack.getDisplayName());
				}

				applyItemEntityDataToEntity(worldIn, playerIn, stack, entity);

				if (!playerIn.capabilities.isCreativeMode)
				{
					--stack.stackSize;
				}
			}

			return EnumActionResult.SUCCESS;
		}
	}

	/**
	 * Applies the data in the EntityTag tag of the given ItemStack to the given Entity.
	 *  
	 * @param entityWorld The world the Entity resides in
	 */
	public static void applyItemEntityDataToEntity(World entityWorld, EntityPlayer p_185079_1_, ItemStack stack, Entity targetEntity)
	{
		MinecraftServer minecraftserver = entityWorld.getMinecraftServer();

		if (minecraftserver != null && targetEntity != null)
		{
			NBTTagCompound nbttagcompound = stack.getTagCompound();

			if (nbttagcompound != null && nbttagcompound.hasKey("EntityTag", 10))
			{
				if (!entityWorld.isRemote && targetEntity.func_184213_bq() && (p_185079_1_ == null || !minecraftserver.getPlayerList().canSendCommands(p_185079_1_.getGameProfile())))
				{
					return;
				}

				NBTTagCompound nbttagcompound1 = new NBTTagCompound();
				targetEntity.writeToNBT(nbttagcompound1);
				UUID uuid = targetEntity.getUniqueID();
				nbttagcompound1.merge(nbttagcompound.getCompoundTag("EntityTag"));
				targetEntity.setUniqueId(uuid);
				targetEntity.readFromNBT(nbttagcompound1);
			}
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
	{
		if (worldIn.isRemote)
		{
			return new ActionResult(EnumActionResult.PASS, itemStackIn);
		}
		else
		{
			RayTraceResult raytraceresult = this.getMovingObjectPositionFromPlayer(worldIn, playerIn, true);

			if (raytraceresult != null && raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
			{
				BlockPos blockpos = raytraceresult.getBlockPos();

				if (!(worldIn.getBlockState(blockpos).getBlock() instanceof BlockLiquid))
				{
					return new ActionResult(EnumActionResult.PASS, itemStackIn);
				}
				else if (worldIn.isBlockModifiable(playerIn, blockpos) && playerIn.canPlayerEdit(blockpos, raytraceresult.sideHit, itemStackIn))
				{
					Entity entity = spawnCreature(worldIn, this.entityName, (double)blockpos.getX() + 0.5D, (double)blockpos.getY() + 0.5D, (double)blockpos.getZ() + 0.5D);

					if (entity == null)
					{
						return new ActionResult(EnumActionResult.PASS, itemStackIn);
					}
					else
					{
						if (entity instanceof EntityLivingBase && itemStackIn.hasDisplayName())
						{
							entity.setCustomNameTag(itemStackIn.getDisplayName());
						}

						applyItemEntityDataToEntity(worldIn, playerIn, itemStackIn, entity);

						if (!playerIn.capabilities.isCreativeMode)
						{
							--itemStackIn.stackSize;
						}

						playerIn.addStat(StatList.func_188057_b(this));
						return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);
					}
				}
				else
				{
					return new ActionResult(EnumActionResult.FAIL, itemStackIn);
				}
			}
			else
			{
				return new ActionResult(EnumActionResult.PASS, itemStackIn);
			}
		}
	}

	/**
	 * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
	 * Parameters: world, entityID, x, y, z.
	 */
	public static Entity spawnCreature(World worldIn, String name, double x, double y, double z)
	{
		if (!EntityList.stringToClassMapping.containsKey(name))
		{
			return null;
		}
		else
		{
			Entity entity = null;

			for (int j = 0; j < 1; ++j)
			{
				entity = EntityList.createEntityByName(name, worldIn);

				if (entity instanceof EntityLivingBase)
				{
					EntityLiving entityliving = (EntityLiving)entity;
					entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(worldIn.rand.nextFloat() * 360.0F), 0.0F);
					entityliving.rotationYawHead = entityliving.rotationYaw;
					entityliving.renderYawOffset = entityliving.rotationYaw;
					entityliving.onInitialSpawn(worldIn.getDifficultyForLocation(new BlockPos(entityliving)), (IEntityLivingData)null);
					worldIn.spawnEntityInWorld(entity);
					entityliving.playLivingSound();
				}
			}
			return entity;
		}
	}
}
