/*    */ package net.minecraft.server.dialog;
/*    */ 
/*    */ import io.netty.buffer.ByteBuf;
/*    */ import java.util.function.IntFunction;
/*    */ import net.minecraft.network.codec.ByteBufCodecs;
/*    */ import net.minecraft.network.codec.StreamCodec;
/*    */ import net.minecraft.util.ByIdMap;
/*    */ import net.minecraft.util.StringRepresentable;
/*    */ 
/*    */ public enum DialogAction
/*    */   implements StringRepresentable {
/* 12 */   CLOSE(0, "close"),
/* 13 */   NONE(1, "none"),
/* 14 */   WAIT_FOR_RESPONSE(2, "wait_for_response");
/*    */   
/*    */   static {
/* 17 */     BY_ID = ByIdMap.continuous(s -> s.id, (Object[])values(), ByIdMap.OutOfBoundsStrategy.ZERO);
/*    */   }
/* 19 */   public static final StringRepresentable.EnumCodec<DialogAction> CODEC = StringRepresentable.fromEnum(DialogAction::values); public static final IntFunction<DialogAction> BY_ID; static {
/* 20 */     STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, s -> s.id);
/*    */   }
/*    */   public static final StreamCodec<ByteBuf, DialogAction> STREAM_CODEC; private final int id;
/*    */   private final String name;
/*    */   
/*    */   DialogAction(int id, String name) {
/* 26 */     this.id = id;
/* 27 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getSerializedName() {
/* 32 */     return this.name;
/*    */   }
/*    */   
/*    */   public boolean willUnpause() {
/* 36 */     return (this == CLOSE || this == WAIT_FOR_RESPONSE);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/DialogAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */