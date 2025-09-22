/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sfc.sf2.battle;

import java.awt.Point;

/**
 *
 * @author wiz
 */
public class AIRegion {
    private int type;
    private Point[] points = new Point[4];

    public AIRegion(int type, Point p0, Point p1, Point p2, Point p3) {
        this.type = type;
        points[0] = p0;
        points[1] = p1;
        points[2] = p2;
        points[3] = p3;
    }
    public AIRegion(int type, Point[] points) {
        this.type = type;
        this.points = points;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
    
    public Point[] getPoints() {
        return points;
    }
    
    public Point getPoint(int index) {
        return points[index];
    }
    
    @Override
    public AIRegion clone() {
        return new AIRegion(type, points[0], points[1], points[2], points[3]);
    }
    
    public static AIRegion emptyAIRegion() {
        return new AIRegion(0, new Point(), new Point(), new Point(), new Point());
    }
}
