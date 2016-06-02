package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelIronGolem;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyIronGolem extends ModelIronGolem
{
    public ModelBabyIronGolem()
    {
        this(0.0F);
    }

    public ModelBabyIronGolem(float p_i1161_1_)
    {
        this(p_i1161_1_, -7.0F);
    }

    public ModelBabyIronGolem(float p_i46362_1_, float p_i46362_2_)
    {
       super(p_i46362_1_, p_i46362_2_);
    }

    @Override
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_, float p_78088_6_, float p_78088_7_)
    {
    	isChild = true;
    	
		 // scale the whole thing for big or small entities
		float scaleFactor = 0.5F;
		GlStateManager.pushMatrix();
	    GlStateManager.translate(0F, 1.5F-1.5F*scaleFactor, 0F);
	    GlStateManager.scale(scaleFactor, scaleFactor, scaleFactor);
    	
    	this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);
        this.ironGolemHead.render(p_78088_7_);
        this.ironGolemBody.render(p_78088_7_);
        this.ironGolemLeftLeg.render(p_78088_7_);
        this.ironGolemRightLeg.render(p_78088_7_);
        this.ironGolemRightArm.render(p_78088_7_);
        this.ironGolemLeftArm.render(p_78088_7_);
        
        GlStateManager.popMatrix();
    }
}