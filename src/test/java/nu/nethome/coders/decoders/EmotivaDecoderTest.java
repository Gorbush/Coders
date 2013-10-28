/**
 * Copyright (C) 2005-2013, Stefan Strömberg <stefangs@nethome.nu>
 *
 * This file is part of OpenNetHome (http://www.nethome.nu).
 *
 * OpenNetHome is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OpenNetHome is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package nu.nethome.coders.decoders;

import nu.nethome.coders.encoders.EmotivaEncoder;
import nu.nethome.util.ps.BadMessageException;
import nu.nethome.util.ps.FieldValue;
import nu.nethome.util.ps.Message;
import nu.nethome.util.ps.ProtocolEncoder;
import nu.nethome.util.ps.impl.PulseTestPlayer;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

/**
 * User: Stefan
 * Date: 2013-01-27
 * Time: 21:53
 */
public class EmotivaDecoderTest {

    private EmotivaEncoder encoder;
   	private EmotivaDecoder decoder;
   	private PulseTestPlayer player;

   	@Before
   	public void setUp() throws Exception {
   		encoder = new EmotivaEncoder();
   		player = new PulseTestPlayer();
   		decoder = new EmotivaDecoder();
           decoder.setTarget(player);
   		player.setDecoder(decoder);
   		player.setPulseWidthModification(0);
   	}

    @Test
    public void canCreateCommand() {
        Message message = EmotivaEncoder.buildMessage(1, 0x5533);
        assertThat(message.getFields().size(), is(2));
        assertThat(message.getFields(), hasItem(new FieldValue("Command", 1)));
        assertThat(message.getFields(), hasItem(new FieldValue("Address", 0x5533)));
    }

    @Test
   	public void canDecodeEncodedMessage() throws BadMessageException {
   		player.playMessage(encoder.encode(EmotivaEncoder.buildMessage(17, 6543), ProtocolEncoder.Phase.REPEATED));
   		assertEquals(1, player.getMessageCount());
   		assertEquals(17, player.getMessageField(0, "Command"));
   		assertEquals(6543, player.getMessageField(0, "Address"));
   		assertEquals(0, player.getMessages()[0].getRepeat());
   	}
}
