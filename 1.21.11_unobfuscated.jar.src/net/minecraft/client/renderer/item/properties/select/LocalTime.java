/*     */ package net.minecraft.client.renderer.item.properties.select;
/*     */ import com.ibm.icu.text.DateFormat;
/*     */ import com.ibm.icu.text.SimpleDateFormat;
/*     */ import com.ibm.icu.util.Calendar;
/*     */ import com.ibm.icu.util.TimeZone;
/*     */ import com.ibm.icu.util.ULocale;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Function3;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import com.mojang.serialization.MapCodec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Date;
/*     */ import java.util.Optional;
/*     */ import java.util.concurrent.TimeUnit;
/*     */ import net.minecraft.client.multiplayer.ClientLevel;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.LivingEntity;
/*     */ import net.minecraft.world.item.ItemDisplayContext;
/*     */ import net.minecraft.world.item.ItemStack;
/*     */ 
/*     */ public class LocalTime implements SelectItemModelProperty<String> {
/*     */   public static final String ROOT_LOCALE = "";
/*  25 */   private static final long UPDATE_INTERVAL_MS = TimeUnit.SECONDS.toMillis(1L);
/*     */   
/*  27 */   public static final Codec<String> VALUE_CODEC = (Codec<String>)Codec.STRING; private static final Codec<TimeZone> TIME_ZONE_CODEC; private static final MapCodec<Data> DATA_MAP_CODEC; public static final SelectItemModelProperty.Type<LocalTime, String> TYPE; private final Data data; private final DateFormat parsedFormat; private long nextUpdateTimeMs;
/*     */   
/*  29 */   static { TIME_ZONE_CODEC = VALUE_CODEC.comapFlatMap(s -> { TimeZone tz = TimeZone.getTimeZone(s); return tz.equals(TimeZone.UNKNOWN_ZONE) ? DataResult.error(()) : DataResult.success(tz); }, TimeZone::getID);
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  46 */     DATA_MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)Codec.STRING.fieldOf("pattern").forGetter(()), (App)Codec.STRING.optionalFieldOf("locale", "").forGetter(()), (App)TIME_ZONE_CODEC.optionalFieldOf("time_zone").forGetter(())).apply((Applicative)i, Data::new));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  52 */     TYPE = SelectItemModelProperty.Type.create(
/*  53 */         DATA_MAP_CODEC.flatXmap(LocalTime::create, d -> DataResult.success(d.data)), VALUE_CODEC); }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  61 */   private String lastResult = "";
/*     */   private static final class Data extends Record {
/*     */     private final String format;
/*     */     private final String localeId; private final Optional<TimeZone> timeZone; private Data(String format, String localeId, Optional<TimeZone> timeZone) { this.format = format; this.localeId = localeId; this.timeZone = timeZone; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #40	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #40	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #40	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/renderer/item/properties/select/LocalTime$Data;
/*  64 */       //   0	8	1	o	Ljava/lang/Object; } public String format() { return this.format; } public String localeId() { return this.localeId; } public Optional<TimeZone> timeZone() { return this.timeZone; } } private LocalTime(Data data, DateFormat parsedFormat) { this.data = data;
/*  65 */     this.parsedFormat = parsedFormat; }
/*     */ 
/*     */   
/*     */   public static LocalTime create(String format, String localeId, Optional<TimeZone> timeZone) {
/*  69 */     return (LocalTime)create(new Data(format, localeId, timeZone)).getOrThrow(msg -> new IllegalStateException("Failed to validate format: " + msg));
/*     */   }
/*     */   
/*     */   private static DataResult<LocalTime> create(Data data) {
/*  73 */     ULocale locale = new ULocale(data.localeId);
/*     */ 
/*     */     
/*  76 */     Calendar calendar = data.timeZone.<Calendar>map(tz -> Calendar.getInstance(tz, locale))
/*  77 */       .orElseGet(() -> Calendar.getInstance(locale));
/*     */     
/*  79 */     SimpleDateFormat parsedFormat = new SimpleDateFormat(data.format, locale);
/*  80 */     parsedFormat.setCalendar(calendar);
/*     */ 
/*     */     
/*     */     try {
/*  84 */       parsedFormat.format(new Date());
/*  85 */     } catch (Exception e) {
/*  86 */       return DataResult.error(() -> "Invalid time format '" + String.valueOf(parsedFormat) + "': " + e.getMessage());
/*     */     } 
/*     */     
/*  89 */     return DataResult.success(new LocalTime(data, (DateFormat)parsedFormat));
/*     */   }
/*     */ 
/*     */   
/*     */   public String get(ItemStack itemStack, ClientLevel level, LivingEntity owner, int seed, ItemDisplayContext displayContext) {
/*  94 */     long currentTimeMs = Util.getMillis();
/*  95 */     if (currentTimeMs > this.nextUpdateTimeMs) {
/*  96 */       this.lastResult = update();
/*  97 */       this.nextUpdateTimeMs = currentTimeMs + UPDATE_INTERVAL_MS;
/*     */     } 
/*     */     
/* 100 */     return this.lastResult;
/*     */   }
/*     */   
/*     */   private String update() {
/* 104 */     return this.parsedFormat.format(new Date());
/*     */   }
/*     */ 
/*     */   
/*     */   public SelectItemModelProperty.Type<LocalTime, String> type() {
/* 109 */     return TYPE;
/*     */   }
/*     */ 
/*     */   
/*     */   public Codec<String> valueCodec() {
/* 114 */     return VALUE_CODEC;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/renderer/item/properties/select/LocalTime.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */