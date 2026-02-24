/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityTippedArrowFix
/*    */   extends SimplestEntityRenameFix {
/*    */   public EntityTippedArrowFix(Schema outputSchema, boolean changesType) {
/*  9 */     super("EntityTippedArrowFix", outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String rename(String name) {
/* 14 */     return Objects.equals(name, "TippedArrow") ? "Arrow" : name;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityTippedArrowFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */