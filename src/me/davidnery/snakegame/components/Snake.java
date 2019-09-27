package me.davidnery.snakegame.components;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Snake {
	
	private List<Point> points;
	
	public Snake() {
		this.points = new ArrayList<>();
	}
	
	public void addPoint(int x, int y) {
		points.add(new Point(x, y));
	}
	
	public List<Point> getPoints() {
		return points;
	}

}
