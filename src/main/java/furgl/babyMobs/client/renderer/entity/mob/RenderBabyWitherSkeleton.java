package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyWitherSkeleton;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBabyWitherSkeleton extends RenderLiving
{
    private static final ResourceLocation witherSkeletonTextures = new ResourceLocation("textures/entity/skeleton/wither_skeleton.png");

	public RenderBabyWitherSkeleton(RenderManager renderManager, ModelBase par1ModelBase,float shadowSize) 
	{
		super(renderManager, new ModelBabyWitherSkeleton(), 0.5F);
		this.addLayer(new LayerBipedArmor(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return witherSkeletonTextures;
	}
}
