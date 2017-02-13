package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyIronGolem;
import furgl.babyMobs.client.renderer.entity.layers.LayerBabyIronGolemFlower;
import net.minecraft.client.renderer.entity.RenderIronGolem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyIronGolem extends RenderIronGolem
{
    public RenderBabyIronGolem(RenderManager renderManager)
    {
        super(renderManager);
        this.mainModel = new ModelBabyIronGolem();
        this.shadowSize = 0.5F;
        for (LayerRenderer layer : this.layerRenderers) {
        	if (layer.getClass().getSimpleName().equals("LayerIronGolemFlower"))
        	{
        		this.layerRenderers.remove(layer);
        		break;
        	}
        }
        this.addLayer(new LayerBabyIronGolemFlower(this));
    }
}