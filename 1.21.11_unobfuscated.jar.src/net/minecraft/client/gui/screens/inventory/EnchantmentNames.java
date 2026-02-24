/*    */ package net.minecraft.client.gui.screens.inventory;
/*    */ 
/*    */ import net.minecraft.client.gui.Font;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.FontDescription;
/*    */ import net.minecraft.network.chat.FormattedText;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.Util;
/*    */ 
/*    */ public class EnchantmentNames {
/* 13 */   private static final FontDescription ALT_FONT = (FontDescription)new FontDescription.Resource(Identifier.withDefaultNamespace("alt"));
/* 14 */   private static final Style ROOT_STYLE = Style.EMPTY.withFont(ALT_FONT);
/*    */   
/* 16 */   private static final EnchantmentNames INSTANCE = new EnchantmentNames();
/*    */   
/* 18 */   private final RandomSource random = RandomSource.create();
/*    */   
/* 20 */   private final String[] words = new String[] { "the", "elder", "scrolls", "klaatu", "berata", "niktu", "xyzzy", "bless", "curse", "light", "darkness", "fire", "air", "earth", "water", "hot", "dry", "cold", "wet", "ignite", "snuff", "embiggen", "twist", "shorten", "stretch", "fiddle", "destroy", "imbue", "galvanize", "enchant", "free", "limited", "range", "of", "towards", "inside", "sphere", "cube", "self", "other", "ball", "mental", "physical", "grow", "shrink", "demon", "elemental", "spirit", "animal", "creature", "beast", "humanoid", "undead", "fresh", "stale", "phnglui", "mglwnafh", "cthulhu", "rlyeh", "wgahnagl", "fhtagn", "baguette" };
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static EnchantmentNames getInstance() {
/* 47 */     return INSTANCE;
/*    */   }
/*    */   
/*    */   public FormattedText getRandomName(Font font, int maxWidth) {
/* 51 */     StringBuilder result = new StringBuilder();
/* 52 */     int wordCount = this.random.nextInt(2) + 3;
/*    */     
/* 54 */     for (int i = 0; i < wordCount; i++) {
/* 55 */       if (i != 0) {
/* 56 */         result.append(" ");
/*    */       }
/* 58 */       result.append((String)Util.getRandom((Object[])this.words, this.random));
/*    */     } 
/* 60 */     return font.getSplitter().headByWidth((FormattedText)Component.literal(result.toString()).withStyle(ROOT_STYLE), maxWidth, Style.EMPTY);
/*    */   }
/*    */   
/*    */   public void initSeed(long seed) {
/* 64 */     this.random.setSeed(seed);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/EnchantmentNames.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */