/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public class Passthrough
/*    */   implements RuleBlockEntityModifier {
/*  9 */   public static final Passthrough INSTANCE = new Passthrough();
/* 10 */   public static final MapCodec<Passthrough> CODEC = MapCodec.unit(INSTANCE);
/*    */ 
/*    */   
/*    */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) {
/* 14 */     return existingTag;
/*    */   }
/*    */ 
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 19 */     return RuleBlockEntityModifierType.PASSTHROUGH;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/rule/blockentity/Passthrough.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */