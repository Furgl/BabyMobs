package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyShulker extends ModelShulker
{
	//private ModelRenderer base; //bottom
	//private ModelRenderer lid; //top

	public ModelBabyShulker()
	{
		super();
		//base = ReflectionHelper.getPrivateValue(ModelShulker.class, this, 0);
		//lid = ReflectionHelper.getPrivateValue(ModelShulker.class, this, 1);
	}
	
	public void render(Entity entityIn, float par2, float limbSwing, float ageInTicks, float netHeadYaw, float headPitch, float scale)
	{
		isChild = true;
		this.setRotationAngles(par2, limbSwing, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
		GlStateManager.pushMatrix();
		float f6 = 2F;
		GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GlStateManager.translate(0.0F, 16.0F * scale + 0.3F,  0.0F);

		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);

		//this.field_187066_a.render(scale); //head - in RenderBabyShulker
		this.base.render(scale); //bottom
		this.lid.render(scale); //top

		GlStateManager.popMatrix();
	}
}