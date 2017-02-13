package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelGhast;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyGhast extends ModelGhast
{
    ModelRenderer body;
    ModelRenderer[] tentacles = new ModelRenderer[9];

    public ModelBabyGhast()
    {
        super();
        this.body = (ModelRenderer) ReflectionHelper.getPrivateValue(ModelGhast.class, this, 0); //body
        this.tentacles = (ModelRenderer[]) ReflectionHelper.getPrivateValue(ModelGhast.class, this, 1); //tentacles
    }

    @Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
    	isChild = true;
    	
		float scaleFactor = 0.3F;
		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, 1.5F-1.5F*scaleFactor, 0F);
		GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
    	
    	this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        GlStateManager.translate(0.0F, 0.6F, 0.0F);
        this.body.render(p_78088_7_);
        ModelRenderer[] amodelrenderer = this.tentacles;
        int i = amodelrenderer.length;

        for (int j = 0; j < i; ++j)
        {
            ModelRenderer modelrenderer = amodelrenderer[j];
            modelrenderer.render(p_78088_7_);
        }

        GlStateManager.popMatrix();
    }
}