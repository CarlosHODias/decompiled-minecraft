/*     */ package net.minecraft.client.gui.screens.telemetry;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.function.DoubleConsumer;
/*     */ import net.minecraft.ChatFormatting;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractTextAreaWidget;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.MultiLineTextWidget;
/*     */ import net.minecraft.client.gui.layouts.Layout;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.layouts.LayoutSettings;
/*     */ import net.minecraft.client.gui.layouts.LinearLayout;
/*     */ import net.minecraft.client.gui.layouts.SpacerElement;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.telemetry.TelemetryEventType;
/*     */ import net.minecraft.client.telemetry.TelemetryProperty;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ 
/*     */ public class TelemetryEventWidget extends AbstractTextAreaWidget {
/*     */   private static final int HEADER_HORIZONTAL_PADDING = 32;
/*     */   private static final String TELEMETRY_REQUIRED_TRANSLATION_KEY = "telemetry.event.required";
/*     */   private static final String TELEMETRY_OPTIONAL_TRANSLATION_KEY = "telemetry.event.optional";
/*     */   private static final String TELEMETRY_OPTIONAL_DISABLED_TRANSLATION_KEY = "telemetry.event.optional.disabled";
/*  31 */   private static final Component PROPERTY_TITLE = (Component)Component.translatable("telemetry_info.property_title").withStyle(ChatFormatting.UNDERLINE);
/*     */   
/*     */   private final Font font;
/*     */   private Content content;
/*     */   private DoubleConsumer onScrolledListener;
/*     */   
/*     */   public TelemetryEventWidget(int x, int y, int width, int height, Font font) {
/*  38 */     super(x, y, width, height, (Component)Component.empty());
/*     */     
/*  40 */     this.font = font;
/*  41 */     this.content = buildContent(Minecraft.getInstance().telemetryOptInExtra());
/*     */   }
/*     */   
/*     */   public void onOptInChanged(boolean optIn) {
/*  45 */     this.content = buildContent(optIn);
/*  46 */     refreshScrollAmount();
/*     */   }
/*     */   
/*     */   public void updateLayout() {
/*  50 */     this.content = buildContent(Minecraft.getInstance().telemetryOptInExtra());
/*  51 */     refreshScrollAmount();
/*     */   }
/*     */   
/*     */   private Content buildContent(boolean hasOptedIn) {
/*  55 */     ContentBuilder content = new ContentBuilder(containerWidth());
/*  56 */     List<TelemetryEventType> eventTypes = new ArrayList<>(TelemetryEventType.values());
/*  57 */     eventTypes.sort(Comparator.comparing(TelemetryEventType::isOptIn));
/*  58 */     for (int i = 0; i < eventTypes.size(); i++) {
/*  59 */       TelemetryEventType eventType = eventTypes.get(i);
/*  60 */       boolean isDisabled = (eventType.isOptIn() && !hasOptedIn);
/*     */       
/*  62 */       addEventType(content, eventType, isDisabled);
/*     */       
/*  64 */       if (i < eventTypes.size() - 1) {
/*  65 */         Objects.requireNonNull(this.font); content.addSpacer(9);
/*     */       } 
/*     */     } 
/*  68 */     return content.build();
/*     */   }
/*     */   
/*     */   public void setOnScrolledListener(DoubleConsumer listener) {
/*  72 */     this.onScrolledListener = listener;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setScrollAmount(double scrollAmount) {
/*  77 */     super.setScrollAmount(scrollAmount);
/*  78 */     if (this.onScrolledListener != null) {
/*  79 */       this.onScrolledListener.accept(scrollAmount());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   protected int getInnerHeight() {
/*  85 */     return this.content.container().getHeight();
/*     */   }
/*     */ 
/*     */   
/*     */   protected double scrollRate() {
/*  90 */     Objects.requireNonNull(this.font); return 9.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  95 */     int top = getInnerTop();
/*  96 */     int left = getInnerLeft();
/*     */     
/*  98 */     graphics.pose().pushMatrix();
/*  99 */     graphics.pose().translate(left, top);
/* 100 */     this.content.container().visitWidgets(widget -> widget.render(graphics, mouseX, mouseY, a));
/* 101 */     graphics.pose().popMatrix();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void updateWidgetNarration(NarrationElementOutput output) {
/* 106 */     output.add(NarratedElementType.TITLE, this.content.narration());
/*     */   }
/*     */   
/*     */   private Component grayOutIfDisabled(Component component, boolean isDisabled) {
/* 110 */     if (isDisabled) {
/* 111 */       return (Component)component.copy().withStyle(ChatFormatting.GRAY);
/*     */     }
/* 113 */     return component;
/*     */   }
/*     */   
/*     */   private void addEventType(ContentBuilder builder, TelemetryEventType eventType, boolean isDisabled) {
/* 117 */     String titleTranslationPattern = eventType.isOptIn() ? (isDisabled ? "telemetry.event.optional.disabled" : "telemetry.event.optional") : "telemetry.event.required";
/* 118 */     builder.addHeader(this.font, grayOutIfDisabled((Component)Component.translatable(titleTranslationPattern, new Object[] { eventType.title() }), isDisabled));
/* 119 */     builder.addHeader(this.font, (Component)eventType.description().withStyle(ChatFormatting.GRAY));
/* 120 */     Objects.requireNonNull(this.font); builder.addSpacer(9 / 2);
/*     */     
/* 122 */     builder.addLine(this.font, grayOutIfDisabled(PROPERTY_TITLE, isDisabled), 2);
/*     */     
/* 124 */     addEventTypeProperties(eventType, builder, isDisabled);
/*     */   }
/*     */   
/*     */   private void addEventTypeProperties(TelemetryEventType eventType, ContentBuilder content, boolean isDisabled) {
/* 128 */     for (TelemetryProperty<?> property : (Iterable<TelemetryProperty<?>>)eventType.properties()) {
/* 129 */       content.addLine(this.font, grayOutIfDisabled((Component)property.title(), isDisabled));
/*     */     }
/*     */   }
/*     */   
/*     */   private int containerWidth() {
/* 134 */     return this.width - totalInnerPadding();
/*     */   }
/*     */ 
/*     */   
/*     */   private static class ContentBuilder
/*     */   {
/*     */     private final int width;
/*     */     private final LinearLayout layout;
/* 142 */     private final MutableComponent narration = Component.empty();
/*     */     
/*     */     public ContentBuilder(int width) {
/* 145 */       this.width = width;
/*     */       
/* 147 */       this.layout = LinearLayout.vertical();
/* 148 */       this.layout.defaultCellSetting().alignHorizontallyLeft();
/*     */       
/* 150 */       this.layout.addChild((LayoutElement)SpacerElement.width(width));
/*     */     }
/*     */     
/*     */     public void addLine(Font font, Component line) {
/* 154 */       addLine(font, line, 0);
/*     */     }
/*     */     
/*     */     public void addLine(Font font, Component line, int paddingBottom) {
/* 158 */       this.layout.addChild((LayoutElement)new MultiLineTextWidget(line, font).setMaxWidth(this.width), s -> s.paddingBottom(paddingBottom));
/* 159 */       this.narration.append(line).append("\n");
/*     */     }
/*     */     
/*     */     public void addHeader(Font font, Component line) {
/* 163 */       this.layout.addChild((LayoutElement)new MultiLineTextWidget(line, font).setMaxWidth(this.width - 64).setCentered(true), s -> s.alignHorizontallyCenter().paddingHorizontal(32));
/* 164 */       this.narration.append(line).append("\n");
/*     */     }
/*     */     
/*     */     public void addSpacer(int height) {
/* 168 */       this.layout.addChild((LayoutElement)SpacerElement.height(height));
/*     */     }
/*     */     
/*     */     public TelemetryEventWidget.Content build() {
/* 172 */       this.layout.arrangeElements();
/* 173 */       return new TelemetryEventWidget.Content((Layout)this.layout, (Component)this.narration);
/*     */     } }
/*     */   private static final class Content extends Record { private final Layout container; private final Component narration;
/*     */     
/* 177 */     private Content(Layout container, Component narration) { this.container = container; this.narration = narration; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 177 */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content; } public Layout container() { return this.container; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #177	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/gui/screens/telemetry/TelemetryEventWidget$Content;
/* 177 */       //   0	8	1	o	Ljava/lang/Object; } public Component narration() { return this.narration; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/telemetry/TelemetryEventWidget.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */