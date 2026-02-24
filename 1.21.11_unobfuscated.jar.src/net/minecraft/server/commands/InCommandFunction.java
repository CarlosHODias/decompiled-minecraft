package net.minecraft.server.commands;

import com.mojang.brigadier.exceptions.CommandSyntaxException;

@FunctionalInterface
public interface InCommandFunction<T, R> {
  R apply(T paramT) throws CommandSyntaxException;
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/commands/InCommandFunction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */