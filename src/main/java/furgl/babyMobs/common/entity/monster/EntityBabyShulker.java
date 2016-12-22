package furgl.babyMobs.common.entity.monster;

import java.util.Iterator;

import furgl.babyMobs.client.gui.achievements.Achievements;
import furgl.babyMobs.common.entity.ModEntities;
import furgl.babyMobs.common.entity.ai.EntityAIBabyHurtByTarget;
import furgl.babyMobs.common.entity.projectile.EntityBabyShulkerBullet;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraftforge.common.util.FakePlayer;

public class EntityBabyShulker extends EntityShulker
{	
	private float f;

	public EntityBabyShulker(World worldIn)
	{
		super(worldIn);
		this.setSize(0.5F, 0.5F);
		this.experienceValue = (int)(this.experienceValue * 2.5F);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.35D);
		this.targetTasks.addTask(1, new EntityAIBabyHurtByTarget(this, true, new Class[0]));
	}

	@Override
	public void onDeath(DamageSource cause) //first achievement
	{
		if (!this.worldObj.isRemote && cause.getEntity() instanceof EntityPlayer && !(cause.getEntity() instanceof FakePlayer))
			((EntityPlayer)cause.getEntity()).addStat(Achievements.achievementWhyAreTheySoStrong);
		super.onDeath(cause);
	}

	@Override
	protected boolean canDropLoot()
	{
		return true;
	}

	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return ModEntities.getSpawnEgg(this.getClass());
	}

	@Override
	public boolean isChild()
	{
		return true;
	}

	@Override
	public float getEyeHeight()
	{
		return 0.3F;
	}

	@Override
	protected void initEntityAI()
	{
		super.initEntityAI();
		Iterator itr = this.tasks.taskEntries.iterator();
		while (itr.hasNext()) {
			if (((EntityAITasks.EntityAITaskEntry) itr.next()).action.getClass().getSimpleName().equals("AIAttack"))
				itr.remove(); 
		}
		this.tasks.addTask(4, new EntityBabyShulker.AIAttack());
	}

	@Override
	public void onUpdate()
	{
		super.onUpdate();

		float f1 = (float)this.getPeekTick() * 0.01F;
		if (this.f > f1)
			this.f = MathHelper.clamp_float(this.f - 0.05F, f1, 1.0F);
		else if (this.f < f1)
			this.f = MathHelper.clamp_float(this.f + 0.05F, 0.0F, f1);
		double d3 = 0.25D - (double)MathHelper.sin((0.5F + this.f) * (float)Math.PI) * 0.25D;
		EnumFacing enumfacing2 = this.getAttachmentFacing();
		switch (enumfacing2)
		{
		case DOWN:
		default:
			this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.25D, this.posY, this.posZ - 0.25D, this.posX + 0.25D, this.posY + 0.5D + d3, this.posZ + 0.25D));
			break;
		case UP:
			this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.25D, this.posY - d3 + 0.5D, this.posZ - 0.25D, this.posX + 0.25D, this.posY + 1.0D, this.posZ + 0.25D));
			break;
		case NORTH:
			this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.25D, this.posY + 0.25D, this.posZ - 0.5D, this.posX + 0.25D, this.posY + 0.75D, this.posZ + 0.0D + d3));
			break;
		case SOUTH:
			this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.25D, this.posY + 0.25D, this.posZ - 0.0D - d3, this.posX + 0.25D, this.posY + 0.75D, this.posZ + 0.5D));
			break;
		case WEST:
			this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.5D, this.posY + 0.25D, this.posZ - 0.25D, this.posX + 0.0D + d3, this.posY + 0.75D, this.posZ + 0.25D));
			break;
		case EAST:
			this.setEntityBoundingBox(new AxisAlignedBB(this.posX - 0.0D - d3, this.posY + 0.25D, this.posZ - 0.25D, this.posX + 0.5D, this.posY + 0.75D, this.posZ + 0.25D));
		}
	}
	
	class AIAttack extends EntityAIBase
    {
        private int field_188520_b;

        public AIAttack()
        {
            this.setMutexBits(3);
        }

        /**
         * Returns whether the EntityAIBase should begin execution.
         */
        public boolean shouldExecute()
        {
            EntityLivingBase entitylivingbase = EntityBabyShulker.this.getAttackTarget();
            return entitylivingbase != null && entitylivingbase.isEntityAlive() ? EntityBabyShulker.this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL : false;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting()
        {
            this.field_188520_b = 20;
            EntityBabyShulker.this.updateArmorModifier(100);
        }

        /**
         * Resets the task
         */
        public void resetTask()
        {
            EntityBabyShulker.this.updateArmorModifier(0);
        }

        /**
         * Updates the task
         */
        public void updateTask()
        {
            if (EntityBabyShulker.this.worldObj.getDifficulty() != EnumDifficulty.PEACEFUL)
            {
                --this.field_188520_b;
                EntityLivingBase entitylivingbase = EntityBabyShulker.this.getAttackTarget();
                EntityBabyShulker.this.getLookHelper().setLookPositionWithEntity(entitylivingbase, 180.0F, 180.0F);
                double d0 = EntityBabyShulker.this.getDistanceSqToEntity(entitylivingbase);

                if (d0 < 400.0D)
                {
                    if (this.field_188520_b <= 0)
                    {
                        this.field_188520_b = 20 + EntityBabyShulker.this.rand.nextInt(10) * 20 / 2;
                        EntityBabyShulkerBullet entityshulkerbullet = new EntityBabyShulkerBullet(EntityBabyShulker.this.worldObj, EntityBabyShulker.this, entitylivingbase, EntityBabyShulker.this.getAttachmentFacing().getAxis());
                        EntityBabyShulker.this.worldObj.spawnEntityInWorld(entityshulkerbullet);
                        EntityBabyShulker.this.playSound(SoundEvents.ENTITY_SHULKER_SHOOT, 2.0F, (EntityBabyShulker.this.rand.nextFloat() - EntityBabyShulker.this.rand.nextFloat()) * 0.2F + 1.0F);
                    }
                }
                else
                {
                    EntityBabyShulker.this.setAttackTarget((EntityLivingBase)null);
                }

                super.updateTask();
            }
        }
    }
}