package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabySpider;
import furgl.babyMobs.client.renderer.entity.layers.LayerBabySpiderEyes;
import furgl.babyMobs.common.entity.monster.EntityBabySpider;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabySpider<T extends EntityBabySpider> extends RenderLiving<T>
{
    private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");

    public RenderBabySpider(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelBabySpider(), 1.0F);
        this.addLayer(new LayerBabySpiderEyes(this));
    }

    protected float getDeathMaxRotation(T entityLivingBaseIn)
    {
        return 180.0F;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(T entity)
    {
        return spiderTextures;
    }
}