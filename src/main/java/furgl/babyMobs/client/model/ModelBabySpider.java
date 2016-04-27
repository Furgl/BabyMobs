package furgl.babyMobs.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

@SideOnly(Side.CLIENT)
public class ModelBabySpider extends ModelBase
{
    /** The BabySpider's head box */
    public ModelRenderer BabySpiderHead;
    /** The BabySpider's neck box */
    public ModelRenderer BabySpiderNeck;
    /** The BabySpider's body box */
    public ModelRenderer BabySpiderBody;
    /** BabySpider's first leg */
    public ModelRenderer BabySpiderLeg1;
    /** BabySpider's second leg */
    public ModelRenderer BabySpiderLeg2;
    /** BabySpider's third leg */
    public ModelRenderer BabySpiderLeg3;
    /** BabySpider's fourth leg */
    public ModelRenderer BabySpiderLeg4;
    /** BabySpider's fifth leg */
    public ModelRenderer BabySpiderLeg5;
    /** BabySpider's sixth leg */
    public ModelRenderer BabySpiderLeg6;
    /** BabySpider's seventh leg */
    public ModelRenderer BabySpiderLeg7;
    /** BabySpider's eight leg */
    public ModelRenderer BabySpiderLeg8;

    public ModelBabySpider()
    {
        float f = 0.0F;
        byte b0 = 15;
        this.BabySpiderHead = new ModelRenderer(this, 32, 4);
        this.BabySpiderHead.addBox(-4.0F, -4.0F, -8.0F, 8, 8, 8, f);
        this.BabySpiderHead.setRotationPoint(0.0F, b0, -3.0F);
        this.BabySpiderNeck = new ModelRenderer(this, 0, 0);
        this.BabySpiderNeck.addBox(-3.0F, -3.0F, -3.0F, 6, 6, 6, f);
        this.BabySpiderNeck.setRotationPoint(0.0F, b0, 0.0F);
        this.BabySpiderBody = new ModelRenderer(this, 0, 12);
        this.BabySpiderBody.addBox(-5.0F, -4.0F, -6.0F, 10, 8, 12, f);
        this.BabySpiderBody.setRotationPoint(0.0F, b0, 9.0F);
        this.BabySpiderLeg1 = new ModelRenderer(this, 18, 0);
        this.BabySpiderLeg1.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.BabySpiderLeg1.setRotationPoint(-4.0F, b0, 2.0F);
        this.BabySpiderLeg2 = new ModelRenderer(this, 18, 0);
        this.BabySpiderLeg2.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.BabySpiderLeg2.setRotationPoint(4.0F, b0, 2.0F);
        this.BabySpiderLeg3 = new ModelRenderer(this, 18, 0);
        this.BabySpiderLeg3.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.BabySpiderLeg3.setRotationPoint(-4.0F, b0, 1.0F);
        this.BabySpiderLeg4 = new ModelRenderer(this, 18, 0);
        this.BabySpiderLeg4.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.BabySpiderLeg4.setRotationPoint(4.0F, b0, 1.0F);
        this.BabySpiderLeg5 = new ModelRenderer(this, 18, 0);
        this.BabySpiderLeg5.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.BabySpiderLeg5.setRotationPoint(-4.0F, b0, 0.0F);
        this.BabySpiderLeg6 = new ModelRenderer(this, 18, 0);
        this.BabySpiderLeg6.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.BabySpiderLeg6.setRotationPoint(4.0F, b0, 0.0F);
        this.BabySpiderLeg7 = new ModelRenderer(this, 18, 0);
        this.BabySpiderLeg7.addBox(-15.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.BabySpiderLeg7.setRotationPoint(-4.0F, b0, -1.0F);
        this.BabySpiderLeg8 = new ModelRenderer(this, 18, 0);
        this.BabySpiderLeg8.addBox(-1.0F, -1.0F, -1.0F, 16, 2, 2, f);
        this.BabySpiderLeg8.setRotationPoint(4.0F, b0, -1.0F);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
        this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        isChild = true;
    	
		 // scale the whole thing for big or small entities
		float scaleFactor = 0.5F;
	    GL11.glPushMatrix();
	    GL11.glTranslatef(0F, 1.5F-1.5F*scaleFactor, 0F);
	    GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
		
		this.BabySpiderHead.render(p_78088_7_);
		this.BabySpiderNeck.render(p_78088_7_);
		this.BabySpiderBody.render(p_78088_7_);
		this.BabySpiderLeg1.render(p_78088_7_);
		this.BabySpiderLeg2.render(p_78088_7_);
		this.BabySpiderLeg3.render(p_78088_7_);
		this.BabySpiderLeg4.render(p_78088_7_);
		this.BabySpiderLeg5.render(p_78088_7_);
		this.BabySpiderLeg6.render(p_78088_7_);
		this.BabySpiderLeg7.render(p_78088_7_);
		this.BabySpiderLeg8.render(p_78088_7_);
		
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
        this.BabySpiderHead.rotateAngleY = p_78087_4_ / (180F / (float)Math.PI);
        this.BabySpiderHead.rotateAngleX = p_78087_5_ / (180F / (float)Math.PI);
        float f6 = ((float)Math.PI / 4F);
        this.BabySpiderLeg1.rotateAngleZ = -f6;
        this.BabySpiderLeg2.rotateAngleZ = f6;
        this.BabySpiderLeg3.rotateAngleZ = -f6 * 0.74F;
        this.BabySpiderLeg4.rotateAngleZ = f6 * 0.74F;
        this.BabySpiderLeg5.rotateAngleZ = -f6 * 0.74F;
        this.BabySpiderLeg6.rotateAngleZ = f6 * 0.74F;
        this.BabySpiderLeg7.rotateAngleZ = -f6;
        this.BabySpiderLeg8.rotateAngleZ = f6;
        float f7 = -0.0F;
        float f8 = 0.3926991F;
        this.BabySpiderLeg1.rotateAngleY = f8 * 2.0F + f7;
        this.BabySpiderLeg2.rotateAngleY = -f8 * 2.0F - f7;
        this.BabySpiderLeg3.rotateAngleY = f8 * 1.0F + f7;
        this.BabySpiderLeg4.rotateAngleY = -f8 * 1.0F - f7;
        this.BabySpiderLeg5.rotateAngleY = -f8 * 1.0F + f7;
        this.BabySpiderLeg6.rotateAngleY = f8 * 1.0F - f7;
        this.BabySpiderLeg7.rotateAngleY = -f8 * 2.0F + f7;
        this.BabySpiderLeg8.rotateAngleY = f8 * 2.0F - f7;
        float f9 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + 0.0F) * 0.4F) * p_78087_2_;
        float f10 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + (float)Math.PI) * 0.4F) * p_78087_2_;
        float f11 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + ((float)Math.PI / 2F)) * 0.4F) * p_78087_2_;
        float f12 = -(MathHelper.cos(p_78087_1_ * 0.6662F * 2.0F + ((float)Math.PI * 3F / 2F)) * 0.4F) * p_78087_2_;
        float f13 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + 0.0F) * 0.4F) * p_78087_2_;
        float f14 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + (float)Math.PI) * 0.4F) * p_78087_2_;
        float f15 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + ((float)Math.PI / 2F)) * 0.4F) * p_78087_2_;
        float f16 = Math.abs(MathHelper.sin(p_78087_1_ * 0.6662F + ((float)Math.PI * 3F / 2F)) * 0.4F) * p_78087_2_;
        this.BabySpiderLeg1.rotateAngleY += f9;
        this.BabySpiderLeg2.rotateAngleY += -f9;
        this.BabySpiderLeg3.rotateAngleY += f10;
        this.BabySpiderLeg4.rotateAngleY += -f10;
        this.BabySpiderLeg5.rotateAngleY += f11;
        this.BabySpiderLeg6.rotateAngleY += -f11;
        this.BabySpiderLeg7.rotateAngleY += f12;
        this.BabySpiderLeg8.rotateAngleY += -f12;
        this.BabySpiderLeg1.rotateAngleZ += f13;
        this.BabySpiderLeg2.rotateAngleZ += -f13;
        this.BabySpiderLeg3.rotateAngleZ += f14;
        this.BabySpiderLeg4.rotateAngleZ += -f14;
        this.BabySpiderLeg5.rotateAngleZ += f15;
        this.BabySpiderLeg6.rotateAngleZ += -f15;
        this.BabySpiderLeg7.rotateAngleZ += f16;
        this.BabySpiderLeg8.rotateAngleZ += -f16;
    }
}