package furgl.babyMobs.client.renderer.entity.mob;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.common.entity.monster.EntityZombiePig;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderZombiePig extends RenderLiving
{
    private static final ResourceLocation saddledPigTextures = new ResourceLocation("textures/entity/pig/pig_saddle.png");
    private static final ResourceLocation pigTextures = new ResourceLocation("babymobs:textures/entity/zombie_pig.png");

    public RenderZombiePig(ModelBase p_i1265_1_, ModelBase p_i1265_2_, float p_i1265_3_)
    {
        super(p_i1265_1_, p_i1265_3_);
        this.setRenderPassModel(p_i1265_2_);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityZombiePig p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        if (p_77032_2_ == 0 && p_77032_1_.getSaddled())
        {
            this.bindTexture(saddledPigTextures);
            return 1;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityZombiePig p_110775_1_)
    {
        return pigTextures;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        return this.shouldRenderPass((EntityZombiePig)p_77032_1_, p_77032_2_, p_77032_3_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityZombiePig)p_110775_1_);
    }
}