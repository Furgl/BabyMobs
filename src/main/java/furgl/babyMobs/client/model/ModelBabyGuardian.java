package furgl.babyMobs.client.model;

import furgl.babyMobs.common.BabyMobs;
import furgl.babyMobs.common.entity.monster.EntityBabyGuardian;
import net.minecraft.client.model.ModelGuardian;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyGuardian extends ModelGuardian
{
	private ModelRenderer guardianBody;
    private ModelRenderer[] guardianSpines;
	
	public ModelBabyGuardian()
	{
		super();
		this.guardianBody = (ModelRenderer) BabyMobs.reflect(ModelGuardian.class, "guardianBody", this);
		this.guardianSpines = new ModelRenderer[12];
		for (int i = 0; i < this.guardianSpines.length; ++i)
		{
			this.guardianSpines[i] = new ModelRenderer(this, 0, 0);
			this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 13, 2);
			//this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 9, 2);
			this.guardianBody.addChild(this.guardianSpines[i]);
		}
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
		this.guardianBody.render(p_78088_7_);

		GlStateManager.popMatrix();
	}
	
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor, entityIn);
		
		float[] afloat = new float[] {1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F};
        float[] afloat1 = new float[] {0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F};
        float[] afloat2 = new float[] {0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F};
        float[] afloat3 = new float[] {0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F};
        float[] afloat4 = new float[] { -8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F};
        float[] afloat5 = new float[] {8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F};
		float f6 = ageInTicks - entityIn.ticksExisted;
		float f7 = (1.0F - ((EntityGuardian) entityIn).func_175469_o(f6)) * 0.55F;

		//TODO render spikes longer
		if (((EntityBabyGuardian) entityIn).longerSpikes())
		{
			for (int i = 0; i < 12; ++i)
			{
				float length = 0.5F;
				this.guardianSpines[i].rotateAngleX = (float)Math.PI * afloat[i];
				this.guardianSpines[i].rotateAngleY = (float)Math.PI * afloat1[i];
				this.guardianSpines[i].rotateAngleZ = (float)Math.PI * afloat2[i];
				this.guardianSpines[i].rotationPointX = afloat3[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F + length);
				this.guardianSpines[i].rotationPointY = 16.0F + afloat4[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F + length);
				this.guardianSpines[i].rotationPointZ = afloat5[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F + length);
			}
		}
		else
		{
			for (int i = 0; i < 12; ++i) //orig
			{
				this.guardianSpines[i].rotateAngleX = (float)Math.PI * afloat[i];
				this.guardianSpines[i].rotateAngleY = (float)Math.PI * afloat1[i];
				this.guardianSpines[i].rotateAngleZ = (float)Math.PI * afloat2[i];
				this.guardianSpines[i].rotationPointX = afloat3[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f7);
				this.guardianSpines[i].rotationPointY = 16.0F + afloat4[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f7);
				this.guardianSpines[i].rotationPointZ = afloat5[i] * (1.0F + MathHelper.cos(ageInTicks * 1.5F + i) * 0.01F - f7);
			}
		}
	}
}