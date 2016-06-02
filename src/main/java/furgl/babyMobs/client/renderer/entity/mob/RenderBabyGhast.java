package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyGhast;
import net.minecraft.client.renderer.entity.RenderGhast;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyGhast extends RenderGhast
{
	public RenderBabyGhast(RenderManager renderManager)
	{
		super(renderManager);
        this.mainModel = new ModelBabyGhast();
        this.shadowSize = 0.5F;
	}
}