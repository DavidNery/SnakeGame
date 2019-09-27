package me.davidnery.snakegame.controllers;

import java.awt.Point;
import java.util.Random;

import me.davidnery.snakegame.components.Fruit;
import me.davidnery.snakegame.components.Snake;
import me.davidnery.snakegame.enums.EMovement;

public class SnakeController {
	
	private final int WINDOWWIDTH, WINDOWHEIGHT;
	
	private final Random RANDOM;
	
	public SnakeController(int windowWidth, int windowHeight, Random random) {	
		this.WINDOWWIDTH = windowWidth;
		this.WINDOWHEIGHT = windowHeight;
		
		this.RANDOM = random;
	}

	public void updatePositions(Snake snake) {
		for(int i = snake.getPoints().size()-1; i>0; i--) {
			Point p = snake.getPoints().get(i-1);
			snake.getPoints().get(i).setLocation(p.getX(), p.getY());
		}
		
		EMovement movementDirection = snake.getMovementDirection();
		
		switch (movementDirection) {
		case NORTH:
			snake.getPoints().get(0).y -= 10;
			break;
		case SOUTH:
			snake.getPoints().get(0).y += 10;
			break;
		case WEST:
			snake.getPoints().get(0).x -= 10;
			break;
		default:
			snake.getPoints().get(0).x += 10;
			break;
		}
	}
	
	public boolean checkEat(Snake snake, Fruit fruit) {
		Point head = snake.getPoints().get(0);
		if(head.getX() == fruit.getX() && head.getY() == fruit.getY()) {
			fruit.setX(RANDOM.nextInt(WINDOWWIDTH/10)*10);
			fruit.setY(RANDOM.nextInt(WINDOWHEIGHT/10)*10);
			
			Point p = snake.getPoints().get(snake.getPoints().size()-1);
			switch (snake.getMovementDirection()) {
			case NORTH:
				snake.addPoint(p.x, p.y + 10);
				break;
			case SOUTH:
				snake.addPoint(p.x, p.y - 10);
				break;
			case WEST:
				snake.addPoint(p.x + 10, p.y);
				break;
			default:
				snake.addPoint(p.x - 10, p.y);
				break;
			}
			
			return true;
		}
		
		return false;
	}

}
