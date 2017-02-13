package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyWitherSkeleton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitherSkeleton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyWitherSkeleton extends RenderWitherSkeleton
{
	public RenderBabyWitherSkeleton(RenderManager renderManager)
	{
		super(renderManager);
        this.mainModel = new ModelBabyWitherSkeleton();
        this.shadowSize = 0.5F;
	}
}