/*     */ package net.minecraft.client.gui.screens.options.controls;
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Arrays;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.KeyMapping;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ContainerObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.FocusableTextWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.FormattedText;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import org.apache.commons.lang3.ArrayUtils;
/*     */ 
/*     */ public class KeyBindsList extends ContainerObjectSelectionList<KeyBindsList.Entry> {
/*     */   private static final int ITEM_HEIGHT = 20;
/*     */   private final KeyBindsScreen keyBindsScreen;
/*     */   private int maxNameWidth;
/*     */   
/*     */   public KeyBindsList(KeyBindsScreen keyBindsScreen, Minecraft minecraft) {
/*  29 */     super(minecraft, keyBindsScreen.width, 
/*  30 */         keyBindsScreen.layout.getContentHeight(), 
/*  31 */         keyBindsScreen.layout.getHeaderHeight(), 20);
/*     */     
/*  33 */     this.keyBindsScreen = keyBindsScreen;
/*     */     
/*  35 */     KeyMapping[] keyMappings = (KeyMapping[])ArrayUtils.clone((Object[])minecraft.options.keyMappings);
/*  36 */     Arrays.sort((Object[])keyMappings);
/*  37 */     KeyMapping.Category previousCategory = null;
/*     */     
/*  39 */     for (KeyMapping key : keyMappings) {
/*  40 */       KeyMapping.Category category = key.getCategory();
/*     */       
/*  42 */       if (category != previousCategory) {
/*  43 */         previousCategory = category;
/*  44 */         addEntry((AbstractSelectionList.Entry)new CategoryEntry(category));
/*     */       } 
/*     */       
/*  47 */       MutableComponent mutableComponent = Component.translatable(key.getName());
/*  48 */       int width = minecraft.font.width((FormattedText)mutableComponent);
/*  49 */       if (width > this.maxNameWidth) {
/*  50 */         this.maxNameWidth = width;
/*     */       }
/*  52 */       addEntry((AbstractSelectionList.Entry)new KeyEntry(key, (Component)mutableComponent));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void resetMappingAndUpdateButtons() {
/*  57 */     KeyMapping.resetMapping();
/*  58 */     refreshEntries();
/*     */   }
/*     */   
/*     */   public void refreshEntries() {
/*  62 */     children().forEach(Entry::refreshEntry);
/*     */   }
/*     */   
/*     */   public static abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {
/*     */     abstract void refreshEntry();
/*     */   }
/*     */   
/*     */   public class CategoryEntry extends Entry {
/*     */     private final FocusableTextWidget categoryName;
/*     */     
/*     */     public CategoryEntry(KeyMapping.Category category) {
/*  73 */       this.categoryName = FocusableTextWidget.builder(category.label(), KeyBindsList.this.minecraft.font).alwaysShowBorder(false).backgroundFill(FocusableTextWidget.BackgroundFill.ON_FOCUS).build();
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*  78 */       this.categoryName.setPosition(KeyBindsList.this.width / 2 - this.categoryName.getWidth() / 2, getContentBottom() - this.categoryName.getHeight());
/*  79 */       this.categoryName.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/*  84 */       return (List)List.of(this.categoryName);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/*  89 */       return (List)List.of(this.categoryName);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void refreshEntry() {}
/*     */   }
/*     */   
/*     */   public class KeyEntry
/*     */     extends Entry
/*     */   {
/*  99 */     private static final Component RESET_BUTTON_TITLE = (Component)Component.translatable("controls.reset");
/*     */     
/*     */     private static final int PADDING = 10;
/*     */     private final KeyMapping key;
/*     */     private final Component name;
/*     */     private final Button changeButton;
/*     */     private final Button resetButton;
/*     */     private boolean hasCollision = false;
/*     */     
/*     */     private KeyEntry(KeyMapping key, Component name) {
/* 109 */       this.key = key;
/* 110 */       this.name = name;
/*     */       
/* 112 */       this
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 121 */         .changeButton = Button.builder(name, button -> { KeyBindsList.this.keyBindsScreen.selectedKey = key; KeyBindsList.this.resetMappingAndUpdateButtons(); }).bounds(0, 0, 75, 20).createNarration(defaultNarrationSupplier -> key.isUnbound() ? Component.translatable("narrator.controls.unbound", new Object[] { name }) : Component.translatable("narrator.controls.bound", new Object[] { name, defaultNarrationSupplier.get() })).build();
/*     */       
/* 123 */       this
/*     */ 
/*     */         
/* 126 */         .resetButton = Button.builder(RESET_BUTTON_TITLE, button -> { key.setKey(key.getDefaultKey()); KeyBindsList.this.resetMappingAndUpdateButtons(); }).bounds(0, 0, 50, 20).createNarration(defaultNarrationSupplier -> Component.translatable("narrator.controls.reset", new Object[] { name })).build();
/* 127 */       refreshEntry();
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 132 */       int resetButtonX = KeyBindsList.this.scrollBarX() - this.resetButton.getWidth() - 10;
/* 133 */       int buttonY = getContentY() - 2;
/* 134 */       this.resetButton.setPosition(resetButtonX, buttonY);
/* 135 */       this.resetButton.render(graphics, mouseX, mouseY, a);
/*     */       
/* 137 */       int changeButtonX = resetButtonX - 5 - this.changeButton.getWidth();
/* 138 */       this.changeButton.setPosition(changeButtonX, buttonY);
/* 139 */       this.changeButton.render(graphics, mouseX, mouseY, a);
/*     */       
/* 141 */       Objects.requireNonNull(KeyBindsList.this.minecraft.font); graphics.drawString(KeyBindsList.this.minecraft.font, this.name, getContentX(), getContentYMiddle() - 9 / 2, -1);
/*     */       
/* 143 */       if (this.hasCollision) {
/* 144 */         int stripeWidth = 3;
/* 145 */         int stripeLeft = this.changeButton.getX() - 6;
/* 146 */         graphics.fill(stripeLeft, getContentY() - 1, stripeLeft + 3, getContentBottom(), -256);
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 152 */       return (List<? extends GuiEventListener>)ImmutableList.of(this.changeButton, this.resetButton);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 157 */       return (List<? extends NarratableEntry>)ImmutableList.of(this.changeButton, this.resetButton);
/*     */     }
/*     */ 
/*     */     
/*     */     protected void refreshEntry() {
/* 162 */       this.changeButton.setMessage(this.key.getTranslatedKeyMessage());
/* 163 */       this.resetButton.active = !this.key.isDefault();
/* 164 */       this.hasCollision = false;
/* 165 */       MutableComponent tooltip = Component.empty();
/*     */       
/* 167 */       if (!this.key.isUnbound()) {
/* 168 */         for (KeyMapping otherKey : KeyBindsList.this.minecraft.options.keyMappings) {
/* 169 */           if (otherKey != this.key && this.key.same(otherKey) && (!otherKey.isDefault() || !this.key.isDefault())) {
/* 170 */             if (this.hasCollision) {
/* 171 */               tooltip.append(", ");
/*     */             }
/* 173 */             this.hasCollision = true;
/* 174 */             tooltip.append((Component)Component.translatable(otherKey.getName()));
/*     */           } 
/*     */         } 
/*     */       }
/*     */       
/* 179 */       if (this.hasCollision) {
/* 180 */         this.changeButton.setMessage((Component)Component.literal("[ ").append((Component)this.changeButton.getMessage().copy().withStyle(ChatFormatting.WHITE)).append(" ]").withStyle(ChatFormatting.YELLOW));
/* 181 */         this.changeButton.setTooltip(Tooltip.create((Component)Component.translatable("controls.keybinds.duplicateKeybinds", new Object[] { tooltip })));
/*     */       } else {
/* 183 */         this.changeButton.setTooltip(null);
/*     */       } 
/*     */       
/* 186 */       if (KeyBindsList.this.keyBindsScreen.selectedKey == this.key) {
/* 187 */         this.changeButton.setMessage((Component)Component.literal("> ").append((Component)this.changeButton.getMessage().copy().withStyle(new ChatFormatting[] { ChatFormatting.WHITE, ChatFormatting.UNDERLINE })).append(" <").withStyle(ChatFormatting.YELLOW));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRowWidth() {
/* 194 */     return 340;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/controls/KeyBindsList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */