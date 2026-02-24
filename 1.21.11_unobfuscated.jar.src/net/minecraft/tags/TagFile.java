/*   */ package net.minecraft.tags;
/*   */ 
/*   */ public final class TagFile extends Record {
/*   */   private final java.util.List<TagEntry> entries;
/*   */   private final boolean replace;
/*   */   public static final com.mojang.serialization.Codec<TagFile> CODEC;
/*   */   
/* 8 */   public TagFile(java.util.List<TagEntry> entries, boolean replace) { this.entries = entries; this.replace = replace; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/tags/TagFile;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 8 */     //   0	7	0	this	Lnet/minecraft/tags/TagFile; } public java.util.List<TagEntry> entries() { return this.entries; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/tags/TagFile;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/tags/TagFile; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/tags/TagFile;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #8	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/tags/TagFile;
/* 8 */     //   0	8	1	o	Ljava/lang/Object; } public boolean replace() { return this.replace; } static {
/* 9 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)TagEntry.CODEC.listOf().fieldOf("values").forGetter(TagFile::entries), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("replace", false).forGetter(TagFile::replace)).apply((com.mojang.datafixers.kinds.Applicative)i, TagFile::new));
/*   */   }
/*   */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/tags/TagFile.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */