import static org.junit.jupiter.api.Assertions.*;
import ex2.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


/**
 * This is a test class for NodeData, each test in this class examines edge cases
 */
class NodeDataTest {

    /**
     * getKeyTest- verify that this method return the correct key
     */

   @Test
   @BeforeAll
    static void getKeyTest(){
        node_data n1=new NodeData(1);
        Assertions.assertEquals(1,n1.getKey());//the first key should be 1
        node_data n2=new NodeData(2);
        Assertions.assertEquals(2,n2.getKey());

    }
    @Test
    void getAndSetWeightTest(){
       node_data n1=new NodeData(1);
       n1.setWeight(2.5);
       Assertions.assertEquals(2.5,n1.getWeight());
       n1.setWeight(2);
       Assertions.assertEquals(2,n1.getWeight());
       n1.setWeight(-1);
       Assertions.assertEquals(2,n1.getWeight());//negative weight does not allowed

    }
    @Test
    void getAndSetInfoTest(){
       node_data n1=new NodeData();
       n1.setInfo("hello");
        assertEquals("hello", n1.getInfo());
        assertNotEquals("  ",n1.getInfo());
    }
    @Test
    void getAndSetTag(){
       node_data n1=new NodeData(1);
       n1.setTag(2);
       Assertions.assertEquals(2,n1.getTag());
       n1.setTag(3);
       Assertions.assertEquals(3,n1.getTag());
    }
}