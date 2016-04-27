package furgl.babyMobs.client.renderer.entity.mob;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import furgl.babyMobs.client.model.ModelBabySnowman;
import furgl.babyMobs.common.entity.monster.EntityBabySnowman;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderBabySnowman extends RenderLiving
{
    private static final ResourceLocation snowManTextures = new ResourceLocation("textures/entity/snowman.png");
    /** A reference to the Snowman model in RenderBabySnowman. */
    private ModelBabySnowman snowmanModel;

    public RenderBabySnowman()
    {
        super(new ModelBabySnowman(), 0.3F);
        this.snowmanModel = (ModelBabySnowman)super.mainModel;
        this.setRenderPassModel(this.snowmanModel);
    }

    protected void renderEquippedItems(EntityBabySnowman p_77029_1_, float p_77029_2_)
    {
        super.renderEquippedItems(p_77029_1_, p_77029_2_);
        /*ItemStack itemstack = new ItemStack(Blocks.pumpkin, 1);

        if (itemstack.getItem() instanceof ItemBlock)
        {
            GL11.glPushMatrix();
            this.snowmanModel.head.postRender(0.0625F);

            IItemRenderer customRenderer = MinecraftForgeClient.getItemRenderer(itemstack, EQUIPPED);
            boolean is3D = (customRenderer != null && customRenderer.shouldUseRenderHelper(EQUIPPED, itemstack, BLOCK_3D));

            if (is3D || RenderBlocks.renderItemIn3d(Block.getBlockFromItem(itemstack.getItem()).getRenderType()))
            {
                float f1 = 0.625F;
                GL11.glTranslatef(0.0F, -0.34375F, 0.0F);
                GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
                GL11.glScalef(f1, -f1, f1);
            }

            this.renderManager.itemRenderer.renderItem(p_77029_1_, itemstack, 0);
            GL11.glPopMatrix();
        }*/
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityBabySnowman p_110775_1_)
    {
        return snowManTextures;
    }

    @Override
	protected void renderEquippedItems(EntityLivingBase p_77029_1_, float p_77029_2_)
    {
        this.renderEquippedItems((EntityBabySnowman)p_77029_1_, p_77029_2_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityBabySnowman)p_110775_1_);
    }
}