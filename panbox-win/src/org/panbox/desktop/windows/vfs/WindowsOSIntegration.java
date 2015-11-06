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
package org.panbox.desktop.windows.vfs;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;
import org.panbox.Settings;
import org.panbox.WinRegistry;

public class WindowsOSIntegration {

	private static final Logger logger = Logger
			.getLogger("org.panbox");
	
	private static final ResourceBundle bundle = ResourceBundle.getBundle(
			"org.panbox.desktop.common.gui.Messages", Settings.getInstance()
					.getLocale());

	private static final String EXPLORER_DRIVE_ICONS = "SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Explorer\\DriveIcons\\";
	
	public static final String SYSTEM_FOLDER_NAME;
	
	static {
		if(is64bitWindows()) {
			SYSTEM_FOLDER_NAME = "SysWow64";
		} else {
			SYSTEM_FOLDER_NAME = "System32";
		}
	}
	
	public static boolean is64bitWindows() {
		try {
			String value = WinRegistry.readString(WinRegistry.HKEY_LOCAL_MACHINE, "SOFTWARE\\Microsoft\\Windows\\CurrentVersion", "ProgramFilesDir");
			if(value.endsWith("(x86)")) {
				return true;
			}
		} catch (IllegalArgumentException | IllegalAccessException
				| InvocationTargetException e) {
		}
		return false;
	}
	
	public static void registerVFS(String mountPoint) {
		try {
			// Register custom Label and Icon in Explorer
			WinRegistry.createKey(WinRegistry.HKEY_CURRENT_USER,
					EXPLORER_DRIVE_ICONS + mountPoint + "\\DefaultIcon");
			WinRegistry.createKey(WinRegistry.HKEY_CURRENT_USER,
					EXPLORER_DRIVE_ICONS + mountPoint + "\\DefaultLabel");
			WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER,
					EXPLORER_DRIVE_ICONS + mountPoint + "\\DefaultIcon", "",
					System.getenv("SYSTEMROOT") + File.separator
							+ SYSTEM_FOLDER_NAME + File.separator
							+ "Panbox.ico");
			WinRegistry.writeStringValue(WinRegistry.HKEY_CURRENT_USER,
					EXPLORER_DRIVE_ICONS + mountPoint + "\\DefaultLabel", "",
					bundle.getString("PanboxDocuments"));
		} catch (Exception ex) {
			logger.error("WindowsOSIntegration : registerVFS : Exception: "
					+ ex.getMessage());
		}
	}

	public static void unregisterVFS(String mountPoint) {
		try {
			WinRegistry.deleteKey(WinRegistry.HKEY_CURRENT_USER,
					EXPLORER_DRIVE_ICONS + mountPoint + "\\DefaultIcon");
			WinRegistry.deleteKey(WinRegistry.HKEY_CURRENT_USER,
					EXPLORER_DRIVE_ICONS + mountPoint + "\\DefaultLabel");
			WinRegistry.deleteKey(WinRegistry.HKEY_CURRENT_USER,
					EXPLORER_DRIVE_ICONS + mountPoint);
		} catch (Exception ex) {
			logger.error("WindowsOSIntegration : unregisterVFS : Exception: "
					+ ex.getMessage());
		}
	}
}
