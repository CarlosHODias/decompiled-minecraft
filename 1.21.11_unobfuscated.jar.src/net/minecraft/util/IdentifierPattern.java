/*    */ package net.minecraft.util;
/*    */ import com.mojang.datafixers.kinds.App;
/*    */ import com.mojang.datafixers.kinds.Applicative;
/*    */ import com.mojang.serialization.codecs.RecordCodecBuilder;
/*    */ import java.util.Optional;
/*    */ import java.util.function.Predicate;
/*    */ import java.util.regex.Pattern;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ public class IdentifierPattern {
/*    */   static {
/* 12 */     CODEC = RecordCodecBuilder.create(i -> i.group((App)ExtraCodecs.PATTERN.optionalFieldOf("namespace").forGetter(()), (App)ExtraCodecs.PATTERN.optionalFieldOf("path").forGetter(())).apply((Applicative)i, IdentifierPattern::new));
/*    */   }
/*    */ 
/*    */   
/*    */   public static final com.mojang.serialization.Codec<IdentifierPattern> CODEC;
/*    */   private final Optional<Pattern> namespacePattern;
/*    */   private final Predicate<String> namespacePredicate;
/*    */   private final Optional<Pattern> pathPattern;
/*    */   private final Predicate<String> pathPredicate;
/*    */   private final Predicate<Identifier> locationPredicate;
/*    */   
/*    */   private IdentifierPattern(Optional<Pattern> namespacePattern, Optional<Pattern> pathPattern) {
/* 24 */     this.namespacePattern = namespacePattern;
/* 25 */     this.namespacePredicate = namespacePattern.<Predicate<String>>map(Pattern::asPredicate).orElse(r -> true);
/* 26 */     this.pathPattern = pathPattern;
/* 27 */     this.pathPredicate = pathPattern.<Predicate<String>>map(Pattern::asPredicate).orElse(r -> true);
/* 28 */     this.locationPredicate = (location -> (this.namespacePredicate.test(location.getNamespace()) && this.pathPredicate.test(location.getPath())));
/*    */   }
/*    */   
/*    */   public Predicate<String> namespacePredicate() {
/* 32 */     return this.namespacePredicate;
/*    */   }
/*    */   
/*    */   public Predicate<String> pathPredicate() {
/* 36 */     return this.pathPredicate;
/*    */   }
/*    */   
/*    */   public Predicate<Identifier> locationPredicate() {
/* 40 */     return this.locationPredicate;
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/IdentifierPattern.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */