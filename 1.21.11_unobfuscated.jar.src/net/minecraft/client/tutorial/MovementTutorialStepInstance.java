/*     */ package net.minecraft.client.tutorial;
/*     */ 
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.toasts.Toast;
/*     */ import net.minecraft.client.gui.components.toasts.TutorialToast;
/*     */ import net.minecraft.client.player.ClientInput;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MovementTutorialStepInstance
/*     */   implements TutorialStepInstance
/*     */ {
/*     */   private static final int MINIMUM_TIME_MOVED = 40;
/*     */   private static final int MINIMUM_TIME_LOOKED = 40;
/*     */   private static final int MOVE_HINT_DELAY = 100;
/*     */   private static final int LOOK_HINT_DELAY = 20;
/*     */   private static final int INCOMPLETE = -1;
/*  19 */   private static final Component MOVE_TITLE = (Component)Component.translatable("tutorial.move.title", new Object[] { Tutorial.key("forward"), Tutorial.key("left"), Tutorial.key("back"), Tutorial.key("right") });
/*  20 */   private static final Component MOVE_DESCRIPTION = (Component)Component.translatable("tutorial.move.description", new Object[] { Tutorial.key("jump") });
/*     */   
/*  22 */   private static final Component LOOK_TITLE = (Component)Component.translatable("tutorial.look.title");
/*  23 */   private static final Component LOOK_DESCRIPTION = (Component)Component.translatable("tutorial.look.description");
/*     */   
/*     */   private final Tutorial tutorial;
/*     */   private TutorialToast moveToast;
/*     */   private TutorialToast lookToast;
/*     */   private int timeWaiting;
/*     */   private int timeMoved;
/*     */   private int timeLooked;
/*     */   private boolean moved;
/*     */   private boolean turned;
/*  33 */   private int moveCompleted = -1;
/*  34 */   private int lookCompleted = -1;
/*     */   
/*     */   public MovementTutorialStepInstance(Tutorial tutorial) {
/*  37 */     this.tutorial = tutorial;
/*     */   }
/*     */ 
/*     */   
/*     */   public void tick() {
/*  42 */     this.timeWaiting++;
/*     */     
/*  44 */     if (this.moved) {
/*  45 */       this.timeMoved++;
/*  46 */       this.moved = false;
/*     */     } 
/*     */     
/*  49 */     if (this.turned) {
/*  50 */       this.timeLooked++;
/*  51 */       this.turned = false;
/*     */     } 
/*     */     
/*  54 */     if (this.moveCompleted == -1 && this.timeMoved > 40) {
/*  55 */       if (this.moveToast != null) {
/*  56 */         this.moveToast.hide();
/*  57 */         this.moveToast = null;
/*     */       } 
/*  59 */       this.moveCompleted = this.timeWaiting;
/*     */     } 
/*     */     
/*  62 */     if (this.lookCompleted == -1 && this.timeLooked > 40) {
/*  63 */       if (this.lookToast != null) {
/*  64 */         this.lookToast.hide();
/*  65 */         this.lookToast = null;
/*     */       } 
/*  67 */       this.lookCompleted = this.timeWaiting;
/*     */     } 
/*     */     
/*  70 */     if (this.moveCompleted != -1 && this.lookCompleted != -1) {
/*  71 */       if (this.tutorial.isSurvival()) {
/*  72 */         this.tutorial.setStep(TutorialSteps.FIND_TREE);
/*     */       } else {
/*  74 */         this.tutorial.setStep(TutorialSteps.NONE);
/*     */       } 
/*     */     }
/*     */     
/*  78 */     if (this.moveToast != null) {
/*  79 */       this.moveToast.updateProgress(this.timeMoved / 40.0F);
/*     */     }
/*     */     
/*  82 */     if (this.lookToast != null) {
/*  83 */       this.lookToast.updateProgress(this.timeLooked / 40.0F);
/*     */     }
/*     */     
/*  86 */     if (this.timeWaiting >= 100) {
/*  87 */       Minecraft minecraft = this.tutorial.getMinecraft();
/*  88 */       if (this.moveCompleted == -1 && this.moveToast == null) {
/*  89 */         this.moveToast = new TutorialToast(minecraft.font, TutorialToast.Icons.MOVEMENT_KEYS, MOVE_TITLE, MOVE_DESCRIPTION, true);
/*  90 */         minecraft.getToastManager().addToast((Toast)this.moveToast);
/*  91 */       } else if (this.moveCompleted != -1 && this.timeWaiting - this.moveCompleted >= 20 && this.lookCompleted == -1 && this.lookToast == null) {
/*  92 */         this.lookToast = new TutorialToast(minecraft.font, TutorialToast.Icons.MOUSE, LOOK_TITLE, LOOK_DESCRIPTION, true);
/*  93 */         minecraft.getToastManager().addToast((Toast)this.lookToast);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void clear() {
/* 100 */     if (this.moveToast != null) {
/* 101 */       this.moveToast.hide();
/* 102 */       this.moveToast = null;
/*     */     } 
/* 104 */     if (this.lookToast != null) {
/* 105 */       this.lookToast.hide();
/* 106 */       this.lookToast = null;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void onInput(ClientInput input) {
/* 112 */     if (input.keyPresses.forward() || input.keyPresses.backward() || input.keyPresses.left() || input.keyPresses.right() || input.keyPresses.jump()) {
/* 113 */       this.moved = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void onMouse(double xd, double yd) {
/* 119 */     if (Math.abs(xd) > 0.01D || Math.abs(yd) > 0.01D)
/* 120 */       this.turned = true; 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/tutorial/MovementTutorialStepInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */