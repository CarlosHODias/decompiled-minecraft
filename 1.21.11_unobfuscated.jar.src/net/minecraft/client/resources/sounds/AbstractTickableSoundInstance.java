/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.util.RandomSource;
/*    */ 
/*    */ public abstract class AbstractTickableSoundInstance extends AbstractSoundInstance implements TickableSoundInstance {
/*    */   private boolean stopped;
/*    */   
/*    */   protected AbstractTickableSoundInstance(SoundEvent event, SoundSource source, RandomSource random) {
/* 11 */     super(event, source, random);
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStopped() {
/* 16 */     return this.stopped;
/*    */   }
/*    */   
/*    */   protected final void stop() {
/* 20 */     this.stopped = true;
/*    */     
/* 22 */     this.looping = false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/AbstractTickableSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */