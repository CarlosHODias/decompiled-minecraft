/*    */ package net.minecraft.core.component.predicates;
/*    */ 
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.serialization.Codec;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ 
/*    */ public final class WrittenBookPredicate extends Record implements net.minecraft.advancements.criterion.SingleComponentItemPredicate<WrittenBookContent> {
/*    */   private final Optional<net.minecraft.advancements.criterion.CollectionPredicate<Filterable<Component>, PagePredicate>> pages;
/*    */   private final Optional<String> author;
/*    */   private final Optional<String> title;
/*    */   private final net.minecraft.advancements.criterion.MinMaxBounds.Ints generation;
/*    */   private final Optional<Boolean> resolved;
/*    */   public static final Codec<WrittenBookPredicate> CODEC;
/*    */   
/* 18 */   public WrittenBookPredicate(Optional<net.minecraft.advancements.criterion.CollectionPredicate<Filterable<Component>, PagePredicate>> pages, Optional<String> author, Optional<String> title, net.minecraft.advancements.criterion.MinMaxBounds.Ints generation, Optional<Boolean> resolved) { this.pages = pages; this.author = author; this.title = title; this.generation = generation; this.resolved = resolved; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/* 18 */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate; } public Optional<net.minecraft.advancements.criterion.CollectionPredicate<Filterable<Component>, PagePredicate>> pages() { return this.pages; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #18	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate;
/* 18 */     //   0	8	1	o	Ljava/lang/Object; } public Optional<String> author() { return this.author; } public Optional<String> title() { return this.title; } public net.minecraft.advancements.criterion.MinMaxBounds.Ints generation() { return this.generation; } public Optional<Boolean> resolved() { return this.resolved; }
/*    */ 
/*    */   
/*    */   public static final class PagePredicate extends Record implements java.util.function.Predicate<Filterable<Component>>
/*    */   {
/*    */     private final Component contents;
/*    */     
/* 25 */     public PagePredicate(Component contents) { this.contents = contents; } public final String toString() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> toString : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;)Ljava/lang/String;
/*    */       //   6: areturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate; } public final int hashCode() { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;)I
/*    */       //   6: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	7	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate; } public final boolean equals(Object o) { // Byte code:
/*    */       //   0: aload_0
/*    */       //   1: aload_1
/*    */       //   2: <illegal opcode> equals : (Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;Ljava/lang/Object;)Z
/*    */       //   7: ireturn
/*    */       // Line number table:
/*    */       //   Java source line number -> byte code offset
/*    */       //   #25	-> 0
/*    */       // Local variable table:
/*    */       //   start	length	slot	name	descriptor
/*    */       //   0	8	0	this	Lnet/minecraft/core/component/predicates/WrittenBookPredicate$PagePredicate;
/* 25 */       //   0	8	1	o	Ljava/lang/Object; } public Component contents() { return this.contents; }
/* 26 */      public static final Codec<PagePredicate> CODEC = net.minecraft.network.chat.ComponentSerialization.CODEC.xmap(PagePredicate::new, PagePredicate::contents);
/*    */ 
/*    */ 
/*    */     
/*    */     public boolean test(Filterable<Component> value) {
/* 31 */       return ((Component)value.raw()).equals(this.contents);
/*    */     } }
/*    */   
/*    */   static {
/* 35 */     CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.create(i -> i.group((App)net.minecraft.advancements.criterion.CollectionPredicate.codec(PagePredicate.CODEC).optionalFieldOf("pages").forGetter(WrittenBookPredicate::pages), (App)Codec.STRING.optionalFieldOf("author").forGetter(WrittenBookPredicate::author), (App)Codec.STRING.optionalFieldOf("title").forGetter(WrittenBookPredicate::title), (App)net.minecraft.advancements.criterion.MinMaxBounds.Ints.CODEC.optionalFieldOf("generation", net.minecraft.advancements.criterion.MinMaxBounds.Ints.ANY).forGetter(WrittenBookPredicate::generation), (App)Codec.BOOL.optionalFieldOf("resolved").forGetter(WrittenBookPredicate::resolved)).apply((com.mojang.datafixers.kinds.Applicative)i, WrittenBookPredicate::new));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public net.minecraft.core.component.DataComponentType<WrittenBookContent> componentType() {
/* 45 */     return net.minecraft.core.component.DataComponents.WRITTEN_BOOK_CONTENT;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean matches(WrittenBookContent value) {
/* 50 */     if (this.author.isPresent() && !((String)this.author.get()).equals(value.author())) {
/* 51 */       return false;
/*    */     }
/*    */     
/* 54 */     if (this.title.isPresent() && !((String)this.title.get()).equals(value.title().raw())) {
/* 55 */       return false;
/*    */     }
/*    */     
/* 58 */     if (!this.generation.matches(value.generation())) {
/* 59 */       return false;
/*    */     }
/*    */     
/* 62 */     if (this.resolved.isPresent() && (Boolean)this.resolved.get() != value.resolved()) {
/* 63 */       return false;
/*    */     }
/*    */     
/* 66 */     if (this.pages.isPresent() && !((net.minecraft.advancements.criterion.CollectionPredicate)this.pages.get()).test(value.pages())) {
/* 67 */       return false;
/*    */     }
/*    */     
/* 70 */     return true;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/core/component/predicates/WrittenBookPredicate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */