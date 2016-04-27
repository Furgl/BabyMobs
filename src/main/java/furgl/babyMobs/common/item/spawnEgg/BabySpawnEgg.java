package furgl.babyMobs.common.item.spawnEgg;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Facing;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;

public class BabySpawnEgg extends Item
{
	@SideOnly(Side.CLIENT)
	private IIcon theIcon;
	public String entityName;

	public BabySpawnEgg(String entityName)
	{
		this.entityName = entityName;
	}

	/**
	 * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
	 * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
	 */
	public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			return true;
		}
		else
		{
			Block block = world.getBlock(x, y, z);
			x += Facing.offsetsXForSide[side];
			y += Facing.offsetsYForSide[side];
			z += Facing.offsetsZForSide[side];
			double d0 = 0.0D;

			if (side == 1 && block.getRenderType() == 11)
			{
				d0 = 0.5D;
			}

			Entity entity = spawnCreature(world, this.entityName, (double)x + 0.5D, (double)y + d0, (double)z + 0.5D);

			if (entity != null)
			{
				if (entity instanceof EntityLivingBase && stack.hasDisplayName())
				{
					((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
				}

				if (!player.capabilities.isCreativeMode)
				{
					--stack.stackSize;
				}
			}

			return true;
		}
	}

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
	{
		if (world.isRemote)
		{
			return stack;
		}
		else
		{
			MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, true);

			if (movingobjectposition == null)
			{
				return stack;
			}
			else
			{
				if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				{
					int i = movingobjectposition.blockX;
					int j = movingobjectposition.blockY;
					int k = movingobjectposition.blockZ;

					if (!world.canMineBlock(player, i, j, k))
					{
						return stack;
					}

					if (!player.canPlayerEdit(i, j, k, movingobjectposition.sideHit, stack))
					{
						return stack;
					}

					if (world.getBlock(i, j, k) instanceof BlockLiquid)
					{
						Entity entity = spawnCreature(world, this.entityName, (double)i, (double)j, (double)k);

						if (entity != null)
						{
							if (entity instanceof EntityLivingBase && stack.hasDisplayName())
							{
								((EntityLiving)entity).setCustomNameTag(stack.getDisplayName());
							}

							if (!player.capabilities.isCreativeMode)
							{
								--stack.stackSize;
							}
						}
					}
				}

				return stack;
			}
		}
	}

	/**
	 * Spawns the creature specified by the egg's type in the location specified by the last three parameters.
	 * Parameters: world, entityID, x, y, z.
	 */
	public static Entity spawnCreature(World world, String name, double x, double y, double z)
	{
		Entity entity = null;

		for (int j = 0; j < 1; ++j)
		{
			entity = EntityList.createEntityByName(name, world);

			if (entity != null && entity instanceof EntityLivingBase)
			{
				EntityLiving entityliving = (EntityLiving)entity;
				entity.setLocationAndAngles(x, y, z, MathHelper.wrapAngleTo180_float(world.rand.nextFloat() * 360.0F), 0.0F);
				entityliving.rotationYawHead = entityliving.rotationYaw;
				entityliving.renderYawOffset = entityliving.rotationYaw;
				entityliving.onSpawnWithEgg((IEntityLivingData)null);
				world.spawnEntityInWorld(entity);
				entityliving.playLivingSound();
			}
		}
		return entity;
	}
}