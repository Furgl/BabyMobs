package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyBlaze extends RenderLiving
{
    private static final ResourceLocation blazeTextures = new ResourceLocation("textures/entity/blaze.png");
    
    public RenderBabyBlaze(RenderManager p_i46191_1_)
    {
        super(p_i46191_1_, new ModelBabyBlaze(), 0.25F);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityBabyBlaze entity)
    {
        return blazeTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.getEntityTexture((EntityBabyBlaze)entity);
    }
}