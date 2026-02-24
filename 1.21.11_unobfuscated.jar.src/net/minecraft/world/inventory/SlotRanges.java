/*     */ package net.minecraft.world.inventory;
/*     */ 
/*     */ import com.mojang.serialization.Codec;
/*     */ import it.unimi.dsi.fastutil.ints.IntArrayList;
/*     */ import it.unimi.dsi.fastutil.ints.IntList;
/*     */ import it.unimi.dsi.fastutil.ints.IntLists;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Stream;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.EquipmentSlot;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SlotRanges
/*     */ {
/*     */   private static final List<SlotRange> SLOTS;
/*     */   
/*     */   static {
/*  23 */     SLOTS = (List<SlotRange>)Util.make(new ArrayList(), values -> {
/*     */           addSingleSlot(values, "contents", 0);
/*     */           addSlotRange(values, "container.", 0, 54);
/*     */           addSlotRange(values, "hotbar.", 0, 9);
/*     */           addSlotRange(values, "inventory.", 9, 27);
/*     */           addSlotRange(values, "enderchest.", 200, 27);
/*     */           addSlotRange(values, "villager.", 300, 8);
/*     */           addSlotRange(values, "horse.", 500, 15);
/*     */           int mainhand = EquipmentSlot.MAINHAND.getIndex(98), offhand = EquipmentSlot.OFFHAND.getIndex(98);
/*     */           addSingleSlot(values, "weapon", mainhand);
/*     */           addSingleSlot(values, "weapon.mainhand", mainhand);
/*     */           addSingleSlot(values, "weapon.offhand", offhand);
/*     */           addSlots(values, "weapon.*", new int[] { mainhand, offhand });
/*     */           int head = EquipmentSlot.HEAD.getIndex(100), chest = EquipmentSlot.CHEST.getIndex(100), legs = EquipmentSlot.LEGS.getIndex(100), feet = EquipmentSlot.FEET.getIndex(100), body = EquipmentSlot.BODY.getIndex(105);
/*     */           addSingleSlot(values, "armor.head", head);
/*     */           addSingleSlot(values, "armor.chest", chest);
/*     */           addSingleSlot(values, "armor.legs", legs);
/*     */           addSingleSlot(values, "armor.feet", feet);
/*     */           addSingleSlot(values, "armor.body", body);
/*     */           addSlots(values, "armor.*", new int[] { head, chest, legs, feet, body });
/*     */           addSingleSlot(values, "saddle", EquipmentSlot.SADDLE.getIndex(106));
/*     */           addSingleSlot(values, "horse.chest", 499);
/*     */           addSingleSlot(values, "player.cursor", 499);
/*     */           addSlotRange(values, "player.crafting.", 500, 4);
/*     */         });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private static final Function<String, SlotRange> NAME_LOOKUP;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  64 */   public static final Codec<SlotRange> CODEC = StringRepresentable.fromValues(() -> (SlotRange[])SLOTS.toArray(()));
/*     */   static {
/*  66 */     NAME_LOOKUP = StringRepresentable.createNameLookup((StringRepresentable[])SLOTS.toArray(x$0 -> new SlotRange[x$0]));
/*     */   }
/*     */   private static SlotRange create(String name, int id) {
/*  69 */     return SlotRange.of(name, IntLists.singleton(id));
/*     */   }
/*     */   
/*     */   private static SlotRange create(String name, IntList ids) {
/*  73 */     return SlotRange.of(name, IntLists.unmodifiable(ids));
/*     */   }
/*     */   
/*     */   private static SlotRange create(String name, int... ids) {
/*  77 */     return SlotRange.of(name, IntList.of(ids));
/*     */   }
/*     */   
/*     */   private static void addSingleSlot(List<SlotRange> output, String name, int id) {
/*  81 */     output.add(create(name, id));
/*     */   }
/*     */   
/*     */   private static void addSlotRange(List<SlotRange> output, String prefix, int offset, int size) {
/*  85 */     IntArrayList intArrayList = new IntArrayList(size);
/*  86 */     for (int i = 0; i < size; i++) {
/*  87 */       int slotId = offset + i;
/*  88 */       output.add(create(prefix + prefix, slotId));
/*  89 */       intArrayList.add(slotId);
/*     */     } 
/*  91 */     output.add(create(prefix + "*", (IntList)intArrayList));
/*     */   }
/*     */   
/*     */   private static void addSlots(List<SlotRange> output, String name, int... values) {
/*  95 */     output.add(create(name, values));
/*     */   }
/*     */   
/*     */   public static SlotRange nameToIds(String name) {
/*  99 */     return NAME_LOOKUP.apply(name);
/*     */   }
/*     */   
/*     */   public static Stream<String> allNames() {
/* 103 */     return SLOTS.stream().map(StringRepresentable::getSerializedName);
/*     */   }
/*     */   
/*     */   public static Stream<String> singleSlotNames() {
/* 107 */     return SLOTS.stream().filter(e -> (e.size() == 1)).map(StringRepresentable::getSerializedName);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/SlotRanges.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */