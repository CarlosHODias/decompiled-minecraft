/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.google.common.collect.Maps;
/*     */ import com.mojang.blaze3d.platform.InputConstants;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import java.util.Map;
/*     */ import java.util.Objects;
/*     */ import java.util.function.Consumer;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.resources.language.I18n;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import net.minecraft.resources.Identifier;
/*     */ 
/*     */ public class KeyMapping
/*     */   implements Comparable<KeyMapping> {
/*  21 */   private static final Map<String, KeyMapping> ALL = Maps.newHashMap();
/*  22 */   private static final Map<InputConstants.Key, List<KeyMapping>> MAP = Maps.newHashMap(); private final String name; private final InputConstants.Key defaultKey; private final Category category; protected InputConstants.Key key; private boolean isDown; private int clickCount; private final int order;
/*     */   public static final class Category extends Record { private final Identifier id;
/*     */     
/*  25 */     public Category(Identifier id) { this.id = id; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/KeyMapping$Category;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*  25 */       //   0	7	0	this	Lnet/minecraft/client/KeyMapping$Category; } public Identifier id() { return this.id; }
/*     */     public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/KeyMapping$Category;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/KeyMapping$Category; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/KeyMapping$Category;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #25	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/KeyMapping$Category;
/*  26 */       //   0	8	1	o	Ljava/lang/Object; } private static final List<Category> SORT_ORDER = new ArrayList<>();
/*  27 */     public static final Category MOVEMENT = register("movement");
/*  28 */     public static final Category MISC = register("misc");
/*  29 */     public static final Category MULTIPLAYER = register("multiplayer");
/*  30 */     public static final Category GAMEPLAY = register("gameplay");
/*  31 */     public static final Category INVENTORY = register("inventory");
/*  32 */     public static final Category CREATIVE = register("creative");
/*  33 */     public static final Category SPECTATOR = register("spectator");
/*  34 */     public static final Category DEBUG = register("debug");
/*     */     
/*     */     private static Category register(String name) {
/*  37 */       return register(Identifier.withDefaultNamespace(name));
/*     */     }
/*     */     
/*     */     public static Category register(Identifier id) {
/*  41 */       Category category = new Category(id);
/*  42 */       if (SORT_ORDER.contains(category)) {
/*  43 */         throw new IllegalArgumentException(String.format(Locale.ROOT, "Category '%s' is already registered.", new Object[] { id }));
/*     */       }
/*  45 */       SORT_ORDER.add(category);
/*  46 */       return category;
/*     */     }
/*     */     
/*     */     public Component label() {
/*  50 */       return (Component)Component.translatable(this.id.toLanguageKey("key.category"));
/*     */     } }
/*     */ 
/*     */   
/*     */   public static void click(InputConstants.Key key) {
/*  55 */     forAllKeyMappings(key, keyMapping -> keyMapping.clickCount++);
/*     */   }
/*     */   
/*     */   public static void set(InputConstants.Key key, boolean state) {
/*  59 */     forAllKeyMappings(key, keyMapping -> keyMapping.setDown(state));
/*     */   }
/*     */   
/*     */   private static void forAllKeyMappings(InputConstants.Key key, Consumer<KeyMapping> operation) {
/*  63 */     List<KeyMapping> keyMappings = MAP.get(key);
/*  64 */     if (keyMappings != null && !keyMappings.isEmpty()) {
/*  65 */       for (KeyMapping keyMapping : keyMappings) {
/*  66 */         operation.accept(keyMapping);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void setAll() {
/*  73 */     Window window = Minecraft.getInstance().getWindow();
/*  74 */     for (KeyMapping keyMapping : ALL.values()) {
/*  75 */       if (keyMapping.shouldSetOnIngameFocus()) {
/*  76 */         keyMapping.setDown(InputConstants.isKeyDown(window, keyMapping.key.getValue()));
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void releaseAll() {
/*  82 */     for (KeyMapping keyMapping : ALL.values()) {
/*  83 */       keyMapping.release();
/*     */     }
/*     */   }
/*     */   
/*     */   public static void restoreToggleStatesOnScreenClosed() {
/*  88 */     for (KeyMapping keyMapping : ALL.values()) {
/*  89 */       if (keyMapping instanceof ToggleKeyMapping) { ToggleKeyMapping toggleKeyMapping = (ToggleKeyMapping)keyMapping; if (toggleKeyMapping.shouldRestoreStateOnScreenClosed())
/*  90 */           toggleKeyMapping.setDown(true);  }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void resetToggleKeys() {
/*  96 */     for (KeyMapping keyMapping : ALL.values()) {
/*  97 */       if (keyMapping instanceof ToggleKeyMapping) { ToggleKeyMapping toggleKeyMapping = (ToggleKeyMapping)keyMapping;
/*  98 */         toggleKeyMapping.reset(); }
/*     */     
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void resetMapping() {
/* 104 */     MAP.clear();
/* 105 */     for (KeyMapping keyMapping : ALL.values()) {
/* 106 */       keyMapping.registerMapping(keyMapping.key);
/*     */     }
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
/*     */   public KeyMapping(String name, int keysym, Category category) {
/* 119 */     this(name, InputConstants.Type.KEYSYM, keysym, category);
/*     */   }
/*     */   
/*     */   public KeyMapping(String name, InputConstants.Type type, int value, Category category) {
/* 123 */     this(name, type, value, category, 0);
/*     */   }
/*     */   
/*     */   public KeyMapping(String name, InputConstants.Type type, int value, Category category, int order) {
/* 127 */     this.name = name;
/* 128 */     this.key = type.getOrCreate(value);
/* 129 */     this.defaultKey = this.key;
/* 130 */     this.category = category;
/* 131 */     this.order = order;
/*     */     
/* 133 */     ALL.put(name, this);
/* 134 */     registerMapping(this.key);
/*     */   }
/*     */   
/*     */   public boolean isDown() {
/* 138 */     return this.isDown;
/*     */   }
/*     */   
/*     */   public Category getCategory() {
/* 142 */     return this.category;
/*     */   }
/*     */   
/*     */   public boolean consumeClick() {
/* 146 */     if (this.clickCount == 0) {
/* 147 */       return false;
/*     */     }
/* 149 */     this.clickCount--;
/* 150 */     return true;
/*     */   }
/*     */   
/*     */   protected void release() {
/* 154 */     this.clickCount = 0;
/* 155 */     setDown(false);
/*     */   }
/*     */   
/*     */   protected boolean shouldSetOnIngameFocus() {
/* 159 */     return (this.key.getType() == InputConstants.Type.KEYSYM && this.key.getValue() != InputConstants.UNKNOWN.getValue());
/*     */   }
/*     */   
/*     */   public String getName() {
/* 163 */     return this.name;
/*     */   }
/*     */   
/*     */   public InputConstants.Key getDefaultKey() {
/* 167 */     return this.defaultKey;
/*     */   }
/*     */   
/*     */   public void setKey(InputConstants.Key key) {
/* 171 */     this.key = key;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compareTo(KeyMapping o) {
/* 176 */     if (this.category == o.category) {
/* 177 */       if (this.order == o.order) {
/* 178 */         return I18n.get(this.name, new Object[0]).compareTo(I18n.get(o.name, new Object[0]));
/*     */       }
/* 180 */       return Integer.compare(this.order, o.order);
/*     */     } 
/* 182 */     return Integer.compare(Category.SORT_ORDER.indexOf(this.category), Category.SORT_ORDER.indexOf(o.category));
/*     */   }
/*     */   
/*     */   public static Supplier<Component> createNameSupplier(String key) {
/* 186 */     KeyMapping map = ALL.get(key);
/* 187 */     if (map == null) {
/* 188 */       return () -> Component.translatable(key);
/*     */     }
/* 190 */     Objects.requireNonNull(map); return map::getTranslatedKeyMessage;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean same(KeyMapping that) {
/* 195 */     return this.key.equals(that.key);
/*     */   }
/*     */   
/*     */   public boolean isUnbound() {
/* 199 */     return this.key.equals(InputConstants.UNKNOWN);
/*     */   }
/*     */   
/*     */   public boolean matches(KeyEvent event) {
/* 203 */     if (event.key() == InputConstants.UNKNOWN.getValue()) {
/* 204 */       return (this.key.getType() == InputConstants.Type.SCANCODE && this.key.getValue() == event.scancode());
/*     */     }
/* 206 */     return (this.key.getType() == InputConstants.Type.KEYSYM && this.key.getValue() == event.key());
/*     */   }
/*     */   
/*     */   public boolean matchesMouse(MouseButtonEvent event) {
/* 210 */     return (this.key.getType() == InputConstants.Type.MOUSE && this.key.getValue() == event.button());
/*     */   }
/*     */   
/*     */   public Component getTranslatedKeyMessage() {
/* 214 */     return this.key.getDisplayName();
/*     */   }
/*     */   
/*     */   public boolean isDefault() {
/* 218 */     return this.key.equals(this.defaultKey);
/*     */   }
/*     */   
/*     */   public String saveString() {
/* 222 */     return this.key.getName();
/*     */   }
/*     */   
/*     */   public void setDown(boolean down) {
/* 226 */     this.isDown = down;
/*     */   }
/*     */   
/*     */   private void registerMapping(InputConstants.Key key) {
/* 230 */     ((List<KeyMapping>)MAP.computeIfAbsent(key, k -> new ArrayList())).add(this);
/*     */   }
/*     */   
/*     */   public static KeyMapping get(String name) {
/* 234 */     return ALL.get(name);
/*     */   }
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/KeyMapping.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */