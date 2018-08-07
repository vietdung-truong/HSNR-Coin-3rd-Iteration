package GUI;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Client.MainStartScreen;
import Client.StringUtil;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SpringLayout;

public class Portfolio extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Portfolio frame = new Portfolio();
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Portfolio() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 440, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		SpringLayout sl_contentPane = new SpringLayout();
		contentPane.setLayout(sl_contentPane);
		
		JLabel lblDeinPublicAdress = new JLabel("Dein Public Adress:");
		sl_contentPane.putConstraint(SpringLayout.NORTH, lblDeinPublicAdress, 9, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, lblDeinPublicAdress, 0, SpringLayout.WEST, contentPane);
		contentPane.add(lblDeinPublicAdress);
		
		JButton btnCopy = new JButton("Copy");
		sl_contentPane.putConstraint(SpringLayout.EAST, btnCopy, -5, SpringLayout.EAST, contentPane);
		sl_contentPane.putConstraint(SpringLayout.NORTH, btnCopy, -4, SpringLayout.NORTH, lblDeinPublicAdress);
		btnCopy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		contentPane.add(btnCopy);
		
		JLabel lblDeinVerlauf = new JLabel("Dein Verlauf:");
		sl_contentPane.putConstraint(SpringLayout.EAST, lblDeinVerlauf, -168, SpringLayout.EAST, contentPane);
		contentPane.add(lblDeinVerlauf);
		
		JTextArea textVerlauf = new JTextArea();
		sl_contentPane.putConstraint(SpringLayout.SOUTH, lblDeinVerlauf, -6, SpringLayout.NORTH, textVerlauf);
		sl_contentPane.putConstraint(SpringLayout.NORTH, textVerlauf, 96, SpringLayout.NORTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.WEST, textVerlauf, 0, SpringLayout.WEST, lblDeinPublicAdress);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textVerlauf, 0, SpringLayout.SOUTH, contentPane);
		sl_contentPane.putConstraint(SpringLayout.EAST, textVerlauf, 419, SpringLayout.WEST, contentPane);
		contentPane.add(textVerlauf);
		
		JTextArea textPublicAdress = new JTextArea(StringUtil.publicKeyToString(MainStartScreen.walletA.publicKey));
		sl_contentPane.putConstraint(SpringLayout.NORTH, textPublicAdress, 0, SpringLayout.NORTH, btnCopy);
		sl_contentPane.putConstraint(SpringLayout.WEST, textPublicAdress, 6, SpringLayout.EAST, lblDeinPublicAdress);
		sl_contentPane.putConstraint(SpringLayout.SOUTH, textPublicAdress, -6, SpringLayout.NORTH, lblDeinVerlauf);
		sl_contentPane.putConstraint(SpringLayout.EAST, textPublicAdress, -6, SpringLayout.WEST, btnCopy);
		contentPane.add(textPublicAdress);
		textPublicAdress.setLineWrap(true);
	}
}
