package io.github.javaherobrine.net.adapter.xueli;

/**
 * Interface for processing received packets.
 * <p>
 * This interface provides a mechanism for handling packets similar to
 * xueli.game2.network packet processing, adapted for CraftGame TCP Library's
 * EventContent::recvExec pattern.
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
 * // Set processor for packet types
 * myPacket.setProcessor(new MyPacketProcessor());
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
