/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ import com.mojang.realmsclient.dto.RealmsRegion;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreference;
/*     */ import com.mojang.realmsclient.dto.ServiceQuality;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.BiConsumer;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractSelectionList;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.ObjectSelectionList;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ public class RealmsPreferredRegionSelectionScreen extends Screen {
/*  26 */   private static final Component REGION_SELECTION_LABEL = (Component)Component.translatable("mco.configure.world.region_preference.title");
/*     */   
/*     */   private static final int SPACING = 8;
/*  29 */   private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
/*     */   
/*     */   private final Screen parent;
/*     */   
/*     */   private final BiConsumer<RegionSelectionPreference, RealmsRegion> applySettings;
/*     */   
/*     */   private final Map<RealmsRegion, ServiceQuality> regionServiceQuality;
/*     */   
/*     */   private RegionSelectionList list;
/*     */   
/*     */   private RealmsSettingsTab.RegionSelection selection;
/*     */   
/*     */   private Button doneButton;
/*     */ 
/*     */   
/*     */   public RealmsPreferredRegionSelectionScreen(Screen parent, BiConsumer<RegionSelectionPreference, RealmsRegion> applySettings, Map<RealmsRegion, ServiceQuality> regionServiceQuality, RealmsSettingsTab.RegionSelection currentSelection) {
/*  45 */     super(REGION_SELECTION_LABEL);
/*  46 */     this.parent = parent;
/*  47 */     this.applySettings = applySettings;
/*  48 */     this.regionServiceQuality = regionServiceQuality;
/*  49 */     this.selection = currentSelection;
/*     */   }
/*     */ 
/*     */   
/*     */   public void onClose() {
/*  54 */     this.minecraft.setScreen(this.parent);
/*     */   }
/*     */ 
/*     */   
/*     */   protected void init() {
/*  59 */     LinearLayout header = (LinearLayout)this.layout.addToHeader((LayoutElement)LinearLayout.vertical().spacing(8));
/*  60 */     header.defaultCellSetting().alignHorizontallyCenter();
/*  61 */     header.addChild((LayoutElement)new net.minecraft.client.gui.components.StringWidget(getTitle(), this.font));
/*     */     
/*  63 */     this.list = (RegionSelectionList)this.layout.addToContents((LayoutElement)new RegionSelectionList());
/*     */     
/*  65 */     LinearLayout footer = (LinearLayout)this.layout.addToFooter((LayoutElement)LinearLayout.horizontal().spacing(8));
/*  66 */     this.doneButton = (Button)footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_DONE, button -> onDone()).build());
/*  67 */     footer.addChild((LayoutElement)Button.builder(CommonComponents.GUI_CANCEL, button -> onClose()).build());
/*     */     
/*  69 */     this.list.setSelected(this.list.children().stream().filter(e -> Objects.equals(e.regionSelection, this.selection)).findFirst().orElse(null));
/*     */     
/*  71 */     this.layout.visitWidgets(x$0 -> (AbstractWidget)rec$.addRenderableWidget(x$0));
/*  72 */     repositionElements();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void repositionElements() {
/*  77 */     this.layout.arrangeElements();
/*  78 */     if (this.list != null) {
/*  79 */       this.list.updateSize(this.width, this.layout);
/*     */     }
/*     */   }
/*     */   
/*     */   private void onDone() {
/*  84 */     if (this.selection.region() != null) {
/*  85 */       this.applySettings.accept(this.selection.preference(), this.selection.region());
/*     */     }
/*  87 */     onClose();
/*     */   }
/*     */   
/*     */   private void updateButtonValidity() {
/*  91 */     if (this.doneButton != null && this.list != null)
/*  92 */       this.doneButton.active = (this.list.getSelected() != null); 
/*     */   }
/*     */   
/*     */   private class RegionSelectionList
/*     */     extends ObjectSelectionList<RegionSelectionList.Entry> {
/*     */     private RegionSelectionList() {
/*  98 */       super(RealmsPreferredRegionSelectionScreen.this.minecraft, RealmsPreferredRegionSelectionScreen.this.width, RealmsPreferredRegionSelectionScreen.this.height - 77, 40, 16);
/*     */       
/* 100 */       addEntry((AbstractSelectionList.Entry)new Entry(RegionSelectionPreference.AUTOMATIC_PLAYER, null));
/* 101 */       addEntry((AbstractSelectionList.Entry)new Entry(RegionSelectionPreference.AUTOMATIC_OWNER, null));
/* 102 */       RealmsPreferredRegionSelectionScreen.this.regionServiceQuality.keySet().stream()
/* 103 */         .map(region -> new Entry(RegionSelectionPreference.MANUAL, region))
/* 104 */         .forEach(x$0 -> rec$.addEntry(x$0));
/*     */     }
/*     */ 
/*     */     
/*     */     public void setSelected(Entry selected) {
/* 109 */       super.setSelected((AbstractSelectionList.Entry)selected);
/*     */       
/* 111 */       if (selected != null) {
/* 112 */         RealmsPreferredRegionSelectionScreen.this.selection = selected.regionSelection;
/*     */       }
/* 114 */       RealmsPreferredRegionSelectionScreen.this.updateButtonValidity();
/*     */     }
/*     */     
/*     */     private class Entry extends ObjectSelectionList.Entry<Entry> {
/*     */       private final RealmsSettingsTab.RegionSelection regionSelection;
/*     */       private final Component name;
/*     */       
/*     */       public Entry(RegionSelectionPreference preference, RealmsRegion region) {
/* 122 */         this(new RealmsSettingsTab.RegionSelection(preference, region));
/*     */       }
/*     */       
/*     */       public Entry(RealmsSettingsTab.RegionSelection regionSelection) {
/* 126 */         this.regionSelection = regionSelection;
/* 127 */         if (regionSelection.preference() == RegionSelectionPreference.MANUAL) {
/* 128 */           if (regionSelection.region() != null) {
/* 129 */             this.name = (Component)Component.translatable((regionSelection.region()).translationKey);
/*     */           } else {
/* 131 */             this.name = (Component)Component.empty();
/*     */           } 
/*     */         } else {
/* 134 */           this.name = (Component)Component.translatable((regionSelection.preference()).translationKey);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public Component getNarration() {
/* 140 */         return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */       }
/*     */ 
/*     */       
/*     */       public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 145 */         graphics.drawString(RealmsPreferredRegionSelectionScreen.this.font, this.name, getContentX() + 5, getContentY() + 2, -1);
/* 146 */         if (this.regionSelection.region() != null && RealmsPreferredRegionSelectionScreen.this.regionServiceQuality.containsKey(this.regionSelection.region())) {
/* 147 */           ServiceQuality serviceQuality = RealmsPreferredRegionSelectionScreen.this.regionServiceQuality.getOrDefault(this.regionSelection.region(), ServiceQuality.UNKNOWN);
/* 148 */           graphics.blitSprite(RenderPipelines.GUI_TEXTURED, serviceQuality.getIcon(), getContentRight() - 18, getContentY() + 2, 10, 8);
/*     */         } 
/*     */       }
/*     */ 
/*     */       
/*     */       public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/* 154 */         RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.setSelected(this);
/* 155 */         if (doubleClick) {
/* 156 */           RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.playDownSound(RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.minecraft.getSoundManager());
/* 157 */           RealmsPreferredRegionSelectionScreen.this.onDone();
/* 158 */           return true;
/*     */         } 
/* 160 */         return super.mouseClicked(event, doubleClick);
/*     */       }
/*     */       
/*     */       public boolean keyPressed(KeyEvent event)
/*     */       {
/* 165 */         if (event.isSelection()) {
/* 166 */           RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.playDownSound(RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.minecraft.getSoundManager());
/* 167 */           RealmsPreferredRegionSelectionScreen.this.onDone();
/* 168 */           return true;
/*     */         } 
/* 170 */         return super.keyPressed(event); } } } private class Entry extends ObjectSelectionList.Entry<RegionSelectionList.Entry> { public boolean keyPressed(KeyEvent event) { if (event.isSelection()) { RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.playDownSound(RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.minecraft.getSoundManager()); RealmsPreferredRegionSelectionScreen.this.onDone(); return true; }  return super.keyPressed(event); }
/*     */ 
/*     */     
/*     */     private final RealmsSettingsTab.RegionSelection regionSelection;
/*     */     private final Component name;
/*     */     
/*     */     public Entry(RegionSelectionPreference preference, RealmsRegion region) {
/*     */       this(new RealmsSettingsTab.RegionSelection(preference, region));
/*     */     }
/*     */     
/*     */     public Entry(RealmsSettingsTab.RegionSelection regionSelection) {
/*     */       this.regionSelection = regionSelection;
/*     */       if (regionSelection.preference() == RegionSelectionPreference.MANUAL) {
/*     */         if (regionSelection.region() != null) {
/*     */           this.name = (Component)Component.translatable((regionSelection.region()).translationKey);
/*     */         } else {
/*     */           this.name = (Component)Component.empty();
/*     */         } 
/*     */       } else {
/*     */         this.name = (Component)Component.translatable((regionSelection.preference()).translationKey);
/*     */       } 
/*     */     }
/*     */     
/*     */     public Component getNarration() {
/*     */       return (Component)Component.translatable("narrator.select", new Object[] { this.name });
/*     */     }
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/*     */       graphics.drawString(RealmsPreferredRegionSelectionScreen.this.font, this.name, getContentX() + 5, getContentY() + 2, -1);
/*     */       if (this.regionSelection.region() != null && RealmsPreferredRegionSelectionScreen.this.regionServiceQuality.containsKey(this.regionSelection.region())) {
/*     */         ServiceQuality serviceQuality = RealmsPreferredRegionSelectionScreen.this.regionServiceQuality.getOrDefault(this.regionSelection.region(), ServiceQuality.UNKNOWN);
/*     */         graphics.blitSprite(RenderPipelines.GUI_TEXTURED, serviceQuality.getIcon(), getContentRight() - 18, getContentY() + 2, 10, 8);
/*     */       } 
/*     */     }
/*     */     
/*     */     public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
/*     */       RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.setSelected(this);
/*     */       if (doubleClick) {
/*     */         RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.playDownSound(RealmsPreferredRegionSelectionScreen.RegionSelectionList.this.minecraft.getSoundManager());
/*     */         RealmsPreferredRegionSelectionScreen.this.onDone();
/*     */         return true;
/*     */       } 
/*     */       return super.mouseClicked(event, doubleClick);
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsPreferredRegionSelectionScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */