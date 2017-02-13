package furgl.babyMobs.client.renderer.entity.mob;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

public class RenderBabyOcelot extends RenderOcelot
{
	private static final ResourceLocation blackOcelotTextures = new ResourceLocation("babymobs:textures/entity/baby_ocelot.png");
	
	public RenderBabyOcelot(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }
	
	@Override
	protected ResourceLocation getEntityTexture(EntityOcelot entity)
	{
		return blackOcelotTextures;
	}
}
