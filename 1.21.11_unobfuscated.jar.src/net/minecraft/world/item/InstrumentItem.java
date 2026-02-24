/*    */ package net.minecraft.world.item;
/*    */ 
/*    */ import java.util.Optional;
/*    */ import net.minecraft.core.Holder;
/*    */ import net.minecraft.core.HolderLookup;
/*    */ import net.minecraft.core.component.DataComponents;
/*    */ import net.minecraft.sounds.SoundEvent;
/*    */ import net.minecraft.sounds.SoundSource;
/*    */ import net.minecraft.stats.Stats;
/*    */ import net.minecraft.util.Mth;
/*    */ import net.minecraft.world.InteractionHand;
/*    */ import net.minecraft.world.InteractionResult;
/*    */ import net.minecraft.world.entity.Entity;
/*    */ import net.minecraft.world.entity.LivingEntity;
/*    */ import net.minecraft.world.entity.player.Player;
/*    */ import net.minecraft.world.item.component.InstrumentComponent;
/*    */ import net.minecraft.world.level.Level;
/*    */ import net.minecraft.world.level.gameevent.GameEvent;
/*    */ 
/*    */ public class InstrumentItem
/*    */   extends Item {
/*    */   public InstrumentItem(Item.Properties properties) {
/* 23 */     super(properties);
/*    */   }
/*    */   
/*    */   public static ItemStack create(Item item, Holder<Instrument> instrument) {
/* 27 */     ItemStack itemStack = new ItemStack(item);
/* 28 */     itemStack.set(DataComponents.INSTRUMENT, new InstrumentComponent(instrument));
/* 29 */     return itemStack;
/*    */   }
/*    */ 
/*    */   
/*    */   public InteractionResult use(Level level, Player player, InteractionHand hand) {
/* 34 */     ItemStack itemStack = player.getItemInHand(hand);
/* 35 */     Optional<? extends Holder<Instrument>> instrumentHolder = getInstrument(itemStack, (HolderLookup.Provider)player.registryAccess());
/* 36 */     if (instrumentHolder.isPresent()) {
/* 37 */       Instrument instrument = (Instrument)((Holder)instrumentHolder.get()).value();
/* 38 */       player.startUsingItem(hand);
/* 39 */       play(level, player, instrument);
/* 40 */       player.getCooldowns().addCooldown(itemStack, Mth.floor(instrument.useDuration() * 20.0F));
/* 41 */       player.awardStat(Stats.ITEM_USED.get(this));
/* 42 */       return (InteractionResult)InteractionResult.CONSUME;
/*    */     } 
/* 44 */     return (InteractionResult)InteractionResult.FAIL;
/*    */   }
/*    */ 
/*    */   
/*    */   public int getUseDuration(ItemStack itemStack, LivingEntity user) {
/* 49 */     Optional<Holder<Instrument>> instrument = getInstrument(itemStack, (HolderLookup.Provider)user.registryAccess());
/* 50 */     return (Integer)instrument.<Integer>map(instrumentHolder -> Mth.floor(((Instrument)instrumentHolder.value()).useDuration() * 20.0F)).orElse(0);
/*    */   }
/*    */   
/*    */   private Optional<Holder<Instrument>> getInstrument(ItemStack itemStack, HolderLookup.Provider registries) {
/* 54 */     InstrumentComponent instrument = (InstrumentComponent)itemStack.get(DataComponents.INSTRUMENT);
/* 55 */     return (instrument != null) ? instrument.unwrap(registries) : Optional.<Holder<Instrument>>empty();
/*    */   }
/*    */ 
/*    */   
/*    */   public ItemUseAnimation getUseAnimation(ItemStack itemStack) {
/* 60 */     return ItemUseAnimation.TOOT_HORN;
/*    */   }
/*    */   
/*    */   private static void play(Level level, Player player, Instrument instrument) {
/* 64 */     SoundEvent soundEvent = (SoundEvent)instrument.soundEvent().value();
/* 65 */     float volume = instrument.range() / 16.0F;
/* 66 */     level.playSound((Entity)player, (Entity)player, soundEvent, SoundSource.RECORDS, volume, 1.0F);
/* 67 */     level.gameEvent((Holder)GameEvent.INSTRUMENT_PLAY, player.position(), GameEvent.Context.of((Entity)player));
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/InstrumentItem.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */