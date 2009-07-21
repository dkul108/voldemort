package voldemort.protocol.pb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;

import voldemort.client.protocol.RequestFormatType;
import voldemort.client.protocol.pb.ProtoBuffClientRequestFormat;
import voldemort.protocol.AbstractRequestFormatTest;
import voldemort.versioning.Versioned;

public class ProtocolBuffersRequestFormatTest extends AbstractRequestFormatTest {

    public ProtocolBuffersRequestFormatTest() {
        super(RequestFormatType.PROTOCOL_BUFFERS);
    }

    /**
     * Replicates a test used by the c++ client. It should give us a warning if
     * a protocol change for get breaks compatibility.
     */
    public void testReadGetResponse() throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        short[] shorts = new short[] { 0x18, 0x0a, 0x16, 0x0a, 0x05, 0x77, 0x6f, 0x72, 0x6c, 0x64,
                0x12, 0x0d, 0x0a, 0x04, 0x08, 0x00, 0x10, 0x01, 0x10, 0xf8, 0x9d, 0xe2, 0x88, 0x9b,
                0x24 };
        byte[] bytes = new byte[shorts.length];
        for(int i = 0; i < shorts.length; i++)
            bytes[i] = (byte) shorts[i];
        out.write(bytes);
        ProtoBuffClientRequestFormat requestFormat = new ProtoBuffClientRequestFormat();
        List<Versioned<byte[]>> getResponse = requestFormat.readGetResponse(new DataInputStream(new ByteArrayInputStream(out.toByteArray())));
        out.write(bytes);
        out.write("Some more gibberish".getBytes());
        assertEquals(1, getResponse.size());
    }

}
