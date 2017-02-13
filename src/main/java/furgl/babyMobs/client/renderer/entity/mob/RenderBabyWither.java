package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyWither;
import furgl.babyMobs.client.renderer.entity.layers.LayerBabyWitherAura;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderWither;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyWither extends RenderWither
{
    public RenderBabyWither(RenderManager renderManager)
    {
    	super(renderManager);
        this.mainModel = new ModelBabyWither(0.0F);
        this.shadowSize = 1.0F;
        for (LayerRenderer layer : this.layerRenderers) {
        	if (layer.getClass().getSimpleName().equals("LayerWitherAura"))
        	{
        		this.layerRenderers.remove(layer);
        		break;
        	}
        }
        this.addLayer(new LayerBabyWitherAura(this));
    }
}