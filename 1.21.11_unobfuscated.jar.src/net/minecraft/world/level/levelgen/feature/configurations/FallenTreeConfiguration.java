/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.util.valueproviders.IntProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
/*    */ 
/*    */ public class FallenTreeConfiguration implements FeatureConfiguration {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(()), (App)IntProvider.codec(0, 16).fieldOf("log_length").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("stump_decorators").forGetter(()), (App)TreeDecorator.CODEC.listOf().fieldOf("log_decorators").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, FallenTreeConfiguration::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<FallenTreeConfiguration> CODEC;
/*    */   
/*    */   public final BlockStateProvider trunkProvider;
/*    */   
/*    */   public final IntProvider logLength;
/*    */   public final List<TreeDecorator> stumpDecorators;
/*    */   public final List<TreeDecorator> logDecorators;
/*    */   
/*    */   protected FallenTreeConfiguration(BlockStateProvider trunkProvider, IntProvider logLength, List<TreeDecorator> stumpDecorators, List<TreeDecorator> logDecorators) {
/* 26 */     this.trunkProvider = trunkProvider;
/* 27 */     this.logLength = logLength;
/* 28 */     this.stumpDecorators = stumpDecorators;
/* 29 */     this.logDecorators = logDecorators;
/*    */   }
/*    */   
/*    */   public static class FallenTreeConfigurationBuilder {
/*    */     private final BlockStateProvider trunkProvider;
/*    */     private final IntProvider logLength;
/* 35 */     private List<TreeDecorator> stumpDecorators = new ArrayList<>();
/* 36 */     private List<TreeDecorator> logDecorators = new ArrayList<>();
/*    */     
/*    */     public FallenTreeConfigurationBuilder(BlockStateProvider trunkProvider, IntProvider logLength) {
/* 39 */       this.trunkProvider = trunkProvider;
/* 40 */       this.logLength = logLength;
/*    */     }
/*    */     
/*    */     public FallenTreeConfigurationBuilder stumpDecorators(List<TreeDecorator> stumpDecorators) {
/* 44 */       this.stumpDecorators = stumpDecorators;
/* 45 */       return this;
/*    */     }
/*    */     
/*    */     public FallenTreeConfigurationBuilder logDecorators(List<TreeDecorator> logDecorators) {
/* 49 */       this.logDecorators = logDecorators;
/* 50 */       return this;
/*    */     }
/*    */     
/*    */     public FallenTreeConfiguration build() {
/* 54 */       return new FallenTreeConfiguration(this.trunkProvider, this.logLength, this.stumpDecorators, this.logDecorators);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/FallenTreeConfiguration.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */