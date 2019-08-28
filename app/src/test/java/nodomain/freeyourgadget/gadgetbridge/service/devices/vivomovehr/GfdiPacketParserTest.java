package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr;

import nodomain.freeyourgadget.gadgetbridge.devices.vivomovehr.VivomoveConstants;
import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages.DeviceInformationResponseMessage;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class GfdiPacketParserTest {
    @Test
    public void testParse() {
        final GfdiPacketParser parser = new GfdiPacketParser();
        parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x21, (byte) 0x04, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x0b, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x0e, (byte) 0x0c});
        assertNull(parser.retrievePacket());
        parser.receivedBytes(new byte[]{(byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x03, (byte) 0x87, (byte) 0xd4, (byte) 0x00});
        byte[] packet = parser.retrievePacket();
        assertNotNull(packet);
        assertArrayEquals(new byte[]{(byte) 0x21, (byte) 0x00, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x00, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x0c, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x00, (byte) 0x87, (byte) 0xd4}, packet);
        assertNull(parser.retrievePacket());

        //parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x2d, (byte) 0x05, (byte) 0x88, (byte) 0x13, (byte) 0xa0, (byte) 0x13, (byte) 0x02, (byte) 0x70, (byte) 0x05, (byte) 0xff, (byte) 0xff, (byte) 0xce, (byte) 0x56, (byte) 0x01, (byte) 0x02, (byte) 0x9a, (byte) 0x01, (byte) 0x1c, (byte) 0x40, (byte) 0x0b, (byte) 0x58, (byte) 0x5a, (byte) 0x31, (byte) 0x20, (byte) 0x43, (byte) 0x6f, (byte) 0x6d, (byte) 0x70, (byte) 0x61, (byte) 0x63, (byte) 0x74, (byte) 0x04, (byte) 0x53, (byte) 0x6f, (byte) 0x6e, (byte) 0x79, (byte) 0x05, (byte) 0x47, (byte) 0x38, (byte) 0x34, (byte) 0x34, (byte) 0x31, (byte) 0x01, (byte) 0xd6, (byte) 0xca, (byte) 0x00});
        parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x2d, (byte) 0x05, (byte) 0x88, (byte) 0x13, (byte) 0xa0, (byte) 0x13, (byte) 0x02, (byte) 0x70, (byte) 0x05, (byte) 0xff, (byte) 0xff, (byte) 0x39, (byte) 0x30, (byte) 0x01, (byte) 0x02, (byte) 0x9a, (byte) 0x01, (byte) 0x1c, (byte) 0x40, (byte) 0x0b, (byte) 0x58, (byte) 0x5a, (byte) 0x31, (byte) 0x20, (byte) 0x43, (byte) 0x6f, (byte) 0x6d, (byte) 0x70, (byte) 0x61, (byte) 0x63, (byte) 0x74, (byte) 0x04, (byte) 0x53, (byte) 0x6f, (byte) 0x6e, (byte) 0x79, (byte) 0x05, (byte) 0x47, (byte) 0x38, (byte) 0x34, (byte) 0x34, (byte) 0x31, (byte) 0x01, (byte) 0xc3, (byte) 0x7b, (byte) 0x00});
        packet = parser.retrievePacket();
        assertNotNull(packet);
        packet = parser.retrievePacket();
        assertNull(packet);

        parser.receivedBytes(new byte[]{
                (byte) 0x00, (byte) 0x05, (byte) 0x03, (byte) 0x02, (byte) 0x8c, (byte) 0x13, (byte) 0x03, (byte) 0x6b, (byte) 0xb0, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0xff, (byte) 0x3c, (byte) 0x3f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x20, (byte) 0x76,
                (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3d, (byte) 0x22, (byte) 0x31, (byte) 0x2e, (byte) 0x30, (byte) 0x22, (byte) 0x20, (byte) 0x65, (byte) 0x6e, (byte) 0x63, (byte) 0x6f, (byte) 0x64, (byte) 0x69, (byte) 0x6e,
                (byte) 0x67, (byte) 0x3d, (byte) 0x22, (byte) 0x55, (byte) 0x54, (byte) 0x46, (byte) 0x2d, (byte) 0x38, (byte) 0x22, (byte) 0x3f, (byte) 0x3e, (byte) 0x3c, (byte) 0x44, (byte) 0x65, (byte) 0x76, (byte) 0x69, (byte) 0x63, (byte) 0x65, (byte) 0x20, (byte) 0x78,
                (byte) 0x6d, (byte) 0x6c, (byte) 0x6e, (byte) 0x73, (byte) 0x3d, (byte) 0x22, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x77, (byte) 0x77, (byte) 0x77, (byte) 0x2e, (byte) 0x67, (byte) 0x61, (byte) 0x72,
                (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x2f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x73, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x61, (byte) 0x73, (byte) 0x2f, (byte) 0x47,
                (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x44, (byte) 0x65, (byte) 0x76, (byte) 0x69, (byte) 0x63, (byte) 0x65, (byte) 0x2f, (byte) 0x76, (byte) 0x32, (byte) 0x22, (byte) 0x20, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x6e,
                (byte) 0x73, (byte) 0x3a, (byte) 0x78, (byte) 0x73, (byte) 0x69, (byte) 0x3d, (byte) 0x22, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x77, (byte) 0x77, (byte) 0x77, (byte) 0x2e, (byte) 0x77, (byte) 0x33,
                (byte) 0x2e, (byte) 0x6f, (byte) 0x72, (byte) 0x67, (byte) 0x2f, (byte) 0x32, (byte) 0x30, (byte) 0x30, (byte) 0x31, (byte) 0x2f, (byte) 0x58, (byte) 0x4d, (byte) 0x4c, (byte) 0x53, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x61, (byte) 0x2d,
                (byte) 0x69, (byte) 0x6e, (byte) 0x73, (byte) 0x74, (byte) 0x61, (byte) 0x6e, (byte) 0x63, (byte) 0x65, (byte) 0x22, (byte) 0x20, (byte) 0x78, (byte) 0x73, (byte) 0x69, (byte) 0x3a, (byte) 0x73, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d, (byte) 0x61,
                (byte) 0x4c, (byte) 0x6f, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3d, (byte) 0x22, (byte) 0x68, (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x77, (byte) 0x77, (byte) 0x77,
                (byte) 0x2e, (byte) 0x67, (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d, (byte) 0x2f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x73, (byte) 0x63, (byte) 0x68, (byte) 0x65, (byte) 0x6d,
                (byte) 0x61, (byte) 0x73, (byte) 0x2f, (byte) 0x47, (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x44, (byte) 0x65, (byte) 0x76, (byte) 0x69, (byte) 0x63, (byte) 0x65, (byte) 0x2f, (byte) 0x76, (byte) 0x32, (byte) 0x20, (byte) 0x68,
                (byte) 0x74, (byte) 0x74, (byte) 0x70, (byte) 0x3a, (byte) 0x2f, (byte) 0x2f, (byte) 0x77, (byte) 0x77, (byte) 0x77, (byte) 0x2e, (byte) 0x67, (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x2e, (byte) 0x63, (byte) 0x6f, (byte) 0x6d,
                (byte) 0x2f, (byte) 0x78, (byte) 0x6d, (byte) 0x6c, (byte) 0x73, (byte) 0x63, (byte) 0x68, (byte) 0xfb, (byte) 0x65, (byte) 0x6d, (byte) 0x61, (byte) 0x73, (byte) 0x2f, (byte) 0x47, (byte) 0x61, (byte) 0x72, (byte) 0x6d, (byte) 0x69, (byte) 0x6e, (byte) 0x44,
                (byte) 0x65, (byte) 0x76, (byte) 0x69, (byte) 0x63, (byte) 0x65, (byte) 0x76, (byte) 0x32, (byte) 0x2e, (byte) 0x78, (byte) 0x73, (byte) 0x64, (byte) 0x22, (byte) 0x3e, (byte) 0x0a, (byte) 0x3c, (byte) 0x4d, (byte) 0x6f, (byte) 0x64, (byte) 0x65, (byte) 0x6c,
                (byte) 0x3e, (byte) 0x3c, (byte) 0x50, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x4e, (byte) 0x75, (byte) 0x6d, (byte) 0x62, (byte) 0x65, (byte) 0x72, (byte) 0x3e, (byte) 0x30, (byte) 0x30, (byte) 0x36, (byte) 0x2d, (byte) 0x42, (byte) 0x32, (byte) 0x37,
                (byte) 0x37, (byte) 0x32, (byte) 0x2d, (byte) 0x30, (byte) 0x30, (byte) 0x3c, (byte) 0x2f, (byte) 0x50, (byte) 0x61, (byte) 0x72, (byte) 0x74, (byte) 0x4e, (byte) 0x75, (byte) 0x6d, (byte) 0x62, (byte) 0x65, (byte) 0x72, (byte) 0x3e, (byte) 0x3c, (byte) 0x53,
                (byte) 0x6f, (byte) 0x66, (byte) 0x74, (byte) 0x77, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x56, (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x34, (byte) 0x35, (byte) 0x30, (byte) 0x3c, (byte) 0x2f,
                (byte) 0x53, (byte) 0x6f, (byte) 0x66, (byte) 0x74, (byte) 0x77, (byte) 0x61, (byte) 0x72, (byte) 0x65, (byte) 0x56, (byte) 0x65, (byte) 0x72, (byte) 0x73, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x3c, (byte) 0x44, (byte) 0x65, (byte) 0x73,
                (byte) 0x63, (byte) 0x72, (byte) 0x69, (byte) 0x70, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48,
                (byte) 0x52, (byte) 0x3c, (byte) 0x2f, (byte) 0x44, (byte) 0x65, (byte) 0x73, (byte) 0x63, (byte) 0x72, (byte) 0x69, (byte) 0x70, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x3c, (byte) 0x2f, (byte) 0x4d, (byte) 0x6f, (byte) 0x64,
                (byte) 0x65, (byte) 0x6c, (byte) 0x3e, (byte) 0x3c, (byte) 0x49, (byte) 0x64, (byte) 0x3e, (byte) 0x33, (byte) 0x39, (byte) 0x38, (byte) 0x37, (byte) 0x39, (byte) 0x30, (byte) 0x38, (byte) 0x30, (byte) 0x37, (byte) 0x32, (byte) 0x3c, (byte) 0x2f, (byte) 0x49,
                (byte) 0x64, (byte) 0x3e, (byte) 0x3c, (byte) 0x4d, (byte) 0x61, (byte) 0x73, (byte) 0x73, (byte) 0x53, (byte) 0x74, (byte) 0x6f, (byte) 0x72, (byte) 0x61, (byte) 0x67, (byte) 0x65, (byte) 0x4d, (byte) 0x6f, (byte) 0x64, (byte) 0x65, (byte) 0x3e, (byte) 0x0a,
                (byte) 0x3c, (byte) 0x44, (byte) 0x61, (byte) 0x74, (byte) 0x61, (byte) 0x54, (byte) 0x79, (byte) 0x70, (byte) 0x65, (byte) 0x3e, (byte) 0x3c, (byte) 0x4e, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x3e, (byte) 0x46, (byte) 0x49, (byte) 0x54, (byte) 0x42,
                (byte) 0x69, (byte) 0x6e, (byte) 0x61, (byte) 0x72, (byte) 0x79, (byte) 0x3c, (byte) 0x2f, (byte) 0x4e, (byte) 0x61, (byte) 0x6d, (byte) 0x65, (byte) 0x3e, (byte) 0x3c, (byte) 0x46, (byte) 0x69, (byte) 0x6c, (byte) 0x65, (byte) 0x3e, (byte) 0x3c, (byte) 0x53,
                (byte) 0x70, (byte) 0x65, (byte) 0x63, (byte) 0x69, (byte) 0x66, (byte) 0x69, (byte) 0x63, (byte) 0x61, (byte) 0x74, (byte) 0x69, (byte) 0x6f, (byte) 0x6e, (byte) 0x3e, (byte) 0x3c, (byte) 0x49, (byte) 0x64, (byte) 0x28, (byte) 0x0b, (byte) 0x00
        });
        packet = parser.retrievePacket();
        assertNotNull(packet);
        packet = parser.retrievePacket();
        assertNull(packet);

        parser.receivedBytes(new byte[]{
                (byte) 0x00, (byte) 0x05, (byte) 0x03, (byte) 0x02, (byte) 0x8c, (byte) 0x13, (byte) 0x05, (byte) 0x4d, (byte) 0x6c, (byte) 0xb0, (byte) 0x0f, (byte) 0x01, (byte) 0x27, (byte) 0x04, (byte) 0xc3, (byte) 0x3c, (byte) 0xcf, (byte) 0xb9, (byte) 0x5f, (byte) 0xb2,
                (byte) 0x5f, (byte) 0xb7, (byte) 0x1f, (byte) 0xb6, (byte) 0x8e, (byte) 0x3f, (byte) 0x90, (byte) 0xb3, (byte) 0x8b, (byte) 0xb3, (byte) 0xaf, (byte) 0x3e, (byte) 0xe2, (byte) 0x54, (byte) 0x04, (byte) 0xdd, (byte) 0x37, (byte) 0xcf, (byte) 0xb7, (byte) 0x07,
                (byte) 0xb2, (byte) 0xa1, (byte) 0xb6, (byte) 0x1e, (byte) 0xb5, (byte) 0xb0, (byte) 0x3d, (byte) 0x68, (byte) 0xb4, (byte) 0xec, (byte) 0xb1, (byte) 0x01, (byte) 0x14, (byte) 0x01, (byte) 0x55, (byte) 0x04, (byte) 0x8e, (byte) 0x32, (byte) 0xb8, (byte) 0xb4,
                (byte) 0x63, (byte) 0xb1, (byte) 0x89, (byte) 0xb5, (byte) 0xda, (byte) 0xb4, (byte) 0x2e, (byte) 0x3c, (byte) 0x83, (byte) 0xb4, (byte) 0xe5, (byte) 0xb1, (byte) 0x01, (byte) 0x7d, (byte) 0x15, (byte) 0x55, (byte) 0x04, (byte) 0x73, (byte) 0x3a, (byte) 0x64,
                (byte) 0xb8, (byte) 0x2a, (byte) 0xb0, (byte) 0xfd, (byte) 0xb4, (byte) 0x5f, (byte) 0xb4, (byte) 0x4b, (byte) 0x3d, (byte) 0x0b, (byte) 0xb1, (byte) 0x72, (byte) 0xb1, (byte) 0x1a, (byte) 0x40, (byte) 0x14, (byte) 0x55, (byte) 0x04, (byte) 0xfd, (byte) 0x3e,
                (byte) 0x9a, (byte) 0xbd, (byte) 0x68, (byte) 0xba, (byte) 0xf9, (byte) 0xbc, (byte) 0xa5, (byte) 0xbc, (byte) 0xa3, (byte) 0x43, (byte) 0x01, (byte) 0xbc, (byte) 0xa3, (byte) 0xba, (byte) 0x28, (byte) 0x44, (byte) 0x66, (byte) 0x54, (byte) 0x04, (byte) 0xcd,
                (byte) 0x44, (byte) 0x40, (byte) 0xc2, (byte) 0x17, (byte) 0xc0, (byte) 0x3c, (byte) 0xc3, (byte) 0x3f, (byte) 0xc3, (byte) 0xe1, (byte) 0x48, (byte) 0xaf, (byte) 0xc1, (byte) 0x87, (byte) 0xc0, (byte) 0x2a, (byte) 0x47, (byte) 0x55, (byte) 0x55, (byte) 0x04,
                (byte) 0x4d, (byte) 0x43, (byte) 0xdc, (byte) 0xbf, (byte) 0xb8, (byte) 0xb8, (byte) 0xce, (byte) 0xbc, (byte) 0x13, (byte) 0xbc, (byte) 0xe8, (byte) 0x44, (byte) 0xbb, (byte) 0xb8, (byte) 0xc6, (byte) 0xb9, (byte) 0x48, (byte) 0x42, (byte) 0x93, (byte) 0x55,
                (byte) 0x04, (byte) 0xba, (byte) 0x42, (byte) 0x69, (byte) 0xbf, (byte) 0x5f, (byte) 0xba, (byte) 0x28, (byte) 0xbe, (byte) 0x84, (byte) 0xbd, (byte) 0x03, (byte) 0x45, (byte) 0x09, (byte) 0xbc, (byte) 0x66, (byte) 0xbb, (byte) 0xd3, (byte) 0x42, (byte) 0x6c,
                (byte) 0x55, (byte) 0x04, (byte) 0x84, (byte) 0x39, (byte) 0x37, (byte) 0xb8, (byte) 0x77, (byte) 0xb4, (byte) 0xcd, (byte) 0xb8, (byte) 0xad, (byte) 0xb7, (byte) 0xee, (byte) 0x3f, (byte) 0xc3, (byte) 0xb6, (byte) 0x93, (byte) 0xb4, (byte) 0x01, (byte) 0x14,
                (byte) 0x26, (byte) 0x55, (byte) 0x04, (byte) 0xff, (byte) 0x1d, (byte) 0x0b, (byte) 0xb1, (byte) 0xbf, (byte) 0x31, (byte) 0x5e, (byte) 0xa5, (byte) 0xc3, (byte) 0xa7, (byte) 0x56, (byte) 0x30, (byte) 0xe1, (byte) 0x2e, (byte) 0x01, (byte) 0x32, (byte) 0x01,
                (byte) 0x29, (byte) 0x0e, (byte) 0x55, (byte) 0x04, (byte) 0x47, (byte) 0x3d, (byte) 0x28, (byte) 0xb9, (byte) 0xb0, (byte) 0xa8, (byte) 0xbd, (byte) 0xb3, (byte) 0x2d, (byte) 0xb4, (byte) 0x40, (byte) 0x3d, (byte) 0x89, (byte) 0xa5, (byte) 0x73, (byte) 0xaf,
                (byte) 0x14, (byte) 0x41, (byte) 0xaa, (byte) 0x54, (byte) 0x04, (byte) 0xca, (byte) 0x3d, (byte) 0x3e, (byte) 0xbb, (byte) 0xf4, (byte) 0xb2, (byte) 0xe0, (byte) 0xb8, (byte) 0xce, (byte) 0xb6, (byte) 0x94, (byte) 0x40, (byte) 0x6a, (byte) 0xb4, (byte) 0x24,
                (byte) 0xb4, (byte) 0x01, (byte) 0xff, (byte) 0xe0, (byte) 0x54, (byte) 0x04, (byte) 0x98, (byte) 0x43, (byte) 0x8b, (byte) 0xbf, (byte) 0xe5, (byte) 0xb5, (byte) 0x85, (byte) 0xbb, (byte) 0x99, (byte) 0xb9, (byte) 0x6d, (byte) 0x44, (byte) 0xa1, (byte) 0xb4,
                (byte) 0x9d, (byte) 0xb7, (byte) 0xcc, (byte) 0x44, (byte) 0x31, (byte) 0x55, (byte) 0x04, (byte) 0x08, (byte) 0x44, (byte) 0x58, (byte) 0xbf, (byte) 0x9b, (byte) 0xb8, (byte) 0xb9, (byte) 0xbc, (byte) 0x38, (byte) 0xbc, (byte) 0xe0, (byte) 0x44, (byte) 0xa4,
                (byte) 0xb8, (byte) 0xc8, (byte) 0xb9, (byte) 0xb4, (byte) 0x45, (byte) 0x35, (byte) 0x55, (byte) 0x04, (byte) 0xbf, (byte) 0x45, (byte) 0x94, (byte) 0xc1, (byte) 0x55, (byte) 0xb9, (byte) 0x0f, (byte) 0xbe, (byte) 0x2b, (byte) 0xbd, (byte) 0x34, (byte) 0x46,
                (byte) 0x12, (byte) 0xb9, (byte) 0x1d, (byte) 0xbb, (byte) 0x87, (byte) 0x48, (byte) 0xf8, (byte) 0x54, (byte) 0x04, (byte) 0x46, (byte) 0x40, (byte) 0x0b, (byte) 0xbc, (byte) 0xa2, (byte) 0xb5, (byte) 0xb4, (byte) 0xb9, (byte) 0x4f, (byte) 0xb9, (byte) 0x9b,
                (byte) 0x41, (byte) 0x5f, (byte) 0xb6, (byte) 0x1a, (byte) 0xb7, (byte) 0x3d, (byte) 0x40, (byte) 0xf0, (byte) 0x54, (byte) 0x04, (byte) 0x7f, (byte) 0x47, (byte) 0x9f, (byte) 0xc3, (byte) 0xa1, (byte) 0xbc, (byte) 0x6e, (byte) 0xc0, (byte) 0xf2, (byte) 0xbf,
                (byte) 0x4a, (byte) 0x48, (byte) 0x7b, (byte) 0xbc, (byte) 0xc5, (byte) 0xbd, (byte) 0x40, (byte) 0x48, (byte) 0xc6, (byte) 0x54, (byte) 0x03, (byte) 0x88, (byte) 0x96, (byte) 0xc7, (byte) 0x37, (byte) 0x01, (byte) 0x03, (byte) 0xc4, (byte) 0x96, (byte) 0xc7,
                (byte) 0x37, (byte) 0x01, (byte) 0x04, (byte) 0x14, (byte) 0x43, (byte) 0x1c, (byte) 0xc0, (byte) 0x9c, (byte) 0xba, (byte) 0x53, (byte) 0xbe, (byte) 0x76, (byte) 0xbd, (byte) 0x66, (byte) 0x45, (byte) 0xff, (byte) 0xbb, (byte) 0x81, (byte) 0xbb, (byte) 0x24,
                (byte) 0x43, (byte) 0x44, (byte) 0x55, (byte) 0x04, (byte) 0xa4, (byte) 0x40, (byte) 0xcb, (byte) 0xbc, (byte) 0x7e, (byte) 0xb5, (byte) 0x15, (byte) 0xba, (byte) 0x13, (byte) 0xb9, (byte) 0x45, (byte) 0x42, (byte) 0xb2, (byte) 0xb5, (byte) 0x6e, (byte) 0xb6,
                (byte) 0xcf, (byte) 0x40, (byte) 0xc0, (byte) 0x54, (byte) 0x04, (byte) 0xc9, (byte) 0x40, (byte) 0xd7, (byte) 0xbc, (byte) 0xb2, (byte) 0xb6, (byte) 0xce, (byte) 0xba, (byte) 0x2a, (byte) 0xba, (byte) 0x75, (byte) 0x42, (byte) 0xc9, (byte) 0xb7, (byte) 0x2d,
                (byte) 0xb8, (byte) 0xc4, (byte) 0x41, (byte) 0xf3, (byte) 0x54, (byte) 0x04, (byte) 0xf1, (byte) 0x42, (byte) 0xfd, (byte) 0xbe, (byte) 0x2e, (byte) 0xb9, (byte) 0x3d, (byte) 0xbd, (byte) 0xa5, (byte) 0xbc, (byte) 0xc3, (byte) 0x44, (byte) 0xff, (byte) 0xb9,
                (byte) 0x39, (byte) 0xba, (byte) 0x34, (byte) 0x44, (byte) 0x88, (byte) 0x54, (byte) 0x04, (byte) 0x8b, (byte) 0x42, (byte) 0x25, (byte) 0xbf, (byte) 0xc5, (byte) 0xba, (byte) 0x4e, (byte) 0xbe, (byte) 0xcf, (byte) 0xbd, (byte) 0x1d, (byte) 0x45, (byte) 0x5c,
                (byte) 0xbc, (byte) 0xe1, (byte) 0xbb, (byte) 0x74, (byte) 0x43, (byte) 0x4f, (byte) 0x55, (byte) 0x04, (byte) 0x49, (byte) 0x40, (byte) 0x49, (byte) 0xbc, (byte) 0xa3, (byte) 0xb4, (byte) 0x58, (byte) 0xb9, (byte) 0x9b, (byte) 0xb8, (byte) 0x90, (byte) 0x41,
                (byte) 0xf2, (byte) 0xb4, (byte) 0xdf, (byte) 0xb5, (byte) 0x12, (byte) 0x41, (byte) 0xd5, (byte) 0x54, (byte) 0x04, (byte) 0xdc, (byte) 0x3d, (byte) 0xde, (byte) 0xb9, (byte) 0x11, (byte) 0xb5, (byte) 0x6f, (byte) 0x23, (byte) 0x00
        });
        packet = parser.retrievePacket();
        assertNotNull(packet);
        assertEquals(0x0203, packet.length);
        packet = parser.retrievePacket();
        assertNull(packet);

        parser.receivedBytes(new byte[]{(byte) 0x00, (byte) 0x05, (byte) 0xad, (byte) 0x01, (byte) 0x8c, (byte) 0x13, (byte) 0x03, (byte) 0xbe, (byte) 0x4f, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x03, (byte) 0x90, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01, (byte) 0x01});
        packet = parser.retrievePacket();
        assertNull(packet);
    }

    @Test
    public void testWrap() {
        byte[] packet = GfdiPacketParser.wrapMessageToPacket(new byte[]{(byte) 0x21, (byte) 0x00, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x00, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x00, (byte) 0x0c, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x00, (byte) 0x87, (byte) 0xd4});

        assertArrayEquals(new byte[]{(byte) 0x00, (byte) 0x02, (byte) 0x21, (byte) 0x04, (byte) 0xa0, (byte) 0x13, (byte) 0x6f, (byte) 0x0b, (byte) 0xd4, (byte) 0x0a, (byte) 0xe8, (byte) 0xa5, (byte) 0xb2, (byte) 0xed, (byte) 0xc2, (byte) 0x01, (byte) 0x08, (byte) 0x02, (byte) 0x0e, (byte) 0x0c, (byte) 0x76, (byte) 0xc3, (byte) 0xad, (byte) 0x76, (byte) 0x6f, (byte) 0x6d, (byte) 0x6f, (byte) 0x76, (byte) 0x65, (byte) 0x20, (byte) 0x48, (byte) 0x52, (byte) 0x03, (byte) 0x87, (byte) 0xd4, (byte) 0x00}, packet);
    }

    @Test
    public void testRoundtrip() {
        final DeviceInformationResponseMessage message = new DeviceInformationResponseMessage(VivomoveConstants.STATUS_ACK, 112, -1, VivomoveConstants.GADGETBRIDGE_UNIT_NUMBER, 123, 16384, "BT", "Test", "Model", 1);
        testRoundtrip(message.packet);

        final byte[] longPacket = new byte[500];
        Arrays.fill(longPacket, (byte) 0xAB);
        testRoundtrip(longPacket);

        final byte[] exactlyLongPacket = new byte[254];
        Arrays.fill(exactlyLongPacket, (byte) 0xAB);
        testRoundtrip(exactlyLongPacket);
    }

    private static void testRoundtrip(byte[] packet) {
        final GfdiPacketParser parser = new GfdiPacketParser();
        final byte[] bytes = GfdiPacketParser.wrapMessageToPacket(packet);
        parser.receivedBytes(bytes);

        final byte[] parsedPacket = parser.retrievePacket();
        assertNotNull(parsedPacket);

        assertArrayEquals(packet, parsedPacket);
    }
}
