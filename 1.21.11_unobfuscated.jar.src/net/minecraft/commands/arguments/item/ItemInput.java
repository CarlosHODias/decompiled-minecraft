/*    */ package net.minecraft.commands.arguments.item;
/*    */ import com.mojang.brigadier.Message;
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
/*    */ import com.mojang.serialization.DynamicOps;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponentPatch;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.TypedDataComponent;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.nbt.NbtOps;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.item.Item;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ 
/*    */ public class ItemInput {
/*    */   static {
/* 25 */     ERROR_STACK_TOO_BIG = new Dynamic2CommandExceptionType((item, count) -> Component.translatableEscape("arguments.item.overstacked", new Object[] { item, count }));
/*    */   }
/*    */   private static final Dynamic2CommandExceptionType ERROR_STACK_TOO_BIG; private final Holder<Item> item;
/*    */   private final DataComponentPatch components;
/*    */   
/*    */   public ItemInput(Holder<Item> item, DataComponentPatch components) {
/* 31 */     this.item = item;
/* 32 */     this.components = components;
/*    */   }
/*    */   
/*    */   public Item getItem() {
/* 36 */     return (Item)this.item.value();
/*    */   }
/*    */   
/*    */   public ItemStack createItemStack(int count, boolean checkSize) throws CommandSyntaxException {
/* 40 */     ItemStack result = new ItemStack(this.item, count);
/* 41 */     result.applyComponents(this.components);
/* 42 */     if (checkSize && count > result.getMaxStackSize()) {
/* 43 */       throw ERROR_STACK_TOO_BIG.create(getItemName(), result.getMaxStackSize());
/*    */     }
/* 45 */     return result;
/*    */   }
/*    */   
/*    */   public String serialize(HolderLookup.Provider registries) {
/* 49 */     StringBuilder result = new StringBuilder(getItemName());
/* 50 */     String serializedComponents = serializeComponents(registries);
/* 51 */     if (!serializedComponents.isEmpty()) {
/* 52 */       result.append('[');
/* 53 */       result.append(serializedComponents);
/* 54 */       result.append(']');
/*    */     } 
/* 56 */     return result.toString();
/*    */   }
/*    */   
/*    */   private String serializeComponents(HolderLookup.Provider registries) {
/* 60 */     net.minecraft.resources.RegistryOps registryOps = registries.createSerializationContext((DynamicOps)NbtOps.INSTANCE);
/* 61 */     return this.components.entrySet().stream()
/* 62 */       .flatMap(entry -> {
/*    */           DataComponentType<?> type = (DataComponentType)entry.getKey();
/*    */           
/*    */           Identifier key = BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type);
/*    */           
/*    */           if (key == null) {
/*    */             return Stream.empty();
/*    */           }
/*    */           
/*    */           Optional<?> value = (Optional)entry.getValue();
/*    */           
/*    */           if (value.isPresent()) {
/*    */             TypedDataComponent<?> typedComponent = TypedDataComponent.createUnchecked(type, value.get());
/*    */             return typedComponent.encodeValue(ops).result().stream().map(());
/*    */           } 
/*    */           return Stream.of("!" + key.toString());
/* 78 */         }).collect(Collectors.joining(String.valueOf(',')));
/*    */   }
/*    */   
/*    */   private String getItemName() {
/* 82 */     return this.item.unwrapKey().map(ResourceKey::identifier).orElseGet(() -> "unknown[" + String.valueOf(this.item) + "]").toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/item/ItemInput.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */