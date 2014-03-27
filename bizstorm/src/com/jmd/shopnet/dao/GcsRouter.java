package com.jmd.shopnet.dao;

import java.io.InputStream;
import java.nio.channels.Channels;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.appengine.tools.cloudstorage.GcsFilename;
import com.google.appengine.tools.cloudstorage.GcsInputChannel;
import com.google.appengine.tools.cloudstorage.GcsService;
import com.google.appengine.tools.cloudstorage.GcsServiceFactory;
import com.google.appengine.tools.cloudstorage.RetryParams;

public class GcsRouter {
	protected static Logger log = Logger.getLogger(GcsRouter.class.getName());
	  private final GcsService gcsService =
		      GcsServiceFactory.createGcsService(RetryParams.getDefaultInstance());

	public InputStream fetchFile(String bucketName, String fileName) {
		GcsFilename gcsFile = new GcsFilename(bucketName, fileName);
		try {
		    GcsInputChannel readChannel = gcsService.openPrefetchingReadChannel(gcsFile, 0, 1024 * 1024);
		    if(log.isLoggable(Level.FINE)) log.fine("Got the readChannel: " + readChannel);
		    InputStream byteStream = Channels.newInputStream(readChannel);
		    if(log.isLoggable(Level.FINER)) log.finer("Got the GCS  object Byte Stream: " + byteStream);
			return byteStream;
		} catch (Exception e) {
			log.logp(Level.SEVERE, GcsRouter.class.getName(), "fetchFile", "Could not get file from GCS: " + bucketName + "/" + fileName, e);
			throw new RuntimeException("Could not get file from GCS: " + bucketName + "/" + fileName);
		}
	}
	  
}
