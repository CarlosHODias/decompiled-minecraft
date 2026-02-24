/*    */ package net.minecraft.world.item.component;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ 
/*    */ public enum MapPostProcessing
/*    */ {
/* 11 */   LOCK(0),
/* 12 */   SCALE(1);
/*    */ 
/*    */   
/* 15 */   public static final IntFunction<MapPostProcessing> ID_MAP = ByIdMap.continuous(MapPostProcessing::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 16 */   public static final StreamCodec<ByteBuf, MapPostProcessing> STREAM_CODEC = ByteBufCodecs.idMapper(ID_MAP, MapPostProcessing::id);
/*    */   
/*    */   private final int id;
/*    */   
/*    */   MapPostProcessing(int id) {
/* 21 */     this.id = id;
/*    */   }
/*    */   
/*    */   public int id() {
/* 25 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/component/MapPostProcessing.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */