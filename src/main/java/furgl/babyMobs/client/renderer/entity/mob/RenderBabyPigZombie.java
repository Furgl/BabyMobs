package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyPigZombie;
import furgl.babyMobs.common.entity.monster.EntityBabyPigZombie;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.client.renderer.entity.layers.LayerHeldItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyPigZombie extends RenderBiped
{
    private static final ResourceLocation field_177120_j = new ResourceLocation("textures/entity/zombie_pigman.png");
    
    public RenderBabyPigZombie(RenderManager p_i46148_1_)
    {
        super(p_i46148_1_, new ModelBabyPigZombie(), 0.5F, 1.0F);
        this.addLayer(new LayerHeldItem(this));
        this.addLayer(new LayerBipedArmor(this)
        {
        	@Override
            protected void initArmor()
            {
                this.field_177189_c = new ModelBabyPigZombie(0.5F, true);
                this.field_177186_d = new ModelBabyPigZombie(1.0F, true);
            }
        });
    }

    protected ResourceLocation func_177119_a(EntityBabyPigZombie p_177119_1_)
    {
        return field_177120_j;
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityLiving entity)
    {
        return this.func_177119_a((EntityBabyPigZombie)entity);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity entity)
    {
        return this.func_177119_a((EntityBabyPigZombie)entity);
    }
}