/*     */ package net.minecraft.client;
/*     */ 
/*     */ import com.mojang.blaze3d.Blaze3D;
/*     */ import com.mojang.blaze3d.platform.InputConstants;
/*     */ import com.mojang.blaze3d.platform.Window;
/*     */ import com.mojang.logging.LogUtils;
/*     */ import java.nio.file.InvalidPathException;
/*     */ import java.nio.file.Path;
/*     */ import java.nio.file.Paths;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Locale;
/*     */ import net.minecraft.CrashReport;
/*     */ import net.minecraft.CrashReportCategory;
/*     */ import net.minecraft.ReportedException;
/*     */ import net.minecraft.client.gui.Font;
/*     */ import net.minecraft.client.gui.GuiGraphics;
/*     */ import net.minecraft.client.gui.components.toasts.SystemToast;
/*     */ import net.minecraft.client.gui.screens.Screen;
/*     */ import net.minecraft.client.input.InputQuirks;
/*     */ import net.minecraft.client.input.MouseButtonEvent;
/*     */ import net.minecraft.client.input.MouseButtonInfo;
/*     */ import net.minecraft.client.input.MouseButtonInfo.Action;
/*     */ import net.minecraft.client.input.MouseButtonInfo.MouseButton;
/*     */ import net.minecraft.util.Mth;
/*     */ import net.minecraft.util.SmoothDouble;
/*     */ import net.minecraft.util.Util;
/*     */ import net.minecraft.world.entity.player.Inventory;
/*     */ import org.joml.Vector2i;
/*     */ import org.lwjgl.glfw.GLFWDropCallback;
/*     */ import org.slf4j.Logger;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class MouseHandler
/*     */ {
/*  37 */   private static final Logger LOGGER = LogUtils.getLogger();
/*     */   
/*     */   public static final long DOUBLE_CLICK_THRESHOLD_MS = 250L;
/*     */   
/*     */   private final Minecraft minecraft;
/*     */   
/*     */   private boolean isLeftPressed;
/*     */   
/*     */   private boolean isMiddlePressed;
/*     */   
/*     */   private boolean isRightPressed;
/*     */   
/*     */   private double xpos;
/*     */   
/*     */   private double ypos;
/*     */   
/*     */   private LastClick lastClick;
/*     */   @MouseButtonInfo.MouseButton
/*     */   protected int lastClickButton;
/*     */   private int fakeRightMouse;
/*  57 */   private MouseButtonInfo activeButton = null;
/*     */   
/*     */   private boolean ignoreFirstMove = true;
/*     */   
/*     */   private int clickDepth;
/*     */   
/*     */   private double mousePressedTime;
/*  64 */   private final SmoothDouble smoothTurnX = new SmoothDouble();
/*  65 */   private final SmoothDouble smoothTurnY = new SmoothDouble();
/*     */   
/*     */   private double accumulatedDX;
/*     */   private double accumulatedDY;
/*     */   private final ScrollWheelHandler scrollWheelHandler;
/*  70 */   private double lastHandleMovementTime = Double.MIN_VALUE; private boolean mouseGrabbed;
/*     */   
/*     */   public MouseHandler(Minecraft minecraft) {
/*  73 */     this.minecraft = minecraft;
/*  74 */     this.scrollWheelHandler = new ScrollWheelHandler();
/*     */   }
/*     */   
/*     */   private void onButton(long handle, MouseButtonInfo rawButtonInfo, @MouseButtonInfo.Action int action) {
/*  78 */     Window window = this.minecraft.getWindow();
/*  79 */     if (handle != window.handle()) {
/*     */       return;
/*     */     }
/*     */     
/*  83 */     this.minecraft.getFramerateLimitTracker().onInputReceived();
/*     */     
/*  85 */     if (this.minecraft.screen != null) {
/*  86 */       this.minecraft.setLastInputType(InputType.MOUSE);
/*     */     }
/*     */     
/*  89 */     boolean pressed = (action == 1);
/*     */     
/*  91 */     MouseButtonInfo buttonInfo = simulateRightClick(rawButtonInfo, pressed);
/*  92 */     if (pressed) {
/*  93 */       if ((Boolean)this.minecraft.options.touchscreen().get() && this.clickDepth++ > 0) {
/*     */         return;
/*     */       }
/*  96 */       this.activeButton = buttonInfo;
/*  97 */       this.mousePressedTime = Blaze3D.getTime();
/*  98 */     } else if (this.activeButton != null) {
/*  99 */       if ((Boolean)this.minecraft.options.touchscreen().get() && --this.clickDepth > 0) {
/*     */         return;
/*     */       }
/* 102 */       this.activeButton = null;
/*     */     } 
/*     */     
/* 105 */     if (this.minecraft.getOverlay() == null)
/*     */     {
/* 107 */       if (this.minecraft.screen == null) {
/* 108 */         if (!this.mouseGrabbed && pressed) {
/* 109 */           grabMouse();
/*     */         }
/*     */       } else {
/* 112 */         double xm = getScaledXPos(window);
/* 113 */         double ym = getScaledYPos(window);
/* 114 */         Screen screen = this.minecraft.screen;
/* 115 */         MouseButtonEvent event = new MouseButtonEvent(xm, ym, buttonInfo);
/*     */         
/* 117 */         if (pressed) {
/* 118 */           screen.afterMouseAction();
/*     */           try {
/* 120 */             long currentTime = Util.getMillis();
/* 121 */             boolean doubleClick = (this.lastClick != null && currentTime - this.lastClick.time() < 250L && this.lastClick.screen() == screen && this.lastClickButton == event.button());
/* 122 */             if (screen.mouseClicked(event, doubleClick)) {
/* 123 */               this.lastClick = new LastClick(currentTime, screen);
/* 124 */               this.lastClickButton = buttonInfo.button();
/*     */               return;
/*     */             } 
/* 127 */           } catch (Throwable t) {
/* 128 */             CrashReport report = CrashReport.forThrowable(t, "mouseClicked event handler");
/* 129 */             screen.fillCrashDetails(report);
/* 130 */             CrashReportCategory mouseDetails = report.addCategory("Mouse");
/* 131 */             fillMousePositionDetails(mouseDetails, window);
/* 132 */             mouseDetails.setDetail("Button", event.button());
/* 133 */             throw new ReportedException(report);
/*     */           } 
/*     */         } else {
/*     */           try {
/* 137 */             if (screen.mouseReleased(event)) {
/*     */               return;
/*     */             }
/* 140 */           } catch (Throwable t) {
/* 141 */             CrashReport report = CrashReport.forThrowable(t, "mouseReleased event handler");
/* 142 */             screen.fillCrashDetails(report);
/* 143 */             CrashReportCategory mouseDetails = report.addCategory("Mouse");
/*     */             
/* 145 */             fillMousePositionDetails(mouseDetails, window);
/*     */             
/* 147 */             mouseDetails.setDetail("Button", event.button());
/* 148 */             throw new ReportedException(report);
/*     */           } 
/*     */         } 
/*     */       }  } 
/* 152 */     if (this.minecraft.screen == null && this.minecraft.getOverlay() == null) {
/* 153 */       if (buttonInfo.button() == 0) {
/* 154 */         this.isLeftPressed = pressed;
/* 155 */       } else if (buttonInfo.button() == 2) {
/* 156 */         this.isMiddlePressed = pressed;
/* 157 */       } else if (buttonInfo.button() == 1) {
/* 158 */         this.isRightPressed = pressed;
/*     */       } 
/*     */       
/* 161 */       InputConstants.Key mouseKey = InputConstants.Type.MOUSE.getOrCreate(buttonInfo.button());
/* 162 */       KeyMapping.set(mouseKey, pressed);
/* 163 */       if (pressed) {
/* 164 */         KeyMapping.click(mouseKey);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private MouseButtonInfo simulateRightClick(MouseButtonInfo info, boolean pressed) {
/* 170 */     if (InputQuirks.SIMULATE_RIGHT_CLICK_WITH_LONG_LEFT_CLICK && info.button() == 0) {
/* 171 */       if (pressed) {
/* 172 */         if ((info.modifiers() & 0x2) == 2) {
/* 173 */           this.fakeRightMouse++;
/* 174 */           return new MouseButtonInfo(1, info.modifiers());
/*     */         } 
/* 176 */       } else if (this.fakeRightMouse > 0) {
/* 177 */         this.fakeRightMouse--;
/* 178 */         return new MouseButtonInfo(1, info.modifiers());
/*     */       } 
/*     */     }
/* 181 */     return info;
/*     */   }
/*     */   
/*     */   public void fillMousePositionDetails(CrashReportCategory category, Window window) {
/* 185 */     category.setDetail("Mouse location", () -> String.format(Locale.ROOT, "Scaled: (%f, %f). Absolute: (%f, %f)", new Object[] { getScaledXPos(window, this.xpos), getScaledYPos(window, this.ypos), this.xpos, this.ypos }));
/* 186 */     category.setDetail("Screen size", () -> String.format(Locale.ROOT, "Scaled: (%d, %d). Absolute: (%d, %d). Scale factor of %d", new Object[] { window.getGuiScaledWidth(), window.getGuiScaledHeight(), window.getWidth(), window.getHeight(), window.getGuiScale() }));
/*     */   }
/*     */   
/*     */   private void onScroll(long handle, double xoffset, double yoffset) {
/* 190 */     if (handle == this.minecraft.getWindow().handle()) {
/* 191 */       this.minecraft.getFramerateLimitTracker().onInputReceived();
/* 192 */       boolean discreteScroll = (Boolean)this.minecraft.options.discreteMouseScroll().get();
/* 193 */       double scrollSensitivity = (Double)this.minecraft.options.mouseWheelSensitivity().get();
/* 194 */       double scaledXOffset = (discreteScroll ? Math.signum(xoffset) : xoffset) * scrollSensitivity;
/* 195 */       double scaledYOffset = (discreteScroll ? Math.signum(yoffset) : yoffset) * scrollSensitivity;
/* 196 */       if (this.minecraft.getOverlay() == null)
/*     */       {
/* 198 */         if (this.minecraft.screen != null) {
/* 199 */           double xm = getScaledXPos(this.minecraft.getWindow());
/* 200 */           double ym = getScaledYPos(this.minecraft.getWindow());
/* 201 */           this.minecraft.screen.mouseScrolled(xm, ym, scaledXOffset, scaledYOffset);
/* 202 */           this.minecraft.screen.afterMouseAction();
/* 203 */         } else if (this.minecraft.player != null) {
/* 204 */           Vector2i wheelXY = this.scrollWheelHandler.onMouseScroll(scaledXOffset, scaledYOffset);
/* 205 */           if (wheelXY.x == 0 && wheelXY.y == 0) {
/*     */             return;
/*     */           }
/* 208 */           int wheel = (wheelXY.y == 0) ? -wheelXY.x : wheelXY.y;
/*     */           
/* 210 */           if (this.minecraft.player.isSpectator()) {
/* 211 */             if (this.minecraft.gui.getSpectatorGui().isMenuActive()) {
/* 212 */               this.minecraft.gui.getSpectatorGui().onMouseScrolled(-wheel);
/*     */             } else {
/* 214 */               float speed = Mth.clamp(this.minecraft.player.getAbilities().getFlyingSpeed() + wheelXY.y * 0.005F, 0.0F, 0.2F);
/* 215 */               this.minecraft.player.getAbilities().setFlyingSpeed(speed);
/*     */             }
/*     */           
/*     */           }
/*     */           else {
/*     */             
/* 221 */             Inventory inventory = this.minecraft.player.getInventory();
/* 222 */             inventory.setSelectedSlot(ScrollWheelHandler.getNextScrollWheelSelection(wheel, inventory.getSelectedSlot(), Inventory.getSelectionSize()));
/*     */           } 
/*     */         }  } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private void onDrop(long handle, List<Path> files, int failedCount) {
/* 229 */     this.minecraft.getFramerateLimitTracker().onInputReceived();
/* 230 */     if (this.minecraft.screen != null) {
/* 231 */       this.minecraft.screen.onFilesDrop(files);
/*     */     }
/*     */     
/* 234 */     if (failedCount > 0) {
/* 235 */       SystemToast.onFileDropFailure(this.minecraft, failedCount);
/*     */     }
/*     */   }
/*     */   
/*     */   public void setup(Window window) {
/* 240 */     InputConstants.setupMouseCallbacks(window, (window1, xpos, ypos) -> this.minecraft.execute(()), (window1, button, action, mods) -> {
/*     */           MouseButtonInfo buttonInfo = new MouseButtonInfo(button, mods);
/*     */           
/*     */           this.minecraft.execute(());
/*     */         }, (window1, xoffset, yoffset) -> this.minecraft.execute(()), (window1, count, namesPtr) -> {
/*     */           List<Path> names = new ArrayList<>(count);
/*     */           
/*     */           int failedCount = 0;
/*     */           
/*     */           for (int i = 0; i < count; i++) {
/*     */             String name = GLFWDropCallback.getName(namesPtr, i);
/*     */             
/*     */             try {
/*     */               names.add(Paths.get(name, new String[0]));
/* 254 */             } catch (InvalidPathException e) {
/*     */               failedCount++;
/*     */               LOGGER.error("Failed to parse path '{}'", name, e);
/*     */             } 
/*     */           } 
/*     */           if (!names.isEmpty()) {
/*     */             int finalFailedCount = failedCount;
/*     */             this.minecraft.execute(());
/*     */           } 
/*     */         });
/*     */   }
/*     */ 
/*     */   
/*     */   private void onMove(long handle, double xpos, double ypos) {
/* 268 */     if (handle != this.minecraft.getWindow().handle()) {
/*     */       return;
/*     */     }
/* 271 */     if (this.ignoreFirstMove) {
/* 272 */       this.xpos = xpos;
/* 273 */       this.ypos = ypos;
/* 274 */       this.ignoreFirstMove = false;
/*     */       
/*     */       return;
/*     */     } 
/* 278 */     if (this.minecraft.isWindowActive()) {
/* 279 */       this.accumulatedDX += xpos - this.xpos;
/* 280 */       this.accumulatedDY += ypos - this.ypos;
/*     */     } 
/*     */     
/* 283 */     this.xpos = xpos;
/* 284 */     this.ypos = ypos;
/*     */   }
/*     */ 
/*     */   
/*     */   public void handleAccumulatedMovement() {
/* 289 */     double time = Blaze3D.getTime();
/* 290 */     double mousea = time - this.lastHandleMovementTime;
/* 291 */     this.lastHandleMovementTime = time;
/* 292 */     if (this.minecraft.isWindowActive()) {
/* 293 */       Screen screen = this.minecraft.screen;
/* 294 */       boolean mouseMoved = (this.accumulatedDX != 0.0D || this.accumulatedDY != 0.0D);
/* 295 */       if (mouseMoved) {
/* 296 */         this.minecraft.getFramerateLimitTracker().onInputReceived();
/*     */       }
/* 298 */       if (screen != null && this.minecraft.getOverlay() == null && mouseMoved) {
/* 299 */         Window window = this.minecraft.getWindow();
/* 300 */         double xm = getScaledXPos(window);
/* 301 */         double ym = getScaledYPos(window);
/*     */         
/*     */         try {
/* 304 */           screen.mouseMoved(xm, ym);
/* 305 */         } catch (Throwable t) {
/* 306 */           CrashReport report = CrashReport.forThrowable(t, "mouseMoved event handler");
/* 307 */           screen.fillCrashDetails(report);
/* 308 */           CrashReportCategory mouseDetails = report.addCategory("Mouse");
/* 309 */           fillMousePositionDetails(mouseDetails, window);
/* 310 */           throw new ReportedException(report);
/*     */         } 
/*     */         
/* 313 */         if (this.activeButton != null && this.mousePressedTime > 0.0D) {
/* 314 */           double dx = getScaledXPos(window, this.accumulatedDX);
/* 315 */           double dy = getScaledYPos(window, this.accumulatedDY);
/*     */           try {
/* 317 */             screen.mouseDragged(new MouseButtonEvent(xm, ym, this.activeButton), dx, dy);
/* 318 */           } catch (Throwable t) {
/* 319 */             CrashReport report = CrashReport.forThrowable(t, "mouseDragged event handler");
/* 320 */             screen.fillCrashDetails(report);
/* 321 */             CrashReportCategory mouseDetails = report.addCategory("Mouse");
/* 322 */             fillMousePositionDetails(mouseDetails, window);
/* 323 */             throw new ReportedException(report);
/*     */           } 
/*     */         } 
/* 326 */         screen.afterMouseMove();
/*     */       } 
/* 328 */       if (isMouseGrabbed() && this.minecraft.player != null) {
/* 329 */         turnPlayer(mousea);
/*     */       }
/*     */     } 
/* 332 */     this.accumulatedDX = 0.0D;
/* 333 */     this.accumulatedDY = 0.0D;
/*     */   }
/*     */   
/*     */   public static double getScaledXPos(Window window, double x) {
/* 337 */     return x * window.getGuiScaledWidth() / window.getScreenWidth();
/*     */   }
/*     */   
/*     */   public double getScaledXPos(Window window) {
/* 341 */     return getScaledXPos(window, this.xpos);
/*     */   }
/*     */   
/*     */   public static double getScaledYPos(Window window, double y) {
/* 345 */     return y * window.getGuiScaledHeight() / window.getScreenHeight();
/*     */   }
/*     */   
/*     */   public double getScaledYPos(Window window) {
/* 349 */     return getScaledYPos(window, this.ypos);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private void turnPlayer(double mousea) {
/*     */     double xo, yo;
/* 356 */     double ss = (Double)this.minecraft.options.sensitivity().get() * 0.6000000238418579D + 0.20000000298023224D;
/* 357 */     double sensitivityMod = ss * ss * ss;
/* 358 */     double sens = sensitivityMod * 8.0D;
/*     */     
/* 360 */     if (this.minecraft.options.smoothCamera) {
/* 361 */       double dx = this.smoothTurnX.getNewDeltaValue(this.accumulatedDX * sens, mousea * sens);
/* 362 */       double dy = this.smoothTurnY.getNewDeltaValue(this.accumulatedDY * sens, mousea * sens);
/* 363 */       xo = dx;
/* 364 */       yo = dy;
/* 365 */     } else if (this.minecraft.options.getCameraType().isFirstPerson() && this.minecraft.player.isScoping()) {
/* 366 */       this.smoothTurnX.reset();
/* 367 */       this.smoothTurnY.reset();
/*     */       
/* 369 */       xo = this.accumulatedDX * sensitivityMod;
/* 370 */       yo = this.accumulatedDY * sensitivityMod;
/*     */     } else {
/* 372 */       this.smoothTurnX.reset();
/* 373 */       this.smoothTurnY.reset();
/*     */       
/* 375 */       xo = this.accumulatedDX * sens;
/* 376 */       yo = this.accumulatedDY * sens;
/*     */     } 
/*     */     
/* 379 */     this.minecraft.getTutorial().onMouse(xo, yo);
/* 380 */     if (this.minecraft.player != null) {
/* 381 */       this.minecraft.player.turn(
/* 382 */           (Boolean)this.minecraft.options.invertMouseX().get() ? -xo : xo, 
/* 383 */           (Boolean)this.minecraft.options.invertMouseY().get() ? -yo : yo);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLeftPressed() {
/* 389 */     return this.isLeftPressed;
/*     */   }
/*     */   
/*     */   public boolean isMiddlePressed() {
/* 393 */     return this.isMiddlePressed;
/*     */   }
/*     */   
/*     */   public boolean isRightPressed() {
/* 397 */     return this.isRightPressed;
/*     */   }
/*     */   
/*     */   public double xpos() {
/* 401 */     return this.xpos;
/*     */   }
/*     */   
/*     */   public double ypos() {
/* 405 */     return this.ypos;
/*     */   }
/*     */   
/*     */   public void setIgnoreFirstMove() {
/* 409 */     this.ignoreFirstMove = true;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMouseGrabbed() {
/* 415 */     return this.mouseGrabbed;
/*     */   }
/*     */   
/*     */   public void grabMouse() {
/* 419 */     if (!this.minecraft.isWindowActive()) {
/*     */       return;
/*     */     }
/* 422 */     if (this.mouseGrabbed) {
/*     */       return;
/*     */     }
/* 425 */     if (InputQuirks.RESTORE_KEY_STATE_AFTER_MOUSE_GRAB) {
/* 426 */       KeyMapping.setAll();
/*     */     }
/* 428 */     this.mouseGrabbed = true;
/* 429 */     this.xpos = (this.minecraft.getWindow().getScreenWidth() / 2);
/* 430 */     this.ypos = (this.minecraft.getWindow().getScreenHeight() / 2);
/* 431 */     InputConstants.grabOrReleaseMouse(this.minecraft.getWindow(), 212995, this.xpos, this.ypos);
/* 432 */     this.minecraft.setScreen(null);
/* 433 */     this.minecraft.missTime = 10000;
/* 434 */     this.ignoreFirstMove = true;
/*     */   }
/*     */   
/*     */   public void releaseMouse() {
/* 438 */     if (!this.mouseGrabbed) {
/*     */       return;
/*     */     }
/* 441 */     this.mouseGrabbed = false;
/* 442 */     this.xpos = (this.minecraft.getWindow().getScreenWidth() / 2);
/* 443 */     this.ypos = (this.minecraft.getWindow().getScreenHeight() / 2);
/* 444 */     InputConstants.grabOrReleaseMouse(this.minecraft.getWindow(), 212993, this.xpos, this.ypos);
/*     */   }
/*     */   
/*     */   public void cursorEntered() {
/* 448 */     this.ignoreFirstMove = true;
/*     */   }
/*     */   
/*     */   public void drawDebugMouseInfo(Font font, GuiGraphics graphics) {
/* 452 */     Window window = this.minecraft.getWindow();
/* 453 */     double x = getScaledXPos(window);
/* 454 */     double y = getScaledYPos(window) - 8.0D;
/* 455 */     String text = String.format(Locale.ROOT, "%.0f,%.0f", new Object[] { x, y });
/* 456 */     graphics.drawString(font, text, (int)x, (int)y, -1);
/*     */   }
/*     */   private static final class LastClick extends Record { private final long time; private final Screen screen;
/* 459 */     private LastClick(long time, Screen screen) { this.time = time; this.screen = screen; } public final String toString() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> toString : (Lnet/minecraft/client/MouseHandler$LastClick;)Ljava/lang/String;
/*     */       //   6: areturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #459	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/* 459 */       //   0	7	0	this	Lnet/minecraft/client/MouseHandler$LastClick; } public long time() { return this.time; } public final int hashCode() { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: <illegal opcode> hashCode : (Lnet/minecraft/client/MouseHandler$LastClick;)I
/*     */       //   6: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #459	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	7	0	this	Lnet/minecraft/client/MouseHandler$LastClick; } public final boolean equals(Object o) { // Byte code:
/*     */       //   0: aload_0
/*     */       //   1: aload_1
/*     */       //   2: <illegal opcode> equals : (Lnet/minecraft/client/MouseHandler$LastClick;Ljava/lang/Object;)Z
/*     */       //   7: ireturn
/*     */       // Line number table:
/*     */       //   Java source line number -> byte code offset
/*     */       //   #459	-> 0
/*     */       // Local variable table:
/*     */       //   start	length	slot	name	descriptor
/*     */       //   0	8	0	this	Lnet/minecraft/client/MouseHandler$LastClick;
/* 459 */       //   0	8	1	o	Ljava/lang/Object; } public Screen screen() { return this.screen; }
/*     */      }
/*     */ 
/*     */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/client/MouseHandler.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */