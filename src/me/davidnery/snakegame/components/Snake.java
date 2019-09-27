package me.davidnery.snakegame.components;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import me.davidnery.snakegame.enums.EMovement;

public class Snake {
	
	private List<Point> points;
	
	private EMovement movementDirection;
	
	private boolean isDead;
	
	public Snake(EMovement movementDirection) {
		this.points = new ArrayList<>();
		
		this.movementDirection = movementDirection;
		
		isDead = false;
	}
	
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}
	
	public List<Point> getPoints() {
		return points;
	}
	
	public void setMovementDirection(EMovement movementDirection) {
		this.movementDirection = movementDirection;
	}
	
	public EMovement getMovementDirection() {
		return movementDirection;
	}
	
	public void setDead(boolean isDead) {
		this.isDead = isDead;
	}
	
	public boolean isDead() {
		return isDead;
	}

}
