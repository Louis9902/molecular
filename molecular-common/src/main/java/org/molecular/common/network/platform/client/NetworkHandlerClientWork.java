/*
 * This file ("NetworkHandlerClientWork.java") is part of the molecular-project by Louis.
 * Copyright © 2017 Louis
 *
 * The molecular-project is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The molecular-project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with molecular-project.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.molecular.common.network.platform.client;

import org.molecular.api.network.NetworkChannel;
import org.molecular.api.network.handler.NetworkHandler;
import org.molecular.common.network.platform.server.work.SPacketRevisionEcho;
import org.molecular.common.network.platform.server.work.SPacketPayload;
import org.molecular.common.network.platform.server.work.SPacketTransmitEcho;

/**
 * @author Louis
 */

public interface NetworkHandlerClientWork extends NetworkHandler {

    void processPayload(SPacketPayload packet, NetworkChannel channel);

    void processTransmitEcho(SPacketTransmitEcho packet, NetworkChannel channel);

    void processRevisionEcho(SPacketRevisionEcho packet, NetworkChannel channel);
}
