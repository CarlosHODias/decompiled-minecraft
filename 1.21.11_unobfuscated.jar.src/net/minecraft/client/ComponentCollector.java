/*    */ package net.minecraft.client;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.List;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ 
/*    */ 
/*    */ public class ComponentCollector
/*    */ {
/* 10 */   private final List<FormattedText> parts = Lists.newArrayList();
/*    */   
/*    */   public void append(FormattedText component) {
/* 13 */     this.parts.add(component);
/*    */   }
/*    */   
/*    */   public FormattedText getResult() {
/* 17 */     if (this.parts.isEmpty()) {
/* 18 */       return null;
/*    */     }
/* 20 */     if (this.parts.size() == 1) {
/* 21 */       return this.parts.get(0);
/*    */     }
/* 23 */     return FormattedText.composite(this.parts);
/*    */   }
/*    */   
/*    */   public FormattedText getResultOrEmpty() {
/* 27 */     FormattedText result = getResult();
/* 28 */     return (result != null) ? result : FormattedText.EMPTY;
/*    */   }
/*    */   
/*    */   public void reset() {
/* 32 */     this.parts.clear();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/ComponentCollector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */