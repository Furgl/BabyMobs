package furgl.babyMobs.client.model;

import furgl.babyMobs.common.BabyMobs;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSquid;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabySquid extends ModelSquid
{
    /** The squid's body */
    ModelRenderer squidBody;
    /** The squid's tentacles */
    ModelRenderer[] squidTentacles = new ModelRenderer[8];
    
    public ModelBabySquid()
    {
       super();
       this.squidBody = (ModelRenderer) BabyMobs.reflect(ModelSquid.class, "squidBody", this);
       this.squidTentacles = (ModelRenderer[]) BabyMobs.reflect(ModelSquid.class, "squidTentacles", this);
    }
    
    @Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
    	isChild = true;
    	
		float scaleFactor = 0.5F;
		GlStateManager.pushMatrix();
	    GlStateManager.translate(0F, 0F, 0F);
	    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
    	
    	this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.squidBody.render(p_78088_7_);

        for (int i = 0; i < this.squidTentacles.length; ++i)
        {
            this.squidTentacles[i].render(p_78088_7_);
        }
        
        GlStateManager.popMatrix();
    }
}