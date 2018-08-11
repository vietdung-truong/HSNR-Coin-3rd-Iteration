package Server;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;


public class ServerTransaction {

	public String transactionID;
	public PublicKey sender;
	public PublicKey recipient;
	public Float value;
	public String signature; //prevent 3rd party from spending our fund.
	
	public ArrayList<ServerTransactionInput> inputs = new ArrayList<ServerTransactionInput>();
	public ArrayList<ServerTransactionOutput> outputs =  new ArrayList<ServerTransactionOutput>();
	
	private static int sequence = 0;
	
	//Constructor
	public ServerTransaction(PublicKey from, PublicKey to, float value, ArrayList<ServerTransactionInput> inputs) {
		this.sender = from;
		this.recipient = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	public ServerTransaction(ServerTransaction transaction) {
		// TODO Auto-generated constructor stub
		this.sender = transaction.sender;
		this.recipient = transaction.recipient;
		this.value = transaction.value;
		this.inputs = transaction.inputs;
	}

	private String calculateHash() {
		sequence ++;
		return ServerStringUtil.applySha256(
				ServerStringUtil.publicKeyToString(sender) +
				ServerStringUtil.publicKeyToString(recipient) +
				Float.toString(value) + sequence);
	}
	
	
	//this was put after the expansion of StringUtil to ECDSA
	public void generateSignature(PrivateKey privateKey) {
		String data = ServerStringUtil.publicKeyToString(sender) + ServerStringUtil.publicKeyToString(recipient);
		signature = ServerStringUtil.applyECDSASig(privateKey, data);
	}
	
	public boolean verifySignature() {
		String data = ServerStringUtil.publicKeyToString(sender) + ServerStringUtil.publicKeyToString(recipient);
		return ServerStringUtil.verifyECDSASig(sender, data, signature);
	}
	
	public boolean processTransaction() {
		if (verifySignature() == false) {
			System.out.println("transaction signature is false");
			return false;
		}
		
		//gathering inputs
		for (ServerTransactionInput i : inputs) {
			i.UTXO = ServerBlockchain.UTXOs.get(i.transactionOutputId);
		}
		
		//is the transaction valid?
		if(getInputsValue() < ServerBlockchain.minimumTransaction) {
			System.out.println("#Transaction Input too small:" + getInputsValue());
			return false;
		}
		
		//generate transaction outputs
		float LeftOver = getInputsValue() - value;
		transactionID = calculateHash();
		outputs.add(new ServerTransactionOutput(this.recipient, value, transactionID)); //send value to recipient
		outputs.add(new ServerTransactionOutput(this.sender, LeftOver, transactionID)); //get "changes" back
		
		//add to unspent list
		for (ServerTransactionOutput o : outputs) {
			ServerBlockchain.UTXOs.put(o.ID, o);
		}
		
		for(ServerTransactionInput i : inputs) {
			if(i.UTXO == null) continue;
			ServerBlockchain.UTXOs.remove(i.UTXO.ID);
		}
		
		return true;
	}
	
	//get the number of unspent input
	public float getInputsValue() {
		float total = 0;
		for(ServerTransactionInput i : inputs) {
			if(i.UTXO == null) continue;
			total += i.UTXO.value;
		}
		return total;
	}
	
	public float getOutputsValue() {
		float total = 0;
		for(ServerTransactionOutput o : outputs)
		{
			total += o.value;
		}
		return total;
	}

	public static ServerTransaction coinbase() {
		// TODO Auto-generated method stub
		ServerTransaction coinBaseTransaction = new ServerTransaction(MainFirstServer.coinBase.publicKey, MainFirstServer.walletA.publicKey, 100f, null);
		coinBaseTransaction.generateSignature(MainFirstServer.coinBase.privateKey); // manually sign the genesis transaction
		coinBaseTransaction.transactionID = "0"; // manually set the transaction id
		coinBaseTransaction.outputs.add(new ServerTransactionOutput(coinBaseTransaction.recipient, coinBaseTransaction.value,
				coinBaseTransaction.transactionID)); // manually add the Transactions Output
		ServerBlockchain.UTXOs.put(coinBaseTransaction.outputs.get(0).ID, coinBaseTransaction.outputs.get(0));
		return coinBaseTransaction;
	}
}

