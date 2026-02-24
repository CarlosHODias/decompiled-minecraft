/*    */ package net.minecraft.tags;
/*    */ 
/*    */ import java.util.ArrayList;
/*    */ import java.util.List;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class TagBuilder
/*    */ {
/*  9 */   private final List<TagEntry> entries = new ArrayList<>();
/*    */   
/*    */   public static TagBuilder create() {
/* 12 */     return new TagBuilder();
/*    */   }
/*    */   
/*    */   public List<TagEntry> build() {
/* 16 */     return List.copyOf(this.entries);
/*    */   }
/*    */   
/*    */   public TagBuilder add(TagEntry entry) {
/* 20 */     this.entries.add(entry);
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   public TagBuilder addElement(Identifier id) {
/* 25 */     return add(TagEntry.element(id));
/*    */   }
/*    */   
/*    */   public TagBuilder addOptionalElement(Identifier id) {
/* 29 */     return add(TagEntry.optionalElement(id));
/*    */   }
/*    */   
/*    */   public TagBuilder addTag(Identifier id) {
/* 33 */     return add(TagEntry.tag(id));
/*    */   }
/*    */   
/*    */   public TagBuilder addOptionalTag(Identifier id) {
/* 37 */     return add(TagEntry.optionalTag(id));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/tags/TagBuilder.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */