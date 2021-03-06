/* 
 * The MIT License
 *
 * Copyright 2015 exsio.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pl.exsio.nestedj.repository;

import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;
import org.springframework.transaction.annotation.Transactional;
import pl.exsio.nestedj.FunctionalNestedjTest;
import pl.exsio.nestedj.ex.InvalidNodesHierarchyException;
import pl.exsio.nestedj.model.TestNodeImpl;
import pl.exsio.nestedj.model.Tree;

/**
 *
 * @author exsio
 */
@Transactional
public class NestedNodeRepositoryTest extends FunctionalNestedjTest {

    @Test
    public void testInitializeTree() {
        try {

            this.removeTree();
            TestNodeImpl x = this.getTestNode("x");
            this.em.persist(x);
            this.em.flush();

            assertTrue(x.getLeft() == null);
            assertTrue(x.getRight() == null);

            this.nodeRepository.rebuildTree(TestNodeImpl.class);

            assertTrue(x.getLeft() == 1);
            assertTrue(x.getRight() == 2);

        } catch (InvalidNodesHierarchyException ex) {
            fail("something went wrong:" + ex.getMessage());
        }
    }

    @Test
    public void testRebuildTree() {
        try {

            this.breakTree();
            this.nodeRepository.rebuildTree(TestNodeImpl.class);

            TestNodeImpl a = this.findNode("a");
            TestNodeImpl e = this.findNode("e");
            TestNodeImpl b = this.findNode("b");
            TestNodeImpl d = this.findNode("d");
            TestNodeImpl g = this.findNode("g");
            TestNodeImpl c = this.findNode("c");
            TestNodeImpl h = this.findNode("h");
            TestNodeImpl f = this.findNode("f");

            assertTrue(a.getLeft() == 1);
            assertTrue(a.getRight() == 8);
            assertTrue(b.getLeft() == 2);
            assertTrue(b.getRight() == 7);
            assertTrue(c.getLeft() == 9);
            assertTrue(c.getRight() == 16);
            assertTrue(d.getLeft() == 5);
            assertTrue(d.getRight() == 6);
            assertTrue(e.getLeft() == 3);
            assertTrue(e.getRight() == 4);
            assertTrue(f.getLeft() == 14);
            assertTrue(f.getRight() == 15);
            assertTrue(g.getLeft() == 10);
            assertTrue(g.getRight() == 13);
            assertTrue(h.getLeft() == 11);
            assertTrue(h.getRight() == 12);

            assertTrue(e.getLevel() == 2);
            assertTrue(f.getLevel() == 1);
            assertTrue(g.getLevel() == 1);
            assertTrue(b.getLevel() == 1);
            assertTrue(c.getLevel() == 0);
            assertTrue(h.getLevel() == 2);

        } catch (InvalidNodesHierarchyException ex) {
            fail("something went wrong:" + ex.getMessage());
        }
    }

    @Test
    public void testInsertParentToChildAsSibling() {
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl e = this.findNode("e");
        try {
            this.nodeRepository.insertAsNextSiblingOf(a, e);
            fail("this action should have triggered an exception");
        } catch (InvalidNodesHierarchyException ex) {
        }
    }

    @Test
    public void testInsertParentToChildAsChild() {
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl e = this.findNode("e");
        try {
            this.nodeRepository.insertAsLastChildOf(a, e);
            fail("this action should have triggered an exception");
        } catch (InvalidNodesHierarchyException ex) {
        }
    }

    @Test
    public void testGetParents() {
        TestNodeImpl h = this.findNode("h");
        List<TestNodeImpl> parents = (List<TestNodeImpl>) this.nodeRepository.getParents(h);
        assertTrue(parents.size() == 3);
        assertTrue(parents.get(0).getName().equals("g"));
        assertTrue(parents.get(1).getName().equals("c"));
        assertTrue(parents.get(2).getName().equals("a"));
    }

    @Test
    public void testGetTree() {
        Tree<TestNodeImpl> tree = this.nodeRepository.getTree(this.findNode("a"));
        assertTrue(tree.getNode().getName().equals("a"));
        assertTrue(tree.getChildren().get(0).getNode().getName().equals("b"));
        assertTrue(tree.getChildren().size() == 2);
        assertTrue(tree.getChildren().get(0).getChildren().size() == 2);
        assertTrue(tree.getChildren().get(1).getChildren().size() == 2);
        assertTrue(tree.getChildren().get(1).getChildren().get(1).getChildren().size() == 1);
        assertTrue(tree.getChildren().get(1).getChildren().get(0).getChildren().isEmpty());
        assertTrue(tree.getChildren().get(0).getChildren().get(0).getChildren().isEmpty());
    }

    @Test
    public void testGetTreeAsList() {
        List<TestNodeImpl> list = (List<TestNodeImpl>) this.nodeRepository.getTreeAsList(this.findNode("a"));
        assertTrue(list.size() == 8);
    }

    @Test
    public void testGetParent() {
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl parent = this.nodeRepository.getParent(b);
        assertTrue(parent instanceof TestNodeImpl);
        assertTrue(parent.getName().equals("a"));
    }

    @Test
    public void testRemoveSubtreeWithoutChildren() {

        TestNodeImpl d = this.findNode("d");
        this.nodeRepository.removeSubtree(d);
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl e = this.findNode("e");
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl h = this.findNode("h");
        TestNodeImpl f = this.findNode("f");

        assertTrue(e.getLeft() == 3);
        assertTrue(e.getRight() == 4);
        assertTrue(b.getRight() == 5);
        assertTrue(h.getLeft() == 10);
        assertTrue(h.getRight() == 11);
        assertTrue(a.getRight() == 14);
        assertTrue(c.getLeft() == 6);
        assertTrue(c.getRight() == 13);
        assertTrue(g.getLeft() == 9);
        assertTrue(g.getRight() == 12);

    }

    @Test
    public void testRemoveSubtree() {

        TestNodeImpl b = this.findNode("b");
        this.nodeRepository.removeSubtree(b);
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl h = this.findNode("h");
        TestNodeImpl f = this.findNode("f");

        assertTrue(h.getLeft() == 6);
        assertTrue(h.getRight() == 7);
        assertTrue(a.getRight() == 10);
        assertTrue(c.getLeft() == 2);
        assertTrue(c.getRight() == 9);
        assertTrue(g.getLeft() == 5);
        assertTrue(g.getRight() == 8);

    }

    @Test
    public void testRemoveSingleNodeThatHasChildren() {

        TestNodeImpl b = this.findNode("b");
        this.nodeRepository.removeSingle(b);
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl e = this.findNode("e");
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl h = this.findNode("h");
        TestNodeImpl f = this.findNode("f");

        assertTrue(d.getLeft() == 2);
        assertTrue(d.getRight() == 3);
        assertTrue(e.getLeft() == 4);
        assertTrue(e.getRight() == 5);
        assertTrue(h.getLeft() == 10);
        assertTrue(h.getRight() == 11);
        assertTrue(a.getRight() == 14);
        assertTrue(c.getLeft() == 6);
        assertTrue(c.getRight() == 13);
        assertTrue(g.getLeft() == 9);
        assertTrue(g.getRight() == 12);
        assertTrue(d.getLevel() == 1);
        assertTrue(e.getLevel() == 1);
    }

    @Test
    public void testRemoveSingleNode() {

        TestNodeImpl d = this.findNode("d");
        this.nodeRepository.removeSingle(d);
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl e = this.findNode("e");

        assertTrue(e.getLeft() == 3);
        assertTrue(e.getRight() == 4);
        assertTrue(a.getRight() == 14);
        assertTrue(c.getLeft() == 6);
        assertTrue(c.getRight() == 13);
        assertTrue(g.getLeft() == 9);
        assertTrue(g.getRight() == 12);

    }

    @Test
    public void testInsertAsNextSiblingSameNode() {
        TestNodeImpl a = this.findNode("a");
        try {
            this.nodeRepository.insertAsNextSiblingOf(a, a);
            fail("this action should have triggered an exception");
        } catch (InvalidNodesHierarchyException ex) {
        }
    }
    
    @Test
    public void testInsertAsLastChildSameNode() {
        TestNodeImpl b = this.findNode("b");
        try {
            this.nodeRepository.insertAsLastChildOf(b, b);
            fail("this action should have triggered an exception");
        } catch (InvalidNodesHierarchyException ex) {
        }
    }
    
    @Test
    public void testInsertAsPrevSiblingSameNode() {
        TestNodeImpl c = this.findNode("c");
        try {
            this.nodeRepository.insertAsPrevSiblingOf(c, c);
            fail("this action should have triggered an exception");
        } catch (InvalidNodesHierarchyException ex) {
        }
    }
    
    @Test
    public void testInsertAsFirstChildSameNode() {
        TestNodeImpl d = this.findNode("d");
        try {
            this.nodeRepository.insertAsFirstChildOf(d, d);
            fail("this action should have triggered an exception");
        } catch (InvalidNodesHierarchyException ex) {
        }
    }

    @Test
    public void testInsertAsLastChildOfDeepMove() throws InvalidNodesHierarchyException {
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl a = this.findNode("a");
        b = this.nodeRepository.insertAsLastChildOf(b, a);
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl c = this.findNode("c");

        assertTrue(c.getLeft() == 2);
        assertTrue(c.getRight() == 9);
        assertTrue(b.getLeft() == 10);
        assertTrue(b.getRight() == 15);
        assertTrue(g.getLeft() == 5);
        assertTrue(g.getRight() == 8);
        assertTrue(d.getLeft() == 11);
        assertTrue(d.getRight() == 12);
        assertTrue(b.getLevel() == 1);
        assertTrue(d.getLevel() == 2);
        assertTrue(this.getParent(b) == a);
    }

    @Test
    public void testInsertAsFirstChildOfDeepMove() throws InvalidNodesHierarchyException {
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        c = this.nodeRepository.insertAsFirstChildOf(c, a);
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl b = this.findNode("b");

        assertTrue(c.getLeft() == 2);
        assertTrue(c.getRight() == 9);
        assertTrue(b.getLeft() == 10);
        assertTrue(b.getRight() == 15);
        assertTrue(g.getLeft() == 5);
        assertTrue(g.getRight() == 8);
        assertTrue(d.getLeft() == 11);
        assertTrue(d.getRight() == 12);
        assertTrue(g.getLevel() == 2);
        assertTrue(c.getLevel() == 1);
        assertTrue(this.getParent(c) == a);
    }

    @Test
    public void testInsertAsNextSiblingOfDeepMove() throws InvalidNodesHierarchyException {
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl a = this.findNode("a");
        b = this.nodeRepository.insertAsNextSiblingOf(b, a);
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl e = this.findNode("e");

        assertTrue(b.getLeft() == 11);
        assertTrue(b.getRight() == 16);
        assertTrue(a.getLeft() == 1);
        assertTrue(a.getRight() == 10);
        assertTrue(g.getLeft() == 5);
        assertTrue(g.getRight() == 8);
        assertTrue(d.getLeft() == 12);
        assertTrue(d.getRight() == 13);
        assertTrue(b.getLevel() == 0);
        assertTrue(d.getLevel() == 1);
        assertTrue(e.getLevel() == 1);
        assertTrue(this.getParent(b) == null);
    }

    @Test
    public void testInsertAsPrevSiblingOfDeepMove() throws InvalidNodesHierarchyException {
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        c = this.nodeRepository.insertAsPrevSiblingOf(c, a);
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl f = this.findNode("f");
        TestNodeImpl h = this.findNode("h");

        assertTrue(c.getLeft() == 1);
        assertTrue(c.getRight() == 8);
        assertTrue(a.getLeft() == 9);
        assertTrue(a.getRight() == 16);
        assertTrue(g.getLeft() == 4);
        assertTrue(g.getRight() == 7);
        assertTrue(d.getLeft() == 11);
        assertTrue(d.getRight() == 12);
        assertTrue(c.getLevel() == 0);
        assertTrue(f.getLevel() == 1);
        assertTrue(g.getLevel() == 1);
        assertTrue(h.getLevel() == 2);
        assertTrue(this.getParent(c) == null);
    }

    @Test
    public void testInsertAsPrevSiblingOfMoveRight() throws InvalidNodesHierarchyException {
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl g = this.findNode("g");
        d = this.nodeRepository.insertAsPrevSiblingOf(d, g);
        TestNodeImpl f = this.findNode("f");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl e = this.findNode("e");
        TestNodeImpl h = this.findNode("h");

        assertTrue(e.getLeft() == 3);
        assertTrue(e.getRight() == 4);
        assertTrue(b.getRight() == 5);
        assertTrue(f.getLeft() == 7);
        assertTrue(f.getRight() == 8);
        assertTrue(g.getLeft() == 11);
        assertTrue(g.getRight() == 14);
        assertTrue(d.getLeft() == 9);
        assertTrue(d.getRight() == 10);
        assertTrue(h.getLeft() == 12);
        assertTrue(h.getRight() == 13);
        assertTrue(c.getRight() == 15);
        assertTrue(a.getRight() == 16);
        assertTrue(d.getLevel() == 2);

        assertTrue(this.getParent(d) == c);
    }

    @Test
    public void testInsertAsPrevSiblingOfMoveLeft() throws InvalidNodesHierarchyException {
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl e = this.findNode("e");
        g = this.nodeRepository.insertAsPrevSiblingOf(g, e);
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl h = this.findNode("h");

        assertTrue(g.getLeft() == 5);
        assertTrue(g.getRight() == 8);
        assertTrue(h.getLeft() == 6);
        assertTrue(h.getRight() == 7);
        assertTrue(e.getLeft() == 9);
        assertTrue(e.getRight() == 10);
        assertTrue(b.getRight() == 11);
        assertTrue(c.getLeft() == 12);
        assertTrue(c.getRight() == 15);
        assertTrue(a.getRight() == 16);
        assertTrue(g.getLevel() == 2);
        assertTrue(h.getLevel() == 3);
        assertTrue(this.getParent(g) == b);
    }

    @Test
    public void testInsertAsNextSiblingOfMoveRight() throws InvalidNodesHierarchyException {
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl f = this.findNode("f");
        d = this.nodeRepository.insertAsNextSiblingOf(d, f);
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl e = this.findNode("e");
        TestNodeImpl h = this.findNode("h");

        assertTrue(e.getLeft() == 3);
        assertTrue(e.getRight() == 4);
        assertTrue(b.getRight() == 5);
        assertTrue(f.getLeft() == 7);
        assertTrue(f.getRight() == 8);
        assertTrue(g.getLeft() == 11);
        assertTrue(g.getRight() == 14);
        assertTrue(d.getLeft() == 9);
        assertTrue(d.getRight() == 10);
        assertTrue(h.getLeft() == 12);
        assertTrue(h.getRight() == 13);
        assertTrue(c.getRight() == 15);
        assertTrue(a.getRight() == 16);
        assertTrue(d.getLevel() == 2);
        assertTrue(this.getParent(d) == c);
    }

    @Test
    public void testInsertAsNextSiblingOfMoveLeft() throws InvalidNodesHierarchyException {
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl d = this.findNode("d");
        g = this.nodeRepository.insertAsNextSiblingOf(g, d);
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl e = this.findNode("e");
        TestNodeImpl h = this.findNode("h");

        assertTrue(g.getLeft() == 5);
        assertTrue(g.getRight() == 8);
        assertTrue(h.getLeft() == 6);
        assertTrue(h.getRight() == 7);
        assertTrue(e.getLeft() == 9);
        assertTrue(e.getRight() == 10);
        assertTrue(b.getRight() == 11);
        assertTrue(c.getLeft() == 12);
        assertTrue(c.getRight() == 15);
        assertTrue(a.getRight() == 16);
        assertTrue(g.getLevel() == 2);
        assertTrue(h.getLevel() == 3);
        assertTrue(this.getParent(g) == b);
    }

    @Test
    public void testInsertAsLastChildOfMoveLeft() throws InvalidNodesHierarchyException {
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl b = this.findNode("b");
        g = this.nodeRepository.insertAsLastChildOf(g, b);
        TestNodeImpl f = this.findNode("f");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl h = this.findNode("h");

        assertTrue(g.getLeft() == 7);
        assertTrue(g.getRight() == 10);
        assertTrue(h.getLeft() == 8);
        assertTrue(h.getRight() == 9);
        assertTrue(b.getRight() == 11);
        assertTrue(f.getLeft() == 13);
        assertTrue(f.getRight() == 14);
        assertTrue(c.getLeft() == 12);
        assertTrue(c.getRight() == 15);
        assertTrue(a.getRight() == 16);
        assertTrue(g.getLevel() == 2);
        assertTrue(h.getLevel() == 3);
        assertTrue(this.getParent(g) == b);
    }

    @Test
    public void testInsertAsLastChildOfMoveRight() throws InvalidNodesHierarchyException {
        TestNodeImpl d = this.findNode("d");
        TestNodeImpl g = this.findNode("g");
        d = this.nodeRepository.insertAsLastChildOf(d, g);
        TestNodeImpl f = this.findNode("f");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl e = this.findNode("e");
        TestNodeImpl h = this.findNode("h");

        assertTrue(e.getLeft() == 3);
        assertTrue(e.getRight() == 4);
        assertTrue(b.getRight() == 5);
        assertTrue(f.getLeft() == 7);
        assertTrue(g.getLeft() == 9);
        assertTrue(d.getLeft() == 12);
        assertTrue(d.getRight() == 13);
        assertTrue(h.getLeft() == 10);
        assertTrue(h.getRight() == 11);
        assertTrue(c.getRight() == 15);
        assertTrue(a.getRight() == 16);
        assertTrue(d.getLevel() == 3);
        assertTrue(this.getParent(d) == g);
    }

    @Test
    public void testInsertAsFirstChildOfMoveRight() throws InvalidNodesHierarchyException {
        TestNodeImpl d = findNode("d");
        TestNodeImpl g = findNode("g");
        d = this.nodeRepository.insertAsFirstChildOf(d, g);
        TestNodeImpl f = findNode("f");
        TestNodeImpl c = findNode("c");
        TestNodeImpl a = findNode("a");
        TestNodeImpl b = findNode("b");
        TestNodeImpl e = findNode("e");
        TestNodeImpl h = findNode("h");

        assertTrue(e.getLeft() == 3);
        assertTrue(e.getRight() == 4);
        assertTrue(b.getRight() == 5);
        assertTrue(f.getLeft() == 7);
        assertTrue(g.getLeft() == 9);
        assertTrue(d.getLeft() == 10);
        assertTrue(d.getRight() == 11);
        assertTrue(h.getLeft() == 12);
        assertTrue(h.getRight() == 13);
        assertTrue(c.getRight() == 15);
        assertTrue(a.getRight() == 16);
        assertTrue(d.getLevel() == 3);
        assertTrue(this.getParent(d) == g);
    }

    @Test
    public void testInsertAsFirstChildOfMoveLeft() throws InvalidNodesHierarchyException {
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl b = this.findNode("b");
        g = this.nodeRepository.insertAsFirstChildOf(g, b);
        TestNodeImpl f = this.findNode("f");
        TestNodeImpl c = this.findNode("c");
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl h = this.findNode("h");

        assertTrue(g.getLeft() == 3);
        assertTrue(g.getRight() == 6);
        assertTrue(f.getLeft() == 13);
        assertTrue(f.getRight() == 14);
        assertTrue(c.getLeft() == 12);
        assertTrue(c.getRight() == 15);
        assertTrue(a.getRight() == 16);
        assertTrue(g.getLevel() == 2);
        assertTrue(h.getLevel() == 3);
        assertTrue(this.getParent(g) == b);
    }

    @Test
    public void testGetChildren() {

        List result = (List) this.nodeRepository.getChildren(this.findNode("a"));
        assertTrue(result.size() == 2);
    }

    @Test
    public void testInsertAsFirstChildOfInsert() throws InvalidNodesHierarchyException {

        TestNodeImpl i = this.getTestNode("i");
        TestNodeImpl e = this.findNode("e");
        i = this.nodeRepository.insertAsFirstChildOf(i, e);
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl b = this.findNode("b");
        TestNodeImpl h = this.findNode("h");

        assertTrue(i.getLeft() == 6);
        assertTrue(i.getRight() == 7);
        assertTrue(a.getRight() == 18);
        assertTrue(b.getRight() == 9);
        assertTrue(h.getLeft() == 14);
        assertTrue(h.getRight() == 15);
        assertTrue(i.getLevel().equals(e.getLevel() + 1));
    }

    @Test
    public void testInsertAsLastChildOfInsert() throws InvalidNodesHierarchyException {

        TestNodeImpl j = this.getTestNode("j");
        TestNodeImpl b = this.findNode("b");
        j = this.nodeRepository.insertAsLastChildOf(j, b);
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl h = this.findNode("h");
        TestNodeImpl c = this.findNode("c");

        assertTrue(j.getLeft() == 7);
        assertTrue(j.getRight() == 8);
        assertTrue(a.getRight() == 18);
        assertTrue(h.getLeft() == 14);
        assertTrue(h.getRight() == 15);
        assertTrue(c.getLeft() == 10);
        assertTrue(j.getLevel().equals(b.getLevel() + 1));
    }

    @Test
    public void testInsertAsPrevSiblingOfInsert() throws InvalidNodesHierarchyException {

        TestNodeImpl k = this.getTestNode("k");
        TestNodeImpl e = this.findNode("e");
        k = this.nodeRepository.insertAsPrevSiblingOf(k, e);
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl h = this.findNode("h");
        TestNodeImpl c = this.findNode("c");

        assertTrue(k.getLeft() == 5);
        assertTrue(k.getRight() == 6);
        assertTrue(a.getRight() == 18);
        assertTrue(h.getLeft() == 14);
        assertTrue(h.getRight() == 15);
        assertTrue(c.getLeft() == 10);
        assertTrue(k.getLevel().equals(e.getLevel()));
        assertTrue(k.getParent().equals(e.getParent()));
    }

    @Test
    public void testInsertAsNextSiblingOfInsert() throws InvalidNodesHierarchyException {

        TestNodeImpl m = this.getTestNode("m");
        TestNodeImpl h = this.findNode("h");
        m = this.nodeRepository.insertAsNextSiblingOf(m, h);
        TestNodeImpl a = this.findNode("a");
        TestNodeImpl g = this.findNode("g");
        TestNodeImpl c = this.findNode("c");

        assertTrue(m.getLeft() == 14);
        assertTrue(m.getRight() == 15);
        assertTrue(a.getRight() == 18);
        assertTrue(g.getRight() == 16);
        assertTrue(c.getRight() == 17);
        assertTrue(m.getLevel().equals(h.getLevel()));
        assertTrue(m.getParent().equals(h.getParent()));
    }

    private TestNodeImpl getTestNode(String symbol) {

        TestNodeImpl n = new TestNodeImpl();
        n.setName(symbol);
        return n;
    }

    private TestNodeImpl getParent(TestNodeImpl f) {
        this.em.refresh(f);
        TestNodeImpl parent = this.nodeRepository.getParent(f);
        if (parent instanceof TestNodeImpl) {
            this.em.refresh(parent);
        }
        return parent;
    }

    private void breakTree() {

        this.em.createQuery("update TestNodeImpl set parent = null where id = 3").executeUpdate();
        this.em.createQuery("update TestNodeImpl set lft = 0, rgt = 0, lvl = 0").executeUpdate();

    }

    private void removeTree() {
        this.em.createQuery("delete from TestNodeImpl").executeUpdate();
    }

}
