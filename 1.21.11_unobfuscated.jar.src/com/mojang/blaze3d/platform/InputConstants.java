/*     */ package com.mojang.blaze3d.platform;
/*     */ import com.google.common.base.Suppliers;
/*     */ import com.google.common.collect.Maps;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
/*     */ import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
/*     */ import java.lang.annotation.ElementType;
/*     */ import java.lang.annotation.Retention;
/*     */ import java.lang.annotation.RetentionPolicy;
/*     */ import java.lang.annotation.Target;
/*     */ import java.lang.invoke.MethodHandle;
/*     */ import java.lang.invoke.MethodHandles;
/*     */ import java.lang.invoke.MethodType;
/*     */ import java.util.Locale;
/*     */ import java.util.OptionalInt;
/*     */ import java.util.function.BiFunction;
/*     */ import java.util.function.Supplier;
/*     */ import net.minecraft.client.input.KeyEvent;
/*     */ import net.minecraft.locale.Language;
/*     */ import net.minecraft.network.chat.Component;
/*     */ import org.lwjgl.glfw.GLFW;
/*     */ import org.lwjgl.glfw.GLFWCharModsCallbackI;
/*     */ import org.lwjgl.glfw.GLFWCursorPosCallbackI;
/*     */ import org.lwjgl.glfw.GLFWDropCallbackI;
/*     */ import org.lwjgl.glfw.GLFWKeyCallbackI;
/*     */ import org.lwjgl.glfw.GLFWMouseButtonCallbackI;
/*     */ import org.lwjgl.glfw.GLFWScrollCallbackI;
/*     */ 
/*     */ public class InputConstants {
/*     */   private static final MethodHandle GLFW_RAW_MOUSE_MOTION_SUPPORTED;
/*     */   private static final int GLFW_RAW_MOUSE_MOTION;
/*     */   public static final int KEY_0 = 48;
/*     */   public static final int KEY_1 = 49;
/*     */   public static final int KEY_2 = 50;
/*     */   public static final int KEY_3 = 51;
/*     */   public static final int KEY_4 = 52;
/*     */   public static final int KEY_5 = 53;
/*     */   
/*     */   static {
/*  39 */     MethodHandles.Lookup lookup = MethodHandles.lookup();
/*  40 */     MethodType type = MethodType.methodType(boolean.class);
/*     */     
/*  42 */     MethodHandle handle = null;
/*  43 */     int rawInput = 0;
/*     */ 
/*     */     
/*  46 */     try { handle = lookup.findStatic(GLFW.class, "glfwRawMouseMotionSupported", type);
/*  47 */       MethodHandle field = lookup.findStaticGetter(GLFW.class, "GLFW_RAW_MOUSE_MOTION", int.class);
/*  48 */       rawInput = field.invokeExact(); }
/*  49 */     catch (NoSuchMethodException|NoSuchFieldException noSuchMethodException) {  }
/*  50 */     catch (Throwable e)
/*  51 */     { throw new RuntimeException(e); }
/*     */ 
/*     */     
/*  54 */     GLFW_RAW_MOUSE_MOTION_SUPPORTED = handle;
/*  55 */     GLFW_RAW_MOUSE_MOTION = rawInput;
/*     */   }
/*     */ 
/*     */   
/*     */   public static final int KEY_6 = 54;
/*     */   
/*     */   public static final int KEY_7 = 55;
/*     */   
/*     */   public static final int KEY_8 = 56;
/*     */   
/*     */   public static final int KEY_9 = 57;
/*     */   
/*     */   public static final int KEY_A = 65;
/*     */   
/*     */   public static final int KEY_B = 66;
/*     */   
/*     */   public static final int KEY_C = 67;
/*     */   
/*     */   public static final int KEY_D = 68;
/*     */   
/*     */   public static final int KEY_E = 69;
/*     */   
/*     */   public static final int KEY_F = 70;
/*     */   
/*     */   public static final int KEY_G = 71;
/*     */   
/*     */   public static final int KEY_H = 72;
/*     */   
/*     */   public static final int KEY_I = 73;
/*     */   
/*     */   public static final int KEY_J = 74;
/*     */   
/*     */   public static final int KEY_K = 75;
/*     */   
/*     */   public static final int KEY_L = 76;
/*     */   
/*     */   public static final int KEY_M = 77;
/*     */   
/*     */   public static final int KEY_N = 78;
/*     */   
/*     */   public static final int KEY_O = 79;
/*     */   
/*     */   public static final int KEY_P = 80;
/*     */   public static final int KEY_Q = 81;
/*     */   public static final int KEY_R = 82;
/*     */   public static final int KEY_S = 83;
/*     */   public static final int KEY_T = 84;
/*     */   public static final int KEY_U = 85;
/*     */   public static final int KEY_V = 86;
/*     */   public static final int KEY_W = 87;
/*     */   public static final int KEY_X = 88;
/*     */   public static final int KEY_Y = 89;
/*     */   public static final int KEY_Z = 90;
/*     */   public static final int KEY_F1 = 290;
/*     */   public static final int KEY_F2 = 291;
/*     */   public static final int KEY_F3 = 292;
/*     */   public static final int KEY_F4 = 293;
/*     */   public static final int KEY_F5 = 294;
/*     */   public static final int KEY_F6 = 295;
/*     */   public static final int KEY_F7 = 296;
/*     */   public static final int KEY_F8 = 297;
/*     */   public static final int KEY_F9 = 298;
/*     */   public static final int KEY_F10 = 299;
/*     */   public static final int KEY_F11 = 300;
/*     */   public static final int KEY_F12 = 301;
/*     */   public static final int KEY_F13 = 302;
/*     */   public static final int KEY_F14 = 303;
/*     */   public static final int KEY_F15 = 304;
/*     */   public static final int KEY_F16 = 305;
/*     */   public static final int KEY_F17 = 306;
/*     */   public static final int KEY_F18 = 307;
/*     */   public static final int KEY_F19 = 308;
/*     */   public static final int KEY_F20 = 309;
/*     */   public static final int KEY_F21 = 310;
/*     */   public static final int KEY_F22 = 311;
/*     */   public static final int KEY_F23 = 312;
/*     */   public static final int KEY_F24 = 313;
/*     */   public static final int KEY_F25 = 314;
/*     */   public static final int KEY_NUMLOCK = 282;
/*     */   public static final int KEY_NUMPAD0 = 320;
/*     */   public static final int KEY_NUMPAD1 = 321;
/*     */   public static final int KEY_NUMPAD2 = 322;
/*     */   public static final int KEY_NUMPAD3 = 323;
/*     */   public static final int KEY_NUMPAD4 = 324;
/*     */   public static final int KEY_NUMPAD5 = 325;
/*     */   public static final int KEY_NUMPAD6 = 326;
/*     */   public static final int KEY_NUMPAD7 = 327;
/*     */   public static final int KEY_NUMPAD8 = 328;
/*     */   public static final int KEY_NUMPAD9 = 329;
/*     */   public static final int KEY_NUMPADCOMMA = 330;
/*     */   public static final int KEY_NUMPADENTER = 335;
/*     */   public static final int KEY_NUMPADEQUALS = 336;
/*     */   public static final int KEY_DOWN = 264;
/*     */   public static final int KEY_LEFT = 263;
/*     */   public static final int KEY_RIGHT = 262;
/*     */   public static final int KEY_UP = 265;
/*     */   public static final int KEY_ADD = 334;
/*     */   public static final int KEY_APOSTROPHE = 39;
/*     */   public static final int KEY_BACKSLASH = 92;
/*     */   public static final int KEY_COMMA = 44;
/*     */   public static final int KEY_EQUALS = 61;
/*     */   public static final int KEY_GRAVE = 96;
/*     */   public static final int KEY_LBRACKET = 91;
/*     */   public static final int KEY_MINUS = 45;
/*     */   public static final int KEY_MULTIPLY = 332;
/*     */   public static final int KEY_PERIOD = 46;
/*     */   public static final int KEY_RBRACKET = 93;
/*     */   public static final int KEY_SEMICOLON = 59;
/*     */   public static final int KEY_SLASH = 47;
/*     */   public static final int KEY_SPACE = 32;
/*     */   public static final int KEY_TAB = 258;
/*     */   public static final int KEY_LALT = 342;
/*     */   public static final int KEY_LCONTROL = 341;
/*     */   public static final int KEY_LSHIFT = 340;
/*     */   public static final int KEY_LSUPER = 343;
/*     */   public static final int KEY_RALT = 346;
/*     */   public static final int KEY_RCONTROL = 345;
/*     */   public static final int KEY_RSHIFT = 344;
/*     */   public static final int KEY_RSUPER = 347;
/*     */   public static final int KEY_RETURN = 257;
/*     */   public static final int KEY_ESCAPE = 256;
/*     */   public static final int KEY_BACKSPACE = 259;
/*     */   public static final int KEY_DELETE = 261;
/*     */   public static final int KEY_END = 269;
/*     */   public static final int KEY_HOME = 268;
/*     */   public static final int KEY_INSERT = 260;
/*     */   public static final int KEY_PAGEDOWN = 267;
/*     */   public static final int KEY_PAGEUP = 266;
/*     */   public static final int KEY_CAPSLOCK = 280;
/*     */   public static final int KEY_PAUSE = 284;
/*     */   public static final int KEY_SCROLLLOCK = 281;
/*     */   public static final int KEY_PRINTSCREEN = 283;
/*     */   public static final int PRESS = 1;
/*     */   public static final int RELEASE = 0;
/*     */   public static final int REPEAT = 2;
/*     */   public static final int MOUSE_BUTTON_LEFT = 0;
/*     */   public static final int MOUSE_BUTTON_RIGHT = 1;
/*     */   public static final int MOUSE_BUTTON_MIDDLE = 2;
/*     */   public static final int MOUSE_BUTTON_4 = 3;
/*     */   public static final int MOUSE_BUTTON_5 = 4;
/*     */   public static final int MOUSE_BUTTON_6 = 5;
/*     */   public static final int MOUSE_BUTTON_7 = 6;
/*     */   public static final int MOUSE_BUTTON_8 = 0;
/*     */   public static final int MOD_SHIFT = 1;
/*     */   public static final int MOD_CONTROL = 2;
/*     */   public static final int MOD_ALT = 4;
/*     */   public static final int MOD_SUPER = 8;
/*     */   public static final int MOD_CAPS_LOCK = 16;
/*     */   public static final int MOD_NUM_LOCK = 32;
/*     */   public static final int CURSOR = 208897;
/*     */   public static final int CURSOR_DISABLED = 212995;
/*     */   public static final int CURSOR_NORMAL = 212993;
/* 207 */   public static final Key UNKNOWN = Type.KEYSYM.getOrCreate(-1);
/*     */   public enum Type { MOUSE, SCANCODE, KEYSYM;
/*     */     static {
/* 210 */       KEYSYM = new Type("KEYSYM", 0, "key.keyboard", (value, name) -> {
/*     */             if ("key.keyboard.unknown".equals(name)) {
/*     */               return Component.translatable(name);
/*     */             }
/*     */             String systemName = GLFW.glfwGetKeyName(value, -1);
/*     */             return (systemName != null) ? (Component)Component.literal(systemName.toUpperCase(Locale.ROOT)) : (Component)Component.translatable(name);
/*     */           });
/* 217 */       SCANCODE = new Type("SCANCODE", 1, "scancode", (value, name) -> {
/*     */             String systemName = GLFW.glfwGetKeyName(-1, value);
/*     */             return (systemName != null) ? (Component)Component.literal(systemName) : (Component)Component.translatable(name);
/*     */           });
/* 221 */       MOUSE = new Type("MOUSE", 2, "key.mouse", (value, name) -> Language.getInstance().has(name) ? (Component)Component.translatable(name) : (Component)Component.translatable("key.mouse", new Object[] { value + 1 }));
/*     */     } private final BiFunction<Integer, String, Component> displayTextSupplier; private final String defaultPrefix;
/*     */     private static void addKey(Type type, String name, int value) {
/* 224 */       InputConstants.Key key = new InputConstants.Key(name, type, value);
/* 225 */       type.map.put(value, key);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/*     */     static {
/* 231 */       addKey(KEYSYM, "key.keyboard.unknown", -1);
/*     */       
/* 233 */       addKey(MOUSE, "key.mouse.left", 0);
/* 234 */       addKey(MOUSE, "key.mouse.right", 1);
/* 235 */       addKey(MOUSE, "key.mouse.middle", 2);
/* 236 */       addKey(MOUSE, "key.mouse.4", 3);
/* 237 */       addKey(MOUSE, "key.mouse.5", 4);
/* 238 */       addKey(MOUSE, "key.mouse.6", 5);
/* 239 */       addKey(MOUSE, "key.mouse.7", 6);
/* 240 */       addKey(MOUSE, "key.mouse.8", 7);
/*     */       
/* 242 */       addKey(KEYSYM, "key.keyboard.0", 48);
/* 243 */       addKey(KEYSYM, "key.keyboard.1", 49);
/* 244 */       addKey(KEYSYM, "key.keyboard.2", 50);
/* 245 */       addKey(KEYSYM, "key.keyboard.3", 51);
/* 246 */       addKey(KEYSYM, "key.keyboard.4", 52);
/* 247 */       addKey(KEYSYM, "key.keyboard.5", 53);
/* 248 */       addKey(KEYSYM, "key.keyboard.6", 54);
/* 249 */       addKey(KEYSYM, "key.keyboard.7", 55);
/* 250 */       addKey(KEYSYM, "key.keyboard.8", 56);
/* 251 */       addKey(KEYSYM, "key.keyboard.9", 57);
/*     */       
/* 253 */       addKey(KEYSYM, "key.keyboard.a", 65);
/* 254 */       addKey(KEYSYM, "key.keyboard.b", 66);
/* 255 */       addKey(KEYSYM, "key.keyboard.c", 67);
/* 256 */       addKey(KEYSYM, "key.keyboard.d", 68);
/* 257 */       addKey(KEYSYM, "key.keyboard.e", 69);
/* 258 */       addKey(KEYSYM, "key.keyboard.f", 70);
/* 259 */       addKey(KEYSYM, "key.keyboard.g", 71);
/* 260 */       addKey(KEYSYM, "key.keyboard.h", 72);
/* 261 */       addKey(KEYSYM, "key.keyboard.i", 73);
/* 262 */       addKey(KEYSYM, "key.keyboard.j", 74);
/* 263 */       addKey(KEYSYM, "key.keyboard.k", 75);
/* 264 */       addKey(KEYSYM, "key.keyboard.l", 76);
/* 265 */       addKey(KEYSYM, "key.keyboard.m", 77);
/* 266 */       addKey(KEYSYM, "key.keyboard.n", 78);
/* 267 */       addKey(KEYSYM, "key.keyboard.o", 79);
/* 268 */       addKey(KEYSYM, "key.keyboard.p", 80);
/* 269 */       addKey(KEYSYM, "key.keyboard.q", 81);
/* 270 */       addKey(KEYSYM, "key.keyboard.r", 82);
/* 271 */       addKey(KEYSYM, "key.keyboard.s", 83);
/* 272 */       addKey(KEYSYM, "key.keyboard.t", 84);
/* 273 */       addKey(KEYSYM, "key.keyboard.u", 85);
/* 274 */       addKey(KEYSYM, "key.keyboard.v", 86);
/* 275 */       addKey(KEYSYM, "key.keyboard.w", 87);
/* 276 */       addKey(KEYSYM, "key.keyboard.x", 88);
/* 277 */       addKey(KEYSYM, "key.keyboard.y", 89);
/* 278 */       addKey(KEYSYM, "key.keyboard.z", 90);
/*     */       
/* 280 */       addKey(KEYSYM, "key.keyboard.f1", 290);
/* 281 */       addKey(KEYSYM, "key.keyboard.f2", 291);
/* 282 */       addKey(KEYSYM, "key.keyboard.f3", 292);
/* 283 */       addKey(KEYSYM, "key.keyboard.f4", 293);
/* 284 */       addKey(KEYSYM, "key.keyboard.f5", 294);
/* 285 */       addKey(KEYSYM, "key.keyboard.f6", 295);
/* 286 */       addKey(KEYSYM, "key.keyboard.f7", 296);
/* 287 */       addKey(KEYSYM, "key.keyboard.f8", 297);
/* 288 */       addKey(KEYSYM, "key.keyboard.f9", 298);
/* 289 */       addKey(KEYSYM, "key.keyboard.f10", 299);
/* 290 */       addKey(KEYSYM, "key.keyboard.f11", 300);
/* 291 */       addKey(KEYSYM, "key.keyboard.f12", 301);
/* 292 */       addKey(KEYSYM, "key.keyboard.f13", 302);
/* 293 */       addKey(KEYSYM, "key.keyboard.f14", 303);
/* 294 */       addKey(KEYSYM, "key.keyboard.f15", 304);
/* 295 */       addKey(KEYSYM, "key.keyboard.f16", 305);
/* 296 */       addKey(KEYSYM, "key.keyboard.f17", 306);
/* 297 */       addKey(KEYSYM, "key.keyboard.f18", 307);
/* 298 */       addKey(KEYSYM, "key.keyboard.f19", 308);
/* 299 */       addKey(KEYSYM, "key.keyboard.f20", 309);
/* 300 */       addKey(KEYSYM, "key.keyboard.f21", 310);
/* 301 */       addKey(KEYSYM, "key.keyboard.f22", 311);
/* 302 */       addKey(KEYSYM, "key.keyboard.f23", 312);
/* 303 */       addKey(KEYSYM, "key.keyboard.f24", 313);
/* 304 */       addKey(KEYSYM, "key.keyboard.f25", 314);
/*     */       
/* 306 */       addKey(KEYSYM, "key.keyboard.num.lock", 282);
/* 307 */       addKey(KEYSYM, "key.keyboard.keypad.0", 320);
/* 308 */       addKey(KEYSYM, "key.keyboard.keypad.1", 321);
/* 309 */       addKey(KEYSYM, "key.keyboard.keypad.2", 322);
/* 310 */       addKey(KEYSYM, "key.keyboard.keypad.3", 323);
/* 311 */       addKey(KEYSYM, "key.keyboard.keypad.4", 324);
/* 312 */       addKey(KEYSYM, "key.keyboard.keypad.5", 325);
/* 313 */       addKey(KEYSYM, "key.keyboard.keypad.6", 326);
/* 314 */       addKey(KEYSYM, "key.keyboard.keypad.7", 327);
/* 315 */       addKey(KEYSYM, "key.keyboard.keypad.8", 328);
/* 316 */       addKey(KEYSYM, "key.keyboard.keypad.9", 329);
/* 317 */       addKey(KEYSYM, "key.keyboard.keypad.add", 334);
/* 318 */       addKey(KEYSYM, "key.keyboard.keypad.decimal", 330);
/* 319 */       addKey(KEYSYM, "key.keyboard.keypad.enter", 335);
/* 320 */       addKey(KEYSYM, "key.keyboard.keypad.equal", 336);
/* 321 */       addKey(KEYSYM, "key.keyboard.keypad.multiply", 332);
/* 322 */       addKey(KEYSYM, "key.keyboard.keypad.divide", 331);
/* 323 */       addKey(KEYSYM, "key.keyboard.keypad.subtract", 333);
/*     */       
/* 325 */       addKey(KEYSYM, "key.keyboard.down", 264);
/* 326 */       addKey(KEYSYM, "key.keyboard.left", 263);
/* 327 */       addKey(KEYSYM, "key.keyboard.right", 262);
/* 328 */       addKey(KEYSYM, "key.keyboard.up", 265);
/*     */       
/* 330 */       addKey(KEYSYM, "key.keyboard.apostrophe", 39);
/* 331 */       addKey(KEYSYM, "key.keyboard.backslash", 92);
/* 332 */       addKey(KEYSYM, "key.keyboard.comma", 44);
/* 333 */       addKey(KEYSYM, "key.keyboard.equal", 61);
/* 334 */       addKey(KEYSYM, "key.keyboard.grave.accent", 96);
/* 335 */       addKey(KEYSYM, "key.keyboard.left.bracket", 91);
/* 336 */       addKey(KEYSYM, "key.keyboard.minus", 45);
/* 337 */       addKey(KEYSYM, "key.keyboard.period", 46);
/* 338 */       addKey(KEYSYM, "key.keyboard.right.bracket", 93);
/* 339 */       addKey(KEYSYM, "key.keyboard.semicolon", 59);
/* 340 */       addKey(KEYSYM, "key.keyboard.slash", 47);
/* 341 */       addKey(KEYSYM, "key.keyboard.space", 32);
/* 342 */       addKey(KEYSYM, "key.keyboard.tab", 258);
/*     */       
/* 344 */       addKey(KEYSYM, "key.keyboard.left.alt", 342);
/* 345 */       addKey(KEYSYM, "key.keyboard.left.control", 341);
/* 346 */       addKey(KEYSYM, "key.keyboard.left.shift", 340);
/* 347 */       addKey(KEYSYM, "key.keyboard.left.win", 343);
/* 348 */       addKey(KEYSYM, "key.keyboard.right.alt", 346);
/* 349 */       addKey(KEYSYM, "key.keyboard.right.control", 345);
/* 350 */       addKey(KEYSYM, "key.keyboard.right.shift", 344);
/* 351 */       addKey(KEYSYM, "key.keyboard.right.win", 347);
/*     */       
/* 353 */       addKey(KEYSYM, "key.keyboard.enter", 257);
/* 354 */       addKey(KEYSYM, "key.keyboard.escape", 256);
/*     */       
/* 356 */       addKey(KEYSYM, "key.keyboard.backspace", 259);
/* 357 */       addKey(KEYSYM, "key.keyboard.delete", 261);
/* 358 */       addKey(KEYSYM, "key.keyboard.end", 269);
/* 359 */       addKey(KEYSYM, "key.keyboard.home", 268);
/* 360 */       addKey(KEYSYM, "key.keyboard.insert", 260);
/* 361 */       addKey(KEYSYM, "key.keyboard.page.down", 267);
/* 362 */       addKey(KEYSYM, "key.keyboard.page.up", 266);
/*     */       
/* 364 */       addKey(KEYSYM, "key.keyboard.caps.lock", 280);
/* 365 */       addKey(KEYSYM, "key.keyboard.pause", 284);
/* 366 */       addKey(KEYSYM, "key.keyboard.scroll.lock", 281);
/*     */       
/* 368 */       addKey(KEYSYM, "key.keyboard.menu", 348);
/* 369 */       addKey(KEYSYM, "key.keyboard.print.screen", 283);
/* 370 */       addKey(KEYSYM, "key.keyboard.world.1", 161);
/* 371 */       addKey(KEYSYM, "key.keyboard.world.2", 162);
/*     */     }
/*     */     
/* 374 */     private final Int2ObjectMap<InputConstants.Key> map = (Int2ObjectMap<InputConstants.Key>)new Int2ObjectOpenHashMap();
/*     */ 
/*     */     
/*     */     private static final String KEY_KEYBOARD_UNKNOWN = "key.keyboard.unknown";
/*     */ 
/*     */     
/*     */     Type(String defaultPrefix, BiFunction<Integer, String, Component> displayTextSupplier) {
/* 381 */       this.defaultPrefix = defaultPrefix;
/* 382 */       this.displayTextSupplier = displayTextSupplier;
/*     */     }
/*     */     
/*     */     public InputConstants.Key getOrCreate(int value) {
/* 386 */       return (InputConstants.Key)this.map.computeIfAbsent(value, intValue -> {
/*     */             int humanReadableValue = intValue;
/*     */             if (this == MOUSE) {
/*     */               humanReadableValue++;
/*     */             }
/*     */             String name = this.defaultPrefix + "." + this.defaultPrefix;
/*     */             return new InputConstants.Key(name, this, intValue);
/*     */           });
/*     */     } }
/*     */ 
/*     */ 
/*     */   
/*     */   public static final class Key
/*     */   {
/*     */     private final String name;
/*     */     private final InputConstants.Type type;
/*     */     private final int value;
/*     */     private final Supplier<Component> displayName;
/* 404 */     private static final java.util.Map<String, Key> NAME_MAP = Maps.newHashMap();
/*     */     
/*     */     private Key(String name, InputConstants.Type type, int value) {
/* 407 */       this.name = name;
/* 408 */       this.type = type;
/* 409 */       this.value = value;
/*     */       
/* 411 */       this.displayName = (Supplier<Component>)Suppliers.memoize(() -> (Component)type.displayTextSupplier.apply(value, name));
/* 412 */       NAME_MAP.put(name, this);
/*     */     }
/*     */     
/*     */     public InputConstants.Type getType() {
/* 416 */       return this.type;
/*     */     }
/*     */     
/*     */     public int getValue() {
/* 420 */       return this.value;
/*     */     }
/*     */     
/*     */     public String getName() {
/* 424 */       return this.name;
/*     */     }
/*     */     
/*     */     public Component getDisplayName() {
/* 428 */       return this.displayName.get();
/*     */     }
/*     */     
/*     */     public OptionalInt getNumericKeyValue() {
/* 432 */       if (this.value >= 48 && this.value <= 57) {
/* 433 */         return OptionalInt.of(this.value - 48);
/*     */       }
/* 435 */       if (this.value >= 320 && this.value <= 329) {
/* 436 */         return OptionalInt.of(this.value - 320);
/*     */       }
/* 438 */       return OptionalInt.empty();
/*     */     }
/*     */ 
/*     */     
/*     */     public boolean equals(Object o) {
/* 443 */       if (this == o) {
/* 444 */         return true;
/*     */       }
/* 446 */       if (o == null || getClass() != o.getClass()) {
/* 447 */         return false;
/*     */       }
/* 449 */       Key key = (Key)o;
/* 450 */       return (this.value == key.value && this.type == key.type);
/*     */     }
/*     */ 
/*     */     
/*     */     public int hashCode() {
/* 455 */       return java.util.Objects.hash(new Object[] { this.type, this.value });
/*     */     }
/*     */ 
/*     */     
/*     */     public String toString() {
/* 460 */       return this.name;
/*     */     }
/*     */   }
/*     */   
/*     */   public static Key getKey(KeyEvent event) {
/* 465 */     if (event.key() == -1) {
/* 466 */       return Type.SCANCODE.getOrCreate(event.scancode());
/*     */     }
/* 468 */     return Type.KEYSYM.getOrCreate(event.key());
/*     */   }
/*     */   
/*     */   public static Key getKey(String name) {
/* 472 */     if (Key.NAME_MAP.containsKey(name)) {
/* 473 */       return Key.NAME_MAP.get(name);
/*     */     }
/*     */     
/* 476 */     for (Type type : Type.values()) {
/* 477 */       if (name.startsWith(type.defaultPrefix)) {
/* 478 */         String humanReadableValue = name.substring(type.defaultPrefix.length() + 1);
/* 479 */         int intValue = Integer.parseInt(humanReadableValue);
/* 480 */         if (type == Type.MOUSE) {
/* 481 */           intValue--;
/*     */         }
/* 483 */         return type.getOrCreate(intValue);
/*     */       } 
/*     */     } 
/* 486 */     throw new IllegalArgumentException("Unknown key name: " + name);
/*     */   }
/*     */   
/*     */   public static boolean isKeyDown(Window window, int key) {
/* 490 */     return (GLFW.glfwGetKey(window.handle(), key) == 1);
/*     */   }
/*     */   
/*     */   public static void setupKeyboardCallbacks(Window window, GLFWKeyCallbackI keyPressCallback, GLFWCharModsCallbackI charTypedCallback) {
/* 494 */     GLFW.glfwSetKeyCallback(window.handle(), keyPressCallback);
/* 495 */     GLFW.glfwSetCharModsCallback(window.handle(), charTypedCallback);
/*     */   }
/*     */   
/*     */   public static void setupMouseCallbacks(Window window, GLFWCursorPosCallbackI onMoveCallback, GLFWMouseButtonCallbackI onPressCallback, GLFWScrollCallbackI onScrollCallback, GLFWDropCallbackI onDropCallback) {
/* 499 */     GLFW.glfwSetCursorPosCallback(window.handle(), onMoveCallback);
/* 500 */     GLFW.glfwSetMouseButtonCallback(window.handle(), onPressCallback);
/* 501 */     GLFW.glfwSetScrollCallback(window.handle(), onScrollCallback);
/* 502 */     GLFW.glfwSetDropCallback(window.handle(), onDropCallback);
/*     */   }
/*     */   
/*     */   public static void grabOrReleaseMouse(Window window, int cursorMode, double xpos, double ypos) {
/* 506 */     GLFW.glfwSetCursorPos(window.handle(), xpos, ypos);
/* 507 */     GLFW.glfwSetInputMode(window.handle(), 208897, cursorMode);
/*     */   }
/*     */   
/*     */   public static boolean isRawMouseInputSupported() {
/*     */     try {
/* 512 */       return (GLFW_RAW_MOUSE_MOTION_SUPPORTED != null && GLFW_RAW_MOUSE_MOTION_SUPPORTED.invokeExact());
/* 513 */     } catch (Throwable throwable) {
/* 514 */       throw new RuntimeException(throwable);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void updateRawMouseInput(Window window, boolean value) {
/* 519 */     if (isRawMouseInputSupported())
/* 520 */       GLFW.glfwSetInputMode(window.handle(), GLFW_RAW_MOUSE_MOTION, value ? 1 : 0); 
/*     */   }
/*     */   
/*     */   @Retention(RetentionPolicy.CLASS)
/*     */   @Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE, ElementType.METHOD, ElementType.TYPE_USE})
/*     */   public static @interface Value {}
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/com/mojang/blaze3d/platform/InputConstants.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */