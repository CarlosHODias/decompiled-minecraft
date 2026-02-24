/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.ChatFormatting;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum Rarity
/*    */   implements StringRepresentable {
/* 14 */   COMMON(0, "common", ChatFormatting.WHITE),
/* 15 */   UNCOMMON(1, "uncommon", ChatFormatting.YELLOW),
/* 16 */   RARE(2, "rare", ChatFormatting.AQUA),
/* 17 */   EPIC(3, "epic", ChatFormatting.LIGHT_PURPLE); public static final IntFunction<Rarity> BY_ID;
/*    */   public static final StreamCodec<ByteBuf, Rarity> STREAM_CODEC;
/* 19 */   public static final Codec<Rarity> CODEC = StringRepresentable.fromValues(Rarity::values);
/*    */   static {
/* 21 */     BY_ID = ByIdMap.continuous(r -> r.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 22 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, r -> r.id);
/*    */   }
/*    */   private final int id;
/*    */   private final String name;
/*    */   private final ChatFormatting color;
/*    */   
/*    */   Rarity(int id, String name, ChatFormatting color) {
/* 29 */     this.id = id;
/* 30 */     this.name = name;
/* 31 */     this.color = color;
/*    */   }
/*    */   
/*    */   public ChatFormatting color() {
/* 35 */     return this.color;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 40 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/Rarity.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */