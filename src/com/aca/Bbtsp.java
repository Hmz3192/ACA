package com.aca;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @Author Hu mingzhi
 * Created by ThinKPad on 2019/12/19.
 */
public class Bbtsp {
    int [][]distance;//距离矩阵
    int citynum = 48;
    public Bbtsp(int [][]dis) {
        this.distance = dis;
    }
    public Bbtsp() {
    }


    public int[][] init(String filename) throws IOException {
        // 读取数据
        int[] x;
        int[] y;
        String strbuff;
        BufferedReader data = new BufferedReader(new InputStreamReader(
                new FileInputStream(filename)));
        distance= new int[citynum][citynum];
        x = new int[citynum];
        y = new int[citynum];
        for (int i = 0; i < citynum; i++) {
            // 读取一行数据，数据格式1 6734 1453
            strbuff = data.readLine();
                // 字符分割
                String[] strcol = strbuff.split(" ");
                x[i] = Integer.valueOf(strcol[1]);// x坐标
                y[i] = Integer.valueOf(strcol[2]);// y坐标
        }
        data.close();

        // 计算距离矩阵
        // ，针对具体问题，距离计算方法也不一样，此处用的是att48作为案例，它有48个城市，距离计算方法为伪欧氏距离，最优值为10628
        for (int i = 0; i < citynum - 1; i++) {
            distance[i][i] = -1; // 对角线为0
            for (int j = i + 1; j < citynum; j++) {
                double rij = Math
                        .sqrt(((x[i] - x[j]) * (x[i] - x[j]) + (y[i] - y[j])
                                * (y[i] - y[j])) /10.0);
                // 四舍五入，取整
                int tij = (int) Math.round(rij);
                if (tij < rij) {
                    distance[i][j] = tij + 1;
                    distance[j][i] = distance[i][j];
                } else {
                    distance[i][j] = tij;
                    distance[j][i] = distance[i][j];
                }
            }
        }

        distance[citynum - 1][citynum - 1] = -1;
        return distance;
    }
}
