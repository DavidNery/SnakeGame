package me.davidnery.snakegame.panel;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

import me.davidnery.snakegame.components.Fruit;
import me.davidnery.snakegame.components.Snake;
import me.davidnery.snakegame.enums.EMovement;

public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;

	private final int WIDTH, HEIGHT;

	private final Random RANDOM;

	private final Snake SNAKE;
	private final Fruit FRUIT;

	private EMovement movementDirection;
	
	private final Timer timer;

	public GamePanel(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		RANDOM = new Random();

		setBackground(Color.black);
		setFocusable(true);

		this.SNAKE = new Snake();
		int x = RANDOM.nextInt(WIDTH/10)*10, y = RANDOM.nextInt(HEIGHT/10)*10;
		SNAKE.addPoint(x, y);
		SNAKE.addPoint(x-10, y);

		this.movementDirection = EMovement.values()[RANDOM.nextInt(EMovement.values().length)];
		
		if(x == 0 && movementDirection == EMovement.WEST)
			movementDirection = EMovement.EAST;
		else if(y == 0 && movementDirection == EMovement.NORTH)
			movementDirection = EMovement.SOUTH;
		else if(x == WIDTH-10 && movementDirection == EMovement.EAST)
			movementDirection = EMovement.WEST;
		else if(y == HEIGHT-10 && movementDirection == EMovement.SOUTH)
			movementDirection = EMovement.NORTH;
		
		addKeyListener(new KeyController());
		
		FRUIT = new Fruit(RANDOM.nextInt(WIDTH/10)*10, RANDOM.nextInt(HEIGHT/10)*10);
		
		timer = new Timer(100, this);
		timer.start();

	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		updatePositions();
		checkEat();
		
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;
		
		g2d.setColor(Color.cyan);
		g2d.fillRect(0, 0, 10, 10);
		g2d.fillRect(WIDTH-10, 0, 10, 10);
		g2d.fillRect(0, HEIGHT-10, 10, 10);
		g2d.fillRect(WIDTH-10, HEIGHT-10, 10, 10);
		
		drawFruit(g2d);
		drawSnake(g2d);
		drawScore(g2d);

	}
	
	public void updatePositions() {
		for(int i = SNAKE.getPoints().size()-1; i>0; i--) {
			Point p = SNAKE.getPoints().get(i-1);
			SNAKE.getPoints().get(i).setLocation(p.getX(), p.getY());
		}
		
		switch (movementDirection) {
		case NORTH:
			SNAKE.getPoints().get(0).y -= 10;
			break;
		case SOUTH:
			SNAKE.getPoints().get(0).y += 10;
			break;
		case WEST:
			SNAKE.getPoints().get(0).x -= 10;
			break;
		default:
			SNAKE.getPoints().get(0).x += 10;
			break;
		}
	}
	
	public void checkEat() {
		Point head = SNAKE.getPoints().get(0);
		if(head.getX() == FRUIT.getX() && head.getY() == FRUIT.getY()) {
			FRUIT.setX(RANDOM.nextInt(WIDTH/10)*10);
			FRUIT.setY(RANDOM.nextInt(HEIGHT/10)*10);
			
			Point p = SNAKE.getPoints().get(SNAKE.getPoints().size()-1);
			switch (movementDirection) {
			case NORTH:
				SNAKE.addPoint(p.x, p.y + 10);
				break;
			case SOUTH:
				SNAKE.addPoint(p.x, p.y - 10);
				break;
			case WEST:
				SNAKE.addPoint(p.x + 10, p.y);
				break;
			default:
				SNAKE.addPoint(p.x - 10, p.y);
				break;
			}
		}
	}
	
	private void drawSnake(Graphics2D g2d) {
		g2d.setColor(Color.white);
		
		SNAKE.getPoints().forEach(p -> g2d.fillRect(p.x, p.y, 10, 10));
		
		Point head = SNAKE.getPoints().get(0);
		System.out.println(head.y + " " + HEIGHT);
		if(head.x-10 == 0 || head.x+10 == WIDTH|| head.y == 0 || head.y == HEIGHT) timer.stop();
	}
	
	private void drawFruit(Graphics2D g2d) {
		g2d.setColor(Color.red);
		
		g2d.fillRect(FRUIT.getX(), FRUIT.getY(), 10, 10);
	}
	
	public void drawScore(Graphics2D g2d) {
		g2d.setColor(Color.blue);
		
		g2d.drawString("Score: " + SNAKE.getPoints().size(), 10, 10);
	}
	
	private class KeyController extends KeyAdapter {
		
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(SNAKE.getPoints().size() != 1 && movementDirection == EMovement.EAST) break;
				movementDirection = EMovement.WEST;
				break;
			case KeyEvent.VK_RIGHT:
				if(SNAKE.getPoints().size() != 1 && movementDirection == EMovement.WEST) break;
				movementDirection = EMovement.EAST;
				break;
			case KeyEvent.VK_UP:
				if(SNAKE.getPoints().size() != 1 && movementDirection == EMovement.SOUTH) break;
				movementDirection = EMovement.NORTH;
				break;
			default:
				if(SNAKE.getPoints().size() != 1 && movementDirection == EMovement.NORTH) break;
				movementDirection = EMovement.SOUTH;
			}
		}
		
	}

}
