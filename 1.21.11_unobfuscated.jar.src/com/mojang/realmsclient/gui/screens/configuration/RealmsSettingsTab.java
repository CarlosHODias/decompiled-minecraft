/*     */ package com.mojang.realmsclient.gui.screens.configuration;
/*     */ 
/*     */ import com.mojang.realmsclient.dto.RealmsRegion;
/*     */ import com.mojang.realmsclient.dto.RealmsServer;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreference;
/*     */ import com.mojang.realmsclient.dto.RegionSelectionPreferenceDto;
/*     */ import com.mojang.realmsclient.dto.ServiceQuality;
/*     */ import com.mojang.realmsclient.gui.screens.RealmsPopups;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.components.Button;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.ImageWidget;
/*     */ import net.minecraft.client.gui.components.PopupScreen;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.components.tabs.GridLayoutTab;
/*     */ import net.minecraft.client.gui.layouts.EqualSpacingLayout;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class RealmsSettingsTab extends GridLayoutTab implements RealmsConfigurationTab {
/*     */   private static final int COMPONENT_WIDTH = 212;
/*     */   private static final int EXTRA_SPACING = 2;
/*     */   private static final int DEFAULT_SPACING = 6;
/*  34 */   static final Component TITLE = (Component)Component.translatable("mco.configure.world.settings.title");
/*  35 */   private static final Component NAME_LABEL = (Component)Component.translatable("mco.configure.world.name");
/*  36 */   private static final Component DESCRIPTION_LABEL = (Component)Component.translatable("mco.configure.world.description");
/*  37 */   private static final Component REGION_PREFERENCE_LABEL = (Component)Component.translatable("mco.configure.world.region_preference");
/*  38 */   private static final Tooltip REALM_NAME_VALIDATION_ERROR_TOOLTIP = Tooltip.create((Component)Component.translatable("mco.configure.world.name.validation.whitespace")); private final RealmsConfigureWorldScreen configurationScreen; private final Minecraft minecraft;
/*     */   private RealmsServer serverData;
/*     */   private final Map<RealmsRegion, ServiceQuality> regionServiceQuality;
/*     */   final Button closeOpenButton;
/*     */   private final EditBox descEdit;
/*     */   private final EditBox nameEdit;
/*     */   private final StringWidget selectedRegionStringWidget;
/*     */   private final ImageWidget selectedRegionImageWidget;
/*     */   private RegionSelection preferredRegionSelection;
/*     */   
/*     */   public static final class RegionSelection extends Record { private final RegionSelectionPreference preference;
/*     */     private final RealmsRegion region;
/*     */     
/*  51 */     public RegionSelection(RegionSelectionPreference preference, RealmsRegion region) { this.preference = preference; this.region = region; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  51 */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection; } public RegionSelectionPreference preference() { return this.preference; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  51 */       //   0	7	0	this	Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection; } public RealmsRegion region() { return this.region; } public final boolean equals(Object o) {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #51	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lcom/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab$RegionSelection;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */     } } RealmsSettingsTab(RealmsConfigureWorldScreen configurationScreen, Minecraft minecraft, RealmsServer serverData, Map<RealmsRegion, ServiceQuality> regionServiceQuality) {
/*  54 */     super(TITLE);
/*  55 */     this.configurationScreen = configurationScreen;
/*  56 */     this.minecraft = minecraft;
/*  57 */     this.serverData = serverData;
/*  58 */     this.regionServiceQuality = regionServiceQuality;
/*  59 */     GridLayout.RowHelper helper = this.layout.rowSpacing(6).createRowHelper(1);
/*     */     
/*  61 */     helper.addChild((LayoutElement)new StringWidget(NAME_LABEL, configurationScreen.getFont()));
/*  62 */     this.nameEdit = new EditBox(minecraft.font, 0, 0, 212, 20, (Component)Component.translatable("mco.configure.world.name"));
/*  63 */     this.nameEdit.setMaxLength(32);
/*  64 */     this.nameEdit.setResponder(value -> {
/*     */           if (!isRealmNameValid()) {
/*     */             this.nameEdit.setTextColor(-2142128);
/*     */             this.nameEdit.setTooltip(REALM_NAME_VALIDATION_ERROR_TOOLTIP);
/*     */             return;
/*     */           } 
/*     */           this.nameEdit.setTooltip(null);
/*     */           this.nameEdit.setTextColor(-2039584);
/*     */         });
/*  73 */     helper.addChild((LayoutElement)this.nameEdit);
/*     */     
/*  75 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  76 */     helper.addChild((LayoutElement)new StringWidget(DESCRIPTION_LABEL, configurationScreen.getFont()));
/*  77 */     this.descEdit = new EditBox(minecraft.font, 0, 0, 212, 20, (Component)Component.translatable("mco.configure.world.description"));
/*  78 */     this.descEdit.setMaxLength(32);
/*  79 */     helper.addChild((LayoutElement)this.descEdit);
/*     */     
/*  81 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*  82 */     helper.addChild((LayoutElement)new StringWidget(REGION_PREFERENCE_LABEL, configurationScreen.getFont()));
/*     */     
/*  84 */     Objects.requireNonNull(configurationScreen.getFont()); EqualSpacingLayout selectedRegion = new EqualSpacingLayout(0, 0, 212, 9, EqualSpacingLayout.Orientation.HORIZONTAL);
/*  85 */     Objects.requireNonNull(configurationScreen.getFont()); this.selectedRegionStringWidget = (StringWidget)selectedRegion.addChild((LayoutElement)new StringWidget(192, 9, (Component)Component.empty(), configurationScreen.getFont()));
/*  86 */     this.selectedRegionImageWidget = (ImageWidget)selectedRegion.addChild((LayoutElement)ImageWidget.sprite(10, 8, ServiceQuality.UNKNOWN.getIcon()));
/*  87 */     helper.addChild((LayoutElement)selectedRegion);
/*     */     
/*  89 */     helper.addChild((LayoutElement)Button.builder((Component)Component.translatable("mco.configure.world.buttons.region_preference"), button -> openPreferenceSelector())
/*  90 */         .bounds(0, 0, 212, 20).build());
/*     */     
/*  92 */     helper.addChild((LayoutElement)SpacerElement.height(2));
/*     */     
/*  94 */     this.closeOpenButton = (Button)helper.addChild((LayoutElement)Button.builder((Component)Component.empty(), button -> {
/*     */             if (serverData.state == RealmsServer.State.OPEN) {
/*     */               serverData.setScreen((Screen)RealmsPopups.customPopupScreen((Screen)serverData, (Component)Component.translatable("mco.configure.world.close.question.title"), (Component)Component.translatable("mco.configure.world.close.question.line1"), ()));
/*     */             } else {
/*     */               save();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */               
/*     */               serverData.openTheWorld(false);
/*     */             } 
/* 107 */           }).bounds(0, 0, 212, 20).build());
/* 108 */     this.closeOpenButton.active = false;
/*     */     
/* 110 */     updateData(serverData);
/*     */   }
/*     */   
/*     */   private static MutableComponent getTranslatableFromPreference(RegionSelection regionSelection) {
/* 114 */     return ((regionSelection.preference().equals(RegionSelectionPreference.MANUAL) && regionSelection.region() != null) ? 
/* 115 */       Component.translatable((regionSelection.region()).translationKey) : 
/* 116 */       Component.translatable((regionSelection.preference()).translationKey))
/* 117 */       .withStyle(ChatFormatting.GRAY);
/*     */   }
/*     */   
/*     */   private static Identifier getServiceQualityIcon(RegionSelection regionSelection, Map<RealmsRegion, ServiceQuality> regionServiceQuality) {
/* 121 */     if (regionSelection.region() != null && regionServiceQuality.containsKey(regionSelection.region())) {
/* 122 */       ServiceQuality serviceQuality = regionServiceQuality.getOrDefault(regionSelection.region(), ServiceQuality.UNKNOWN);
/* 123 */       return serviceQuality.getIcon();
/*     */     } 
/* 125 */     return ServiceQuality.UNKNOWN.getIcon();
/*     */   }
/*     */   
/*     */   private boolean isRealmNameValid() {
/* 129 */     String name = this.nameEdit.getValue();
/* 130 */     String trimmedName = name.trim();
/* 131 */     return (!trimmedName.isEmpty() && name.length() == trimmedName.length());
/*     */   }
/*     */   
/*     */   private void openPreferenceSelector() {
/* 135 */     this.minecraft.setScreen(new RealmsPreferredRegionSelectionScreen((Screen)this.configurationScreen, this::applyRegionPreferenceSelection, this.regionServiceQuality, this.preferredRegionSelection));
/*     */   }
/*     */   
/*     */   private void applyRegionPreferenceSelection(RegionSelectionPreference preference, RealmsRegion region) {
/* 139 */     this.preferredRegionSelection = new RegionSelection(preference, region);
/* 140 */     updateRegionPreferenceValues();
/*     */   }
/*     */   
/*     */   private void updateRegionPreferenceValues() {
/* 144 */     this.selectedRegionStringWidget.setMessage((Component)getTranslatableFromPreference(this.preferredRegionSelection));
/* 145 */     this.selectedRegionImageWidget.updateResource(getServiceQualityIcon(this.preferredRegionSelection, this.regionServiceQuality));
/* 146 */     this.selectedRegionImageWidget.visible = (this.preferredRegionSelection.preference == RegionSelectionPreference.MANUAL);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onSelected(RealmsServer serverData) {
/* 151 */     updateData(serverData);
/*     */   }
/*     */ 
/*     */   
/*     */   public void updateData(RealmsServer serverData) {
/* 156 */     this.serverData = serverData;
/* 157 */     if (serverData.regionSelectionPreference == null) {
/* 158 */       serverData.regionSelectionPreference = RegionSelectionPreferenceDto.DEFAULT;
/*     */     }
/*     */     
/* 161 */     if (serverData.regionSelectionPreference.regionSelectionPreference == RegionSelectionPreference.MANUAL && serverData.regionSelectionPreference.preferredRegion == null) {
/* 162 */       Optional<RealmsRegion> first = this.regionServiceQuality.keySet().stream().findFirst();
/* 163 */       first.ifPresent(region -> serverData.regionSelectionPreference.preferredRegion = region);
/*     */     } 
/*     */     
/* 166 */     String key = (serverData.state == RealmsServer.State.OPEN) ? "mco.configure.world.buttons.close" : "mco.configure.world.buttons.open";
/* 167 */     this.closeOpenButton.setMessage((Component)Component.translatable(key));
/* 168 */     this.closeOpenButton.active = true;
/*     */     
/* 170 */     this.preferredRegionSelection = new RegionSelection(serverData.regionSelectionPreference.regionSelectionPreference, serverData.regionSelectionPreference.preferredRegion);
/* 171 */     this.nameEdit.setValue(Objects.<String>requireNonNullElse(serverData.getName(), ""));
/* 172 */     this.descEdit.setValue(serverData.getDescription());
/* 173 */     updateRegionPreferenceValues();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDeselected(RealmsServer serverData) {
/* 178 */     save();
/*     */   }
/*     */   
/*     */   public void save() {
/* 182 */     String realmName = this.nameEdit.getValue().trim();
/* 183 */     if (this.serverData.regionSelectionPreference != null && 
/* 184 */       Objects.equals(realmName, this.serverData.name) && 
/* 185 */       Objects.equals(this.descEdit.getValue(), this.serverData.motd) && 
/* 186 */       this.preferredRegionSelection.preference() == this.serverData.regionSelectionPreference.regionSelectionPreference && 
/* 187 */       this.preferredRegionSelection.region() == this.serverData.regionSelectionPreference.preferredRegion) {
/*     */       return;
/*     */     }
/* 190 */     this.configurationScreen.saveSettings(realmName, this.descEdit.getValue(), this.preferredRegionSelection.preference(), this.preferredRegionSelection.region());
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/realmsclient/gui/screens/configuration/RealmsSettingsTab.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */