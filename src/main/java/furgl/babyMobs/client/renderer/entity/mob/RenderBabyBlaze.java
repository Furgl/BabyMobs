package furgl.babyMobs.client.renderer.entity.mob;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.client.model.ModelBabyBlaze;
import furgl.babyMobs.common.entity.monster.EntityBabyBlaze;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderBabyBlaze extends RenderLiving
{
    private static final ResourceLocation blazeTextures = new ResourceLocation("textures/entity/blaze.png");
                                                         
    public RenderBabyBlaze()
    {
        super(new ModelBabyBlaze(), 0.25F);
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