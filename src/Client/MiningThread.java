package Client;

import java.awt.Component;

import javax.swing.JOptionPane;

public class MiningThread extends Thread{
	

	@SuppressWarnings("null")
	public void run() {
		while(true) {
			if(!Blockchain.blocklessPool.isEmpty()) {
				Block tempBlock = new Block(Blockchain.blockchain.get(Blockchain.blockchain.size()-1).hash);
				for (int i = 0; i < 10; i++) {
					Transaction tempTransaction = Blockchain.blocklessPool.get(i);
					tempTransaction = new Transaction(MainStartScreen.coinBase.publicKey, MainStartScreen.coinBase.publicKey, 100f, null);
					tempTransaction.generateSignature(MainStartScreen.coinBase.privateKey); // manually sign the genesis transaction
					tempTransaction.transactionID = "0"; // manually set the transaction id
					tempTransaction.outputs.add(new TransactionOutput(tempTransaction.recipient, tempTransaction.value,
							tempTransaction.transactionID)); // manually add the Transactions Output
					Blockchain.UTXOs.put(tempTransaction.outputs.get(0).ID, tempTransaction.outputs.get(0));
					tempBlock.addTransaction(tempTransaction);
					//Setting parameter
					if(Blockchain.blocklessPool.isEmpty()) break;
				}
				Blockchain.addBlock(tempBlock);
				for(Transaction i : tempBlock.transactions) {
					for(int b = 0; b < Blockchain.blocklessPool.size(); b++) {
						if(i.transactionID.equals(Blockchain.blocklessPool.get(b).transactionID)) {
							Blockchain.blocklessPool.remove(b);
						}
					}
				}
				Blockchain.isChainValid();
				Component frame = null;
				//default title and icon
				JOptionPane.showMessageDialog(frame,
				    "Ein Block wurde gemint! Hash:\n" + tempBlock.hash);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				frame.setVisible(false);
			}
		}
	}

}
