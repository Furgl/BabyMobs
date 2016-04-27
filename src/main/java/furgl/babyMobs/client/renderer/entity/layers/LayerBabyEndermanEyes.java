package furgl.babyMobs.client.renderer.entity.layers;

import furgl.babyMobs.client.renderer.entity.mob.RenderBabyEnderman;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBabyEndermanEyes implements LayerRenderer
{
    private static final ResourceLocation resourceLocation = new ResourceLocation("textures/entity/enderman/enderman_eyes.png");
	private final RenderBabyEnderman renderBabyEnderman;

	public LayerBabyEndermanEyes(RenderBabyEnderman renderBabyEnderman)
	{
		this.renderBabyEnderman = renderBabyEnderman;
	}

	public void func_177201_a(EntityBabyEnderman entityBabyEnderman, float par1, float par2, float par3, float par4, float par5, float par6, float par7)
	{
		this.renderBabyEnderman.bindTexture(resourceLocation);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(1, 1);
		GlStateManager.disableLighting();
		
		if (entityBabyEnderman.isInvisible())
		{
			GlStateManager.depthMask(false);
		}
		else
		{
			GlStateManager.depthMask(true);
		}
		char c0 = 61680;
		int i = c0 % 65536;
		int j = c0 / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		GlStateManager.enableLighting();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderBabyEnderman.getMainModel().render(entityBabyEnderman, par1, par2, par4, par5, par6, par7);
		this.renderBabyEnderman.func_177105_a(entityBabyEnderman, par3);
		GlStateManager.disableBlend();
		GlStateManager.enableAlpha();
	}

	 @Override
	public boolean shouldCombineTextures()
	    {
	        return false;
	    }

	    @Override
		public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
	    {
	        this.func_177201_a((EntityBabyEnderman)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	    }
}