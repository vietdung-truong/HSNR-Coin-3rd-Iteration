package Client;


import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.util.ArrayList;


import org.bouncycastle.asn1.x9.X9ECParameters;
import org.bouncycastle.crypto.ec.CustomNamedCurves;
import org.bouncycastle.jcajce.provider.asymmetric.util.EC5Util;
import org.bouncycastle.jce.ECPointUtil;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECParameterSpec;
import org.bouncycastle.jce.spec.ECPrivateKeySpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.math.ec.ECPoint;

import static stringUtil.Hex.getHex;
import static stringUtil.Hex.fromHex;
import static stringUtil.Base58.encode;
import static stringUtil.Base58.decode;


public class StringUtil {
	
	static String WIF_PREFIX = "0x80";
    static String curve = "secp256k1";
    static X9ECParameters x9Params = CustomNamedCurves.getByName(curve);
    
    

	public static String getWIF_PREFIX() {
		return WIF_PREFIX;
	}

	public static void setWIF_PREFIX(String wIF_PREFIX) {
		WIF_PREFIX = wIF_PREFIX;
	}

	public static String getCurve() {
		return curve;
	}

	public static void setCurve(String curve) {
		StringUtil.curve = curve;
	}

	public static X9ECParameters getX9Params() {
		return x9Params;
	}

	public static void setX9Params(X9ECParameters x9Params) {
		StringUtil.x9Params = x9Params;
	}

	// applies Sha256 to a string and returns the result.
	public static String applySha256(String input) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			byte[] hash = digest.digest(input.getBytes("UTF-8"));
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < hash.length; i++) {
				String hex = Integer.toHexString(0xff & hash[i]);
				if (hex.length() == 1)
					hexString.append('0');
				hexString.append(hex);
			}
			return hexString.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static String applyECDSASig(PrivateKey privateKey, String input) {
		// Sign the transaction with privatekey
		Signature dsa;
		//byte[] outputByte = new byte[0];
		String output;
		try {
			dsa = Signature.getInstance("ECDSA", "BC");
			dsa.initSign(privateKey);
			byte[] strByte = input.getBytes();
			dsa.update(strByte);
			byte[] realSig = dsa.sign();
			output = new String(realSig, "ISO-8859-1");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return output;
	}

	public static boolean verifyECDSASig(PublicKey publicKey, String data, String signature) {
		try {
			Signature ecdsaVerify = Signature.getInstance("ECDSA", "BC");
			ecdsaVerify.initVerify(publicKey);
			ecdsaVerify.update(data.getBytes());
			byte[] encoded = signature.getBytes("ISO-8859-1"); 
			return ecdsaVerify.verify(encoded);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	// find out more about merkleroot
	public static String getMerkleRoot(ArrayList<Transaction> transactions) {
		int count = transactions.size();
		ArrayList<String> previousTreeLayer = new ArrayList<String>();
		for (Transaction transaction : transactions) {
			previousTreeLayer.add(transaction.transactionID);
		}
		ArrayList<String> treeLayer = previousTreeLayer;
		while (count > 1) {
			treeLayer = new ArrayList<String>();
			for (int i = 1; i < previousTreeLayer.size(); i++) {
				treeLayer.add(applySha256(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
			}
			count = treeLayer.size();
			previousTreeLayer = treeLayer;
		}
		String merkleRoot = (treeLayer.size() == 1) ? treeLayer.get(0) : "";
		return merkleRoot;
	}

/*	abbandond ue to incompatibility
 * public static String getStringFromKey(Key key) {
		// wird zu Verschlüsselung von TransactionID benutzt
		// TODO Auto-generated method stub
		// Entkodierung durch Base 64
		return Base64.getEncoder().encodeToString(key.getEncoded());
	}
*/	
	 public static String privateKeyToWIF(PrivateKey privateKey) {
	        

	        String tempKey = getHex(((ECPrivateKey)privateKey).getS().toByteArray());

	        if(tempKey.startsWith("00")) {
	            tempKey = tempKey.substring(2);
	        }

	        tempKey = WIF_PREFIX + tempKey;

	        String hash = applySha256(applySha256(tempKey));
	        hash = hash.substring(0,8);

	        String wif = tempKey + hash;
	        return encode(fromHex(wif));
	    }

	 public static String privateKeyToString(PrivateKey privateKey) {
	        
	        return getHex(((ECPrivateKey)privateKey).getS().toByteArray());
	    }
	 
	 public static PrivateKey stringToPrivateKey(String wif) {
	        

	        validateWIF(wif);

	        String temp = getHex(decode(wif));
	        while(temp.startsWith("0")) {
	            temp = temp.substring(1);
	        }

	        temp = temp.substring(2, temp.length() - 8);

	        BigInteger s = new BigInteger(temp, 16);
	        ECParameterSpec ecParameterSpec = new ECNamedCurveParameterSpec(curve, x9Params.getCurve(), x9Params.getG(), x9Params.getN(), x9Params.getH(), x9Params.getSeed());
	        ECPrivateKeySpec privateKeySpec = new ECPrivateKeySpec(s, ecParameterSpec);
	        KeyFactory factory;
	        try {
	            factory = KeyFactory.getInstance("ECDSA","BC");
	        }
	        catch(Exception e) {
	            System.out.println("The key generator algorithm was not found.");
	            throw new RuntimeException(e);
	        }
	        PrivateKey privateKey;
	        try {
	            privateKey = factory.generatePrivate(privateKeySpec);
	        }
	        catch (Exception e) {
	            System.out.println("The keyspec on the private key string is invalid.");
	            throw new RuntimeException(e);
	        }
	        return privateKey;
	    }

	 public static void validateWIF(String wif) {
	        
	        wif = getHex(decode(wif));
	        String prefix = wif.substring(0, 2);
	        String key = wif.substring(2, wif.length() - 8);
	        String chksum = wif.substring(wif.length() - 8);
	        try{
	            if(getHex(fromHex(prefix)).equals(getHex(fromHex(WIF_PREFIX)))) {
	                String keyHash = applySha256(applySha256(("0x" + prefix + key)));
	                String checkSum = keyHash.substring(0,8);

	                if(!chksum.equals(checkSum)) {
	                    throw new RuntimeException("WIF Invalid: Checksum is invalid.");
	                }
	            }else {
	                throw new RuntimeException("WIF Invalid: Prefix does not match.");
	            }
	        }
	        catch(Exception e) {
	            System.out.println("Wallet import failed.");
	            throw new RuntimeException(e);
	        }
	    }
	 
	 public static String publicKeyToString(PublicKey publicKey) {
		 	Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
	        String tempX = getHex(((ECPublicKey)publicKey).getW().getAffineX().toByteArray());
	        String tempY = getHex(((ECPublicKey)publicKey).getW().getAffineY().toByteArray());

	        if (tempX.startsWith("00")) {
	            tempX = tempX.substring(2);
	        }

	        if (tempY.startsWith("00")) {
	            tempY = tempY.substring(2);
	        }

	        String temp = tempX + tempY;
	        temp = "0x04" + temp;

	        return encode(fromHex(temp));
	    }
	 
	 public static String publicKeyToHexString(PublicKey publicKey) {
	        Security.addProvider(new BouncyCastleProvider());
	        String tempX = getHex(((ECPublicKey)publicKey).getW().getAffineX().toByteArray());
	        String tempY = getHex(((ECPublicKey)publicKey).getW().getAffineY().toByteArray());

	        if (tempX.startsWith("00")) {
	            tempX = tempX.substring(2);
	        }

	        if (tempY.startsWith("00")) {
	            tempY = tempY.substring(2);
	        }

	        String temp = tempX + tempY;
	        temp = "0x04" + temp;

	        return temp;
	    }
	 
	 public static PublicKey privateKeyToPublicKey(PrivateKey privateKey) {
	        Security.addProvider(new BouncyCastleProvider());

	        KeyFactory keyFactory;
	        try{
	            keyFactory = KeyFactory.getInstance("ECDSA", "BC");
	        }
	        catch (Exception e) {
	            System.out.println("The key generator algorithm was not found.");
	            throw new RuntimeException(e);
	        }

	        ECParameterSpec ecSpec = new ECParameterSpec(x9Params.getCurve(), x9Params.getG(), x9Params.getN(), x9Params.getH(), x9Params.getSeed());

	        ECPoint Q = ecSpec.getG().multiply(((org.bouncycastle.jce.interfaces.ECPrivateKey) privateKey).getD());

	        ECPublicKeySpec pubSpec = new ECPublicKeySpec(Q, ecSpec);
	        PublicKey publicKey;
	        try{
	            publicKey = keyFactory.generatePublic(pubSpec);
	        }
	        catch(Exception e) {
	            System.out.println("The keyspec on the imported wallet is invalid.");
	            System.out.println("Failed to derive public key from private key.");
	            throw new RuntimeException(e);
	        }
	        return publicKey;
	    }
	 
	 public static PublicKey stringToPublicKey(String pubKeyString) {
	        pubKeyString = getHex(decode(pubKeyString));
	        if(pubKeyString.startsWith("0x")) {
	            pubKeyString= pubKeyString.substring(2);
	        }

	        byte[] encoded = fromHex(pubKeyString);

	        ECNamedCurveParameterSpec params = org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec(curve);

	        KeyFactory fact;
	        try {
	            fact = KeyFactory.getInstance("ECDSA","BC");
	        }
	        catch (Exception e) {
	            System.out.println("The key generator algorithm was not found.");
	            System.out.println("Failed to convert string to public key.");
	            throw new RuntimeException(e);
	        }

	        java.security.spec.EllipticCurve ellipticCurve = EC5Util.convertCurve(x9Params.getCurve(), x9Params.getSeed());
	        java.security.spec.ECPoint point= ECPointUtil.decodePoint(ellipticCurve, encoded);
	        java.security.spec.ECParameterSpec params2=EC5Util.convertSpec(ellipticCurve, params);
	        java.security.spec.ECPublicKeySpec keySpec = new java.security.spec.ECPublicKeySpec(point,params2);

	        PublicKey publicKey;
	        try {
	            publicKey = fact.generatePublic(keySpec);
	        }
	        catch (Exception e) {
	            System.out.println("The keyspec on the public key string is invalid.");
	            System.out.println("Failed to convert string to public key.");
	            throw new RuntimeException(e);
	        }

	        return publicKey;
	    }
	 
	 public static PublicKey hexStringToPublicKey(String pubKeyString) {
	        if(pubKeyString.startsWith("0x")) {
	            pubKeyString= pubKeyString.substring(2);
	        }

	        byte[] encoded = fromHex(pubKeyString);

	        ECNamedCurveParameterSpec params = org.bouncycastle.jce.ECNamedCurveTable.getParameterSpec(curve);

	        KeyFactory fact;
	        try {
	            fact = KeyFactory.getInstance("ECDSA","BC");
	        }
	        catch (Exception e) {
	            System.out.println("The key generator algorithm was not found.");
	            System.out.println("Failed to convert string to public key.");
	            throw new RuntimeException(e);
	        }

	        java.security.spec.EllipticCurve ellipticCurve = EC5Util.convertCurve(x9Params.getCurve(), x9Params.getSeed());
	        java.security.spec.ECPoint point= ECPointUtil.decodePoint(ellipticCurve, encoded);
	        java.security.spec.ECParameterSpec params2=EC5Util.convertSpec(ellipticCurve, params);
	        java.security.spec.ECPublicKeySpec keySpec = new java.security.spec.ECPublicKeySpec(point,params2);

	        PublicKey publicKey;
	        try {
	            publicKey = fact.generatePublic(keySpec);
	        }
	        catch (Exception e) {
	            System.out.println("The keyspec on the public key string is invalid.");
	            System.out.println("Failed to convert string to public key.");
	            throw new RuntimeException(e);
	        }

	        return publicKey;
	    }
	 
}
