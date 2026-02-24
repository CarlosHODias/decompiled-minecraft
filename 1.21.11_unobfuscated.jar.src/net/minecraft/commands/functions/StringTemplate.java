/*    */ package net.minecraft.commands.functions;
/*    */ 
/*    */ public final class StringTemplate extends Record {
/*    */   private final java.util.List<String> segments;
/*    */   private final java.util.List<String> variables;
/*    */   
/*  7 */   public StringTemplate(java.util.List<String> segments, java.util.List<String> variables) { this.segments = segments; this.variables = variables; } public final String toString() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> toString : (Lnet/minecraft/commands/functions/StringTemplate;)Ljava/lang/String;
/*    */     //   6: areturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*  7 */     //   0	7	0	this	Lnet/minecraft/commands/functions/StringTemplate; } public java.util.List<String> segments() { return this.segments; } public final int hashCode() { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: <illegal opcode> hashCode : (Lnet/minecraft/commands/functions/StringTemplate;)I
/*    */     //   6: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	7	0	this	Lnet/minecraft/commands/functions/StringTemplate; } public final boolean equals(Object o) { // Byte code:
/*    */     //   0: aload_0
/*    */     //   1: aload_1
/*    */     //   2: <illegal opcode> equals : (Lnet/minecraft/commands/functions/StringTemplate;Ljava/lang/Object;)Z
/*    */     //   7: ireturn
/*    */     // Line number table:
/*    */     //   Java source line number -> byte code offset
/*    */     //   #7	-> 0
/*    */     // Local variable table:
/*    */     //   start	length	slot	name	descriptor
/*    */     //   0	8	0	this	Lnet/minecraft/commands/functions/StringTemplate;
/*  7 */     //   0	8	1	o	Ljava/lang/Object; } public java.util.List<String> variables() { return this.variables; }
/*    */   
/*    */   public static StringTemplate fromString(String input) {
/* 10 */     com.google.common.collect.ImmutableList.Builder<String> segments = com.google.common.collect.ImmutableList.builder();
/* 11 */     com.google.common.collect.ImmutableList.Builder<String> variables = com.google.common.collect.ImmutableList.builder();
/*    */     
/* 13 */     int length = input.length();
/* 14 */     int start = 0;
/* 15 */     int index = input.indexOf('$');
/* 16 */     while (index != -1) {
/* 17 */       if (index == length - 1 || input.charAt(index + 1) != '(') {
/* 18 */         index = input.indexOf('$', index + 1);
/*    */         
/*    */         continue;
/*    */       } 
/* 22 */       segments.add(input.substring(start, index));
/* 23 */       int variableEnd = input.indexOf(')', index + 1);
/* 24 */       if (variableEnd == -1) {
/* 25 */         throw new IllegalArgumentException("Unterminated macro variable");
/*    */       }
/* 27 */       String variable = input.substring(index + 2, variableEnd);
/* 28 */       if (!isValidVariableName(variable)) {
/* 29 */         throw new IllegalArgumentException("Invalid macro variable name '" + variable + "'");
/*    */       }
/* 31 */       variables.add(variable);
/* 32 */       start = variableEnd + 1;
/* 33 */       index = input.indexOf('$', start);
/*    */     } 
/* 35 */     if (start == 0) {
/* 36 */       throw new IllegalArgumentException("No variables in macro");
/*    */     }
/* 38 */     if (start != length) {
/* 39 */       segments.add(input.substring(start));
/*    */     }
/* 41 */     return new StringTemplate((java.util.List<String>)segments.build(), (java.util.List<String>)variables.build());
/*    */   }
/*    */   
/*    */   public static boolean isValidVariableName(String variable) {
/* 45 */     for (int i = 0; i < variable.length(); i++) {
/* 46 */       char character = variable.charAt(i);
/* 47 */       if (!Character.isLetterOrDigit(character) && character != '_') {
/* 48 */         return false;
/*    */       }
/*    */     } 
/* 51 */     return true;
/*    */   }
/*    */   
/*    */   public String substitute(java.util.List<String> arguments) {
/* 55 */     StringBuilder builder = new StringBuilder();
/* 56 */     for (int i = 0; i < this.variables.size(); i++) {
/* 57 */       builder.append(this.segments.get(i)).append(arguments.get(i));
/* 58 */       CommandFunction.checkCommandLineLength(builder);
/*    */     } 
/*    */     
/* 61 */     if (this.segments.size() > this.variables.size()) {
/* 62 */       builder.append(this.segments.getLast());
/*    */     }
/* 64 */     CommandFunction.checkCommandLineLength(builder);
/* 65 */     return builder.toString();
/*    */   }
/*    */ }


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/functions/StringTemplate.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */