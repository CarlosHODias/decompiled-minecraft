/*     */ package net.minecraft.world.entity.ai.attributes;
/*     */ 
/*     */ import com.google.common.collect.Multimap;
/*     */ import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
/*     */ import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.stream.Collectors;
/*     */ import net.minecraft.core.Holder;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ 
/*     */ public class AttributeMap
/*     */ {
/*  18 */   private final Map<Holder<Attribute>, AttributeInstance> attributes = (Map<Holder<Attribute>, AttributeInstance>)new Object2ObjectOpenHashMap();
/*  19 */   private final Set<AttributeInstance> attributesToSync = (Set<AttributeInstance>)new ObjectOpenHashSet();
/*  20 */   private final Set<AttributeInstance> attributesToUpdate = (Set<AttributeInstance>)new ObjectOpenHashSet();
/*     */   private final AttributeSupplier supplier;
/*     */   
/*     */   public AttributeMap(AttributeSupplier supplier) {
/*  24 */     this.supplier = supplier;
/*     */   }
/*     */   
/*     */   private void onAttributeModified(AttributeInstance attributeInstance) {
/*  28 */     this.attributesToUpdate.add(attributeInstance);
/*  29 */     if (((Attribute)attributeInstance.getAttribute().value()).isClientSyncable()) {
/*  30 */       this.attributesToSync.add(attributeInstance);
/*     */     }
/*     */   }
/*     */   
/*     */   public Set<AttributeInstance> getAttributesToSync() {
/*  35 */     return this.attributesToSync;
/*     */   }
/*     */   
/*     */   public Set<AttributeInstance> getAttributesToUpdate() {
/*  39 */     return this.attributesToUpdate;
/*     */   }
/*     */   
/*     */   public Collection<AttributeInstance> getSyncableAttributes() {
/*  43 */     return (Collection<AttributeInstance>)this.attributes.values().stream().filter(instance -> ((Attribute)instance.getAttribute().value()).isClientSyncable()).collect(Collectors.toList());
/*     */   }
/*     */   
/*     */   public AttributeInstance getInstance(Holder<Attribute> attribute) {
/*  47 */     return this.attributes.computeIfAbsent(attribute, key -> this.supplier.createInstance(this::onAttributeModified, key));
/*     */   }
/*     */   
/*     */   public boolean hasAttribute(Holder<Attribute> attribute) {
/*  51 */     return (this.attributes.get(attribute) != null || this.supplier.hasAttribute(attribute));
/*     */   }
/*     */   
/*     */   public boolean hasModifier(Holder<Attribute> attribute, Identifier id) {
/*  55 */     AttributeInstance attributeInstance = this.attributes.get(attribute);
/*  56 */     return (attributeInstance != null) ? ((attributeInstance.getModifier(id) != null)) : this.supplier.hasModifier(attribute, id);
/*     */   }
/*     */   
/*     */   public double getValue(Holder<Attribute> attribute) {
/*  60 */     AttributeInstance ownAttribute = this.attributes.get(attribute);
/*  61 */     return (ownAttribute != null) ? ownAttribute.getValue() : this.supplier.getValue(attribute);
/*     */   }
/*     */   
/*     */   public double getBaseValue(Holder<Attribute> attribute) {
/*  65 */     AttributeInstance ownAttribute = this.attributes.get(attribute);
/*  66 */     return (ownAttribute != null) ? ownAttribute.getBaseValue() : this.supplier.getBaseValue(attribute);
/*     */   }
/*     */   
/*     */   public double getModifierValue(Holder<Attribute> attribute, Identifier id) {
/*  70 */     AttributeInstance attributeInstance = this.attributes.get(attribute);
/*  71 */     return (attributeInstance != null) ? attributeInstance.getModifier(id).amount() : this.supplier.getModifierValue(attribute, id);
/*     */   }
/*     */   
/*     */   public void addTransientAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> modifiers) {
/*  75 */     modifiers.forEach((attribute, attributeModifier) -> {
/*     */           AttributeInstance instance = getInstance(attribute);
/*     */           if (instance != null) {
/*     */             instance.removeModifier(attributeModifier.id());
/*     */             instance.addTransientModifier(attributeModifier);
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void removeAttributeModifiers(Multimap<Holder<Attribute>, AttributeModifier> modifiers) {
/*  86 */     modifiers.asMap().forEach((attribute, attributeModifiers) -> {
/*     */           AttributeInstance instance = this.attributes.get(attribute);
/*     */           if (instance != null) {
/*     */             attributeModifiers.forEach(());
/*     */           }
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   public void assignAllValues(AttributeMap other) {
/*  96 */     other.attributes.values().forEach(otherInstance -> {
/*     */           AttributeInstance selfInstance = getInstance(otherInstance.getAttribute());
/*     */           if (selfInstance != null) {
/*     */             selfInstance.replaceFrom(otherInstance);
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void assignBaseValues(AttributeMap other) {
/* 105 */     other.attributes.values().forEach(otherInstance -> {
/*     */           AttributeInstance selfInstance = getInstance(otherInstance.getAttribute());
/*     */           if (selfInstance != null) {
/*     */             selfInstance.setBaseValue(otherInstance.getBaseValue());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public void assignPermanentModifiers(AttributeMap other) {
/* 114 */     other.attributes.values().forEach(otherInstance -> {
/*     */           AttributeInstance selfInstance = getInstance(otherInstance.getAttribute());
/*     */           if (selfInstance != null) {
/*     */             selfInstance.addPermanentModifiers(otherInstance.getPermanentModifiers());
/*     */           }
/*     */         });
/*     */   }
/*     */   
/*     */   public boolean resetBaseValue(Holder<Attribute> attribute) {
/* 123 */     if (!this.supplier.hasAttribute(attribute)) {
/* 124 */       return false;
/*     */     }
/* 126 */     AttributeInstance instance = this.attributes.get(attribute);
/* 127 */     if (instance != null) {
/* 128 */       instance.setBaseValue(this.supplier.getBaseValue(attribute));
/*     */     }
/* 130 */     return true;
/*     */   }
/*     */   
/*     */   public List<AttributeInstance.Packed> pack() {
/* 134 */     List<AttributeInstance.Packed> result = new ArrayList<>(this.attributes.values().size());
/* 135 */     for (AttributeInstance attribute : this.attributes.values()) {
/* 136 */       result.add(attribute.pack());
/*     */     }
/* 138 */     return result;
/*     */   }
/*     */   
/*     */   public void apply(List<AttributeInstance.Packed> packedAttributes) {
/* 142 */     for (AttributeInstance.Packed packedAttribute : packedAttributes) {
/* 143 */       AttributeInstance instance = getInstance(packedAttribute.attribute());
/* 144 */       if (instance != null)
/* 145 */         instance.apply(packedAttribute); 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/ai/attributes/AttributeMap.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */