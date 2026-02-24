/*    */ package net.minecraft.world.level.storage.loot.providers.score;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Set;
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public final class FixedScoreboardNameProvider extends Record implements ScoreboardNameProvider {
/*    */   private final String name;
/*    */   public static final com.mojang.serialization.MapCodec<FixedScoreboardNameProvider> CODEC;
/*    */   
/* 12 */   public FixedScoreboardNameProvider(String name) { this.name = name; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider; } public String name() { return this.name; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.STRING.fieldOf("name").forGetter(FixedScoreboardNameProvider::name)).apply((com.mojang.datafixers.kinds.Applicative)i, FixedScoreboardNameProvider::new)); }
/*    */ 
/*    */ 
/*    */   
/*    */   public static ScoreboardNameProvider forName(String name) {
/* 18 */     return new FixedScoreboardNameProvider(name);
/*    */   }
/*    */ 
/*    */   
/*    */   public LootScoreProviderType getType() {
/* 23 */     return ScoreboardNameProviders.FIXED;
/*    */   }
/*    */ 
/*    */   
/*    */   public net.minecraft.world.scores.ScoreHolder getScoreHolder(net.minecraft.world.level.storage.loot.LootContext context) {
/* 28 */     return net.minecraft.world.scores.ScoreHolder.forNameOnly(this.name);
/*    */   }
/*    */ 
/*    */   
/*    */   public Set<net.minecraft.util.context.ContextKey<?>> getReferencedContextParams() {
/* 33 */     return Set.of();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/providers/score/FixedScoreboardNameProvider.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */