package furgl.babyMobs.client.renderer.entity.layers;

import furgl.babyMobs.client.renderer.entity.mob.RenderBabyDragon;
import furgl.babyMobs.common.entity.monster.EntityBabyDragon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBabyEnderDragonEyes implements LayerRenderer
{
    private static final ResourceLocation TEXTURE = new ResourceLocation("textures/entity/enderdragon/dragon_eyes.png");
    private final RenderBabyDragon dragonRenderer;

    public LayerBabyEnderDragonEyes(RenderBabyDragon p_i46118_1_)
    {
        this.dragonRenderer = p_i46118_1_;
    }

    public void func_177210_a(EntityBabyDragon p_177210_1_, float p_177210_2_, float p_177210_3_, float p_177210_4_, float p_177210_5_, float p_177210_6_, float p_177210_7_, float p_177210_8_)
    {
        this.dragonRenderer.bindTexture(TEXTURE);
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.blendFunc(1, 1);
        GlStateManager.disableLighting();
        GlStateManager.depthFunc(514);
        char c0 = 61680;
        int i = c0 % 65536;
        int j = c0 / 65536;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, i / 1.0F, j / 1.0F);
        GlStateManager.enableLighting();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.dragonRenderer.getMainModel().render(p_177210_1_, p_177210_2_, p_177210_3_, p_177210_5_, p_177210_6_, p_177210_7_, p_177210_8_);
        this.dragonRenderer.func_177105_a(p_177210_1_, p_177210_4_);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.depthFunc(515);
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return false;
    }

    @Override
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        this.func_177210_a((EntityBabyDragon)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}