package Client;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;


public class Transaction {

	public String transactionID;
	public PublicKey sender;
	public PublicKey recipient;
	public Float value;
	public String signature; //prevent 3rd party from spending our fund.
	
	public ArrayList<TransactionInput> inputs = new ArrayList<TransactionInput>();
	public ArrayList<TransactionOutput> outputs =  new ArrayList<TransactionOutput>();
	
	private static int sequence = 0;
	
	//Constructor
	public Transaction(PublicKey from, PublicKey to, float value, ArrayList<TransactionInput> inputs) {
		this.sender = from;
		this.recipient = to;
		this.value = value;
		this.inputs = inputs;
	}
	
	public Transaction(Transaction transaction) {
		// TODO Auto-generated constructor stub
		this.sender = transaction.sender;
		this.recipient = transaction.recipient;
		this.value = transaction.value;
		this.inputs = transaction.inputs;
	}

	private String calculateHash() {
		sequence ++;
		return StringUtil.applySha256(
				StringUtil.publicKeyToString(sender) +
				StringUtil.publicKeyToString(recipient) +
				Float.toString(value) + sequence);
	}
	
	
	//this was put after the expansion of StringUtil to ECDSA
	public void generateSignature(PrivateKey privateKey) {
		String data = StringUtil.publicKeyToString(sender) + StringUtil.publicKeyToString(recipient);
		signature = StringUtil.applyECDSASig(privateKey, data);
	}
	
	public boolean verifySignature() {
		String data = StringUtil.publicKeyToString(sender) + StringUtil.publicKeyToString(recipient);
		return StringUtil.verifyECDSASig(sender, data, signature);
	}
	
	public boolean processTransaction() {
		if (verifySignature() == false) {
			System.out.println("transaction signature is false");
			return false;
		}
		
		//gathering inputs
		for (TransactionInput i : inputs) {
			i.UTXO = Blockchain.UTXOs.get(i.transactionOutputId);
		}
		
		//is the transaction valid?
		if(getInputsValue() < Blockchain.minimumTransaction) {
			System.out.println("#Transaction Input too small:" + getInputsValue());
			return false;
		}
		
		//generate transaction outputs
		float LeftOver = getInputsValue() - value;
		transactionID = calculateHash();
		outputs.add(new TransactionOutput(this.recipient, value, transactionID)); //send value to recipient
		outputs.add(new TransactionOutput(this.sender, LeftOver, transactionID)); //get "changes" back
		
		//add to unspent list
		for (TransactionOutput o : outputs) {
			Blockchain.UTXOs.put(o.ID, o);
		}
		
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue;
			Blockchain.UTXOs.remove(i.UTXO.ID);
		}
		
		return true;
	}
	
	//get the number of unspent input
	public float getInputsValue() {
		float total = 0;
		for(TransactionInput i : inputs) {
			if(i.UTXO == null) continue;
			total += i.UTXO.value;
		}
		return total;
	}
	
	public float getOutputsValue() {
		float total = 0;
		for(TransactionOutput o : outputs)
		{
			total += o.value;
		}
		return total;
	}

	public static Transaction coinbase() {
		// TODO Auto-generated method stub
		Transaction coinBaseTransaction = new Transaction(MainStartScreen.coinBase.publicKey, MainStartScreen.walletA.publicKey, 100f, null);
		coinBaseTransaction.generateSignature(MainStartScreen.coinBase.privateKey); // manually sign the genesis transaction
		coinBaseTransaction.transactionID = "0"; // manually set the transaction id
		coinBaseTransaction.outputs.add(new TransactionOutput(coinBaseTransaction.recipient, coinBaseTransaction.value,
				coinBaseTransaction.transactionID)); // manually add the Transactions Output
		Blockchain.UTXOs.put(coinBaseTransaction.outputs.get(0).ID, coinBaseTransaction.outputs.get(0));
		return coinBaseTransaction;
	}
}

