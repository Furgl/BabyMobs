package furgl.babyMobs.client.model;

import org.lwjgl.opengl.GL11;

import furgl.babyMobs.common.entity.monster.EntityBabyWitherSkeleton;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelZombie;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;


public class ModelBabyWitherSkeleton extends ModelZombie
{
	public ModelRenderer witherSkull;
	
	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		isChild = true;
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		GL11.glPushMatrix();
		float f6 = 2.0F;
		GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GL11.glTranslatef(0.0F, 16.0F * p_78088_7_, 0.0F);
		this.bipedHead.render(p_78088_7_);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
		this.witherSkull.render(p_78088_7_);
		
		this.bipedBody.render(p_78088_7_);
		this.bipedRightArm.render(p_78088_7_);
		this.bipedLeftArm.render(p_78088_7_);
		this.bipedRightLeg.render(p_78088_7_);
		this.bipedLeftLeg.render(p_78088_7_);
		this.bipedHeadwear.render(p_78088_7_);
		
		GL11.glPopMatrix();
	}
	

	    public ModelBabyWitherSkeleton()
	    {
	        this(0.0F, false);
	    }

	    public ModelBabyWitherSkeleton(float p_i46303_1_, boolean p_i46303_2_)
	    {
	        super(p_i46303_1_, 0.0F, 64, 32);

	        if (!p_i46303_2_)
	        {
	            this.witherSkull = new ModelRenderer(this, 0, 0);
	            this.witherSkull.addBox(-4.0F, -2.0F, -17.0F, 8, 8, 8, p_i46303_1_);
	            this.witherSkull.setRotationPoint(0.0F, 0.0F, 0.0F);
	            
	        	this.bipedRightArm = new ModelRenderer(this, 40, 16);
	            this.bipedRightArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, p_i46303_1_);
	            this.bipedRightArm.setRotationPoint(-5.0F, 2.0F, 0.0F);
	            this.bipedLeftArm = new ModelRenderer(this, 40, 16);
	            this.bipedLeftArm.mirror = true;
	            this.bipedLeftArm.addBox(-1.0F, -2.0F, -1.0F, 2, 12, 2, p_i46303_1_);
	            this.bipedLeftArm.setRotationPoint(5.0F, 2.0F, 0.0F);
	            this.bipedRightLeg = new ModelRenderer(this, 0, 16);
	            this.bipedRightLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, p_i46303_1_);
	            this.bipedRightLeg.setRotationPoint(-2.0F, 12.0F, 0.0F);
	            this.bipedLeftLeg = new ModelRenderer(this, 0, 16);
	            this.bipedLeftLeg.mirror = true;
	            this.bipedLeftLeg.addBox(-1.0F, 0.0F, -1.0F, 2, 12, 2, p_i46303_1_);
	            this.bipedLeftLeg.setRotationPoint(2.0F, 12.0F, 0.0F);
	        }
	    }

	    /**
	     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
	     * and third as in the setRotationAngles method.
	     */
	    @Override
		public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_)
	    {
	        this.aimedBow = ((EntityBabyWitherSkeleton)p_78086_1_).getSkeletonType() == 1;
	        super.setLivingAnimations(p_78086_1_, p_78086_2_, p_78086_3_, p_78086_4_);
	    }

	    /**
	     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	     * "far" arms and legs can swing at most.
	     */
	    @Override
		public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_, float p_78087_5_, float p_78087_6_, Entity p_78087_7_)
	    {
	        super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
	    }
}
