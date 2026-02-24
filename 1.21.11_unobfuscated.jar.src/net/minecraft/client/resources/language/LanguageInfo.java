/*    */ package net.minecraft.client.resources.language;
/*    */ public final class LanguageInfo extends Record {
/*    */   private final String region;
/*    */   private final String name;
/*    */   private final boolean bidirectional;
/*    */   public static final com.mojang.serialization.Codec<LanguageInfo> CODEC;
/*    */   
/*  8 */   public LanguageInfo(String region, String name, boolean bidirectional) { this.region = region; this.name = name; this.bidirectional = bidirectional; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/resources/language/LanguageInfo;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  8 */     //   0	7	0	this	Lnet/minecraft/client/resources/language/LanguageInfo; } public String region() { return this.region; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/resources/language/LanguageInfo;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/client/resources/language/LanguageInfo; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/resources/language/LanguageInfo;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #8	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/client/resources/language/LanguageInfo;
/*  8 */     //   0	8	1	o	Ljava/lang/Object; } public String name() { return this.name; } public boolean bidirectional() { return this.bidirectional; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static {
/* 17 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_EMPTY_STRING.fieldOf("region").forGetter(LanguageInfo::region), (com.mojang.datafixers.kinds.App)net.minecraft.util.ExtraCodecs.NON_EMPTY_STRING.fieldOf("name").forGetter(LanguageInfo::name), (com.mojang.datafixers.kinds.App)com.mojang.serialization.Codec.BOOL.optionalFieldOf("bidirectional", false).forGetter(LanguageInfo::bidirectional)).apply((com.mojang.datafixers.kinds.Applicative)i, LanguageInfo::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.network.chat.Component toComponent() {
/* 24 */     return (net.minecraft.network.chat.Component)net.minecraft.network.chat.Component.literal(this.name + " (" + this.name + ")");
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/language/LanguageInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */