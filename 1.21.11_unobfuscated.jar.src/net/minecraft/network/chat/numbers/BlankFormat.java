/*    */ package net.minecraft.network.chat.numbers;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.MutableComponent;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ 
/*    */ public class BlankFormat implements NumberFormat {
/* 10 */   public static final BlankFormat INSTANCE = new BlankFormat();
/*    */   
/* 12 */   public static final NumberFormatType<BlankFormat> TYPE = new NumberFormatType<BlankFormat>() {
/* 13 */       private static final MapCodec<BlankFormat> CODEC = MapCodec.unit(BlankFormat.INSTANCE);
/*    */       
/* 15 */       private static final StreamCodec<RegistryFriendlyByteBuf, BlankFormat> STREAM_CODEC = StreamCodec.unit(BlankFormat.INSTANCE);
/*    */ 
/*    */       
/*    */       public MapCodec<BlankFormat> mapCodec() {
/* 19 */         return CODEC;
/*    */       }
/*    */ 
/*    */       
/*    */       public StreamCodec<RegistryFriendlyByteBuf, BlankFormat> streamCodec() {
/* 24 */         return STREAM_CODEC;
/*    */       }
/*    */     };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public MutableComponent format(int value) {
/* 33 */     return Component.empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public NumberFormatType<BlankFormat> type() {
/* 38 */     return TYPE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/numbers/BlankFormat.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */