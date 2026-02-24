/*    */ package net.minecraft.world.level.dimension.end;
/*    */ 
/*    */ import com.google.common.collect.ImmutableList;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.BlockPos;
/*    */ import net.minecraft.server.level.ServerLevel;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.WorldGenLevel;
/*    */ import net.minecraft.world.level.levelgen.feature.Feature;
/*    */ import net.minecraft.world.level.levelgen.feature.SpikeFeature;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
/*    */ import net.minecraft.world.level.levelgen.feature.configurations.SpikeConfiguration;
/*    */ 
/*    */ public enum DragonRespawnAnimation {
/* 18 */   START
/*    */   {
/*    */     public void tick(ServerLevel level, EndDragonFight fight, List<EndCrystal> crystals, int time, BlockPos portal) {
/* 21 */       BlockPos beamPos = new BlockPos(0, 128, 0);
/* 22 */       for (EndCrystal respawnCrystal : crystals) {
/* 23 */         respawnCrystal.setBeamTarget(beamPos);
/*    */       }
/* 25 */       fight.setRespawnStage(PREPARING_TO_SUMMON_PILLARS);
/*    */     }
/*    */   },
/* 28 */   PREPARING_TO_SUMMON_PILLARS
/*    */   {
/*    */     public void tick(ServerLevel level, EndDragonFight fight, List<EndCrystal> crystals, int time, BlockPos portal) {
/* 31 */       if (time < 100) {
/* 32 */         if (time == 0 || time == 50 || time == 51 || time == 52 || time >= 95) {
/* 33 */           level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*    */         }
/*    */       } else {
/* 36 */         fight.setRespawnStage(SUMMONING_PILLARS);
/*    */       } 
/*    */     }
/*    */   },
/* 40 */   SUMMONING_PILLARS
/*    */   {
/*    */     public void tick(ServerLevel level, EndDragonFight fight, List<EndCrystal> crystals, int time, BlockPos portal) {
/* 43 */       int interval = 40;
/* 44 */       boolean startOfBeam = (time % 40 == 0);
/* 45 */       boolean endOfBeam = (time % 40 == 39);
/* 46 */       if (startOfBeam || endOfBeam) {
/* 47 */         List<SpikeFeature.EndSpike> spikes = SpikeFeature.getSpikesForLevel((WorldGenLevel)level);
/* 48 */         int index = time / 40;
/* 49 */         if (index < spikes.size()) {
/* 50 */           SpikeFeature.EndSpike spike = spikes.get(index);
/*    */           
/* 52 */           if (startOfBeam) {
/* 53 */             for (EndCrystal respawnCrystal : crystals) {
/* 54 */               respawnCrystal.setBeamTarget(new BlockPos(spike.getCenterX(), spike.getHeight() + 1, spike.getCenterZ()));
/*    */             }
/*    */           } else {
/* 57 */             int radius = 10;
/* 58 */             for (BlockPos pos : (Iterable<BlockPos>)BlockPos.betweenClosed(new BlockPos(
/* 59 */                   spike.getCenterX() - 10, spike.getHeight() - 10, spike.getCenterZ() - 10), new BlockPos(
/* 60 */                   spike.getCenterX() + 10, spike.getHeight() + 10, spike.getCenterZ() + 10)))
/*    */             {
/* 62 */               level.removeBlock(pos, false);
/*    */             }
/* 64 */             level.explode(null, (spike.getCenterX() + 0.5F), spike.getHeight(), (spike.getCenterZ() + 0.5F), 5.0F, Level.ExplosionInteraction.BLOCK);
/*    */             
/* 66 */             SpikeConfiguration configuration = new SpikeConfiguration(true, (List)ImmutableList.of(spike), new BlockPos(0, 128, 0));
/* 67 */             Feature.END_SPIKE.place((FeatureConfiguration)configuration, (WorldGenLevel)level, level.getChunkSource().getGenerator(), RandomSource.create(), new BlockPos(spike.getCenterX(), 45, spike.getCenterZ()));
/*    */           } 
/* 69 */         } else if (startOfBeam) {
/* 70 */           fight.setRespawnStage(SUMMONING_DRAGON);
/*    */         } 
/*    */       } 
/*    */     }
/*    */   },
/* 75 */   SUMMONING_DRAGON
/*    */   {
/*    */     public void tick(ServerLevel level, EndDragonFight fight, List<EndCrystal> crystals, int time, BlockPos portal) {
/* 78 */       if (time >= 100) {
/* 79 */         fight.setRespawnStage(END);
/* 80 */         fight.resetSpikeCrystals();
/* 81 */         for (EndCrystal crystal : crystals) {
/* 82 */           crystal.setBeamTarget(null);
/* 83 */           level.explode((Entity)crystal, crystal.getX(), crystal.getY(), crystal.getZ(), 6.0F, Level.ExplosionInteraction.NONE);
/* 84 */           crystal.discard();
/*    */         } 
/* 86 */       } else if (time >= 80) {
/* 87 */         level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/* 88 */       } else if (time == 0) {
/* 89 */         for (EndCrystal crystal : crystals) {
/* 90 */           crystal.setBeamTarget(new BlockPos(0, 128, 0));
/*    */         }
/* 92 */       } else if (time < 5) {
/* 93 */         level.levelEvent(3001, new BlockPos(0, 128, 0), 0);
/*    */       } 
/*    */     }
/*    */   },
/* 97 */   END {
/*    */     public void tick(ServerLevel level, EndDragonFight fight, List<EndCrystal> crystals, int time, BlockPos portal) {}
/*    */   };
/*    */   
/*    */   public abstract void tick(ServerLevel paramServerLevel, EndDragonFight paramEndDragonFight, List<EndCrystal> paramList, int paramInt, BlockPos paramBlockPos);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/dimension/end/DragonRespawnAnimation.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */