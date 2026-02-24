/*     */ package net.minecraft.client.data.models.model;
/*     */ 
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.client.color.item.Constant;
/*     */ import net.minecraft.client.color.item.ItemTintSource;
/*     */ import net.minecraft.client.renderer.item.BlockModelWrapper;
/*     */ import net.minecraft.client.renderer.item.CompositeModel;
/*     */ import net.minecraft.client.renderer.item.ConditionalItemModel;
/*     */ import net.minecraft.client.renderer.item.ItemModel;
/*     */ import net.minecraft.client.renderer.item.RangeSelectItemModel;
/*     */ import net.minecraft.client.renderer.item.SelectItemModel;
/*     */ import net.minecraft.client.renderer.item.SpecialModelWrapper;
/*     */ import net.minecraft.client.renderer.item.properties.conditional.ConditionalItemModelProperty;
/*     */ import net.minecraft.client.renderer.item.properties.conditional.HasComponent;
/*     */ import net.minecraft.client.renderer.item.properties.conditional.IsUsingItem;
/*     */ import net.minecraft.client.renderer.item.properties.numeric.RangeSelectItemModelProperty;
/*     */ import net.minecraft.client.renderer.item.properties.select.ContextDimension;
/*     */ import net.minecraft.client.renderer.item.properties.select.ItemBlockState;
/*     */ import net.minecraft.client.renderer.item.properties.select.LocalTime;
/*     */ import net.minecraft.client.renderer.item.properties.select.SelectItemModelProperty;
/*     */ import net.minecraft.client.renderer.special.SpecialModelRenderer;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.SpecialDates;
/*     */ import net.minecraft.world.level.Level;
/*     */ import net.minecraft.world.level.block.state.properties.Property;
/*     */ 
/*     */ public class ItemModelUtils {
/*     */   public static ItemModel.Unbaked plainModel(Identifier id) {
/*  35 */     return (ItemModel.Unbaked)new BlockModelWrapper.Unbaked(id, List.of());
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked tintedModel(Identifier id, ItemTintSource... tints) {
/*  39 */     return (ItemModel.Unbaked)new BlockModelWrapper.Unbaked(id, List.of(tints));
/*     */   }
/*     */   
/*     */   public static ItemTintSource constantTint(int color) {
/*  43 */     return (ItemTintSource)new Constant(color);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked composite(ItemModel.Unbaked... models) {
/*  47 */     return (ItemModel.Unbaked)new CompositeModel.Unbaked(List.of(models));
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked specialModel(Identifier base, SpecialModelRenderer.Unbaked model) {
/*  51 */     return (ItemModel.Unbaked)new SpecialModelWrapper.Unbaked(base, model);
/*     */   }
/*     */   
/*     */   public static RangeSelectItemModel.Entry override(ItemModel.Unbaked model, float value) {
/*  55 */     return new RangeSelectItemModel.Entry(value, model);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, ItemModel.Unbaked fallback, RangeSelectItemModel.Entry... entries) {
/*  59 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(property, 1.0F, List.of(entries), Optional.of(fallback));
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, float scale, ItemModel.Unbaked fallback, RangeSelectItemModel.Entry... entries) {
/*  63 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(property, scale, List.of(entries), Optional.of(fallback));
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, ItemModel.Unbaked fallback, List<RangeSelectItemModel.Entry> entries) {
/*  67 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(property, 1.0F, entries, Optional.of(fallback));
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, List<RangeSelectItemModel.Entry> entries) {
/*  71 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(property, 1.0F, entries, Optional.empty());
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked rangeSelect(RangeSelectItemModelProperty property, float scale, List<RangeSelectItemModel.Entry> entries) {
/*  75 */     return (ItemModel.Unbaked)new RangeSelectItemModel.Unbaked(property, scale, entries, Optional.empty());
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked conditional(ConditionalItemModelProperty property, ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) {
/*  79 */     return (ItemModel.Unbaked)new ConditionalItemModel.Unbaked(property, onTrue, onFalse);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> SelectItemModel.SwitchCase<T> when(T value, ItemModel.Unbaked model) {
/*  87 */     return new SelectItemModel.SwitchCase(List.of(value), model);
/*     */   }
/*     */   
/*     */   public static <T> SelectItemModel.SwitchCase<T> when(List<T> values, ItemModel.Unbaked model) {
/*  91 */     return new SelectItemModel.SwitchCase(values, model);
/*     */   }
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> ItemModel.Unbaked select(SelectItemModelProperty<T> property, ItemModel.Unbaked fallback, SelectItemModel.SwitchCase<T>... cases) {
/*  96 */     return select(property, fallback, List.of(cases));
/*     */   }
/*     */   
/*     */   public static <T> ItemModel.Unbaked select(SelectItemModelProperty<T> property, ItemModel.Unbaked fallback, List<SelectItemModel.SwitchCase<T>> cases) {
/* 100 */     return (ItemModel.Unbaked)new SelectItemModel.Unbaked(new SelectItemModel.UnbakedSwitch(property, cases), 
/*     */         
/* 102 */         Optional.of(fallback));
/*     */   }
/*     */ 
/*     */   
/*     */   @SafeVarargs
/*     */   public static <T> ItemModel.Unbaked select(SelectItemModelProperty<T> property, SelectItemModel.SwitchCase<T>... cases) {
/* 108 */     return select(property, List.of(cases));
/*     */   }
/*     */   
/*     */   public static <T> ItemModel.Unbaked select(SelectItemModelProperty<T> property, List<SelectItemModel.SwitchCase<T>> cases) {
/* 112 */     return (ItemModel.Unbaked)new SelectItemModel.Unbaked(new SelectItemModel.UnbakedSwitch(property, cases), 
/*     */         
/* 114 */         Optional.empty());
/*     */   }
/*     */ 
/*     */   
/*     */   public static ConditionalItemModelProperty isUsingItem() {
/* 119 */     return (ConditionalItemModelProperty)new IsUsingItem();
/*     */   }
/*     */   
/*     */   public static ConditionalItemModelProperty hasComponent(DataComponentType<?> component) {
/* 123 */     return (ConditionalItemModelProperty)new HasComponent(component, false);
/*     */   }
/*     */   
/*     */   public static ItemModel.Unbaked inOverworld(ItemModel.Unbaked ifTrue, ItemModel.Unbaked ifFalse) {
/* 127 */     return select((SelectItemModelProperty<?>)new ContextDimension(), ifFalse, (SelectItemModel.SwitchCase<?>[])new SelectItemModel.SwitchCase[] {
/*     */ 
/*     */           
/* 130 */           when(Level.OVERWORLD, ifTrue)
/*     */         });
/*     */   }
/*     */   
/*     */   public static <T extends Comparable<T>> ItemModel.Unbaked selectBlockItemProperty(Property<T> property, ItemModel.Unbaked fallback, Map<T, ItemModel.Unbaked> cases) {
/* 135 */     List<SelectItemModel.SwitchCase<String>> entries = cases.entrySet().stream().sorted(Map.Entry.comparingByKey()).map(e -> {
/*     */           String valueName = property.getName((Comparable)e.getKey());
/*     */           return new SelectItemModel.SwitchCase(List.of(valueName), (ItemModel.Unbaked)e.getValue());
/* 138 */         }).toList();
/*     */     
/* 140 */     return select((SelectItemModelProperty<String>)new ItemBlockState(
/* 141 */           property.getName()), fallback, entries);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static ItemModel.Unbaked isXmas(ItemModel.Unbaked onTrue, ItemModel.Unbaked onFalse) {
/* 149 */     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd", Locale.ROOT);
/* 150 */     Objects.requireNonNull(formatter); List<String> days = SpecialDates.CHRISTMAS_RANGE.stream().map(formatter::format).toList();
/*     */     
/* 152 */     return select(
/* 153 */         (SelectItemModelProperty<?>)LocalTime.create("MM-dd", "", Optional.empty()), onFalse, 
/*     */         
/* 155 */         List.of(
/* 156 */           when(days, onTrue)));
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/data/models/model/ItemModelUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */