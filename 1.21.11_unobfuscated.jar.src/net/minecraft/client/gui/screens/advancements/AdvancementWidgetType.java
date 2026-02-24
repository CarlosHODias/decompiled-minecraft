/*    */ package net.minecraft.client.gui.screens.advancements;
/*    */ 
/*    */ import net.minecraft.advancements.AdvancementType;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public enum AdvancementWidgetType {
/*  7 */   OBTAINED(
/*  8 */     Identifier.withDefaultNamespace("advancements/box_obtained"), 
/*  9 */     Identifier.withDefaultNamespace("advancements/task_frame_obtained"), 
/* 10 */     Identifier.withDefaultNamespace("advancements/challenge_frame_obtained"), 
/* 11 */     Identifier.withDefaultNamespace("advancements/goal_frame_obtained")),
/*    */   
/* 13 */   UNOBTAINED(
/* 14 */     Identifier.withDefaultNamespace("advancements/box_unobtained"), 
/* 15 */     Identifier.withDefaultNamespace("advancements/task_frame_unobtained"), 
/* 16 */     Identifier.withDefaultNamespace("advancements/challenge_frame_unobtained"), 
/* 17 */     Identifier.withDefaultNamespace("advancements/goal_frame_unobtained"));
/*    */   
/*    */   private final Identifier boxSprite;
/*    */   
/*    */   private final Identifier taskFrameSprite;
/*    */   
/*    */   private final Identifier challengeFrameSprite;
/*    */   private final Identifier goalFrameSprite;
/*    */   
/*    */   AdvancementWidgetType(Identifier boxSprite, Identifier taskFrameSprite, Identifier challengeFrameSprite, Identifier goalFrameSprite) {
/* 27 */     this.boxSprite = boxSprite;
/* 28 */     this.taskFrameSprite = taskFrameSprite;
/* 29 */     this.challengeFrameSprite = challengeFrameSprite;
/* 30 */     this.goalFrameSprite = goalFrameSprite;
/*    */   }
/*    */   
/*    */   public Identifier boxSprite() {
/* 34 */     return this.boxSprite;
/*    */   }
/*    */   
/*    */   public Identifier frameSprite(AdvancementType type) {
/* 38 */     switch (type) { default: throw new MatchException(null, null);case TASK: case CHALLENGE: case GOAL: break; }  return 
/*    */ 
/*    */       
/* 41 */       this.goalFrameSprite;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/advancements/AdvancementWidgetType.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */