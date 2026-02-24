/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function3;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.util.ProblemReporter;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulator;
/*    */ import net.minecraft.world.level.storage.loot.ContainerComponentManipulators;
/*    */ import net.minecraft.world.level.storage.loot.LootContext;
/*    */ import net.minecraft.world.level.storage.loot.ValidationContext;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class ModifyContainerContents extends LootItemConditionalFunction {
/*    */   static {
/* 16 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ContainerComponentManipulators.CODEC.fieldOf("component").forGetter(()), (App)LootItemFunctions.ROOT_CODEC.fieldOf("modifier").forGetter(()))).apply((Applicative)i, ModifyContainerContents::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<ModifyContainerContents> CODEC;
/*    */   private final ContainerComponentManipulator<?> component;
/*    */   private final LootItemFunction modifier;
/*    */   
/*    */   private ModifyContainerContents(java.util.List<LootItemCondition> predicates, ContainerComponentManipulator<?> component, LootItemFunction modifier) {
/* 25 */     super(predicates);
/* 26 */     this.component = component;
/* 27 */     this.modifier = modifier;
/*    */   }
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<ModifyContainerContents> getType() {
/* 32 */     return LootItemFunctions.MODIFY_CONTENTS;
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemStack run(ItemStack itemStack, LootContext context) {
/* 37 */     if (itemStack.isEmpty()) {
/* 38 */       return itemStack;
/*    */     }
/*    */     
/* 41 */     this.component.modifyItems(itemStack, c -> this.modifier.apply(context, context));
/*    */     
/* 43 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public void validate(ValidationContext context) {
/* 48 */     super.validate(context);
/* 49 */     this.modifier.validate(context.forChild((ProblemReporter.PathElement)new ProblemReporter.FieldPathElement("modifier")));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/ModifyContainerContents.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */