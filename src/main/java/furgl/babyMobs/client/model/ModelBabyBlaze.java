package furgl.babyMobs.client.model;

import net.minecraft.client.model.ModelBlaze;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ModelBabyBlaze extends ModelBlaze
{
    /** The sticks that fly around the Blaze. */
    private ModelRenderer[] blazeSticks = new ModelRenderer[12];
    private ModelRenderer blazeHead;

    public ModelBabyBlaze()
    {
    	super();
    	this.blazeSticks = (ModelRenderer[]) ReflectionHelper.getPrivateValue(ModelBlaze.class, this, 0);
    	this.blazeHead = (ModelRenderer) ReflectionHelper.getPrivateValue(ModelBlaze.class, this, 1);
    }

    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
	public void render(Entity par1, float par2, float par3, float par4, float par5, float par6, float par7)
    {
    	isChild = true;
		this.setRotationAngles(par2, par3, par4, par5, par6, par7, par1);
		GlStateManager.pushMatrix();
		float f6 = 2F;
		GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
		GlStateManager.translate(0.0F, 16.0F * par7 + 0.1F,  0.0F);
		this.blazeHead.render(par7);
		GlStateManager.popMatrix();
		GlStateManager.pushMatrix();
		GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
		GlStateManager.translate(0.0F, 24.0F * par7, 0.0F);

        for (int i = 0; i < this.blazeSticks.length; ++i)
        {
            this.blazeSticks[i].render(par7);
        }
        
        GlStateManager.popMatrix();
    }
}