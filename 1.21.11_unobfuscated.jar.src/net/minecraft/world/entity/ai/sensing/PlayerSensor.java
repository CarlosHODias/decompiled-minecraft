/*    */ package net.minecraft.world.entity.ai.sensing;
/*    */ import com.google.common.collect.ImmutableSet;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Set;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.server.level.ServerPlayer;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.EntitySelector;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.ai.Brain;
/*    */ import net.minecraft.world.entity.ai.attributes.Attributes;
/*    */ import net.minecraft.world.entity.ai.memory.MemoryModuleType;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ 
/*    */ public class PlayerSensor extends Sensor<LivingEntity> {
/*    */   public Set<MemoryModuleType<?>> requires() {
/* 20 */     return (Set<MemoryModuleType<?>>)ImmutableSet.of(MemoryModuleType.NEAREST_PLAYERS, MemoryModuleType.NEAREST_VISIBLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected void doTick(ServerLevel level, LivingEntity body) {
/* 33 */     Objects.requireNonNull(body); List<Player> players = (List<Player>)level.players().stream().filter(EntitySelector.NO_SPECTATORS).filter(player -> body.closerThan((Entity)body, getFollowDistance(body))).sorted(Comparator.comparingDouble(body::distanceToSqr))
/* 34 */       .collect(Collectors.toList());
/*    */     
/* 36 */     Brain<?> brain = body.getBrain();
/* 37 */     brain.setMemory(MemoryModuleType.NEAREST_PLAYERS, players);
/* 38 */     List<Player> visiblePlayers = (List<Player>)players.stream()
/* 39 */       .filter(livingEntity -> isEntityTargetable(level, body, (LivingEntity)livingEntity))
/* 40 */       .collect(Collectors.toList());
/* 41 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_PLAYER, visiblePlayers.isEmpty() ? null : visiblePlayers.get(0));
/*    */ 
/*    */     
/* 44 */     List<Player> visibleAttackablePlayers = visiblePlayers.stream().filter(livingEntity -> isEntityAttackable(level, body, (LivingEntity)livingEntity)).toList();
/* 45 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYERS, visibleAttackablePlayers);
/*    */     
/* 47 */     brain.setMemory(MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER, visibleAttackablePlayers.isEmpty() ? null : visibleAttackablePlayers.get(0));
/*    */   }
/*    */ 
/*    */   
/*    */   protected double getFollowDistance(LivingEntity body) {
/* 52 */     return body.getAttributeValue(Attributes.FOLLOW_RANGE);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/sensing/PlayerSensor.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */