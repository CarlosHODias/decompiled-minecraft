/*     */ package net.minecraft.tags;
/*     */ import com.mojang.datafixers.kinds.App;
/*     */ import com.mojang.datafixers.kinds.Applicative;
/*     */ import com.mojang.datafixers.util.Either;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*     */ import java.util.Collection;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Predicate;
/*     */ import net.minecraft.resources.Identifier;
/*     */ import net.minecraft.util.ExtraCodecs;
/*     */ 
/*     */ public class TagEntry {
/*     */   static {
/*  15 */     FULL_CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.TAG_OR_ELEMENT_ID.fieldOf("id").forGetter(TagEntry::elementOrTag), (App)Codec.BOOL.optionalFieldOf("required", true).forGetter(())).apply((Applicative)i, TagEntry::new));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  20 */     CODEC = Codec.either(ExtraCodecs.TAG_OR_ELEMENT_ID, FULL_CODEC).xmap(e -> (TagEntry)e.map((), ()), entry -> entry.required ? Either.left(entry.elementOrTag()) : Either.right(entry));
/*     */   }
/*     */   
/*     */   private static final Codec<TagEntry> FULL_CODEC;
/*     */   public static final Codec<TagEntry> CODEC;
/*     */   private final Identifier id;
/*     */   private final boolean tag;
/*     */   private final boolean required;
/*     */   
/*     */   private TagEntry(Identifier id, boolean tag, boolean required) {
/*  30 */     this.id = id;
/*  31 */     this.tag = tag;
/*  32 */     this.required = required;
/*     */   }
/*     */   
/*     */   private TagEntry(ExtraCodecs.TagOrElementLocation elementOrTag, boolean required) {
/*  36 */     this.id = elementOrTag.id();
/*  37 */     this.tag = elementOrTag.tag();
/*  38 */     this.required = required;
/*     */   }
/*     */   
/*     */   private ExtraCodecs.TagOrElementLocation elementOrTag() {
/*  42 */     return new ExtraCodecs.TagOrElementLocation(this.id, this.tag);
/*     */   }
/*     */   
/*     */   public static TagEntry element(Identifier id) {
/*  46 */     return new TagEntry(id, false, true);
/*     */   }
/*     */   
/*     */   public static TagEntry optionalElement(Identifier id) {
/*  50 */     return new TagEntry(id, false, false);
/*     */   }
/*     */   
/*     */   public static TagEntry tag(Identifier id) {
/*  54 */     return new TagEntry(id, true, true);
/*     */   }
/*     */   
/*     */   public static TagEntry optionalTag(Identifier id) {
/*  58 */     return new TagEntry(id, true, false);
/*     */   }
/*     */   
/*     */   public <T> boolean build(Lookup<T> lookup, Consumer<T> output) {
/*  62 */     if (this.tag) {
/*  63 */       Collection<T> result = lookup.tag(this.id);
/*  64 */       if (result == null) {
/*  65 */         return !this.required;
/*     */       }
/*  67 */       result.forEach(output);
/*     */     } else {
/*  69 */       T result = lookup.element(this.id, this.required);
/*  70 */       if (result == null) {
/*  71 */         return !this.required;
/*     */       }
/*  73 */       output.accept(result);
/*     */     } 
/*  75 */     return true;
/*     */   }
/*     */   
/*     */   public void visitRequiredDependencies(Consumer<Identifier> output) {
/*  79 */     if (this.tag && this.required) {
/*  80 */       output.accept(this.id);
/*     */     }
/*     */   }
/*     */   
/*     */   public void visitOptionalDependencies(Consumer<Identifier> output) {
/*  85 */     if (this.tag && !this.required) {
/*  86 */       output.accept(this.id);
/*     */     }
/*     */   }
/*     */   
/*     */   public boolean verifyIfPresent(Predicate<Identifier> elementCheck, Predicate<Identifier> tagCheck) {
/*  91 */     return (!this.required || (this.tag ? tagCheck : elementCheck).test(this.id));
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/*  96 */     StringBuilder result = new StringBuilder();
/*  97 */     if (this.tag) {
/*  98 */       result.append('#');
/*     */     }
/* 100 */     result.append(this.id);
/* 101 */     if (!this.required) {
/* 102 */       result.append('?');
/*     */     }
/* 104 */     return result.toString();
/*     */   }
/*     */   
/*     */   public static interface Lookup<T> {
/*     */     T element(Identifier param1Identifier, boolean param1Boolean);
/*     */     
/*     */     Collection<T> tag(Identifier param1Identifier);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/tags/TagEntry.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */