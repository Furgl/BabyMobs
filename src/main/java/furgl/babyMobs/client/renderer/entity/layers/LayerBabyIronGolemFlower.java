package furgl.babyMobs.client.renderer.entity.layers;

import furgl.babyMobs.client.model.ModelBabyIronGolem;
import furgl.babyMobs.client.renderer.entity.mob.RenderBabyIronGolem;
import furgl.babyMobs.common.entity.monster.EntityBabyIronGolem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class LayerBabyIronGolemFlower implements LayerRenderer
{
    private final RenderBabyIronGolem field_177154_a;

    public LayerBabyIronGolemFlower(RenderBabyIronGolem p_i46107_1_)
    {
        this.field_177154_a = p_i46107_1_;
    }

    public void func_177153_a(EntityBabyIronGolem p_177153_1_, float p_177153_2_, float p_177153_3_, float p_177153_4_, float p_177153_5_, float p_177153_6_, float p_177153_7_, float p_177153_8_)
    {
        if (p_177153_1_.getHoldRoseTick() != 0)
        {
            BlockRendererDispatcher blockrendererdispatcher = Minecraft.getMinecraft().getBlockRendererDispatcher();
            GlStateManager.enableRescaleNormal();
            GlStateManager.pushMatrix();
            GlStateManager.rotate(5.0F + 180.0F * ((ModelBabyIronGolem)this.field_177154_a.getMainModel()).ironGolemRightArm.rotateAngleX / (float)Math.PI, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
            //GlStateManager.translate(-0.9375F, -0.625F, -0.9375F); orig
            GlStateManager.translate(-0.6F, 0.2F, -0.9F);
            float f7 = 0.5F;
            GlStateManager.scale(f7, -f7, f7);
            int i = p_177153_1_.getBrightnessForRender(p_177153_4_);
            int j = i % 65536;
            int k = i / 65536;
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, j / 1.0F, k / 1.0F);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.field_177154_a.bindTexture(TextureMap.locationBlocksTexture);
            blockrendererdispatcher.renderBlockBrightness(Blocks.red_flower.getDefaultState(), 1.0F);
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
        this.func_177153_a((EntityBabyIronGolem)p_177141_1_, p_177141_2_, p_177141_3_, p_177141_4_, p_177141_5_, p_177141_6_, p_177141_7_, p_177141_8_);
    }
}