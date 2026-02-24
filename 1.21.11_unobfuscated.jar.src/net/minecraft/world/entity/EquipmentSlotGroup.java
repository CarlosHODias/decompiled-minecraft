/*    */ package net.minecraft.world.entity;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.Iterator;
/*    */ import java.util.List;
/*    */ import java.util.function.IntFunction;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum EquipmentSlotGroup
/*    */   implements StringRepresentable, Iterable<EquipmentSlot> {
/* 16 */   ANY(0, "any", slot -> true),
/* 17 */   MAINHAND(1, "mainhand", EquipmentSlot.MAINHAND),
/* 18 */   OFFHAND(2, "offhand", EquipmentSlot.OFFHAND),
/*    */   
/* 20 */   FEET(4, "feet", EquipmentSlot.FEET),
/* 21 */   LEGS(5, "legs", EquipmentSlot.LEGS),
/* 22 */   CHEST(6, "chest", EquipmentSlot.CHEST),
/* 23 */   HEAD(7, "head", EquipmentSlot.HEAD),
/* 24 */   ARMOR(8, "armor", EquipmentSlot::isArmor),
/* 25 */   BODY(9, "body", EquipmentSlot.BODY),
/* 26 */   SADDLE(10, "saddle", EquipmentSlot.SADDLE), HAND(10, "saddle", EquipmentSlot.SADDLE); static {
/*    */     HAND = new EquipmentSlotGroup("HAND", 3, 3, "hand", slot -> (slot.getType() == EquipmentSlot.Type.HAND));
/*    */   } static {
/* 29 */     BY_ID = ByIdMap.continuous(s -> s.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   }
/* 31 */   public static final Codec<EquipmentSlotGroup> CODEC = (Codec<EquipmentSlotGroup>)StringRepresentable.fromEnum(EquipmentSlotGroup::values); public static final IntFunction<EquipmentSlotGroup> BY_ID; public static final StreamCodec<ByteBuf, EquipmentSlotGroup> STREAM_CODEC; private final int id; private final String key; private final Predicate<EquipmentSlot> predicate; private final List<EquipmentSlot> slots; static {
/* 32 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, s -> s.id);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   EquipmentSlotGroup(int id, String key, Predicate<EquipmentSlot> predicate) {
/* 40 */     this.id = id;
/* 41 */     this.key = key;
/* 42 */     this.predicate = predicate;
/* 43 */     this.slots = EquipmentSlot.VALUES.stream().filter(predicate).toList();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static EquipmentSlotGroup bySlot(EquipmentSlot slot) {
/* 51 */     switch (slot) { default: throw new MatchException(null, null);case MAINHAND: case OFFHAND: case FEET: case LEGS: case CHEST: case HEAD: case BODY: case SADDLE: break; }  return 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */       
/* 59 */       SADDLE;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 65 */     return this.key;
/*    */   }
/*    */   
/*    */   public boolean test(EquipmentSlot slot) {
/* 69 */     return this.predicate.test(slot);
/*    */   }
/*    */   
/*    */   public List<EquipmentSlot> slots() {
/* 73 */     return this.slots;
/*    */   }
/*    */ 
/*    */   
/*    */   public Iterator<EquipmentSlot> iterator() {
/* 78 */     return this.slots.iterator();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EquipmentSlotGroup.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */