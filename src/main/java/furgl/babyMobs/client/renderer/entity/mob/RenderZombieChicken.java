package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.common.entity.monster.EntityZombieChicken;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieChicken extends RenderLiving
{
    private static final ResourceLocation chickenTextures = new ResourceLocation("babymobs:textures/entity/zombie_chicken.png");

    public RenderZombieChicken(RenderManager p_i46188_1_, ModelBase p_i46188_2_, float p_i46188_3_)
    {
        super(p_i46188_1_, p_i46188_2_, p_i46188_3_);
    }

    protected ResourceLocation func_180568_a(EntityZombieChicken p_180568_1_)
    {
        return chickenTextures;
    }

    protected float func_180569_a(EntityZombieChicken p_180569_1_, float p_180569_2_)
    {
        float f1 = p_180569_1_.field_70888_h + (p_180569_1_.field_70886_e - p_180569_1_.field_70888_h) * p_180569_2_;
        float f2 = p_180569_1_.field_70884_g + (p_180569_1_.destPos - p_180569_1_.field_70884_g) * p_180569_2_;
        return (MathHelper.sin(f1) + 1.0F) * f2;
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    protected float handleRotationFloat(EntityLivingBase p_77044_1_, float p_77044_2_)
    {
        return this.func_180569_a((EntityZombieChicken)p_77044_1_, p_77044_2_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_180568_a((EntityZombieChicken)entity);
    }
}