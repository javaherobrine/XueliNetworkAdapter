package io.github.javaherobrine.net.adapter.xueli;

import java.util.*;
import java.util.function.Consumer;

/**
 * Central registry for processing received packets.
 * <p>
 * This class provides a mechanism for handling packets similar to
 * xueli.game2.network.processor.PacketProcessor, adapted for CraftGame TCP Library's
 * EventContent::recvExec pattern.
 * </p>
 * <p>
 * Unlike storing processors in each packet instance, this design uses a central
 * registry where processors are registered by packet class. This matches
 * LovelyZeeiam's architecture where processors are not serialized with packets.
 * </p>
 *
 * <h2>Usage Example:</h2>
 * <pre>
 * // Create a packet processor registry
 * PacketProcessor processor = new PacketProcessor();
 *
 * // Register processors for specific packet types
 * processor.addProcessor(LoginPacket.class, packet -> {
 *     System.out.println("Logging: " + packet);
 * });
 *
 * processor.addProcessor(LoginPacket.class, packet -> {
 *     // Handle login logic
 *     LoginPacket login = (LoginPacket) packet;
 *     // ... process login
 * });
 *
 * // When a packet is received, process it
 * processor.doProcess(receivedPacket);
 * </pre>
 */
public class PacketProcessor {

	private final HashMap<Class<? extends Packet>, ArrayList<Consumer<? extends Packet>>> packetProcessors = new HashMap<>();

	public PacketProcessor() {
	}

	/**
	 * Add a processor for a specific packet type.
	 * Multiple processors can be registered for the same packet type.
	 *
	 * @param <T> the packet type
	 * @param clazz the packet class
	 * @param processor the consumer that processes the packet
	 */
	public <T extends Packet> void addProcessor(Class<T> clazz, Consumer<T> processor) {
		ArrayList<Consumer<? extends Packet>> list = packetProcessors.computeIfAbsent(clazz, c -> new ArrayList<>());
		list.add(processor);
	}

	/**
	 * Process a received packet by executing all registered processors for its type.
	 * All processors are executed in the order they were registered.
	 *
	 * @param <T> the packet type
	 * @param packet the packet to process
	 */
	@SuppressWarnings("unchecked")
	public <T extends Packet> void doProcess(T packet) {
		Class<? extends Packet> clazz = packet.getClass();
		ArrayList<Consumer<? extends Packet>> list = packetProcessors.get(clazz);
		if (list == null) {
			// No processors registered for this packet type
			return;
		}

		list.forEach(consumer -> {
			((Consumer<T>) consumer).accept(packet);
		});
	}
}
