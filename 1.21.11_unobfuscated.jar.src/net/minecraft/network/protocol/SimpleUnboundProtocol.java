package net.minecraft.network.protocol;

import io.netty.buffer.ByteBuf;
import java.util.function.Function;
import net.minecraft.network.ProtocolInfo;

public interface SimpleUnboundProtocol<T extends net.minecraft.network.PacketListener, B extends ByteBuf> extends ProtocolInfo.DetailsProvider {
  ProtocolInfo<T> bind(Function<ByteBuf, B> paramFunction);
}


/* Location:              /home/carlos/.minecraft/versions/1.21.11_unobfuscated/1.21.11_unobfuscated.jar!/net/minecraft/network/protocol/SimpleUnboundProtocol.class
 * Java compiler version: 21 (65.0)
 * JD-Core Version:       1.2.3
 */