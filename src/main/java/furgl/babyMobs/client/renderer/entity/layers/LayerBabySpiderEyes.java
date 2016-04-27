package furgl.babyMobs.client.renderer.entity.layers;

import furgl.babyMobs.client.renderer.entity.mob.RenderBabySpider;
import furgl.babyMobs.common.entity.monster.EntityBabySpider;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerSpiderEyes;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

public class LayerBabySpiderEyes extends LayerSpiderEyes
{
    private static final ResourceLocation field_177150_a = new ResourceLocation("textures/entity/spider_eyes.png");
	private final RenderBabySpider renderBabySpider;

	public LayerBabySpiderEyes(RenderBabySpider p_i46109_1_)
	{
		super(null);
		this.renderBabySpider = p_i46109_1_;
	}

	public void func_177148_a(EntityBabySpider p_177148_1_, float p_177148_2_, float p_177148_3_, float p_177148_4_, float p_177148_5_, float p_177148_6_, float p_177148_7_, float p_177148_8_)
	{
		this.renderBabySpider.bindTexture(field_177150_a);
		GlStateManager.enableBlend();
		GlStateManager.disableAlpha();
		GlStateManager.blendFunc(1, 1);

		if (p_177148_1_.isInvisible())
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
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.renderBabySpider.getMainModel().render(p_177148_1_, p_177148_2_, p_177148_3_, p_177148_5_, p_177148_6_, p_177148_7_, p_177148_8_);
		int k = p_177148_1_.getBrightnessForRender(p_177148_4_);
		i = k % 65536;
		j = k / 65536;
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
		this.renderBabySpider.func_177105_a(p_177148_1_, p_177148_4_);
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
		this.func_177148_a((EntityBabySpider)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
	}



}
