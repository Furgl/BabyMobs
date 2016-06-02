package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabySkeleton;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSkeleton;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabySkeleton extends RenderSkeleton
{
    public RenderBabySkeleton(RenderManager renderManager)
    {
    	super(renderManager);
        this.mainModel = new ModelBabySkeleton();
        this.shadowSize = 0.5F;
    }
}