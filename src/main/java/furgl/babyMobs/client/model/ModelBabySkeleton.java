package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;


public class ModelBabySkeleton extends ModelSkeleton
{
	public ModelBabySkeleton() 
	{
		super();
	}
	
	public ModelBabySkeleton(float p_i46303_1_, boolean p_i46303_2_)
	{
		super(p_i46303_1_, p_i46303_2_);
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

		this.bipedBody.render(p_78088_7_);
		this.bipedRightArm.render(p_78088_7_);
		this.bipedLeftArm.render(p_78088_7_);
		this.bipedRightLeg.render(p_78088_7_);
		this.bipedLeftLeg.render(p_78088_7_);
		this.bipedHeadwear.render(p_78088_7_);

		GlStateManager.popMatrix();
	}
}
