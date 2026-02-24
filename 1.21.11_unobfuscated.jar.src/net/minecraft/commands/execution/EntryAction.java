package net.minecraft.commands.execution;

@FunctionalInterface
public interface EntryAction<T> {
  void execute(ExecutionContext<T> paramExecutionContext, Frame paramFrame);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/commands/execution/EntryAction.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */