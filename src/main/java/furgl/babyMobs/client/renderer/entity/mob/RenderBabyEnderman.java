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
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyEnderman extends RenderLiving
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
    public void doRender(EntityBabyEnderman entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.babyEndermanModel.isCarrying = entity.func_175489_ck().getBlock().getMaterial() != Material.air;
        this.babyEndermanModel.isAttacking = entity.isScreaming();

        if (entity.isScreaming())
        {
            double d3 = 0.02D;
            x += this.rnd.nextGaussian() * d3;
            z += this.rnd.nextGaussian() * d3;
        }

        super.doRender(entity, x, y, z, p_76986_8_, partialTicks);
        //TODO beam
        EntityLivingBase entitylivingbase = entity.getTargetedEntity();

        if (entitylivingbase != null && !entity.isDead)
        {
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
            float f4 = entity.worldObj.getTotalWorldTime() + partialTicks;
            float f5 = f4 * 0.5F % 1.0F;
            float f6 = entity.getEyeHeight();
            GlStateManager.pushMatrix();
            if (entity.isScreaming())
            	GlStateManager.translate((float)x, (float)y + f6 + 0.3F, (float)z);
            else 
            	GlStateManager.translate((float)x, (float)y + f6 + 0.0F, (float)z);
            Vec3 vec3 = this.func_177110_a(entitylivingbase, entitylivingbase.height * 0.5D, partialTicks);
            Vec3 vec31 = this.func_177110_a(entity, f6, partialTicks);
            Vec3 vec32 = vec3.subtract(vec31);
            double d3 = vec32.lengthVector();
            vec32 = vec32.normalize();
            float f7 = (float)Math.acos(vec32.yCoord);
            float f8 = (float)Math.atan2(vec32.zCoord, vec32.xCoord);
            GlStateManager.rotate((((float)Math.PI / 2F) + -f8) * (180F / (float)Math.PI), 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(f7 * (180F / (float)Math.PI), 1.0F, 0.0F, 0.0F);
            double d4 = (f4 * 0.05D * (1.0D - (1 & 1) * 2.5D));
            worldrenderer.startDrawingQuads();
            worldrenderer.setColorRGBA(200, 0, 200, 255);
            double d15 = Math.cos(d4*0D + Math.PI) * 0.2D;
            double d16 = Math.sin(d4*0D + Math.PI) * 0.2D;
            double d17 = Math.cos(d4*0D + 0.0D) * 0.2D;
            double d18 = Math.sin(d4*0D + 0.0D) * 0.2D;
            double d25 = -1.0F + f5;
            double d26 = d3 * 2.5D + d25;
            worldrenderer.addVertexWithUV(d15, d3, d16, 0.5D, d26);
            worldrenderer.addVertexWithUV(d15, 0.0D, d16, 0.5D, d25);
            worldrenderer.addVertexWithUV(d17, 0.0D, d18, 0, d25);
            worldrenderer.addVertexWithUV(d17, d3, d18,0, d26);
            tessellator.draw();
            GlStateManager.popMatrix();
        }
    }
    
    private Vec3 func_177110_a(EntityLivingBase p_177110_1_, double p_177110_2_, float p_177110_4_)
    {
        double d1 = p_177110_1_.lastTickPosX + (p_177110_1_.posX - p_177110_1_.lastTickPosX) * p_177110_4_;
        double d2 = p_177110_2_ + p_177110_1_.lastTickPosY + (p_177110_1_.posY - p_177110_1_.lastTickPosY) * p_177110_4_;
        double d3 = p_177110_1_.lastTickPosZ + (p_177110_1_.posZ - p_177110_1_.lastTickPosZ) * p_177110_4_;
        return new Vec3(d1, d2, d3);
    }
    //end

    protected ResourceLocation func_180573_a(EntityBabyEnderman p_180573_1_)
    {
        return endermanTextures;
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    @Override
	public void doRender(EntityLiving entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityBabyEnderman)entity, x, y, z, p_76986_8_, partialTicks);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    @Override
	public void doRender(EntityLivingBase entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityBabyEnderman)entity, x, y, z, p_76986_8_, partialTicks);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_180573_a((EntityBabyEnderman)entity);
    }

    /**
     * Actually renders the given argument. This is a synthetic bridge method, always casting down its argument and then
     * handing it off to a worker function which does the actual work. In all probabilty, the class Render is generic
     * (Render<T extends Entity>) and this method has signature public void func_76986_a(T entity, double d, double d1,
     * double d2, float f, float f1). But JAD is pre 1.5 so doe
     */
    @Override
	public void doRender(Entity entity, double x, double y, double z, float p_76986_8_, float partialTicks)
    {
        this.doRender((EntityBabyEnderman)entity, x, y, z, p_76986_8_, partialTicks);
    }
}