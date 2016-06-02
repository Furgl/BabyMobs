package furgl.babyMobs.client.model;

import furgl.babyMobs.common.entity.monster.EntityBabyShulker;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelShulker;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyShulker extends ModelShulker
{
    private ModelRenderer field_187067_b; //bottom
    private ModelRenderer field_187068_c; //top
    public ModelRenderer field_187066_a; //head

    public ModelBabyShulker()
    {
        this.textureHeight = 64;
        this.textureWidth = 64;
        this.field_187068_c = new ModelRenderer(this);
        this.field_187067_b = new ModelRenderer(this);
        this.field_187066_a = new ModelRenderer(this);
        this.field_187068_c.setTextureOffset(0, 0).addBox(-8.0F, -16.0F, -8.0F, 16, 12, 16);
        this.field_187068_c.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.field_187067_b.setTextureOffset(0, 28).addBox(-8.0F, -8.0F, -8.0F, 16, 8, 16);
        this.field_187067_b.setRotationPoint(0.0F, 24.0F, 0.0F);
        this.field_187066_a.setTextureOffset(0, 52).addBox(-3.0F, 0.0F, -3.0F, 6, 6, 6);
        this.field_187066_a.setRotationPoint(0.0F, 12.0F, 0.0F);
    }

    public int func_187065_a()
    {
        return 28;
    }

    /**
     * Used for easily adding entity-dependent animations. The second and third float params here are the same second
     * and third as in the setRotationAngles method.
     */
    public void setLivingAnimations(EntityLivingBase entitylivingbaseIn, float p_78086_2_, float p_78086_3_, float partialTickTime)
    {
    }

    /**
     * Sets the model's various rotation angles. For bipeds, par1 and par2 are used for animating the movement of arms
     * and legs, where par1 represents the time(so that arms and legs swing back and forth) and par2 represents how
     * "far" arms and legs can swing at most.
     */
    public void setRotationAngles(float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor, Entity entityIn)
    {
        EntityBabyShulker entityshulker = (EntityBabyShulker)entityIn;
        float f = ageInTicks - (float)entityshulker.ticksExisted;
        float f1 = (0.5F + entityshulker.func_184688_a(f)) * (float)Math.PI;
        float f2 = -1.0F + MathHelper.sin(f1);
        float f3 = 0.0F;

        if (f1 > (float)Math.PI)
        {
            f3 = MathHelper.sin(ageInTicks * 0.1F) * 0.7F;
        }

        this.field_187068_c.setRotationPoint(0.0F, 16.0F + MathHelper.sin(f1) * 8.0F + f3, 0.0F);

        if (entityshulker.func_184688_a(f) > 0.3F)
        {
            this.field_187068_c.rotateAngleY = f2 * f2 * f2 * f2 * (float)Math.PI * 0.125F;
        }
        else
        {
            this.field_187068_c.rotateAngleY = 0.0F;
        }

        this.field_187066_a.rotateAngleX = headPitch * 0.017453292F;
        this.field_187066_a.rotateAngleY = netHeadYaw * 0.017453292F;
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
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

		this.field_187066_a.render(scale); //head
		this.field_187067_b.render(scale); //bottom
		this.field_187068_c.render(scale); //top

		GlStateManager.popMatrix();
    }
}