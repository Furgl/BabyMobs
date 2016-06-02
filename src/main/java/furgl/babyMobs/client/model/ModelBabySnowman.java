package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelSnowMan;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabySnowMan extends ModelSnowMan
{
	public ModelBabySnowMan()
	{
		super();
	}
	
	@Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
	{
		isChild = true;

		float scaleFactor = 0.5F;
		GlStateManager.pushMatrix();
		GlStateManager.translate(0F, 1.5F-1.5F*scaleFactor, 0F);
		GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);

		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		this.body.render(p_78088_7_);
		this.bottomBody.render(p_78088_7_);
		this.head.render(p_78088_7_);
		this.rightHand.render(p_78088_7_);
		this.leftHand.render(p_78088_7_);

		GlStateManager.popMatrix();
	}
}