/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.BlockPos;
/*    */ 
/*    */ public class EndGatewayConfiguration implements FeatureConfiguration {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockPos.CODEC.optionalFieldOf("exit").forGetter(()), (App)Codec.BOOL.fieldOf("exact").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, EndGatewayConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final Codec<EndGatewayConfiguration> CODEC;
/*    */   private final Optional<BlockPos> exit;
/*    */   private final boolean exact;
/*    */   
/*    */   private EndGatewayConfiguration(Optional<BlockPos> exit, boolean exact) {
/* 19 */     this.exit = exit;
/* 20 */     this.exact = exact;
/*    */   }
/*    */   
/*    */   public static EndGatewayConfiguration knownExit(BlockPos exit, boolean exact) {
/* 24 */     return new EndGatewayConfiguration(Optional.of(exit), exact);
/*    */   }
/*    */   
/*    */   public static EndGatewayConfiguration delayedExitSearch() {
/* 28 */     return new EndGatewayConfiguration(Optional.empty(), false);
/*    */   }
/*    */   
/*    */   public Optional<BlockPos> getExit() {
/* 32 */     return this.exit;
/*    */   }
/*    */   
/*    */   public boolean isExitExact() {
/* 36 */     return this.exact;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/EndGatewayConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */