/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum ItemDisplayContext
/*    */   implements StringRepresentable {
/* 10 */   NONE(0, "none"),
/* 11 */   THIRD_PERSON_LEFT_HAND(1, "thirdperson_lefthand"),
/* 12 */   THIRD_PERSON_RIGHT_HAND(2, "thirdperson_righthand"),
/* 13 */   FIRST_PERSON_LEFT_HAND(3, "firstperson_lefthand"),
/* 14 */   FIRST_PERSON_RIGHT_HAND(4, "firstperson_righthand"),
/* 15 */   HEAD(5, "head"),
/* 16 */   GUI(6, "gui"),
/* 17 */   GROUND(7, "ground"),
/* 18 */   FIXED(8, "fixed"),
/* 19 */   ON_SHELF(9, "on_shelf");
/*    */ 
/*    */   
/* 22 */   public static final Codec<ItemDisplayContext> CODEC = (Codec<ItemDisplayContext>)StringRepresentable.fromEnum(ItemDisplayContext::values);
/* 23 */   public static final IntFunction<ItemDisplayContext> BY_ID = ByIdMap.continuous(ItemDisplayContext::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   
/*    */   private final byte id;
/*    */   private final String name;
/*    */   
/*    */   ItemDisplayContext(int id, String name) {
/* 29 */     this.name = name;
/* 30 */     this.id = (byte)id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 35 */     return this.name;
/*    */   }
/*    */   
/*    */   public byte getId() {
/* 39 */     return this.id;
/*    */   }
/*    */   
/*    */   public boolean firstPerson() {
/* 43 */     return (this == FIRST_PERSON_LEFT_HAND || this == FIRST_PERSON_RIGHT_HAND);
/*    */   }
/*    */   
/*    */   public boolean leftHand() {
/* 47 */     return (this == FIRST_PERSON_LEFT_HAND || this == THIRD_PERSON_LEFT_HAND);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/ItemDisplayContext.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */