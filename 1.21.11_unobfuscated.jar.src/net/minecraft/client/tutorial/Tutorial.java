/*     */ package net.minecraft.client.tutorial;
/*     */ 
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.client.player.ClientInput;
/*     */ import net.minecraft.core.BlockPos;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.inventory.ClickAction;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.GameType;
/*     */ import net.minecraft.world.level.block.state.BlockState;
/*     */ import net.minecraft.world.phys.HitResult;
/*     */ 
/*     */ public class Tutorial
/*     */ {
/*     */   private final Minecraft minecraft;
/*     */   private TutorialStepInstance instance;
/*     */   
/*     */   public Tutorial(Minecraft minecraft, Options options) {
/*  22 */     this.minecraft = minecraft;
/*     */   }
/*     */   
/*     */   public void onInput(ClientInput input) {
/*  26 */     if (this.instance != null) {
/*  27 */       this.instance.onInput(input);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onMouse(double xd, double yd) {
/*  32 */     if (this.instance != null) {
/*  33 */       this.instance.onMouse(xd, yd);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onLookAt(ClientLevel level, HitResult hit) {
/*  38 */     if (this.instance != null && hit != null && level != null) {
/*  39 */       this.instance.onLookAt(level, hit);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onDestroyBlock(ClientLevel level, BlockPos pos, BlockState state, float percent) {
/*  44 */     if (this.instance != null) {
/*  45 */       this.instance.onDestroyBlock(level, pos, state, percent);
/*     */     }
/*     */   }
/*     */   
/*     */   public void onOpenInventory() {
/*  50 */     if (this.instance != null) {
/*  51 */       this.instance.onOpenInventory();
/*     */     }
/*     */   }
/*     */   
/*     */   public void onGetItem(ItemStack itemStack) {
/*  56 */     if (this.instance != null) {
/*  57 */       this.instance.onGetItem(itemStack);
/*     */     }
/*     */   }
/*     */   
/*     */   public void stop() {
/*  62 */     if (this.instance == null) {
/*     */       return;
/*     */     }
/*  65 */     this.instance.clear();
/*  66 */     this.instance = null;
/*     */   }
/*     */   
/*     */   public void start() {
/*  70 */     if (this.instance != null) {
/*  71 */       stop();
/*     */     }
/*  73 */     this.instance = this.minecraft.options.tutorialStep.create(this);
/*     */   }
/*     */   
/*     */   public void tick() {
/*  77 */     if (this.instance != null) {
/*  78 */       if (this.minecraft.level != null) {
/*  79 */         this.instance.tick();
/*     */       } else {
/*  81 */         stop();
/*     */       } 
/*  83 */     } else if (this.minecraft.level != null) {
/*  84 */       start();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void setStep(TutorialSteps step) {
/*  89 */     this.minecraft.options.tutorialStep = step;
/*  90 */     this.minecraft.options.save();
/*  91 */     if (this.instance != null) {
/*  92 */       this.instance.clear();
/*  93 */       this.instance = step.create(this);
/*     */     } 
/*     */   }
/*     */   
/*     */   public Minecraft getMinecraft() {
/*  98 */     return this.minecraft;
/*     */   }
/*     */   
/*     */   public boolean isSurvival() {
/* 102 */     if (this.minecraft.gameMode == null) {
/* 103 */       return false;
/*     */     }
/* 105 */     return (this.minecraft.gameMode.getPlayerMode() == GameType.SURVIVAL);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Component key(String name) {
/* 110 */     return (Component)Component.keybind("key." + name).withStyle(ChatFormatting.BOLD);
/*     */   }
/*     */   
/*     */   public void onInventoryAction(ItemStack itemCarried, ItemStack itemInSlot, ClickAction clickAction) {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/tutorial/Tutorial.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */