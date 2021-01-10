/*
 * Copyright (c) 2018 superblaubeere27
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package uwu.smsgamer.pasteclient.injection.mixins.client;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.types.EventType;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.*;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import uwu.smsgamer.pasteclient.events.PacketEvent;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.*;

import java.util.Queue;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Mixin(NetworkManager.class)
public abstract class MixinNetworkManager {

    @Shadow public abstract boolean isChannelOpen();

    @Shadow protected abstract void flushOutboundQueue();

    @Shadow protected abstract void dispatchPacket(Packet<?> p_dispatchPacket_1_, @Nullable GenericFutureListener<? extends Future<? super Void>>[] p_dispatchPacket_2_);

    @Shadow @Final private ReentrantReadWriteLock readWriteLock;

    @Shadow @Final private Queue outboundPacketsQueue;

    @Inject(method = "channelRead0", at = @At(value = "INVOKE", target = "Lnet/minecraft/network/Packet;processPacket(Lnet/minecraft/network/INetHandler;)V", shift = At.Shift.BEFORE), cancellable = true)
    private void packetReceived(ChannelHandlerContext p_channelRead0_1_, Packet<?> packet, CallbackInfo ci) {
        PacketEvent event = new PacketEvent(EventType.RECIEVE, packet);
        EventManager.call(event);
        if (event.isCancelled()) ci.cancel();
    }

    /**
     * @author Sms_Gamer_3808
     */
    @Overwrite
    public void sendPacket(Packet<?> packetIn) {
        PacketEvent event = new PacketEvent(EventType.SEND, packetIn);
        EventManager.call(event);
        if (event.isCancelled()) return;
        packetIn = event.getPacket();
        if (this.isChannelOpen()) {
            this.flushOutboundQueue();
            this.dispatchPacket(packetIn, null);
        } else {
            this.readWriteLock.writeLock().lock();

            try {
                this.outboundPacketsQueue.add(new InboundHandlerTuplePacketListener(packetIn, new GenericFutureListener[0]));
            } finally {
                this.readWriteLock.writeLock().unlock();
            }
        }
    }

    @Mixin(targets = "net.minecraft.network.NetworkManager$InboundHandlerTuplePacketListener")
    static class InboundHandlerTuplePacketListener {
        private final Packet<?> packet;
        private final GenericFutureListener<? extends Future<? super Void>>[] futureListeners;

        public InboundHandlerTuplePacketListener(Packet<?> p_i45146_1_, GenericFutureListener<? extends Future<? super Void>>... p_i45146_2_) {
            this.packet = p_i45146_1_;
            this.futureListeners = p_i45146_2_;
        }
    }
}
