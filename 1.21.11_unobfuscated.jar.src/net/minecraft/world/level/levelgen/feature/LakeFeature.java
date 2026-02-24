/*     */ package net.minecraft.world.level.levelgen.feature;
/*     */ 
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.util.RandomSource;
/*     */ import net.minecraft.world.level.WorldGenLevel;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*     */ 
/*     */ @Deprecated
/*     */ public class LakeFeature extends Feature<LakeFeature.Configuration> {
/*     */   public static final class Configuration extends Record implements net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration {
/*     */     private final BlockStateProvider fluid;
/*     */     private final BlockStateProvider barrier;
/*     */     public static final Codec<Configuration> CODEC;
/*     */     
/*  19 */     public Configuration(BlockStateProvider fluid, BlockStateProvider barrier) { this.fluid = fluid; this.barrier = barrier; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #19	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  19 */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration; } public BlockStateProvider fluid() { return this.fluid; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #19	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #19	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/world/level/levelgen/feature/LakeFeature$Configuration;
/*  19 */       //   0	8	1	o	Ljava/lang/Object; } public BlockStateProvider barrier() { return this.barrier; } static {
/*  20 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("fluid").forGetter(Configuration::fluid), (App)BlockStateProvider.CODEC.fieldOf("barrier").forGetter(Configuration::barrier)).apply((com.mojang.datafixers.kinds.Applicative)i, Configuration::new));
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*  26 */   private static final BlockState AIR = net.minecraft.world.level.block.Blocks.CAVE_AIR.defaultBlockState();
/*     */   
/*     */   public LakeFeature(Codec<Configuration> codec) {
/*  29 */     super(codec);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean place(FeaturePlaceContext<Configuration> context) {
/*  34 */     BlockPos origin = context.origin();
/*  35 */     WorldGenLevel level = context.level();
/*  36 */     RandomSource random = context.random();
/*  37 */     Configuration config = context.config();
/*     */     
/*  39 */     if (origin.getY() <= level.getMinY() + 4) {
/*  40 */       return false;
/*     */     }
/*     */     
/*  43 */     origin = origin.below(4);
/*     */     
/*  45 */     boolean[] grid = new boolean[2048];
/*     */     
/*  47 */     int spots = random.nextInt(4) + 4;
/*  48 */     for (int i = 0; i < spots; i++) {
/*  49 */       double xr = random.nextDouble() * 6.0D + 3.0D;
/*  50 */       double yr = random.nextDouble() * 4.0D + 2.0D;
/*  51 */       double zr = random.nextDouble() * 6.0D + 3.0D;
/*     */       
/*  53 */       double xp = random.nextDouble() * (16.0D - xr - 2.0D) + 1.0D + xr / 2.0D;
/*  54 */       double yp = random.nextDouble() * (8.0D - yr - 4.0D) + 2.0D + yr / 2.0D;
/*  55 */       double zp = random.nextDouble() * (16.0D - zr - 2.0D) + 1.0D + zr / 2.0D;
/*     */       
/*  57 */       for (int k = 1; k < 15; k++) {
/*  58 */         for (int zz = 1; zz < 15; zz++) {
/*  59 */           for (int yy = 1; yy < 7; yy++) {
/*  60 */             double xd = (k - xp) / xr / 2.0D;
/*  61 */             double yd = (yy - yp) / yr / 2.0D;
/*  62 */             double zd = (zz - zp) / zr / 2.0D;
/*  63 */             double d = xd * xd + yd * yd + zd * zd;
/*  64 */             if (d < 1.0D) {
/*  65 */               grid[(k * 16 + zz) * 8 + yy] = true;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  72 */     BlockState fluid = config.fluid().getState(random, origin);
/*     */     
/*  74 */     for (int xx = 0; xx < 16; xx++) {
/*  75 */       for (int zz = 0; zz < 16; zz++) {
/*  76 */         for (int yy = 0; yy < 8; yy++) {
/*  77 */           boolean check = (!grid[(xx * 16 + zz) * 8 + yy] && ((xx < 15 && grid[((xx + 1) * 16 + zz) * 8 + yy]) || (xx > 0 && grid[((xx - 1) * 16 + zz) * 8 + yy]) || (zz < 15 && grid[(xx * 16 + zz + 1) * 8 + yy]) || (zz > 0 && grid[(xx * 16 + zz - 1) * 8 + yy]) || (yy < 7 && grid[(xx * 16 + zz) * 8 + yy + 1]) || (yy > 0 && grid[(xx * 16 + zz) * 8 + yy - 1])));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */           
/*  86 */           if (check) {
/*  87 */             BlockState blockState = level.getBlockState(origin.offset(xx, yy, zz));
/*  88 */             if (yy >= 4 && blockState.liquid()) {
/*  89 */               return false;
/*     */             }
/*  91 */             if (yy < 4 && !blockState.isSolid() && level.getBlockState(origin.offset(xx, yy, zz)) != fluid) {
/*  92 */               return false;
/*     */             }
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */     
/*  99 */     for (int j = 0; j < 16; j++) {
/* 100 */       for (int zz = 0; zz < 16; zz++) {
/* 101 */         for (int yy = 0; yy < 8; yy++) {
/* 102 */           if (grid[(j * 16 + zz) * 8 + yy]) {
/* 103 */             BlockPos placePos = origin.offset(j, yy, zz);
/* 104 */             if (canReplaceBlock(level.getBlockState(placePos))) {
/*     */ 
/*     */               
/* 107 */               boolean placeAir = (yy >= 4);
/* 108 */               level.setBlock(placePos, placeAir ? AIR : fluid, 2);
/* 109 */               if (placeAir) {
/* 110 */                 level.scheduleTick(placePos, AIR.getBlock(), 0);
/* 111 */                 markAboveForPostProcessing(level, placePos);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 118 */     BlockState barrier = config.barrier().getState(random, origin);
/*     */     
/* 120 */     if (!barrier.isAir()) {
/* 121 */       for (int k = 0; k < 16; k++) {
/* 122 */         for (int zz = 0; zz < 16; zz++) {
/* 123 */           for (int yy = 0; yy < 8; yy++) {
/* 124 */             boolean check = (!grid[(k * 16 + zz) * 8 + yy] && ((k < 15 && grid[((k + 1) * 16 + zz) * 8 + yy]) || (k > 0 && grid[((k - 1) * 16 + zz) * 8 + yy]) || (zz < 15 && grid[(k * 16 + zz + 1) * 8 + yy]) || (zz > 0 && grid[(k * 16 + zz - 1) * 8 + yy]) || (yy < 7 && grid[(k * 16 + zz) * 8 + yy + 1]) || (yy > 0 && grid[(k * 16 + zz) * 8 + yy - 1])));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */             
/* 133 */             if (check && (
/* 134 */               yy < 4 || random.nextInt(2) != 0)) {
/* 135 */               BlockState blockState = level.getBlockState(origin.offset(k, yy, zz));
/* 136 */               if (blockState.isSolid() && !blockState.is(net.minecraft.tags.BlockTags.LAVA_POOL_STONE_CANNOT_REPLACE)) {
/* 137 */                 BlockPos barrierPos = origin.offset(k, yy, zz);
/* 138 */                 level.setBlock(barrierPos, barrier, 2);
/* 139 */                 markAboveForPostProcessing(level, barrierPos);
/*     */               } 
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 148 */     if (fluid.getFluidState().is(net.minecraft.tags.FluidTags.WATER)) {
/* 149 */       for (int k = 0; k < 16; k++) {
/* 150 */         for (int zz = 0; zz < 16; zz++) {
/* 151 */           int yy = 4;
/* 152 */           BlockPos offset = origin.offset(k, 4, zz);
/* 153 */           if (((net.minecraft.world.level.biome.Biome)level.getBiome(offset).value()).shouldFreeze((net.minecraft.world.level.LevelReader)level, offset, false) && canReplaceBlock(level.getBlockState(offset))) {
/* 154 */             level.setBlock(offset, net.minecraft.world.level.block.Blocks.ICE.defaultBlockState(), 2);
/*     */           }
/*     */         } 
/*     */       } 
/*     */     }
/*     */     
/* 160 */     return true;
/*     */   }
/*     */   
/*     */   private boolean canReplaceBlock(BlockState state) {
/* 164 */     return !state.is(net.minecraft.tags.BlockTags.FEATURES_CANNOT_REPLACE);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/LakeFeature.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */