package work.icql.java.datastructure.graph;

import java.util.LinkedList;
import java.util.Queue;

/**
 * ϡ��ͼ
 * �ڽӱ�ʵ��
 */
public class SparseGraph {

    /**
     * ������
     */
    private int vertexNum;

    /**
     * ����
     */
    private int edgeNum;

    /**
     * �Ƿ�Ϊ����ͼ
     */
    private boolean isDirected;

    /**
     * ͼ�ľ�������
     */
    private LinkedList<Integer>[] data;

    public SparseGraph(int vertexNum, boolean directed) {
        assert vertexNum >= 0;
        this.vertexNum = vertexNum;
        //��ʼ��û���κα�
        this.edgeNum = 0;
        this.isDirected = directed;
        //data ��ʼ��Ϊ vertexNum ���յ� ArrayList
        //��ʾÿһ��data[i]��Ϊ��, ��û���κͱ�
        data = new LinkedList[vertexNum];
        for (int i = 0; i < vertexNum; i++) {
            data[i] = new LinkedList<>();
        }
    }

    /**
     * ���ؽ�����
     *
     * @return
     */
    public int V() {
        return vertexNum;
    }

    /**
     * ���رߵĸ���
     *
     * @return
     */
    public int E() {
        return edgeNum;
    }

    /**
     * ��ͼ�����һ����
     *
     * @param i
     * @param j
     */
    public void addEdge(int i, int j) {
        assert i >= 0 && i < vertexNum;
        assert j >= 0 && j < vertexNum;
        data[i].add(j);
        if (i != j && !isDirected) {
            data[j].add(i);
        }
        edgeNum++;
    }

    /**
     * i��j�Ƿ��б�
     *
     * @param i
     * @param j
     * @return
     */
    boolean hasEdge(int i, int j) {
        assert i >= 0 && i < vertexNum;
        assert j >= 0 && j < vertexNum;
        for (int k = 0; k < data[k].size(); k++) {
            if (data[k].get(k) == j) {
                return true;
            }
        }
        return false;
    }

    /**
     * ������ȱ���
     *
     * @param start
     * @param end
     */
    public void bfs(int start, int end) {
        if (start == end) {
            return;
        }
        //��¼�Ѿ����ʹ��Ķ���
        boolean[] visited = new boolean[vertexNum];
        visited[start] = true;
        //�����洢�Ѿ������ʡ��������Ķ��㻹û�б����ʵĶ���
        Queue<Integer> queue = new LinkedList<>();
        queue.add(start);
        //������¼����·��
        int[] prev = new int[vertexNum];
        for (int i = 0; i < vertexNum; i++) {
            prev[i] = -1;
        }
        while (queue.size() != 0) {
            int curVertex = queue.poll();
            //���ʶ��������Ķ���
            for (int i = 0; i < data[curVertex].size(); ++i) {
                int linkedVertex = data[curVertex].get(i);
                if (!visited[linkedVertex]) {
                    prev[linkedVertex] = curVertex;
                    if (linkedVertex == end) {
                        print(prev, start, end);
                        return;
                    }
                    visited[linkedVertex] = true;
                    queue.add(linkedVertex);
                }
            }
        }
    }

    /**
     * �ݹ��ӡ s->t ��·��
     *
     * @param prev
     * @param s
     * @param t
     */
    private void print(int[] prev, int s, int t) {
        if (prev[t] != -1 && t != s) {
            print(prev, s, prev[t]);
        }
        System.out.print(t + " ");
    }


    public void dfs(int s, int t) {
        found = false;
        boolean[] visited = new boolean[vertexNum];
        int[] prev = new int[vertexNum];
        for (int i = 0; i < vertexNum; ++i) {
            prev[i] = -1;
        }
        recurDfs(s, t, visited, prev);
        print(prev, s, t);
    }

    boolean found = false;

    private void recurDfs(int w, int t, boolean[] visited, int[] prev) {
        if (found) {
            return;
        }
        visited[w] = true;
        if (w == t) {
            found = true;
            return;
        }
        for (int i = 0; i < data[w].size(); ++i) {
            int q = data[w].get(i);
            if (!visited[q]) {
                prev[q] = w;
                recurDfs(q, t, visited, prev);
            }
        }
    }

    public static void main(String[] args) {
        SparseGraph graph = new SparseGraph(5, false);
        graph.addEdge(0, 1);
        graph.addEdge(1, 2);
        graph.addEdge(3, 4);

        graph.bfs(0, 4);
        graph.dfs(0, 4);
    }
}
