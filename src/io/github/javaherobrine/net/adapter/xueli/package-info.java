/**
 * Adapter for LovelyZeeiam's implementation of network in his/her game.
 * <p>
 * This package provides a protocol adapter that bridges CraftGame TCP Library
 * with xueli.game2.network packet format and processing patterns.
 * </p>
 *
 * <h2>Key Components:</h2>
 * <ul>
 * <li>{@link io.github.javaherobrine.net.adapter.xueli.XueliProtocol} - Protocol implementation using VAR_INT encoding</li>
 * <li>{@link io.github.javaherobrine.net.adapter.xueli.Packet} - Base packet class with serialization support</li>
 * <li>{@link io.github.javaherobrine.net.adapter.xueli.PacketProcessor} - Central registry for handling received packets</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * // Create protocol and register packet types
 * XueliProtocol protocol = new XueliProtocol();
 * protocol.registerPacket(1, () -> new LoginPacket());
 *
 * // Create packet processor registry
 * PacketProcessor processor = new PacketProcessor();
 *
 * // Define packet class
 * class LoginPacket extends Packet {
 *     public LoginPacket() {
 *         this.classID = 1;
 *     }
 *     // implement encode() and initFrom()
 * }
 *
 * // Register multiple processors for the packet type
 * processor.addProcessor(LoginPacket.class, packet -> {
 *     System.out.println("Logging: " + packet);
 * });
 *
 * processor.addProcessor(LoginPacket.class, packet -> {
 *     LoginPacket login = (LoginPacket) packet;
 *     // Handle login logic
 * });
 *
 * // Set the processor registry globally
 * Packet.setPacketProcessor(processor);
 *
 * // When packets are received, recvExec automatically calls registered processors
 * </pre>
 *
 * <h2>Architecture Notes:</h2>
 * <p>
 * This design matches xueli.game2.network.processor.PacketProcessor where processors
 * are stored in a central registry by packet class, not in packet instances. This is
 * important because processors are not serialized/deserialized with packet data.
 * </p>
 */
package io.github.javaherobrine.net.adapter.xueli;