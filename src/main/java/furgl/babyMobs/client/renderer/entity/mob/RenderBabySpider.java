package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabySpider;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderSpider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabySpider extends RenderSpider
{
    public RenderBabySpider(RenderManager renderManager)
    {
    	super(renderManager);
        this.mainModel = new ModelBabySpider();
        this.shadowSize = 1.0F;
    }
}