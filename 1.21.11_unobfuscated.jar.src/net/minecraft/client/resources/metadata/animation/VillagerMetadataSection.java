/*    */ package net.minecraft.client.resources.metadata.animation;
/*    */ 
/*    */ 
/*    */ public final class VillagerMetadataSection extends Record {
/*    */   private final Hat hat;
/*    */   public static final com.mojang.serialization.Codec<VillagerMetadataSection> CODEC;
/*    */   
/*  8 */   public VillagerMetadataSection(Hat hat) { this.hat = hat; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection; } public Hat hat() { return this.hat; }
/*    */   public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection; }
/*    */   public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/metadata/animation/VillagerMetadataSection;
/*    */     //   0	8	1	o	Ljava/lang/Object; } static {
/* 11 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)Hat.CODEC.optionalFieldOf("hat", Hat.NONE).forGetter(VillagerMetadataSection::hat)).apply((com.mojang.datafixers.kinds.Applicative)i, VillagerMetadataSection::new));
/*    */   }
/*    */ 
/*    */   
/* 15 */   public static final net.minecraft.server.packs.metadata.MetadataSectionType<VillagerMetadataSection> TYPE = new net.minecraft.server.packs.metadata.MetadataSectionType("villager", CODEC);
/*    */   
/*    */   public enum Hat implements net.minecraft.util.StringRepresentable {
/* 18 */     NONE("none"),
/* 19 */     PARTIAL("partial"),
/* 20 */     FULL("full");
/*    */     
/* 22 */     public static final com.mojang.serialization.Codec<Hat> CODEC = (com.mojang.serialization.Codec<Hat>)net.minecraft.util.StringRepresentable.fromEnum(Hat::values);
/*    */     
/*    */     private final String name;
/*    */     
/*    */     Hat(String name) {
/* 27 */       this.name = name;
/*    */     }
/*    */ 
/*    */     
/*    */     public String getSerializedName() {
/* 32 */       return this.name;
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/metadata/animation/VillagerMetadataSection.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */