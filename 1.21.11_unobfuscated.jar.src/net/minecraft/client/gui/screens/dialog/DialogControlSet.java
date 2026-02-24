/*    */ package net.minecraft.client.gui.screens.dialog;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Consumer;
/*    */ import java.util.function.Supplier;
/*    */ import net.minecraft.client.gui.components.Button;
/*    */ import net.minecraft.client.gui.components.Tooltip;
/*    */ import net.minecraft.client.gui.layouts.LayoutElement;
/*    */ import net.minecraft.client.gui.screens.dialog.input.InputControlHandlers;
/*    */ import net.minecraft.network.chat.ClickEvent;
/*    */ import net.minecraft.server.dialog.ActionButton;
/*    */ import net.minecraft.server.dialog.CommonButtonData;
/*    */ import net.minecraft.server.dialog.Input;
/*    */ import net.minecraft.server.dialog.action.Action;
/*    */ 
/*    */ public class DialogControlSet
/*    */ {
/* 20 */   public static final Supplier<Optional<ClickEvent>> EMPTY_ACTION = Optional::empty;
/*    */   
/*    */   private final DialogScreen<?> screen;
/* 23 */   private final Map<String, Action.ValueGetter> valueGetters = new HashMap<>();
/*    */   
/*    */   public DialogControlSet(DialogScreen<?> screen) {
/* 26 */     this.screen = screen;
/*    */   }
/*    */   
/*    */   public void addInput(Input data, Consumer<LayoutElement> output) {
/* 30 */     String key = data.key();
/* 31 */     InputControlHandlers.createHandler(data.control(), this.screen, (element, valueGetter) -> {
/*    */           this.valueGetters.put(key, valueGetter);
/*    */           key.accept(output);
/*    */         });
/*    */   }
/*    */   
/*    */   private static Button.Builder createDialogButton(CommonButtonData data, Button.OnPress clickAction) {
/* 38 */     Button.Builder result = Button.builder(data.label(), clickAction);
/* 39 */     result.width(data.width());
/* 40 */     if (data.tooltip().isPresent()) {
/* 41 */       result = result.tooltip(Tooltip.create(data.tooltip().get()));
/*    */     }
/* 43 */     return result;
/*    */   }
/*    */   
/*    */   public Supplier<Optional<ClickEvent>> bindAction(Optional<Action> maybeAction) {
/* 47 */     if (maybeAction.isPresent()) {
/* 48 */       Action action = maybeAction.get();
/* 49 */       return () -> action.createAction(this.valueGetters);
/*    */     } 
/* 51 */     return EMPTY_ACTION;
/*    */   }
/*    */   
/*    */   public Button.Builder createActionButton(ActionButton actionButton) {
/* 55 */     Supplier<Optional<ClickEvent>> action = bindAction(actionButton.action());
/* 56 */     return createDialogButton(actionButton.button(), button -> this.screen.runAction(action.get()));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/DialogControlSet.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */