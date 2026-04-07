package io.github.javaherobrine.net.adapter.xueli;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import io.github.javaherobrine.net.*;

/**
 * Protocol adapter for LovelyZeeiam's network implementation specifications.
 * <p>
 * This protocol bridges CraftGame TCP Library's Protocol/EventContent system
 * with xueli.game2.network.Packet format. It serializes packets with their classID
 * in little-endian byte order for proper type identification during transmission,
 * matching the xueli.utils.Bytes specification.
 * </p>
 *
 * <h2>Wire Format:</h2>
 * <pre>
 * [classID: 4 bytes, little-endian][packet data: variable length]
 * </pre>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * XueliProtocol protocol = new XueliProtocol();
 *
 * // Register packet types before using
 * protocol.registerPacket(1, () -> new LoginPacket());
 * protocol.registerPacket(2, () -> new ChatPacket());
 *
 * // Use with CraftGame TCP Library
 * protocol.send(myPacket);
 * Packet received = protocol.next();
 * </pre>
 *
 * @see Packet
 */
public class XueliProtocol extends Protocol{
	private final Map<Integer, Supplier<Packet>> packetRegistry = new HashMap<>();

	/**
	 * Register a packet type with its classID.
	 * @param classID The unique identifier for this packet type
	 * @param packetSupplier A supplier that creates a new instance of the packet
	 */
	public void registerPacket(int classID, Supplier<Packet> packetSupplier) {
		packetRegistry.put(classID, packetSupplier);
	}

	@Override
	public Packet next() throws IOException {
		if (in == null) {
			throw new IOException("Input stream is not initialized");
		}

		// Read the classID (4 bytes for int, little-endian)
		byte[] idBytes = new byte[4];
		int bytesRead = in.read(idBytes);
		if (bytesRead != 4) {
			throw new IOException("Failed to read classID");
		}
		int classID = bytesToInt(idBytes);

		// Get the packet factory for this classID
		Supplier<Packet> packetSupplier = packetRegistry.get(classID);
		if (packetSupplier == null) {
			throw new IOException("Unknown packet classID: " + classID);
		}

		// Create packet instance and initialize from stream
		Packet packet = packetSupplier.get();
		packet.classID = classID;
		packet.initFrom(in);

		return packet;
	}

	@Override
	public void send(EventContent ec) throws IOException {
		if (out == null) {
			throw new IOException("Output stream is not initialized");
		}

		if (!(ec instanceof Packet)) {
			throw new IOException("EventContent must be a Packet instance");
		}

		Packet packet = (Packet) ec;

		// Write the classID (little-endian)
		byte[] idBytes = intToBytes(packet.classID);
		out.write(idBytes);

		// Encode packet data and write to stream
		byte[] data = packet.encode();
		out.write(data);
		out.flush();
	}

	/**
	 * Convert int to byte array in little-endian format (LSB first).
	 * Compatible with xueli.utils.Bytes.getBytes(int).
	 */
	private byte[] intToBytes(int data) {
		byte[] bytes = new byte[4];
		bytes[0] = (byte) (data & 0xff);
		bytes[1] = (byte) ((data & 0xff00) >> 8);
		bytes[2] = (byte) ((data & 0xff0000) >> 16);
		bytes[3] = (byte) ((data & 0xff000000) >> 24);
		return bytes;
	}

	/**
	 * Convert byte array in little-endian format (LSB first) to int.
	 * Compatible with xueli.utils.Bytes.getInt(byte[]).
	 */
	private int bytesToInt(byte[] bytes) {
		return (0xff & bytes[0]) | (0xff00 & (bytes[1] << 8)) |
		       (0xff0000 & (bytes[2] << 16)) | (0xff000000 & (bytes[3] << 24));
	}
}
