package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelWitch;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyWitch extends ModelWitch
{
    public ModelBabyWitch(float p_i46361_1_)
    {
        super(p_i46361_1_);
    }
    
    @Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
    	isChild = true;
    	
		float scaleFactor = 0.5F;
		GlStateManager.pushMatrix();
	    GlStateManager.translate(0F, 1.5F-1.5F*scaleFactor, 0F);
	    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
    	
    	this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.villagerHead.render(p_78088_7_);
        this.villagerBody.render(p_78088_7_);
        this.rightVillagerLeg.render(p_78088_7_);
        this.leftVillagerLeg.render(p_78088_7_);
        this.villagerArms.render(p_78088_7_);
        
    	GlStateManager.popMatrix();
    }
}