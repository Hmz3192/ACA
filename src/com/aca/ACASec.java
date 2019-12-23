package com.aca;

import java.io.IOException;

/**
 * @Author Hu mingzhi
 * Created by ThinKPad on 2019/12/19.
 */
public class ACASec {

    int ant_num = 50;/*蚂蚁数量*/
    double alpha = 1;//α因子
    double beta = 10;//β因子
    double rho = 0.5;//信息素挥发因子
    int Q = 1;
    int citynum = 31;
    int generation = 100;
    int deltaType; // 信息素更新方式模型，0: Ant-quantity; 1: Ant-density; 2: Ant-cycle
    double minDist = 0;
    int[] BestPath;
    private int[] bestTour;
    private int bestLength;


    public ACASec(int cityNum, int antNum, int generation, double alpha, double beta, double rho, int q, int deltaType) {
        this.ant_num = antNum;
        this.alpha = alpha;//α因子
        this.beta = beta;//β因子
        this.rho = rho;//信息素挥发因子
        this.citynum = cityNum;
        this.Q = q;
        this.deltaType = deltaType;
        this.generation = generation;
    }


    public ACASec() {
    }

    public double[][] init_p(int[][] distance) {
        //对信息素概率的初始量进行更新，初始概率为每个点到其余点的路径和分之路径
        double p[][] = new double[48][48];//用于保存信息素概率
        for (int i = 0; i < 48; i++) {
            double sum = for_each_sum(distance, i);//对每个结点进行和的计算
            for (int j = 0; j < 48; j++) {
                p[i][j] = (distance[i][j] * 1.0) / sum;
            }
        }
        for (int i = 0; i < 48; i++) {
            p[i][i] = 0;
        }
        return p;
    }

    public double[][] init_p2(int[][] distance) {
        //对信息素概率的初始量进行更新，初始概率为每个点到其余点的路径和分之路径
        double p[][] = new double[citynum][citynum];//用于保存信息素概率
        for (int i = 0; i < citynum; i++) {
            double sum = for_each_sum(distance, i);//对每个结点进行和的计算
            for (int j = 0; j < citynum; j++) {
                p[i][j] = (distance[i][j] * 1.0) / sum;
            }
        }
        for (int i = 0; i < citynum; i++) {
            p[i][i] = 0;
        }
        return p;
    }

    public int[] start(double[][] p, int[][] distance) {
        int length = 0;
        int[] allow = new int[citynum];//保存走过的距离
        for (int m = 0; m < citynum; m++) {
            allow[m] = 100;//防止NOT_EXIT失效
        }
        int i = 0;
        //firstnode
        int temple = (int) (Math.random() * citynum);
        allow[i] = temple;
        i++;
        int current = temple;
        while (i < citynum) {
            int tmp[] = new int[citynum];
            for (int m = 0; m < citynum; m++) {
                tmp[m] = allow[m];
            }
            int next = roulette(p, current, tmp, distance);
            allow[i] = next;
            current = next;
            i++;
        }
        return allow;
    }

    public int roulette(double[][] p, int current, int[] allow, int[][] distance) {
        //轮盘赌，current为当前城市标号
        double sum = 0;
        int temple = 0;
        double a = sum_single_possible(distance, p, current, allow);
        while (sum < 0.3) {
            temple = (int) (Math.random() * citynum);
            if (not_exit(temple, allow)) {
                //假如该数不在数组里，则令概率增加
                double n = 1.0 / distance[current][temple];
                n = Math.pow(n, beta);
                double t = p[current][temple];
                t = Math.pow(t, alpha);
                sum = sum + n * t * 1.0 / a;
            }
        }
        return temple;

    }

    public double sum_single_possible(int[][] distance, double[][] p, int current, int[] allow) {
        //根据公式计算概率
        double sum = 0;
        for (int i = 0; i < citynum; i++) {
            if (not_exit(i, allow)) {
                double a = 1.0 / distance[current][i];
                a = Math.pow(a, beta);
                double b = p[current][i];
                b = Math.pow(b, alpha);
                sum = sum + a * b;
            }
        }
        return sum;
    }

    public boolean not_exit(int current, int[] allow) {
        //判断当前数是否已经存在于走过的路径中
        boolean flag = true;
        for (int i = 0; i < allow.length; i++) {
            if (current == allow[i])
                return false;
        }
        return true;
    }

    public double for_each_sum(int[][] distance, int i) {
        //辅助函数，帮助计算每行总值，I表达第I个结点
        double sum = 0;
        for (int j = 0; j < citynum; j++) {
            sum = sum + distance[i][j];
        }
        return sum;
    }

    public double sum_path_length(int[][] distance, int[][] path, int current) {
        //辅助函数，计算蚂蚁走过路径总值
        double sum = 0;
        for (int i = 0; i < citynum - 1; i++) {
            sum = sum + distance[path[current][i]][path[current][i + 1]];
        }
        //回到原点
        sum = sum + distance[path[current][47]][path[current][0]];
        return sum;
    }

    public double sum_path_length2(int[][] distance, int[][] path, int current, DistArrange distArrange) {
        //辅助函数，计算蚂蚁走过路径总值
        double dist = Math
                .sqrt((distArrange.getFirstNode().getX() - distArrange.getArrNode().get(path[current][0]).getX()) * (distArrange.getFirstNode().getX() - distArrange.getArrNode().get(path[current][0]).getX()) + (distArrange.getFirstNode().getY() - distArrange.getArrNode().get(path[current][0]).getY())
                        * (distArrange.getFirstNode().getY() - distArrange.getArrNode().get(path[current][0]).getY()));
        double sum = dist;
        int currentDemand = (int) (distArrange.getCurrentDemand() - distArrange.getArrNode().get(path[current][0]).getDemand());
        for (int i = 0; i < citynum - 1; i++) {
            if (currentDemand >= distArrange.getArrNode().get(path[current][i + 1]).getDemand()) {
                sum = sum + distance[path[current][i]][path[current][i + 1]];
                currentDemand -= distArrange.getArrNode().get(path[current][i + 1]).getDemand();
            } else {
                currentDemand = 100;
                sum += 2 * sum_go_back_length(distArrange.getFirstNode(), distArrange.getArrNode().get(path[current][i + 1]));
                i--;
            }

        }
        return sum;
    }

    public double sum_go_back_length(Node firstNode, Node node) {
        double dist = Math.sqrt((firstNode.getX() - node.getX()) * (firstNode.getX() - node.getX()) + (firstNode.getY() - node.getY())
                        * (firstNode.getY() - node.getY()));
        return dist;
    }

    public void ACA(int cityNum, int antNum, int generation, double alpha, double beta, double rho, int q, int deltaType) throws IOException {
        FileLoader fL = new FileLoader();
        int[][] distance = fL.loadNodeInfoACA("node_chn.txt", citynum);
        ACASec b = new ACASec(cityNum, antNum, generation, alpha, beta, rho, q, deltaType);
        double p[][] = b.init_p2(distance);//产生概率
        int iter = 200;
        int i = 1;
        int[][] path = new int[ant_num][citynum];
        double min = Integer.MAX_VALUE;//保存最佳路径
        BestPath = new int[citynum];//保存最佳路径
        while (i < 100) {
            for (int j = 0; j < ant_num; j++)//令每个蚂蚁去寻找路径
            {
                int[] allow = b.start(p, distance);//产生一个初始路径
                for (int m = 0; m < citynum; m++) {//将此值保存在路径数组中用于处理
                    path[j][m] = allow[m];
                }
            }
            //释放信息素
            for (int j = 0; j < ant_num; j++) {
                int current = j;//选中第J条路径
                double L = b.sum_path_length2(distance, path, current, fL.getDistArrange());//计算每条路径长度
                if (L < min) {
                    min = L;//更新最优解
                    for (int k = 0; k < citynum; k++)//保存最短路径
                    {
                        BestPath[k] = path[current][k];
                    }
                }
                for (int k = 0; k < citynum - 1; k++) {
                    //释放信息素的值
                    p[path[j][k]][path[j][k + 1]] = p[path[j][k]][path[j][k + 1]] + 1.0 / L;
                }
                p[path[j][citynum - 1]][path[j][0]] = p[path[j][citynum - 1]][path[j][0]] + 1.0 / L;
            }
            //挥发信息素
            for (int j = 0; j < citynum; j++) {
                for (int k = 0; k < citynum; k++) {
                    p[j][k] = this.rho * p[j][k];
                }
            }
            i++;

        }
        System.out.println("最终路径：");
        for (int k = 0; k < citynum; k++) {
            System.out.print(BestPath[k] + 1 + " ");
        }
        System.out.print("最小距离：");
        System.out.println(min);
        minDist = min;
    }

    public int[] getBestTour() {
        return BestPath;
    }

    public double getBestLength() {
        return minDist;
    }

/*
    public void main2() throws IOException {
        Bbtsp a = new Bbtsp();
        int[][] distance = a.init("C:\\Users\\ThinKPad\\Desktop\\GA\\att48.txt");
        ACASec b = new ACASec();
        double p[][] = b.init_p(distance);//产生概率
        int iter = 200;
        int i = 1;
        int[][] path = new int[50][48];
        double min = Integer.MAX_VALUE;//保存最佳路径
        int best_path[] = new int[48];//保存最佳路径
        while (i < 100) {
            for (int j = 0; j < ant_num; j++)//令每个蚂蚁去寻找路径
            {
                int[] allow = b.start(p, distance);//产生一个初始路径
                for (int m = 0; m < 48; m++) {//将此值保存在路径数组中用于处理
                    path[j][m] = allow[m];
                }
            }
            //释放信息素
            for (int j = 0; j < ant_num; j++) {
                int current = j;//选中第J条路径
                double L = b.sum_path_length(distance, path, current);//计算每条路径长度
                if (L < min) {
                    min = L;//更新最优解
                    for (int k = 0; k < 48; k++)//保存最短路径
                    {
                        best_path[k] = path[current][k];
                    }

                }
                for (int k = 0; k < 47; k++) {
                    //释放信息素的值
                    p[path[j][k]][path[j][k + 1]] = p[path[j][k]][path[j][k + 1]] + 1.0 / L;
                }
                p[path[j][47]][path[j][0]] = p[path[j][47]][path[j][0]] + 1.0 / L;
            }
            //挥发信息素
            for (int j = 0; j < 48; j++) {
                for (int k = 0; k < 48; k++) {
                    p[j][k] = rho * p[j][k];
                }
            }
            i++;
        }
        System.out.println("最终路径：");
        for (int k = 0; k < 48; k++) {
            System.out.print(best_path[k] + 1 + " ");
        }
        System.out.print("最小距离：");
        System.out.println(min);

    }
*/
}
