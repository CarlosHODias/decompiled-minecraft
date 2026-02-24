/*    */ package net.minecraft.client.renderer.item;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ public class TrackingItemStackRenderState
/*    */   extends ItemStackRenderState
/*    */ {
/* 10 */   private final List<Object> modelIdentityElements = new ArrayList();
/*    */ 
/*    */   
/*    */   public void appendModelIdentityElement(Object element) {
/* 14 */     this.modelIdentityElements.add(element);
/*    */   }
/*    */   
/*    */   public Object getModelIdentity() {
/* 18 */     return this.modelIdentityElements;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/TrackingItemStackRenderState.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */