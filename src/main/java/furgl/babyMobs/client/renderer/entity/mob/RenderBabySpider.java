package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabySpider;
import furgl.babyMobs.client.renderer.entity.layers.LayerBabySpiderEyes;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public class RenderBabySpider extends RenderLiving
{
    private static final ResourceLocation spiderTextures = new ResourceLocation("textures/entity/spider/spider.png");

	public RenderBabySpider(RenderManager renderManager, ModelBase par1ModelBase,float shadowSize)
	{
		super(renderManager, new ModelBabySpider(), 0.5F);
		this.addLayer(new LayerBabySpiderEyes(this));
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity entity) {
		return spiderTextures;
	}
}
