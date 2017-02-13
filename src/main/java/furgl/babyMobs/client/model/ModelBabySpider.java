package furgl.babyMobs.client.model;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelSpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;


public class ModelBabySpider extends ModelSpider
{
	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		isChild = true;
	
		 // scale the whole thing for big or small entities
		float scaleFactor = 0.5F;
	    GL11.glPushMatrix();
	    GL11.glTranslatef(0F, 1.5F-1.5F*scaleFactor, 0F);
	    GL11.glScalef(scaleFactor, scaleFactor, scaleFactor);
		
		this.spiderHead.render(p_78088_7_);
		this.spiderNeck.render(p_78088_7_);
		this.spiderBody.render(p_78088_7_);
		this.spiderLeg1.render(p_78088_7_);
		this.spiderLeg2.render(p_78088_7_);
		this.spiderLeg3.render(p_78088_7_);
		this.spiderLeg4.render(p_78088_7_);
		this.spiderLeg5.render(p_78088_7_);
		this.spiderLeg6.render(p_78088_7_);
		this.spiderLeg7.render(p_78088_7_);
		this.spiderLeg8.render(p_78088_7_);
		
		GlStateManager.popMatrix();
	}
}
