/*    */ package net.minecraft.commands.arguments.selector;
/*    */ 
/*    */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*    */ 
/*    */ public final class SelectorPattern extends Record {
/*    */   private final String pattern;
/*    */   
/*  8 */   public SelectorPattern(String pattern, EntitySelector resolved) { this.pattern = pattern; this.resolved = resolved; } private final EntitySelector resolved; public String pattern() { return this.pattern; } public EntitySelector resolved() { return this.resolved; }
/*  9 */    public static final com.mojang.serialization.Codec<SelectorPattern> CODEC = com.mojang.serialization.Codec.STRING.comapFlatMap(SelectorPattern::parse, SelectorPattern::pattern);
/*    */   
/*    */   public static com.mojang.serialization.DataResult<SelectorPattern> parse(String pattern) {
/*    */     try {
/* 13 */       EntitySelectorParser parser = new EntitySelectorParser(new com.mojang.brigadier.StringReader(pattern), true);
/* 14 */       return com.mojang.serialization.DataResult.success(new SelectorPattern(pattern, parser.parse()));
/* 15 */     } catch (CommandSyntaxException ex) {
/* 16 */       return com.mojang.serialization.DataResult.error(() -> "Invalid selector component: " + pattern + ": " + ex.getMessage());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean equals(Object obj) {
/* 22 */     if (obj instanceof SelectorPattern) { SelectorPattern selector = (SelectorPattern)obj; if (this.pattern.equals(selector.pattern)); }  return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public int hashCode() {
/* 27 */     return this.pattern.hashCode();
/*    */   }
/*    */ 
/*    */   
/*    */   public String toString() {
/* 32 */     return this.pattern;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/arguments/selector/SelectorPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */