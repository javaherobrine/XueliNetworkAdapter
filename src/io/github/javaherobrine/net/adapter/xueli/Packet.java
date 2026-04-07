package io.github.javaherobrine.net.adapter.xueli;
import io.github.javaherobrine.net.*;
import java.io.*;
/**
 * Binding of xueli.game2.network.Packet
 */
public abstract class Packet extends EventContent{
	private static final long serialVersionUID = 1L;
	public int classID;
	public abstract byte[] encode() throws IOException;
	public abstract void initFrom(InputStream in) throws IOException;
	@Override
	public void recvExec(boolean serverside) throws Exception {
		// TODO Auto-generated method stub
	}
}
