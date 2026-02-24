/*    */ package net.minecraft.world.inventory;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum ClickType
/*    */ {
/* 11 */   PICKUP(0),
/* 12 */   QUICK_MOVE(1),
/* 13 */   SWAP(2),
/* 14 */   CLONE(3),
/* 15 */   THROW(4),
/* 16 */   QUICK_CRAFT(5),
/* 17 */   PICKUP_ALL(6);
/*    */ 
/*    */   
/* 20 */   private static final IntFunction<ClickType> BY_ID = ByIdMap.continuous(ClickType::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   
/* 22 */   public static final StreamCodec<ByteBuf, ClickType> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, ClickType::id);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   ClickType(int id) {
/* 27 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int id() {
/* 31 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/inventory/ClickType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */