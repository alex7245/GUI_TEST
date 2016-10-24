package pck;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

//import org.apache.commons.io.FileUtils;
//import org.apache.logging.log4j.LogManager;

public class FileUtil {
	
	private static final Map<String, String> CACHE = new ConcurrentHashMap<>();
	
	public static void clearCache() {
		CACHE.clear();
	}
	
	public static String readClassLocalFileToString(Class<?> clazz, String filename, boolean cache) throws IOException {
		return readClassLocalFileToString(clazz, filename, "UTF-8", true);
	}
	
	public static String readClassLocalFileToString(Class<?> clazz, String filename) throws IOException {
		return readClassLocalFileToString(clazz, filename, "UTF-8");
	}
	
	public static String readClassLocalFileToString(Class<?> clazz, String filename, String charset) throws IOException {
		return readClassLocalFileToString(clazz, filename, charset, true);
	}
	
	public static String readClassLocalFileToString(Class<?> clazz, String filename, String charset, boolean cache) throws IOException {
		if (clazz == null) {
			throw new IllegalArgumentException("clazz cannot be null.");
		}
		if (filename == null || filename.isEmpty()) {
			throw new IllegalArgumentException("filename cannot be null or empty.");
		}
		if (charset == null || charset.isEmpty()) {
			throw new IllegalArgumentException("charset cannot be null or empty.");
		}
		String tag = clazz.getCanonicalName();
		if (tag == null || tag.isEmpty()) {
			throw new IllegalArgumentException("The supplied clazz does not have a canonical name, i.e. it is internal/anonymous etc. This is unsupported.");
		}
		tag += "_" + filename;
		String content = CACHE.get(tag);
		if (content != null) {
			return content;
		}
		try (
				InputStream in = clazz.getResourceAsStream(filename);
				Scanner scanner = new Scanner(in, charset);
		) {
			scanner.useDelimiter("\\Z");
			content = scanner.next();
			if (cache) {
				CACHE.put(tag, content);
			}
			return content;
		} catch (NullPointerException e) {
			throw new IOException("Failed to open resource: " + filename + ".");
		}
	}
	
	/**
	 * Renames a file to the supplied filename.
	 * @param file The file to rename
	 * @param filename The name of the file
	 * @return A reference to the renamed file
	 * @throws IOException if the rename operation fails
	 * @throws NullPointerException if file or filename are null
	 */
	public static File renameFile(File file, String filename) throws IOException {
		if (!filename.equals(file.getName())) {
			File renameTo = new File(file.getParentFile(), filename);
			if (renameTo.exists()) {
				renameTo.delete();
			}
			if (!file.renameTo(renameTo)) {
				throw new IOException("Failed to rename file to " + filename);
			}
			file = renameTo;
		}
		return file;
	}
	
	public static void unzip(File zipFile, File directory) throws IOException {
		unzip(new FileInputStream(zipFile), directory);
	}
	
	public static void unzip(InputStream in, File directory) throws IOException {
		try (
			ZipInputStream zipIn = new ZipInputStream(in);
		) {
			ZipEntry entry = null;
			while ((entry = zipIn.getNextEntry()) != null) {
				File file = new File(directory, entry.getName());
				if (entry.isDirectory()) {
					if (!file.exists()) {
						file.mkdirs();
					}
				} else {
					//make parent directory in case this entry is extracted before the entry for the parent folder
					file.getParentFile().mkdirs();
					try (
						OutputStream out = new FileOutputStream(file); 
					) {
						copy(zipIn, out);
					}
				}
			}
		}
	}
	
	public static void zip(File directory, File zipFile) throws IOException {
        zip(directory, new FileOutputStream(zipFile));
    }

    public static void zip(File directory, OutputStream out) throws IOException {
        try (
        	ZipOutputStream zipOut = new ZipOutputStream(out)
        ) {
            zip(directory, zipOut, directory.getPath().length() + 1);
        }
    }

    private static void zip(File directory, ZipOutputStream zipOut, int prefixLength) throws IOException {
    	File[] files = directory.listFiles();
    	if (files.length == 0) {
    		ZipEntry zipEntry = new ZipEntry(directory.getPath().replace('\\','/').substring(prefixLength) + '/');
            zipOut.putNextEntry(zipEntry);
            zipOut.closeEntry();
            return;
    	}
        for (File file : files) {
            if (file.isFile()) {
                ZipEntry zipEntry = new ZipEntry(file.getPath().replace('\\','/').substring(prefixLength));
                zipOut.putNextEntry(zipEntry);
                try (
                	FileInputStream in = new FileInputStream(file)
                ) {
                	copy(in, zipOut);
                } finally {
                	zipOut.closeEntry();
                }
            } else if (file.isDirectory()) {
                zip(file, zipOut, prefixLength);
            }
        }
    }
    
    public static long copy(InputStream in, OutputStream out) throws IOException {
    	byte[] buf = new byte[8192];
		int len;
		long bytesCopied = 0l;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
			bytesCopied += len;
		}
		return bytesCopied;
    }
    
//	/**
//	 * Attempts to delete the supplied file, logging any failures and preventing exceptions from being thrown.
//	 * @param file The file to delete
//	 */
//	public static void deleteFileQuietly(File file) {
//		if (file != null && file.exists()) {
//			try {
//				if (!file.delete()) {
//					try {
//						LogManager.getLogger().warn("Delete operation on uploaded file " + file.getAbsolutePath() + " failed (returned false)");
//					} catch (Throwable t) {}
//				}
//			} catch (Throwable t) {
//				try {
//					LogManager.getLogger().warn("Delete operation on uploaded file " + file.getAbsolutePath(), t);
//				} catch (Throwable t1) {}
//			}
//		}
//	}
//
//	/**
//	 * Attempts to delete the supplied directory, logging any failures and preventing exceptions from being thrown.
//	 * @param directory The directory to delete
//	 */
//	public static void deleteDirectoryQuietly(File directory) {
//		if (directory != null && directory.exists()) {
//			try {
//				FileUtils.deleteDirectory(directory); 
//			} catch (Throwable t) {
//				try {
//					LogManager.getLogger().warn("Failed to delete temporary extracted directory " + directory.getAbsolutePath(), t);
//				} catch (Throwable t1) {}
//			}
//		}
//	}
	
	public static byte[] getMd5(File file) throws NoSuchAlgorithmException, IOException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		try (
				InputStream in =  new FileInputStream(file);
		) {
			byte[] buf = new byte[8192];
			int bytesRead;
			while((bytesRead = in.read(buf)) > 0) {
				md5.update(buf, 0, bytesRead);
			}
			return md5.digest(); 
		}
	}
}
