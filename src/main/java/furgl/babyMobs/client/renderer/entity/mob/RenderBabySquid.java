package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabySquid;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSquid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabySquid extends RenderSquid
{
	public RenderBabySquid(RenderManager renderManager)
	{
		super(renderManager);
        this.mainModel = new ModelBabySquid();
        this.shadowSize = 0.5F;
	}
}