package io.github.javaherobrine.net.adapter.xueli;
import io.github.javaherobrine.net.*;
import java.io.*;
import java.util.*;
/**
 * Binding of xueli.game2.network.Packet
 * <p>
 * This abstract class adapts xueli's Packet interface to CraftGame TCP Library's
 * EventContent system. It provides packet serialization via encode()/initFrom()
 * and packet processing via the PacketProcessor pattern.
 * </p>
 * <p>
 * Unlike LovelyZeeiam's Packet which doesn't have built-in processing,
 * this adapter supports multiple processors per packet, matching CraftGame TCP
 * Library's EventContent design.
 * </p>
 *
 * <h2>Usage:</h2>
 * <pre>
 * class MyPacket extends Packet {
 *     public MyPacket() {
 *         this.classID = 1;
 *         this.addProcessor(new LoggingProcessor());
 *         this.addProcessor(new ValidationProcessor());
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
	private List<PacketProcessor> processors = new ArrayList<>();

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
	 * Add a packet processor for handling this packet when received.
	 * Multiple processors can be added and will be executed in order.
	 *
	 * @param processor the processor to add
	 */
	public void addProcessor(PacketProcessor processor) {
		processors.add(processor);
	}

	/**
	 * Remove a packet processor.
	 *
	 * @param processor the processor to remove
	 * @return true if the processor was removed, false if it wasn't present
	 */
	public boolean removeProcessor(PacketProcessor processor) {
		return processors.remove(processor);
	}

	/**
	 * Get all packet processors.
	 *
	 * @return unmodifiable list of processors
	 */
	public List<PacketProcessor> getProcessors() {
		return Collections.unmodifiableList(processors);
	}

	/**
	 * Clear all packet processors.
	 */
	public void clearProcessors() {
		processors.clear();
	}

	/**
	 * Called when packet is received. Delegates to all registered PacketProcessors.
	 * All processors are executed in the order they were added.
	 *
	 * @param serverside true if received on server, false if on client
	 * @throws Exception if processing fails
	 */
	@Override
	public void recvExec(boolean serverside) throws Exception {
		for (PacketProcessor processor : processors) {
			if (serverside) {
				processor.processServerside(this);
			} else {
				processor.processClientside(this);
			}
		}
	}
}
