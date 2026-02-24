/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum SwingAnimationType
/*    */   implements StringRepresentable {
/* 13 */   NONE(0, "none"),
/* 14 */   WHACK(1, "whack"),
/* 15 */   STAB(2, "stab");
/*    */ 
/*    */   
/* 18 */   private static final IntFunction<SwingAnimationType> BY_ID = ByIdMap.continuous(SwingAnimationType::getId, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 19 */   public static final Codec<SwingAnimationType> CODEC = (Codec<SwingAnimationType>)StringRepresentable.fromEnum(SwingAnimationType::values);
/* 20 */   public static final StreamCodec<ByteBuf, SwingAnimationType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, SwingAnimationType::getId);
/*    */   
/*    */   private final int id;
/*    */   private final String name;
/*    */   
/*    */   SwingAnimationType(int id, String name) {
/* 26 */     this.id = id;
/* 27 */     this.name = name;
/*    */   }
/*    */   
/*    */   public int getId() {
/* 31 */     return this.id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 36 */     return this.name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/SwingAnimationType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */