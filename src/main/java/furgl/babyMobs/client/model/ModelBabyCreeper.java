package furgl.babyMobs.client.model;

import org.lwjgl.opengl.GL11;

import furgl.babyMobs.common.entity.monster.EntityBabyCreeper;
import net.minecraft.client.model.ModelCreeper;
import net.minecraft.entity.Entity;

public class ModelBabyCreeper extends ModelCreeper 
{
	@Override
	public void render(Entity par1, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		isChild = true;
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1);
		GL11.glPushMatrix();
		float f6 = 2F;
		GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GL11.glTranslatef(0.0F, 16.0F * par7 + 0.1F,  0.0F);
		this.head.render(par7);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GL11.glTranslatef(0.0F, 24.0F * par7, 0.0F);
		
		this.body.render(par7);
		this.leg1.render(par7);
		this.leg2.render(par7);
		this.leg3.render(par7);
		this.leg4.render(par7);
		
		GL11.glPopMatrix();
	}

	//used for charged creeper layer
	public void render(EntityBabyCreeper par1, float par2, float par3, float par4, float par5, float par6, float par7, float increase) {
		isChild = true;
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1);
		GL11.glPushMatrix();
		float f6 = 1.6F;
		GL11.glScalef(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GL11.glTranslatef(0.0F, 16.0F * par7 - 0.15F,  0.0F);
		this.head.render(par7);
		GL11.glPopMatrix();
		GL11.glPushMatrix();
		GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GL11.glTranslatef(0.0F, 24.0F * par7 - 0.55F, 0.0F);
		
		this.body.render(par7);
		this.leg1.render(par7);
		this.leg2.render(par7);
		this.leg3.render(par7);
		this.leg4.render(par7);
		
		GL11.glPopMatrix();		
	}
}
