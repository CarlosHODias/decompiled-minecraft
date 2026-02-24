/*    */ package net.minecraft.world.entity.animal.golem;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum CopperGolemState
/*    */   implements StringRepresentable {
/* 13 */   IDLE("idle", 0),
/* 14 */   GETTING_ITEM("getting_item", 1),
/* 15 */   GETTING_NO_ITEM("getting_no_item", 2),
/* 16 */   DROPPING_ITEM("dropping_item", 3),
/* 17 */   DROPPING_NO_ITEM("dropping_no_item", 4);
/*    */   
/* 19 */   public static final Codec<CopperGolemState> CODEC = (Codec<CopperGolemState>)StringRepresentable.fromEnum(CopperGolemState::values);
/* 20 */   private static final IntFunction<CopperGolemState> BY_ID = ByIdMap.continuous(CopperGolemState::id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/* 21 */   public static final StreamCodec<ByteBuf, CopperGolemState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CopperGolemState::id);
/*    */   
/*    */   private final String name;
/*    */   private final int id;
/*    */   
/*    */   CopperGolemState(String name, int id) {
/* 27 */     this.name = name;
/* 28 */     this.id = id;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 33 */     return this.name;
/*    */   }
/*    */   
/*    */   private int id() {
/* 37 */     return this.id;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/golem/CopperGolemState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */