package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.common.entity.monster.EntityZombiePig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombiePig extends RenderLiving
{
    private static final ResourceLocation pigTextures = new ResourceLocation("babymobs:textures/entity/zombie_pig.png");

    public RenderZombiePig(RenderManager p_i46149_1_, ModelBase p_i46149_2_, float p_i46149_3_)
    {
        super(p_i46149_1_, p_i46149_2_, p_i46149_3_);
    }

    protected ResourceLocation func_180583_a(EntityZombiePig p_180583_1_)
    {
        return pigTextures;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_180583_a((EntityZombiePig)entity);
    }
}