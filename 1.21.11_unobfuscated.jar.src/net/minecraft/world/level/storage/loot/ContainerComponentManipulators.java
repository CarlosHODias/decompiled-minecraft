/*    */ package net.minecraft.world.level.storage.loot;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Collectors;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.component.DataComponentType;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.BundleContents;
/*    */ import net.minecraft.world.item.component.ChargedProjectiles;
/*    */ import net.minecraft.world.item.component.ItemContainerContents;
/*    */ 
/*    */ public interface ContainerComponentManipulators {
/* 18 */   public static final ContainerComponentManipulator<ItemContainerContents> CONTAINER = new ContainerComponentManipulator<ItemContainerContents>()
/*    */     {
/*    */       public DataComponentType<ItemContainerContents> type() {
/* 21 */         return DataComponents.CONTAINER;
/*    */       }
/*    */ 
/*    */       
/*    */       public Stream<ItemStack> getContents(ItemContainerContents component) {
/* 26 */         return component.stream();
/*    */       }
/*    */ 
/*    */       
/*    */       public ItemContainerContents empty() {
/* 31 */         return ItemContainerContents.EMPTY;
/*    */       }
/*    */ 
/*    */       
/*    */       public ItemContainerContents setContents(ItemContainerContents component, Stream<ItemStack> newContents) {
/* 36 */         return ItemContainerContents.fromItems(newContents.toList());
/*    */       }
/*    */     };
/*    */   
/* 40 */   public static final ContainerComponentManipulator<BundleContents> BUNDLE_CONTENTS = new ContainerComponentManipulator<BundleContents>()
/*    */     {
/*    */       public DataComponentType<BundleContents> type() {
/* 43 */         return DataComponents.BUNDLE_CONTENTS;
/*    */       }
/*    */ 
/*    */       
/*    */       public BundleContents empty() {
/* 48 */         return BundleContents.EMPTY;
/*    */       }
/*    */ 
/*    */       
/*    */       public Stream<ItemStack> getContents(BundleContents component) {
/* 53 */         return component.itemCopyStream();
/*    */       }
/*    */ 
/*    */       
/*    */       public BundleContents setContents(BundleContents component, Stream<ItemStack> newContents) {
/* 58 */         BundleContents.Mutable builder = new BundleContents.Mutable(component).clearItems();
/* 59 */         Objects.requireNonNull(builder); newContents.forEach(builder::tryInsert);
/* 60 */         return builder.toImmutable();
/*    */       }
/*    */     };
/*    */   
/* 64 */   public static final ContainerComponentManipulator<ChargedProjectiles> CHARGED_PROJECTILES = new ContainerComponentManipulator<ChargedProjectiles>()
/*    */     {
/*    */       public DataComponentType<ChargedProjectiles> type() {
/* 67 */         return DataComponents.CHARGED_PROJECTILES;
/*    */       }
/*    */ 
/*    */       
/*    */       public ChargedProjectiles empty() {
/* 72 */         return ChargedProjectiles.EMPTY;
/*    */       }
/*    */ 
/*    */       
/*    */       public Stream<ItemStack> getContents(ChargedProjectiles component) {
/* 77 */         return component.getItems().stream();
/*    */       }
/*    */ 
/*    */       
/*    */       public ChargedProjectiles setContents(ChargedProjectiles component, Stream<ItemStack> newContents) {
/* 82 */         return ChargedProjectiles.of(newContents.toList());
/*    */       }
/*    */     };
/*    */   
/*    */   public static final Map<DataComponentType<?>, ContainerComponentManipulator<?>> ALL_MANIPULATORS;
/*    */   public static final Codec<ContainerComponentManipulator<?>> CODEC;
/*    */   
/*    */   static {
/* 90 */     ALL_MANIPULATORS = (Map<DataComponentType<?>, ContainerComponentManipulator<?>>)Stream.<ContainerComponentManipulator>of(new ContainerComponentManipulator[] { CONTAINER, BUNDLE_CONTENTS, CHARGED_PROJECTILES }).collect(Collectors.toMap(ContainerComponentManipulator::type, e -> e));
/*    */     
/* 92 */     CODEC = BuiltInRegistries.DATA_COMPONENT_TYPE.byNameCodec().comapFlatMap(type -> { ContainerComponentManipulator<?> manipulator = ALL_MANIPULATORS.get(type); return (manipulator != null) ? DataResult.success(manipulator) : DataResult.error(()); }, ContainerComponentManipulator::type);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/ContainerComponentManipulators.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */