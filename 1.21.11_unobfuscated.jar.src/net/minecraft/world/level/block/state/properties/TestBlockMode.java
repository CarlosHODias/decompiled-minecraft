/*    */ package net.minecraft.world.level.block.state.properties;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum TestBlockMode
/*    */   implements StringRepresentable {
/* 14 */   START(0, "start"),
/* 15 */   LOG(1, "log"),
/* 16 */   FAIL(2, "fail"),
/* 17 */   ACCEPT(3, "accept");
/*    */   
/*    */   static {
/* 20 */     BY_ID = ByIdMap.continuous(mode -> mode.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   }
/* 22 */   public static final Codec<TestBlockMode> CODEC = (Codec<TestBlockMode>)StringRepresentable.fromEnum(TestBlockMode::values); private static final IntFunction<TestBlockMode> BY_ID; static {
/* 23 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, mode -> mode.id);
/*    */   }
/*    */   public static final StreamCodec<ByteBuf, TestBlockMode> STREAM_CODEC; private final int id;
/*    */   private final String name;
/*    */   private final Component displayName;
/*    */   private final Component detailedMessage;
/*    */   
/*    */   TestBlockMode(int id, String name) {
/* 31 */     this.id = id;
/* 32 */     this.name = name;
/* 33 */     this.displayName = (Component)Component.translatable("test_block.mode." + name);
/* 34 */     this.detailedMessage = (Component)Component.translatable("test_block.mode_info." + name);
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 39 */     return this.name;
/*    */   }
/*    */   
/*    */   public Component getDisplayName() {
/* 43 */     return this.displayName;
/*    */   }
/*    */   
/*    */   public Component getDetailedMessage() {
/* 47 */     return this.detailedMessage;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/block/state/properties/TestBlockMode.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */