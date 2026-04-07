package io.github.javaherobrine.net.adapter.xueli;
import io.github.javaherobrine.net.*;
import java.io.*;
/**
 * Binding of xueli.game2.network.Packet
 * <p>
 * This abstract class adapts xueli's Packet interface to CraftGame TCP Library's
 * EventContent system. It provides packet serialization via encode()/initFrom()
 * and packet processing via the PacketProcessor pattern.
 * </p>
 *
 * <h2>Usage:</h2>
 * <pre>
 * class MyPacket extends Packet {
 *     public MyPacket() {
 *         this.classID = 1;
 *         this.setProcessor(new MyPacketProcessor());
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
 * </pre>
 */
public abstract class Packet extends EventContent{
	private static final long serialVersionUID = 1L;
	public int classID;
	private PacketProcessor processor;

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
	 * Set the packet processor for handling this packet when received.
	 *
	 * @param processor the processor to handle this packet
	 */
	public void setProcessor(PacketProcessor processor) {
		this.processor = processor;
	}

	/**
	 * Get the current packet processor.
	 *
	 * @return the current processor, or null if none set
	 */
	public PacketProcessor getProcessor() {
		return processor;
	}

	/**
	 * Called when packet is received. Delegates to the PacketProcessor if set.
	 *
	 * @param serverside true if received on server, false if on client
	 * @throws Exception if processing fails
	 */
	@Override
	public void recvExec(boolean serverside) throws Exception {
		if (processor != null) {
			if (serverside) {
				processor.processServerside(this);
			} else {
				processor.processClientside(this);
			}
		}
	}
}
