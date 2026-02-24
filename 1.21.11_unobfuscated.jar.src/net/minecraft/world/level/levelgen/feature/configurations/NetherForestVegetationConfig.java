/*    */ package net.minecraft.world.level.levelgen.feature.configurations;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
/*    */ 
/*    */ public class NetherForestVegetationConfig extends BlockPileConfiguration {
/*    */   static {
/*  9 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BlockStateProvider.CODEC.fieldOf("state_provider").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("spread_width").forGetter(()), (App)ExtraCodecs.POSITIVE_INT.fieldOf("spread_height").forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, NetherForestVegetationConfig::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<NetherForestVegetationConfig> CODEC;
/*    */   
/*    */   public final int spreadWidth;
/*    */   public final int spreadHeight;
/*    */   
/*    */   public NetherForestVegetationConfig(BlockStateProvider stateProvider, int spreadWidth, int spreadHeight) {
/* 19 */     super(stateProvider);
/* 20 */     this.spreadWidth = spreadWidth;
/* 21 */     this.spreadHeight = spreadHeight;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/feature/configurations/NetherForestVegetationConfig.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */