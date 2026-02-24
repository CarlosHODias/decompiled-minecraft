/*    */ package net.minecraft.world.effect;
/*    */ 
/*    */ import java.util.List;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Position;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.util.StringUtil;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.phys.Vec3;
/*    */ 
/*    */ public final class MobEffectUtil
/*    */ {
/*    */   public static Component formatDuration(MobEffectInstance instance, float scale, float tickrate) {
/* 18 */     if (instance.isInfiniteDuration()) {
/* 19 */       return (Component)Component.translatable("effect.duration.infinite");
/*    */     }
/* 21 */     int duration = Mth.floor(instance.getDuration() * scale);
/* 22 */     return (Component)Component.literal(StringUtil.formatTickDuration(duration, tickrate));
/*    */   }
/*    */   
/*    */   public static boolean hasDigSpeed(LivingEntity mob) {
/* 26 */     return (mob.hasEffect(MobEffects.HASTE) || mob.hasEffect(MobEffects.CONDUIT_POWER));
/*    */   }
/*    */   
/*    */   public static int getDigSpeedAmplification(LivingEntity mob) {
/* 30 */     int a = 0, b = 0;
/* 31 */     if (mob.hasEffect(MobEffects.HASTE)) {
/* 32 */       a = mob.getEffect(MobEffects.HASTE).getAmplifier();
/*    */     }
/* 34 */     if (mob.hasEffect(MobEffects.CONDUIT_POWER)) {
/* 35 */       b = mob.getEffect(MobEffects.CONDUIT_POWER).getAmplifier();
/*    */     }
/*    */     
/* 38 */     return Math.max(a, b);
/*    */   }
/*    */   
/*    */   public static boolean hasWaterBreathing(LivingEntity mob) {
/* 42 */     return (mob.hasEffect(MobEffects.WATER_BREATHING) || mob.hasEffect(MobEffects.CONDUIT_POWER) || mob.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS));
/*    */   }
/*    */   
/*    */   public static boolean shouldEffectsRefillAirsupply(LivingEntity mob) {
/* 46 */     return (!mob.hasEffect(MobEffects.BREATH_OF_THE_NAUTILUS) || mob.hasEffect(MobEffects.WATER_BREATHING) || mob.hasEffect(MobEffects.CONDUIT_POWER));
/*    */   }
/*    */   
/*    */   public static List<ServerPlayer> addEffectToPlayersAround(ServerLevel level, Entity source, Vec3 position, double radius, MobEffectInstance effectInstance, int displayEffectLimit) {
/* 50 */     Holder<MobEffect> effect = effectInstance.getEffect();
/* 51 */     List<ServerPlayer> players = level.getPlayers(input -> 
/* 52 */         (input.gameMode.isSurvival() && (source == null || !source.isAlliedTo((Entity)input)) && position.closerThan((Position)input.position(), radius) && (!input.hasEffect(effect) || input.getEffect(effect).getAmplifier() < effectInstance.getAmplifier() || input.getEffect(effect).endsWithin(displayEffectLimit - 1))));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 62 */     players.forEach(player -> player.addEffect(new MobEffectInstance(effectInstance), source));
/*    */     
/* 64 */     return players;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/effect/MobEffectUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */