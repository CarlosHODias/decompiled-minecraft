/*    */ package net.minecraft.server.dialog.body;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.List;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ 
/*    */ public interface DialogBody {
/*    */   public static final Codec<DialogBody> DIALOG_BODY_CODEC;
/*    */   
/*    */   static {
/* 12 */     DIALOG_BODY_CODEC = BuiltInRegistries.DIALOG_BODY_TYPE.byNameCodec().dispatch(DialogBody::mapCodec, c -> c);
/*    */   } MapCodec<? extends DialogBody> mapCodec();
/* 14 */   public static final Codec<List<DialogBody>> COMPACT_LIST_CODEC = ExtraCodecs.compactListCodec(DIALOG_BODY_CODEC);
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/body/DialogBody.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */