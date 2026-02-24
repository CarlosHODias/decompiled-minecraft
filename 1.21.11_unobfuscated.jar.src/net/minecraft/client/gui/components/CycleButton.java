/*     */ package net.minecraft.client.gui.components;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.Minecraft;
/*     */ import net.minecraft.client.OptionInstance;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.narration.NarratedElementType;
/*     */ import net.minecraft.client.gui.narration.NarrationElementOutput;
/*     */ import net.minecraft.client.input.InputWithModifiers;
/*     */ import net.minecraft.client.renderer.RenderPipelines;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.MutableComponent;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.Mth;
/*     */ 
/*     */ public class CycleButton<T>
/*     */   extends AbstractButton
/*     */   implements ResettableOptionWidget {
/*     */   public static final BooleanSupplier DEFAULT_ALT_LIST_SELECTOR = () -> Minecraft.getInstance().hasAltDown();
/*  26 */   private static final List<Boolean> BOOLEAN_OPTIONS = (List<Boolean>)ImmutableList.of(Boolean.TRUE, Boolean.FALSE);
/*     */   
/*     */   private final Supplier<T> defaultValueSupplier;
/*     */   
/*     */   private final Component name;
/*     */   
/*     */   private int index;
/*     */   
/*     */   private T value;
/*     */   
/*     */   private final ValueListSupplier<T> values;
/*     */   
/*     */   private final Function<T, Component> valueStringifier;
/*     */   
/*     */   private final Function<CycleButton<T>, MutableComponent> narrationProvider;
/*     */   private final OnValueChange<T> onValueChange;
/*     */   private final DisplayState displayState;
/*     */   private final OptionInstance.TooltipSupplier<T> tooltipSupplier;
/*     */   private final SpriteSupplier<T> spriteSupplier;
/*     */   
/*     */   private CycleButton(int x, int y, int width, int height, Component message, Component name, int index, T value, Supplier<T> defaultValueSupplier, ValueListSupplier<T> values, Function<T, Component> valueStringifier, Function<CycleButton<T>, MutableComponent> narrationProvider, OnValueChange<T> onValueChange, OptionInstance.TooltipSupplier<T> tooltipSupplier, DisplayState displayState, SpriteSupplier<T> spriteSupplier) {
/*  47 */     super(x, y, width, height, message);
/*  48 */     this.name = name;
/*  49 */     this.index = index;
/*  50 */     this.defaultValueSupplier = defaultValueSupplier;
/*  51 */     this.value = value;
/*  52 */     this.values = values;
/*  53 */     this.valueStringifier = valueStringifier;
/*  54 */     this.narrationProvider = narrationProvider;
/*  55 */     this.onValueChange = onValueChange;
/*  56 */     this.displayState = displayState;
/*  57 */     this.tooltipSupplier = tooltipSupplier;
/*  58 */     this.spriteSupplier = spriteSupplier;
/*  59 */     updateTooltip();
/*     */   }
/*     */ 
/*     */   
/*     */   protected void renderContents(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/*  64 */     Identifier sprite = this.spriteSupplier.apply(this, getValue());
/*  65 */     if (sprite != null) {
/*  66 */       graphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, getX(), getY(), getWidth(), getHeight());
/*     */     } else {
/*  68 */       renderDefaultSprite(graphics);
/*     */     } 
/*     */     
/*  71 */     if (this.displayState != DisplayState.HIDE) {
/*  72 */       renderDefaultLabel(graphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE));
/*     */     }
/*     */   }
/*     */   
/*     */   private void updateTooltip() {
/*  77 */     setTooltip(this.tooltipSupplier.apply(this.value));
/*     */   }
/*     */ 
/*     */   
/*     */   public void onPress(InputWithModifiers input) {
/*  82 */     if (input.hasShiftDown()) {
/*  83 */       cycleValue(-1);
/*     */     } else {
/*  85 */       cycleValue(1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void cycleValue(int delta) {
/*  90 */     List<T> list = this.values.getSelectedList();
/*  91 */     this.index = Mth.positiveModulo(this.index + delta, list.size());
/*  92 */     T newValue = list.get(this.index);
/*     */     
/*  94 */     updateValue(newValue);
/*  95 */     this.onValueChange.onValueChange(this, newValue);
/*     */   }
/*     */   
/*     */   private T getCycledValue(int delta) {
/*  99 */     List<T> list = this.values.getSelectedList();
/* 100 */     return list.get(Mth.positiveModulo(this.index + delta, list.size()));
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mouseScrolled(double x, double y, double scrollX, double scrollY) {
/* 105 */     if (scrollY > 0.0D) {
/* 106 */       cycleValue(-1);
/* 107 */     } else if (scrollY < 0.0D) {
/* 108 */       cycleValue(1);
/*     */     } 
/* 110 */     return true;
/*     */   }
/*     */   
/*     */   public void setValue(T newValue) {
/* 114 */     List<T> list = this.values.getSelectedList();
/* 115 */     int newIndex = list.indexOf(newValue);
/* 116 */     if (newIndex != -1) {
/* 117 */       this.index = newIndex;
/*     */     }
/*     */ 
/*     */     
/* 121 */     updateValue(newValue);
/*     */   }
/*     */ 
/*     */   
/*     */   public void resetValue() {
/* 126 */     setValue(this.defaultValueSupplier.get());
/*     */   }
/*     */   
/*     */   private void updateValue(T newValue) {
/* 130 */     Component newMessage = createLabelForValue(newValue);
/* 131 */     setMessage(newMessage);
/* 132 */     this.value = newValue;
/* 133 */     updateTooltip();
/*     */   }
/*     */   
/*     */   private Component createLabelForValue(T newValue) {
/* 137 */     return (this.displayState == DisplayState.VALUE) ? this.valueStringifier.apply(newValue) : (Component)createFullName(newValue);
/*     */   }
/*     */   
/*     */   private MutableComponent createFullName(T newValue) {
/* 141 */     return CommonComponents.optionNameValue(this.name, this.valueStringifier.apply(newValue));
/*     */   }
/*     */   
/*     */   public T getValue() {
/* 145 */     return this.value;
/*     */   }
/*     */ 
/*     */   
/*     */   protected MutableComponent createNarrationMessage() {
/* 150 */     return this.narrationProvider.apply(this);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void updateWidgetNarration(NarrationElementOutput output) {
/* 156 */     output.add(NarratedElementType.TITLE, (Component)createNarrationMessage());
/* 157 */     if (this.active) {
/* 158 */       T nextValue = getCycledValue(1);
/* 159 */       Component nextValueText = createLabelForValue(nextValue);
/* 160 */       if (isFocused()) {
/* 161 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.cycle_button.usage.focused", new Object[] { nextValueText }));
/*     */       } else {
/* 163 */         output.add(NarratedElementType.USAGE, (Component)Component.translatable("narration.cycle_button.usage.hovered", new Object[] { nextValueText }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public MutableComponent createDefaultNarrationMessage() {
/* 170 */     return wrapDefaultNarrationMessage((this.displayState == DisplayState.VALUE) ? (Component)createFullName(this.value) : getMessage());
/*     */   }
/*     */   
/*     */   public static <T> Builder<T> builder(Function<T, Component> valueStringifier, Supplier<T> defaultValueSupplier) {
/* 174 */     return new Builder<>(valueStringifier, defaultValueSupplier);
/*     */   }
/*     */   
/*     */   public static <T> Builder<T> builder(Function<T, Component> valueStringifier, T defaultValue) {
/* 178 */     return new Builder<>(valueStringifier, () -> defaultValue);
/*     */   }
/*     */   
/*     */   public static Builder<Boolean> booleanBuilder(Component trueText, Component falseText, boolean defaultValue) {
/* 182 */     return new Builder<>(b -> (b == Boolean.TRUE) ? trueText : falseText, () -> defaultValue).withValues(BOOLEAN_OPTIONS);
/*     */   }
/*     */   
/*     */   public static Builder<Boolean> onOffBuilder(boolean initialValue) {
/* 186 */     return new Builder<>(b -> (b == Boolean.TRUE) ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF, () -> initialValue).withValues(BOOLEAN_OPTIONS);
/*     */   }
/*     */   
/*     */   public static class Builder<T> {
/*     */     private final Supplier<T> defaultValueSupplier;
/*     */     private final Function<T, Component> valueStringifier;
/*     */     private OptionInstance.TooltipSupplier<T> tooltipSupplier = value -> null;
/*     */     private CycleButton.SpriteSupplier<T> spriteSupplier = (button, value) -> null;
/* 194 */     private Function<CycleButton<T>, MutableComponent> narrationProvider = CycleButton::createDefaultNarrationMessage;
/*     */     
/* 196 */     private CycleButton.ValueListSupplier<T> values = CycleButton.ValueListSupplier.create((Collection<T>)ImmutableList.of());
/* 197 */     private CycleButton.DisplayState displayState = CycleButton.DisplayState.NAME_AND_VALUE;
/*     */     
/*     */     public Builder(Function<T, Component> valueStringifier, Supplier<T> defaultValueSupplier) {
/* 200 */       this.valueStringifier = valueStringifier;
/* 201 */       this.defaultValueSupplier = defaultValueSupplier;
/*     */     }
/*     */     
/*     */     public Builder<T> withValues(Collection<T> values) {
/* 205 */       return withValues(CycleButton.ValueListSupplier.create(values));
/*     */     }
/*     */     
/*     */     @SafeVarargs
/*     */     public final Builder<T> withValues(T... values) {
/* 210 */       return withValues((Collection<T>)ImmutableList.copyOf((Object[])values));
/*     */     }
/*     */     
/*     */     public Builder<T> withValues(List<T> values, List<T> altValues) {
/* 214 */       return withValues(CycleButton.ValueListSupplier.create(CycleButton.DEFAULT_ALT_LIST_SELECTOR, values, altValues));
/*     */     }
/*     */     
/*     */     public Builder<T> withValues(BooleanSupplier altCondition, List<T> values, List<T> altValues) {
/* 218 */       return withValues(CycleButton.ValueListSupplier.create(altCondition, values, altValues));
/*     */     }
/*     */     
/*     */     public Builder<T> withValues(CycleButton.ValueListSupplier<T> valueListSupplier) {
/* 222 */       this.values = valueListSupplier;
/* 223 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> withTooltip(OptionInstance.TooltipSupplier<T> tooltipSupplier) {
/* 227 */       this.tooltipSupplier = tooltipSupplier;
/* 228 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> withCustomNarration(Function<CycleButton<T>, MutableComponent> narrationProvider) {
/* 232 */       this.narrationProvider = narrationProvider;
/* 233 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> withSprite(CycleButton.SpriteSupplier<T> spriteSupplier) {
/* 237 */       this.spriteSupplier = spriteSupplier;
/* 238 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> displayState(CycleButton.DisplayState state) {
/* 242 */       this.displayState = state;
/* 243 */       return this;
/*     */     }
/*     */     
/*     */     public Builder<T> displayOnlyValue() {
/* 247 */       return displayState(CycleButton.DisplayState.VALUE);
/*     */     }
/*     */     
/*     */     public CycleButton<T> create(Component name, CycleButton.OnValueChange<T> valueChangeListener) {
/* 251 */       return create(0, 0, 150, 20, name, valueChangeListener);
/*     */     }
/*     */     
/*     */     public CycleButton<T> create(int x, int y, int width, int height, Component name) {
/* 255 */       return create(x, y, width, height, name, (button, value) -> {
/*     */           
/*     */           });
/*     */     } public CycleButton<T> create(int x, int y, int width, int height, Component name, CycleButton.OnValueChange<T> valueChangeListener) {
/* 259 */       List<T> values = this.values.getDefaultList();
/* 260 */       if (values.isEmpty()) {
/* 261 */         throw new IllegalStateException("No values for cycle button");
/*     */       }
/*     */       
/* 264 */       T initialValue = this.defaultValueSupplier.get();
/* 265 */       int initialIndex = values.indexOf(initialValue);
/* 266 */       Component valueText = this.valueStringifier.apply(initialValue);
/* 267 */       Component initialTitle = (this.displayState == CycleButton.DisplayState.VALUE) ? valueText : (Component)CommonComponents.optionNameValue(name, valueText);
/*     */       
/* 269 */       return new CycleButton<>(x, y, width, height, initialTitle, name, initialIndex, initialValue, this.defaultValueSupplier, this.values, this.valueStringifier, this.narrationProvider, valueChangeListener, this.tooltipSupplier, this.displayState, this.spriteSupplier);
/*     */     }
/*     */   }
/*     */   
/*     */   public static interface ValueListSupplier<T> {
/*     */     List<T> getSelectedList();
/*     */     
/*     */     List<T> getDefaultList();
/*     */     
/*     */     static <T> ValueListSupplier<T> create(Collection<T> values) {
/* 279 */       final ImmutableList copy = ImmutableList.copyOf(values);
/* 280 */       return new ValueListSupplier<T>()
/*     */         {
/*     */           public List<T> getSelectedList() {
/* 283 */             return (List)copy;
/*     */           }
/*     */ 
/*     */           
/*     */           public List<T> getDefaultList() {
/* 288 */             return (List)copy;
/*     */           }
/*     */         };
/*     */     }
/*     */     
/*     */     static <T> ValueListSupplier<T> create(final BooleanSupplier altSelector, List<T> defaultList, List<T> altList) {
/* 294 */       final ImmutableList defaultCopy = ImmutableList.copyOf(defaultList);
/* 295 */       final ImmutableList altCopy = ImmutableList.copyOf(altList);
/* 296 */       return new ValueListSupplier<T>()
/*     */         {
/*     */           public List<T> getSelectedList() {
/* 299 */             return altSelector.getAsBoolean() ? altCopy : defaultCopy;
/*     */           }
/*     */           
/*     */           public List<T> getDefaultList()
/*     */           {
/* 304 */             return defaultCopy; } }; } } class null implements ValueListSupplier<T> { public List<T> getSelectedList() { return (List)copy; } public List<T> getDefaultList() { return (List)copy; } } class null implements ValueListSupplier<T> { public List<T> getDefaultList() { return defaultCopy; }
/*     */ 
/*     */     
/*     */     public List<T> getSelectedList() {
/*     */       return altSelector.getAsBoolean() ? altCopy : defaultCopy;
/*     */     } }
/*     */   
/* 311 */   public enum DisplayState { NAME_AND_VALUE,
/* 312 */     VALUE,
/* 313 */     HIDE; }
/*     */ 
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface OnValueChange<T> {
/*     */     void onValueChange(CycleButton<T> param1CycleButton, T param1T);
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface SpriteSupplier<T> {
/*     */     Identifier apply(CycleButton<T> param1CycleButton, T param1T);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/components/CycleButton.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */