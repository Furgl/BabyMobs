package furgl.babyMobs.common.packet;

import furgl.babyMobs.common.potion.ModPotions;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketVolatileLevitation implements IMessage
{
	protected int time;

	public PacketVolatileLevitation() 
	{

	}

	public PacketVolatileLevitation(int time) 
	{
		this.time = time;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.time = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeInt(time);
	}

	public static class Handler implements IMessageHandler<PacketVolatileLevitation, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketVolatileLevitation packet, final MessageContext ctx) 
		{
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
					player.addPotionEffect(new PotionEffect(ModPotions.volatileLevitationPotion, packet.time));
				}
			});
			return null;
		}
	}
}