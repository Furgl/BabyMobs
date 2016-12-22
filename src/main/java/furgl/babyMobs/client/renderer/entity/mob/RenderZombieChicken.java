package furgl.babyMobs.client.renderer.entity.mob;

import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieChicken extends RenderChicken
{
    private static final ResourceLocation chickenTextures = new ResourceLocation("babymobs:textures/entity/zombie_chicken.png");

    public RenderZombieChicken(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityChicken entity)
    {
        return chickenTextures;
    }
}