package org.violeterra.matchainfused;

import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.ReadOnlyScoreInfo;
import net.minecraft.world.scores.Scoreboard;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

public final class HealthHandler {
    private static final String HEARTS_OBJECTIVE = "matchainfused_hearts";
    private static final double MIN_HEALTH = 20.0D;
    private static final double MAX_HEALTH = 60.0D;

    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) {
            return;
        }

        Scoreboard scoreboard = player.level().getScoreboard();
        Objective objective = scoreboard.getObjective(HEARTS_OBJECTIVE);
        if (objective == null) {
            return;
        }

        ReadOnlyScoreInfo score = scoreboard.getPlayerScoreInfo(player, objective);
        if (score == null) {
            return;
        }

        double desiredHealth = Math.max(MIN_HEALTH, Math.min(MAX_HEALTH, score.value()));
        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth == null || maxHealth.getBaseValue() == desiredHealth) {
            return;
        }

        maxHealth.setBaseValue(desiredHealth);
        if (player.getHealth() > desiredHealth) {
            player.setHealth((float) desiredHealth);
        }
    }
}
