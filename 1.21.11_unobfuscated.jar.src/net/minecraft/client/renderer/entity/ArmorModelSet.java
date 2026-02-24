/*    */ package net.minecraft.client.renderer.entity;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.client.model.geom.builders.LayerDefinition;
/*    */ import net.minecraft.world.entity.EquipmentSlot;
/*    */ 
/*    */ public final class ArmorModelSet<T> extends Record {
/*    */   private final T head;
/*    */   private final T chest;
/*    */   private final T legs;
/*    */   private final T feet;
/*    */   
/* 13 */   public ArmorModelSet(T head, T chest, T legs, T feet) { this.head = head; this.chest = chest; this.legs = legs; this.feet = feet; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/entity/ArmorModelSet;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 13 */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet<TT;>; } public T head() { return this.head; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/entity/ArmorModelSet;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet<TT;>; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/entity/ArmorModelSet;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #13	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/* 13 */     //   0	8	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet<TT;>; } public T chest() { return this.chest; } public T legs() { return this.legs; } public T feet() { return this.feet; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public T get(EquipmentSlot slot) {
/*    */     // Byte code:
/*    */     //   0: getstatic net/minecraft/client/renderer/entity/ArmorModelSet$1.$SwitchMap$net$minecraft$world$entity$EquipmentSlot : [I
/*    */     //   3: aload_1
/*    */     //   4: invokevirtual ordinal : ()I
/*    */     //   7: iaload
/*    */     //   8: tableswitch default -> 80, 1 -> 40, 2 -> 50, 3 -> 60, 4 -> 70
/*    */     //   40: aload_0
/*    */     //   41: getfield head : Ljava/lang/Object;
/*    */     //   44: checkcast java/lang/Object
/*    */     //   47: goto -> 97
/*    */     //   50: aload_0
/*    */     //   51: getfield chest : Ljava/lang/Object;
/*    */     //   54: checkcast java/lang/Object
/*    */     //   57: goto -> 97
/*    */     //   60: aload_0
/*    */     //   61: getfield legs : Ljava/lang/Object;
/*    */     //   64: checkcast java/lang/Object
/*    */     //   67: goto -> 97
/*    */     //   70: aload_0
/*    */     //   71: getfield feet : Ljava/lang/Object;
/*    */     //   74: checkcast java/lang/Object
/*    */     //   77: goto -> 97
/*    */     //   80: new java/lang/IllegalStateException
/*    */     //   83: dup
/*    */     //   84: aload_1
/*    */     //   85: invokestatic valueOf : (Ljava/lang/Object;)Ljava/lang/String;
/*    */     //   88: <illegal opcode> makeConcatWithConstants : (Ljava/lang/String;)Ljava/lang/String;
/*    */     //   93: invokespecial <init> : (Ljava/lang/String;)V
/*    */     //   96: athrow
/*    */     //   97: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #20	-> 0
/*    */     //   #21	-> 40
/*    */     //   #22	-> 50
/*    */     //   #23	-> 60
/*    */     //   #24	-> 70
/*    */     //   #25	-> 80
/*    */     //   #20	-> 97
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	98	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet;
/*    */     //   0	98	1	slot	Lnet/minecraft/world/entity/EquipmentSlot;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	98	0	this	Lnet/minecraft/client/renderer/entity/ArmorModelSet<TT;>;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public <U> ArmorModelSet<U> map(Function<? super T, ? extends U> mapper) {
/* 30 */     return new ArmorModelSet((T)mapper.apply(this.head), (T)mapper.apply(this.chest), (T)mapper.apply(this.legs), (T)mapper.apply(this.feet));
/*    */   }
/*    */   
/*    */   public void putFrom(ArmorModelSet<LayerDefinition> values, com.google.common.collect.ImmutableMap.Builder<T, LayerDefinition> output) {
/* 34 */     output.put(this.head, (LayerDefinition)values.head);
/* 35 */     output.put(this.chest, (LayerDefinition)values.chest);
/* 36 */     output.put(this.legs, (LayerDefinition)values.legs);
/* 37 */     output.put(this.feet, (LayerDefinition)values.feet);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <M extends net.minecraft.client.model.HumanoidModel<?>> ArmorModelSet<M> bake(ArmorModelSet<net.minecraft.client.model.geom.ModelLayerLocation> locations, net.minecraft.client.model.geom.EntityModelSet modelSet, Function<net.minecraft.client.model.geom.ModelPart, M> factory) {
/* 45 */     return locations.map(id -> (net.minecraft.client.model.HumanoidModel)factory.apply(modelSet.bakeLayer(id)));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/entity/ArmorModelSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */