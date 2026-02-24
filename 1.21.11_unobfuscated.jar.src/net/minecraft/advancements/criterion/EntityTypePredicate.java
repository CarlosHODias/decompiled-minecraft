/*    */ package net.minecraft.advancements.criterion;
/*    */ 
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderGetter;
/*    */ import net.minecraft.core.HolderSet;
/*    */ import net.minecraft.world.entity.EntityType;
/*    */ 
/*    */ public final class EntityTypePredicate extends Record {
/*    */   private final HolderSet<EntityType<?>> types;
/*    */   
/* 11 */   public EntityTypePredicate(HolderSet<EntityType<?>> types) { this.types = types; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/advancements/criterion/EntityTypePredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 11 */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityTypePredicate; } public HolderSet<EntityType<?>> types() { return this.types; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/advancements/criterion/EntityTypePredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/advancements/criterion/EntityTypePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/advancements/criterion/EntityTypePredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #11	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/advancements/criterion/EntityTypePredicate;
/* 12 */     //   0	8	1	o	Ljava/lang/Object; } public static final com.mojang.serialization.Codec<EntityTypePredicate> CODEC = net.minecraft.core.RegistryCodecs.homogeneousList(net.minecraft.core.registries.Registries.ENTITY_TYPE).xmap(EntityTypePredicate::new, EntityTypePredicate::types);
/*    */ 
/*    */   
/*    */   public static EntityTypePredicate of(HolderGetter<EntityType<?>> lookup, EntityType<?> type) {
/* 16 */     return new EntityTypePredicate((HolderSet<EntityType<?>>)HolderSet.direct(new Holder[] { (Holder)type.builtInRegistryHolder() }));
/*    */   }
/*    */   
/*    */   public static EntityTypePredicate of(HolderGetter<EntityType<?>> lookup, net.minecraft.tags.TagKey<EntityType<?>> type) {
/* 20 */     return new EntityTypePredicate((HolderSet<EntityType<?>>)lookup.getOrThrow(type));
/*    */   }
/*    */   
/*    */   public boolean matches(EntityType<?> type) {
/* 24 */     return type.is(this.types);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/criterion/EntityTypePredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */