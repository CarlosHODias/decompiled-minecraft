/*    */ package net.minecraft.server.packs;
/*    */ 
/*    */ import net.minecraft.server.packs.metadata.pack.PackFormat;
/*    */ 
/*    */ public final class OverlayEntry extends Record {
/*    */   private final net.minecraft.util.InclusiveRange<PackFormat> format;
/*    */   private final String overlay;
/*    */   
/*    */   public final String toString() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/*    */   }
/*    */   
/*    */   public final int hashCode() {
/*    */     // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/*    */   }
/*    */   
/* 17 */   public OverlayEntry(net.minecraft.util.InclusiveRange<PackFormat> format, String overlay) { this.format = format; this.overlay = overlay; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #17	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry;
/* 17 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.util.InclusiveRange<PackFormat> format() { return this.format; } public String overlay() { return this.overlay; } private static final class IntermediateEntry extends Record implements PackFormat.IntermediaryFormatHolder { private final PackFormat.IntermediaryFormat format; private final String overlay; private static final com.mojang.serialization.Codec<IntermediateEntry> CODEC;
/* 18 */     private IntermediateEntry(PackFormat.IntermediaryFormat format, String overlay) { this.format = format; this.overlay = overlay; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #18	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/server/packs/OverlayMetadataSection$OverlayEntry$IntermediateEntry;
/* 18 */       //   0	8	1	o	Ljava/lang/Object; } public PackFormat.IntermediaryFormat format() { return this.format; } public String overlay() { return this.overlay; } static {
/* 19 */       CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)PackFormat.IntermediaryFormat.OVERLAY_CODEC.forGetter(IntermediateEntry::format), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.STRING.validate(OverlayMetadataSection::validateOverlayDir).fieldOf("directory").forGetter(IntermediateEntry::overlay)).apply((com.mojang.datafixers.kinds.Applicative)i, IntermediateEntry::new));
/*    */     }
/*    */ 
/*    */ 
/*    */ 
/*    */     
/*    */     public String toString() {
/* 26 */       return this.overlay;
/*    */     } }
/*    */ 
/*    */   
/*    */   private static com.mojang.serialization.Codec<java.util.List<OverlayEntry>> listCodecForPackType(PackType packType) {
/* 31 */     int lastPreMinorVersion = PackFormat.lastPreMinorVersion(packType);
/* 32 */     return IntermediateEntry.CODEC.listOf().flatXmap(list -> PackFormat.validateHolderList(list, lastPreMinorVersion, ()), list -> com.mojang.serialization.DataResult.success(list.stream().map(()).toList()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isApplicable(PackFormat formatToTest) {
/* 39 */     return this.format.isValueInRange((Comparable)formatToTest);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/packs/OverlayMetadataSection$OverlayEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */