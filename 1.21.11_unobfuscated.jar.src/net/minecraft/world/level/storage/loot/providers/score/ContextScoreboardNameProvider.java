/*    */ package net.minecraft.world.level.storage.loot.providers.score;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ 
/*    */ public final class ContextScoreboardNameProvider extends Record implements ScoreboardNameProvider {
/*    */   private final LootContext.EntityTarget target;
/*    */   public static final com.mojang.serialization.MapCodec<ContextScoreboardNameProvider> CODEC;
/*    */   
/* 13 */   public ContextScoreboardNameProvider(LootContext.EntityTarget target) { this.target = target; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 13 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider; } public LootContext.EntityTarget target() { return this.target; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootContext.EntityTarget.CODEC.fieldOf("target").forGetter(ContextScoreboardNameProvider::target)).apply((com.mojang.datafixers.kinds.Applicative)i, ContextScoreboardNameProvider::new)); }
/*    */ 
/*    */ 
/*    */   
/* 18 */   public static final com.mojang.serialization.Codec<ContextScoreboardNameProvider> INLINE_CODEC = LootContext.EntityTarget.CODEC.xmap(ContextScoreboardNameProvider::new, ContextScoreboardNameProvider::target);
/*    */   
/*    */   public static ScoreboardNameProvider forTarget(LootContext.EntityTarget target) {
/* 21 */     return new ContextScoreboardNameProvider(target);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootScoreProviderType getType() {
/* 26 */     return ScoreboardNameProviders.CONTEXT;
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.world.scores.ScoreHolder getScoreHolder(LootContext context) {
/* 31 */     return (net.minecraft.world.scores.ScoreHolder)context.getOptionalParameter(this.target.contextParam());
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 36 */     return Set.of(this.target.contextParam());
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/score/ContextScoreboardNameProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */