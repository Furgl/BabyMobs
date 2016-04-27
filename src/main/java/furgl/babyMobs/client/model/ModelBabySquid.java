package furgl.babyMobs.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;

@SideOnly(Side.CLIENT)
public class ModelBabySquid extends ModelBase
{
    /** The squid's body */
    ModelRenderer squidBody;
    /** The squid's tentacles */
    ModelRenderer[] squidTentacles = new ModelRenderer[8];

    public ModelBabySquid()
    {
        byte b0 = -16;
        this.squidBody = new ModelRenderer(this, 0, 0);
        this.squidBody.addBox(-6.0F, -8.0F, -6.0F, 12, 16, 12);
        this.squidBody.rotationPointY += 24 + b0;

        for (int i = 0; i < this.squidTentacles.length; ++i)
        {
            this.squidTentacles[i] = new ModelRenderer(this, 48, 0);
            double d0 = i * Math.PI * 2.0D / this.squidTentacles.length;
            float f = (float)Math.cos(d0) * 5.0F;
            float f1 = (float)Math.sin(d0) * 5.0F;
            this.squidTentacles[i].addBox(-1.0F, 0.0F, -1.0F, 2, 18, 2);
            this.squidTentacles[i].rotationPointX = f;
            this.squidTentacles[i].rotationPointZ = f1;
            this.squidTentacles[i].rotationPointY = 31 + b0;
            d0 = i * Math.PI * -2.0D / this.squidTentacles.length + (Math.PI / 2D);
            this.squidTentacles[i].rotateAngleY = (float)d0;
        }
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        ModelRenderer[] amodelrenderer = this.squidTentacles;
        int i = amodelrenderer.length;

        for (int j = 0; j < i; ++j)
        {
            ModelRenderer modelrenderer = amodelrenderer[j];
            modelrenderer.rotateAngleX = p_78087_3_;
        }
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
    	isChild = true;
    	
		 // glScalef the whole thing for big or small entities
		float glScalefFactor = 0.5F;
	    GL11.glPushMatrix();
	    GL11.glTranslatef(0F, 0F, 0F);
	    GL11.glScalef(glScalefFactor, glScalefFactor, glScalefFactor);
    	
    	this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.squidBody.render(p_78088_7_);

        for (int i = 0; i < this.squidTentacles.length; ++i)
        {
            this.squidTentacles[i].render(p_78088_7_);
        }
        
        GL11.glPopMatrix();
    }
}