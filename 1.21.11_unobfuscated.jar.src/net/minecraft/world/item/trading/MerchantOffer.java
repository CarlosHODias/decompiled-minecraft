/*     */ package net.minecraft.world.item.trading;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function10;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class MerchantOffer {
/*     */   static {
/*  13 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ItemCost.CODEC.fieldOf("buy").forGetter(()), (App)ItemCost.CODEC.lenientOptionalFieldOf("buyB").forGetter(()), (App)ItemStack.CODEC.fieldOf("sell").forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("uses", 0).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("maxUses", 4).forGetter(()), (App)Codec.BOOL.lenientOptionalFieldOf("rewardExp", true).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("specialPrice", 0).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("demand", 0).forGetter(()), (App)Codec.FLOAT.lenientOptionalFieldOf("priceMultiplier", 0.0F).forGetter(()), (App)Codec.INT.lenientOptionalFieldOf("xp", 1).forGetter(())).apply((com.mojang.datafixers.kinds.Applicative)i, MerchantOffer::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<MerchantOffer> CODEC;
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  26 */   public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, MerchantOffer> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(MerchantOffer::writeToStream, MerchantOffer::createFromStream);
/*     */   
/*     */   private final ItemCost baseCostA;
/*     */   
/*     */   private final Optional<ItemCost> costB;
/*     */   private final ItemStack result;
/*     */   private int uses;
/*     */   private final int maxUses;
/*     */   private final boolean rewardExp;
/*     */   private int specialPriceDiff;
/*     */   private int demand;
/*     */   private final float priceMultiplier;
/*     */   private final int xp;
/*     */   
/*     */   private MerchantOffer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int uses, int maxUses, boolean rewardExp, int specialPriceDiff, int demand, float priceMultiplier, int xp) {
/*  41 */     this.baseCostA = baseCostA;
/*  42 */     this.costB = costB;
/*  43 */     this.result = result;
/*  44 */     this.uses = uses;
/*  45 */     this.maxUses = maxUses;
/*  46 */     this.rewardExp = rewardExp;
/*  47 */     this.specialPriceDiff = specialPriceDiff;
/*  48 */     this.demand = demand;
/*  49 */     this.priceMultiplier = priceMultiplier;
/*  50 */     this.xp = xp;
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemCost buy, ItemStack result, int maxUses, int xp, float priceMultiplier) {
/*  54 */     this(buy, Optional.empty(), result, maxUses, xp, priceMultiplier);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int maxUses, int xp, float priceMultiplier) {
/*  58 */     this(baseCostA, costB, result, 0, maxUses, xp, priceMultiplier);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int uses, int maxUses, int xp, float priceMultiplier) {
/*  62 */     this(baseCostA, costB, result, uses, maxUses, xp, priceMultiplier, 0);
/*     */   }
/*     */   
/*     */   public MerchantOffer(ItemCost baseCostA, Optional<ItemCost> costB, ItemStack result, int uses, int maxUses, int xp, float priceMultiplier, int demand) {
/*  66 */     this(baseCostA, costB, result, uses, maxUses, true, 0, demand, priceMultiplier, xp);
/*     */   }
/*     */   
/*     */   private MerchantOffer(MerchantOffer offer) {
/*  70 */     this(offer.baseCostA, offer.costB, 
/*     */ 
/*     */         
/*  73 */         offer.result.copy(), offer.uses, offer.maxUses, offer.rewardExp, offer.specialPriceDiff, offer.demand, offer.priceMultiplier, offer.xp);
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
/*     */   public ItemStack getBaseCostA() {
/*  85 */     return this.baseCostA.itemStack();
/*     */   }
/*     */ 
/*     */   
/*     */   public ItemStack getCostA() {
/*  90 */     return this.baseCostA.itemStack().copyWithCount(getModifiedCostCount(this.baseCostA));
/*     */   }
/*     */   
/*     */   private int getModifiedCostCount(ItemCost cost) {
/*  94 */     int basePrice = cost.count();
/*     */ 
/*     */     
/*  97 */     int demandDiff = Math.max(0, Mth.floor((basePrice * this.demand) * this.priceMultiplier));
/*     */     
/*  99 */     return Mth.clamp(basePrice + demandDiff + this.specialPriceDiff, 1, cost.itemStack().getMaxStackSize());
/*     */   }
/*     */   
/*     */   public ItemStack getCostB() {
/* 103 */     return this.costB.<ItemStack>map(ItemCost::itemStack).orElse(ItemStack.EMPTY);
/*     */   }
/*     */   
/*     */   public ItemCost getItemCostA() {
/* 107 */     return this.baseCostA;
/*     */   }
/*     */   
/*     */   public Optional<ItemCost> getItemCostB() {
/* 111 */     return this.costB;
/*     */   }
/*     */   
/*     */   public ItemStack getResult() {
/* 115 */     return this.result;
/*     */   }
/*     */   
/*     */   public void updateDemand() {
/* 119 */     this.demand = this.demand + this.uses - this.maxUses - this.uses;
/*     */   }
/*     */   
/*     */   public ItemStack assemble() {
/* 123 */     return this.result.copy();
/*     */   }
/*     */   
/*     */   public int getUses() {
/* 127 */     return this.uses;
/*     */   }
/*     */   
/*     */   public void resetUses() {
/* 131 */     this.uses = 0;
/*     */   }
/*     */   
/*     */   public int getMaxUses() {
/* 135 */     return this.maxUses;
/*     */   }
/*     */   
/*     */   public void increaseUses() {
/* 139 */     this.uses++;
/*     */   }
/*     */   
/*     */   public int getDemand() {
/* 143 */     return this.demand;
/*     */   }
/*     */   
/*     */   public void addToSpecialPriceDiff(int add) {
/* 147 */     this.specialPriceDiff += add;
/*     */   }
/*     */   
/*     */   public void resetSpecialPriceDiff() {
/* 151 */     this.specialPriceDiff = 0;
/*     */   }
/*     */   
/*     */   public int getSpecialPriceDiff() {
/* 155 */     return this.specialPriceDiff;
/*     */   }
/*     */   
/*     */   public void setSpecialPriceDiff(int value) {
/* 159 */     this.specialPriceDiff = value;
/*     */   }
/*     */   
/*     */   public float getPriceMultiplier() {
/* 163 */     return this.priceMultiplier;
/*     */   }
/*     */   
/*     */   public int getXp() {
/* 167 */     return this.xp;
/*     */   }
/*     */   
/*     */   public boolean isOutOfStock() {
/* 171 */     return (this.uses >= this.maxUses);
/*     */   }
/*     */   
/*     */   public void setToOutOfStock() {
/* 175 */     this.uses = this.maxUses;
/*     */   }
/*     */   
/*     */   public boolean needsRestock() {
/* 179 */     return (this.uses > 0);
/*     */   }
/*     */   
/*     */   public boolean shouldRewardExp() {
/* 183 */     return this.rewardExp;
/*     */   }
/*     */   
/*     */   public boolean satisfiedBy(ItemStack buyA, ItemStack buyB) {
/* 187 */     if (!this.baseCostA.test(buyA) || buyA.getCount() < getModifiedCostCount(this.baseCostA)) {
/* 188 */       return false;
/*     */     }
/* 190 */     if (this.costB.isPresent()) {
/* 191 */       return (((ItemCost)this.costB.get()).test(buyB) && buyB.getCount() >= ((ItemCost)this.costB.get()).count());
/*     */     }
/* 193 */     return buyB.isEmpty();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean take(ItemStack buyA, ItemStack buyB) {
/* 198 */     if (!satisfiedBy(buyA, buyB)) {
/* 199 */       return false;
/*     */     }
/*     */     
/* 202 */     buyA.shrink(getCostA().getCount());
/* 203 */     if (!getCostB().isEmpty()) {
/* 204 */       buyB.shrink(getCostB().getCount());
/*     */     }
/* 206 */     return true;
/*     */   }
/*     */   
/*     */   public MerchantOffer copy() {
/* 210 */     return new MerchantOffer(this);
/*     */   }
/*     */   
/*     */   private static void writeToStream(RegistryFriendlyByteBuf output, MerchantOffer offer) {
/* 214 */     ItemCost.STREAM_CODEC.encode(output, offer.getItemCostA());
/* 215 */     ItemStack.STREAM_CODEC.encode(output, offer.getResult());
/* 216 */     ItemCost.OPTIONAL_STREAM_CODEC.encode(output, offer.getItemCostB());
/* 217 */     output.writeBoolean(offer.isOutOfStock());
/* 218 */     output.writeInt(offer.getUses());
/* 219 */     output.writeInt(offer.getMaxUses());
/* 220 */     output.writeInt(offer.getXp());
/* 221 */     output.writeInt(offer.getSpecialPriceDiff());
/* 222 */     output.writeFloat(offer.getPriceMultiplier());
/* 223 */     output.writeInt(offer.getDemand());
/*     */   }
/*     */   
/*     */   public static MerchantOffer createFromStream(RegistryFriendlyByteBuf input) {
/* 227 */     ItemCost buy = (ItemCost)ItemCost.STREAM_CODEC.decode(input);
/* 228 */     ItemStack sell = (ItemStack)ItemStack.STREAM_CODEC.decode(input);
/* 229 */     Optional<ItemCost> buyB = (Optional<ItemCost>)ItemCost.OPTIONAL_STREAM_CODEC.decode(input);
/* 230 */     boolean isExhausted = input.readBoolean();
/* 231 */     int uses = input.readInt();
/* 232 */     int maxUses = input.readInt();
/* 233 */     int xp = input.readInt();
/* 234 */     int specialPriceDiff = input.readInt();
/* 235 */     float priceMultiplier = input.readFloat();
/* 236 */     int demand = input.readInt();
/*     */     
/* 238 */     MerchantOffer offer = new MerchantOffer(buy, buyB, sell, uses, maxUses, xp, priceMultiplier, demand);
/* 239 */     if (isExhausted) {
/* 240 */       offer.setToOutOfStock();
/*     */     }
/* 242 */     offer.setSpecialPriceDiff(specialPriceDiff);
/* 243 */     return offer;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/world/item/trading/MerchantOffer.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */