package io.github.javaherobrine.net.adapter.xueli;
import io.github.javaherobrine.net.*;
import java.io.*;
/**
 * Binding of xueli.game2.network.Packet
 * <p>
 * This abstract class adapts xueli's Packet interface to CraftGame TCP Library's
 * EventContent system. It provides packet serialization via encode()/initFrom()
 * and packet processing via the PacketProcessor registry pattern.
 * </p>
 * <p>
 * Unlike storing processors in each packet instance, this design uses a central
 * PacketProcessor registry (similar to LovelyZeeiam's implementation) where
 * processors are registered by packet class and not serialized with the packet data.
 * </p>
 *
 * <h2>Usage:</h2>
 * <pre>
 * // Create packet processor registry
 * PacketProcessor processor = new PacketProcessor();
 *
 * // Define packet class
 * class LoginPacket extends Packet {
 *     public LoginPacket() {
 *         this.classID = 1;
 *     }
 *
 *     {@literal @}Override
 *     public byte[] encode() throws IOException {
 *         // Serialize packet data
 *         return data;
 *     }
 *
 *     {@literal @}Override
 *     public void initFrom(InputStream in) throws IOException {
 *         // Deserialize packet data
 *     }
 * }
 *
 * // Register processors for the packet type
 * processor.addProcessor(LoginPacket.class, packet -> {
 *     System.out.println("Received: " + packet);
 * });
 *
 * processor.addProcessor(LoginPacket.class, packet -> {
 *     // Handle login
 * });
 *
 * // Set the processor registry for packets
 * LoginPacket.setPacketProcessor(processor);
 *
 * // When packet is received, recvExec will call the processor
 * </pre>
 */
public abstract class Packet extends EventContent{
	private static final long serialVersionUID = 1L;
	public int classID;

	private static PacketProcessor packetProcessor;

	/**
	 * Set the global PacketProcessor registry for all packets.
	 * This should be called once during application initialization.
	 *
	 * @param processor the PacketProcessor registry
	 */
	public static void setPacketProcessor(PacketProcessor processor) {
		packetProcessor = processor;
	}

	/**
	 * Get the global PacketProcessor registry.
	 *
	 * @return the PacketProcessor registry, or null if not set
	 */
	public static PacketProcessor getPacketProcessor() {
		return packetProcessor;
	}

	/**
	 * Encode packet data to byte array for transmission.
	 * This should encode only the packet-specific data, not the classID.
	 *
	 * @return byte array containing encoded packet data
	 * @throws IOException if encoding fails
	 */
	public abstract byte[] encode() throws IOException;

	/**
	 * Initialize packet data from input stream.
	 * This should read only the packet-specific data, as classID is already read.
	 *
	 * @param in the input stream to read from
	 * @throws IOException if reading fails
	 */
	public abstract void initFrom(InputStream in) throws IOException;

	/**
	 * Called when packet is received. Delegates to the PacketProcessor registry.
	 * All processors registered for this packet's class are executed in order.
	 *
	 * @param serverside true if received on server, false if on client
	 * @throws Exception if processing fails
	 */
	@Override
	public void recvExec(boolean serverside) throws Exception {
		if (packetProcessor != null) {
			packetProcessor.doProcess(this);
		}
	}
}
