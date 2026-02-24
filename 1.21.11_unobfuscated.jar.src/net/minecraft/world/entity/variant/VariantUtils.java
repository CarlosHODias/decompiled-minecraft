/*    */ package net.minecraft.world.entity.variant;
/*    */ 
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import java.util.stream.Stream;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.Registry;
/*    */ import net.minecraft.core.RegistryAccess;
/*    */ import net.minecraft.resources.Identifier;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.world.level.ServerLevelAccessor;
/*    */ import net.minecraft.world.level.storage.ValueInput;
/*    */ import net.minecraft.world.level.storage.ValueOutput;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class VariantUtils
/*    */ {
/*    */   public static final String TAG_VARIANT = "variant";
/*    */   
/*    */   public static <T> Holder<T> getDefaultOrAny(RegistryAccess registryAccess, ResourceKey<T> id) {
/* 23 */     Registry<T> registry = registryAccess.lookupOrThrow(id.registryKey());
/* 24 */     Objects.requireNonNull(registry); return registry.get(id).or(registry::getAny).orElseThrow();
/*    */   }
/*    */   
/*    */   public static <T> Holder<T> getAny(RegistryAccess registryAccess, ResourceKey<? extends Registry<T>> registryId) {
/* 28 */     return registryAccess.lookupOrThrow(registryId).getAny().orElseThrow();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> void writeVariant(ValueOutput output, Holder<T> holder) {
/* 35 */     holder.unwrapKey().ifPresent(k -> output.store("variant", Identifier.CODEC, k.identifier()));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static <T> Optional<Holder<T>> readVariant(ValueInput input, ResourceKey<? extends Registry<T>> registryId) {
/* 44 */     Objects.requireNonNull(input.lookup()); return input.read("variant", Identifier.CODEC).map(id -> ResourceKey.create(registryId, id)).flatMap(input.lookup()::get);
/*    */   }
/*    */   
/*    */   public static <T extends PriorityProvider<SpawnContext, ?>> Optional<Holder.Reference<T>> selectVariantToSpawn(SpawnContext context, ResourceKey<Registry<T>> variantRegistry) {
/* 48 */     ServerLevelAccessor level = context.level();
/* 49 */     Stream<Holder.Reference<T>> entries = level.registryAccess().lookupOrThrow(variantRegistry).listElements();
/* 50 */     return PriorityProvider.pick(entries, Holder::value, level.getRandom(), context);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/entity/variant/VariantUtils.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */