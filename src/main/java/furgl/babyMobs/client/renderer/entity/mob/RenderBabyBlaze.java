package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyBlaze;
import net.minecraft.client.renderer.entity.RenderBlaze;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyBlaze extends RenderBlaze
{   
    public RenderBabyBlaze(RenderManager renderManager)
    {
        super(renderManager);
        this.mainModel = new ModelBabyBlaze();
        this.shadowSize = 0.25F;
    }
}