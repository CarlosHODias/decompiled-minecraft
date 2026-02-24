/*    */ package net.minecraft.world.level.chunk;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.world.level.levelgen.DebugLevelSource;
/*    */ import net.minecraft.world.level.levelgen.FlatLevelSource;
/*    */ import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
/*    */ 
/*    */ public class ChunkGenerators {
/*    */   public static MapCodec<? extends ChunkGenerator> bootstrap(Registry<MapCodec<? extends ChunkGenerator>> registry) {
/* 11 */     Registry.register(registry, "noise", NoiseBasedChunkGenerator.CODEC);
/* 12 */     Registry.register(registry, "flat", FlatLevelSource.CODEC);
/* 13 */     return (MapCodec<? extends ChunkGenerator>)Registry.register(registry, "debug", DebugLevelSource.CODEC);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/chunk/ChunkGenerators.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */