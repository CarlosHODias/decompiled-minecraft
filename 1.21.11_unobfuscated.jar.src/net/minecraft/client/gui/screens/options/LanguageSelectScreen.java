/*     */ package net.minecraft.client.gui.screens.options;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.SortedMap;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.resources.language.LanguageInfo;
/*     */ import net.minecraft.client.resources.language.LanguageManager;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class LanguageSelectScreen extends OptionsSubScreen {
/*  28 */   private static final Component WARNING_LABEL = (Component)Component.translatable("options.languageAccuracyWarning").withColor(-4539718);
/*     */   
/*     */   private static final int FOOTER_HEIGHT = 53;
/*  31 */   private static final Component SEARCH_HINT = (Component)Component.translatable("gui.language.search").withStyle(EditBox.SEARCH_HINT_STYLE);
/*     */   
/*     */   private static final int SEARCH_BOX_HEIGHT = 15;
/*     */   
/*     */   private final LanguageManager languageManager;
/*     */   private LanguageSelectionList languageSelectionList;
/*     */   private EditBox search;
/*     */   
/*     */   public LanguageSelectScreen(Screen lastScreen, Options options, LanguageManager languageManager) {
/*  40 */     super(lastScreen, options, (Component)Component.translatable("options.language.title"));
/*  41 */     this.languageManager = languageManager;
/*  42 */     this.layout.setFooterHeight(53);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addTitle() {
/*  47 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(4));
/*  48 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  49 */     header.addChild((LayoutElement)new StringWidget(this.title, this.font));
/*  50 */     this.search = (EditBox)header.addChild((LayoutElement)new EditBox(this.font, 0, 0, 200, 15, (Component)Component.empty()));
/*  51 */     this.search.setHint(SEARCH_HINT);
/*  52 */     this.search.setResponder(string -> {
/*     */           if (this.languageSelectionList != null) {
/*     */             this.languageSelectionList.filterEntries(string);
/*     */           }
/*     */         });
/*  57 */     Objects.requireNonNull(this.font); this.layout.setHeaderHeight((int)(12.0D + 9.0D + 15.0D));
/*     */   }
/*     */ 
/*     */   
/*     */   protected void setInitialFocus() {
/*  62 */     if (this.search != null) {
/*  63 */       setInitialFocus((GuiEventListener)this.search);
/*     */     } else {
/*  65 */       super.setInitialFocus();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   protected void addContents() {
/*  71 */     this.languageSelectionList = (LanguageSelectionList)this.layout.addToContents((LayoutElement)new LanguageSelectionList(this.minecraft));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addOptions() {}
/*     */ 
/*     */ 
/*     */   
/*     */   protected void addFooter() {
/*  81 */     LinearLayout footer = ((LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.vertical())).spacing(8);
/*  82 */     footer.defaultCellSetting().alignHorizontallyCenter();
/*  83 */     footer.addChild((LayoutElement)new StringWidget(WARNING_LABEL, this.font));
/*  84 */     LinearLayout bottomButtons = (LinearLayout)footer.addChild((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  85 */     bottomButtons.addChild((LayoutElement)Button.builder((Component)Component.translatable("options.font"), button -> this.minecraft.setScreen(new FontOptionsScreen(this, this.options))).build());
/*  86 */     bottomButtons.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).build());
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  91 */     super.repositionElements();
/*  92 */     if (this.languageSelectionList != null) {
/*  93 */       this.languageSelectionList.updateSize(this.width, this.layout);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onDone() {
/*  98 */     if (this.languageSelectionList != null) { AbstractSelectionList.Entry entry = this.languageSelectionList.getSelected(); if (entry instanceof LanguageSelectionList.Entry) { LanguageSelectionList.Entry selectedEntry = (LanguageSelectionList.Entry)entry; if (!selectedEntry.code.equals(this.languageManager.getSelected()))
/*  99 */         { this.languageManager.setSelected(selectedEntry.code);
/* 100 */           this.options.languageCode = selectedEntry.code;
/* 101 */           this.minecraft.reloadResourcePacks(); }  }
/*     */        }
/* 103 */      this.minecraft.setScreen(this.lastScreen);
/*     */   }
/*     */ 
/*     */   
/*     */   protected boolean panoramaShouldSpin() {
/* 108 */     return !(this.lastScreen instanceof net.minecraft.client.gui.screens.AccessibilityOnboardingScreen);
/*     */   }
/*     */   
/*     */   private class LanguageSelectionList extends ObjectSelectionList<LanguageSelectionList.Entry> {
/*     */     public LanguageSelectionList(Minecraft minecraft) {
/* 113 */       super(minecraft, LanguageSelectScreen.this.width, LanguageSelectScreen.this.height - 33 - 53, 33, 18);
/*     */       
/* 115 */       String selectedLanguage = LanguageSelectScreen.this.languageManager.getSelected();
/* 116 */       LanguageSelectScreen.this.languageManager.getLanguages().forEach((code, info) -> {
/*     */             Entry entry = new Entry(selectedLanguage, info);
/*     */             
/*     */             addEntry((AbstractSelectionList.Entry)entry);
/*     */             if (selectedLanguage.equals(selectedLanguage)) {
/*     */               setSelected((AbstractSelectionList.Entry)entry);
/*     */             }
/*     */           });
/* 124 */       if (getSelected() != null) {
/* 125 */         centerScrollOn(getSelected());
/*     */       }
/*     */     }
/*     */     
/*     */     private void filterEntries(String filter) {
/* 130 */       SortedMap<String, LanguageInfo> languages = LanguageSelectScreen.this.languageManager.getLanguages();
/*     */       
/* 132 */       List<Entry> filteredEntries = languages.entrySet().stream()
/* 133 */         .filter(entry -> 
/* 134 */           (filter.isEmpty() || ((LanguageInfo)entry.getValue()).name().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT)) || ((LanguageInfo)entry.getValue()).region().toLowerCase(Locale.ROOT).contains(filter.toLowerCase(Locale.ROOT))))
/*     */ 
/*     */         
/* 137 */         .map(entry -> new Entry((String)entry.getKey(), (LanguageInfo)entry.getValue()))
/* 138 */         .toList();
/*     */       
/* 140 */       replaceEntries(filteredEntries);
/* 141 */       refreshScrollAmount();
/*     */     }
/*     */ 
/*     */     
/*     */     public int getRowWidth() {
/* 146 */       return super.getRowWidth() + 50;
/*     */     }
/*     */     
/*     */     public class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */       private final String code;
/*     */       private final Component language;
/*     */       
/*     */       public Entry(String code, LanguageInfo language) {
/* 154 */         this.code = code;
/* 155 */         this.language = language.toComponent();
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 160 */         Objects.requireNonNull(LanguageSelectScreen.this.font); graphics.drawCenteredString(LanguageSelectScreen.this.font, this.language, LanguageSelectScreen.LanguageSelectionList.this.width / 2, getContentYMiddle() - 9 / 2, -1);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean keyPressed(KeyEvent event) {
/* 165 */         if (event.isSelection()) {
/* 166 */           select();
/* 167 */           LanguageSelectScreen.this.onDone();
/* 168 */           return true;
/*     */         } 
/* 170 */         return super.keyPressed(event);
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 175 */         select();
/* 176 */         if (doubleClick) {
/* 177 */           LanguageSelectScreen.this.onDone();
/*     */         }
/* 179 */         return super.mouseClicked(event, doubleClick);
/*     */       }
/*     */       
/*     */       private void select() {
/* 183 */         LanguageSelectScreen.LanguageSelectionList.this.setSelected((AbstractSelectionList.Entry)this);
/*     */       }
/*     */       
/*     */       public Component getNarration()
/*     */       {
/* 188 */         return (Component)Component.translatable("narrator.select", new Object[] { this.language }); } } } public class Entry extends ObjectSelectionList.Entry<LanguageSelectionList.Entry> { public Component getNarration() { return (Component)Component.translatable("narrator.select", new Object[] { this.language }); }
/*     */ 
/*     */     
/*     */     private final String code;
/*     */     private final Component language;
/*     */     
/*     */     public Entry(String code, LanguageInfo language) {
/*     */       this.code = code;
/*     */       this.language = language.toComponent();
/*     */     }
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       Objects.requireNonNull(LanguageSelectScreen.this.font);
/*     */       graphics.drawCenteredString(LanguageSelectScreen.this.font, this.language, LanguageSelectScreen.LanguageSelectionList.this.width / 2, getContentYMiddle() - 9 / 2, -1);
/*     */     }
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/*     */       if (event.isSelection()) {
/*     */         select();
/*     */         LanguageSelectScreen.this.onDone();
/*     */         return true;
/*     */       } 
/*     */       return super.keyPressed(event);
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*     */       select();
/*     */       if (doubleClick)
/*     */         LanguageSelectScreen.this.onDone(); 
/*     */       return super.mouseClicked(event, doubleClick);
/*     */     }
/*     */     
/*     */     private void select() {
/*     */       LanguageSelectScreen.LanguageSelectionList.this.setSelected((AbstractSelectionList.Entry)this);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/options/LanguageSelectScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */