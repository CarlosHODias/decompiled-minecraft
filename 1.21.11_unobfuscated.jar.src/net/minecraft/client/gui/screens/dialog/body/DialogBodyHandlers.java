/*    */ package net.minecraft.client.gui.screens.dialog.body;
/*    */ 
/*    */ import com.mojang.logging.LogUtils;
/*    */ import com.mojang.serialization.MapCodec;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.client.Minecraft;
/*    */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*    */ import net.minecraft.client.gui.components.ItemDisplayWidget;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.layouts.LinearLayout;
/*    */ import net.minecraft.client.gui.screens.dialog.DialogScreen;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Style;
/*    */ import net.minecraft.server.dialog.body.DialogBody;
/*    */ import net.minecraft.server.dialog.body.ItemBody;
/*    */ import net.minecraft.server.dialog.body.PlainMessage;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ 
/*    */ public class DialogBodyHandlers
/*    */ {
/* 25 */   private static final Logger LOGGER = LogUtils.getLogger();
/*    */   
/* 27 */   private static final Map<MapCodec<? extends DialogBody>, DialogBodyHandler<?>> HANDLERS = new HashMap<>();
/*    */   
/*    */   private static <B extends DialogBody> void register(MapCodec<B> type, DialogBodyHandler<? super B> handler) {
/* 30 */     HANDLERS.put(type, handler);
/*    */   }
/*    */ 
/*    */   
/*    */   private static <B extends DialogBody> DialogBodyHandler<B> getHandler(B body) {
/* 35 */     return (DialogBodyHandler<B>)HANDLERS.get(body.mapCodec());
/*    */   }
/*    */   
/*    */   public static <B extends DialogBody> LayoutElement createBodyElement(DialogScreen<?> screen, B body) {
/* 39 */     DialogBodyHandler<B> handler = getHandler(body);
/* 40 */     if (handler == null) {
/* 41 */       LOGGER.warn("Unrecognized dialog body {}", body);
/* 42 */       return null;
/*    */     } 
/*    */     
/* 45 */     return handler.createControls(screen, body);
/*    */   }
/*    */   
/*    */   public static void bootstrap() {
/* 49 */     register(PlainMessage.MAP_CODEC, (DialogBodyHandler<? super DialogBody>)new PlainMessageHandler());
/* 50 */     register(ItemBody.MAP_CODEC, (DialogBodyHandler<? super DialogBody>)new ItemHandler());
/*    */   }
/*    */   
/*    */   private static void runActionOnParent(DialogScreen<?> parent, Style clickedStyle) {
/* 54 */     if (clickedStyle != null) {
/* 55 */       ClickEvent clickEvent = clickedStyle.getClickEvent();
/* 56 */       if (clickEvent != null)
/* 57 */         parent.runAction(Optional.of(clickEvent)); 
/*    */     } 
/*    */   }
/*    */   
/*    */   private static class PlainMessageHandler
/*    */     implements DialogBodyHandler<PlainMessage>
/*    */   {
/*    */     public LayoutElement createControls(DialogScreen<?> parent, PlainMessage message) {
/* 65 */       return (LayoutElement)FocusableTextWidget.builder(message.contents(), parent.getFont()).maxWidth(message.width()).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.NEVER).build()
/* 66 */         .setCentered(true)
/* 67 */         .setComponentClickHandler(style -> DialogBodyHandlers.runActionOnParent(parent, style));
/*    */     }
/*    */   }
/*    */   
/*    */   private static class ItemHandler
/*    */     implements DialogBodyHandler<ItemBody> {
/*    */     public LayoutElement createControls(DialogScreen<?> parent, ItemBody item) {
/* 74 */       if (item.description().isPresent()) {
/* 75 */         PlainMessage description = item.description().get();
/* 76 */         LinearLayout layout = LinearLayout.horizontal().spacing(2);
/* 77 */         layout.defaultCellSetting().alignVerticallyMiddle();
/* 78 */         ItemDisplayWidget itemWidget = new ItemDisplayWidget(Minecraft.getInstance(), 0, 0, item.width(), item.height(), CommonComponents.EMPTY, item.item(), item.showDecorations(), item.showTooltip());
/* 79 */         layout.addChild((LayoutElement)itemWidget);
/*    */         
/* 81 */         layout.addChild((LayoutElement)FocusableTextWidget.builder(description.contents(), parent.getFont()).maxWidth(description.width()).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.NEVER).build()
/* 82 */             .setComponentClickHandler(style -> DialogBodyHandlers.runActionOnParent(parent, style)));
/* 83 */         return (LayoutElement)layout;
/*    */       } 
/* 85 */       return (LayoutElement)new ItemDisplayWidget(Minecraft.getInstance(), 0, 0, item.width(), item.height(), item.item().getHoverName(), item.item(), item.showDecorations(), item.showTooltip());
/*    */     }
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/body/DialogBodyHandlers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */