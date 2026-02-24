/*    */ package net.minecraft.client.color;
/*    */ 
/*    */ import com.google.common.collect.Maps;
/*    */ import java.util.Arrays;
/*    */ import java.util.Map;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.util.ARGB;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.item.DyeColor;
/*    */ 
/*    */ public class ColorLerper
/*    */ {
/* 13 */   public static final DyeColor[] MUSIC_NOTE_COLORS = new DyeColor[] { DyeColor.WHITE, DyeColor.LIGHT_GRAY, DyeColor.LIGHT_BLUE, DyeColor.BLUE, DyeColor.CYAN, DyeColor.GREEN, DyeColor.LIME, DyeColor.YELLOW, DyeColor.ORANGE, DyeColor.PINK, DyeColor.RED, DyeColor.MAGENTA };
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static int getLerpedColor(Type type, float tick) {
/* 19 */     int tickCount = Mth.floor(tick);
/* 20 */     int value = tickCount / type.colorDuration;
/* 21 */     int colorCount = type.colors.length;
/* 22 */     int c1 = value % colorCount;
/* 23 */     int c2 = (value + 1) % colorCount;
/* 24 */     float subStep = ((tickCount % type.colorDuration) + Mth.frac(tick)) / type.colorDuration;
/* 25 */     int color1 = type.getColor(type.colors[c1]);
/* 26 */     int color2 = type.getColor(type.colors[c2]);
/* 27 */     return ARGB.srgbLerp(subStep, color1, color2);
/*    */   }
/*    */ 
/*    */   
/*    */   private static int getModifiedColor(DyeColor color, float brightness) {
/* 32 */     if (color == DyeColor.WHITE) {
/* 33 */       return -1644826;
/*    */     }
/*    */     
/* 36 */     int src = color.getTextureDiffuseColor();
/*    */     
/* 38 */     return ARGB.color(255, 
/*    */         
/* 40 */         Mth.floor(ARGB.red(src) * brightness), 
/* 41 */         Mth.floor(ARGB.green(src) * brightness), 
/* 42 */         Mth.floor(ARGB.blue(src) * brightness));
/*    */   }
/*    */   
/*    */   public enum Type
/*    */   {
/* 47 */     SHEEP(25, DyeColor.values(), 0.75F),
/* 48 */     MUSIC_NOTE(30, ColorLerper.MUSIC_NOTE_COLORS, 1.25F);
/*    */     
/*    */     private final int colorDuration;
/*    */     private final Map<DyeColor, Integer> colorByDye;
/*    */     private final DyeColor[] colors;
/*    */     
/*    */     Type(int colorDuration, DyeColor[] colors, float brightness) {
/* 55 */       this.colorDuration = colorDuration;
/* 56 */       this.colorByDye = Maps.newHashMap((Map)Arrays.<DyeColor>stream(colors).collect(Collectors.toMap(d -> d, color -> ColorLerper.getModifiedColor(color, brightness))));
/* 57 */       this.colors = colors;
/*    */     }
/*    */     
/*    */     public final int getColor(DyeColor dyeColor) {
/* 61 */       return (Integer)this.colorByDye.get(dyeColor);
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/color/ColorLerper.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */