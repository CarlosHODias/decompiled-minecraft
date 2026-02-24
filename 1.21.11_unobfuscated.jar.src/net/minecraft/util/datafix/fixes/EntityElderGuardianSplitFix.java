/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.datafixers.util.Pair;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ 
/*    */ public class EntityElderGuardianSplitFix
/*    */   extends SimpleEntityRenameFix {
/*    */   public EntityElderGuardianSplitFix(Schema outputSchema, boolean changesType) {
/* 11 */     super("EntityElderGuardianSplitFix", outputSchema, changesType);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Pair<String, Dynamic<?>> getNewNameAndTag(String name, Dynamic<?> tag) {
/* 16 */     return Pair.of((Objects.equals(name, "Guardian") && tag.get("Elder").asBoolean(false)) ? "ElderGuardian" : name, tag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/EntityElderGuardianSplitFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */