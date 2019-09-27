package me.davidnery.snakegame.panel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;

import me.davidnery.snakegame.components.Fruit;
import me.davidnery.snakegame.components.Snake;
import me.davidnery.snakegame.controllers.SnakeController;
import me.davidnery.snakegame.enums.EMovement;

public class GamePanel extends JPanel {

	private static final long serialVersionUID = 1L;

	private final int WIDTH, HEIGHT;

	private final Random RANDOM;

	private Snake snake;
	private Fruit fruit;

	private final SnakeController SCONTROLLER;

	private final Thread THREAD;

	private boolean run, textDisplayed;

	public GamePanel(int width, int height) {
		WIDTH = width;
		HEIGHT = height;
		RANDOM = new Random();

		setBackground(Color.black);
		setFocusable(true);
		setPreferredSize(new Dimension(WIDTH, HEIGHT));

		addKeyListener(new KeyController());

		SCONTROLLER = new SnakeController(WIDTH, HEIGHT, RANDOM);

		run = true;
		textDisplayed = true;
		THREAD = new Thread(() -> {

			int time;

			while(run) {

				if(snake != null && !snake.isDead()) {
					SCONTROLLER.updatePositions(snake);
					SCONTROLLER.checkEat(snake, fruit);
					time = 100;
				}else {
					time = 600;
				}

				repaint();

				try {
					Thread.sleep(time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

			}

		});
		THREAD.start();

	}

	private void startGame() {
		int x = RANDOM.nextInt(WIDTH/10)*10, y = RANDOM.nextInt(HEIGHT/10)*10;
		EMovement movementDirection = EMovement.values()[RANDOM.nextInt(EMovement.values().length)];
		if(x == 0 && movementDirection == EMovement.WEST)
			movementDirection = EMovement.EAST;
		else if(y == 0 && movementDirection == EMovement.NORTH)
			movementDirection = EMovement.SOUTH;
		else if(x == WIDTH-10 && movementDirection == EMovement.EAST)
			movementDirection = EMovement.WEST;
		else if(y == HEIGHT-10 && movementDirection == EMovement.SOUTH)
			movementDirection = EMovement.NORTH;

		this.snake = new Snake(movementDirection);
		snake.addPoint(x, y);
		snake.addPoint(x-10, y);

		fruit = new Fruit(RANDOM.nextInt(WIDTH/10)*10, RANDOM.nextInt(HEIGHT/10)*10);
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		Graphics2D g2d = (Graphics2D) g;

		if(snake != null) {
			if(!checkGameOver(g2d)) {
				drawSnake(g2d);
				drawFruit(g2d);
				drawScore(g2d);
			}else {
				if(textDisplayed) {
					g2d.setFont(new Font("Verdana", Font.BOLD, 50));
					g2d.setColor(Color.red);
					drawCenteredText("Game Over", WIDTH, HEIGHT, g2d, - 20);

					g2d.setFont(new Font("Verdana", Font.BOLD, 20));
					g2d.setColor(Color.blue);
					drawCenteredText("Press any key to restart", WIDTH, HEIGHT, g2d, 20);
				}

				textDisplayed = !textDisplayed;
			}
		}else {
			if(textDisplayed) {
				g2d.setFont(new Font("Verdana", Font.BOLD, 50));
				g2d.setColor(Color.red);
				drawCenteredText("Snake Game", WIDTH, HEIGHT, g2d, - 20);

				g2d.setFont(new Font("Verdana", Font.BOLD, 20));
				g2d.setColor(Color.blue);
				drawCenteredText("Press any key to start", WIDTH, HEIGHT, g2d, 20);
			}

			textDisplayed = !textDisplayed;
		}
	}

	public void drawCenteredText(String text, int windowWidth, int windowHeight, Graphics g2d, int yOffset) {
		FontMetrics fm = g2d.getFontMetrics();

		int x = (windowWidth - fm.stringWidth(text)) / 2;
		int y = (fm.getAscent() + (windowHeight - (fm.getAscent() + fm.getDescent())) / 2);

		g2d.drawString(text, x, y + yOffset);
	}

	private void drawSnake(Graphics2D g2d) {
		g2d.setColor(Color.white);

		snake.getPoints().forEach(p -> g2d.fillRect(p.x, p.y, 10, 10));
	}

	private void drawFruit(Graphics2D g2d) {
		g2d.setColor(Color.red);

		g2d.fillRect(fruit.getX(), fruit.getY(), 10, 10);
	}

	public void drawScore(Graphics2D g2d) {
		g2d.setColor(Color.blue);

		g2d.drawString("Score: " + snake.getPoints().size(), 10, 20);
	}

	private boolean checkGameOver(Graphics2D g2d) {
		Point head = snake.getPoints().get(0);
		EMovement movementDirection = snake.getMovementDirection();

		if(snake.isDead()) return true;

		if((head.x == -10 && movementDirection == EMovement.WEST) 
				|| (head.x-10 == WIDTH && movementDirection == EMovement.EAST) 
				|| (head.y == -10 && movementDirection == EMovement.NORTH)
				|| (head.y-10 == HEIGHT && movementDirection == EMovement.SOUTH)
				|| snake.getPoints().stream().filter(p -> p.distance(head) == 0).count() == 2) {

			snake.setDead(true);

			textDisplayed = true;
			return true;
		}

		return false;
	}

	private class KeyController extends KeyAdapter {

		@Override
		public void keyPressed(KeyEvent e) {
			if(snake == null || snake.isDead()) {
				startGame();
			}else {
				switch(e.getKeyCode()) {
				case KeyEvent.VK_LEFT:
					if(snake.getPoints().get(1).x == snake.getPoints().get(0).x-10) break;
					snake.setMovementDirection(EMovement.WEST);
					break;
				case KeyEvent.VK_RIGHT:
					if(snake.getPoints().get(1).x == snake.getPoints().get(0).x+10) break;
					snake.setMovementDirection(EMovement.EAST);
					break;
				case KeyEvent.VK_UP:
					if(snake.getPoints().get(1).y == snake.getPoints().get(0).y-10) break;
					snake.setMovementDirection(EMovement.NORTH);
					break;
				default:
					if(snake.getPoints().get(1).y == snake.getPoints().get(0).y+10) break;
					snake.setMovementDirection(EMovement.SOUTH);
				}
			}
		}

	}

}
