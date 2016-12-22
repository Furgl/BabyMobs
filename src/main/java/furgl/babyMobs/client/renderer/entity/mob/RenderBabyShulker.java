package furgl.babyMobs.client.renderer.entity.mob;

import furgl.babyMobs.client.model.ModelBabyShulker;
import furgl.babyMobs.common.entity.monster.EntityBabyShulker;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderShulker;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderBabyShulker extends RenderShulker
{
	public RenderBabyShulker(RenderManager renderManager)
	{
		super(renderManager);
		this.mainModel = new ModelBabyShulker();
		for (LayerRenderer layer : this.layerRenderers) {
			if (layer.getClass().getSimpleName().equals("HeadLayer"))
			{
				this.layerRenderers.remove(layer);
				break;
			}
		}
		this.addLayer(new RenderBabyShulker.HeadLayer());
	}

	@SideOnly(Side.CLIENT)
	class HeadLayer implements LayerRenderer<EntityBabyShulker>
	{
		private HeadLayer()
		{
		}

		public void doRenderLayer(EntityBabyShulker entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
		{
			GlStateManager.pushMatrix();

			/*switch (entitylivingbaseIn.func_184696_cZ())
			{
			case DOWN:
			default:
				break;
			case EAST:
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(1.0F, -1.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				break;
			case WEST:
				GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(-1.0F, -1.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				break;
			case NORTH:
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0F, -1.0F, -1.0F);
				break;
			case SOUTH:
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0F, -1.0F, 1.0F);
				break;
			case UP:
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0F, -2.0F, 0.0F);
			}*/

			ModelRenderer modelrenderer = ((ModelBabyShulker)RenderBabyShulker.this.getMainModel()).head;
			modelrenderer.rotateAngleY = netHeadYaw * 0.017453292F;
			modelrenderer.rotateAngleX = headPitch * 0.017453292F;
            RenderBabyShulker.this.bindTexture(RenderShulker.SHULKER_ENDERGOLEM_TEXTURE[entitylivingbaseIn.func_190769_dn().getMetadata()]);

			float f6 = 2F;
			GlStateManager.scale(1.5F / f6, 1.5F / f6, 1.5F / f6);
			GlStateManager.translate(0.0F, 16.0F * scale + 0.3F,  0.0F);

			GlStateManager.popMatrix();
			GlStateManager.pushMatrix();
			GlStateManager.scale(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GlStateManager.translate(0.0F, 24.0F * scale, 0.0F);
			
			switch (entitylivingbaseIn.getAttachmentFacing())
			{
			case DOWN:
			default:
				break;
			case EAST:
				GlStateManager.rotate(90.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(1.0F, -1.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				break;
			case WEST:
				GlStateManager.rotate(-90.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(-1.0F, -1.0F, 0.0F);
				GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
				break;
			case NORTH:
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0F, -1.0F, -1.0F);
				break;
			case SOUTH:
				GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
				GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0F, -1.0F, 1.0F);
				break;
			case UP:
				GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
				GlStateManager.translate(0.0F, -2.0F, 0.0F);
			}

			modelrenderer.render(scale);
			GlStateManager.popMatrix();
		}

		@Override
		public boolean shouldCombineTextures() 
		{
			return false;
		}
	}
}