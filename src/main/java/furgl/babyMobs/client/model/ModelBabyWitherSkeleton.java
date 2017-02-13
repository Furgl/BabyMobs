package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;


public class ModelBabyWitherSkeleton extends ModelSkeleton
{
	public ModelRenderer witherSkull;

	public ModelBabyWitherSkeleton()
	{
		this(0.0F, false);
	}

	public ModelBabyWitherSkeleton(float p_i46303_1_, boolean p_i46303_2_)
	{
		super(p_i46303_1_, p_i46303_2_);
		this.witherSkull = new ModelRenderer(this, 0, 0);
		this.witherSkull.addBox(-4.0F, -2.0F, -17.0F, 8, 8, 8, p_i46303_1_);
		this.witherSkull.setRotationPoint(0.0F, 0.0F, 0.0F);
	}

	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		isChild = true;
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		GlStateManager.pushMatrix();
		float f6 = 2.0F;
		GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GlStateManager.translate(0.0F, 16.0F * p_78088_7_, 0.0F);
		this.bipedHead.render(p_78088_7_);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GlStateManager.translate(0.0F, 24.0F * p_78088_7_, 0.0F);
		this.witherSkull.render(p_78088_7_);

		this.bipedBody.render(p_78088_7_);
		this.bipedRightArm.render(p_78088_7_);
		this.bipedLeftArm.render(p_78088_7_);
		this.bipedRightLeg.render(p_78088_7_);
		this.bipedLeftLeg.render(p_78088_7_);
		this.bipedHeadwear.render(p_78088_7_);

		GlStateManager.popMatrix();
	}

	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);

		this.bipedRightArm.rotateAngleX = -1.2F;
		this.bipedLeftArm.rotateAngleX = -1.2F;
	}
}
