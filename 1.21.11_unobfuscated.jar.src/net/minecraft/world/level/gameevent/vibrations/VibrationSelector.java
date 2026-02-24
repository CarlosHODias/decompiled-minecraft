/*    */ package net.minecraft.world.level.gameevent.vibrations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import org.apache.commons.lang3.tuple.Pair;
/*    */ 
/*    */ public class VibrationSelector {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)VibrationInfo.CODEC.lenientOptionalFieldOf("event").forGetter(()), (App)Codec.LONG.fieldOf("tick").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, VibrationSelector::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<VibrationSelector> CODEC;
/*    */   private Optional<Pair<VibrationInfo, Long>> currentVibrationData;
/*    */   
/*    */   public VibrationSelector(Optional<VibrationInfo> currentVibration, long tick) {
/* 18 */     this.currentVibrationData = currentVibration.map(vibrationInfo -> Pair.of(vibrationInfo, tick));
/*    */   }
/*    */   
/*    */   public VibrationSelector() {
/* 22 */     this.currentVibrationData = Optional.empty();
/*    */   }
/*    */   
/*    */   public void addCandidate(VibrationInfo newVibration, long tickTime) {
/* 26 */     if (shouldReplaceVibration(newVibration, tickTime)) {
/* 27 */       this.currentVibrationData = Optional.of(Pair.of(newVibration, tickTime));
/*    */     }
/*    */   }
/*    */   
/*    */   private boolean shouldReplaceVibration(VibrationInfo newVibration, long tickTime) {
/* 32 */     if (this.currentVibrationData.isEmpty()) {
/* 33 */       return true;
/*    */     }
/* 35 */     Pair<VibrationInfo, Long> previousData = this.currentVibrationData.get();
/* 36 */     long previousTick = (Long)previousData.getRight();
/* 37 */     if (tickTime != previousTick)
/*    */     {
/* 39 */       return false;
/*    */     }
/* 41 */     VibrationInfo previousVibration = (VibrationInfo)previousData.getLeft();
/* 42 */     if (newVibration.distance() < previousVibration.distance())
/* 43 */       return true; 
/* 44 */     if (newVibration.distance() > previousVibration.distance()) {
/* 45 */       return false;
/*    */     }
/* 47 */     return (VibrationSystem.getGameEventFrequency(newVibration.gameEvent()) > VibrationSystem.getGameEventFrequency(previousVibration.gameEvent()));
/*    */   }
/*    */   
/*    */   public Optional<VibrationInfo> chosenCandidate(long time) {
/* 51 */     if (this.currentVibrationData.isEmpty()) {
/* 52 */       return Optional.empty();
/*    */     }
/* 54 */     if ((Long)((Pair)this.currentVibrationData.get()).getRight() < time) {
/* 55 */       return Optional.of((VibrationInfo)((Pair)this.currentVibrationData.get()).getLeft());
/*    */     }
/* 57 */     return Optional.empty();
/*    */   }
/*    */   
/*    */   public void startOver() {
/* 61 */     this.currentVibrationData = Optional.empty();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/gameevent/vibrations/VibrationSelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */