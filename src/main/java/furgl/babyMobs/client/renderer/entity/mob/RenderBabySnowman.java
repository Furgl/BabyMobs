package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabySnowman;
import furgl.babyMobs.common.entity.monster.EntityBabySnowman;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabySnowman extends RenderLiving
{
    private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");

    public RenderBabySnowman(RenderManager p_i46140_1_)
    {
        super(p_i46140_1_, new ModelBabySnowman(), 0.5F);
    }

    protected ResourceLocation func_180587_a(EntityBabySnowman p_180587_1_)
    {
        return snowManTextures;
    }

    public ModelBabySnowman func_177123_g()
    {
        return (ModelBabySnowman)super.getMainModel();
    }

    @Override
	public ModelBase getMainModel()
    {
        return this.func_177123_g();
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_180587_a((EntityBabySnowman)entity);
    }
}