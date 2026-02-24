/*    */ package net.minecraft.client.renderer.block.model.multipart;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.client.renderer.block.model.BlockStateModel;
/*    */ 
/*    */ public final class Selector extends Record {
/*    */   private final java.util.Optional<Condition> condition;
/*    */   private final BlockStateModel.Unbaked variant;
/*    */   public static final com.mojang.serialization.Codec<Selector> CODEC;
/*    */   
/* 12 */   public Selector(java.util.Optional<Condition> condition, BlockStateModel.Unbaked variant) { this.condition = condition; this.variant = variant; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/block/model/multipart/Selector;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/Selector; } public java.util.Optional<Condition> condition() { return this.condition; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/block/model/multipart/Selector;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/block/model/multipart/Selector; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/block/model/multipart/Selector;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/block/model/multipart/Selector;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public BlockStateModel.Unbaked variant() { return this.variant; }
/*    */ 
/*    */   
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)Condition.CODEC.optionalFieldOf("when").forGetter(Selector::condition), (App)BlockStateModel.Unbaked.CODEC.fieldOf("apply").forGetter(Selector::variant)).apply((com.mojang.datafixers.kinds.Applicative)i, Selector::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public <O, S extends net.minecraft.world.level.block.state.StateHolder<O, S>> java.util.function.Predicate<S> instantiate(net.minecraft.world.level.block.state.StateDefinition<O, S> definition) {
/* 22 */     return this.condition.<java.util.function.Predicate<S>>map(c -> c.instantiate(definition)).orElse(state -> true);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/multipart/Selector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */