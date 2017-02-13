package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelDragon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyDragon extends ModelDragon
{
    public ModelBabyDragon(float par)
    {
        super(par);
    }

    @Override
    public void render(Entity entityIn, float p_78088_2_, float limbSwing, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	isChild = true;
		float scaleFactor = 0.1F; 
	    GlStateManager.pushMatrix();
	    GlStateManager.translate(0F, 1.5F-1.5F*scaleFactor, 0F);
	    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
	    super.render(entityIn, p_78088_2_, limbSwing, ageInTicks, netHeadYaw, headPitch, scaleFactor);
	    GlStateManager.popMatrix();
    }
}