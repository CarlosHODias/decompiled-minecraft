/*    */ package net.minecraft.client.renderer.texture;
/*    */ 
/*    */ import java.util.Collection;
/*    */ import java.util.Locale;
/*    */ 
/*    */ public class StitcherException extends RuntimeException {
/*    */   private final Collection<Stitcher.Entry> allSprites;
/*    */   
/*    */   public StitcherException(Stitcher.Entry sprite, Collection<Stitcher.Entry> allSprites) {
/* 10 */     super(String.format(Locale.ROOT, "Unable to fit: %s - size: %dx%d - Maybe try a lower resolution resourcepack?", new Object[] {
/*    */ 
/*    */             
/* 13 */             sprite.name(), 
/* 14 */             sprite.width(), 
/* 15 */             sprite.height()
/*    */           }));
/*    */     
/* 18 */     this.allSprites = allSprites;
/*    */   }
/*    */   
/*    */   public Collection<Stitcher.Entry> getAllSprites() {
/* 22 */     return this.allSprites;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/texture/StitcherException.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */