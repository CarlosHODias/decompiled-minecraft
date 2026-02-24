/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.core.registries.Registries;
/*    */ import net.minecraft.world.item.equipment.trim.ArmorTrim;
/*    */ import net.minecraft.world.item.equipment.trim.TrimMaterial;
/*    */ import net.minecraft.world.item.equipment.trim.TrimPattern;
/*    */ 
/*    */ public final class TrimPredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<ArmorTrim> {
/*    */   private final Optional<HolderSet<TrimMaterial>> material;
/*    */   private final Optional<HolderSet<TrimPattern>> pattern;
/*    */   public static final com.mojang.serialization.Codec<TrimPredicate> CODEC;
/*    */   
/* 17 */   public TrimPredicate(Optional<HolderSet<TrimMaterial>> material, Optional<HolderSet<TrimPattern>> pattern) { this.material = material; this.pattern = pattern; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/TrimPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 17 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/TrimPredicate; } public Optional<HolderSet<TrimMaterial>> material() { return this.material; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/TrimPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/TrimPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/TrimPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/TrimPredicate;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<HolderSet<TrimPattern>> pattern() { return this.pattern; }
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 22 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)net.minecraft.core.RegistryCodecs.homogeneousList(Registries.TRIM_MATERIAL).optionalFieldOf("material").forGetter(TrimPredicate::material), (App)net.minecraft.core.RegistryCodecs.homogeneousList(Registries.TRIM_PATTERN).optionalFieldOf("pattern").forGetter(TrimPredicate::pattern)).apply((com.mojang.datafixers.kinds.Applicative)i, TrimPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<ArmorTrim> componentType() {
/* 29 */     return net.minecraft.core.component.DataComponents.TRIM;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(ArmorTrim value) {
/* 34 */     if (this.material.isPresent() && !((HolderSet)this.material.get()).contains(value.material())) {
/* 35 */       return false;
/*    */     }
/*    */     
/* 38 */     if (this.pattern.isPresent() && !((HolderSet)this.pattern.get()).contains(value.pattern())) {
/* 39 */       return false;
/*    */     }
/*    */     
/* 42 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/TrimPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */