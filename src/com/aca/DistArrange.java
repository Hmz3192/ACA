package com.aca;

import java.util.ArrayList;

/**
 * @Author Hu mingzhi
 * Created by ThinKPad on 2019/12/23.
 */
public class DistArrange {

    ArrayList<Node> arrNode;
    double totalDistance, currentDemand, rechargeCount;
    Node firstNode;


    public DistArrange() {
        arrNode=new ArrayList<>();
        currentDemand=100;  //载货容量初始为100
        rechargeCount=0;
        totalDistance=0;
        firstNode=new Node();
    }

    public ArrayList<Node> getArrNode() {
        return arrNode;
    }

    public void setArrNode(ArrayList<Node> arrNode) {
        this.arrNode = arrNode;
    }

    public double getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(double totalDistance) {
        this.totalDistance = totalDistance;
    }

    public double getCurrentDemand() {
        return currentDemand;
    }

    public void setCurrentDemand(double currentDemand) {
        this.currentDemand = currentDemand;
    }

    public double getRechargeCount() {
        return rechargeCount;
    }

    public void setRechargeCount(double rechargeCount) {
        this.rechargeCount = rechargeCount;
    }

    public Node getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(Node firstNode) {
        this.firstNode = firstNode;
    }
}


