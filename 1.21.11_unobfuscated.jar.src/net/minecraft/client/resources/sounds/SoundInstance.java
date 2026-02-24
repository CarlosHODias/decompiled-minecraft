/*    */ package net.minecraft.client.resources.sounds;public interface SoundInstance { net.minecraft.resources.Identifier getIdentifier(); net.minecraft.client.sounds.WeighedSoundEvents resolve(net.minecraft.client.sounds.SoundManager paramSoundManager); Sound getSound();
/*    */   net.minecraft.sounds.SoundSource getSource();
/*    */   boolean isLooping();
/*    */   boolean isRelative();
/*    */   int getDelay();
/*    */   float getVolume();
/*    */   float getPitch();
/*    */   double getX();
/*    */   double getY();
/*    */   double getZ();
/*    */   Attenuation getAttenuation();
/* 12 */   public enum Attenuation { NONE,
/* 13 */     LINEAR; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   default boolean canStartSilent() {
/* 46 */     return false;
/*    */   }
/*    */   
/*    */   default boolean canPlaySound() {
/* 50 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   static net.minecraft.util.RandomSource createUnseededRandom() {
/* 58 */     return net.minecraft.util.RandomSource.create();
/*    */   } }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/SoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */