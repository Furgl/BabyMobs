package furgl.babyMobs.client.renderer.entity.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieChicken extends RenderLiving<EntityChicken>
{
    private static final ResourceLocation chickenTextures = new ResourceLocation("babymobs:textures/entity/zombie_chicken.png");

    public RenderZombieChicken(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityChicken entity)
    {
        return chickenTextures;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityChicken livingBase, float partialTicks)
    {
        float f = livingBase.field_70888_h + (livingBase.wingRotation - livingBase.field_70888_h) * partialTicks;
        float f1 = livingBase.field_70884_g + (livingBase.destPos - livingBase.field_70884_g) * partialTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}