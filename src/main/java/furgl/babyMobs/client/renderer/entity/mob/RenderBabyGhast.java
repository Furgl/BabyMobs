package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyGhast;
import furgl.babyMobs.common.entity.monster.EntityBabyGhast;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyGhast extends RenderLiving
{
	private static final ResourceLocation ghastTextures = new ResourceLocation("textures/entity/ghast/ghast.png");
	private static final ResourceLocation ghastShootingTextures = new ResourceLocation("textures/entity/ghast/ghast_shooting.png");

	public RenderBabyGhast(RenderManager p_i46174_1_)
	{
		super(p_i46174_1_, new ModelBabyGhast(), 0.5F);
	}

	protected ResourceLocation func_180576_a(EntityBabyGhast p_180576_1_)
	{
		return p_180576_1_.func_110182_bF() ? ghastShootingTextures : ghastTextures;
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	protected void preRenderCallback(EntityBabyGhast p_77041_1_, float p_77041_2_)
	{
		float f1 = 1.0F;
		float f2 = (8.0F + f1) / 2.0F;
		float f3 = (8.0F + 1.0F / f1) / 2.0F;
		GlStateManager.scale(f3, f2, f3);
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
	}

	/**
	 * Allows the render to do any OpenGL state modifications necessary before the model is rendered. Args:
	 * entityLiving, partialTickTime
	 */
	@Override
	protected void preRenderCallback(EntityLivingBase p_77041_1_, float p_77041_2_)
	{
		this.preRenderCallback((EntityBabyGhast)p_77041_1_, p_77041_2_);
	}

	/**
	 * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
	 */
	@Override
	protected ResourceLocation getEntityTexture(Entity entity)
	{
		return this.func_180576_a((EntityBabyGhast)entity);
	}
}