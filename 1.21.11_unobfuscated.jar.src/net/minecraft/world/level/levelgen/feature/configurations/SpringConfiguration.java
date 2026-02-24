/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function5;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.level.block.Block;
/*    */ import net.minecraft.world.level.material.FluidState;
/*    */ 
/*    */ public class SpringConfiguration implements FeatureConfiguration {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)FluidState.CODEC.fieldOf("state").forGetter(()), (App)Codec.BOOL.fieldOf("requires_block_below").orElse(true).forGetter(()), (App)Codec.INT.fieldOf("rock_count").orElse(4).forGetter(()), (App)Codec.INT.fieldOf("hole_count").orElse(1).forGetter(()), (App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.BLOCK).fieldOf("valid_blocks").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, SpringConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<SpringConfiguration> CODEC;
/*    */   
/*    */   public final FluidState state;
/*    */   
/*    */   public final boolean requiresBlockBelow;
/*    */   
/*    */   public final int rockCount;
/*    */   public final int holeCount;
/*    */   public final HolderSet<Block> validBlocks;
/*    */   
/*    */   public SpringConfiguration(FluidState state, boolean requiresBlockBelow, int rockCount, int holeCount, HolderSet<Block> validBlocks) {
/* 27 */     this.state = state;
/* 28 */     this.requiresBlockBelow = requiresBlockBelow;
/* 29 */     this.rockCount = rockCount;
/* 30 */     this.holeCount = holeCount;
/* 31 */     this.validBlocks = validBlocks;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/SpringConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */