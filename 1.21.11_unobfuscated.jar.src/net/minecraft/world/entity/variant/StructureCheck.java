/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.HolderSet;
/*    */ 
/*    */ public final class StructureCheck extends Record implements SpawnCondition {
/*    */   private final HolderSet<net.minecraft.world.level.levelgen.structure.Structure> requiredStructures;
/*    */   public static final com.mojang.serialization.MapCodec<StructureCheck> MAP_CODEC;
/*    */   
/* 10 */   public StructureCheck(HolderSet<net.minecraft.world.level.levelgen.structure.Structure> requiredStructures) { this.requiredStructures = requiredStructures; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/variant/StructureCheck;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 10 */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/StructureCheck; } public HolderSet<net.minecraft.world.level.levelgen.structure.Structure> requiredStructures() { return this.requiredStructures; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/variant/StructureCheck;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/variant/StructureCheck; } public final boolean equals(Object o) {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/variant/StructureCheck;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #10	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/variant/StructureCheck;
/*    */     //   0	8	1	o	Ljava/lang/Object;
/*    */   } static {
/* 14 */     MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.STRUCTURE).fieldOf("structures").forGetter(StructureCheck::requiredStructures)).apply((com.mojang.datafixers.kinds.Applicative)i, StructureCheck::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean test(SpawnContext context) {
/* 20 */     return context.level().getLevel().structureManager().getStructureWithPieceAt(context.pos(), this.requiredStructures).isValid();
/*    */   }
/*    */ 
/*    */   
/*    */   public com.mojang.serialization.MapCodec<StructureCheck> codec() {
/* 25 */     return MAP_CODEC;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/variant/StructureCheck.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */