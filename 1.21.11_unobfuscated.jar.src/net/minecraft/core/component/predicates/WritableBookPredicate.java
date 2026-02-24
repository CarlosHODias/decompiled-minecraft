/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.advancements.criterion.CollectionPredicate;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.component.WritableBookContent;
/*    */ 
/*    */ public final class WritableBookPredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<WritableBookContent> {
/*    */   private final Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages;
/*    */   public static final Codec<WritableBookPredicate> CODEC;
/*    */   
/* 15 */   public WritableBookPredicate(Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages) { this.pages = pages; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 15 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate; } public Optional<CollectionPredicate<Filterable<String>, PagePredicate>> pages() { return this.pages; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WritableBookPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #15	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate;
/*    */     //   0	8	1	o	Ljava/lang/Object; } public static final class PagePredicate extends Record implements java.util.function.Predicate<Filterable<String>> {
/* 16 */     private final String contents; public PagePredicate(String contents) { this.contents = contents; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #16	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/WritableBookPredicate$PagePredicate;
/* 16 */       //   0	8	1	o	Ljava/lang/Object; } public String contents() { return this.contents; }
/* 17 */      public static final Codec<PagePredicate> CODEC = Codec.STRING.xmap(PagePredicate::new, PagePredicate::contents);
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean test(Filterable<String> value) {
/* 22 */       return ((String)value.raw()).equals(this.contents);
/*    */     } }
/*    */   
/*    */   static {
/* 26 */     CODEC = RecordCodecBuilder.create(i -> i.group((com.mojang.datafixers.kinds.App)CollectionPredicate.codec(PagePredicate.CODEC).optionalFieldOf("pages").forGetter(WritableBookPredicate::pages)).apply((com.mojang.datafixers.kinds.Applicative)i, WritableBookPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<WritableBookContent> componentType() {
/* 32 */     return net.minecraft.core.component.DataComponents.WRITABLE_BOOK_CONTENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(WritableBookContent value) {
/* 37 */     if (this.pages.isPresent() && !((CollectionPredicate)this.pages.get()).test(value.pages())) {
/* 38 */       return false;
/*    */     }
/*    */     
/* 41 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/WritableBookPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */