/*
 * 
 *               Panbox - encryption for cloud storage 
 *      Copyright (C) 2014-2015 by Fraunhofer SIT and Sirrix AG 
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * Additonally, third party code may be provided with notices and open source
 * licenses from communities and third parties that govern the use of those
 * portions, and any licenses granted hereunder do not alter any rights and
 * obligations you may have under such open source licenses, however, the
 * disclaimer of warranty and limitation of liability provisions of the GPLv3 
 * will apply to all the product.
 * 
 */
package org.panbox.desktop.common.gui.shares;

import org.panbox.core.csp.StorageBackendType;

import javax.swing.*;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.UUID;

public class DropboxPanboxShare extends PanboxShare {

	private static final long serialVersionUID = -1733766183691252286L;

	public DropboxPanboxShare(String id, String path, String name,
			int syncStatus) {
		super(id, path, name, syncStatus);
		this.icon = new ImageIcon(getClass().getResource("dropbox-gray.png"),
				"Dropbox Icon");
	}

    public DropboxPanboxShare(UUID uuid, String name, String path, HashMap<PublicKey, String> deviceMap,
                              HashMap<PublicKey, String> shareParticipants) {
		super(uuid, name, path, deviceMap, shareParticipants);
		this.icon = new ImageIcon(getClass().getResource("dropbox-gray.png"),
				"Dropbox Icon");
	}

	public StorageBackendType getType() {
		return StorageBackendType.DROPBOX;
	}
}
