package Server;


public class ServerTransactionInput {

	public String transactionOutputId; //referenziert if transationIDs
	public ServerTransactionOutput UTXO; //dass sind unbenutzte Inputs, die dir zur Verf�gung stehen
	
	public ServerTransactionInput(String transactionOutputId) {
	this.transactionOutputId = transactionOutputId;
	}
}
