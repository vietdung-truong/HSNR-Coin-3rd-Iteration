package Client;



public class UpdateThread extends Thread{
	
	public void run() {
		while(true){
			MainStartScreen.txtCoin.setText(Float.toString(MainStartScreen.walletA.getBalance()));
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
