/*    */ package net.minecraft.client.renderer.item.properties.numeric;
/*    */ 
/*    */ import net.minecraft.client.multiplayer.ClientLevel;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.ItemOwner;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.level.Level;
/*    */ 
/*    */ public abstract class NeedleDirectionHelper
/*    */ {
/*    */   protected NeedleDirectionHelper(boolean wobble) {
/* 13 */     this.wobble = wobble;
/*    */   } private final boolean wobble;
/*    */   public float get(ItemStack itemStack, ClientLevel clientLevel, ItemOwner owner, int seed) {
/*    */     Entity entity;
/* 17 */     if (owner == null) {
/* 18 */       entity = itemStack.getEntityRepresentation();
/*    */     }
/*    */     
/* 21 */     if (entity == null) {
/* 22 */       return 0.0F;
/*    */     }
/*    */     
/* 25 */     if (clientLevel == null) { Level level = entity.level(); if (level instanceof ClientLevel) { ClientLevel clientLevel1 = (ClientLevel)level;
/* 26 */         clientLevel = clientLevel1; }
/*    */        }
/*    */     
/* 29 */     if (clientLevel == null) {
/* 30 */       return 0.0F;
/*    */     }
/*    */     
/* 33 */     return calculate(itemStack, clientLevel, seed, (ItemOwner)entity);
/*    */   }
/*    */   
/*    */   protected abstract float calculate(ItemStack paramItemStack, ClientLevel paramClientLevel, int paramInt, ItemOwner paramItemOwner);
/*    */   
/*    */   protected boolean wobble() {
/* 39 */     return this.wobble;
/*    */   }
/*    */   
/*    */   protected Wobbler newWobbler(float factor) {
/* 43 */     return this.wobble ? standardWobbler(factor) : nonWobbler();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static Wobbler standardWobbler(final float factor) {
/* 55 */     return new Wobbler()
/*    */       {
/*    */         private float rotation;
/*    */         private float deltaRotation;
/*    */         private long lastUpdateTick;
/*    */         
/*    */         public float rotation() {
/* 62 */           return this.rotation;
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean shouldUpdate(long tick) {
/* 67 */           return (this.lastUpdateTick != tick);
/*    */         }
/*    */ 
/*    */         
/*    */         public void update(long tick, float targetRotation) {
/* 72 */           this.lastUpdateTick = tick;
/* 73 */           float tempDeltaRotation = Mth.positiveModulo(targetRotation - this.rotation + 0.5F, 1.0F) - 0.5F;
/*    */           
/* 75 */           this.deltaRotation += tempDeltaRotation * 0.1F;
/* 76 */           this.deltaRotation *= factor;
/* 77 */           this.rotation = Mth.positiveModulo(this.rotation + this.deltaRotation, 1.0F);
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static Wobbler nonWobbler() {
/* 83 */     return new Wobbler()
/*    */       {
/*    */         private float targetValue;
/*    */         
/*    */         public float rotation() {
/* 88 */           return this.targetValue;
/*    */         }
/*    */ 
/*    */         
/*    */         public boolean shouldUpdate(long tick) {
/* 93 */           return true;
/*    */         }
/*    */ 
/*    */         
/*    */         public void update(long tick, float targetRotation) {
/* 98 */           this.targetValue = targetRotation;
/*    */         }
/*    */       };
/*    */   }
/*    */   
/*    */   public static interface Wobbler {
/*    */     float rotation();
/*    */     
/*    */     boolean shouldUpdate(long param1Long);
/*    */     
/*    */     void update(long param1Long, float param1Float);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/numeric/NeedleDirectionHelper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */