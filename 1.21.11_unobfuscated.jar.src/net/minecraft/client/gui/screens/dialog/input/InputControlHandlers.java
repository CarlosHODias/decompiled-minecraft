/*     */ package net.minecraft.client.gui.screens.dialog.input;
/*     */ 
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.components.AbstractSliderButton;
/*     */ import net.minecraft.client.gui.components.Checkbox;
/*     */ import net.minecraft.client.gui.components.CycleButton;
/*     */ import net.minecraft.client.gui.components.EditBox;
/*     */ import net.minecraft.client.gui.components.MultiLineEditBox;
/*     */ import net.minecraft.client.gui.layouts.CommonLayouts;
/*     */ import net.minecraft.client.gui.layouts.LayoutElement;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.nbt.ByteTag;
/*     */ import net.minecraft.nbt.FloatTag;
/*     */ import net.minecraft.nbt.StringTag;
/*     */ import net.minecraft.nbt.Tag;
/*     */ import net.minecraft.network.chat.CommonComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.server.dialog.action.Action;
/*     */ import net.minecraft.server.dialog.input.BooleanInput;
/*     */ import net.minecraft.server.dialog.input.InputControl;
/*     */ import net.minecraft.server.dialog.input.NumberRangeInput;
/*     */ import net.minecraft.server.dialog.input.SingleOptionInput;
/*     */ import net.minecraft.server.dialog.input.TextInput;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class InputControlHandlers
/*     */ {
/*  36 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*  38 */   private static final Map<MapCodec<? extends InputControl>, InputControlHandler<?>> HANDLERS = new HashMap<>();
/*     */   
/*     */   private static <T extends InputControl> void register(MapCodec<T> type, InputControlHandler<? super T> handler) {
/*  41 */     HANDLERS.put(type, handler);
/*     */   }
/*     */ 
/*     */   
/*     */   private static <T extends InputControl> InputControlHandler<T> get(T inputControl) {
/*  46 */     return (InputControlHandler<T>)HANDLERS.get(inputControl.mapCodec());
/*     */   }
/*     */   
/*     */   public static <T extends InputControl> void createHandler(T inputControl, Screen screen, InputControlHandler.Output outputConsumer) {
/*  50 */     InputControlHandler<T> handler = get(inputControl);
/*  51 */     if (handler == null) {
/*  52 */       LOGGER.warn("Unrecognized input control {}", inputControl);
/*     */       
/*     */       return;
/*     */     } 
/*  56 */     handler.addControl(inputControl, screen, outputConsumer);
/*     */   }
/*     */   
/*     */   public static void bootstrap() {
/*  60 */     register(TextInput.MAP_CODEC, (InputControlHandler<? super InputControl>)new TextInputHandler());
/*  61 */     register(SingleOptionInput.MAP_CODEC, (InputControlHandler<? super InputControl>)new SingleOptionHandler());
/*  62 */     register(BooleanInput.MAP_CODEC, (InputControlHandler<? super InputControl>)new BooleanHandler());
/*  63 */     register(NumberRangeInput.MAP_CODEC, (InputControlHandler<? super InputControl>)new NumberRangeHandler());
/*     */   }
/*     */   
/*     */   private static class TextInputHandler implements InputControlHandler<TextInput> {
/*     */     public void addControl(TextInput input, Screen screen, InputControlHandler.Output output) { EditBox editBox;
/*     */       final Supplier<String> getter;
/*  69 */       Font font = screen.getFont();
/*     */ 
/*     */       
/*  72 */       if (input.multiline().isPresent()) {
/*  73 */         TextInput.MultilineOptions multiline = input.multiline().get();
/*  74 */         int computedHeight = (Integer)multiline.height().orElseGet(() -> {
/*     */               int lineCountToFit = (Integer)multiline.maxLines().orElse(4); Objects.requireNonNull(font);
/*     */               return Math.min(9 * lineCountToFit + 8, 512);
/*     */             });
/*  78 */         MultiLineEditBox multiLineEditBox2 = MultiLineEditBox.builder().build(font, input.width(), computedHeight, CommonComponents.EMPTY);
/*  79 */         multiLineEditBox2.setCharacterLimit(input.maxLength());
/*  80 */         Objects.requireNonNull(multiLineEditBox2); multiline.maxLines().ifPresent(multiLineEditBox2::setLineLimit);
/*  81 */         multiLineEditBox2.setValue(input.initial());
/*  82 */         MultiLineEditBox multiLineEditBox1 = multiLineEditBox2;
/*  83 */         Objects.requireNonNull(multiLineEditBox2); getter = multiLineEditBox2::getValue;
/*     */       } else {
/*  85 */         EditBox editBox1 = new EditBox(font, input.width(), 20, input.label());
/*  86 */         editBox1.setMaxLength(input.maxLength());
/*  87 */         editBox1.setValue(input.initial());
/*  88 */         editBox = editBox1;
/*  89 */         Objects.requireNonNull(editBox1); getter = editBox1::getValue;
/*     */       } 
/*  91 */       LayoutElement wrappedControl = input.labelVisible() ? (LayoutElement)CommonLayouts.labeledElement(font, (LayoutElement)editBox, input.label()) : (LayoutElement)editBox;
/*  92 */       output.accept(wrappedControl, new Action.ValueGetter(this)
/*     */           {
/*     */             public String asTemplateSubstitution() {
/*  95 */               return StringTag.escapeWithoutQuotes(getter.get());
/*     */             }
/*     */             
/*     */             public Tag asTag()
/*     */             {
/* 100 */               return (Tag)StringTag.valueOf(getter.get()); } }); } } class null implements Action.ValueGetter { null(InputControlHandlers.TextInputHandler this$0) {} public String asTemplateSubstitution() { return StringTag.escapeWithoutQuotes(getter.get()); } public Tag asTag() { return (Tag)StringTag.valueOf(getter.get()); }
/*     */      }
/*     */ 
/*     */ 
/*     */   
/*     */   private static class SingleOptionHandler
/*     */     implements InputControlHandler<SingleOptionInput>
/*     */   {
/*     */     public void addControl(SingleOptionInput input, Screen screen, InputControlHandler.Output output) {
/* 109 */       SingleOptionInput.Entry initial = input.initial().orElse(input.entries().getFirst());
/* 110 */       CycleButton.Builder<SingleOptionInput.Entry> controlBuilder = CycleButton.builder(SingleOptionInput.Entry::displayOrDefault, initial)
/* 111 */         .withValues(input.entries())
/* 112 */         .displayState(!input.labelVisible() ? CycleButton.DisplayState.VALUE : CycleButton.DisplayState.NAME_AND_VALUE);
/*     */       
/* 114 */       CycleButton<SingleOptionInput.Entry> control = controlBuilder.create(0, 0, input.width(), 20, input.label());
/*     */       
/* 116 */       output.accept((LayoutElement)control, Action.ValueGetter.of(() -> ((SingleOptionInput.Entry)control.getValue()).id()));
/*     */     }
/*     */   }
/*     */   
/*     */   private static class BooleanHandler
/*     */     implements InputControlHandler<BooleanInput>
/*     */   {
/* 123 */     public void addControl(final BooleanInput input, Screen screen, InputControlHandler.Output output) { Font font = screen.getFont();
/* 124 */       final Checkbox control = Checkbox.builder(input.label(), font).selected(input.initial()).build();
/* 125 */       output.accept((LayoutElement)control, new Action.ValueGetter(this)
/*     */           {
/*     */             public String asTemplateSubstitution() {
/* 128 */               return control.selected() ? input.onTrue() : input.onFalse();
/*     */             }
/*     */             
/*     */             public Tag asTag()
/*     */             {
/* 133 */               return (Tag)ByteTag.valueOf(control.selected()); } }); } } class null implements Action.ValueGetter { null(InputControlHandlers.BooleanHandler this$0) {} public String asTemplateSubstitution() { return control.selected() ? input.onTrue() : input.onFalse(); } public Tag asTag() { return (Tag)ByteTag.valueOf(control.selected()); }
/*     */      }
/*     */ 
/*     */   
/*     */   private static class NumberRangeHandler
/*     */     implements InputControlHandler<NumberRangeInput> {
/*     */     private static class SliderImpl
/*     */       extends AbstractSliderButton {
/*     */       private final NumberRangeInput input;
/*     */       
/*     */       private SliderImpl(NumberRangeInput input, double initialSliderValue) {
/* 144 */         super(0, 0, input.width(), 20, computeMessage(input, initialSliderValue), initialSliderValue);
/* 145 */         this.input = input;
/*     */       }
/*     */ 
/*     */       
/*     */       protected void updateMessage() {
/* 150 */         setMessage(computeMessage(this.input, this.value));
/*     */       }
/*     */ 
/*     */       
/*     */       protected void applyValue() {}
/*     */ 
/*     */       
/*     */       public String stringValueToSend() {
/* 158 */         return sliderValueToString(this.input, this.value);
/*     */       }
/*     */       
/*     */       public float floatValueToSend() {
/* 162 */         return scaledValue(this.input, this.value);
/*     */       }
/*     */       
/*     */       private static float scaledValue(NumberRangeInput input, double sliderValue) {
/* 166 */         return input.rangeInfo().computeScaledValue((float)sliderValue);
/*     */       }
/*     */       
/*     */       private static String sliderValueToString(NumberRangeInput input, double sliderValue) {
/* 170 */         return valueToString(scaledValue(input, sliderValue));
/*     */       }
/*     */       
/*     */       private static Component computeMessage(NumberRangeInput input, double sliderValue) {
/* 174 */         return input.computeLabel(sliderValueToString(input, sliderValue));
/*     */       }
/*     */       
/*     */       private static String valueToString(float v) {
/* 178 */         int intV = (int)v;
/* 179 */         if (intV == v)
/*     */         {
/* 181 */           return Integer.toString(intV);
/*     */         }
/* 183 */         return Float.toString(v);
/*     */       }
/*     */     }
/*     */     
/*     */     public void addControl(NumberRangeInput input, Screen screen, InputControlHandler.Output output)
/*     */     {
/* 189 */       float initialValue = input.rangeInfo().initialSliderValue();
/* 190 */       final SliderImpl control = new SliderImpl(input, initialValue);
/* 191 */       output.accept((LayoutElement)control, new Action.ValueGetter(this)
/*     */           {
/*     */             public String asTemplateSubstitution() {
/* 194 */               return control.stringValueToSend();
/*     */             }
/*     */             
/*     */             public Tag asTag()
/*     */             {
/* 199 */               return (Tag)FloatTag.valueOf(control.floatValueToSend()); } }); } } private static class SliderImpl extends AbstractSliderButton { private final NumberRangeInput input; private SliderImpl(NumberRangeInput input, double initialSliderValue) { super(0, 0, input.width(), 20, computeMessage(input, initialSliderValue), initialSliderValue); this.input = input; } protected void updateMessage() { setMessage(computeMessage(this.input, this.value)); } protected void applyValue() {} public String stringValueToSend() { return sliderValueToString(this.input, this.value); } public float floatValueToSend() { return scaledValue(this.input, this.value); } private static float scaledValue(NumberRangeInput input, double sliderValue) { return input.rangeInfo().computeScaledValue((float)sliderValue); } private static String sliderValueToString(NumberRangeInput input, double sliderValue) { return valueToString(scaledValue(input, sliderValue)); } private static Component computeMessage(NumberRangeInput input, double sliderValue) { return input.computeLabel(sliderValueToString(input, sliderValue)); } private static String valueToString(float v) { int intV = (int)v; if (intV == v) return Integer.toString(intV);  return Float.toString(v); } } class null implements Action.ValueGetter { public Tag asTag() { return (Tag)FloatTag.valueOf(control.floatValueToSend()); }
/*     */ 
/*     */     
/*     */     null(InputControlHandlers.NumberRangeHandler this$0) {}
/*     */     
/*     */     public String asTemplateSubstitution() {
/*     */       return control.stringValueToSend();
/*     */     } }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/gui/screens/dialog/input/InputControlHandlers.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */