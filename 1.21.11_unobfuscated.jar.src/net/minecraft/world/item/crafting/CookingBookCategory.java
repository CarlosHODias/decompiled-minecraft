/*    */ package net.minecraft.world.item.crafting;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum CookingBookCategory
/*    */   implements StringRepresentable {
/* 13 */   FOOD(0, "food"),
/* 14 */   BLOCKS(1, "blocks"),
/* 15 */   MISC(2, "misc");
/*    */   
/*    */   static {
/* 18 */     BY_ID = ByIdMap.continuous(e -> e.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   }
/* 20 */   public static final Codec<CookingBookCategory> CODEC = (Codec<CookingBookCategory>)StringRepresentable.fromEnum(CookingBookCategory::values); private static final IntFunction<CookingBookCategory> BY_ID; static {
/* 21 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, e -> e.id);
/*    */   }
/*    */   public static final StreamCodec<ByteBuf, CookingBookCategory> STREAM_CODEC; private final int id;
/*    */   private final String name;
/*    */   
/*    */   CookingBookCategory(int id, String name) {
/* 27 */     this.id = id;
/* 28 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 33 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/crafting/CookingBookCategory.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */