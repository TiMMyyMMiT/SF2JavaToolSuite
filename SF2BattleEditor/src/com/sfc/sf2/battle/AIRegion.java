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
    
    public void validateRegionPoints() {
        int index = -1;
        int dist = Integer.MAX_VALUE;
        for (int i = 0; i < points.length; i++) {
            int calcDist = Math.abs((points[i].x*points[i].x)-(points[i].y*points[i].y));
            if (calcDist < dist) {
                index = i;
                dist = calcDist;
            }
        }
        if (index > 0) {
            //The first point is not top-left
            Point[] newPoints = new Point[4];
            for (int i = 0; i < newPoints.length; i++) {
                newPoints[i] = points[(index+i)%4];
            }
            points = newPoints;
        }
    }
    
    @Override
    public AIRegion clone() {
        return new AIRegion(type, points[0], points[1], points[2], points[3]);
    }
    
    public static AIRegion emptyAIRegion() {
        return new AIRegion(0, new Point(), new Point(), new Point(), new Point());
    }
}
