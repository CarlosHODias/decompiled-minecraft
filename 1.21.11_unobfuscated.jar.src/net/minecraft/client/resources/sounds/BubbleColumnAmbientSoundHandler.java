/*    */ package net.minecraft.client.resources.sounds;
/*    */ 
/*    */ import net.minecraft.client.player.LocalPlayer;
/*    */ import net.minecraft.sounds.SoundEvents;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.block.Blocks;
/*    */ import net.minecraft.world.level.block.BubbleColumnBlock;
/*    */ import net.minecraft.world.level.block.state.BlockState;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ public class BubbleColumnAmbientSoundHandler implements AmbientSoundHandler {
/*    */   private final LocalPlayer player;
/*    */   private boolean wasInBubbleColumn;
/*    */   private boolean firstTick = true;
/*    */   
/*    */   public BubbleColumnAmbientSoundHandler(LocalPlayer player) {
/* 17 */     this.player = player;
/*    */   }
/*    */ 
/*    */   
/*    */   public void tick() {
/* 22 */     Level level = this.player.level();
/* 23 */     BlockState state = level.getBlockStatesIfLoaded(this.player.getBoundingBox().inflate(0.0D, -0.4000000059604645D, 0.0D).deflate(1.0E-6D)).filter(s -> s.is(Blocks.BUBBLE_COLUMN)).findFirst().orElse(null);
/* 24 */     if (state != null) {
/* 25 */       if (!this.wasInBubbleColumn && !this.firstTick && 
/* 26 */         state.is(Blocks.BUBBLE_COLUMN) && !this.player.isSpectator()) {
/* 27 */         boolean dragDown = (Boolean)state.getValue((Property)BubbleColumnBlock.DRAG_DOWN);
/* 28 */         if (dragDown) {
/* 29 */           this.player.playSound(SoundEvents.BUBBLE_COLUMN_WHIRLPOOL_INSIDE, 1.0F, 1.0F);
/*    */         } else {
/* 31 */           this.player.playSound(SoundEvents.BUBBLE_COLUMN_UPWARDS_INSIDE, 1.0F, 1.0F);
/*    */         } 
/*    */       } 
/*    */       
/* 35 */       this.wasInBubbleColumn = true;
/*    */     } else {
/* 37 */       this.wasInBubbleColumn = false;
/*    */     } 
/* 39 */     this.firstTick = false;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/sounds/BubbleColumnAmbientSoundHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */