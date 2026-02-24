/*     */ package net.minecraft.advancements;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.util.Function8;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import net.minecraft.core.ClientAsset;
/*     */ import net.minecraft.network.RegistryFriendlyByteBuf;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.chat.ComponentSerialization;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class DisplayInfo {
/*     */   static {
/*  15 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ItemStack.STRICT_CODEC.fieldOf("icon").forGetter(DisplayInfo::getIcon), (App)ComponentSerialization.CODEC.fieldOf("title").forGetter(DisplayInfo::getTitle), (App)ComponentSerialization.CODEC.fieldOf("description").forGetter(DisplayInfo::getDescription), (App)ClientAsset.ResourceTexture.CODEC.optionalFieldOf("background").forGetter(DisplayInfo::getBackground), (App)AdvancementType.CODEC.optionalFieldOf("frame", AdvancementType.TASK).forGetter(DisplayInfo::getType), (App)Codec.BOOL.optionalFieldOf("show_toast", true).forGetter(DisplayInfo::shouldShowToast), (App)Codec.BOOL.optionalFieldOf("announce_to_chat", true).forGetter(DisplayInfo::shouldAnnounceChat), (App)Codec.BOOL.optionalFieldOf("hidden", false).forGetter(DisplayInfo::isHidden)).apply((com.mojang.datafixers.kinds.Applicative)i, DisplayInfo::new));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static final Codec<DisplayInfo> CODEC;
/*     */ 
/*     */ 
/*     */   
/*  26 */   public static final net.minecraft.network.codec.StreamCodec<RegistryFriendlyByteBuf, DisplayInfo> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.ofMember(DisplayInfo::serializeToNetwork, DisplayInfo::fromNetwork);
/*     */   
/*     */   private final Component title;
/*     */   private final Component description;
/*     */   private final ItemStack icon;
/*     */   private final Optional<ClientAsset.ResourceTexture> background;
/*     */   private final AdvancementType type;
/*     */   private final boolean showToast;
/*     */   private final boolean announceChat;
/*     */   private final boolean hidden;
/*     */   private float x;
/*     */   private float y;
/*     */   
/*     */   public DisplayInfo(ItemStack icon, Component title, Component description, Optional<ClientAsset.ResourceTexture> background, AdvancementType type, boolean showToast, boolean announceChat, boolean hidden) {
/*  40 */     this.title = title;
/*  41 */     this.description = description;
/*  42 */     this.icon = icon;
/*  43 */     this.background = background;
/*  44 */     this.type = type;
/*  45 */     this.showToast = showToast;
/*  46 */     this.announceChat = announceChat;
/*  47 */     this.hidden = hidden;
/*     */   }
/*     */   
/*     */   public void setLocation(float x, float y) {
/*  51 */     this.x = x;
/*  52 */     this.y = y;
/*     */   }
/*     */   
/*     */   public Component getTitle() {
/*  56 */     return this.title;
/*     */   }
/*     */   
/*     */   public Component getDescription() {
/*  60 */     return this.description;
/*     */   }
/*     */   
/*     */   public ItemStack getIcon() {
/*  64 */     return this.icon;
/*     */   }
/*     */   
/*     */   public Optional<ClientAsset.ResourceTexture> getBackground() {
/*  68 */     return this.background;
/*     */   }
/*     */   
/*     */   public AdvancementType getType() {
/*  72 */     return this.type;
/*     */   }
/*     */   
/*     */   public float getX() {
/*  76 */     return this.x;
/*     */   }
/*     */   
/*     */   public float getY() {
/*  80 */     return this.y;
/*     */   }
/*     */   
/*     */   public boolean shouldShowToast() {
/*  84 */     return this.showToast;
/*     */   }
/*     */   
/*     */   public boolean shouldAnnounceChat() {
/*  88 */     return this.announceChat;
/*     */   }
/*     */   
/*     */   public boolean isHidden() {
/*  92 */     return this.hidden;
/*     */   }
/*     */   
/*     */   private void serializeToNetwork(RegistryFriendlyByteBuf output) {
/*  96 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.title);
/*  97 */     ComponentSerialization.TRUSTED_STREAM_CODEC.encode(output, this.description);
/*  98 */     ItemStack.STREAM_CODEC.encode(output, this.icon);
/*  99 */     output.writeEnum(this.type);
/* 100 */     int flags = 0;
/* 101 */     if (this.background.isPresent()) {
/* 102 */       flags |= 0x1;
/*     */     }
/* 104 */     if (this.showToast) {
/* 105 */       flags |= 0x2;
/*     */     }
/* 107 */     if (this.hidden) {
/* 108 */       flags |= 0x4;
/*     */     }
/* 110 */     output.writeInt(flags);
/* 111 */     java.util.Objects.requireNonNull(output); this.background.map(ClientAsset::id).ifPresent(output::writeIdentifier);
/* 112 */     output.writeFloat(this.x);
/* 113 */     output.writeFloat(this.y);
/*     */   }
/*     */   
/*     */   private static DisplayInfo fromNetwork(RegistryFriendlyByteBuf input) {
/* 117 */     Component title = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 118 */     Component description = (Component)ComponentSerialization.TRUSTED_STREAM_CODEC.decode(input);
/* 119 */     ItemStack icon = (ItemStack)ItemStack.STREAM_CODEC.decode(input);
/* 120 */     AdvancementType frame = (AdvancementType)input.readEnum(AdvancementType.class);
/* 121 */     int flags = input.readInt();
/* 122 */     Optional<ClientAsset.ResourceTexture> background = ((flags & 0x1) != 0) ? Optional.<ClientAsset.ResourceTexture>of(new ClientAsset.ResourceTexture(input.readIdentifier())) : Optional.<ClientAsset.ResourceTexture>empty();
/* 123 */     boolean showToast = ((flags & 0x2) != 0);
/* 124 */     boolean hidden = ((flags & 0x4) != 0);
/* 125 */     DisplayInfo info = new DisplayInfo(icon, title, description, background, frame, showToast, false, hidden);
/* 126 */     info.setLocation(input.readFloat(), input.readFloat());
/* 127 */     return info;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/advancements/DisplayInfo.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */