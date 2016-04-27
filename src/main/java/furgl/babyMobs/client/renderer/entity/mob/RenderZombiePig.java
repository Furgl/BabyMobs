package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.common.entity.monster.EntityZombiePig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombiePig extends RenderLiving<EntityZombiePig>
{
    private static final ResourceLocation pigTextures = new ResourceLocation("babymobs:textures/entity/zombie_pig.png");

    public RenderZombiePig(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityZombiePig entity)
    {
        return pigTextures;
    }
}