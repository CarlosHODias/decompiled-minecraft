/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import net.minecraft.client.renderer.item.SelectItemModel;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ 
/*    */ public final class ComponentContents<T> extends Record implements SelectItemModelProperty<T> {
/*    */   private final DataComponentType<T> componentType;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents<TT;>;
/*    */   }
/*    */   
/* 15 */   public ComponentContents(DataComponentType<T> componentType) { this.componentType = componentType; } public DataComponentType<T> componentType() { return this.componentType; }
/*    */    public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents<TT;>;
/*    */   } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */     // Local variable type table:
/*    */     //   start	length	slot	name	signature
/*    */     //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/ComponentContents<TT;>;
/* 20 */   } private static final SelectItemModelProperty.Type<? extends ComponentContents<?>, ?> TYPE = createType();
/*    */ 
/*    */   
/*    */   private static <T> SelectItemModelProperty.Type<ComponentContents<T>, T> createType() {
/* 24 */     Codec<? extends DataComponentType<?>> rawComponentCodec = net.minecraft.core.registries.BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec()
/* 25 */       .validate(t -> t.isTransient() ? DataResult.error(()) : DataResult.success(t));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 31 */     Codec<? extends DataComponentType<?>> codec1 = rawComponentCodec;
/*    */     
/* 33 */     com.mojang.serialization.MapCodec<SelectItemModel.UnbakedSwitch<ComponentContents<T>, T>> switchCodec = codec1.dispatchMap("component", switchObject -> ((ComponentContents)switchObject.property()).componentType, componentType -> SelectItemModelProperty.Type.<T>createCasesFieldCodec(componentType.codecOrThrow()).xmap((), SelectItemModel.UnbakedSwitch::cases));
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 43 */     return new SelectItemModelProperty.Type<>(switchCodec);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> SelectItemModelProperty.Type<ComponentContents<T>, T> castType() {
/* 49 */     return (SelectItemModelProperty.Type)TYPE;
/*    */   }
/*    */ 
/*    */   
/*    */   public T get(net.minecraft.world.item.ItemStack itemStack, net.minecraft.client.multiplayer.ClientLevel level, net.minecraft.world.entity.LivingEntity owner, int seed, net.minecraft.world.item.ItemDisplayContext displayContext) {
/* 54 */     return (T)itemStack.get(this.componentType);
/*    */   }
/*    */ 
/*    */   
/*    */   public SelectItemModelProperty.Type<ComponentContents<T>, T> type() {
/* 59 */     return castType();
/*    */   }
/*    */ 
/*    */   
/*    */   public Codec<T> valueCodec() {
/* 64 */     return this.componentType.codecOrThrow();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/ComponentContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */