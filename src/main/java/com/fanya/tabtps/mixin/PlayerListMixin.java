package com.fanya.tabtps.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.PlayerTabOverlay;
import net.minecraft.network.chat.Component;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerTabOverlay.class)
public abstract class PlayerListMixin {
    @Inject(method = "render", at = @At("TAIL"))
    private void renderCustomText(PoseStack poseStack, int width, Scoreboard scoreboard, Objective objective, CallbackInfo ci) {
        Minecraft minecraft = Minecraft.getInstance();
        Font font = minecraft.font;

        int x = width / 2;
        int y = minecraft.getWindow().getGuiScaledHeight() - 20;

        String customText = "Test";

        font.draw(poseStack, Component.literal(customText), (float)x, (float)y, 0xFFFFFF);
    }
}
