package mods.battlegear2.items.arrows;


import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;

public class EntityEnderArrow extends AbstractMBArrow{

    public EntityEnderArrow(World par1World, EntityLivingBase par2EntityLivingBase, float par3) {
        super(par1World, par2EntityLivingBase, par3);
    }

    public EntityEnderArrow(World par1World, EntityLivingBase par2EntityLivingBase, EntityLivingBase par3EntityLivingBase, float par4, float par5) {
        super(par1World, par2EntityLivingBase, par3EntityLivingBase, par4, par5);
    }

    @Override
    public void onUpdate() {
        super.onUpdate();


    }

    @Override
    public boolean onHitEntity(Entity entityHit) {
    	if(entityHit instanceof EntityLivingBase){
            if (!this.worldObj.isRemote && shootingEntity instanceof EntityPlayerMP){
                EntityPlayerMP entityplayermp = (EntityPlayerMP)this.shootingEntity;
                double x = entityplayermp.posX;
                double y = entityplayermp.posY;
                double z = entityplayermp.posZ;
            	if (!entityplayermp.playerNetServerHandler.connectionClosed && entityplayermp.worldObj == this.worldObj)
                {
                    EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, entityHit.posX+0.5F, entityHit.posY, entityHit.posZ+0.5F, 9.0F);
                    if (!MinecraftForge.EVENT_BUS.post(event)){
                        if (shootingEntity.isRiding()){
                            shootingEntity.mountEntity((Entity)null);
                        }
                        entityplayermp.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                        entityplayermp.fallDistance = 0.0F;
                        entityplayermp.attackEntityFrom(DamageSource.fall, event.attackDamage);
                    }
                    event = new EnderTeleportEvent((EntityLivingBase) entityHit, x+0.5F, y, z+0.5F, ((EntityLivingBase) entityHit).getMaxHealth()/10);
                    if (!MinecraftForge.EVENT_BUS.post(event)){
                        if (entityHit.isRiding()){
                        	entityHit.mountEntity((Entity)null);
                        }
                        ((EntityLivingBase) entityHit).setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                        entityHit.fallDistance = 0.0F;
                        entityHit.attackEntityFrom(DamageSource.fall, event.attackDamage);
                    }
                }
            }
        	this.setDead();
            return true;
        }
    	this.setDead();
        return false;
    }

    @Override
    public void onHitGround(int x, int y, int z) {

        while(y < 255 && !(worldObj.isAirBlock(x,y,z) && worldObj.isAirBlock(x,y+1,z))){
            y++;
        }

        if(shootingEntity != null){


            if (!this.worldObj.isRemote)
            {
                if (shootingEntity instanceof EntityPlayerMP)
                {

                    for (int i = 0; i < 32; ++i)
                    {
                        this.worldObj.spawnParticle("portal", x+0.5F, y + this.rand.nextDouble() * 2.0D, z+0.5F, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian());
                    }

                    EntityPlayerMP entityplayermp = (EntityPlayerMP)this.shootingEntity;
                    if (!entityplayermp.playerNetServerHandler.connectionClosed && entityplayermp.worldObj == this.worldObj)
                    {
                        EnderTeleportEvent event = new EnderTeleportEvent(entityplayermp, x+0.5F, y, z+0.5F, 9.0F);
                        if (!MinecraftForge.EVENT_BUS.post(event))
                        {
                            if (shootingEntity.isRiding())
                            {
                                shootingEntity.mountEntity((Entity)null);
                            }

                            entityplayermp.setPositionAndUpdate(event.targetX, event.targetY, event.targetZ);
                            entityplayermp.fallDistance = 0.0F;
                            entityplayermp.attackEntityFrom(DamageSource.fall, event.attackDamage);
                        }
                    }

                }
            }

        }

        this.setDead();
    }



}
