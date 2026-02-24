/*     */ package net.minecraft.resources;
/*     */ 
/*     */ import com.mojang.brigadier.Message;
/*     */ import com.mojang.brigadier.StringReader;
/*     */ import com.mojang.brigadier.exceptions.CommandSyntaxException;
/*     */ import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
/*     */ import com.mojang.serialization.Codec;
/*     */ import com.mojang.serialization.DataResult;
/*     */ import io.netty.buffer.ByteBuf;
/*     */ import java.util.function.UnaryOperator;
/*     */ import net.minecraft.IdentifierException;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.network.codec.ByteBufCodecs;
/*     */ import net.minecraft.network.codec.StreamCodec;
/*     */ 
/*     */ public final class Identifier
/*     */   implements Comparable<Identifier> {
/*  18 */   public static final Codec<Identifier> CODEC = Codec.STRING.comapFlatMap(Identifier::read, Identifier::toString).stable();
/*  19 */   public static final StreamCodec<ByteBuf, Identifier> STREAM_CODEC = ByteBufCodecs.STRING_UTF8.map(Identifier::parse, Identifier::toString);
/*     */   
/*  21 */   public static final SimpleCommandExceptionType ERROR_INVALID = new SimpleCommandExceptionType((Message)Component.translatable("argument.id.invalid"));
/*     */   
/*     */   public static final char NAMESPACE_SEPARATOR = ':';
/*     */   
/*     */   public static final String DEFAULT_NAMESPACE = "minecraft";
/*     */   
/*     */   public static final String REALMS_NAMESPACE = "realms";
/*     */   private final String namespace;
/*     */   private final String path;
/*     */   
/*     */   private Identifier(String namespace, String path) {
/*  32 */     assert isValidNamespace(namespace);
/*  33 */     assert isValidPath(path);
/*  34 */     this.namespace = namespace;
/*  35 */     this.path = path;
/*     */   }
/*     */   
/*     */   private static Identifier createUntrusted(String namespace, String path) {
/*  39 */     return new Identifier(assertValidNamespace(namespace, path), assertValidPath(namespace, path));
/*     */   }
/*     */   
/*     */   public static Identifier fromNamespaceAndPath(String namespace, String path) {
/*  43 */     return createUntrusted(namespace, path);
/*     */   }
/*     */   
/*     */   public static Identifier parse(String identifier) {
/*  47 */     return bySeparator(identifier, ':');
/*     */   }
/*     */   
/*     */   public static Identifier withDefaultNamespace(String path) {
/*  51 */     return new Identifier("minecraft", assertValidPath("minecraft", path));
/*     */   }
/*     */   
/*     */   public static Identifier tryParse(String identifier) {
/*  55 */     return tryBySeparator(identifier, ':');
/*     */   }
/*     */   
/*     */   public static Identifier tryBuild(String namespace, String path) {
/*  59 */     if (isValidNamespace(namespace) && isValidPath(path)) {
/*  60 */       return new Identifier(namespace, path);
/*     */     }
/*  62 */     return null;
/*     */   }
/*     */   
/*     */   public static Identifier bySeparator(String identifier, char separator) {
/*  66 */     int separatorIndex = identifier.indexOf(separator);
/*  67 */     if (separatorIndex >= 0) {
/*  68 */       String path = identifier.substring(separatorIndex + 1);
/*  69 */       if (separatorIndex != 0) {
/*  70 */         String namespace = identifier.substring(0, separatorIndex);
/*  71 */         return createUntrusted(namespace, path);
/*     */       } 
/*     */       
/*  74 */       return withDefaultNamespace(path);
/*     */     } 
/*     */ 
/*     */     
/*  78 */     return withDefaultNamespace(identifier);
/*     */   }
/*     */   
/*     */   public static Identifier tryBySeparator(String identifier, char separator) {
/*  82 */     int separatorIndex = identifier.indexOf(separator);
/*  83 */     if (separatorIndex >= 0) {
/*  84 */       String path = identifier.substring(separatorIndex + 1);
/*  85 */       if (!isValidPath(path)) {
/*  86 */         return null;
/*     */       }
/*  88 */       if (separatorIndex != 0) {
/*  89 */         String namespace = identifier.substring(0, separatorIndex);
/*  90 */         return isValidNamespace(namespace) ? new Identifier(namespace, path) : null;
/*     */       } 
/*     */       
/*  93 */       return new Identifier("minecraft", path);
/*     */     } 
/*     */ 
/*     */     
/*  97 */     return isValidPath(identifier) ? new Identifier("minecraft", identifier) : null;
/*     */   }
/*     */   
/*     */   public static DataResult<Identifier> read(String input) {
/*     */     try {
/* 102 */       return DataResult.success(parse(input));
/* 103 */     } catch (IdentifierException e) {
/* 104 */       return DataResult.error(() -> "Not a valid resource location: " + input + " " + e.getMessage());
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getPath() {
/* 109 */     return this.path;
/*     */   }
/*     */   
/*     */   public String getNamespace() {
/* 113 */     return this.namespace;
/*     */   }
/*     */   
/*     */   public Identifier withPath(String newPath) {
/* 117 */     return new Identifier(this.namespace, assertValidPath(this.namespace, newPath));
/*     */   }
/*     */   
/*     */   public Identifier withPath(UnaryOperator<String> modifier) {
/* 121 */     return withPath(modifier.apply(this.path));
/*     */   }
/*     */   
/*     */   public Identifier withPrefix(String prefix) {
/* 125 */     return withPath(prefix + prefix);
/*     */   }
/*     */   
/*     */   public Identifier withSuffix(String suffix) {
/* 129 */     return withPath(this.path + this.path);
/*     */   }
/*     */ 
/*     */   
/*     */   public String toString() {
/* 134 */     return this.namespace + ":" + this.namespace;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean equals(Object o) {
/* 139 */     if (this == o) {
/* 140 */       return true;
/*     */     }
/*     */     
/* 143 */     if (o instanceof Identifier) { Identifier that = (Identifier)o;
/* 144 */       return (this.namespace.equals(that.namespace) && this.path.equals(that.path)); }
/*     */ 
/*     */     
/* 147 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public int hashCode() {
/* 152 */     return 31 * this.namespace.hashCode() + this.path.hashCode();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int compareTo(Identifier o) {
/* 158 */     int result = this.path.compareTo(o.path);
/* 159 */     if (result == 0) {
/* 160 */       result = this.namespace.compareTo(o.namespace);
/*     */     }
/* 162 */     return result;
/*     */   }
/*     */   
/*     */   public String toDebugFileName() {
/* 166 */     return toString().replace('/', '_').replace(':', '_');
/*     */   }
/*     */   
/*     */   public String toLanguageKey() {
/* 170 */     return this.namespace + "." + this.namespace;
/*     */   }
/*     */   
/*     */   public String toShortLanguageKey() {
/* 174 */     return this.namespace.equals("minecraft") ? this.path : toLanguageKey();
/*     */   }
/*     */   
/*     */   public String toShortString() {
/* 178 */     return this.namespace.equals("minecraft") ? this.path : toString();
/*     */   }
/*     */   
/*     */   public String toLanguageKey(String prefix) {
/* 182 */     return prefix + "." + prefix;
/*     */   }
/*     */   
/*     */   public String toLanguageKey(String prefix, String suffix) {
/* 186 */     return prefix + "." + prefix + "." + toLanguageKey();
/*     */   }
/*     */   
/*     */   private static String readGreedy(StringReader reader) {
/* 190 */     int start = reader.getCursor();
/* 191 */     while (reader.canRead() && isAllowedInIdentifier(reader.peek())) {
/* 192 */       reader.skip();
/*     */     }
/* 194 */     return reader.getString().substring(start, reader.getCursor());
/*     */   }
/*     */   
/*     */   public static Identifier read(StringReader reader) throws CommandSyntaxException {
/* 198 */     int start = reader.getCursor();
/* 199 */     String raw = readGreedy(reader);
/*     */     try {
/* 201 */       return parse(raw);
/* 202 */     } catch (IdentifierException ex) {
/* 203 */       reader.setCursor(start);
/* 204 */       throw ERROR_INVALID.createWithContext(reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Identifier readNonEmpty(StringReader reader) throws CommandSyntaxException {
/* 209 */     int start = reader.getCursor();
/* 210 */     String raw = readGreedy(reader);
/* 211 */     if (raw.isEmpty()) {
/* 212 */       throw ERROR_INVALID.createWithContext(reader);
/*     */     }
/*     */     try {
/* 215 */       return parse(raw);
/* 216 */     } catch (IdentifierException ex) {
/* 217 */       reader.setCursor(start);
/* 218 */       throw ERROR_INVALID.createWithContext(reader);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static boolean isAllowedInIdentifier(char c) {
/* 223 */     return ((c >= '0' && c <= '9') || (c >= 'a' && c <= 'z') || c == '_' || c == ':' || c == '/' || c == '.' || c == '-');
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isValidPath(String path) {
/* 231 */     for (int i = 0; i < path.length(); i++) {
/* 232 */       if (!validPathChar(path.charAt(i))) {
/* 233 */         return false;
/*     */       }
/*     */     } 
/* 236 */     return true;
/*     */   }
/*     */   
/*     */   public static boolean isValidNamespace(String namespace) {
/* 240 */     for (int i = 0; i < namespace.length(); i++) {
/* 241 */       if (!validNamespaceChar(namespace.charAt(i))) {
/* 242 */         return false;
/*     */       }
/*     */     } 
/* 245 */     return true;
/*     */   }
/*     */   
/*     */   private static String assertValidNamespace(String namespace, String path) {
/* 249 */     if (!isValidNamespace(namespace)) {
/* 250 */       throw new IdentifierException("Non [a-z0-9_.-] character in namespace of location: " + namespace + ":" + path);
/*     */     }
/* 252 */     return namespace;
/*     */   }
/*     */   
/*     */   public static boolean validPathChar(char c) {
/* 256 */     return (c == '_' || c == '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '/' || c == '.');
/*     */   }
/*     */   
/*     */   private static boolean validNamespaceChar(char c) {
/* 260 */     return (c == '_' || c == '-' || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || c == '.');
/*     */   }
/*     */   
/*     */   private static String assertValidPath(String namespace, String path) {
/* 264 */     if (!isValidPath(path)) {
/* 265 */       throw new IdentifierException("Non [a-z0-9/._-] character in path of location: " + namespace + ":" + path);
/*     */     }
/* 267 */     return path;
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/resources/Identifier.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */