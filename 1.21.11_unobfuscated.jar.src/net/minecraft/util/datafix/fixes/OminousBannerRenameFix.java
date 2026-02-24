/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class OminousBannerRenameFix
/*    */   extends ItemStackTagFix
/*    */ {
/*    */   public OminousBannerRenameFix(Schema outputSchema) {
/* 15 */     super(outputSchema, "OminousBannerRenameFix", id -> id.equals("minecraft:white_banner"));
/*    */   }
/*    */   
/*    */   private <T> Dynamic<T> fixItemStackTag(Dynamic<T> tag) {
/* 19 */     return tag.update("display", display -> display.update("Name", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   protected Typed<?> fixItemStackTag(Typed<?> tag) {
/* 30 */     return Util.writeAndReadTypedOrThrow(tag, tag.getType(), this::fixItemStackTag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/OminousBannerRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */