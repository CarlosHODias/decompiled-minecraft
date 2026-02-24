/*   */ package net.minecraft.client.particle;public final class ParticleRenderType extends Record { private final String name;
/*   */   
/* 3 */   public ParticleRenderType(String name) { this.name = name; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/client/particle/ParticleRenderType;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 3 */     //   0	7	0	this	Lnet/minecraft/client/particle/ParticleRenderType; } public String name() { return this.name; }
/*   */   public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/particle/ParticleRenderType;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/client/particle/ParticleRenderType; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/client/particle/ParticleRenderType;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #3	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/client/particle/ParticleRenderType;
/* 4 */     //   0	8	1	o	Ljava/lang/Object; } public static final ParticleRenderType SINGLE_QUADS = new ParticleRenderType("SINGLE_QUADS");
/* 5 */   public static final ParticleRenderType ITEM_PICKUP = new ParticleRenderType("ITEM_PICKUP");
/* 6 */   public static final ParticleRenderType ELDER_GUARDIANS = new ParticleRenderType("ELDER_GUARDIANS");
/* 7 */   public static final ParticleRenderType NO_RENDER = new ParticleRenderType("NO_RENDER"); }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/particle/ParticleRenderType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */