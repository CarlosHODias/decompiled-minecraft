/*     */ package net.minecraft.world.entity;
/*     */ 
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.List;
/*     */ import java.util.function.IntFunction;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ import net.minecraft.util.ByIdMap;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public enum EquipmentSlot
/*     */   implements StringRepresentable
/*     */ {
/*  15 */   MAINHAND(Type.HAND, 0, 0, "mainhand"),
/*  16 */   OFFHAND(Type.HAND, 1, 5, "offhand"),
/*  17 */   FEET(Type.HUMANOID_ARMOR, 0, 1, 1, "feet"),
/*  18 */   LEGS(Type.HUMANOID_ARMOR, 1, 1, 2, "legs"),
/*  19 */   CHEST(Type.HUMANOID_ARMOR, 2, 1, 3, "chest"),
/*  20 */   HEAD(Type.HUMANOID_ARMOR, 3, 1, 4, "head"),
/*  21 */   BODY(Type.ANIMAL_ARMOR, 0, 1, 6, "body"),
/*  22 */   SADDLE(Type.SADDLE, 0, 1, 7, "saddle");
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  27 */   public static final List<EquipmentSlot> VALUES = List.of(values());
/*     */   static {
/*  29 */     BY_ID = ByIdMap.continuous(s -> s.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*     */   }
/*  31 */   public static final StringRepresentable.EnumCodec<EquipmentSlot> CODEC = StringRepresentable.fromEnum(EquipmentSlot::values); public static final int NO_COUNT_LIMIT = 0; public static final IntFunction<EquipmentSlot> BY_ID; static {
/*  32 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, s -> s.id);
/*     */   }
/*     */   public static final StreamCodec<ByteBuf, EquipmentSlot> STREAM_CODEC; private final Type type;
/*     */   private final int index;
/*     */   private final int countLimit;
/*     */   private final int id;
/*     */   private final String name;
/*     */   
/*     */   EquipmentSlot(Type type, int index, int countLimit, int id, String name) {
/*  41 */     this.type = type;
/*  42 */     this.index = index;
/*  43 */     this.countLimit = countLimit;
/*  44 */     this.id = id;
/*  45 */     this.name = name;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Type getType() {
/*  53 */     return this.type;
/*     */   }
/*     */   
/*     */   public int getIndex() {
/*  57 */     return this.index;
/*     */   }
/*     */   
/*     */   public int getIndex(int base) {
/*  61 */     return base + this.index;
/*     */   }
/*     */   
/*     */   public ItemStack limit(ItemStack toEquip) {
/*  65 */     return (this.countLimit > 0) ? toEquip.split(this.countLimit) : toEquip;
/*     */   }
/*     */   
/*     */   public int getId() {
/*  69 */     return this.id;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFilterBit(int offset) {
/*  74 */     return this.id + offset;
/*     */   }
/*     */   
/*     */   public String getName() {
/*  78 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean isArmor() {
/*  82 */     return (this.type == Type.HUMANOID_ARMOR || this.type == Type.ANIMAL_ARMOR);
/*     */   }
/*     */ 
/*     */   
/*     */   public String getSerializedName() {
/*  87 */     return this.name;
/*     */   }
/*     */   
/*     */   public boolean canIncreaseExperience() {
/*  91 */     return (this.type != Type.SADDLE);
/*     */   }
/*     */   
/*     */   public enum Type {
/*  95 */     HAND,
/*  96 */     HUMANOID_ARMOR,
/*  97 */     ANIMAL_ARMOR,
/*  98 */     SADDLE;
/*     */   }
/*     */   
/*     */   public static EquipmentSlot byName(String name) {
/* 102 */     EquipmentSlot slot = (EquipmentSlot)CODEC.byName(name);
/* 103 */     if (slot != null) {
/* 104 */       return slot;
/*     */     }
/* 106 */     throw new IllegalArgumentException("Invalid slot '" + name + "'");
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/EquipmentSlot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */