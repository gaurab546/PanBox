package org.panbox.desktop.windows.client;


import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.InvocationTargetException;
import java.rmi.RemoteException;

import javax.naming.ConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.panbox.core.crypto.AbstractObfuscatorFactory;
import org.panbox.core.crypto.FileObfuscatorFactory;
import org.panbox.core.crypto.randomness.SecureRandomWrapper;
import org.panbox.core.exception.ObfuscationException;
import org.panbox.core.exception.RandomDataGenerationException;
import org.panbox.core.keymgmt.VolumeParams;
import org.panbox.core.vfs.backend.VirtualVolume;
import org.panbox.desktop.common.sharemgmt.AbstractPanboxService;
import org.panbox.desktop.common.vfs.PanboxFS;
import org.panbox.desktop.common.vfs.backend.IRootVolume;
import org.panbox.desktop.common.vfs.backend.VFSShare;
import org.panbox.desktop.common.vfs.backend.VirtualRootVolume;
import org.panbox.desktop.common.vfs.backend.dropbox.DropboxVirtualVolume;
import org.panbox.desktop.windows.vfs.VFSManager;

public class PanboxWindowsService extends AbstractPanboxService {

	private static final Logger logger = Logger.getLogger("org.panbox");
	
	public void startService() {
		if (!VFSManager.isRunning()) {
			try {
				SecureRandomWrapper.getInstance();
				AbstractObfuscatorFactory.getFactory(FileObfuscatorFactory.class);
				VFSManager.getInstance().startVFS();
			} catch (ConfigurationException | IllegalArgumentException
					| IllegalAccessException | InvocationTargetException
					| ClassNotFoundException | InstantiationException
					| RandomDataGenerationException ex) {
				logger.error(
						"PanboxWindowsService : startupVFS : Caught exception from VFSManager",
						ex);
				shutdownService();
				System.exit(-1);
			}
		}
	}
	
	public void shutdownService() {
		try {
			int tries = 3;
			while (VFSManager.isRunning() && tries > 0) {
				logger.debug("PanboxWindowsService : stopService : Can not stop service, because VFS is still running! Will stop it now! Tries: "
						+ tries);
				VFSManager.getInstance().stopVFS();
				Thread.sleep(1000);
				--tries;
			}
	
			if (VFSManager.isRunning()) {
				logger.error("PanboxWindowsService : stopService : Stopping VFS failed! Will not shut down service!");
				System.exit(-1);
			}
		} catch (ConfigurationException | IllegalArgumentException
				| IllegalAccessException | InvocationTargetException
				| InterruptedException ex) {
			logger.error(
					"PanboxWindowsService : startupVFS : Caught exception from VFSManager",
					ex);
			shutdownService();
			System.exit(-1);
		}
		
	}
	
	@Override
	public String getOnlineFilename(VolumeParams p, String fileName)
			throws RemoteException, FileNotFoundException, ObfuscationException {
		String shareid = FilenameUtils.getName(p.path); // Dropbox share name
		String path = File.separator + p.shareName + File.separator + fileName;
		PanboxFS fs;
		try {
			fs = VFSManager.getInstance().getVFS();
			String obf = fs.backingStorage.obfuscatePath(path, false);
			String windowsPath = obf.replace(p.shareName, shareid);
			return windowsPath.replace("\\", "/");
		} catch (ConfigurationException | IllegalArgumentException
				| IllegalAccessException | InvocationTargetException e) {
			throw new RemoteException("Error obtaining VFS manager instance", e);
		}
	}

	@Override
	protected VirtualVolume getVirtualVolume(VolumeParams p)
			throws FileNotFoundException {
		VirtualVolume virtualVolume = null;
		switch (p.type) {
		case FOLDER:
			// same
		case DROPBOX:
			virtualVolume = new DropboxVirtualVolume(p.path);
			// TODO: Refactor to generic virtualvolume instead of dropbox
			break;
		}
		return virtualVolume;
	}

	@Override
	protected void registerShare(VFSShare vfsShare, VolumeParams p) {
		IRootVolume vrv = VirtualRootVolume.getInstance();
		if (vrv.existsAndChanged(vfsShare)) {
			vrv.removeShare(p.shareName);
			vrv.registerShare(vfsShare);
		} else {
			vrv.registerShare(vfsShare);
		}
	}

	@Override
	protected boolean unregisterShare(VolumeParams p) {
		return VirtualRootVolume.getInstance().removeShare(p.shareName);
	}

}
