package furgl.babyMobs.client.renderer.entity.mob;

import java.util.Random;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.client.model.ModelBabyEnderman;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

@SideOnly(Side.CLIENT)
public class RenderBabyEnderman extends RenderLiving
{
	private static final ResourceLocation beam = new ResourceLocation("babymobs:textures/entity/enderman_laser.png");
	private static final ResourceLocation endermanTextures = new ResourceLocation("textures/entity/enderman/enderman.png");
	/** The model of the enderman */
	private ModelBabyEnderman endermanModel;
	private Random rnd = new Random();

	public RenderBabyEnderman()
	{
		super(new ModelBabyEnderman(0.0F), 0.5F);
		this.endermanModel = (ModelBabyEnderman)super.mainModel;
		this.setRenderPassModel(this.endermanModel);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	public void doRender(EntityBabyEnderman entity, double x, double y, double z, float p_76986_8_, float partialTicks)
	{
		this.endermanModel.isCarrying = entity.getCarriedBlock().getMaterial() != Material.air;
		this.endermanModel.isAttacking = entity.isScreaming();

		if (entity.isScreaming())
		{
			double d3 = 0.02D;
			x += this.rnd.nextGaussian() * d3;
			z += this.rnd.nextGaussian() * d3;
		}

		super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
		//TODO beam
		EntityLivingBase entitylivingbase = entity.getTargetedEntity();

		if (entitylivingbase != null && entity.getHealth() > 0)
		{
			Tessellator tessellator = Tessellator.instance;
			this.bindTexture(beam);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
			GL11.glDisable(GL11.GL_LIGHTING);
			GL11.glDisable(GL11.GL_CULL_FACE);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glDepthMask(true);
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
			OpenGlHelper.glBlendFunc(770, 1, 1, 0);
			float f4 = entity.worldObj.getTotalWorldTime() + partialTicks;
			float f5 = f4 * 0.5F % 1.0F;
			float f6 = entity.getEyeHeight()-0.1F;
			GL11.glPushMatrix();
			if (entity.isScreaming())
				GL11.glTranslatef((float)x, (float)y + f6 + 0.3F, (float)z);
			else 
				GL11.glTranslatef((float)x, (float)y + f6 + 0.1F, (float)z);
			Vec3 vec3 = this.func_177110_a(entitylivingbase, entitylivingbase.height * 0.5D-1.5D, partialTicks);
			Vec3 vec31 = this.func_177110_a(entity, f6, partialTicks);
			Vec3 vec32 = vec3.subtract(vec31);
			double d3 = vec32.lengthVector()+0.1D;
			vec32 = vec32.normalize();
			float f7 = (float)Math.acos(vec32.yCoord);
			float f8 = (float)Math.atan2(vec32.zCoord, vec32.xCoord);
			GL11.glRotatef((((float)Math.PI / 2F) + -f8) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
			GL11.glRotatef(f7 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
			double d4 = (f4 * 0.05D * (1.0D - (1 & 1) * 2.5D));
			tessellator.startDrawingQuads();
			tessellator.setColorRGBA(200, 0, 200, 255);
			double d15 = Math.cos(d4*0D + Math.PI) * 0.2D;
			double d16 = Math.sin(d4*0D + Math.PI) * 0.2D;
			double d17 = Math.cos(d4*0D + 0.0D) * 0.2D;
			double d18 = Math.sin(d4*0D + 0.0D) * 0.2D;
			double d25 = -1.0F + f5;
			double d26 = d3 * (0.5D / 0.2D) + d25;
			tessellator.addVertexWithUV(d15, -d3, d16, 0.5D, d26);
			tessellator.addVertexWithUV(d15, 0, d16, 0.5D, d25);
			tessellator.addVertexWithUV(d17, 0, d18, 0, d25);
			tessellator.addVertexWithUV(d17, -d3, d18, 0, d26);
			tessellator.draw();
			GL11.glPopMatrix();
		}
	}

	private Vec3 func_177110_a(EntityLivingBase p_177110_1_, double p_177110_2_, float p_177110_4_)
	{
		double d1 = p_177110_1_.lastTickPosX + (p_177110_1_.posX - p_177110_1_.lastTickPosX) * p_177110_4_;
		double d2 = p_177110_2_ + p_177110_1_.lastTickPosY + (p_177110_1_.posY - p_177110_1_.lastTickPosY) * p_177110_4_;
		double d3 = p_177110_1_.lastTickPosZ + (p_177110_1_.posZ - p_177110_1_.lastTickPosZ) * p_177110_4_;
		return Vec3.createVectorHelper(d1, d2, d3);
	}
	//end

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityBabyEnderman p_110775_1_)
	{
		return endermanTextures;
	}

	protected void renderEquippedItems(EntityBabyEnderman p_77029_1_, float p_77029_2_)
	{
		super.renderEquippedItems(p_77029_1_, p_77029_2_);

		if (p_77029_1_.getCarriedBlock().getMaterial() != Material.air)
		{
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
			GL11.glPushMatrix();
			float f1 = 0.5F;
			//side, up/down, in/out 
			GL11.glTranslatef(0.0F, 1.0F, -0.6F);
			GL11.glRotatef(20.0F, 1.0F, 0.0F, 0.0F);
			GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
			GL11.glScalef(-f1, -f1, f1);
			int i = p_77029_1_.getBrightnessForRender(p_77029_2_);
			int j = i % 65536;
			int k = i / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.bindTexture(TextureMap.locationBlocksTexture);
			this.field_147909_c.renderBlockAsItem(p_77029_1_.getCarriedBlock(), p_77029_1_.getCarryingData(), 1.0F);
			GL11.glPopMatrix();
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
		}
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	protected int shouldRenderPass(EntityBabyEnderman p_77032_1_, int p_77032_2_, float p_77032_3_)
	{
		if (p_77032_2_ != 0)
		{
			return -1;
		}
		else
		{
			// this.bindTexture(endermanEyesTexture);
			float f1 = 1.0F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
			GL11.glDisable(GL11.GL_LIGHTING);

			if (p_77032_1_.isInvisible())
			{
				GL11.glDepthMask(false);
			}
			else
			{
				GL11.glDepthMask(true);
			}

			char c0 = 61680;
			int j = c0 % 65536;
			int k = c0 / 65536;
			OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
			GL11.glEnable(GL11.GL_LIGHTING);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, f1);
			return 1;
		}
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(EntityLiving entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		this.doRender((EntityBabyEnderman)entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	/**
	 * Queries whether should render the specified pass or not.
	 */
	@Override
	protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_)
	{
		return this.shouldRenderPass((EntityBabyEnderman)p_77032_1_, p_77032_2_, p_77032_3_);
	}

	@Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_)
	{
		this.renderEquippedItems((EntityBabyEnderman)p_77029_1_, p_77029_2_);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(EntityLivingBase entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		this.doRender((EntityBabyEnderman)entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_)
	{
		return this.getEntityTexture((EntityBabyEnderman)p_110775_1_);
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doesn't do that.
	 */
	@Override
	public void doRender(Entity entity, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_, float p_76986_9_)
	{
		this.doRender((EntityBabyEnderman)entity, p_76986_2_, p_76986_4_, p_76986_6_, p_76986_8_, p_76986_9_);
	}
}