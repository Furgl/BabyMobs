package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelEnderman;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelBabyEnderman extends ModelEnderman
{
    public ModelBabyEnderman(float scale)
    {
        super(scale);
    }

	@Override
	public void render(Entity par1, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		isChild = true;
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1);
		GlStateManager.pushMatrix();
		float f6 = 2F;
		GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GlStateManager.translate(0.0F, 16.0F * par7 + 0.3F,  0.0F);
		this.bipedHead.render(par7);
		this.bipedHeadwear.render(par7);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GlStateManager.translate(0.0F, 24.0F * par7, 0.0F);
		
		this.bipedBody.render(par7);
		this.bipedRightLeg.render(par7);
		this.bipedLeftLeg.render(par7);
		this.bipedRightArm.render(par7);
		this.bipedLeftArm.render(par7);
		
		GlStateManager.popMatrix();
	}
}
