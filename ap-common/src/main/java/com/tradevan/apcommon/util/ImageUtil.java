package com.tradevan.apcommon.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * Title: ImageUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
@SuppressWarnings("restriction")
public class ImageUtil {
	
	private static Logger logger = LoggerFactory.getLogger(ImageUtil.class);
	
	public static String compress(String str) throws Exception {
        return compress(str.getBytes("UTF-8"));
    }

    public static String compress(byte[] bytes) throws Exception {
        Deflater deflater = new Deflater();
        deflater.setInput(bytes);
        deflater.finish();
        ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
        byte[] buffer = new byte[1024];
        while(!deflater.finished()) {
            int count = deflater.deflate(buffer);
            bos.write(buffer, 0, count);
        }
        bos.close();
        byte[] output = bos.toByteArray();
        return encodeBase64(output);
    }

    public static String decompress(String str) throws Exception {
    	return decompress(decodeBase64(str));
    }
    
    public static String decompress(byte[] bytes) throws Exception {
        Inflater inflater = new Inflater();
        inflater.setInput(bytes);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(bytes.length);
        byte[] buffer = new byte[1024];
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            bos.write(buffer, 0, count);
        }
        bos.close();
        byte[] output = bos.toByteArray();
        return new String(output);
    }

    public static String encodeBase64(byte[] bytes) throws Exception {
        BASE64Encoder base64Encoder = new BASE64Encoder();
        return base64Encoder.encodeBuffer(bytes).replace("\r\n", "").replace("\n", "");
    }

    public static byte[] decodeBase64(String str) throws Exception {
        BASE64Decoder base64Decoder = new BASE64Decoder();
        return base64Decoder.decodeBuffer(str);
    }
    
    public static void writeDataUrlToFile(String path, String name, String dataUrl) {
    	FileOutputStream fos = null;
    	try {
			int idx1 = dataUrl.indexOf("data:image/");
			int idx2 = dataUrl.indexOf(";base64,");
			if (idx1 == 0 && idx2 != -1) {
				Base64 decoder = new Base64();
				byte[] imgBytes = decoder.decode(dataUrl.substring(idx2 + 8));
				fos = new FileOutputStream(new File(path, name + "." + dataUrl.substring(idx1 + 11, idx2)));
				fos.write(imgBytes);
				fos.flush();
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    	finally {
    		if (fos != null) {
    			try {
					fos.close();
				} catch (IOException e) {
					logger.error(e.getMessage(), e);
				}
    		}
    	}
    }
    
    public static BufferedImage convertDataUrlToImage(String dataUrl) {
    	try {
			int idx1 = dataUrl.indexOf("data:image/");
			int idx2 = dataUrl.indexOf(";base64,");
			if (idx1 == 0 && idx2 != -1) {
				Base64 decoder = new Base64();
				byte[] imgBytes = decoder.decode(dataUrl.substring(idx2 + 8));
				BufferedImage image = ImageIO.read( new ByteArrayInputStream( imgBytes ) );
				return image;
			}
		}
		catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
    	return null;
    }
	
}
