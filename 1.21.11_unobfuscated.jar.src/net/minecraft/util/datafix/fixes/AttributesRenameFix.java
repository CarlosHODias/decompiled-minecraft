/*    */ package net.minecraft.util.datafix.fixes;
/*    */ 
/*    */ import com.mojang.datafixers.DSL;
/*    */ import com.mojang.datafixers.DataFix;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.TypeRewriteRule;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.function.UnaryOperator;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.util.datafix.ExtraDataFixUtils;
/*    */ 
/*    */ public class AttributesRenameFix extends DataFix {
/*    */   private final String name;
/*    */   
/*    */   public AttributesRenameFix(Schema outputSchema, String name, UnaryOperator<String> renames) {
/* 19 */     super(outputSchema, false);
/* 20 */     this.name = name;
/* 21 */     this.renames = renames;
/*    */   }
/*    */   private final UnaryOperator<String> renames;
/*    */   
/*    */   protected TypeRewriteRule makeRule() {
/* 26 */     return TypeRewriteRule.seq(
/* 27 */         fixTypeEverywhereTyped(this.name + " (Components)", getInputSchema().getType(References.DATA_COMPONENTS), this::fixDataComponents), new TypeRewriteRule[] {
/* 28 */           fixTypeEverywhereTyped(this.name + " (Entity)", getInputSchema().getType(References.ENTITY), this::fixEntity), 
/* 29 */           fixTypeEverywhereTyped(this.name + " (Player)", getInputSchema().getType(References.PLAYER), this::fixEntity)
/*    */         });
/*    */   }
/*    */   
/*    */   private Typed<?> fixDataComponents(Typed<?> components) {
/* 34 */     return components.update(DSL.remainderFinder(), componentData -> componentData.update("minecraft:attribute_modifiers", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Typed<?> fixEntity(Typed<?> entity) {
/* 47 */     return entity.update(DSL.remainderFinder(), tag -> tag.update("attributes", ()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private Dynamic<?> fixIdField(Dynamic<?> dynamic) {
/* 58 */     return ExtraDataFixUtils.fixStringField(dynamic, "id", this.renames);
/*    */   }
/*    */   
/*    */   private Dynamic<?> fixTypeField(Dynamic<?> dynamic) {
/* 62 */     return ExtraDataFixUtils.fixStringField(dynamic, "type", this.renames);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/AttributesRenameFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */