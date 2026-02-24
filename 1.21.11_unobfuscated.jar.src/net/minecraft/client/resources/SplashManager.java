/*    */ package net.minecraft.client.resources;
/*    */ 
/*    */ import java.io.BufferedReader;
/*    */ import java.io.IOException;
/*    */ import java.time.MonthDay;
/*    */ import java.util.List;
/*    */ import java.util.Locale;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.User;
/*    */ import net.minecraft.client.gui.components.SplashRenderer;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.server.packs.resources.ResourceManager;
/*    */ import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.util.SpecialDates;
/*    */ import net.minecraft.util.profiling.ProfilerFiller;
/*    */ 
/*    */ 
/*    */ public class SplashManager
/*    */   extends SimplePreparableReloadListener<List<Component>>
/*    */ {
/* 24 */   private static final Style DEFAULT_STYLE = Style.EMPTY.withColor(-256);
/*    */   
/* 26 */   public static final Component CHRISTMAS = literalSplash("Merry X-mas!");
/* 27 */   public static final Component NEW_YEAR = literalSplash("Happy new year!");
/* 28 */   public static final Component HALLOWEEN = literalSplash("OOoooOOOoooo! Spooky!");
/*    */   
/* 30 */   private static final Identifier SPLASHES_LOCATION = Identifier.withDefaultNamespace("texts/splashes.txt");
/* 31 */   private static final RandomSource RANDOM = RandomSource.create();
/*    */   
/* 33 */   private List<Component> splashes = List.of();
/*    */   private final User user;
/*    */   
/*    */   public SplashManager(User user) {
/* 37 */     this.user = user;
/*    */   }
/*    */   
/*    */   private static Component literalSplash(String text) {
/* 41 */     return (Component)Component.literal(text).setStyle(DEFAULT_STYLE);
/*    */   }
/*    */   
/*    */   protected List<Component> prepare(ResourceManager manager, ProfilerFiller profiler) {
/*    */     
/* 46 */     try { BufferedReader reader = Minecraft.getInstance().getResourceManager().openAsReader(SPLASHES_LOCATION);
/*    */ 
/*    */ 
/*    */       
/* 50 */       try { List<Component> list = reader.lines()
/* 51 */           .map(String::trim)
/* 52 */           .filter(line -> (line.hashCode() != 125780783))
/* 53 */           .map(SplashManager::literalSplash)
/* 54 */           .toList();
/* 55 */         if (reader != null) reader.close();  return list; } catch (Throwable throwable) { if (reader != null) try { reader.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }   throw throwable; }  } catch (IOException ignored)
/* 56 */     { return List.of(); }
/*    */   
/*    */   }
/*    */ 
/*    */   
/*    */   protected void apply(List<Component> preparations, ResourceManager manager, ProfilerFiller profiler) {
/* 62 */     this.splashes = List.copyOf(preparations);
/*    */   }
/*    */   
/*    */   public SplashRenderer getSplash() {
/* 66 */     MonthDay monthDay = SpecialDates.dayNow();
/*    */     
/* 68 */     if (monthDay.equals(SpecialDates.CHRISTMAS))
/* 69 */       return SplashRenderer.CHRISTMAS; 
/* 70 */     if (monthDay.equals(SpecialDates.NEW_YEAR))
/* 71 */       return SplashRenderer.NEW_YEAR; 
/* 72 */     if (monthDay.equals(SpecialDates.HALLOWEEN)) {
/* 73 */       return SplashRenderer.HALLOWEEN;
/*    */     }
/*    */ 
/*    */     
/* 77 */     if (this.splashes.isEmpty())
/* 78 */       return null; 
/* 79 */     if (this.user != null && RANDOM.nextInt(this.splashes.size()) == 42) {
/* 80 */       return new SplashRenderer(literalSplash(this.user.getName().toUpperCase(Locale.ROOT) + " IS YOU"));
/*    */     }
/* 82 */     return new SplashRenderer(this.splashes.get(RANDOM.nextInt(this.splashes.size())));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/resources/SplashManager.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */