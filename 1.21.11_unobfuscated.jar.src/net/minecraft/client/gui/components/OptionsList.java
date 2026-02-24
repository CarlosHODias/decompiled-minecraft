/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.Lists;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.Options;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.events.GuiEventListener;
/*     */ import net.minecraft.client.gui.narration.NarratableEntry;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.gui.screens.options.OptionsSubScreen;
/*     */ import net.minecraft.network.chat.Component;
/*     */ 
/*     */ 
/*     */ public class OptionsList
/*     */   extends ContainerObjectSelectionList<OptionsList.AbstractEntry>
/*     */ {
/*     */   private static final int BIG_BUTTON_WIDTH = 310;
/*     */   private static final int DEFAULT_ITEM_HEIGHT = 25;
/*     */   private final OptionsSubScreen screen;
/*     */   
/*     */   public OptionsList(Minecraft minecraft, int width, OptionsSubScreen screen) {
/*  25 */     super(minecraft, width, screen.layout.getContentHeight(), screen.layout.getHeaderHeight(), 25);
/*  26 */     this.centerListVertically = false;
/*  27 */     this.screen = screen;
/*     */   }
/*     */   
/*     */   public void addBig(OptionInstance<?> option) {
/*  31 */     addEntry(Entry.big(this.minecraft.options, option, (Screen)this.screen));
/*     */   }
/*     */   
/*     */   public void addSmall(OptionInstance<?>... options) {
/*  35 */     for (int i = 0; i < options.length; i += 2) {
/*  36 */       OptionInstance<?> secondOption = (i < options.length - 1) ? options[i + 1] : null;
/*  37 */       addEntry(Entry.small(this.minecraft.options, options[i], secondOption, this.screen));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void addSmall(List<AbstractWidget> widgets) {
/*  42 */     for (int i = 0; i < widgets.size(); i += 2) {
/*  43 */       addSmall(widgets.get(i), (i < widgets.size() - 1) ? widgets.get(i + 1) : null);
/*     */     }
/*     */   }
/*     */   
/*     */   public void addSmall(AbstractWidget firstOption, AbstractWidget secondOption) {
/*  48 */     addEntry(Entry.small(firstOption, secondOption, (Screen)this.screen));
/*     */   }
/*     */   
/*     */   public void addSmall(AbstractWidget firstOption, OptionInstance<?> firstOptionInstance, AbstractWidget secondOption) {
/*  52 */     addEntry(Entry.small(firstOption, firstOptionInstance, secondOption, (Screen)this.screen));
/*     */   }
/*     */   
/*     */   public void addHeader(Component text) {
/*  56 */     Objects.requireNonNull(this.minecraft.font); int lineHeight = 9;
/*  57 */     int paddingTop = children().isEmpty() ? 0 : (lineHeight * 2);
/*  58 */     addEntry(new HeaderEntry((Screen)this.screen, text, paddingTop), paddingTop + lineHeight + 4);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRowWidth() {
/*  63 */     return 310;
/*     */   }
/*     */   
/*     */   public AbstractWidget findOption(OptionInstance<?> option) {
/*  67 */     for (AbstractEntry child : children()) {
/*  68 */       if (child instanceof Entry) { Entry entry = (Entry)child;
/*  69 */         AbstractWidget widgetForOption = entry.findOption(option);
/*  70 */         if (widgetForOption != null) {
/*  71 */           return widgetForOption;
/*     */         } }
/*     */     
/*     */     } 
/*  75 */     return null;
/*     */   }
/*     */   
/*     */   public void applyUnsavedChanges() {
/*  79 */     for (AbstractEntry child : children()) {
/*  80 */       if (child instanceof Entry) { Entry entry = (Entry)child;
/*  81 */         for (OptionInstanceWidget optionInstanceWidget : entry.children) {
/*  82 */           if (optionInstanceWidget.optionInstance() != null) { AbstractWidget abstractWidget = optionInstanceWidget.widget(); if (abstractWidget instanceof OptionInstance.OptionInstanceSliderButton) { OptionInstance.OptionInstanceSliderButton<?> optionSlider = (OptionInstance.OptionInstanceSliderButton)abstractWidget;
/*  83 */               optionSlider.applyUnsavedValue(); }
/*     */              }
/*     */         
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */   public void resetOption(OptionInstance<?> option) {
/*  91 */     for (AbstractEntry child : children()) {
/*  92 */       if (child instanceof Entry) { Entry entry = (Entry)child;
/*  93 */         for (OptionInstanceWidget optionInstanceWidget : entry.children) {
/*  94 */           if (optionInstanceWidget.optionInstance() == option) { AbstractWidget abstractWidget = optionInstanceWidget.widget(); if (abstractWidget instanceof ResettableOptionWidget) { ResettableOptionWidget resettableOptionWidget = (ResettableOptionWidget)abstractWidget;
/*  95 */               resettableOptionWidget.resetValue();
/*     */               return; }
/*     */              }
/*     */         
/*     */         }  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   protected static abstract class AbstractEntry extends ContainerObjectSelectionList.Entry<AbstractEntry> {}
/*     */   
/*     */   protected static class HeaderEntry extends AbstractEntry {
/*     */     private final Screen screen;
/*     */     private final int paddingTop;
/*     */     private final StringWidget widget;
/*     */     
/*     */     protected HeaderEntry(Screen screen, Component text, int paddingTop) {
/* 112 */       this.screen = screen;
/* 113 */       this.paddingTop = paddingTop;
/* 114 */       this.widget = new StringWidget(text, screen.getFont());
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 119 */       return List.of(this.widget);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 124 */       this.widget.setPosition(this.screen.width / 2 - 155, getContentY() + this.paddingTop);
/* 125 */       this.widget.render(graphics, mouseX, mouseY, a);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 130 */       return List.of(this.widget);
/*     */     }
/*     */   }
/*     */   
/*     */   protected static class Entry extends AbstractEntry {
/*     */     private final List<OptionsList.OptionInstanceWidget> children;
/*     */     private final Screen screen;
/*     */     private static final int X_OFFSET = 160;
/*     */     
/*     */     private Entry(List<OptionsList.OptionInstanceWidget> widgets, Screen screen) {
/* 140 */       this.children = widgets;
/* 141 */       this.screen = screen;
/*     */     }
/*     */     
/*     */     public static Entry big(Options options, OptionInstance<?> optionInstance, Screen screen) {
/* 145 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(optionInstance.createButton(options, 0, 0, 310), optionInstance)), screen);
/*     */     }
/*     */     
/*     */     public static Entry small(AbstractWidget leftWidget, AbstractWidget rightWidget, Screen screen) {
/* 149 */       if (rightWidget == null) {
/* 150 */         return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget)), screen);
/*     */       }
/* 152 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget), new OptionsList.OptionInstanceWidget(rightWidget)), screen);
/*     */     }
/*     */     
/*     */     public static Entry small(AbstractWidget leftWidget, OptionInstance<?> leftWidgetOptionInstance, AbstractWidget rightWidget, Screen screen) {
/* 156 */       if (rightWidget == null) {
/* 157 */         return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget, leftWidgetOptionInstance)), screen);
/*     */       }
/* 159 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(leftWidget, leftWidgetOptionInstance), new OptionsList.OptionInstanceWidget(rightWidget)), screen);
/*     */     }
/*     */     
/*     */     public static Entry small(Options options, OptionInstance<?> optionA, OptionInstance<?> optionB, OptionsSubScreen screen) {
/* 163 */       AbstractWidget buttonA = optionA.createButton(options);
/* 164 */       if (optionB == null) {
/* 165 */         return new Entry(List.of(new OptionsList.OptionInstanceWidget(buttonA, optionA)), (Screen)screen);
/*     */       }
/* 167 */       return new Entry(List.of(new OptionsList.OptionInstanceWidget(buttonA, optionA), new OptionsList.OptionInstanceWidget(optionB.createButton(options), optionB)), (Screen)screen);
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderContent(GuiGraphics graphics, int mouseX, int mouseY, boolean hovered, float a) {
/* 172 */       int xOffset = 0;
/* 173 */       int x = this.screen.width / 2 - 155;
/* 174 */       for (OptionsList.OptionInstanceWidget optionInstanceWidget : this.children) {
/* 175 */         optionInstanceWidget.widget().setPosition(x + xOffset, getContentY());
/* 176 */         optionInstanceWidget.widget().render(graphics, mouseX, mouseY, a);
/* 177 */         xOffset += 160;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends GuiEventListener> children() {
/* 183 */       return Lists.transform(this.children, OptionsList.OptionInstanceWidget::widget);
/*     */     }
/*     */ 
/*     */     
/*     */     public List<? extends NarratableEntry> narratables() {
/* 188 */       return Lists.transform(this.children, OptionsList.OptionInstanceWidget::widget);
/*     */     }
/*     */     
/*     */     public AbstractWidget findOption(OptionInstance<?> option) {
/* 192 */       for (OptionsList.OptionInstanceWidget child : this.children) {
/* 193 */         if (child.optionInstance == option) {
/* 194 */           return child.widget();
/*     */         }
/*     */       } 
/* 197 */       return null;
/*     */     } }
/*     */   public static final class OptionInstanceWidget extends Record { private final AbstractWidget widget; private final OptionInstance<?> optionInstance;
/*     */     
/* 201 */     public OptionInstance<?> optionInstance() { return this.optionInstance; } public AbstractWidget widget() { return this.widget; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #201	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;
/* 201 */       //   0	8	1	o	Ljava/lang/Object; } public OptionInstanceWidget(AbstractWidget widget, OptionInstance<?> optionInstance) { this.widget = widget; this.optionInstance = optionInstance; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #201	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget; }
/*     */     public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #201	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 203 */       //   0	7	0	this	Lnet/minecraft/client/gui/components/OptionsList$OptionInstanceWidget; } public OptionInstanceWidget(AbstractWidget widget) { this(widget, null); }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/OptionsList.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */