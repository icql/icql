package work.icql.java.datastructure.graph;

/**
 * ����ͼ
 * �ڽӾ���ʵ��
 */
public class DenseGraph {

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
    private boolean[][] data;

    /**
     * ���캯��
     *
     * @param vertexNum
     * @param isDirected
     */
    public DenseGraph(int vertexNum, boolean isDirected) {
        assert vertexNum >= 0;
        this.vertexNum = vertexNum;
        //��ʼ��û���κα�
        this.edgeNum = 0;
        this.isDirected = isDirected;
        //data ��ʼ��Ϊ vertexNum*vertexNum �Ĳ�������
        //ÿһ��data[i][j]��Ϊfalse, ��ʾû���κα�
        data = new boolean[vertexNum][vertexNum];
    }

    /**
     * ���ض������
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
        if (hasEdge(i, j)) {
            return;
        }
        data[i][j] = true;
        if (!isDirected) {
            data[j][i] = true;
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
    public boolean hasEdge(int i, int j) {
        assert i >= 0 && i < vertexNum;
        assert j >= 0 && j < vertexNum;
        return data[i][j];
    }
}
