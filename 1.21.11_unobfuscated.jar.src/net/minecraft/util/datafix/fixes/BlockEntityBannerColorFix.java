/*    */ package net.minecraft.util.datafix.fixes;
/*    */ import com.mojang.datafixers.DataFixUtils;
/*    */ import com.mojang.datafixers.Typed;
/*    */ import com.mojang.datafixers.schemas.Schema;
/*    */ import com.mojang.serialization.Dynamic;
/*    */ import java.util.Objects;
/*    */ import java.util.stream.Stream;
/*    */ 
/*    */ public class BlockEntityBannerColorFix extends NamedEntityFix {
/*    */   public BlockEntityBannerColorFix(Schema outputSchema, boolean changesType) {
/* 11 */     super(outputSchema, changesType, "BlockEntityBannerColorFix", References.BLOCK_ENTITY, "minecraft:banner");
/*    */   }
/*    */   
/*    */   public Dynamic<?> fixTag(Dynamic<?> input) {
/* 15 */     input = input.update("Base", base -> base.createInt(15 - base.asInt(0)));
/*    */     
/* 17 */     input = input.update("Patterns", list -> {
/*    */           Objects.requireNonNull(list);
/*    */           
/*    */           return DataFixUtils.orElse(list.asStreamOpt().map(()).map(list::createList).result(), list);
/*    */         });
/*    */     
/* 23 */     return input;
/*    */   }
/*    */ 
/*    */   
/*    */   protected Typed<?> fix(Typed<?> entity) {
/* 28 */     return entity.update(com.mojang.datafixers.DSL.remainderFinder(), this::fixTag);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/datafix/fixes/BlockEntityBannerColorFix.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */