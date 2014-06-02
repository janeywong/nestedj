package pl.exsio.nestedj.config;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author exsio
 */
public class DefaultNestedNodeConfigTest {
    
    public DefaultNestedNodeConfigTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getEntityName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testGetEntityName() {
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        String expResult = null;
        String result = instance.getEntityName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setEntityName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testSetEntityName() {
        String entityName = null;
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        instance.setEntityName(entityName);
    }

    /**
     * Test of getRightFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testGetRightFieldName() {
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        String expResult = null;
        String result = instance.getRightFieldName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRightFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testSetRightFieldName() {
        String rightFieldName = null;
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        instance.setRightFieldName(rightFieldName);
    }
    
    /**
     * Test of setIdFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testSetIdFieldName() {
        String idFieldName = null;
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        instance.setIdFieldName(idFieldName);
    }
    
    /**
     * Test of getIdFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testGetIdFieldName() {
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        String expResult = null;
        String result = instance.getIdFieldName();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLeftFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testGetLeftFieldName() {
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        String expResult = null;
        String result = instance.getLeftFieldName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLeftFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testSetLeftFieldName() {
        String leftFieldName = null;
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        instance.setLeftFieldName(leftFieldName);
    }

    /**
     * Test of getLevelFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testGetLevelFieldName() {
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        String expResult = null;
        String result = instance.getLevelFieldName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setLevelFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testSetLevelFieldName() {
        String levelFieldName = null;
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        instance.setLevelFieldName(levelFieldName);
    }

    /**
     * Test of getParentFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testGetParentFieldName() {
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        String expResult = null;
        String result = instance.getParentFieldName();
        assertEquals(expResult, result);
    }

    /**
     * Test of setParentFieldName method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testSetParentFieldName() {
        String parentFieldName = null;
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        instance.setParentFieldName(parentFieldName);
    }

    /**
     * Test of toString method, of class NestedNodeConfigImpl.
     */
    @Test
    public void testToString() {
        NestedNodeConfigImpl instance = new NestedNodeConfigImpl();
        String expResult = "[leftFieldName: null, rightFieldName:null, levelFieldName: null, parentFieldName:null]";
        String result = instance.toString();
        assertEquals(expResult, result);
    }
    
}
