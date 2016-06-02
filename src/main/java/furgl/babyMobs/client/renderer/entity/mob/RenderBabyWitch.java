package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyWitch;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWitch;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyWitch extends RenderWitch
{
    public RenderBabyWitch(RenderManager renderManager)
    {
    	super(renderManager);
        this.mainModel = new ModelBabyWitch(0.0F);
        this.shadowSize = 0.5F;
    }
}