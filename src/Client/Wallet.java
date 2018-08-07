package Client;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.bouncycastle.jce.spec.ECParameterSpec;


public class Wallet {

	public PublicKey publicKey;
	public PrivateKey privateKey;
	
	public String publicKeyString;
	public String privateKeyString;

	public HashMap<String, TransactionOutput> UTXOs = new HashMap<String, TransactionOutput>();

	public Wallet() {
		this.privateKey = generateNewPrivateKey();
		refreshKeyPair();
	}

	/*abbandoned due to incompactibility
	 * public void generateKeyPair() {
		try {
			KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDSA", "BC");
			SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
			ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
			keyGen.initialize(ecSpec, random);
			KeyPair keyPair = keyGen.generateKeyPair();
			privateKey = keyPair.getPrivate();
			publicKey = keyPair.getPublic();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}*/
	
	public static PrivateKey generateNewPrivateKey() {
        SecureRandom random;
        try {
            random = SecureRandom.getInstance("SHA1PRNG");
        }
        catch (Exception e) {
            System.out.println("The random number generator failed to initialize.");
            throw new RuntimeException(e);
        }

        KeyPairGenerator keyGen;
        try {
            keyGen = KeyPairGenerator.getInstance("ECDSA","BC");
        }
        catch (Exception e) {
            System.out.println("The key generator algorithm was not found.");
            throw new RuntimeException(e);
        }

        ECParameterSpec ecSpec = new ECParameterSpec(StringUtil.x9Params.getCurve(), StringUtil.x9Params.getG(), StringUtil.x9Params.getN(), StringUtil.x9Params.getH(), StringUtil.x9Params.getSeed());

        try {
            keyGen.initialize(ecSpec, random);
        }
        catch (Exception e) {
            System.out.println("The key generator failed to initialize.");
            throw new RuntimeException(e);
        }

        KeyPair keyPair = keyGen.generateKeyPair();

        PrivateKey privateKey = keyPair.getPrivate();
        return privateKey;
    }
	
	public void refreshKeyPair() {
        this.publicKey = StringUtil.privateKeyToPublicKey(this.privateKey);
    }

	public float getBalance() {
		float total = 0;
		for (Map.Entry<String, TransactionOutput> item : Blockchain.UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			if (UTXO.isMine(publicKey)) {// does this belong to me?
				UTXOs.put(UTXO.ID, UTXO);
				total += UTXO.value;
			}
		}
		return total;
	}

	// generate and returns a new transaction from this wallet.
	public Transaction sendFunds(PublicKey _recipient, float value) {
		if (getBalance() < value) {
			System.out.println("#not enough funs to send");
			return null;
		}
		// generating an array list of inputs
		ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();

		float total = 0;
		for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
			TransactionOutput UTXO = item.getValue();
			total += UTXO.value;
			inputs.add(new TransactionInput(UTXO.ID));
			if (total > value)
				break;
		}

		Transaction newTransaction = new Transaction(publicKey, _recipient, value, inputs);
		newTransaction.generateSignature(privateKey);

		for (TransactionInput input : inputs) {
			UTXOs.remove(input.transactionOutputId);
		}
		return newTransaction;
	}

	public PublicKey getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(PublicKey publicKey) {
		this.publicKey = publicKey;
	}

	public PrivateKey getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(PrivateKey privateKey) {
		this.privateKey = privateKey;
	}

	public HashMap<String, TransactionOutput> getUTXOs() {
		return UTXOs;
	}

	public void setUTXOs(HashMap<String, TransactionOutput> uTXOs) {
		UTXOs = uTXOs;
	}
}
