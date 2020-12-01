package api;


import java.util.Objects;

/**
 * This class represent a vertex in the directional weighted graph
 */

public class NodeData implements  node_data{
    int _key=0;
    geo_location _gl=null;
    double _weight;
    int _tag;
    String _info="white";
    private static int _id=1;


    /**
     * This class implements geo_location that represent point3D (the geographic location of the node)
     */
    //----------GeoLocation-------------//
    public static class GeoLocation implements geo_location{
        private double _x;
        private double _y;
        private double _z;


        //-----constructor-----//
        public GeoLocation(double x,double y,double z){
            this._x=x;
            this._y=y;
            this._z=z;
        }
        public GeoLocation(GeoLocation gl){
            this._x=gl._x;
            this._y=gl._y;
            this._z=gl._z;
        }
        public GeoLocation(String x,String y,String z){
        this._x=Double.parseDouble(x);
        this._y=Double.parseDouble(y);
        this._z=Double.parseDouble(z);
        }
        //-----------getters---------------//
        @Override
        public double x() {
            return this._x;
        }

        @Override
        public double y() {
            return this._y;
        }

        @Override
        public double z() {
            return this._z;
        }

        @Override
        public double distance(geo_location g) {
            double dx,dy,dz,ans;
            dx=Math.pow(this._x-g.x(),2);
            dy=Math.pow(this._y-g.y(),2);
            dz=Math.pow(this._z-g.z(),2);
            ans=dx+dy+dz;
            return Math.sqrt(ans);
        }

        @Override
        public String toString() {
            return "GeoLocation{" +
                    "_x=" + _x +
                    ", _y=" + _y +
                    ", _z=" + _z +
                    '}';
        }
    }

    //----------------NodeInfo_methods--------------//

    //---------Constructors--------//

    public NodeData(int key){
        this._key=key;
        _gl=null;
    }
    public NodeData(int key,double weight,int tag,String info){
        this._key=key;
        this._info=info;
        this._weight=weight;
        this._tag=tag;
    }
//---------setters and getters-----------------//

    /**
     * return the key associate with this node
     * @return key
     */
    @Override
    public int getKey() {
        return this._key;
    }

    /**
     * return the geographic location of this node
     * @return node location
     */
    @Override
    public geo_location getLocation() {
        return this._gl;
    }

    /**
     * allow to set a new location to this node
     * @param p - new new location  (position) of this node.
     */
    @Override
    public void setLocation(geo_location p) {
        this._gl=(GeoLocation)p;
    }

    /**
     * return the weight of this node
     * @return weight
     */
    @Override
    public double getWeight() {
        return this._weight;
    }

    /**
     * allow to set a new weight to this node
     * Note:the weight must be positive
     * @param w - the new weight
     */
    @Override
    public void setWeight(double w) {
        if(w>0){
        this._weight=w;}
    }

    /**
     * return the info of this node
     * @return node info
     */
    @Override
    public String getInfo() {
        return this._info;
    }

    /**
     * allow to set a new info to this node
     * @param s-the new info
     */
    @Override
    public void setInfo(String s) {
        this._info=s;
    }

    /**
     * return the tag of this node
     * @return tag
     */
    @Override
    public int getTag() {
        return this._tag;
    }

    /**
     * allow to set a new tag to this node
     * @param t - the new value of the tag
     */
    @Override
    public void setTag(int t) {
        this._tag=t;
    }

    /**
     * allow to set a new key to this node
     * @param key
     */
    public void setKey(int key){
        _key=key;
    }

    //------------equals,toString--------------//
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NodeData)) return false;
        NodeData nodeData = (NodeData) o;
        return _key == nodeData._key &&
                Double.compare(nodeData._weight, _weight) == 0 &&
                _tag == nodeData._tag &&
                Objects.equals(_info, nodeData._info);
    }

    @Override
    public String toString() {
        return "NodeData{" +
                "_key=" + _key +
                ", _gl=" + _gl +
                ", _weight=" + _weight +
                ", _tag=" + _tag +
                ", _info='" + _info + '\'' +
                '}';
    }
}