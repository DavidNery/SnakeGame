package me.davidnery.snakegame;

import java.awt.EventQueue;

import javax.swing.JFrame;

import me.davidnery.snakegame.panel.GamePanel;

public class Main extends JFrame {

	private static final long serialVersionUID = 1L;
	
	public Main() {
		super("Snake Game");
		
		init();
	}
	
	public void init() {
		
		add(new GamePanel(600, 500));
		
		setResizable(false);
		
		pack();
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setLocationRelativeTo(null);
		
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			new Main();
		});
	}

}
