package org.yeshira.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

public class DigestUtils {
	private static final Logger logger = Logger.getLogger(DigestUtils.class);

	private static final String MESSAGE_DIGEST_SHA1 = "SHA-1";
	private static final String MESSAGE_DIGEST_MD5 = "MD5";

	private Map<String, MessageDigest> messageDigesters;

	public DigestUtils() throws NoSuchAlgorithmException {
		this.messageDigesters = new HashMap<String, MessageDigest>();
		messageDigesters.put(MESSAGE_DIGEST_SHA1,
				MessageDigest.getInstance(MESSAGE_DIGEST_SHA1));
		messageDigesters.put(MESSAGE_DIGEST_MD5,
				MessageDigest.getInstance(MESSAGE_DIGEST_MD5));
	}

	public String digest(String plainText, String algorithm)
			throws NoSuchAlgorithmException {
		MessageDigest mdAlgorithm = messageDigesters.get(algorithm);
		if (mdAlgorithm == null) {
			mdAlgorithm = MessageDigest.getInstance(algorithm);
			messageDigesters.put(algorithm, mdAlgorithm);
		}
		// Message Digestors are not thread-safe
		synchronized (mdAlgorithm) {
			mdAlgorithm.reset();

			mdAlgorithm.update(plainText.getBytes());

			byte[] digest = mdAlgorithm.digest();
			StringBuffer hexString = new StringBuffer();

			for (byte element : digest) {
				plainText = Integer.toHexString(0xFF & element);

				if (plainText.length() < 2)
					plainText = "0" + plainText;

				hexString.append(plainText);
			}
			return hexString.toString();
		}

	}

	public String md5(String plainText) {
		try {
			return digest(plainText, "MD5");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
			return null;
		}
	}

	public String sh1(String plainText) {
		try {
			return digest(plainText, "SHA-1");
		} catch (NoSuchAlgorithmException e) {
			logger.error(e);
			return null;
		}
	}

}
