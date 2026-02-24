/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.google.common.collect.ImmutableList;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import java.util.List;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import java.util.function.BooleanSupplier;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.DoubleFunction;
/*     */ import java.util.function.Function;
/*     */ import java.util.function.IntFunction;
/*     */ import java.util.function.IntSupplier;
/*     */ import java.util.function.Supplier;
/*     */ import java.util.function.ToDoubleFunction;
/*     */ import java.util.function.ToIntFunction;
/*     */ import java.util.stream.IntStream;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.AbstractOptionSliderButton;
/*     */ import net.minecraft.client.gui.components.AbstractWidget;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.ResettableOptionWidget;
/*     */ import net.minecraft.client.gui.components.Tooltip;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.Util;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public final class OptionInstance<T>
/*     */ {
/*  39 */   private static final Logger LOGGER = LogUtils.getLogger();
/*  40 */   public static final Enum<Boolean> BOOLEAN_VALUES = new Enum<>((List<Boolean>)ImmutableList.of(Boolean.TRUE, Boolean.FALSE), (Codec<Boolean>)Codec.BOOL); public static final CaptionBasedToString<Boolean> BOOLEAN_TO_STRING; private final TooltipSupplier<T> tooltip; private final Function<T, Component> toString; private final ValueSet<T> values; private final Codec<T> codec; private final T initialValue; private final Consumer<T> onValueUpdate; private final Component caption; private T value; static {
/*  41 */     BOOLEAN_TO_STRING = ((caption, b) -> b ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionInstance<Boolean> createBoolean(String captionId, boolean initialValue, Consumer<Boolean> onValueUpdate) {
/*  54 */     return createBoolean(captionId, noTooltip(), initialValue, onValueUpdate);
/*     */   }
/*     */   
/*     */   public static OptionInstance<Boolean> createBoolean(String captionId, boolean initialValue) {
/*  58 */     return createBoolean(captionId, noTooltip(), initialValue, value -> {
/*     */         
/*     */         });
/*     */   } public static OptionInstance<Boolean> createBoolean(String captionId, TooltipSupplier<Boolean> tooltip, boolean initialValue) {
/*  62 */     return createBoolean(captionId, tooltip, initialValue, value -> {
/*     */         
/*     */         });
/*     */   } public static OptionInstance<Boolean> createBoolean(String captionId, TooltipSupplier<Boolean> tooltip, boolean initialValue, Consumer<Boolean> onValueUpdate) {
/*  66 */     return createBoolean(captionId, tooltip, BOOLEAN_TO_STRING, initialValue, onValueUpdate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static OptionInstance<Boolean> createBoolean(String captionId, TooltipSupplier<Boolean> tooltip, CaptionBasedToString<Boolean> toString, boolean initialValue, Consumer<Boolean> onValueUpdate) {
/*  76 */     return new OptionInstance<>(captionId, tooltip, toString, BOOLEAN_VALUES, initialValue, 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/*  81 */         onValueUpdate);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public OptionInstance(String captionId, TooltipSupplier<T> tooltip, CaptionBasedToString<T> toString, ValueSet<T> values, T initialValue, Consumer<T> onValueUpdate) {
/*  87 */     this(captionId, tooltip, toString, values, values.codec(), initialValue, onValueUpdate);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public OptionInstance(String captionId, TooltipSupplier<T> tooltip, CaptionBasedToString<T> toString, ValueSet<T> values, Codec<T> codec, T initialValue, Consumer<T> onValueUpdate) {
/*  99 */     this.caption = (Component)Component.translatable(captionId);
/* 100 */     this.tooltip = tooltip;
/* 101 */     this.toString = (value -> toString.toString(this.caption, toString));
/* 102 */     this.values = values;
/* 103 */     this.codec = codec;
/* 104 */     this.initialValue = initialValue;
/* 105 */     this.onValueUpdate = onValueUpdate;
/* 106 */     this.value = this.initialValue;
/*     */   }
/*     */   
/*     */   public static <T> TooltipSupplier<T> noTooltip() {
/* 110 */     return value -> null;
/*     */   }
/*     */   
/*     */   public static <T> TooltipSupplier<T> cachedConstantTooltip(Component tooltipComponent) {
/* 114 */     return value -> Tooltip.create(tooltipComponent);
/*     */   }
/*     */   
/*     */   public AbstractWidget createButton(Options options) {
/* 118 */     return createButton(options, 0, 0, 150);
/*     */   }
/*     */   
/*     */   public AbstractWidget createButton(Options options, int x, int y, int width) {
/* 122 */     return createButton(options, x, y, width, value -> {
/*     */         
/*     */         });
/*     */   } public AbstractWidget createButton(Options options, int x, int y, int width, Consumer<T> onValueChanged) {
/* 126 */     return this.values.createButton(this.tooltip, options, x, y, width, onValueChanged).apply(this);
/*     */   }
/*     */   
/*     */   public T get() {
/* 130 */     return this.value;
/*     */   }
/*     */   
/*     */   public Codec<T> codec() {
/* 134 */     return this.codec;
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 139 */     return this.caption.getString();
/*     */   }
/*     */   
/*     */   public void set(T value) {
/* 143 */     T newValue = this.values.validateValue(value).orElseGet(() -> {
/*     */           LOGGER.error("Illegal option value {} for {}", value, this.caption.getString());
/*     */           return this.initialValue;
/*     */         });
/* 147 */     if (!Minecraft.getInstance().isRunning()) {
/*     */       
/* 149 */       this.value = newValue;
/*     */       return;
/*     */     } 
/* 152 */     if (!Objects.equals(this.value, newValue)) {
/* 153 */       this.value = newValue;
/* 154 */       this.onValueUpdate.accept(this.value);
/*     */     } 
/*     */   }
/*     */   
/*     */   public ValueSet<T> values() {
/* 159 */     return this.values;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface SliderableValueSet<T>
/*     */     extends ValueSet<T>
/*     */   {
/*     */     double toSliderValue(T param1T);
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default Optional<T> next(T current) {
/* 174 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     default Optional<T> previous(T current) {
/* 178 */       return Optional.empty();
/*     */     }
/*     */     
/*     */     T fromSliderValue(double param1Double);
/*     */     
/*     */     default boolean applyValueImmediately() {
/* 184 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, Consumer<T> onValueChanged) {
/* 189 */       return instance -> new OptionInstance.OptionInstanceSliderButton<>(options, options, x, y, 20, onValueChanged, this, width, tooltip, applyValueImmediately());
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   static interface CycleableValueSet<T>
/*     */     extends ValueSet<T>
/*     */   {
/*     */     CycleButton.ValueListSupplier<T> valueListSupplier();
/*     */ 
/*     */     
/*     */     default ValueSetter<T> valueSetter() {
/* 201 */       return OptionInstance::set;
/*     */     }
/*     */ 
/*     */     
/*     */     default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, Consumer<T> onValueChanged) {
/* 206 */       return instance -> {
/*     */           Objects.requireNonNull(y);
/*     */           return CycleButton.builder(y.toString, y::get).withValues(valueListSupplier()).withTooltip(tooltip).create(tooltip, x, y, 20, y.caption, ());
/*     */         };
/*     */     }
/*     */     
/*     */     public static interface ValueSetter<T> {
/*     */       void set(OptionInstance<T> param2OptionInstance, T param2T);
/*     */     }
/*     */   }
/*     */   
/*     */   static interface SliderableOrCyclableValueSet<T>
/*     */     extends SliderableValueSet<T>, CycleableValueSet<T> {
/*     */     boolean createCycleButton();
/*     */     
/*     */     default Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> tooltip, Options options, int x, int y, int width, Consumer<T> onValueChanged) {
/* 222 */       if (createCycleButton()) {
/* 223 */         return super.createButton(tooltip, options, x, y, width, onValueChanged);
/*     */       }
/* 225 */       return super.createButton(tooltip, options, x, y, width, onValueChanged);
/*     */     } }
/*     */   
/*     */   public static final class AltEnum<T> extends Record implements CycleableValueSet<T> { private final List<T> values;
/*     */     private final List<T> altValues;
/*     */     private final BooleanSupplier altCondition;
/*     */     private final OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter;
/*     */     private final Codec<T> codec;
/*     */     
/* 234 */     public AltEnum(List<T> values, List<T> altValues, BooleanSupplier altCondition, OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter, Codec<T> codec) { this.values = values; this.altValues = altValues; this.altCondition = altCondition; this.valueSetter = valueSetter; this.codec = codec; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$AltEnum;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$AltEnum;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$AltEnum;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #234	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$AltEnum;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 234 */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$AltEnum<TT;>; } public List<T> values() { return this.values; } public List<T> altValues() { return this.altValues; } public BooleanSupplier altCondition() { return this.altCondition; } public OptionInstance.CycleableValueSet.ValueSetter<T> valueSetter() { return this.valueSetter; } public Codec<T> codec() { return this.codec; }
/*     */     
/*     */     public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 237 */       return CycleButton.ValueListSupplier.create(this.altCondition, this.values, this.altValues);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<T> validateValue(T value) {
/* 242 */       return (this.altCondition.getAsBoolean() ? this.altValues : this.values).contains(value) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */     } }
/*     */   public static final class Enum<T> extends Record implements CycleableValueSet<T> { private final List<T> values; private final Codec<T> codec;
/*     */     
/* 246 */     public Enum(List<T> values, Codec<T> codec) { this.values = values; this.codec = codec; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$Enum;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #246	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$Enum;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #246	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$Enum;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #246	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$Enum;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 246 */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$Enum<TT;>; } public List<T> values() { return this.values; } public Codec<T> codec() { return this.codec; }
/*     */     
/*     */     public Optional<T> validateValue(T value) {
/* 249 */       return this.values.contains(value) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 254 */       return CycleButton.ValueListSupplier.create(this.values);
/*     */     } }
/*     */   public static final class LazyEnum<T> extends Record implements CycleableValueSet<T> { private final Supplier<List<T>> values; private final Function<T, Optional<T>> validateValue; private final Codec<T> codec;
/*     */     
/* 258 */     public LazyEnum(Supplier<List<T>> values, Function<T, Optional<T>> validateValue, Codec<T> codec) { this.values = values; this.validateValue = validateValue; this.codec = codec; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$LazyEnum;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #258	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$LazyEnum;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #258	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$LazyEnum;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #258	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 258 */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$LazyEnum<TT;>; } public Supplier<List<T>> values() { return this.values; } public Function<T, Optional<T>> validateValue() { return this.validateValue; } public Codec<T> codec() { return this.codec; }
/*     */     
/*     */     public Optional<T> validateValue(T value) {
/* 261 */       return this.validateValue.apply(value);
/*     */     }
/*     */ 
/*     */     
/*     */     public CycleButton.ValueListSupplier<T> valueListSupplier() {
/* 266 */       return CycleButton.ValueListSupplier.create(this.values.get());
/*     */     } }
/*     */ 
/*     */   
/*     */   public static final class OptionInstanceSliderButton<N> extends AbstractOptionSliderButton implements ResettableOptionWidget {
/*     */     private final OptionInstance<N> instance;
/*     */     private final OptionInstance.SliderableValueSet<N> values;
/*     */     private final OptionInstance.TooltipSupplier<N> tooltipSupplier;
/*     */     private final Consumer<N> onValueChanged;
/*     */     private Long delayedApplyAt;
/*     */     private final boolean applyValueImmediately;
/*     */     
/*     */     private OptionInstanceSliderButton(Options options, int x, int y, int width, int height, OptionInstance<N> instance, OptionInstance.SliderableValueSet<N> values, OptionInstance.TooltipSupplier<N> tooltipSupplier, Consumer<N> onValueChanged, boolean applyValueImmediately) {
/* 279 */       super(options, x, y, width, height, values.toSliderValue(instance.get()));
/* 280 */       this.instance = instance;
/* 281 */       this.values = values;
/* 282 */       this.tooltipSupplier = tooltipSupplier;
/* 283 */       this.onValueChanged = onValueChanged;
/* 284 */       this.applyValueImmediately = applyValueImmediately;
/* 285 */       updateMessage();
/*     */     }
/*     */ 
/*     */     
/*     */     protected void updateMessage() {
/* 290 */       setMessage(this.instance.toString.apply(this.values.fromSliderValue(this.value)));
/* 291 */       setTooltip(this.tooltipSupplier.apply(this.values.fromSliderValue(this.value)));
/*     */     }
/*     */ 
/*     */     
/*     */     protected void applyValue() {
/* 296 */       if (this.applyValueImmediately) {
/* 297 */         applyUnsavedValue();
/*     */       } else {
/* 299 */         this.delayedApplyAt = Util.getMillis() + 600L;
/*     */       } 
/*     */     }
/*     */     
/*     */     public void applyUnsavedValue() {
/* 304 */       N sliderValue = this.values.fromSliderValue(this.value);
/* 305 */       if (!Objects.equals(sliderValue, this.instance.get())) {
/* 306 */         this.instance.set(sliderValue);
/* 307 */         this.onValueChanged.accept(this.instance.get());
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void resetValue() {
/* 313 */       if (this.value != this.values.toSliderValue(this.instance.get())) {
/* 314 */         this.value = this.values.toSliderValue(this.instance.get());
/* 315 */         this.delayedApplyAt = null;
/* 316 */         updateMessage();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float a) {
/* 322 */       super.renderWidget(graphics, mouseX, mouseY, a);
/*     */       
/* 324 */       if (this.delayedApplyAt != null && Util.getMillis() >= this.delayedApplyAt) {
/* 325 */         this.delayedApplyAt = null;
/* 326 */         applyUnsavedValue();
/* 327 */         resetValue();
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/*     */     public void onRelease(MouseButtonEvent event) {
/* 333 */       super.onRelease(event);
/* 334 */       if (this.applyValueImmediately) {
/* 335 */         resetValue();
/*     */       }
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean keyPressed(KeyEvent event) {
/* 341 */       if (event.isSelection()) {
/* 342 */         this.canChangeValue = !this.canChangeValue;
/* 343 */         return true;
/*     */       } 
/* 345 */       if (this.canChangeValue) {
/* 346 */         boolean left = event.isLeft();
/* 347 */         boolean right = event.isRight();
/* 348 */         if (left) {
/* 349 */           Optional<N> previous = this.values.previous(this.values.fromSliderValue(this.value));
/* 350 */           if (previous.isPresent()) {
/* 351 */             setValue(this.values.toSliderValue(previous.get()));
/* 352 */             return true;
/*     */           } 
/*     */         } 
/* 355 */         if (right) {
/* 356 */           Optional<N> next = this.values.next(this.values.fromSliderValue(this.value));
/* 357 */           if (next.isPresent()) {
/* 358 */             setValue(this.values.toSliderValue(next.get()));
/* 359 */             return true;
/*     */           } 
/*     */         } 
/* 362 */         if (left || right) {
/* 363 */           float direction = left ? -1.0F : 1.0F;
/* 364 */           setValue(this.value + (direction / (this.width - 8)));
/* 365 */           return true;
/*     */         } 
/*     */       } 
/* 368 */       return false;
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   static interface IntRangeBase
/*     */     extends SliderableValueSet<Integer>
/*     */   {
/*     */     default Optional<Integer> next(Integer current) {
/* 379 */       return Optional.of(current + 1);
/*     */     }
/*     */ 
/*     */     
/*     */     default Optional<Integer> previous(Integer current) {
/* 384 */       return Optional.of(current - 1);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     default double toSliderValue(Integer value) {
/* 391 */       if (value == minInclusive())
/* 392 */         return 0.0D; 
/* 393 */       if (value == maxInclusive()) {
/* 394 */         return 1.0D;
/*     */       }
/* 396 */       return Mth.map(value + 0.5D, minInclusive(), maxInclusive() + 1.0D, 0.0D, 1.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     default Integer fromSliderValue(double slider) {
/* 401 */       if (slider >= 1.0D) {
/* 402 */         slider = 0.9999899864196777D;
/*     */       }
/* 404 */       return Mth.floor(Mth.map(slider, 0.0D, 1.0D, minInclusive(), maxInclusive() + 1.0D));
/*     */     }
/*     */     
/*     */     default <R> OptionInstance.SliderableValueSet<R> xmap(final IntFunction<? extends R> to, final ToIntFunction<? super R> from, final boolean discrete) {
/* 408 */       return new OptionInstance.SliderableValueSet<R>()
/*     */         {
/*     */           public Optional<R> validateValue(R value) {
/* 411 */             Objects.requireNonNull(to); return OptionInstance.IntRangeBase.this.validateValue(from.applyAsInt(value)).map(to::apply);
/*     */           }
/*     */ 
/*     */           
/*     */           public double toSliderValue(R value) {
/* 416 */             return OptionInstance.IntRangeBase.this.toSliderValue(from.applyAsInt(value));
/*     */           }
/*     */ 
/*     */           
/*     */           public Optional<R> next(R current) {
/* 421 */             if (!discrete) {
/* 422 */               return Optional.empty();
/*     */             }
/* 424 */             int currentIndex = from.applyAsInt(current);
/* 425 */             return Optional.of(to.apply((Integer)OptionInstance.IntRangeBase.this.validateValue(currentIndex + 1).orElse(currentIndex)));
/*     */           }
/*     */ 
/*     */           
/*     */           public Optional<R> previous(R current) {
/* 430 */             if (!discrete) {
/* 431 */               return Optional.empty();
/*     */             }
/* 433 */             int currentIndex = from.applyAsInt(current);
/* 434 */             return Optional.of(to.apply((Integer)OptionInstance.IntRangeBase.this.validateValue(currentIndex - 1).orElse(currentIndex)));
/*     */           }
/*     */ 
/*     */           
/*     */           public R fromSliderValue(double slider) {
/* 439 */             return to.apply(OptionInstance.IntRangeBase.this.fromSliderValue(slider));
/*     */           }
/*     */           
/*     */           public Codec<R> codec()
/*     */           {
/* 444 */             Objects.requireNonNull(to); Objects.requireNonNull(from); return OptionInstance.IntRangeBase.this.codec().xmap(to::apply, from::applyAsInt); } }; } int minInclusive(); int maxInclusive(); } class null implements SliderableValueSet<R> { public Optional<R> validateValue(R value) { Objects.requireNonNull(to); return OptionInstance.IntRangeBase.this.validateValue(from.applyAsInt(value)).map(to::apply); } public double toSliderValue(R value) { return OptionInstance.IntRangeBase.this.toSliderValue(from.applyAsInt(value)); } public Codec<R> codec() { Objects.requireNonNull(to); Objects.requireNonNull(from); return OptionInstance.IntRangeBase.this.codec().xmap(to::apply, from::applyAsInt); } public Optional<R> next(R current) { if (!discrete)
/*     */         return Optional.empty();  int currentIndex = from.applyAsInt(current); return Optional.of(to.apply((Integer)OptionInstance.IntRangeBase.this.validateValue(currentIndex + 1).orElse(currentIndex))); } public Optional<R> previous(R current) { if (!discrete)
/*     */         return Optional.empty();  int currentIndex = from.applyAsInt(current);
/*     */       return Optional.of(to.apply((Integer)OptionInstance.IntRangeBase.this.validateValue(currentIndex - 1).orElse(currentIndex))); } public R fromSliderValue(double slider) { return to.apply(OptionInstance.IntRangeBase.this.fromSliderValue(slider)); } }
/*     */    public static final class IntRange extends Record implements IntRangeBase
/*     */   {
/* 450 */     private final int minInclusive; public IntRange(int minInclusive, int maxInclusive, boolean applyValueImmediately) { this.minInclusive = minInclusive; this.maxInclusive = maxInclusive; this.applyValueImmediately = applyValueImmediately; } private final int maxInclusive; private final boolean applyValueImmediately; public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$IntRange;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #450	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$IntRange; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$IntRange;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #450	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$IntRange; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$IntRange;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #450	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$IntRange;
/* 450 */       //   0	8	1	o	Ljava/lang/Object; } public int minInclusive() { return this.minInclusive; } public int maxInclusive() { return this.maxInclusive; } public boolean applyValueImmediately() { return this.applyValueImmediately; }
/*     */      public IntRange(int minInclusive, int maxInclusive) {
/* 452 */       this(minInclusive, maxInclusive, true);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<Integer> validateValue(Integer value) {
/* 457 */       return (value.compareTo(minInclusive()) >= 0 && value.compareTo(maxInclusive()) <= 0) ? Optional.<Integer>of(value) : Optional.<Integer>empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public Codec<Integer> codec() {
/* 462 */       return Codec.intRange(this.minInclusive, this.maxInclusive + 1);
/*     */     } }
/*     */   public static final class ClampingLazyMaxIntRange extends Record implements IntRangeBase, SliderableOrCyclableValueSet<Integer> { private final int minInclusive; private final IntSupplier maxSupplier; private final int encodableMaxInclusive;
/*     */     
/* 466 */     public ClampingLazyMaxIntRange(int minInclusive, IntSupplier maxSupplier, int encodableMaxInclusive) { this.minInclusive = minInclusive; this.maxSupplier = maxSupplier; this.encodableMaxInclusive = encodableMaxInclusive; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #466	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #466	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #466	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$ClampingLazyMaxIntRange;
/* 466 */       //   0	8	1	o	Ljava/lang/Object; } public int minInclusive() { return this.minInclusive; } public IntSupplier maxSupplier() { return this.maxSupplier; } public int encodableMaxInclusive() { return this.encodableMaxInclusive; }
/*     */     
/*     */     public Optional<Integer> validateValue(Integer value) {
/* 469 */       return Optional.of(Mth.clamp(value, minInclusive(), maxInclusive()));
/*     */     }
/*     */ 
/*     */     
/*     */     public int maxInclusive() {
/* 474 */       return this.maxSupplier.getAsInt();
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Codec<Integer> codec() {
/* 480 */       return Codec.INT.validate(value -> {
/*     */             int maxExclusive = this.encodableMaxInclusive + 1;
/* 482 */             return (value.compareTo(this.minInclusive) >= 0 && value.compareTo(maxExclusive) <= 0) ? DataResult.success(value) : DataResult.error((), value);
/*     */           });
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public boolean createCycleButton() {
/* 491 */       return true;
/*     */     }
/*     */ 
/*     */     
/*     */     public CycleButton.ValueListSupplier<Integer> valueListSupplier() {
/* 496 */       return CycleButton.ValueListSupplier.create(IntStream.range(this.minInclusive, maxInclusive() + 1).boxed().toList());
/*     */     } }
/*     */   public static final class SliderableEnum<T> extends Record implements SliderableValueSet<T> { private final List<T> values; private final Codec<T> codec;
/*     */     
/* 500 */     public SliderableEnum(List<T> values, Codec<T> codec) { this.values = values; this.codec = codec; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/OptionInstance$SliderableEnum;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #500	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/OptionInstance$SliderableEnum;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #500	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/*     */       //   0	7	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/OptionInstance$SliderableEnum;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #500	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum;
/*     */       //   0	8	1	o	Ljava/lang/Object;
/*     */       // Local variable type table:
/*     */       //   start	length	slot	name	signature
/* 500 */       //   0	8	0	this	Lnet/minecraft/client/OptionInstance$SliderableEnum<TT;>; } public List<T> values() { return this.values; } public Codec<T> codec() { return this.codec; }
/*     */ 
/*     */ 
/*     */     
/*     */     public double toSliderValue(T value) {
/* 505 */       if (value == this.values.getFirst())
/* 506 */         return 0.0D; 
/* 507 */       if (value == this.values.getLast()) {
/* 508 */         return 1.0D;
/*     */       }
/* 510 */       return Mth.map(this.values.indexOf(value), 0.0D, (this.values.size() - 1), 0.0D, 1.0D);
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<T> next(T current) {
/* 515 */       int currentIntex = this.values.indexOf(current);
/* 516 */       int nextIndex = Mth.clamp(currentIntex + 1, 0, this.values.size() - 1);
/* 517 */       return Optional.of(this.values.get(nextIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<T> previous(T current) {
/* 522 */       int currentIntex = this.values.indexOf(current);
/* 523 */       int previousIndex = Mth.clamp(currentIntex - 1, 0, this.values.size() - 1);
/* 524 */       return Optional.of(this.values.get(previousIndex));
/*     */     }
/*     */ 
/*     */     
/*     */     public T fromSliderValue(double slider) {
/* 529 */       if (slider >= 1.0D) {
/* 530 */         slider = 0.9999899864196777D;
/*     */       }
/* 532 */       int index = Mth.floor(Mth.map(slider, 0.0D, 1.0D, 0.0D, this.values.size()));
/* 533 */       return this.values.get(Mth.clamp(index, 0, this.values.size() - 1));
/*     */     }
/*     */ 
/*     */     
/*     */     public Optional<T> validateValue(T value) {
/* 538 */       int index = this.values.indexOf(value);
/* 539 */       return (index > -1) ? Optional.<T>of(value) : Optional.<T>empty();
/*     */     } }
/*     */ 
/*     */   
/*     */   public enum UnitDouble implements SliderableValueSet<Double> {
/* 544 */     INSTANCE;
/*     */ 
/*     */     
/*     */     public Optional<Double> validateValue(Double value) {
/* 548 */       return (value >= 0.0D && value <= 1.0D) ? Optional.<Double>of(value) : Optional.<Double>empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public double toSliderValue(Double value) {
/* 553 */       return value;
/*     */     }
/*     */ 
/*     */     
/*     */     public Double fromSliderValue(double slider) {
/* 558 */       return slider;
/*     */     }
/*     */     
/*     */     public <R> OptionInstance.SliderableValueSet<R> xmap(final DoubleFunction<? extends R> to, final ToDoubleFunction<? super R> from) {
/* 562 */       return new OptionInstance.SliderableValueSet<R>()
/*     */         {
/*     */           public Optional<R> validateValue(R value) {
/* 565 */             Objects.requireNonNull(to); return OptionInstance.UnitDouble.this.validateValue(from.applyAsDouble(value)).map(to::apply);
/*     */           }
/*     */ 
/*     */           
/*     */           public double toSliderValue(R value) {
/* 570 */             return OptionInstance.UnitDouble.this.toSliderValue(from.applyAsDouble(value));
/*     */           }
/*     */ 
/*     */           
/*     */           public R fromSliderValue(double slider) {
/* 575 */             return to.apply(OptionInstance.UnitDouble.this.fromSliderValue(slider));
/*     */           }
/*     */ 
/*     */           
/*     */           public Codec<R> codec() {
/* 580 */             Objects.requireNonNull(to); Objects.requireNonNull(from); return OptionInstance.UnitDouble.this.codec().xmap(to::apply, from::applyAsDouble);
/*     */           }
/*     */         };
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public Codec<Double> codec() {
/* 588 */       return Codec.withAlternative(
/* 589 */           Codec.doubleRange(0.0D, 1.0D), (Codec)Codec.BOOL, b -> b ? 1.0D : 0.0D);
/*     */     }
/*     */   }
/*     */   
/*     */   class null implements SliderableValueSet<R> {
/*     */     public Optional<R> validateValue(R value) {
/*     */       Objects.requireNonNull(to);
/*     */       return OptionInstance.UnitDouble.this.validateValue(from.applyAsDouble(value)).map(to::apply);
/*     */     }
/*     */     
/*     */     public double toSliderValue(R value) {
/*     */       return OptionInstance.UnitDouble.this.toSliderValue(from.applyAsDouble(value));
/*     */     }
/*     */     
/*     */     public R fromSliderValue(double slider) {
/*     */       return to.apply(OptionInstance.UnitDouble.this.fromSliderValue(slider));
/*     */     }
/*     */     
/*     */     public Codec<R> codec() {
/*     */       Objects.requireNonNull(to);
/*     */       Objects.requireNonNull(from);
/*     */       return OptionInstance.UnitDouble.this.codec().xmap(to::apply, from::applyAsDouble);
/*     */     }
/*     */   }
/*     */   
/*     */   @FunctionalInterface
/*     */   public static interface TooltipSupplier<T> {
/*     */     Tooltip apply(T param1T);
/*     */   }
/*     */   
/*     */   public static interface CaptionBasedToString<T> {
/*     */     Component toString(Component param1Component, T param1T);
/*     */   }
/*     */   
/*     */   static interface ValueSet<T> {
/*     */     Function<OptionInstance<T>, AbstractWidget> createButton(OptionInstance.TooltipSupplier<T> param1TooltipSupplier, Options param1Options, int param1Int1, int param1Int2, int param1Int3, Consumer<T> param1Consumer);
/*     */     
/*     */     Optional<T> validateValue(T param1T);
/*     */     
/*     */     Codec<T> codec();
/*     */   }
/*     */   
/*     */   public static interface ValueSetter<T> {
/*     */     void set(OptionInstance<T> param1OptionInstance, T param1T);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/OptionInstance.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */