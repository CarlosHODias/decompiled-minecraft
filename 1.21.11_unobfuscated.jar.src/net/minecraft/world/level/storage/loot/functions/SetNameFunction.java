/*     */ package net.minecraft.world.level.storage.loot.functions;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function4;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.List;
/*     */ import java.util.Optional;
/*     */ import java.util.Set;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.commands.CommandSourceStack;
/*     */ import net.minecraft.core.component.DataComponentType;
/*     */ import net.minecraft.core.component.DataComponents;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.network.chat.ComponentUtils;
/*     */ import net.minecraft.server.permissions.LevelBasedPermissionSet;
/*     */ import net.minecraft.server.permissions.PermissionSet;
/*     */ import net.minecraft.util.StringRepresentable;
/*     */ import net.minecraft.util.context.ContextKey;
/*     */ import net.minecraft.world.entity.Entity;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ import net.minecraft.world.level.storage.loot.LootContext;
/*     */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ public class SetNameFunction extends LootItemConditionalFunction {
/*  30 */   private static final Logger LOGGER = LogUtils.getLogger(); public static final com.mojang.serialization.MapCodec<SetNameFunction> CODEC; private final Optional<Component> name; private final Optional<LootContext.EntityTarget> resolutionContext; private final Target target;
/*     */   static {
/*  32 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(()), (App)LootContext.EntityTarget.CODEC.optionalFieldOf("entity").forGetter(()), (App)Target.CODEC.optionalFieldOf("target", Target.CUSTOM_NAME).forGetter(()))).apply((Applicative)i, SetNameFunction::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private SetNameFunction(List<LootItemCondition> predicates, Optional<Component> name, Optional<LootContext.EntityTarget> resolutionContext, Target target) {
/*  43 */     super(predicates);
/*  44 */     this.name = name;
/*  45 */     this.resolutionContext = resolutionContext;
/*  46 */     this.target = target;
/*     */   }
/*     */ 
/*     */   
/*     */   public LootItemFunctionType<SetNameFunction> getType() {
/*  51 */     return LootItemFunctions.SET_NAME;
/*     */   }
/*     */ 
/*     */   
/*     */   public Set<ContextKey<?>> getReferencedContextParams() {
/*  56 */     return this.resolutionContext.<Set<ContextKey<?>>>map(target -> Set.of(target.contextParam())).orElse(Set.of());
/*     */   }
/*     */   
/*     */   public static UnaryOperator<Component> createResolver(LootContext context, LootContext.EntityTarget entityTarget) {
/*  60 */     if (entityTarget != null) {
/*  61 */       Entity entity = (Entity)context.getOptionalParameter(entityTarget.contextParam());
/*  62 */       if (entity != null) {
/*     */         
/*  64 */         CommandSourceStack commandSourceStack = entity.createCommandSourceStackForNameResolution(context.getLevel())
/*  65 */           .withPermission((PermissionSet)LevelBasedPermissionSet.GAMEMASTER);
/*  66 */         return line -> {
/*     */             try {
/*     */               return ComponentUtils.updateForEntity(commandSourceStack, line, entity, 0);
/*  69 */             } catch (CommandSyntaxException e) {
/*     */               LOGGER.warn("Failed to resolve text component", (Throwable)e);
/*     */               return line;
/*     */             } 
/*     */           };
/*     */       } 
/*     */     } 
/*  76 */     return line -> line;
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack run(ItemStack itemStack, LootContext context) {
/*  81 */     this.name.ifPresent(name -> itemStack.set(this.target.component(), createResolver(itemStack, this.resolutionContext.orElse(null)).apply(context)));
/*  82 */     return itemStack;
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> setName(Component value, Target target) {
/*  86 */     return simpleBuilder(conditions -> new SetNameFunction(conditions, Optional.of(value), Optional.empty(), target));
/*     */   }
/*     */   
/*     */   public static LootItemConditionalFunction.Builder<?> setName(Component value, Target target, LootContext.EntityTarget resolutionContext) {
/*  90 */     return simpleBuilder(conditions -> new SetNameFunction(conditions, Optional.of(value), Optional.of(resolutionContext), target));
/*     */   }
/*     */   
/*     */   public enum Target implements StringRepresentable {
/*  94 */     CUSTOM_NAME("custom_name"),
/*  95 */     ITEM_NAME("item_name");
/*     */ 
/*     */     
/*  98 */     public static final Codec<Target> CODEC = (Codec<Target>)StringRepresentable.fromEnum(Target::values);
/*     */     
/*     */     private final String name;
/*     */     
/*     */     Target(String name) {
/* 103 */       this.name = name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 108 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public DataComponentType<Component> component() {
/* 113 */       switch (ordinal()) { default: throw new MatchException(null, null);case 1: case 0: break; }  return 
/*     */         
/* 115 */         DataComponents.CUSTOM_NAME;
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetNameFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */