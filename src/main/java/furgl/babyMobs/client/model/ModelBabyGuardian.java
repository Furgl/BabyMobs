package furgl.babyMobs.client.model;

import org.lwjgl.opengl.GL11;

import furgl.babyMobs.common.entity.monster.EntityBabyGuardian;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyGuardian extends ModelBase
{
	private ModelRenderer guardianBody;
	private ModelRenderer guardianEye;
	private ModelRenderer[] guardianSpines;
	private ModelRenderer[] guardianTail;

	public ModelBabyGuardian()
	{
		this.textureWidth = 64;
		this.textureHeight = 64;
		this.guardianSpines = new ModelRenderer[12];
		this.guardianBody = new ModelRenderer(this);
		this.guardianBody.setTextureOffset(0, 0).addBox(-6.0F, 10.0F, -8.0F, 12, 12, 16);
		this.guardianBody.setTextureOffset(0, 28).addBox(-8.0F, 10.0F, -6.0F, 2, 12, 12);
		this.guardianBody.setTextureOffset(0, 28).addBox(6.0F, 10.0F, -6.0F, 2, 12, 12, true);
		this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 8.0F, -6.0F, 12, 2, 12);
		this.guardianBody.setTextureOffset(16, 40).addBox(-6.0F, 22.0F, -6.0F, 12, 2, 12);

		for (int i = 0; i < this.guardianSpines.length; ++i)
		{
			this.guardianSpines[i] = new ModelRenderer(this, 0, 0);
			this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 13, 2);
			//this.guardianSpines[i].addBox(-1.0F, -4.5F, -1.0F, 2, 9, 2);
			this.guardianBody.addChild(this.guardianSpines[i]);
		}

		this.guardianEye = new ModelRenderer(this, 8, 0);
		this.guardianEye.addBox(-1.0F, 15.0F, 0.0F, 2, 2, 1);
		this.guardianBody.addChild(this.guardianEye);
		this.guardianTail = new ModelRenderer[3];
		this.guardianTail[0] = new ModelRenderer(this, 40, 0);
		this.guardianTail[0].addBox(-2.0F, 14.0F, 7.0F, 4, 4, 8);
		this.guardianTail[1] = new ModelRenderer(this, 0, 54);
		this.guardianTail[1].addBox(0.0F, 14.0F, 0.0F, 3, 3, 7);
		this.guardianTail[2] = new ModelRenderer(this);
		this.guardianTail[2].setTextureOffset(41, 32).addBox(0.0F, 14.0F, 0.0F, 2, 2, 6);
		this.guardianTail[2].setTextureOffset(25, 19).addBox(1.0F, 10.5F, 3.0F, 1, 9, 9);
		this.guardianBody.addChild(this.guardianTail[0]);
		this.guardianTail[0].addChild(this.guardianTail[1]);
		this.guardianTail[1].addChild(this.guardianTail[2]);
	}

	public int func_178706_a()
	{
		return 54;
	}

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

		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
		this.guardianBody.render(p_78088_7_);

		GlStateManager.popMatrix();
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
	 * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	@Override
	public void setRotationAngles(float par1, float par2, float par3, float par4, float par5, float par6, Entity entity)
	{
		EntityBabyGuardian EntityBabyGuardian = (EntityBabyGuardian)entity;
		float f6 = par3 - EntityBabyGuardian.ticksExisted;
		this.guardianBody.rotateAngleY = par4 / (180F / (float)Math.PI);
		this.guardianBody.rotateAngleX = par5 / (180F / (float)Math.PI);
		float[] afloat = new float[] {1.75F, 0.25F, 0.0F, 0.0F, 0.5F, 0.5F, 0.5F, 0.5F, 1.25F, 0.75F, 0.0F, 0.0F};
		float[] afloat1 = new float[] {0.0F, 0.0F, 0.0F, 0.0F, 0.25F, 1.75F, 1.25F, 0.75F, 0.0F, 0.0F, 0.0F, 0.0F};
		float[] afloat2 = new float[] {0.0F, 0.0F, 0.25F, 1.75F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.75F, 1.25F};
		float[] afloat3 = new float[] {0.0F, 0.0F, 8.0F, -8.0F, -8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F, 8.0F, -8.0F};
		float[] afloat4 = new float[] { -8.0F, -8.0F, -8.0F, -8.0F, 0.0F, 0.0F, 0.0F, 0.0F, 8.0F, 8.0F, 8.0F, 8.0F};
		float[] afloat5 = new float[] {8.0F, -8.0F, 0.0F, 0.0F, -8.0F, -8.0F, 8.0F, 8.0F, 8.0F, -8.0F, 0.0F, 0.0F};
		float f7 = (1.0F - EntityBabyGuardian.func_175469_o(f6)) * 0.55F;

		//TODO render spikes longer
		if (EntityBabyGuardian.longerSpikes())
		{
			for (int i = 0; i < 12; ++i)
			{
				float length = 0.5F;
				this.guardianSpines[i].rotateAngleX = (float)Math.PI * afloat[i];
				this.guardianSpines[i].rotateAngleY = (float)Math.PI * afloat1[i];
				this.guardianSpines[i].rotateAngleZ = (float)Math.PI * afloat2[i];
				this.guardianSpines[i].rotationPointX = afloat3[i] * (1.0F + MathHelper.cos(par3 * 1.5F + i) * 0.01F + length);
				this.guardianSpines[i].rotationPointY = 16.0F + afloat4[i] * (1.0F + MathHelper.cos(par3 * 1.5F + i) * 0.01F + length);
				this.guardianSpines[i].rotationPointZ = afloat5[i] * (1.0F + MathHelper.cos(par3 * 1.5F + i) * 0.01F + length);
			}
		}
		else
		{
			for (int i = 0; i < 12; ++i) //orig
			{
				this.guardianSpines[i].rotateAngleX = (float)Math.PI * afloat[i];
				this.guardianSpines[i].rotateAngleY = (float)Math.PI * afloat1[i];
				this.guardianSpines[i].rotateAngleZ = (float)Math.PI * afloat2[i];
				this.guardianSpines[i].rotationPointX = afloat3[i] * (1.0F + MathHelper.cos(par3 * 1.5F + i) * 0.01F - f7);
				this.guardianSpines[i].rotationPointY = 16.0F + afloat4[i] * (1.0F + MathHelper.cos(par3 * 1.5F + i) * 0.01F - f7);
				this.guardianSpines[i].rotationPointZ = afloat5[i] * (1.0F + MathHelper.cos(par3 * 1.5F + i) * 0.01F - f7);
			}
		}
		//end

		this.guardianEye.rotationPointZ = -8.25F;
		Object object = Minecraft.getMinecraft().getRenderViewEntity();

		if (EntityBabyGuardian.func_175474_cn())
		{
			object = EntityBabyGuardian.getTargetedEntity();
		}

		if (object != null)
		{
			Vec3 vec3 = ((Entity)object).getPositionEyes(0.0F);
			Vec3 vec31 = entity.getPositionEyes(0.0F);
			double d0 = vec3.yCoord - vec31.yCoord;

			if (d0 > 0.0D)
			{
				this.guardianEye.rotationPointY = 0.0F;
			}
			else
			{
				this.guardianEye.rotationPointY = 1.0F;
			}

			Vec3 vec32 = entity.getLook(0.0F);
			vec32 = new Vec3(vec32.xCoord, 0.0D, vec32.zCoord);
			Vec3 vec33 = (new Vec3(vec31.xCoord - vec3.xCoord, 0.0D, vec31.zCoord - vec3.zCoord)).normalize().rotateYaw(((float)Math.PI / 2F));
			double d1 = vec32.dotProduct(vec33);
			this.guardianEye.rotationPointX = MathHelper.sqrt_float((float)Math.abs(d1)) * 2.0F * (float)Math.signum(d1);
		}

		this.guardianEye.showModel = true;
		float f8 = EntityBabyGuardian.func_175471_a(f6);
		this.guardianTail[0].rotateAngleY = MathHelper.sin(f8) * (float)Math.PI * 0.05F;
		this.guardianTail[1].rotateAngleY = MathHelper.sin(f8) * (float)Math.PI * 0.1F;
		this.guardianTail[1].rotationPointX = -1.5F;
		this.guardianTail[1].rotationPointY = 0.5F;
		this.guardianTail[1].rotationPointZ = 14.0F;
		this.guardianTail[2].rotateAngleY = MathHelper.sin(f8) * (float)Math.PI * 0.15F;
		this.guardianTail[2].rotationPointX = 0.5F;
		this.guardianTail[2].rotationPointY = 0.5F;
		this.guardianTail[2].rotationPointZ = 6.0F;
	}
}