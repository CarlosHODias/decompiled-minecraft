/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ 
/*    */ public final class ClientItem extends Record {
/*    */   private final ItemModel.Unbaked model;
/*    */   private final Properties properties;
/*    */   private final net.minecraft.util.RegistryContextSwapper registrySwapper;
/*    */   public static final com.mojang.serialization.Codec<ClientItem> CODEC;
/*    */   
/* 12 */   public ClientItem(ItemModel.Unbaked model, Properties properties, net.minecraft.util.RegistryContextSwapper registrySwapper) { this.model = model; this.properties = properties; this.registrySwapper = registrySwapper; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ClientItem;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 12 */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem; } public ItemModel.Unbaked model() { return this.model; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ClientItem;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ClientItem;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #12	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/ClientItem;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public Properties properties() { return this.properties; } public net.minecraft.util.RegistryContextSwapper registrySwapper() { return this.registrySwapper; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 17 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ItemModels.CODEC.fieldOf("model").forGetter(ClientItem::model), (App)Properties.MAP_CODEC.forGetter(ClientItem::properties)).apply((com.mojang.datafixers.kinds.Applicative)i, ClientItem::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public ClientItem(ItemModel.Unbaked model, Properties properties) {
/* 23 */     this(model, properties, null);
/*    */   }
/*    */   
/*    */   public ClientItem withRegistrySwapper(net.minecraft.util.RegistryContextSwapper registrySwapper) {
/* 27 */     return new ClientItem(this.model, this.properties, registrySwapper);
/*    */   }
/*    */   public static final class Properties extends Record { private final boolean handAnimationOnSwap;
/*    */     private final boolean oversizedInGui;
/*    */     private final float swapAnimationScale;
/*    */     
/* 33 */     public Properties(boolean handAnimationOnSwap, boolean oversizedInGui, float swapAnimationScale) { this.handAnimationOnSwap = handAnimationOnSwap; this.oversizedInGui = oversizedInGui; this.swapAnimationScale = swapAnimationScale; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/ClientItem$Properties;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #33	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/ClientItem$Properties;
/* 33 */       //   0	8	1	o	Ljava/lang/Object; } public boolean handAnimationOnSwap() { return this.handAnimationOnSwap; } public boolean oversizedInGui() { return this.oversizedInGui; } public float swapAnimationScale() { return this.swapAnimationScale; }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 38 */     public static final Properties DEFAULT = new Properties(true, false, 1.0F); public static final com.mojang.serialization.MapCodec<Properties> MAP_CODEC;
/*    */     static {
/* 40 */       MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("hand_animation_on_swap", true).forGetter(Properties::handAnimationOnSwap), (App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("oversized_in_gui", false).forGetter(Properties::oversizedInGui), (App)com.mojang.serialization.Codec.FLOAT.optionalFieldOf("swap_animation_scale", 1.0F).forGetter(Properties::swapAnimationScale)).apply((com.mojang.datafixers.kinds.Applicative)i, Properties::new));
/*    */     } }
/*    */ 
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/ClientItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */