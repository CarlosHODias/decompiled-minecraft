/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ 
/*    */ public class AppendStatic implements RuleBlockEntityModifier {
/*    */   static {
/* 10 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)CompoundTag.CODEC.fieldOf("data").forGetter(())).apply((Applicative)i, AppendStatic::new));
/*    */   }
/*    */   public static final com.mojang.serialization.MapCodec<AppendStatic> CODEC;
/*    */   private final CompoundTag tag;
/*    */   
/*    */   public AppendStatic(CompoundTag tag) {
/* 16 */     this.tag = tag;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag apply(net.minecraft.util.RandomSource random, CompoundTag existingTag) {
/* 21 */     return (existingTag == null) ? this.tag.copy() : existingTag.merge(this.tag);
/*    */   }
/*    */ 
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 26 */     return RuleBlockEntityModifierType.APPEND_STATIC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/rule/blockentity/AppendStatic.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */