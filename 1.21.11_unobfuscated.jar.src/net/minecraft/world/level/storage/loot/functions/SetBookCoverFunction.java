/*    */ package net.minecraft.world.level.storage.loot.functions;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.datafixers.util.Function4;
/*    */ import com.mojang.serialization.Codec;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.List;
/*    */ import java.util.Objects;
/*    */ import java.util.Optional;
/*    */ import net.minecraft.server.network.Filterable;
/*    */ import net.minecraft.util.ExtraCodecs;
/*    */ import net.minecraft.world.item.ItemStack;
/*    */ import net.minecraft.world.item.component.WrittenBookContent;
/*    */ import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
/*    */ 
/*    */ public class SetBookCoverFunction extends LootItemConditionalFunction {
/*    */   static {
/* 18 */     CODEC = RecordCodecBuilder.mapCodec(i -> commonFields(i).and(i.group((App)Filterable.codec(Codec.string(0, 32)).optionalFieldOf("title").forGetter(()), (App)Codec.STRING.optionalFieldOf("author").forGetter(()), (App)ExtraCodecs.intRange(0, 3).optionalFieldOf("generation").forGetter(()))).apply((Applicative)i, SetBookCoverFunction::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.MapCodec<SetBookCoverFunction> CODEC;
/*    */   
/*    */   private final Optional<String> author;
/*    */   private final Optional<Filterable<String>> title;
/*    */   private final Optional<Integer> generation;
/*    */   
/*    */   public SetBookCoverFunction(List<LootItemCondition> predicates, Optional<Filterable<String>> title, Optional<String> author, Optional<Integer> generation) {
/* 29 */     super(predicates);
/* 30 */     this.author = author;
/* 31 */     this.title = title;
/* 32 */     this.generation = generation;
/*    */   }
/*    */ 
/*    */   
/*    */   protected ItemStack run(ItemStack itemStack, net.minecraft.world.level.storage.loot.LootContext context) {
/* 37 */     itemStack.update(net.minecraft.core.component.DataComponents.WRITTEN_BOOK_CONTENT, WrittenBookContent.EMPTY, this::apply);
/* 38 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   private WrittenBookContent apply(WrittenBookContent original) {
/* 43 */     Objects.requireNonNull(original);
/* 44 */     Objects.requireNonNull(original);
/* 45 */     Objects.requireNonNull(original); return new WrittenBookContent(this.title.orElseGet(original::title), this.author.orElseGet(original::author), (Integer)this.generation.orElseGet(original::generation), 
/* 46 */         original.pages(), 
/* 47 */         original.resolved());
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public LootItemFunctionType<SetBookCoverFunction> getType() {
/* 53 */     return LootItemFunctions.SET_BOOK_COVER;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/level/storage/loot/functions/SetBookCoverFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */