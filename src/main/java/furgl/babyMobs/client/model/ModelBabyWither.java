package furgl.babyMobs.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelBabyWither extends ModelBase
{
    private ModelRenderer[] pieces;
    private ModelRenderer[] heads;

    public ModelBabyWither(float p_i46302_1_)
    {
        this.textureWidth = 64;
        this.textureHeight = 64;
        this.pieces = new ModelRenderer[3];
        this.pieces[0] = new ModelRenderer(this, 0, 16);
        //this.pieces[0].addBox(-10.0F, 3.9F, -0.5F, 20, 3, 3, p_i46302_1_); orig
        //this.pieces[0].addBox(-1.5F, 3.5F, -0.5F, 3, 3, 3, p_i46302_1_); //neck
        this.pieces[1] = (new ModelRenderer(this)).setTextureSize(this.textureWidth, this.textureHeight);
        this.pieces[1].setRotationPoint(-2.0F, 6.9F, -0.5F);
        this.pieces[1].setTextureOffset(0, 22).addBox(0.0F, 0.0F, 0.0F, 3, 10, 3, p_i46302_1_); //body
        this.pieces[1].setTextureOffset(24, 22).addBox(-4.0F, 1.5F, 0.5F, 11, 2, 2, p_i46302_1_); //rib
        this.pieces[1].setTextureOffset(24, 22).addBox(-4.0F, 4.0F, 0.5F, 11, 2, 2, p_i46302_1_); //rib
        this.pieces[1].setTextureOffset(24, 22).addBox(-4.0F, 6.5F, 0.5F, 11, 2, 2, p_i46302_1_); //rib
        this.pieces[2] = new ModelRenderer(this, 12, 22);
        this.pieces[2].addBox(0.0F, 0.0F, 0.0F, 3, 6, 3, p_i46302_1_); //tail
        this.heads = new ModelRenderer[1];
        this.heads[0] = new ModelRenderer(this, 0, 0);
        this.heads[0].addBox(-4.0F, -1.0F, -4.0F, 8, 8, 8, p_i46302_1_);
        /*this.field_82904_b[1] = new ModelRenderer(this, 32, 0);
        this.field_82904_b[1].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
        this.field_82904_b[1].rotationPointX = -8.0F;
        this.field_82904_b[1].rotationPointY = 4.0F;
        this.field_82904_b[2] = new ModelRenderer(this, 32, 0);
        this.field_82904_b[2].addBox(-4.0F, -4.0F, -4.0F, 6, 6, 6, p_i46302_1_);
        this.field_82904_b[2].rotationPointX = 10.0F;
        this.field_82904_b[2].rotationPointY = 4.0F;*/
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
	    GL11.glTranslatef(0F, 1.5F-1.5F*glScalefFactor, 0F);
	    GL11.glScalef(glScalefFactor, glScalefFactor, glScalefFactor);
    	
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
        
        GL11.glPopMatrix();
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    @Override
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
    {
        float f6 = MathHelper.cos(p_78087_3_ * 0.1F);
        this.pieces[1].rotateAngleX = (0.065F + 0.05F * f6) * (float)Math.PI;
        this.pieces[2].setRotationPoint(-2.0F, 6.9F + MathHelper.cos(this.pieces[1].rotateAngleX) * 10.0F, -0.5F + MathHelper.sin(this.pieces[1].rotateAngleX) * 10.0F);
        this.pieces[2].rotateAngleX = (0.265F + 0.1F * f6) * (float)Math.PI;
        this.heads[0].rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.heads[0].rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    @Override
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
    {

        /*for (int i = 1; i < 3; ++i)
        {
            this.field_82904_b[i].rotateAngleY = (EntityBabyWither.func_82207_a(i - 1) - p_78086_1_.renderYawOffset) / (180F / (float)Math.PI);
            this.field_82904_b[i].rotateAngleX = EntityBabyWither.func_82210_r(i - 1) / (180F / (float)Math.PI);
        }*/
    }
}