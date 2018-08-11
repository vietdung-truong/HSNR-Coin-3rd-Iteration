package Server;


import java.util.ArrayList;
import java.util.HashMap;


public class ServerBlockchain {

	public static ArrayList<ServerBlock> blockchain = new ArrayList<ServerBlock>();
	public static HashMap<String, ServerTransactionOutput> UTXOs = new HashMap<String, ServerTransactionOutput>(); // listet alle
																										// noch nicht
																										// benutzte
																										// transaktionen
	public static ArrayList<ServerTransaction> blocklessPool = new ArrayList<ServerTransaction>();

	public static float minimumTransaction = 0.1f;
	final static int difficulty = 1;



	public static void addBlock(ServerBlock newBlock) {// hier wird ein Block gemint und danach ins das Blockchain geadded.
		// TODO Auto-generated method stub
		newBlock.mineBlock(difficulty);
		blockchain.add(newBlock);
	}
	
	public static void initiateblocklessPool() {
		for (int i = 0; i < 50; i++) {
			blocklessPool.add(ServerTransaction.coinbase());
		}
	}

	public static Boolean isChainValid() {// hier werden die Werte von hashes und previoushashes mit den 'calculate'
		// Werten verglichen. Falls die Werte nicht stimmen, liefert der Vergleich eine
		// negative Antwort
		ServerBlock currentBlock;
		ServerBlock previousBlock;
		String hashTarget = new String(new char[difficulty]).replace('\0', '0');
		HashMap<String, ServerTransactionOutput> tempUTXOs = new HashMap<String, ServerTransactionOutput>(); // a temporary working
																									// list of unspent
																									// transactions at a
																									// given block
																									// state.
		tempUTXOs.put(MainFirstServer.genesisTransaction.outputs.get(0).ID, MainFirstServer.genesisTransaction.outputs.get(0));
		for (int i = 1; i < blockchain.size(); i++) {
			currentBlock = blockchain.get(i);
			previousBlock = blockchain.get(i - 1);
			// compare registered hash and calculated hash:
			if (!currentBlock.hash.equals(currentBlock.testHash)) {
				System.out.println(i + ": Current Hashes not equal");
				return false;
			}

			// compare previous hash and registered previous hash
			if (!previousBlock.hash.equals(currentBlock.previousHash)) {
				System.out.println("Previous Hashes not equal");
				return false;
			}
			if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget)) {
				System.out.println("#This block hasn't been mined");
				return false;
			}

			// loop thru blockchains transactions:
			ServerTransactionOutput tempOutput;
			for (int t = 0; t < currentBlock.transactions.size(); t++) {
				ServerTransaction currentTransaction = currentBlock.transactions.get(t);

				if (!currentTransaction.verifySignature()) {
					System.out.println("#Signature on Transaction(" + t + ") is Invalid");
					return false;
				}
				if (currentTransaction.getInputsValue() != currentTransaction.getOutputsValue()) {
					System.out.println("#Inputs are note equal to outputs on Transaction(" + t + ")");
					return false;
				}

				for (ServerTransactionInput input : currentTransaction.inputs) {
					tempOutput = tempUTXOs.get(input.transactionOutputId);

					if (tempOutput == null) {
						System.out.println("#Referenced input on Transaction(" + t + ") is Missing");
						return false;
					}

					if (input.UTXO.value != tempOutput.value) {
						System.out.println("#Referenced input Transaction(" + t + ") value is Invalid");
						return false;
					}

					tempUTXOs.remove(input.transactionOutputId);
				}

				for (ServerTransactionOutput output : currentTransaction.outputs) {
					tempUTXOs.put(output.ID, output);
				}

				if (currentTransaction.outputs.get(0).recipient != currentTransaction.recipient) {
					System.out.println("#Transaction(" + t + ") output reciepient is not who it should be");
					return false;
				}
				if (currentTransaction.outputs.get(1).recipient != currentTransaction.sender) {
					System.out.println("#Transaction(" + t + ") output 'change' is not sender.");
					return false;
				}

			}

		}
		System.out.println("Blockchain is valid");
		return true;
	}
}

