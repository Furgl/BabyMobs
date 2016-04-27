package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.common.entity.monster.EntityBabyOcelot;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyOcelot extends RenderLiving<EntityBabyOcelot>
{
	private static final ResourceLocation blackOcelotTextures = new ResourceLocation("babymobs:textures/entity/baby_ocelot.png");

	public RenderBabyOcelot(RenderManager renderManagerIn, ModelBase modelBaseIn, float shadowSizeIn)
	{
		super(renderManagerIn, modelBaseIn, shadowSizeIn);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	protected ResourceLocation getEntityTexture(EntityBabyOcelot entity)
	{
		return blackOcelotTextures;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityBabyOcelot entitylivingbaseIn, float partialTickTime)
	{
		super.preRenderCallback(entitylivingbaseIn, partialTickTime);

		if (entitylivingbaseIn.isTamed())
		{
			GlStateManager.scale(0.8F, 0.8F, 0.8F);
		}
	}
}