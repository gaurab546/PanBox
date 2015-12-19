package org.panbox.desktop.common.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.log4j.Logger;
import org.panbox.OS;
import org.panbox.desktop.common.PanboxDesktopConstants;

public class VersionUtils {

	private final static Logger logger = Logger.getLogger(VersionUtils.class);

	public static boolean isNewerVersionAvailable() {
		byte[] currentVersion = formatVersionString(getVersion());
		byte[] remoteVersion = formatVersionString(getRemoteVersion(OS.getOperatingSystem()));

		for (int i = 0; i < 3; i++) {
			if (remoteVersion[i] > currentVersion[i]) {
				return true;
			}
		}

		return false;
	}

	public static byte[] formatVersionString(String version) {
		byte[] formatVersion = new byte[3];
		String[] versionStringParts = version.split("\\.");
		if (versionStringParts.length != 3) {
			return new byte[] { 0, 0, 0 };
		}
		for (int i = 0; i < 3; i++) {
			formatVersion[i] = Byte.parseByte(versionStringParts[i]);
		}
		return formatVersion;
	}

	public static String getVersion() {
		String ret = "";

		for (int i = 0; i < PanboxDesktopConstants.PANBOX_VERSION.length; i++) {
			ret += (char) PanboxDesktopConstants.PANBOX_VERSION[i];
		}
		return ret;
	}

	public static String getRemoteVersion(OS.OperatingSystem os) {
		String filename = "VERSION";
		switch (os) {
		case WIN32:
			filename = "VERSION.win32";
			break;
		case LINUX32:
		case LINUX64:
			filename = "VERSION.linux";
			break;
		case ANDROID:
			filename = "VERSION.android";
			break;
		case OSX32:
			// TODO: Add later on MacOSX support
			return "0.0.0";
		}

		URL url;
		try {
			url = new URL("https://raw.githubusercontent.com/Sirrix-AG/PanBox/master/" + filename);
			URLConnection uc = url.openConnection();

			InputStreamReader input = new InputStreamReader(uc.getInputStream());
			BufferedReader in = new BufferedReader(input);
			String inputLine = in.readLine();
			if (inputLine != null) {
				logger.debug("VersionUtils : getRemoteVersion : Found remote version: " + inputLine);
				return inputLine;
			}
		} catch (IOException e) {
			logger.error("VersionUtils : getRemoteVersion : Error while reading remote version.", e);
		}
		return "0.0.0";
	}
}
