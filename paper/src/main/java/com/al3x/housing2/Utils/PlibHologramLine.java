package com.al3x.housing2.Utils;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataValue;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class PlibHologramLine {

  private final UUID entityUid;
  private final int entityId;
  private Location location;
  private String text;

  public PlibHologramLine(Location location) {
    this.location = location;
    // Could be safer but this is probably fine
    this.entityId = ThreadLocalRandom.current().nextInt();
    this.entityUid = UUID.randomUUID();
  }

  public String getText() {
    return text;
  }

  public int getEntityId() {
    return entityId;
  }

    public Location getLocation() {
        return location;
    }

  public void showTo(Player player) {
    ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();
    protocolManager.sendServerPacket(player, createAddPacket());
    protocolManager.sendServerPacket(player, createDataPacket());
  }

  public void hideFrom(Player player) {
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, createRemovePacket());
  }

  public void teleport(Location location) {
    this.location = location;
    ProtocolLibrary.getProtocolManager().broadcastServerPacket(createMovePacket());
  }

  public void teleport(Player player, Location location) {
    this.location = location;
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, createMovePacket());
  }

  public void setText(String text) {
    this.text = text;
    ProtocolLibrary.getProtocolManager().broadcastServerPacket(createDataPacket());
  }

  public void setText(Player player, String text) {
    this.text = text;
    ProtocolLibrary.getProtocolManager().sendServerPacket(player, createDataPacket());
  }

  private PacketContainer createAddPacket() {
    PacketType type = PacketType.Play.Server.SPAWN_ENTITY;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    StructureModifier<Integer> intMod = packet.getIntegers();
    StructureModifier<EntityType> typeMod = packet.getEntityTypeModifier();
    StructureModifier<UUID> uuidMod = packet.getUUIDs();
    StructureModifier<Double> doubleMod = packet.getDoubles();

    // Write id of entity
    intMod.write(0, this.entityId);

    // Write type of entity
    typeMod.write(0, EntityType.ARMOR_STAND);

    // Write entities UUID
    uuidMod.write(0, this.entityUid);

    // Write position
    doubleMod.write(0, location.getX());
    doubleMod.write(1, location.getY());
    doubleMod.write(2, location.getZ());

    return packet;
  }

  private PacketContainer createDataPacket() {
    PacketType type = PacketType.Play.Server.ENTITY_METADATA;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntegers().write(0, this.entityId);

    WrappedDataWatcher.Serializer byteSerializer = WrappedDataWatcher.Registry.get(Byte.class);
    WrappedDataWatcher.Serializer chatSerializer = WrappedDataWatcher.Registry.getChatComponentSerializer(true);
    WrappedDataWatcher.Serializer boolSerializer = WrappedDataWatcher.Registry.get(Boolean.class);

    List<WrappedDataValue> dataValues = new ArrayList<>();

    Byte flags = 0x20;
    dataValues.add(new WrappedDataValue(0, byteSerializer, flags));

    Optional<?> optChat = Optional.of(WrappedChatComponent.fromChatMessage(this.text.replace("&", "§"))[0].getHandle());
    dataValues.add(new WrappedDataValue(2, chatSerializer, optChat));

    Boolean nameVisible = true;
    dataValues.add(new WrappedDataValue(3, boolSerializer, nameVisible));

    Boolean hasNoGravity = true;
    dataValues.add(new WrappedDataValue(5, boolSerializer, hasNoGravity));

    Byte armorstandFlags = 0x01;
    dataValues.add(new WrappedDataValue(15, byteSerializer, armorstandFlags));

    packet.getDataValueCollectionModifier().write(0, dataValues);

    return packet;
  }

  private PacketContainer createMovePacket() {
    PacketType type = PacketType.Play.Server.ENTITY_TELEPORT;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntegers().write(0, entityId);

    StructureModifier<Double> doubleMod = packet.getDoubles();
    doubleMod.write(0, this.location.getX());
    doubleMod.write(1, this.location.getY());
    doubleMod.write(2, this.location.getZ());

    return packet;
  }

  private PacketContainer createRemovePacket() {
    PacketType type = PacketType.Play.Server.ENTITY_DESTROY;
    PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(type);

    packet.getIntLists().write(0, List.of(this.entityId));

    return packet;
  }

}