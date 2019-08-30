package nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.messages;

import nodomain.freeyourgadget.gadgetbridge.service.devices.vivomovehr.ancs.AncsControlCommand;

public class GncsControlPointMessage {
    public final AncsControlCommand command;

    public GncsControlPointMessage(AncsControlCommand command) {
        this.command = command;
    }

    public static GncsControlPointMessage parsePacket(byte[] packet) {
        return new GncsControlPointMessage(AncsControlCommand.parseCommand(packet, 4, packet.length - 6));
    }
}
