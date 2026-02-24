/*    */ package net.minecraft.client.renderer.item.properties.conditional;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.component.predicates.DataComponentPredicate;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public final class ComponentMatches extends Record implements ConditionalItemModelProperty {
/*    */   private final DataComponentPredicate.Single<?> predicate;
/*    */   public static final com.mojang.serialization.MapCodec<ComponentMatches> MAP_CODEC;
/*    */   
/* 12 */   public ComponentMatches(DataComponentPredicate.Single<?> predicate) { this.predicate = predicate; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches; } public DataComponentPredicate.Single<?> predicate() { return this.predicate; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/conditional/ComponentMatches;
/* 13 */     //   0	8	1	o	Ljava/lang/Object; } static { MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)DataComponentPredicate.singleCodec("predicate").forGetter(ComponentMatches::predicate)).apply((com.mojang.datafixers.kinds.Applicative)i, ComponentMatches::new)); }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean get(ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner, int seed, net.minecraft.world.item.ItemDisplayContext displayContext) {
/* 19 */     return this.predicate.predicate().matches((net.minecraft.core.component.DataComponentGetter)itemStack);
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<ComponentMatches> type() {
/* 24 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/conditional/ComponentMatches.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */