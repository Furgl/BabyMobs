package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyWitherSkeleton;
import furgl.babyMobs.common.entity.monster.EntityBabyWitherSkeleton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyWitherSkeleton extends RenderBiped<EntityBabyWitherSkeleton>
{
    private static final ResourceLocation witherWitherSkeletonTextures = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

    public RenderBabyWitherSkeleton(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBabyWitherSkeleton(), 0.5F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.field_177189_c = new ModelBabyWitherSkeleton(0.5F, true);
                this.field_177186_d = new ModelBabyWitherSkeleton(1.0F, true);
            }
        });
    }

    /**
     * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
     * entityLiving, partialTickTime
     */
    protected void preRenderCallback(EntityBabyWitherSkeleton entitylivingbaseIn, float partialTickTime)
    {
            GlStateManager.scale(1.2F, 1.2F, 1.2F);
    }

    public void transformHeldFull3DItemLayer()
    {
        GlStateManager.translate(0.09375F, 0.1875F, 0.0F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityBabyWitherSkeleton entity)
    {
        return witherWitherSkeletonTextures;
    }
}