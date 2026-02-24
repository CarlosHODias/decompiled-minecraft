/*     */ package net.minecraft.client.gui.screens.worldselection;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.components.StringWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.gui.layouts.GridLayout;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ class SwitchGrid {
/*     */   private static final int DEFAULT_SWITCH_BUTTON_WIDTH = 44;
/*     */   private final List<LabeledSwitch> switches;
/*     */   private final Layout layout;
/*     */   
/*     */   public static class Builder {
/*     */     private final int width;
/*  29 */     private final List<SwitchGrid.SwitchBuilder> switchBuilders = new ArrayList<>();
/*     */     private int paddingLeft;
/*  31 */     private int rowSpacing = 4;
/*     */     private int rowCount;
/*  33 */     private Optional<SwitchGrid.InfoUnderneathSettings> infoUnderneath = Optional.empty();
/*     */     
/*     */     public Builder(int width) {
/*  36 */       this.width = width;
/*     */     }
/*     */     
/*     */     private void increaseRow() {
/*  40 */       this.rowCount++;
/*     */     }
/*     */     
/*     */     public SwitchGrid.SwitchBuilder addSwitch(Component label, BooleanSupplier stateSupplier, Consumer<Boolean> onClicked) {
/*  44 */       SwitchGrid.SwitchBuilder switchBuilder = new SwitchGrid.SwitchBuilder(label, stateSupplier, onClicked, 44);
/*  45 */       this.switchBuilders.add(switchBuilder);
/*  46 */       return switchBuilder;
/*     */     }
/*     */     
/*     */     public Builder withPaddingLeft(int paddingLeft) {
/*  50 */       this.paddingLeft = paddingLeft;
/*  51 */       return this;
/*     */     }
/*     */     
/*     */     public Builder withRowSpacing(int rowSpacing) {
/*  55 */       this.rowSpacing = rowSpacing;
/*  56 */       return this;
/*     */     }
/*     */     
/*     */     public SwitchGrid build() {
/*  60 */       GridLayout switchGrid = new GridLayout().rowSpacing(this.rowSpacing);
/*     */       
/*  62 */       switchGrid.addChild((LayoutElement)SpacerElement.width(this.width - 44), 0, 0);
/*  63 */       switchGrid.addChild((LayoutElement)SpacerElement.width(44), 0, 1);
/*     */       
/*  65 */       List<SwitchGrid.LabeledSwitch> switches = new ArrayList<>();
/*  66 */       this.rowCount = 0;
/*  67 */       for (SwitchGrid.SwitchBuilder switchBuilder : this.switchBuilders) {
/*  68 */         switches.add(switchBuilder.build(this, switchGrid, 0));
/*     */       }
/*     */       
/*  71 */       switchGrid.arrangeElements();
/*  72 */       SwitchGrid result = new SwitchGrid(switches, (Layout)switchGrid);
/*  73 */       result.refreshStates();
/*  74 */       return result;
/*     */     }
/*     */     
/*     */     public Builder withInfoUnderneath(int maxRows, boolean alwaysMaxHeight) {
/*  78 */       this.infoUnderneath = Optional.of(new SwitchGrid.InfoUnderneathSettings(maxRows, alwaysMaxHeight));
/*  79 */       return this;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SwitchGrid(List<LabeledSwitch> switches, Layout layout) {
/*  87 */     this.switches = switches;
/*  88 */     this.layout = layout;
/*     */   }
/*     */   
/*     */   public Layout layout() {
/*  92 */     return this.layout;
/*     */   }
/*     */   
/*     */   public void refreshStates() {
/*  96 */     this.switches.forEach(LabeledSwitch::refreshState);
/*     */   }
/*     */   
/*     */   public static Builder builder(int width) {
/* 100 */     return new Builder(width);
/*     */   }
/*     */   
/*     */   public static class SwitchBuilder {
/*     */     private final Component label;
/*     */     private final BooleanSupplier stateSupplier;
/*     */     private final Consumer<Boolean> onClicked;
/*     */     private Component info;
/*     */     private BooleanSupplier isActiveCondition;
/*     */     private final int buttonWidth;
/*     */     
/*     */     private SwitchBuilder(Component label, BooleanSupplier stateSupplier, Consumer<Boolean> onClicked, int buttonWidth) {
/* 112 */       this.label = label;
/* 113 */       this.stateSupplier = stateSupplier;
/* 114 */       this.onClicked = onClicked;
/* 115 */       this.buttonWidth = buttonWidth;
/*     */     }
/*     */     
/*     */     public SwitchBuilder withIsActiveCondition(BooleanSupplier isActiveCondition) {
/* 119 */       this.isActiveCondition = isActiveCondition;
/* 120 */       return this;
/*     */     }
/*     */     
/*     */     public SwitchBuilder withInfo(Component info) {
/* 124 */       this.info = info;
/* 125 */       return this;
/*     */     }
/*     */     
/*     */     private SwitchGrid.LabeledSwitch build(SwitchGrid.Builder switchGridBuilder, GridLayout gridLayout, int startColumn) {
/* 129 */       switchGridBuilder.increaseRow();
/* 130 */       StringWidget labelWidget = new StringWidget(this.label, (Minecraft.getInstance()).font);
/* 131 */       gridLayout.addChild((LayoutElement)labelWidget, switchGridBuilder.rowCount, startColumn, gridLayout.newCellSettings().align(0.0F, 0.5F).paddingLeft(switchGridBuilder.paddingLeft));
/*     */       
/* 133 */       Optional<SwitchGrid.InfoUnderneathSettings> infoUnderneath = switchGridBuilder.infoUnderneath;
/*     */       
/* 135 */       CycleButton.Builder<Boolean> buttonBuilder = CycleButton.onOffBuilder(this.stateSupplier.getAsBoolean());
/* 136 */       buttonBuilder.displayOnlyValue();
/*     */       
/* 138 */       boolean hasTooltip = (this.info != null && infoUnderneath.isEmpty());
/* 139 */       if (hasTooltip) {
/* 140 */         Tooltip tooltip = Tooltip.create(this.info);
/* 141 */         buttonBuilder.withTooltip(value -> tooltip);
/*     */       } 
/*     */       
/* 144 */       if (this.info != null && !hasTooltip) {
/* 145 */         buttonBuilder.withCustomNarration(button -> CommonComponents.joinForNarration(new Component[] { this.label, (Component)button.createDefaultNarrationMessage(), this.info }));
/*     */       } else {
/* 147 */         buttonBuilder.withCustomNarration(button -> CommonComponents.joinForNarration(new Component[] { this.label, (Component)button.createDefaultNarrationMessage() }));
/*     */       } 
/*     */       
/* 150 */       CycleButton<Boolean> button = buttonBuilder.create(0, 0, this.buttonWidth, 20, (Component)Component.empty(), (b, value) -> this.onClicked.accept(value));
/* 151 */       if (this.isActiveCondition != null) {
/* 152 */         button.active = this.isActiveCondition.getAsBoolean();
/*     */       }
/* 154 */       gridLayout.addChild((LayoutElement)button, switchGridBuilder.rowCount, startColumn + 1, gridLayout.newCellSettings().alignHorizontallyRight());
/*     */       
/* 156 */       if (this.info != null) {
/* 157 */         infoUnderneath.ifPresent(infoUnderneathSettings -> {
/*     */               MutableComponent mutableComponent = this.info.copy().withStyle(ChatFormatting.GRAY);
/*     */               
/*     */               Font font = (Minecraft.getInstance()).font;
/*     */               MultiLineTextWidget infoWidget = new MultiLineTextWidget((Component)mutableComponent, font);
/*     */               infoWidget.setMaxWidth(switchGridBuilder.width - switchGridBuilder.paddingLeft - this.buttonWidth);
/*     */               infoWidget.setMaxRows(switchGridBuilder.maxInfoRows());
/*     */               switchGridBuilder.increaseRow();
/*     */               java.util.Objects.requireNonNull(font);
/*     */               int extraBottomPadding = switchGridBuilder.alwaysMaxHeight ? (9 * switchGridBuilder.maxInfoRows - infoWidget.getHeight()) : 0;
/*     */               switchGridBuilder.addChild((LayoutElement)infoWidget, switchGridBuilder.rowCount, gridLayout, switchGridBuilder.newCellSettings().paddingTop(-switchGridBuilder.rowSpacing).paddingBottom(extraBottomPadding));
/*     */             });
/*     */       }
/* 170 */       return new SwitchGrid.LabeledSwitch(button, this.stateSupplier, this.isActiveCondition);
/*     */     } }
/*     */   private static final class LabeledSwitch extends Record { private final CycleButton<Boolean> button; private final BooleanSupplier stateSupplier; private final BooleanSupplier isActiveCondition;
/*     */     
/* 174 */     private LabeledSwitch(CycleButton<Boolean> button, BooleanSupplier stateSupplier, BooleanSupplier isActiveCondition) { this.button = button; this.stateSupplier = stateSupplier; this.isActiveCondition = isActiveCondition; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #174	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 174 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch; } public CycleButton<Boolean> button() { return this.button; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #174	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #174	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$LabeledSwitch;
/* 174 */       //   0	8	1	o	Ljava/lang/Object; } public BooleanSupplier stateSupplier() { return this.stateSupplier; } public BooleanSupplier isActiveCondition() { return this.isActiveCondition; }
/*     */      public void refreshState() {
/* 176 */       this.button.setValue(this.stateSupplier.getAsBoolean());
/* 177 */       if (this.isActiveCondition != null)
/* 178 */         this.button.active = this.isActiveCondition.getAsBoolean(); 
/*     */     } }
/*     */   private static final class InfoUnderneathSettings extends Record { private final int maxInfoRows;
/*     */     private final boolean alwaysMaxHeight;
/*     */     
/* 183 */     private InfoUnderneathSettings(int maxInfoRows, boolean alwaysMaxHeight) { this.maxInfoRows = maxInfoRows; this.alwaysMaxHeight = alwaysMaxHeight; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #183	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/worldselection/SwitchGrid$InfoUnderneathSettings;
/* 183 */       //   0	8	1	o	Ljava/lang/Object; } public int maxInfoRows() { return this.maxInfoRows; } public boolean alwaysMaxHeight() { return this.alwaysMaxHeight; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/worldselection/SwitchGrid.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */