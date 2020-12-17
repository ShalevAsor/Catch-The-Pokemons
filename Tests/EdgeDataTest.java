
import static org.junit.jupiter.api.Assertions.*;
import api.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * This is a simple test class for EdgeData,each test in this class examines edge cases
 */
public class EdgeDataTest {

    /**
     * Verify that getSrc and getDest return the correct data
     */
    @Test
    void getSrcAndDestTest(){
        edge_data e1=new EdgeData(1,2,1);//src=1,dest=2,weight=1
        Assertions.assertEquals(1,e1.getSrc());
        Assertions.assertEquals(2,e1.getDest());
        edge_data e2=new EdgeData(3,4,0.5);//src=3,dest=4,weight=0.5
        Assertions.assertEquals(3, e2.getSrc());
        Assertions.assertEquals(4,e2.getDest());
    }
    /**
     * Verify that getWeight return the correct weight of the edge
     */
    @Test
    void getWeight(){
        edge_data e3=new EdgeData(1,2,1);//weight =1
        Assertions.assertEquals(1,e3.getWeight());
        edge_data e4=new EdgeData(1,1,2);
        Assertions.assertEquals(1,1,2);
        Assertions.assertEquals(2,e4.getWeight());
    }

    /**
     * Verify that getInfo return the correct info(String) and setInfo allows to set new info for the edge
     */
    @Test
    void getInfoSetInfoTest(){
        edge_data e5=new EdgeData(1,2,1);
        Assertions.assertNull(e5.getInfo());
        e5.setInfo("test");
        Assertions.assertEquals("test",e5.getInfo());
        e5.setInfo("");
        Assertions.assertEquals("",e5.getInfo());
    }
    /**
     * Verify that getTag return the correct tag(int) and setTag allows to change the tag for the edge
     */
    @Test
    void setTagGetTagTest(){
        edge_data e6=new EdgeData(1,2,1);
        Assertions.assertEquals(0,e6.getTag());
        e6.setTag(2);
        Assertions.assertEquals(2,e6.getTag());
    }

}
