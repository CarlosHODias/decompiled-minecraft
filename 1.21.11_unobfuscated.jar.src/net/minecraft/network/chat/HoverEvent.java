/*     */ package net.minecraft.network.chat;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Optional;
/*     */ import java.util.UUID;
/*     */ import java.util.function.Function;
/*     */ import net.minecraft.world.entity.EntityType;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public interface HoverEvent {
/*     */   public static final Codec<HoverEvent> CODEC;
/*     */   
/*     */   Action action();
/*     */   
/*     */   static {
/*  21 */     CODEC = Action.CODEC.dispatch("action", HoverEvent::action, action -> action.codec);
/*     */   }
/*     */   public static final class ShowText extends Record implements HoverEvent { private final Component value; public static final MapCodec<ShowText> CODEC;
/*     */     
/*  25 */     public ShowText(Component value) { this.value = value; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowText;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  25 */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText; } public Component value() { return this.value; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowText;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowText;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowText;
/*  26 */       //   0	8	1	o	Ljava/lang/Object; } static { CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)ComponentSerialization.CODEC.fieldOf("value").forGetter(ShowText::value)).apply((Applicative)i, ShowText::new)); }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public HoverEvent.Action action() {
/*  32 */       return HoverEvent.Action.SHOW_TEXT;
/*     */     } } public static final class ShowItem extends Record implements HoverEvent { private final ItemStack item; public final String toString() {
/*     */       // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowItem;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #36	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowItem;
/*     */     } public ItemStack item() {
/*  36 */       return this.item;
/*  37 */     } public static final MapCodec<ShowItem> CODEC = ItemStack.MAP_CODEC.xmap(ShowItem::new, ShowItem::item);
/*     */     
/*     */     public ShowItem(ItemStack item) {
/*  40 */       item = item.copy();
/*     */       this.item = item;
/*     */     }
/*     */     
/*     */     public HoverEvent.Action action() {
/*  45 */       return HoverEvent.Action.SHOW_ITEM;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object obj) {
/*  50 */       if (obj instanceof ShowItem) { ShowItem showItem = (ShowItem)obj; if (ItemStack.matches(this.item, showItem.item)); }  return false;
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/*  55 */       return ItemStack.hashItemAndComponents(this.item);
/*     */     } }
/*     */   public static final class ShowEntity extends Record implements HoverEvent { private final HoverEvent.EntityTooltipInfo entity; public static final MapCodec<ShowEntity> CODEC;
/*     */     
/*  59 */     public ShowEntity(HoverEvent.EntityTooltipInfo entity) { this.entity = entity; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/network/chat/HoverEvent$ShowEntity;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #59	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/network/chat/HoverEvent$ShowEntity;
/*  59 */       //   0	8	1	o	Ljava/lang/Object; } public HoverEvent.EntityTooltipInfo entity() { return this.entity; } static {
/*  60 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)HoverEvent.EntityTooltipInfo.CODEC.forGetter(ShowEntity::entity)).apply((Applicative)i, ShowEntity::new));
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     public HoverEvent.Action action() {
/*  66 */       return HoverEvent.Action.SHOW_ENTITY;
/*     */     } }
/*     */   public static class EntityTooltipInfo { public static final MapCodec<EntityTooltipInfo> CODEC; public final EntityType<?> type; public final UUID uuid; public final Optional<Component> name; private java.util.List<Component> linesCache;
/*     */     
/*     */     static {
/*  71 */       CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("id").forGetter(()), (App)net.minecraft.core.UUIDUtil.LENIENT_CODEC.fieldOf("uuid").forGetter(()), (App)ComponentSerialization.CODEC.optionalFieldOf("name").forGetter(())).apply((Applicative)i, EntityTooltipInfo::new));
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*     */     public EntityTooltipInfo(EntityType<?> type, UUID uuid, Component name) {
/*  84 */       this(type, uuid, Optional.ofNullable(name));
/*     */     }
/*     */     
/*     */     public EntityTooltipInfo(EntityType<?> type, UUID uuid, Optional<Component> name) {
/*  88 */       this.type = type;
/*  89 */       this.uuid = uuid;
/*  90 */       this.name = name;
/*     */     }
/*     */     
/*     */     public java.util.List<Component> getTooltipLines() {
/*  94 */       if (this.linesCache == null) {
/*  95 */         this.linesCache = new java.util.ArrayList<>();
/*  96 */         java.util.Objects.requireNonNull(this.linesCache); this.name.ifPresent(this.linesCache::add);
/*  97 */         this.linesCache.add(Component.translatable("gui.entity_tooltip.type", new Object[] { this.type.getDescription() }));
/*  98 */         this.linesCache.add(Component.literal(this.uuid.toString()));
/*     */       } 
/* 100 */       return this.linesCache;
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 105 */       if (this == o) {
/* 106 */         return true;
/*     */       }
/* 108 */       if (o == null || getClass() != o.getClass()) {
/* 109 */         return false;
/*     */       }
/*     */       
/* 112 */       EntityTooltipInfo that = (EntityTooltipInfo)o;
/* 113 */       return (this.type.equals(that.type) && this.uuid.equals(that.uuid) && this.name.equals(that.name));
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 118 */       int result = this.type.hashCode();
/* 119 */       result = 31 * result + this.uuid.hashCode();
/* 120 */       result = 31 * result + this.name.hashCode();
/* 121 */       return result;
/*     */     } }
/*     */ 
/*     */   
/*     */   public enum Action
/*     */     implements net.minecraft.util.StringRepresentable {
/* 127 */     SHOW_TEXT("show_text", true, HoverEvent.ShowText.CODEC),
/* 128 */     SHOW_ITEM("show_item", true, HoverEvent.ShowItem.CODEC),
/* 129 */     SHOW_ENTITY("show_entity", true, HoverEvent.ShowEntity.CODEC);
/*     */ 
/*     */     
/* 132 */     public static final Codec<Action> UNSAFE_CODEC = net.minecraft.util.StringRepresentable.fromValues(Action::values);
/* 133 */     public static final Codec<Action> CODEC = UNSAFE_CODEC.validate(Action::filterForSerialization);
/*     */     
/*     */     private final String name;
/*     */     private final boolean allowFromServer;
/*     */     private final MapCodec<? extends HoverEvent> codec;
/*     */     
/*     */     Action(String name, boolean allowFromServer, MapCodec<? extends HoverEvent> codec) {
/* 140 */       this.name = name;
/* 141 */       this.allowFromServer = allowFromServer;
/* 142 */       this.codec = codec;
/*     */     }
/*     */     
/*     */     public boolean isAllowedFromServer() {
/* 146 */       return this.allowFromServer;
/*     */     }
/*     */ 
/*     */     
/*     */     public String getSerializedName() {
/* 151 */       return this.name;
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 156 */       return "<action " + this.name + ">";
/*     */     }
/*     */     
/*     */     private static DataResult<Action> filterForSerialization(Action action) {
/* 160 */       if (!action.isAllowedFromServer()) {
/* 161 */         return DataResult.error(() -> "Action not allowed: " + String.valueOf(action));
/*     */       }
/* 163 */       return DataResult.success(action, com.mojang.serialization.Lifecycle.stable());
/*     */     }
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/chat/HoverEvent.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */