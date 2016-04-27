package furgl.babyMobs.client.renderer.entity.layers;

import furgl.babyMobs.client.renderer.entity.mob.RenderBabyEnderman;
import furgl.babyMobs.common.entity.monster.EntityBabyEnderman;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBabyHeldBlock implements LayerRenderer
{
    private final RenderBabyEnderman field_177174_a;

    public LayerBabyHeldBlock(RenderBabyEnderman p_i46122_1_)
    {
        this.field_177174_a = p_i46122_1_;
    }

    public void func_177173_a(EntityBabyEnderman p_177173_1_, float p_177173_2_, float p_177173_3_, float p_177173_4_, float p_177173_5_, float p_177173_6_, float p_177173_7_, float p_177173_8_)
    {
        IBlockState iblockstate = p_177173_1_.func_175489_ck();

        if (iblockstate.getBlock().getMaterial() != Material.air)
        {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.6875F, -0.75F);
            GlStateManager.rotate(20.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(45.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.translate(0.25F, 0.65F, 0.25F);
            float f7 = 0.5F;
            GlStateManager.scale(-f7, -f7, f7);
            int i = p_177173_1_.getBrightnessForRender(p_177173_4_);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_177174_a.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(iblockstate, 1.0F);
            GlStateManager.popMatrix();
            GlStateManager.disableRescaleNormal();
        }
    }

    @Override
	public boolean shouldCombineTextures()
    {
        return false;
    }

    @Override
	public void doRenderLayer(EntityLivingBase p_177141_1_, float p_177141_2_, float p_177141_3_, float p_177141_4_, float p_177141_5_, float p_177141_6_, float p_177141_7_, float p_177141_8_)
    {
        this.func_177173_a((EntityBabyEnderman)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}