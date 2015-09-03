/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import gameObjects.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import math.Ray;
import utils.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class QuadTree<T extends GameObject> {

    int maxHeight;
    int capacity;
    Node root;
    private HashMap<GameObject, Point2D.Double> cachedObjects;
    private int numberOfObjectsChecked;
    
    private class Node<T extends GameObject> {

        private AABB aabb;
        private Node[] children;
        private int height;

        private ArrayList<T> objects;
        

        public Node(AABB aabb, int height) {
            this.aabb = new AABB(aabb);
            children = null;
            objects = new ArrayList<>();
            this.height = height;
           
            
        }

        public void insert(T object) {

            if (!aabb.intersect(object.getAabb())) {
                return;
            }
            if (capacity > objects.size() && children == null) {
                objects.add(object);
                return;
            }

            if (height >= maxHeight) {
                objects.add(object);
                return;
            } else {
                if (children == null) {
                    subDivide();

                }

                for (int i = 0; i < 4; i++) {
                    children[i].insert(object);
                }
            }
        }

        public void subDivide() {
            children = new Node[4];
            double minX = aabb.getMinX();
            double minY = aabb.getMinY();
            double halfWidth = (aabb.getMaxX() - minX) / 2;
            double halfHeight = (aabb.getMaxY() - minY) / 2;

            children[0] = new Node(new AABB(minX, minY, minX + halfWidth, minY + halfHeight), height + 1);
            children[1] = new Node(new AABB(minX + halfWidth, minY, minX + 2 * halfWidth, minY + halfHeight), height + 1);
            children[2] = new Node(new AABB(minX + halfWidth, minY + halfHeight, minX + 2 * halfWidth, minY + 2 * halfHeight), height + 1);
            children[3] = new Node(new AABB(minX, minY + halfHeight, minX + halfWidth, minY + 2 * halfHeight), height + 1);

            reinsertObjects();

        }

        public void reinsertObjects() {
            for (int i = 0; i < objects.size(); i++) {
                insert(objects.get(i));
            }
            objects = new ArrayList<>();
        }

        public void draw(Graphics2D g2d, int offSetX, int offSetY) {
            Polygon p = new Polygon();
            p.addPoint((int) aabb.getMinX() + offSetX, (int) aabb.getMinY() + offSetY);
            p.addPoint((int) (aabb.getMaxX()) + offSetX, (int) aabb.getMinY() + offSetY);
            p.addPoint((int) (aabb.getMaxX()) + offSetX, (int) (aabb.getMaxY()) + offSetY);
            p.addPoint((int) aabb.getMinX() + offSetX, (int) (aabb.getMaxY()) + offSetY);

            g2d.setColor(Color.BLACK);
            if (children == null) {

                g2d.drawPolygon(p);
                return;
            }
            children[0].draw(g2d, offSetX, offSetY);
            children[1].draw(g2d, offSetX, offSetY);
            children[2].draw(g2d, offSetX, offSetY);
            children[3].draw(g2d, offSetX, offSetY);
            g2d.drawPolygon(p);

        }

        public void print() {
            System.out.println(aabb);
            System.out.println("NUMBER OF OBJECTS IN THE SECTOR " + aabb + " - " + objects.size());
            for (int i = 0; i < objects.size(); i++) {

                System.out.println(objects.get(i).getAabb());

            }

            if (children == null) {
                return;
            }
            children[0].print();
            children[1].print();
            children[2].print();
            children[3].print();
        }

        public Point2D.Double intersect(Ray r) {
            //System.out.println("checking aabb:"+aabb);
            if (children != null) {
                Arrays.sort(children, new AABBComparator(r.source));
                for (int i = 0; i < 4; i++) {
                    if (r.intersect(children[i].aabb)) {
                        Point2D.Double intersection = children[i].intersect(r);
                        if (intersection != null) {
                            return intersection;
                        }
                        
                    }
                }

            } else {

                return getClosestIntersection(r);
                
            }
            return null;

        }

        public void resetCachedObjects() {
            cachedObjects = new HashMap<>();
        }

        public Point2D.Double getClosestIntersection(Ray r) {
            
            Point2D.Double intersection = null;
            double currentDistance = 100000;
            
            for (GameObject obj : objects) {
                if (cachedObjects.get(obj) != null) {
                    Point2D.Double tmpPoint = cachedObjects.get(obj);
                    if (GeometryUtil.getDistance(tmpPoint, r.source) < currentDistance && aabb.contains(tmpPoint)) {
                        intersection = tmpPoint;
                        currentDistance = GeometryUtil.getDistance(tmpPoint, r.source);
                    }

                } else if (r.intersect(obj.getAabb())) {
                    numberOfObjectsChecked++;
                    for (Line2D.Double line : obj.getLines()) {
                        Point2D.Double tmpPoint = GeometryUtil.getIntersectionRayLine(r, line);
                        if(tmpPoint!=null && !aabb.contains(tmpPoint)){
                            if(cachedObjects.get(obj)!=null){
                                if(GeometryUtil.getDistance(cachedObjects.get(obj),r.source)>GeometryUtil.getDistance(tmpPoint,r.source)){
                                    cachedObjects.put(obj, tmpPoint);
                                }    
                            }
                            else{
                                
                                cachedObjects.put(obj, tmpPoint);
                            }
                            
                            //System.out.println("CACHING obj: "+obj.getAabb()+"with point: "+checkedObjects.get(obj));
                        }
                        else if (tmpPoint != null && currentDistance > GeometryUtil.getDistance(tmpPoint, r.source)) {
                            currentDistance = GeometryUtil.getDistance(tmpPoint, r.source);
                            intersection = tmpPoint;
                        }
                    }
                }
            }
            
            return intersection;

        }

        
        public void checkCollision(Physics physics){
            if(children!=null){
                for(int i=0;i<4;i++){
                    children[i].checkCollision(physics);
                }
            }
            else{
                for(int i=0;i<objects.size();i++){
                    for(int j=0;j<objects.size();j++){
                        physics.checkCollision(objects.get(i), objects.get(j));
                    }
                }
            }
        }
        
        public class AABBComparator implements Comparator {

            Point2D.Double origin;

            public AABBComparator(Point2D.Double _origin) {
                origin = _origin;
            }

            @Override
            public int compare(Object o1, Object o2) {
                Node n1 = (Node) o1;
                Node n2 = (Node) o2;
                double dist = GeometryUtil.getDistance(origin, n1.aabb.getCenter()) - GeometryUtil.getDistance(origin, n2.aabb.getCenter());
                if (dist > 0) {
                    return 1;
                } else if (dist < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }

        }

    }
    
    public void checkCollision(Physics physics){
        root.checkCollision(physics);
    }
    
    public QuadTree(int capacity, int maxHeight, AABB aabb) {
        this.capacity = capacity;
        root = new Node(aabb, 0);
        this.maxHeight = maxHeight;
    }

    public Point2D.Double intersect(Ray r) {
        cachedObjects = new HashMap<>();
        numberOfObjectsChecked = 0;
        Point2D.Double result = root.intersect(r);
        //System.out.println("NumberOfObjectsChecked: "+numberOfObjectsChecked);
        return result;
    }

    public void insert(T object) {
        root.insert(object);
    }

    public void drawTree(Graphics2D g2d, int offSetX, int offSetY) {
        root.draw(g2d, offSetX, offSetY);
    }

    public void printTree() {
        root.print();
    }

}
