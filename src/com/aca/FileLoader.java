/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.aca;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Yorozuya
 */
public class FileLoader {
    ArrayList<Node> arrNode = new ArrayList();
    DistArrange distArrange = new DistArrange();

    public FileLoader() {
    }

    public void loadNodeInfo(String url) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(url));
        String line;
        String[] splitLine;
        ArrayList<Node> tempArrNode = new ArrayList<>();
        while ((line = br.readLine()) != null) {
            splitLine = line.split(" ");
            Node tempNode = new Node();
            tempNode.setIndex(splitLine[0]);
            tempNode.setX(Double.valueOf(splitLine[1]));
            tempNode.setY(Double.valueOf(splitLine[2]));
            tempNode.setDemand(Double.valueOf(splitLine[3]));
            tempArrNode.add(tempNode);
        }
        arrNode.clear();
        arrNode.addAll(tempArrNode);
    }

    public int[][] loadNodeInfoACA(String url, int cityNum) throws FileNotFoundException, IOException {
        BufferedReader br = new BufferedReader(new java.io.FileReader(url));
        String line;
        String[] splitLine;
        ArrayList<Node> tempArrNode = new ArrayList<>();
        String[] first = br.readLine().split(" ");
        distArrange.setFirstNode(new Node(first[0],
                Double.valueOf(first[1]),
                Double.valueOf(first[2]),
                Double.valueOf(first[3])));
        while ((line = br.readLine()) != null) {
            splitLine = line.split(" ");
            Node tempNode = new Node();
            tempNode.setIndex(splitLine[0]);
            tempNode.setX(Double.valueOf(splitLine[1]));
            tempNode.setY(Double.valueOf(splitLine[2]));
            tempNode.setDemand(Double.valueOf(splitLine[3]));
            tempArrNode.add(tempNode);
        }
        distArrange.getArrNode().clear();
        distArrange.getArrNode().addAll(tempArrNode);

        int citynum = cityNum;
        //距离矩阵
        int[][] distance = new int[citynum][citynum];
        // 计算距离矩阵
        for (int i = 0; i < citynum - 1; i++) {
            distance[i][i] = -1; // 对角线为0
            for (int j = i + 1; j < citynum; j++) {
                double rij = Math
                        .sqrt(((distArrange.getArrNode().get(i).getX() - distArrange.getArrNode().get(j).getX()) * (distArrange.getArrNode().get(i).getX() - distArrange.getArrNode().get(j).getX()) + (distArrange.getArrNode().get(i).getY() - distArrange.getArrNode().get(j).getY())
                                * (distArrange.getArrNode().get(i).getY() - distArrange.getArrNode().get(j).getY())));
                // 四舍五入，取整
                int tij = (int) Math.round(rij);
                distance[i][j] = tij;
                distance[j][i] = distance[i][j];
            }
        }

        distance[citynum - 1][citynum - 1] = -1;
        return distance;
    }



    public DistArrange getDistArrange() {
        return distArrange;
    }

    public void setDistArrange(DistArrange distArrange) {
        this.distArrange = distArrange;
    }

    public ArrayList<Node> getArrNode() {
        return arrNode;
    }

    public void setArrNode(ArrayList<Node> arrNode) {
        this.arrNode = arrNode;
    }

    public void testPrint() {
        for (int i = 0; i < arrNode.size(); i++) {
            System.out.println(arrNode.get(i).getIndex() + " " + arrNode.get(i).getX() + " " + arrNode.get(i).getY() + " " + arrNode.get(i).getDemand());
        }
    }
}
