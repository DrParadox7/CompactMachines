//package dev.compactmods.machines.network;
//
//import javax.annotation.Nullable;
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.UUID;
//import java.util.function.Supplier;
//import com.google.common.collect.ImmutableSet;
//import com.mojang.serialization.Codec;
//import com.mojang.serialization.codecs.RecordCodecBuilder;
//import dev.compactmods.machines.CompactMachines;
//import dev.compactmods.machines.api.location.IDimensionalPosition;
//import dev.compactmods.machines.client.machine.MachinePlayerEventHandler;
//import dev.compactmods.machines.data.codec.CodecExtensions;
//import dev.compactmods.machines.data.persistent.CompactMachineData;
//import dev.compactmods.machines.data.persistent.MachineConnections;
//import dev.compactmods.machines.teleportation.DimensionalPosition;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.server.level.ServerPlayer;
//import net.minecraft.world.level.ChunkPos;
//import net.minecraftforge.network.NetworkEvent;
//
//public class MachinePlayersChangedPacket {
//
//    public ChunkPos machine;
//    public UUID playerID;
//    public ImmutableSet<IDimensionalPosition> machinePositions;
//    public EnumPlayerChangeType type;
//
//    public static final Codec<MachinePlayersChangedPacket> CODEC = RecordCodecBuilder.create(i -> i.group(
//            CodecExtensions.CHUNKPOS.fieldOf("machine").forGetter(MachinePlayersChangedPacket::getChunkPos),
//            CodecExtensions.UUID_CODEC.fieldOf("player").forGetter(MachinePlayersChangedPacket::getPlayer),
//            Codec.STRING.fieldOf("type").forGetter((p) -> p.type.name()),
//            DimensionalPosition.CODEC.listOf().fieldOf("positions").forGetter(p -> p.machinePositions.asList())
//    ).apply(i, MachinePlayersChangedPacket::new));
//
//    public MachinePlayersChangedPacket(FriendlyByteBuf buf) {
//        MachinePlayersChangedPacket pkt = buf.readWithCodec(MachinePlayersChangedPacket.CODEC);
//
//        machine = pkt.machine;
//        playerID = pkt.playerID;
//        machinePositions = pkt.machinePositions;
//        type = pkt.type;
//    }
//
//    private MachinePlayersChangedPacket(ChunkPos chunkPos, UUID player, String type, Collection<IDimensionalPosition> positions) {
//        this.machine = chunkPos;
//        this.playerID = player;
//        this.type = EnumPlayerChangeType.valueOf(type);
//        this.machinePositions = ImmutableSet.copyOf(positions);
//    }
//
//    public static void handle(MachinePlayersChangedPacket message, Supplier<NetworkEvent.Context> context) {
//        NetworkEvent.Context ctx = context.get();
//
//        message.machinePositions.forEach(machinePos -> {
//            CompactMachines.LOGGER.debug("Player {} machine {} via {}", message.type, message.machine, machinePos);
//            MachinePlayerEventHandler.handlePlayerMachineChanged(message.playerID, message.type, machinePos);
//        });
//
//        ctx.setPacketHandled(true);
//    }
//
//    public static void encode(MachinePlayersChangedPacket pkt, FriendlyByteBuf buf) {
//        buf.writeWithCodec(CODEC, pkt);
//    }
//
//    private UUID getPlayer() {
//        return playerID;
//    }
//
//    private ChunkPos getChunkPos() {
//        return machine;
//    }
//
//    public enum EnumPlayerChangeType {
//        ENTERED,
//        EXITED
//    }
//
//    public static class Builder {
//
//        private final MinecraftServer server;
//        private EnumPlayerChangeType change;
//        private ChunkPos chunk;
//        private UUID player;
//        private int entryPoint;
//
//        private Builder(MinecraftServer server) {
//            this.server = server;
//            this.change = EnumPlayerChangeType.EXITED;
//        }
//
//        public static Builder create(MinecraftServer server) {
//            return new Builder(server);
//        }
//
//        @Nullable
//        public MachinePlayersChangedPacket build() {
//            MachineConnections connections = MachineConnections.get(server);
//            CompactMachineData extern = CompactMachineData.get(server);
//            if(connections == null || extern == null)
//            {
//                CompactMachines.LOGGER.fatal("Could not load external machine data from server.");
//                return null;
//            }
//
//            Collection<Integer> externalMachineIDs = connections.graph.getMachinesFor(chunk);
//            HashSet<IDimensionalPosition> externalLocations = new HashSet<>();
//            for(int eid : externalMachineIDs) {
//                final var loc = extern.getMachineLocation(eid);
//                loc.ifPresent(externalLocations::add);
//            }
//
//            return new MachinePlayersChangedPacket(chunk, player, change.name(), externalLocations);
//        }
//
//        public Builder forMachine(ChunkPos insideChunk) {
//            this.chunk = insideChunk;
//            return this;
//        }
//
//        public Builder forPlayer(ServerPlayer player) {
//            this.player = player.getUUID();
//            return this;
//        }
//
//        public Builder forPlayer(UUID player) {
//            this.player = player;
//            return this;
//        }
//
//        public Builder enteredFrom(int machineId) {
//            this.entryPoint = machineId;
//            this.change = EnumPlayerChangeType.ENTERED;
//            return this;
//        }
//
//        public Builder exited() {
//            this.change = EnumPlayerChangeType.EXITED;
//            return this;
//        }
//    }
//}
