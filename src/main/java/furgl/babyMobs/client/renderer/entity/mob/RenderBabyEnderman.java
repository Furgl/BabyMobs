package furgl.babyMobs.client.renderer.entity.mob;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import furgl.babyMobs.client.model.ModelBabyEnderman;
import furgl.babyMobs.client.renderer.entity.layers.LayerBabyEndermanEyes;
import furgl.babyMobs.client.renderer.entity.layers.LayerBabyHeldBlock;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyEnderman extends RenderLiving<EntityBabyEnderman>
{
	private static final ResourceLocation beam = new ResourceLocation("babymobs:textures/entity/enderman_laser.png");
	private static final ResourceLocation endermanTextures = new ResourceLocation("textures/entity/enderman/enderman.png");
	private ModelBabyEnderman babyEndermanModel;
	private Random rnd = new Random();

	public RenderBabyEnderman(RenderManager renderManager)
	{
		super(renderManager, new ModelBabyEnderman(0.0F), 0.25F);
		this.babyEndermanModel = (ModelBabyEnderman) super.mainModel;
		this.addLayer(new LayerBabyEndermanEyes(this));
		this.addLayer(new LayerBabyHeldBlock(this));
	}

	/**
	 * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
	 * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
	 * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
	 * double d2, float f, float f1). But JAD is pre 1.5 so doe
	 */
	@Override
	public void doRender(EntityBabyEnderman entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
		this.babyEndermanModel.isCarrying = entity.func_175489_ck().getBlock().getMaterial() != Material.air;
		this.babyEndermanModel.isAttacking = entity.isScreaming();

		if (entity.isScreaming())
		{
			double d3 = 0.02D;
			x += this.rnd.nextGaussian() * d3;
			z += this.rnd.nextGaussian() * d3;
		}

		//TODO beam
		EntityLivingBase entitylivingbase = entity.getTargetedEntity();

		if (entitylivingbase != null && !entity.isDead)
		{
			entity.setScreaming(true);
			Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            this.bindTexture(beam);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, 10497.0F);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, 10497.0F);
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.depthMask(true);
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240.0F, 240.0F);
            GlStateManager.tryBlendFuncSeparate(770, 1, 1, 0);
            float f6 = entity.getEyeHeight();
            GlStateManager.pushMatrix();
            if (entity.isScreaming())
            	GlStateManager.translate((float)x, (float)y + f6 + 0.3F, (float)z);
            else 
            	GlStateManager.translate((float)x, (float)y + f6 + 0.0F, (float)z);
            Vec3 vec3 = this.func_177110_a(entitylivingbase, entitylivingbase.height * 0.5D, partialTicks);
            Vec3 vec31 = this.func_177110_a(entity, f6, partialTicks);
            Vec3 vec32 = vec3.subtract(vec31);
            //double d3 = vec32.lengthVector();
            double d0 = vec32.lengthVector() + 0.0D;
            vec32 = vec32.normalize();
            float f7 = (float)Math.acos(vec32.yCoord);
            float f8 = (float)Math.atan2(vec32.zCoord, vec32.xCoord);
            GlStateManager.rotate((((float)Math.PI / 2F) + -f8) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f7 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            //double d4 = (f4 * 0.05D * (1.0D - (1 & 1) * 2.5D));
            //worldrenderer.startDrawingQuads();
            //worldrenderer.setColorRGBA(200, 0, 200, 255);
           /* double d15 = Math.cos(d4*0D + Math.PI) * 0.2D;
            double d16 = Math.sin(d4*0D + Math.PI) * 0.2D;
            double d17 = Math.cos(d4*0D + 0.0D) * 0.2D;
            double d18 = Math.sin(d4*0D + 0.0D) * 0.2D;
            double d25 = -1.0F + f5;
            double d26 = d3 * 2.5D + d25;*/
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_TEX_COLOR);
            int j = 200;
            int k = 0;
            int l = 200;
            int i = 1;
            float f2 = 0;//(float)entity.worldObj.getTotalWorldTime() + partialTicks;
            float f3 = f2 * 0.5F % 1.0F;
            double d1 = (double)f2 * 0.05D * (1.0D - (double)(i & 1) * 2.5D);
            double d2 = (double)i * 0.2D;
            double d3 = d2 * 1.41D;
            double d4 = 0.0D + Math.cos(d1 + 2.356194490192345D) * d3;
            double d5 = 0.0D + Math.sin(d1 + 2.356194490192345D) * d3;
            double d6 = 0.0D + Math.cos(d1 + (Math.PI / 4D)) * d3;
            double d7 = 0.0D + Math.sin(d1 + (Math.PI / 4D)) * d3;
            double d8 = 0.0D + Math.cos(d1 + 3.9269908169872414D) * d3;
            double d9 = 0.0D + Math.sin(d1 + 3.9269908169872414D) * d3;
            double d10 = 0.0D + Math.cos(d1 + 5.497787143782138D) * d3;
            double d11 = 0.0D + Math.sin(d1 + 5.497787143782138D) * d3;
            double d12 = 0.0D + Math.cos(d1 + Math.PI) * d2;
            double d13 = 0.0D + Math.sin(d1 + Math.PI) * d2;
            double d14 = 0.0D + Math.cos(d1 + 0.0D) * d2;
            double d15 = 0.0D + Math.sin(d1 + 0.0D) * d2;
            double d16 = 0.0D + Math.cos(d1 + (Math.PI / 2D)) * d2;
            double d17 = 0.0D + Math.sin(d1 + (Math.PI / 2D)) * d2;
            double d18 = 0.0D + Math.cos(d1 + (Math.PI * 3D / 2D)) * d2;
            double d19 = 0.0D + Math.sin(d1 + (Math.PI * 3D / 2D)) * d2;
            double d22 = (double)(-1.0F + f3);
            double d23 = d0 * (0.5D / d2) + d22;
            worldrenderer.pos(d12, d0, d13).tex(0.5D, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d12, 0.0D, d13).tex(0.5D, d22).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d14, 0.0D, d15).tex(0.0D, d22).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d14, d0, d15).tex(0.0D, d23).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d16, d0, d17).tex(0.5D, d23).color(j, k, l, 255).endVertex();
            //worldrenderer.pos(d16, 0.0D, d17).tex(0.5D, d22).color(j, k, l, 255).endVertex();
            //worldrenderer.pos(d18, 0.0D, d19).tex(0.0D, d22).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d18, d0, d19).tex(0.0D, d23).color(j, k, l, 255).endVertex();
            double d24 = 0.0D;

            /*if (entity.ticksExisted % 2 == 0)
            {
                d24 = 0.5D;
            }*/

            worldrenderer.pos(d4, d0, d5).tex(0.5D, d24 + 0.5D).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d6, d0, d7).tex(1.0D, d24 + 0.5D).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d10, d0, d11).tex(1.0D, d24).color(j, k, l, 255).endVertex();
            worldrenderer.pos(d8, d0, d9).tex(0.5D, d24).color(j, k, l, 255).endVertex();
            //worldrenderer.addVertexWithUV(d15, d3, d16, 0.5D, d26);
            //worldrenderer.addVertexWithUV(d15, 0.0D, d16, 0.5D, d25);
            //worldrenderer.addVertexWithUV(d17, 0.0D, d18, 0, d25);
            //worldrenderer.addVertexWithUV(d17, d3, d18,0, d26);
            tessellator.draw();
            GlStateManager.popMatrix();
		}
	}

	private Vec3 func_177110_a(EntityLivingBase entityLivingBaseIn, double p_177110_2_, float p_177110_4_)
    {
        double d0 = entityLivingBaseIn.lastTickPosX + (entityLivingBaseIn.posX - entityLivingBaseIn.lastTickPosX) * (double)p_177110_4_;
        double d1 = p_177110_2_ + entityLivingBaseIn.lastTickPosY + (entityLivingBaseIn.posY - entityLivingBaseIn.lastTickPosY) * (double)p_177110_4_;
        double d2 = entityLivingBaseIn.lastTickPosZ + (entityLivingBaseIn.posZ - entityLivingBaseIn.lastTickPosZ) * (double)p_177110_4_;
        return new Vec3(d0, d1, d2);
    }
	//end

	/**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityBabyEnderman entity)
    {
        return endermanTextures;
    }
}