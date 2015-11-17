/*
 * JDokan : Java library for Dokan Copyright (C) 2008 Yu Kobayashi http://yukoba.accelart.jp/ http://decas-dev.net/en This
 * program is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version. This
 * program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package net.decasdev.dokan;

public class DokanVolumeInformation {

	public static final int FILE_CASE_SENSITIVE_SEARCH = 1;
	public static final int FILE_CASE_PRESERVED_NAMES = 2;
	public static final int FILE_SUPPORTS_REMOTE_STORAGE = 256;
	public static final int FILE_UNICODE_ON_DISK = 4;
	public static final int FILE_PERSISTENT_ACLS = 8;

	public int fileSystemFlags;
	public String fileSystemName;
	public int maximumComponentLength;
	public String volumeName;
	public int volumeSerialNumber;

	public DokanVolumeInformation(final String volumeName,
			final String fileSystemName, final int maximumComponentLength,
			final int serialNumber) {
		this.fileSystemFlags = FILE_CASE_SENSITIVE_SEARCH
				| FILE_CASE_PRESERVED_NAMES;
		this.volumeName = volumeName;
		this.fileSystemName = fileSystemName;
		this.maximumComponentLength = maximumComponentLength;
		this.volumeSerialNumber = serialNumber;
	}

	public DokanVolumeInformation() {
		this.fileSystemFlags = FILE_CASE_SENSITIVE_SEARCH
				| FILE_CASE_PRESERVED_NAMES;
	}

	@Override
	public String toString() {
		return "DokanVolumeInformation(" + "volumeName=" + volumeName + ","
				+ "volumeSerialNumber=" + volumeSerialNumber + ","
				+ "maximumComponentLength=" + maximumComponentLength + ","
				+ "fileSystemFlags=" + fileSystemFlags + ","
				+ "fileSystemName=" + fileSystemName + ")";
	}
}
