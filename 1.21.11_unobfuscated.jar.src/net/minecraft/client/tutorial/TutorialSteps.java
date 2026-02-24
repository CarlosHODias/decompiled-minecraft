/*    */ package net.minecraft.client.tutorial;
/*    */ 
/*    */ import java.util.function.Function;
/*    */ 
/*    */ public enum TutorialSteps {
/*  6 */   MOVEMENT("movement", MovementTutorialStepInstance::new),
/*  7 */   FIND_TREE("find_tree", FindTreeTutorialStepInstance::new),
/*  8 */   PUNCH_TREE("punch_tree", PunchTreeTutorialStepInstance::new),
/*  9 */   OPEN_INVENTORY("open_inventory", OpenInventoryTutorialStep::new),
/* 10 */   CRAFT_PLANKS("craft_planks", CraftPlanksTutorialStep::new),
/* 11 */   NONE("none", CompletedTutorialStepInstance::new);
/*    */   
/*    */   private final String name;
/*    */   
/*    */   private final Function<Tutorial, ? extends TutorialStepInstance> constructor;
/*    */   
/*    */   <T extends TutorialStepInstance> TutorialSteps(String name, Function<Tutorial, T> constructor) {
/* 18 */     this.name = name;
/* 19 */     this.constructor = constructor;
/*    */   }
/*    */   
/*    */   public TutorialStepInstance create(Tutorial tutorial) {
/* 23 */     return this.constructor.apply(tutorial);
/*    */   }
/*    */   
/*    */   public String getName() {
/* 27 */     return this.name;
/*    */   }
/*    */   
/*    */   public static TutorialSteps getByName(String name) {
/* 31 */     for (TutorialSteps step : values()) {
/* 32 */       if (step.name.equals(name)) {
/* 33 */         return step;
/*    */       }
/*    */     } 
/* 36 */     return NONE;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/tutorial/TutorialSteps.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */