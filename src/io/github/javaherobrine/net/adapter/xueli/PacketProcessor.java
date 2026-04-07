package io.github.javaherobrine.net.adapter.xueli;

/**
 * Interface for processing received packets.
 * <p>
 * This interface provides a mechanism for handling packets similar to
 * xueli.game2.network packet processing, adapted for CraftGame TCP Library's
 * EventContent::recvExec pattern.
 * </p>
 * <p>
 * Unlike LovelyZeeiam's Packet implementation, CraftGame TCP Library's EventContent
 * supports multiple processors per packet. This allows for modular packet handling
 * where different aspects can be handled by different processors (e.g., logging,
 * validation, business logic).
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * class MyPacketProcessor implements PacketProcessor {
 *     {@literal @}Override
 *     public void processServerside(Packet packet) throws Exception {
 *         if (packet instanceof LoginPacket) {
 *             LoginPacket login = (LoginPacket) packet;
 *             // Handle login on server
 *         }
 *     }
 *
 *     {@literal @}Override
 *     public void processClientside(Packet packet) throws Exception {
 *         if (packet instanceof ChatPacket) {
 *             ChatPacket chat = (ChatPacket) packet;
 *             // Handle chat on client
 *         }
 *     }
 * }
 *
 * // Add multiple processors to a packet
 * myPacket.addProcessor(new LoggingProcessor());
 * myPacket.addProcessor(new MyPacketProcessor());
 * </pre>
 */
public interface PacketProcessor {

	/**
	 * Process a packet received on the server side.
	 *
	 * @param packet The packet to process
	 * @throws Exception if processing fails
	 */
	void processServerside(Packet packet) throws Exception;

	/**
	 * Process a packet received on the client side.
	 *
	 * @param packet The packet to process
	 * @throws Exception if processing fails
	 */
	void processClientside(Packet packet) throws Exception;
}
