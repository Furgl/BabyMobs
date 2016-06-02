package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyDragon;
import net.minecraft.client.renderer.entity.RenderDragon;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyDragon extends RenderDragon
{
    public RenderBabyDragon(RenderManager renderManager)
    {
    	super(renderManager);
        this.mainModel = new ModelBabyDragon(0.0F);
        this.shadowSize = 0.5F;
    }
}