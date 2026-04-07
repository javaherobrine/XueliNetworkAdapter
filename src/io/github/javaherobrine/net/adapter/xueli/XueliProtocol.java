package io.github.javaherobrine.net.adapter.xueli;
import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import io.github.javaherobrine.net.*;

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

		// Read the classID (4 bytes for int)
		DataInputStream din = new DataInputStream(in);
		int classID = din.readInt();

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

		// Write the classID
		DataOutputStream dout = new DataOutputStream(out);
		dout.writeInt(packet.classID);

		// Encode packet data and write to stream
		byte[] data = packet.encode();
		out.write(data);
		out.flush();
	}
}
