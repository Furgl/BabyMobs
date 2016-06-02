package furgl.babyMobs.client.renderer.entity.mob;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderOcelot;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.ResourceLocation;

public class RenderBabyOcelot extends RenderOcelot
{
	private static final ResourceLocation blackOcelotTextures = new ResourceLocation("babymobs:textures/entity/baby_ocelot.png");
	
	public RenderBabyOcelot(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
    {
        super(renderManagerIn, modelBaseIn, shadowSizeIn);
    }
	
	@Override
	protected ResourceLocation getEntityTexture(EntityOcelot entity)
	{
		return blackOcelotTextures;
	}
}
