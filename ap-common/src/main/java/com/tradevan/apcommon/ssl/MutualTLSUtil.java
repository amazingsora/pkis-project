package com.tradevan.apcommon.ssl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import com.tradevan.apcommon.bean.CommonMsgCode;
import com.tradevan.apcommon.exception.ApRuntimeException;

/**
 * Title: MutualTlsClientUtil<br>
 * Description: <br>
 * Company: Tradevan Co.<br>
 * 
 * @author Neil Chen
 * @version 1.0
 */
public class MutualTLSUtil {

	public static void initTLSClient(String p12File, String p12Pwd, String trustStoreFile, String trustStorePwd) {
		FileInputStream keyStoreStream = null;
		FileInputStream trustStoreStream = null;
		try {
			keyStoreStream = new FileInputStream(new File(p12File));
			KeyStore clientStore = KeyStore.getInstance("PKCS12");
			clientStore.load(keyStoreStream, p12Pwd.toCharArray());
		 
			KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			kmf.init(clientStore, p12Pwd.toCharArray());
			KeyManager[] kms = kmf.getKeyManagers();
		 
			KeyStore trustStore = KeyStore.getInstance("JKS");
			trustStoreStream = new FileInputStream(new File(trustStoreFile));
			trustStore.load(trustStoreStream, trustStorePwd.toCharArray());
		 
			TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
			tmf.init(trustStore);
			TrustManager[] tms = tmf.getTrustManagers();
		 
			SSLContext sslContext = SSLContext.getInstance("TLS");
			sslContext.init(kms, tms, new SecureRandom());
		 
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
		}
		catch (KeyStoreException e) {
			throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
		}
		catch (NoSuchAlgorithmException e) {
			throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
		}
		catch (CertificateException e) {
			throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
		}
		catch (UnrecoverableKeyException e) {
			throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
		}
		catch (KeyManagementException e) {
			throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
		}
		catch (FileNotFoundException e) {
			throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
		}
		catch (IOException e) {
			throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
		}
		finally {
			if (keyStoreStream != null) {
				try {
					keyStoreStream.close();
				}
				catch (IOException e) {
					throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
				}
			}
			if (trustStoreStream != null) {
				try {
					trustStoreStream.close();
				}
				catch (IOException e) {
					throw new ApRuntimeException(CommonMsgCode.CODE_F_SYS_ERR, e.getMessage(), e);
				}
			}
		}
	}
	
}
