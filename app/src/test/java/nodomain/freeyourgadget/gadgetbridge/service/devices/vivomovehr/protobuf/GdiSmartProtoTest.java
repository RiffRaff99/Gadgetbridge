package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.protobuf;

import com.google.protobuf.InvalidProtocolBufferException;
import nodomain.freeyourgadget.gadgetbridge.util.GB;
import org.junit.Test;

import static org.junit.Assert.*;

public class GdiSmartProtoTest {
    @Test
    public void testParse() throws InvalidProtocolBufferException {
        GdiSmartProto.Smart.parseFrom(GB.hexStringToByteArray("62021200"));
    }
}
