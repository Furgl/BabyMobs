package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyCreeper;
import furgl.babyMobs.client.renderer.entity.layers.LayerBabyCreeperCharge;
import net.minecraft.client.renderer.entity.RenderCreeper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyCreeper extends RenderCreeper
{
    public RenderBabyCreeper(RenderManager renderManager)
    {
    	super(renderManager);
        this.mainModel = new ModelBabyCreeper();
        this.shadowSize = 0.5F;
        for (LayerRenderer layer : this.layerRenderers) {
        	if (layer.getClass().getSimpleName().equals("LayerCreeperCharge"))
        	{
        		this.layerRenderers.remove(layer);
        		break;
        	}
        }
        this.addLayer(new LayerBabyCreeperCharge(this));
    }
}