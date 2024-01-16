package dev.zprestige.mud.util.impl;

import dev.zprestige.mud.Mud;
import dev.zprestige.mud.util.MC;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.Vec3d;

import java.util.Arrays;
import java.util.Objects;

public class EntityUtil implements MC {
    private static final KeyBinding[] movementKeys = new KeyBinding[] {
            mc.options.forwardKey,
            mc.options.rightKey,
            mc.options.backKey,
            mc.options.leftKey,
            mc.options.jumpKey,
            mc.options.sprintKey
    };

    public static EntityOtherPlayerMP setupEntity(PlayerEntity entityPlayer, Vec3d vec) {
        EntityOtherPlayerMP entityOtherPlayerMP1 = new EntityOtherPlayerMP(mc.world, entityPlayer.getGameProfile());
        entityOtherPlayerMP1.copyLocationAndAnglesFrom(entityPlayer);
        entityOtherPlayerMP1.rotationYawHead = entityPlayer.rotationYawHead;
        entityOtherPlayerMP1.prevRotationYawHead = entityPlayer.rotationYawHead;
        entityOtherPlayerMP1.rotationYaw = entityPlayer.rotationYaw;
        entityOtherPlayerMP1.prevRotationYaw = entityPlayer.rotationYaw;
        entityOtherPlayerMP1.rotationPitch = entityPlayer.rotationPitch;
        entityOtherPlayerMP1.prevRotationPitch = entityPlayer.rotationPitch;
        entityOtherPlayerMP1.cameraYaw = entityPlayer.rotationYaw;
        entityOtherPlayerMP1.cameraPitch = entityPlayer.rotationPitch;
        entityOtherPlayerMP1.limbSwing = entityPlayer.limbSwing;
        entityOtherPlayerMP1.setPosition(vec.x, vec.y, vec.z);
        return entityOtherPlayerMP1;
    }


    public static boolean isMoving(){
        return Arrays.stream(getMovementKeys()).anyMatch(KeyBinding::isPressed);
    }

    public static KeyBinding[] getMovementKeys() {
        return movementKeys;
    }

    public static void setSpeed(double speed) {
        float[] dir = forward(speed);
        mc.player.motionX = dir[0];
        mc.player.motionZ = dir[1];
    }

    public static float getBaseMoveSpeed() {
        float baseSpeed = 0.2873f;
        if (mc.player != null && mc.player.isPotionActive(Objects.requireNonNull(Potion.getPotionById(1)))) {
            int amplifier = Objects.requireNonNull(mc.player.getActivePotionEffect(Objects.requireNonNull(Potion.getPotionById(1)))).getAmplifier();
            baseSpeed *= 1.0 + 0.2 * (amplifier + 1);
        }
        return baseSpeed;
    }

    public static float[] forward(double speed) {
        float forward = mc.player.movementInput.moveForward,
                strafe = mc.player.movementInput.moveStrafe,
                yaw = mc.player.prevRotationYaw + (mc.player.rotationYaw - mc.player.prevRotationYaw) * mc.getRenderPartialTicks();
        if (forward != 0.0f) {
            if (strafe > 0.0f) {
                yaw += ((forward > 0.0f) ? -45 : 45);
            } else if (strafe < 0.0f) {
                yaw += ((forward > 0.0f) ? 45 : -45);
            }
            strafe = 0.0f;
            if (forward > 0.0f) {
                forward = 1.0f;
            } else if (forward < 0.0f) {
                forward = -1.0f;
            }
        }
        double sin = Math.sin(Math.toRadians(yaw + 90.0f)),
                cos = Math.cos(Math.toRadians(yaw + 90.0f)),
                posX = forward * speed * cos + strafe * speed * sin,
                posZ = forward * speed * sin - strafe * speed * cos;
        return new float[]{(float) posX, (float) posZ};
    }




    public static PlayerEntity getEntityPlayer(float range) {
        PlayerEntity lowest = null;
        for (PlayerEntity PlayerEntity : mc.world.playerEntities) {
            if (PlayerEntity.equals(mc.player)) {
                continue;
            }
            if (PlayerEntity.isDead || PlayerEntity.getHealth() <= 0.0f) {
                continue;
            }
            if (mc.player.getDistance(PlayerEntity) > range) {
                continue;
            }
            if (Mud.friendManager.contains(PlayerEntity)) {
                continue;
            }
            if (lowest == null || mc.player.getDistance(PlayerEntity) < mc.player.getDistance(lowest)) {
                lowest = PlayerEntity;
            }
        }
        return lowest;
    }

    public static float getHealth(EntityPlayer entityPlayer) {
        return entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount();
    }
}
