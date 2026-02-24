/*     */ package net.minecraft.advancements.criterion;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.advancements.CriteriaTriggers;
/*     */ import net.minecraft.advancements.Criterion;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.server.level.ServerPlayer;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.ItemLike;
/*     */ 
/*     */ public class InventoryChangeTrigger extends SimpleCriterionTrigger<InventoryChangeTrigger.TriggerInstance> {
/*     */   public Codec<TriggerInstance> codec() {
/*  21 */     return TriggerInstance.CODEC;
/*     */   }
/*     */   
/*     */   public void trigger(ServerPlayer player, Inventory inventory, ItemStack changedItem) {
/*  25 */     int slotsFull = 0;
/*  26 */     int slotsEmpty = 0;
/*  27 */     int slotsOccupied = 0;
/*     */     
/*  29 */     for (int slot = 0; slot < inventory.getContainerSize(); slot++) {
/*  30 */       ItemStack itemStack = inventory.getItem(slot);
/*  31 */       if (itemStack.isEmpty()) {
/*  32 */         slotsEmpty++;
/*     */       } else {
/*  34 */         slotsOccupied++;
/*  35 */         if (itemStack.getCount() >= itemStack.getMaxStackSize()) {
/*  36 */           slotsFull++;
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/*  41 */     trigger(player, inventory, changedItem, slotsFull, slotsEmpty, slotsOccupied);
/*     */   }
/*     */   
/*     */   private void trigger(ServerPlayer player, Inventory inventory, ItemStack changedItem, int slotsFull, int slotsEmpty, int slotsOccupied) {
/*  45 */     trigger(player, t -> t.matches(inventory, changedItem, slotsFull, slotsEmpty, slotsOccupied));
/*     */   }
/*     */   public static final class TriggerInstance extends Record implements SimpleCriterionTrigger.SimpleInstance { private final Optional<ContextAwarePredicate> player; private final Slots slots; private final List<ItemPredicate> items; public static final Codec<TriggerInstance> CODEC;
/*  48 */     public TriggerInstance(Optional<ContextAwarePredicate> player, Slots slots, List<ItemPredicate> items) { this.player = player; this.slots = slots; this.items = items; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  48 */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance; } public Optional<ContextAwarePredicate> player() { return this.player; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #48	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance;
/*  48 */       //   0	8	1	o	Ljava/lang/Object; } public Slots slots() { return this.slots; } public List<ItemPredicate> items() { return this.items; }
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/*  53 */       CODEC = RecordCodecBuilder.create(i -> i.group((App)EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(TriggerInstance::player), (App)Slots.CODEC.optionalFieldOf("slots", Slots.ANY).forGetter(TriggerInstance::slots), (App)ItemPredicate.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(TriggerInstance::items)).apply((Applicative)i, TriggerInstance::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public static Criterion<TriggerInstance> hasItems(ItemPredicate.Builder... items) {
/*  60 */       return hasItems((ItemPredicate[])Stream.<ItemPredicate.Builder>of(items).map(ItemPredicate.Builder::build).toArray(x$0 -> new ItemPredicate[x$0]));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> hasItems(ItemPredicate... items) {
/*  64 */       return CriteriaTriggers.INVENTORY_CHANGED.createCriterion(new TriggerInstance(Optional.empty(), Slots.ANY, List.of(items)));
/*     */     }
/*     */     
/*     */     public static Criterion<TriggerInstance> hasItems(ItemLike... items) {
/*  68 */       ItemPredicate[] predicates = new ItemPredicate[items.length];
/*  69 */       for (int i = 0; i < items.length; i++) {
/*  70 */         predicates[i] = new ItemPredicate((Optional)Optional.of(net.minecraft.core.HolderSet.direct(new Holder[] { (Holder)items[i].asItem().builtInRegistryHolder() })), MinMaxBounds.Ints.ANY, DataComponentMatchers.ANY);
/*     */       } 
/*  72 */       return hasItems(predicates);
/*     */     }
/*     */     
/*     */     public boolean matches(Inventory inventory, ItemStack changedItem, int slotsFull, int slotsEmpty, int slotsOccupied) {
/*  76 */       if (!this.slots.matches(slotsFull, slotsEmpty, slotsOccupied)) {
/*  77 */         return false;
/*     */       }
/*     */       
/*  80 */       if (this.items.isEmpty()) {
/*  81 */         return true;
/*     */       }
/*     */ 
/*     */ 
/*     */       
/*  86 */       if (this.items.size() == 1) {
/*  87 */         return (!changedItem.isEmpty() && ((ItemPredicate)this.items.get(0)).test(changedItem));
/*     */       }
/*     */       
/*  90 */       ObjectArrayList objectArrayList = new ObjectArrayList(this.items);
/*  91 */       int count = inventory.getContainerSize();
/*  92 */       for (int slot = 0; slot < count; slot++) {
/*  93 */         if (objectArrayList.isEmpty()) {
/*  94 */           return true;
/*     */         }
/*     */         
/*  97 */         ItemStack itemStack = inventory.getItem(slot);
/*  98 */         if (!itemStack.isEmpty()) {
/*  99 */           objectArrayList.removeIf(predicate -> predicate.test(itemStack));
/*     */         }
/*     */       } 
/* 102 */       return objectArrayList.isEmpty();
/*     */     }
/*     */     public static final class Slots extends Record { private final MinMaxBounds.Ints occupied; private final MinMaxBounds.Ints full; private final MinMaxBounds.Ints empty; public static final Codec<Slots> CODEC;
/* 105 */       public Slots(MinMaxBounds.Ints occupied, MinMaxBounds.Ints full, MinMaxBounds.Ints empty) { this.occupied = occupied; this.full = full; this.empty = empty; } public final String toString() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;)Ljava/lang/String;
/*     */         //   6: areturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #105	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots; } public final int hashCode() { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;)I
/*     */         //   6: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #105	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots; } public final boolean equals(Object o) { // Byte code:
/*     */         //   0: aload_0
/*     */         //   1: aload_1
/*     */         //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;Ljava/lang/Object;)Z
/*     */         //   7: ireturn
/*     */         // Line number table:
/*     */         //   Java source line number -> byte code offset
/*     */         //   #105	-> 0
/*     */         // Local variable table:
/*     */         //   start	length	slot	name	descriptor
/*     */         //   0	8	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;
/* 105 */         //   0	8	1	o	Ljava/lang/Object; } public MinMaxBounds.Ints occupied() { return this.occupied; } public MinMaxBounds.Ints full() { return this.full; } public MinMaxBounds.Ints empty() { return this.empty; }
/*     */ 
/*     */ 
/*     */       
/*     */       static {
/* 110 */         CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("occupied", MinMaxBounds.Ints.ANY).forGetter(Slots::occupied), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("full", MinMaxBounds.Ints.ANY).forGetter(Slots::full), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("empty", MinMaxBounds.Ints.ANY).forGetter(Slots::empty)).apply((Applicative)i, Slots::new));
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 116 */       public static final Slots ANY = new Slots(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY);
/*     */       
/*     */       public boolean matches(int slotsFull, int slotsEmpty, int slotsOccupied) {
/* 119 */         if (!this.full.matches(slotsFull)) {
/* 120 */           return false;
/*     */         }
/* 122 */         if (!this.empty.matches(slotsEmpty)) {
/* 123 */           return false;
/*     */         }
/* 125 */         if (!this.occupied.matches(slotsOccupied)) {
/* 126 */           return false;
/*     */         }
/* 128 */         return true; } } } public static final class Slots extends Record { private final MinMaxBounds.Ints occupied; public boolean matches(int slotsFull, int slotsEmpty, int slotsOccupied) { if (!this.full.matches(slotsFull)) return false;  if (!this.empty.matches(slotsEmpty)) return false;  if (!this.occupied.matches(slotsOccupied)) return false;  return true; }
/*     */ 
/*     */     
/*     */     private final MinMaxBounds.Ints full;
/*     */     private final MinMaxBounds.Ints empty;
/*     */     public static final Codec<Slots> CODEC;
/*     */     
/*     */     public Slots(MinMaxBounds.Ints occupied, MinMaxBounds.Ints full, MinMaxBounds.Ints empty) {
/*     */       this.occupied = occupied;
/*     */       this.full = full;
/*     */       this.empty = empty;
/*     */     }
/*     */     
/*     */     public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;
/*     */     }
/*     */     
/*     */     public final int hashCode() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;
/*     */     }
/*     */     
/*     */     public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #105	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/advancements/criterion/InventoryChangeTrigger$TriggerInstance$Slots;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Ints occupied() {
/*     */       return this.occupied;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Ints full() {
/*     */       return this.full;
/*     */     }
/*     */     
/*     */     public MinMaxBounds.Ints empty() {
/*     */       return this.empty;
/*     */     }
/*     */     
/*     */     static {
/*     */       CODEC = RecordCodecBuilder.create(i -> i.group((App)MinMaxBounds.Ints.CODEC.optionalFieldOf("occupied", MinMaxBounds.Ints.ANY).forGetter(Slots::occupied), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("full", MinMaxBounds.Ints.ANY).forGetter(Slots::full), (App)MinMaxBounds.Ints.CODEC.optionalFieldOf("empty", MinMaxBounds.Ints.ANY).forGetter(Slots::empty)).apply((Applicative)i, Slots::new));
/*     */     }
/*     */     
/*     */     public static final Slots ANY = new Slots(MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY); }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/InventoryChangeTrigger.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */