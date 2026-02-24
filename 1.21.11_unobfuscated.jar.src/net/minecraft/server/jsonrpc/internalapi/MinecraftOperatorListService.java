package net.minecraft.server.jsonrpc.internalapi;

import java.util.Collection;
import java.util.Optional;
import net.minecraft.server.jsonrpc.methods.ClientInfo;
import net.minecraft.server.permissions.PermissionLevel;
import net.minecraft.server.players.NameAndId;
import net.minecraft.server.players.ServerOpListEntry;

public interface MinecraftOperatorListService {
  Collection<ServerOpListEntry> getEntries();
  
  void op(NameAndId paramNameAndId, Optional<PermissionLevel> paramOptional, Optional<Boolean> paramOptional1, ClientInfo paramClientInfo);
  
  void op(NameAndId paramNameAndId, ClientInfo paramClientInfo);
  
  void deop(NameAndId paramNameAndId, ClientInfo paramClientInfo);
  
  void clear(ClientInfo paramClientInfo);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/server/jsonrpc/internalapi/MinecraftOperatorListService.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */