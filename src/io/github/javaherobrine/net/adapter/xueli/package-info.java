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
 * <li>{@link io.github.javaherobrine.net.adapter.xueli.PacketProcessor} - Interface for handling received packets</li>
 * </ul>
 *
 * <h2>Example Usage:</h2>
 * <pre>
 * // Create protocol and register packet types
 * XueliProtocol protocol = new XueliProtocol();
 * protocol.registerPacket(1, () -> new LoginPacket());
 *
 * // Create packet with multiple processors
 * class LoginPacket extends Packet {
 *     public LoginPacket() {
 *         this.classID = 1;
 *         // Multiple processors can be added - they execute in order
 *         this.addProcessor(new LoggingProcessor());
 *         this.addProcessor(new LoginProcessor());
 *     }
 *     // implement encode() and initFrom()
 * }
 *
 * // Processors handle packet logic
 * class LoggingProcessor implements PacketProcessor {
 *     public void processServerside(Packet packet) {
 *         System.out.println("Received: " + packet);
 *     }
 *     public void processClientside(Packet packet) {
 *         System.out.println("Received: " + packet);
 *     }
 * }
 *
 * class LoginProcessor implements PacketProcessor {
 *     public void processServerside(Packet packet) {
 *         // Handle login on server
 *     }
 *     public void processClientside(Packet packet) {
 *         // Handle login response on client
 *     }
 * }
 * </pre>
 */
package io.github.javaherobrine.net.adapter.xueli;