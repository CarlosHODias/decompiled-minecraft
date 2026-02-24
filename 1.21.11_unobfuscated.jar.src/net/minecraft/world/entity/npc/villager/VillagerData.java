/*    */ package net.minecraft.world.entity.npc.villager;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.registries.BuiltInRegistries;
/*    */ 
/*    */ public final class VillagerData extends Record {
/*    */   private final Holder<VillagerType> type;
/*    */   private final Holder<VillagerProfession> profession;
/*    */   private final int level;
/*    */   public static final int MIN_VILLAGER_LEVEL = 1;
/*    */   
/* 14 */   public Holder<VillagerType> type() { return this.type; } public static final int MAX_VILLAGER_LEVEL = 5; public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/npc/villager/VillagerData;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/npc/villager/VillagerData;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/npc/villager/VillagerData;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #14	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/world/entity/npc/villager/VillagerData;
/* 14 */     //   0	8	1	o	Ljava/lang/Object; } public Holder<VillagerProfession> profession() { return this.profession; } public int level() { return this.level; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 21 */   private static final int[] NEXT_LEVEL_XP_THRESHOLDS = new int[] { 0, 10, 70, 150, 250 }; public static final com.mojang.serialization.Codec<VillagerData> CODEC;
/*    */   static {
/* 23 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)BuiltInRegistries.VILLAGER_TYPE.holderByNameCodec().fieldOf("type").orElseGet(()).forGetter(()), (App)BuiltInRegistries.VILLAGER_PROFESSION.holderByNameCodec().fieldOf("profession").orElseGet(()).forGetter(()), (App)com.mojang.serialization.Codec.INT.fieldOf("level").orElse(1).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, VillagerData::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/* 29 */   public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, VillagerData> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.composite(
/* 30 */       net.minecraft.network.codec.ByteBufCodecs.holderRegistry(net.minecraft.core.registries.Registries.VILLAGER_TYPE), VillagerData::type, 
/* 31 */       net.minecraft.network.codec.ByteBufCodecs.holderRegistry(net.minecraft.core.registries.Registries.VILLAGER_PROFESSION), VillagerData::profession, net.minecraft.network.codec.ByteBufCodecs.VAR_INT, VillagerData::level, VillagerData::new);
/*    */ 
/*    */ 
/*    */   
/*    */   public VillagerData(Holder<VillagerType> type, Holder<VillagerProfession> profession, int level)
/*    */   {
/* 37 */     level = Math.max(1, level);
/*    */     this.type = type;
/*    */     this.profession = profession;
/*    */     this.level = level; } public VillagerData withType(Holder<VillagerType> type) {
/* 41 */     return new VillagerData(type, this.profession, this.level);
/*    */   }
/*    */   
/*    */   public VillagerData withType(net.minecraft.core.HolderGetter.Provider registries, net.minecraft.resources.ResourceKey<VillagerType> type) {
/* 45 */     return withType((Holder<VillagerType>)registries.getOrThrow(type));
/*    */   }
/*    */   
/*    */   public VillagerData withProfession(Holder<VillagerProfession> profession) {
/* 49 */     return new VillagerData(this.type, profession, this.level);
/*    */   }
/*    */   
/*    */   public VillagerData withProfession(net.minecraft.core.HolderGetter.Provider registries, net.minecraft.resources.ResourceKey<VillagerProfession> profession) {
/* 53 */     return withProfession((Holder<VillagerProfession>)registries.getOrThrow(profession));
/*    */   }
/*    */   
/*    */   public VillagerData withLevel(int level) {
/* 57 */     return new VillagerData(this.type, this.profession, level);
/*    */   }
/*    */   
/*    */   public static int getMinXpPerLevel(int level) {
/* 61 */     return canLevelUp(level) ? NEXT_LEVEL_XP_THRESHOLDS[level - 1] : 0;
/*    */   }
/*    */   
/*    */   public static int getMaxXpPerLevel(int level) {
/* 65 */     return canLevelUp(level) ? NEXT_LEVEL_XP_THRESHOLDS[level] : 0;
/*    */   }
/*    */   
/*    */   public static boolean canLevelUp(int currentLevel) {
/* 69 */     return (currentLevel >= 1 && currentLevel < 5);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/npc/villager/VillagerData.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */