package api;

/**
 * This class represent an edge between two nodes in directed weighted graph
 */
public class EdgeData implements edge_data{

    private int _src;
    private int _dest;
    private double _weight;
    private String _info;
    private int _tag;


    //-----------------------EdgeData_methods-----------------------//
    //-------------constructor----------------//
    public EdgeData(int src,int dest,double weight){
        this._src=src;
        this._dest=dest;
        this._weight=weight;
    }
    /**
     * This method return the key of the source node of this edge
     * @return int key
     */
    @Override
    public int getSrc() {
        return this._src;
    }

    /**
     * This method return the key of the destination node of this edge
     * @return int key
     */
    @Override
    public int getDest() {
        return this._dest;
    }

    /**
     * This method return the weight of this edge
     * @return double weight
     */
    @Override
    public double getWeight() {
        return this._weight;
    }

    /**
     * This method return the info (String) of this edge
     * @return String info
     */
    @Override
    public String getInfo() {
        return this._info;
    }

    /**
     * This method allows to set an info in this edge
     * @param s
     */
    @Override
    public void setInfo(String s) {
        this._info=s;

    }

    /**
     * This method return the tag of this edge
     * @return int tag
     */
    @Override
    public int getTag() {
        return this._tag;
    }

    /**
     * This method allows to set a new value to this edge
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this._tag=t;
    }
    //---------equals----------//

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EdgeData)) return false;
        EdgeData edgeData = (EdgeData) o;
        return _src == edgeData._src &&
                _dest == edgeData._dest &&
                Double.compare(edgeData._weight, _weight) == 0;
    }

    @Override
    public String toString() {
        return "EdgeData{" +
                "_src=" + _src +
                ", _dest=" + _dest +
                ", _weight=" + _weight +
                ", _info='" + _info + '\'' +
                ", _tag=" + _tag +
                '}';
    }
}
