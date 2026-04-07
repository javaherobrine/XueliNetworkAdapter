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
 * using VAR_INT encoding for proper type identification during transmission,
 * matching the xueli.game2.network.pipeline specification.
 * </p>
 *
 * <h2>Wire Format:</h2>
 * <pre>
 * [classID: VAR_INT][packet data: variable length]
 * </pre>
 * <p>
 * VAR_INT encoding uses variable-length format where each byte contains 7 bits of data
 * and 1 continuation bit (MSB). Values 0-127 use 1 byte, larger values use more bytes.
 * </p>
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

		// Read the classID using VAR_INT encoding
		int classID = readVarInt(in);

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

		// Write the classID using VAR_INT encoding
		writeVarInt(packet.classID, out);

		// Encode packet data and write to stream
		byte[] data = packet.encode();
		out.write(data);
		out.flush();
	}

	/**
	 * Read a VAR_INT from the input stream.
	 * Compatible with xueli.game2.network.PrimitiveCodec.VAR_INT.
	 * <p>
	 * VAR_INT encoding uses 7 bits per byte for data, with the MSB as continuation flag.
	 * Reading continues while the continuation bit is set.
	 * </p>
	 */
	private int readVarInt(InputStream in) throws IOException {
		byte readByte;
		int loopCount = 0;
		int result = 0;

		while (true) {
			int b = in.read();
			if (b == -1) {
				throw new IOException("Unexpected end of stream while reading VAR_INT");
			}
			readByte = (byte) b;
			result |= (((int) readByte) & 0b1111111) << (loopCount * 7);

			if ((readByte & (1 << 7)) == 0) {
				break;
			}
			loopCount++;
		}

		return result;
	}

	/**
	 * Write a VAR_INT to the output stream.
	 * Compatible with xueli.game2.network.PrimitiveCodec.VAR_INT.
	 * <p>
	 * VAR_INT encoding uses 7 bits per byte for data, with the MSB as continuation flag.
	 * Each byte has the continuation bit set if more bytes follow.
	 * </p>
	 */
	private void writeVarInt(int value, OutputStream out) throws IOException {
		int i = value;
		int nextI;
		boolean flag;
		byte thisByte;

		while (true) {
			thisByte = (byte) (i & 0b1111111);

			nextI = i >>> 7;
			flag = nextI != 0;
			if (flag) {
				thisByte |= (1 << 7);
			}

			out.write(thisByte);

			if (!flag) {
				break;
			}
			i = nextI;
		}
	}
}
