/*    */ package net.minecraft.util;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public enum Unit {
/*  9 */   INSTANCE;
/*    */   
/* 11 */   public static final Codec<Unit> CODEC = MapCodec.unitCodec(INSTANCE);
/* 12 */   public static final StreamCodec<ByteBuf, Unit> STREAM_CODEC = StreamCodec.unit(INSTANCE);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/Unit.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */