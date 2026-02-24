/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.world.entity.animal.bee.Bee;
/*    */ 
/*    */ public class BeeFlyingSoundInstance extends BeeSoundInstance {
/*    */   public BeeFlyingSoundInstance(Bee bee) {
/*  9 */     super(bee, SoundEvents.BEE_LOOP, SoundSource.NEUTRAL);
/*    */   }
/*    */ 
/*    */   
/*    */   protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
/* 14 */     return new BeeAggressiveSoundInstance(this.bee);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean shouldSwitchSounds() {
/* 19 */     return this.bee.isAngry();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/BeeFlyingSoundInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */