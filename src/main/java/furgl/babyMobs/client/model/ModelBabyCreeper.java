package furgl.babyMobs.client.model;

import furgl.babyMobs.common.entity.monster.EntityBabyCreeper;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;

public class ModelBabyCreeper extends ModelCreeper 
{
	@Override
	public void render(Entity par1, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		isChild = true;
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1);
		GlStateManager.pushMatrix();
		float f6 = 2F;
		GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GlStateManager.translate(0.0F, 16.0F * par7 + 0.1F,  0.0F);
		this.head.render(par7);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GlStateManager.translate(0.0F, 24.0F * par7, 0.0F);
		
		this.body.render(par7);
		this.leg1.render(par7);
		this.leg2.render(par7);
		this.leg3.render(par7);
		this.leg4.render(par7);
		
		GlStateManager.popMatrix();
	}

	//used for charged creeper layer - called by LayerBabyCreeperCharge
	public void render(EntityBabyCreeper par1, float par2, float par3, float par4, float par5, float par6, float par7, float increase) {
		isChild = true;
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1);
		GlStateManager.pushMatrix();
		float f6 = 1.6F;
		GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GlStateManager.translate(0.0F, 16.0F * par7 - 0.15F,  0.0F);
		this.head.render(par7);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GlStateManager.translate(0.0F, 24.0F * par7 - 0.55F, 0.0F);
		
		this.body.render(par7);
		this.leg1.render(par7);
		this.leg2.render(par7);
		this.leg3.render(par7);
		this.leg4.render(par7);
		
		GlStateManager.popMatrix();		
	}
}
