/*   */ package net.minecraft.world.entity.animal.golem;public final class CopperGolemOxidationLevel extends Record { private final net.minecraft.sounds.SoundEvent spinHeadSound; private final net.minecraft.sounds.SoundEvent hurtSound; private final net.minecraft.sounds.SoundEvent deathSound;
/*   */   private final net.minecraft.sounds.SoundEvent stepSound;
/*   */   private final net.minecraft.resources.Identifier texture;
/*   */   private final net.minecraft.resources.Identifier eyeTexture;
/*   */   
/* 6 */   public CopperGolemOxidationLevel(net.minecraft.sounds.SoundEvent spinHeadSound, net.minecraft.sounds.SoundEvent hurtSound, net.minecraft.sounds.SoundEvent deathSound, net.minecraft.sounds.SoundEvent stepSound, net.minecraft.resources.Identifier texture, net.minecraft.resources.Identifier eyeTexture) { this.spinHeadSound = spinHeadSound; this.hurtSound = hurtSound; this.deathSound = deathSound; this.stepSound = stepSound; this.texture = texture; this.eyeTexture = eyeTexture; } public final String toString() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> toString : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;)Ljava/lang/String;
/*   */     //   6: areturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/* 6 */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel; } public net.minecraft.sounds.SoundEvent spinHeadSound() { return this.spinHeadSound; } public final int hashCode() { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;)I
/*   */     //   6: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	7	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel; } public final boolean equals(Object o) { // Byte code:
/*   */     //   0: aload_0
/*   */     //   1: aload_1
/*   */     //   2: <illegal opcode> equals : (Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;Ljava/lang/Object;)Z
/*   */     //   7: ireturn
/*   */     // Line number table:
/*   */     //   Java source line number -> byte code offset
/*   */     //   #6	-> 0
/*   */     // Local variable table:
/*   */     //   start	length	slot	name	descriptor
/*   */     //   0	8	0	this	Lnet/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel;
/* 6 */     //   0	8	1	o	Ljava/lang/Object; } public net.minecraft.sounds.SoundEvent hurtSound() { return this.hurtSound; } public net.minecraft.sounds.SoundEvent deathSound() { return this.deathSound; } public net.minecraft.sounds.SoundEvent stepSound() { return this.stepSound; } public net.minecraft.resources.Identifier texture() { return this.texture; } public net.minecraft.resources.Identifier eyeTexture() { return this.eyeTexture; }
/*   */    }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/animal/golem/CopperGolemOxidationLevel.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */