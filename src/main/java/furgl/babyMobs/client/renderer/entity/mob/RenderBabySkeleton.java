package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabySkeleton;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBabySkeleton extends RenderLiving
{
    private static final ResourceLocation skeletonTextures = new ResourceLocation("textures/entity/skeleton/skeleton.png");

	public RenderBabySkeleton(RenderManager renderManager, ModelBase par1ModelBase,float shadowSize) 
	{
		super(renderManager, new ModelBabySkeleton(), 0.5F);
		this.addLayer(new LayerHeldItem(this));
		this.addLayer(new LayerBipedArmor(this)
        {
            @Override
			protected void func_177177_a()
            {
                this.field_177189_c = new ModelBabySkeleton(0.5F, true);
                this.field_177186_d = new ModelBabySkeleton(1.0F, true);
            }
        });
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return skeletonTextures;
	}
}
