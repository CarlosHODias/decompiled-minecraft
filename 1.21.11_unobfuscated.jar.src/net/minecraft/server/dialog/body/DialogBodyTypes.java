/*    */ package net.minecraft.server.dialog.body;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class DialogBodyTypes {
/*    */   public static MapCodec<? extends DialogBody> bootstrap(Registry<MapCodec<? extends DialogBody>> registry) {
/*  9 */     Registry.register(registry, Identifier.withDefaultNamespace("item"), ItemBody.MAP_CODEC);
/* 10 */     return (MapCodec<? extends DialogBody>)Registry.register(registry, Identifier.withDefaultNamespace("plain_message"), PlainMessage.MAP_CODEC);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/dialog/body/DialogBodyTypes.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */