package com.minimi.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.mortennobel.imagescaling.AdvancedResizeOp;
import com.mortennobel.imagescaling.MultiStepRescaleOp;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public final class FileUtils {

	@Value("${file-dir}")
	private String filePreFix;
	private static final int RESIZE_WIDTH = 1000;

	public String fileUpload(MultipartFile file) throws IOException {
		String randomName = UUID.randomUUID().toString();
		File rootDir = new File(filePreFix);
		if (!rootDir.exists()) {
			rootDir.mkdirs();
		}
		File dir = new File(rootDir
			+ File.separator + randomName);
		dir.mkdirs();
		File saveDir = new File(
			rootDir
				+ File.separator + randomName
				+ File.separator + randomName + getFileExt(file.getOriginalFilename()));

		int orientation = getOrientation(file);
		BufferedImage bufferedImage = getRotatedImage(file, orientation);
		bufferedImage = resize(bufferedImage);
		boolean result = ImageIO.write(bufferedImage,
			getFileExt(file.getOriginalFilename()).replaceAll("\\.", ""),
			saveDir);
		log.info("FILE RESIZING RESULT : {}", result);
		return randomName;
	}

	private BufferedImage getRotatedImage(MultipartFile file, int orientation) throws IOException {
		BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
		switch (orientation) {
			case 3:
				bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_180);
				break;
			case 6:
				bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_90);
				break;
			case 8:
				bufferedImage = Scalr.rotate(bufferedImage, Scalr.Rotation.CW_270);
				break;
			default:
				break;
		}
		return bufferedImage;
	}

	private int getOrientation(MultipartFile file) throws IOException {
		int orientation = 1;
		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file.getInputStream());
			ExifIFD0Directory exifDir = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
			//            JpegCommentDirectory jpegDir = metadata.getFirstDirectoryOfType(JpegCommentDirectory.class);
			if (exifDir != null) {
				orientation = exifDir.getInt(ExifIFD0Directory.TAG_ORIENTATION);
			}
		} catch (ImageProcessingException e) {
			throw new IOException("메타데이터 읽기 실패");
		} catch (MetadataException e) {
			log.info("NOT FOUND META DATA");
			//            orientation = 1;
		}
		return orientation;
	}

	public String getFilePreFix() {
		return filePreFix;
	}

	public String getFileExt(String fileName) {
		return fileName.substring(fileName.lastIndexOf("."));
	}

	public String removeFileExt(String fileName) {
		int index = fileName.lastIndexOf(".");
		return fileName.substring(0, index);
	}

	private BufferedImage resize(BufferedImage img) {
		boolean widthCheck = RESIZE_WIDTH < img.getWidth();
		BufferedImage resultImg = null;
		int nHeight = 0;
		int nWidth = 0;

		if (widthCheck) {
			log.info("::::::::: IMAGE RESIZING START :::::::");
			nHeight = RESIZE_WIDTH * img.getHeight() / img.getWidth();
			nWidth = RESIZE_WIDTH;
			MultiStepRescaleOp rescale = new MultiStepRescaleOp(nWidth, nHeight);
			rescale.setUnsharpenMask(AdvancedResizeOp.UnsharpenMask.Soft);
			resultImg = rescale.filter(img, null);
			log.info("::::::::: IMAGE RESIZING END :::::::");
		} else {
			resultImg = img;
		}
		return resultImg;
	}

}
