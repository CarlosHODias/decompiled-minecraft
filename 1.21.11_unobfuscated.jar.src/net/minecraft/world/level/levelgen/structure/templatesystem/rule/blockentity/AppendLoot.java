/*    */ package net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.function.Function;
/*    */ import net.minecraft.nbt.CompoundTag;
/*    */ import net.minecraft.resources.ResourceKey;
/*    */ import net.minecraft.util.RandomSource;
/*    */ import net.minecraft.world.level.storage.loot.LootTable;
/*    */ 
/*    */ public class AppendLoot implements RuleBlockEntityModifier {
/*    */   static {
/* 13 */     CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)LootTable.KEY_CODEC.fieldOf("loot_table").forGetter(())).apply((Applicative)i, AppendLoot::new));
/*    */   }
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<AppendLoot> CODEC;
/*    */   private final ResourceKey<LootTable> lootTable;
/*    */   
/*    */   public AppendLoot(ResourceKey<LootTable> lootTable) {
/* 20 */     this.lootTable = lootTable;
/*    */   }
/*    */ 
/*    */   
/*    */   public CompoundTag apply(RandomSource random, CompoundTag existingTag) {
/* 25 */     CompoundTag result = (existingTag == null) ? new CompoundTag() : existingTag.copy();
/*    */     
/* 27 */     result.store("LootTable", LootTable.KEY_CODEC, this.lootTable);
/* 28 */     result.putLong("LootTableSeed", random.nextLong());
/*    */     
/* 30 */     return result;
/*    */   }
/*    */ 
/*    */   
/*    */   public RuleBlockEntityModifierType<?> getType() {
/* 35 */     return RuleBlockEntityModifierType.APPEND_LOOT;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/levelgen/structure/templatesystem/rule/blockentity/AppendLoot.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */