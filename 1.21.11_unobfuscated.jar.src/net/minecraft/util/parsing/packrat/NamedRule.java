package net.minecraft.util.parsing.packrat;

public interface NamedRule<S, T> {
  Atom<T> name();
  
  Rule<S, T> value();
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/util/parsing/packrat/NamedRule.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */