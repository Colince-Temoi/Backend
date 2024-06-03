package tech.csm.util;

import java.io.FileOutputStream;
import java.io.InputStream;

import jakarta.servlet.http.Part;

public class FileUploadUtil {
	public static String uploadFile(Part part) {
		try {
			InputStream is = part.getInputStream();
			byte[] allData = is.readAllBytes();
			FileOutputStream fos = new FileOutputStream("D:\\file_server\\" + part.getSubmittedFileName());
			fos.write(allData);
			is.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return part.getSubmittedFileName();		
	}
}
