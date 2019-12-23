/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aca;

/**
 *
 * @author Yorozuya
 */
public class Node {
    String index;
    double x,y, demand;

    public Node() {
    }

    public Node(String index, double x, double y, double demand) {
        this.index = index;
        this.x = x;
        this.y = y;
        this.demand = demand;
    }



    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getDemand() {
        return demand;
    }

    public void setDemand(double demand) {
        this.demand = demand;
    }
    
}
