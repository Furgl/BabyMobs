package furgl.babyMobs.common.packet;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketMotionY implements IMessage
{
	protected double motionY;

	public PacketMotionY() 
	{

	}

	public PacketMotionY(double motionY) 
	{
		this.motionY = motionY;
	}

	@Override
	public void fromBytes(ByteBuf buf) 
	{
		this.motionY = buf.readDouble();
	}

	@Override
	public void toBytes(ByteBuf buf) 
	{
		buf.writeDouble(motionY);
	}

	public static class Handler implements IMessageHandler<PacketMotionY, IMessage>
	{
		@Override
		public IMessage onMessage(final PacketMotionY packet, final MessageContext ctx) 
		{
			IThreadListener mainThread = Minecraft.getMinecraft();
			mainThread.addScheduledTask(new Runnable() 
			{
				@Override
				public void run() 
				{
					EntityPlayerSP player = Minecraft.getMinecraft().player;
					player.motionY = packet.motionY;
				}
			});
			return null;
		}
	}
}