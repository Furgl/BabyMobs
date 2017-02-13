package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabySnowMan;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSnowMan;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabySnowMan extends RenderSnowMan
{
    public RenderBabySnowMan(RenderManager renderManager)
    {
    	super(renderManager);
        this.mainModel = new ModelBabySnowMan();
        this.shadowSize = 0.5F;
        for (LayerRenderer layer : this.layerRenderers) {
        	if (layer.getClass().getSimpleName().equals("LayerSnowmanHead"))
        	{
        		this.layerRenderers.remove(layer);
        		break;
        	}
        }
    }
}