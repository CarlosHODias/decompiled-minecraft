/*    */ package net.minecraft.client.gui.screens.dialog;
/*    */ 
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.server.dialog.ConfirmationDialog;
/*    */ import net.minecraft.server.dialog.Dialog;
/*    */ import net.minecraft.server.dialog.DialogListDialog;
/*    */ import net.minecraft.server.dialog.MultiActionDialog;
/*    */ import net.minecraft.server.dialog.NoticeDialog;
/*    */ import net.minecraft.server.dialog.ServerLinksDialog;
/*    */ 
/*    */ 
/*    */ public class DialogScreens
/*    */ {
/* 17 */   private static final Map<MapCodec<? extends Dialog>, Factory<?>> FACTORIES = new HashMap<>();
/*    */   
/*    */   private static <T extends Dialog> void register(MapCodec<T> type, Factory<? super T> factory) {
/* 20 */     FACTORIES.put(type, factory);
/*    */   }
/*    */   
/*    */   public static <T extends Dialog> DialogScreen<T> createFromData(T dialog, Screen previousScreen, DialogConnectionAccess connectionAccess) {
/* 24 */     Factory<T> factory = (Factory<T>)FACTORIES.get(dialog.codec());
/* 25 */     if (factory != null) {
/* 26 */       return factory.create(previousScreen, dialog, connectionAccess);
/*    */     }
/* 28 */     return null;
/*    */   }
/*    */ 
/*    */   
/*    */   public static void bootstrap() {
/* 33 */     register(ConfirmationDialog.MAP_CODEC, SimpleDialogScreen::new);
/* 34 */     register(NoticeDialog.MAP_CODEC, SimpleDialogScreen::new);
/*    */ 
/*    */     
/* 37 */     register(DialogListDialog.MAP_CODEC, DialogListDialogScreen::new);
/* 38 */     register(MultiActionDialog.MAP_CODEC, MultiButtonDialogScreen::new);
/* 39 */     register(ServerLinksDialog.MAP_CODEC, ServerLinksDialogScreen::new);
/*    */   }
/*    */   
/*    */   @FunctionalInterface
/*    */   public static interface Factory<T extends Dialog> {
/*    */     DialogScreen<T> create(Screen param1Screen, T param1T, DialogConnectionAccess param1DialogConnectionAccess);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/DialogScreens.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */