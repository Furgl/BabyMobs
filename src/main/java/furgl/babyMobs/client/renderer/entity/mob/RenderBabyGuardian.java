package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyGuardian;
import net.minecraft.client.renderer.entity.RenderGuardian;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyGuardian extends RenderGuardian
{    
    private static final ResourceLocation GUARDIAN_TEXTURE = new ResourceLocation("babyMobs:textures/entity/baby_guardian.png");
	
    public RenderBabyGuardian(RenderManager renderManager)
    {
    	super(renderManager);
        this.mainModel = new ModelBabyGuardian();
        this.shadowSize = 0.5F;
    }
    
    @Override
    protected ResourceLocation getEntityTexture(EntityGuardian entity)
    {
        return GUARDIAN_TEXTURE;
    }
}