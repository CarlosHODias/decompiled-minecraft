/*    */ package net.minecraft.realms;
/*    */ 
/*    */ import com.google.common.collect.Lists;
/*    */ import java.util.Collection;
/*    */ import java.util.List;
/*    */ import java.util.stream.Collectors;
/*    */ import net.minecraft.client.gui.components.ImageWidget;
/*    */ import net.minecraft.client.gui.screens.Screen;
/*    */ import net.minecraft.network.chat.CommonComponents;
/*    */ import net.minecraft.network.chat.Component;
/*    */ import net.minecraft.resources.Identifier;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public abstract class RealmsScreen
/*    */   extends Screen
/*    */ {
/*    */   protected static final int TITLE_HEIGHT = 17;
/*    */   protected static final int EXPIRATION_NOTIFICATION_DAYS = 7;
/*    */   protected static final long SIZE_LIMIT = 5368709120L;
/*    */   protected static final int COLOR_DARK_GRAY = -11776948;
/*    */   protected static final int COLOR_MEDIUM_GRAY = -9671572;
/*    */   protected static final int COLOR_GREEN = -8388737;
/*    */   protected static final int COLOR_LINK = -13408581;
/*    */   protected static final int COLOR_LINK_HOVER = -9670204;
/*    */   protected static final int SKIN_FACE_SIZE = 32;
/*    */   protected static final int HARDCORE_HEART_SIZE = 8;
/* 29 */   protected static final Identifier LOGO_LOCATION = Identifier.withDefaultNamespace("textures/gui/title/realms.png");
/*    */   
/*    */   protected static final int LOGO_WIDTH = 128;
/*    */   protected static final int LOGO_HEIGHT = 34;
/*    */   protected static final int LOGO_TEXTURE_WIDTH = 128;
/*    */   protected static final int LOGO_TEXTURE_HEIGHT = 64;
/* 35 */   private final List<RealmsLabel> labels = Lists.newArrayList();
/*    */   
/*    */   public RealmsScreen(Component title) {
/* 38 */     super(title);
/*    */   }
/*    */   
/*    */   protected static int row(int i) {
/* 42 */     return 40 + i * 13;
/*    */   }
/*    */   
/*    */   protected RealmsLabel addLabel(RealmsLabel label) {
/* 46 */     this.labels.add(label);
/* 47 */     return (RealmsLabel)addRenderableOnly(label);
/*    */   }
/*    */   
/*    */   public Component createLabelNarration() {
/* 51 */     return CommonComponents.joinLines((Collection)this.labels.stream().map(RealmsLabel::getText).collect(Collectors.toList()));
/*    */   }
/*    */   
/*    */   protected static ImageWidget realmsLogo() {
/* 55 */     return ImageWidget.texture(128, 34, LOGO_LOCATION, 128, 64);
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/realms/RealmsScreen.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */