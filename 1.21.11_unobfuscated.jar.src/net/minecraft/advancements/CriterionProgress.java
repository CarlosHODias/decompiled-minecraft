/*    */ package net.minecraft.advancements;
/*    */ 
/*    */ import java.time.Instant;
/*    */ import net.minecraft.network.FriendlyByteBuf;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CriterionProgress
/*    */ {
/*    */   private Instant obtained;
/*    */   
/*    */   public CriterionProgress() {}
/*    */   
/*    */   public CriterionProgress(Instant obtained) {
/* 15 */     this.obtained = obtained;
/*    */   }
/*    */   
/*    */   public boolean isDone() {
/* 19 */     return (this.obtained != null);
/*    */   }
/*    */   
/*    */   public void grant() {
/* 23 */     this.obtained = Instant.now();
/*    */   }
/*    */   
/*    */   public void revoke() {
/* 27 */     this.obtained = null;
/*    */   }
/*    */   
/*    */   public Instant getObtained() {
/* 31 */     return this.obtained;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String toString() {
/* 37 */     return "CriterionProgress{obtained=" + String.valueOf((this.obtained == null) ? "false" : this.obtained) + "}";
/*    */   }
/*    */ 
/*    */   
/*    */   public void serializeToNetwork(FriendlyByteBuf output) {
/* 42 */     output.writeNullable(this.obtained, FriendlyByteBuf::writeInstant);
/*    */   }
/*    */   
/*    */   public static CriterionProgress fromNetwork(FriendlyByteBuf input) {
/* 46 */     CriterionProgress result = new CriterionProgress();
/* 47 */     result.obtained = (Instant)input.readNullable(FriendlyByteBuf::readInstant);
/* 48 */     return result;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/CriterionProgress.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */