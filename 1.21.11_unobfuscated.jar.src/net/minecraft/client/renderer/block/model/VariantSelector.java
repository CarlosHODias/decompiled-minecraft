/*    */ package net.minecraft.client.renderer.block.model;
/*    */ 
/*    */ import com.google.common.base.Splitter;
/*    */ import java.util.HashMap;
/*    */ import java.util.Iterator;
/*    */ import java.util.Map;
/*    */ import java.util.Objects;
/*    */ import java.util.function.Predicate;
/*    */ import net.minecraft.world.level.block.state.StateDefinition;
/*    */ import net.minecraft.world.level.block.state.StateHolder;
/*    */ import net.minecraft.world.level.block.state.properties.Property;
/*    */ 
/*    */ 
/*    */ public class VariantSelector
/*    */ {
/* 16 */   private static final Splitter COMMA_SPLITTER = Splitter.on(',');
/* 17 */   private static final Splitter EQUAL_SPLITTER = Splitter.on('=').limit(2);
/*    */   
/*    */   public static <O, S extends StateHolder<O, S>> Predicate<StateHolder<O, S>> predicate(StateDefinition<O, S> stateDefinition, String properties) {
/* 20 */     Map<Property<?>, Comparable<?>> map = new HashMap<>();
/* 21 */     for (String keyValue : (Iterable<String>)COMMA_SPLITTER.split(properties)) {
/* 22 */       Iterator<String> iterator = EQUAL_SPLITTER.split(keyValue).iterator();
/* 23 */       if (iterator.hasNext()) {
/* 24 */         String propertyName = iterator.next();
/* 25 */         Property<?> property = stateDefinition.getProperty(propertyName);
/* 26 */         if (property != null && iterator.hasNext()) {
/* 27 */           String propertyValue = iterator.next();
/* 28 */           Comparable<?> value = (Comparable<?>)getValueHelper(property, propertyValue);
/* 29 */           if (value != null) {
/* 30 */             map.put(property, value); continue;
/*    */           } 
/* 32 */           throw new RuntimeException("Unknown value: '" + propertyValue + "' for blockstate property: '" + propertyName + "' " + String.valueOf(property.getPossibleValues()));
/*    */         } 
/* 34 */         if (!propertyName.isEmpty()) {
/* 35 */           throw new RuntimeException("Unknown blockstate property: '" + propertyName + "'");
/*    */         }
/*    */       } 
/*    */     } 
/*    */     
/* 40 */     return input -> {
/*    */         for (Map.Entry<Property<?>, Comparable<?>> entry : (Iterable<Map.Entry<Property<?>, Comparable<?>>>)map.entrySet()) {
/*    */           if (!Objects.equals(input.getValue(entry.getKey()), entry.getValue())) {
/*    */             return false;
/*    */           }
/*    */         } 
/*    */         return true;
/*    */       };
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   private static <T extends Comparable<T>> T getValueHelper(Property<T> property, String next) {
/* 53 */     return (T)property.getValue(next).orElse(null);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/block/model/VariantSelector.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */