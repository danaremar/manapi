package com.manapi.manapigateway.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;

@Service
public class FileUtils {

    public static final String IMAGES_PATH = "src/main/resources/images/";
	
	private FileUtils() {}

	public static String getFileName(MultipartFile file) {
		return file.getResource().getFilename();
	}

	public static String getFileNameUID(MultipartFile file) {
		return getFileNameUID(getFileName(file));
	}

	public static String getFileNameUID(String fileName) {
		return UUID.randomUUID().toString() + "." + Files.getFileExtension(fileName);
	}

	public static void uploadToPath(MultipartFile file, String path) throws IllegalStateException, IOException {
		uploadToPath(file, path, getFileName(file));
	}

	public static void uploadToPath(MultipartFile file, String path, String fileName)
			throws IllegalStateException, IOException {
		String realPath = path + fileName;
		File dest = new File(realPath);

		// parent file exists?
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdir();
		}
		
		Files.write(file.getBytes(), dest);
	}

	public static void deleteFromPath(String path, String fileName) throws IOException {
		Path pathToDelete = Paths.get(path + fileName);
		java.nio.file.Files.delete(pathToDelete);

	}
    
}
