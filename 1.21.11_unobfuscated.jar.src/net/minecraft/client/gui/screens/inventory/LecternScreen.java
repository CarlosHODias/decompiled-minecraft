/*     */ package net.minecraft.client.gui.screens.inventory;
/*     */ 
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import net.minecraft.world.inventory.AbstractContainerMenu;
/*     */ import net.minecraft.world.inventory.ContainerListener;
/*     */ import net.minecraft.world.inventory.LecternMenu;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class LecternScreen
/*     */   extends BookViewScreen
/*     */   implements MenuAccess<LecternMenu> {
/*     */   private static final int MENU_BUTTON_MARGIN = 4;
/*     */   private static final int MENU_BUTTON_SIZE = 98;
/*  19 */   private static final Component TAKE_BOOK_LABEL = (Component)Component.translatable("lectern.take_book");
/*     */   
/*     */   private final LecternMenu menu;
/*     */   
/*  23 */   private final ContainerListener listener = new ContainerListener()
/*     */     {
/*     */       public void slotChanged(AbstractContainerMenu container, int slotIndex, ItemStack itemStack) {
/*  26 */         LecternScreen.this.bookChanged();
/*     */       }
/*     */ 
/*     */       
/*     */       public void dataChanged(AbstractContainerMenu container, int id, int value) {
/*  31 */         if (id == 0) {
/*  32 */           LecternScreen.this.pageChanged();
/*     */         }
/*     */       }
/*     */     };
/*     */   
/*     */   public LecternScreen(LecternMenu menu, Inventory inventory, Component title) {
/*  38 */     this.menu = menu;
/*     */   }
/*     */ 
/*     */   
/*     */   public LecternMenu getMenu() {
/*  43 */     return this.menu;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  48 */     super.init();
/*  49 */     this.menu.addSlotListener(this.listener);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  54 */     this.minecraft.player.closeContainer();
/*  55 */     super.onClose();
/*     */   }
/*     */ 
/*     */   
/*     */   public void removed() {
/*  60 */     super.removed();
/*  61 */     this.menu.removeSlotListener(this.listener);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void createMenuControls() {
/*  66 */     if (this.minecraft.player.mayBuild()) {
/*  67 */       int buttonY = menuControlsTop();
/*  68 */       int middle = this.width / 2;
/*  69 */       addRenderableWidget((GuiEventListener)Button.builder(CommonComponents.GUI_DONE, button -> onClose()).pos(middle - 98 - 2, buttonY).width(98).build());
/*  70 */       addRenderableWidget((GuiEventListener)Button.builder(TAKE_BOOK_LABEL, button -> sendButtonClick(3)).pos(middle + 2, buttonY).width(98).build());
/*     */     } else {
/*  72 */       super.createMenuControls();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pageBack() {
/*  78 */     sendButtonClick(1);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void pageForward() {
/*  83 */     sendButtonClick(2);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean forcePage(int page) {
/*  88 */     if (page != this.menu.getPage()) {
/*  89 */       sendButtonClick(100 + page);
/*  90 */       return true;
/*     */     } 
/*  92 */     return false;
/*     */   }
/*     */   
/*     */   private void sendButtonClick(int button) {
/*  96 */     this.minecraft.gameMode.handleInventoryButtonClick(this.menu.containerId, button);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPauseScreen() {
/* 101 */     return false;
/*     */   }
/*     */   
/*     */   private void bookChanged() {
/* 105 */     ItemStack book = this.menu.getBook();
/* 106 */     setBookAccess(Objects.<BookViewScreen.BookAccess>requireNonNullElse(BookViewScreen.BookAccess.fromItem(book), BookViewScreen.EMPTY_ACCESS));
/*     */   }
/*     */   
/*     */   private void pageChanged() {
/* 110 */     setPage(this.menu.getPage());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void closeContainerOnServer() {
/* 115 */     this.minecraft.player.closeContainer();
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/inventory/LecternScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */