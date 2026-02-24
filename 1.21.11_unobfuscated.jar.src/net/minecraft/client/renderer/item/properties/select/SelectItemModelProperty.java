/*    */ package net.minecraft.client.renderer.item.properties.select;
/*    */ 
/*    */ import com.google.common.collect.HashMultiset;
/*    */ import com.google.common.collect.Multiset;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.function.BiFunction;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.client.renderer.item.SelectItemModel;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ 
/*    */ public interface SelectItemModelProperty<T> {
/*    */   T get(net.minecraft.world.item.ItemStack paramItemStack, ClientLevel paramClientLevel, LivingEntity paramLivingEntity, int paramInt, net.minecraft.world.item.ItemDisplayContext paramItemDisplayContext);
/*    */   
/*    */   Codec<T> valueCodec();
/*    */   
/*    */   Type<? extends SelectItemModelProperty<T>, T> type();
/*    */   
/*    */   public static final class Type<P extends SelectItemModelProperty<T>, T> extends Record {
/*    */     private final MapCodec<SelectItemModel.UnbakedSwitch<P, T>> switchCodec;
/*    */     
/* 28 */     public Type(MapCodec<SelectItemModel.UnbakedSwitch<P, T>> switchCodec) { this.switchCodec = switchCodec; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/* 28 */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type<TP;TT;>; } public MapCodec<SelectItemModel.UnbakedSwitch<P, T>> switchCodec() { return this.switchCodec; }
/*    */     public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type<TP;TT;>; } public final boolean equals(Object o) {
/*    */       // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #28	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type;
/*    */       //   0	8	1	o	Ljava/lang/Object;
/*    */       // Local variable type table:
/*    */       //   start	length	slot	name	signature
/*    */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/SelectItemModelProperty$Type<TP;TT;>;
/*    */     } public static <P extends SelectItemModelProperty<T>, T> Type<P, T> create(MapCodec<P> propertyMapCodec, Codec<T> valueCodec) {
/* 32 */       MapCodec<SelectItemModel.UnbakedSwitch<P, T>> switchCodec = RecordCodecBuilder.mapCodec(i -> i.group((App)propertyMapCodec.forGetter(SelectItemModel.UnbakedSwitch::property), (App)createCasesFieldCodec(valueCodec).forGetter(SelectItemModel.UnbakedSwitch::cases)).apply((Applicative)i, net.minecraft.client.renderer.item.SelectItemModel.UnbakedSwitch::new));
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 37 */       return new Type<>(switchCodec);
/*    */     }
/*    */     
/*    */     public static <T> MapCodec<List<SelectItemModel.SwitchCase<T>>> createCasesFieldCodec(Codec<T> valueCodec) {
/* 41 */       return SelectItemModel.SwitchCase.codec(valueCodec).listOf().validate(Type::validateCases).fieldOf("cases");
/*    */     }
/*    */     
/*    */     private static <T> DataResult<List<SelectItemModel.SwitchCase<T>>> validateCases(List<SelectItemModel.SwitchCase<T>> cases) {
/* 45 */       if (cases.isEmpty()) {
/* 46 */         return DataResult.error(() -> "Empty case list");
/*    */       }
/*    */       
/* 49 */       HashMultiset hashMultiset = HashMultiset.create();
/* 50 */       for (SelectItemModel.SwitchCase<T> c : cases) {
/* 51 */         hashMultiset.addAll(c.values());
/*    */       }
/*    */       
/* 54 */       if (hashMultiset.size() != hashMultiset.entrySet().size()) {
/* 55 */         return DataResult.error(() -> "Duplicate case conditions: " + (String)counts.entrySet().stream().filter(()).map(()).collect(Collectors.joining(", ")));
/*    */       }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 64 */       return DataResult.success(cases);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/SelectItemModelProperty.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */