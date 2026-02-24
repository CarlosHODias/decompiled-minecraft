/*    */ package net.minecraft.server.jsonrpc.api;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.DataResult;
/*    */ import java.net.URI;
/*    */ import java.net.URISyntaxException;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class ReferenceUtil {
/*    */   static {
/* 10 */     REFERENCE_CODEC = Codec.STRING.comapFlatMap(string -> {
/*    */           
/*    */           try {
/*    */             return DataResult.success(new URI(string));
/* 14 */           } catch (URISyntaxException e) {
/*    */             Objects.requireNonNull(e);
/*    */             return DataResult.error(e::getMessage);
/*    */           } 
/*    */         }, URI::toString);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public static final Codec<URI> REFERENCE_CODEC;
/*    */ 
/*    */   
/*    */   public static URI createLocalReference(String typeId) {
/* 27 */     return URI.create("#/components/schemas/" + typeId);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/api/ReferenceUtil.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */