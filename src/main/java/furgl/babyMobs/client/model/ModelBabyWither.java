package furgl.babyMobs.client.model;

import furgl.babyMobs.common.BabyMobs;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWither;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyWither extends ModelWither
{
    private ModelRenderer[] pieces;
    private ModelRenderer[] heads;

    public ModelBabyWither(float p_i46302_1_)
    {
        super(p_i46302_1_);
        this.pieces = (ModelRenderer[]) BabyMobs.reflect(ModelWither.class, "field_82905_a", this);
        this.pieces[0] = new ModelRenderer(this, 0, 16);
        this.heads = new ModelRenderer[1];
        this.heads[0] = new ModelRenderer(this, 0, 0);
        this.heads[0].addBox(-4.0F, -1.0F, -4.0F, 8, 8, 8, p_i46302_1_);
    }

    @Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
    	isChild = true;
    	
		float scaleFactor = 0.5F;
		GlStateManager.pushMatrix();
	    GlStateManager.translate(0F, 1.5F-1.5F*scaleFactor, 0F);
	    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
    	
    	this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        ModelRenderer[] amodelrenderer = this.heads;
        int i = amodelrenderer.length;
        int j;
        ModelRenderer modelrenderer;
        for (j = 0; j < i; ++j)
        {
            modelrenderer = amodelrenderer[j];
            modelrenderer.render(p_78088_7_);
        }
        amodelrenderer = this.pieces;
        i = amodelrenderer.length;
        for (j = 0; j < i; ++j)
        {
            modelrenderer = amodelrenderer[j];
            modelrenderer.render(p_78088_7_);
        }
        
        GlStateManager.popMatrix();
    }

    @Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {
        
    }
}