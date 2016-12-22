package furgl.babyMobs.client.renderer.entity.mob;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPig;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombiePig extends RenderPig
{
    private static final ResourceLocation pigTextures = new ResourceLocation("babymobs:textures/entity/zombie_pig.png");

    public RenderZombiePig(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityPig entity)
    {
        return pigTextures;
    }
}