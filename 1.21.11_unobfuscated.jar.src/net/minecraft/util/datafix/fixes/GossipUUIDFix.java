/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class GossipUUIDFix extends NamedEntityFix {
/*    */   public GossipUUIDFix(Schema outputSchema, String entityName) {
/* 10 */     super(outputSchema, false, "Gossip for for " + entityName, References.ENTITY, entityName);
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 15 */     return entity.update(com.mojang.datafixers.DSL.remainderFinder(), tag -> tag.update("Gossips", ()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/GossipUUIDFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */