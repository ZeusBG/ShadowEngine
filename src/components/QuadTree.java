/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package components;

import gameObjects.GameObject;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import math.Line2f;
import math.Ray;
import math.Vector2f;
import engine.render.Renderer;
import math.GeometryUtil;

/**
 *
 * @author Zeus
 */
public class QuadTree<T extends GameObject> {

    int maxHeight;
    int capacity;
    Node root;
    private HashMap<GameObject, Vector2f> cachedObjects;
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
            if(children==null && objects.contains(object))
                return;
            if (capacity > objects.size() && children == null ) {
                objects.add(object);
                return;
            }

            if (height >= maxHeight ) {
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

        public void insertObject(Line2f l,T object){
            
            if (!GeometryUtil.checkIntersectionLineAABB(l, aabb) ) {
                return;
            }
            if(children==null && objects.contains(object))
                return;

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
                    children[i].insertObject(l,object);
                }
            }
        }
        
        public void subDivide() {
            children = new Node[4];
            float minX = aabb.getMinX();
            float minY = aabb.getMinY();
            float halfWidth = (aabb.getMaxX() - minX) / 2.0f;
            float halfHeight = (aabb.getMaxY() - minY) / 2.0f;

            children[0] = new Node(new AABB(minX, minY, minX + halfWidth, minY + halfHeight), height + 1);
            children[1] = new Node(new AABB(minX + halfWidth, minY, aabb.getMaxX(), minY + halfHeight), height + 1);
            children[2] = new Node(new AABB(minX + halfWidth, minY + halfHeight, aabb.getMaxX(), aabb.getMaxY()), height + 1);
            children[3] = new Node(new AABB(minX, minY + halfHeight, minX + halfWidth, aabb.getMaxY()), height + 1);

            reinsertObjects();

        }

        public void reinsertObjects() {
            for (int i = 0; i < objects.size(); i++) {
                if(objects.get(i).getLines().isEmpty()){
                    insert(objects.get(i));
                }
                else
                {
                    for(Line2f l : objects.get(i).getLines())
                        insertObject(l,objects.get(i));
                }
            }
            objects = new ArrayList<>();
        }

        public void draw(Renderer r) {
            
            if (children == null) {
                
                r.drawRect((float)aabb.getMinX(), (float)aabb.getMinY(), (float)(aabb.getMaxX()-aabb.getMinX()), (float)(aabb.getMaxY()-aabb.getMinY()));
                return;
            }
            for(int i=0;i<4;i++){
                children[i].draw(r);
            }
            r.drawRect((float)aabb.getMinX(), (float)aabb.getMinY(), (float)(aabb.getMaxX()-aabb.getMinX()), (float)(aabb.getMaxY()-aabb.getMinY()));

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

        public Vector2f intersect(Ray r,HashSet<GameObject> collidableObjects) {
            //System.out.println("checking aabb:"+aabb);
            if (children != null) {
                Arrays.sort(children, new AABBComparator(r.source));
                for (int i = 0; i < 4; i++) {
                    if (r.intersect(children[i].aabb)) {
                        Vector2f intersection = children[i].intersect(r,collidableObjects);
                        if (intersection != null) {
                            return intersection;
                        }
                        
                    }
                }

            } else {

                return getClosestIntersection(r,collidableObjects);
                
            }
            return null;

        }

        public void resetCachedObjects() {
            cachedObjects = new HashMap<>();
        }

        public Vector2f getClosestIntersection(Ray r,HashSet<GameObject> collidableObjects) {
            
            Vector2f intersection = null;
            float currentDistance = 100000;
            
            for (GameObject obj : objects) {
                
                if(!obj.isRenderable() || (collidableObjects != null && !collidableObjects.contains(obj)))
                    continue;
                
                
                if (cachedObjects.get(obj) != null) {
                    Vector2f tmpPoint = cachedObjects.get(obj);
                    if (GeometryUtil.getDistance(tmpPoint, r.source) < currentDistance && aabb.contains(tmpPoint)) {
                        intersection = tmpPoint;
                        currentDistance = GeometryUtil.getDistance(tmpPoint, r.source);
                    }

                } else if (r.intersect(obj.getAabb())) {
                    numberOfObjectsChecked++;
                    for (Line2f line : obj.getLines()) {
                        Vector2f tmpPoint = GeometryUtil.getIntersectionRayLine(r, line);
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

        public HashSet<GameObject> getObjectsInRange(AABB aabb){
            HashSet<GameObject> objectsInRange = new HashSet<>();
            if(children!=null){
                for(int i=0;i<4;i++){
                    if(aabb.intersect(children[i].aabb)){
                        objectsInRange.addAll(children[i].getObjectsInRange(aabb));
                    }
                }
            }
            else{
                for(GameObject obj: objects){
                    if(obj.getAabb().intersect(aabb))
                        objectsInRange.add(obj);
                }
            }
            return objectsInRange;
            
        }
        
        public HashSet<GameObject> getRenderableObjectsInRange(AABB aabb){
            HashSet<GameObject> objectsInRange = new HashSet<>();
            if(children!=null){
                for(int i=0;i<4;i++){
                    if(aabb.intersect(children[i].aabb)){
                        objectsInRange.addAll(children[i].getObjectsInRange(aabb));
                    }
                }
            }
            else{
                for(int i=0;i<objects.size();++i){
                    if(objects.get(i).isRenderable())
                        objectsInRange.add(objects.get(i));
                }
            }
            return objectsInRange;
            
        }
        
        
        public HashSet<GameObject> getObjectLineIntersect(Line2f line){
            HashSet<GameObject> objectsLineIntersect = new HashSet<>();
            if(children!=null){
                for(int i=0;i<4;i++){
                    if(GeometryUtil.checkIntersectionLineAABB(line, children[i].aabb)){
                        objectsLineIntersect.addAll(children[i].getObjectLineIntersect(line));
                    }
                }
            }
            else{
                for(GameObject go : objects){
                    if(GeometryUtil.checkIntersectionLineAABB(line, go.getAabb())){
                        objectsLineIntersect.add(go);
                    }
                }
                
            }
            return objectsLineIntersect;
            
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
                        if(i>=objects.size() || j>=objects.size())
                            continue;
                        physics.checkCollision(objects.get(i), objects.get(j));
                    }
                }
            }
        }
        
        public void removeObject(GameObject object){
            if(!aabb.intersect(object.getAabb()))
                return;
            
            if(children!=null){
                for(int i=0;i<4;i++){
                    children[i].removeObject(object);
                }
            }
            else{
                objects.remove(object);
            }
        }
        
        public class AABBComparator implements Comparator {

            Vector2f origin;

            public AABBComparator(Vector2f _origin) {
                origin = _origin;
            }

            @Override
            public int compare(Object o1, Object o2) {
                Node n1 = (Node) o1;
                Node n2 = (Node) o2;
                float dist = GeometryUtil.getDistance(origin, n1.aabb.getCenter()) - GeometryUtil.getDistance(origin, n2.aabb.getCenter());
                if (dist > 0) {
                    return 1;
                } else if (dist < 0) {
                    return -1;
                } else {
                    return 0;
                }
            }

        }
        
        public int getNumOfObjects(){
            
            if(children == null){
                return objects.size();
            }
            else{
                return children[0].getNumOfObjects()+
                        children[1].getNumOfObjects()+
                        children[2].getNumOfObjects()+
                        children[3].getNumOfObjects();
            }
        }
        
        public int getNumOfONodes(){
            if(children == null){
                return 1;
            }
            else{
                return 1+children[0].getNumOfObjects()+
                        children[1].getNumOfObjects()+
                        children[2].getNumOfObjects()+
                        children[3].getNumOfObjects();
            }
        }
        public void clean(){
            if(children==null){
                objects = null;
                return;
                
            }
            for(int i=0;i<4;i++){
                children[i].clean();
            }
            children = null;
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

    public Vector2f intersect(Ray r,HashSet<GameObject> collidableObjects) {
        cachedObjects = new HashMap<>();
        numberOfObjectsChecked = 0;
        Vector2f result = root.intersect(r,collidableObjects);
        //System.out.println("NumberOfObjectsChecked: "+numberOfObjectsChecked);
        return result;
    }

    public void insert(T object) {
        if(object.getLines().isEmpty()){
            root.insert(object);
            return;
        }
        
        
        for(int i=0;i<object.getLines().size();i++){
            root.insertObject(object.getLines().get(i),object);
        }
        //root.insert(object);
    }

    public void drawTree(Renderer r) {
        r.setColor(Color.white);
        root.draw(r);
    }

    public void printTree() {
        root.print();
    }
    
    public HashSet<GameObject> getObjectsInRange(AABB aabb){
        HashSet<GameObject> objects = root.getObjectsInRange(aabb);
        return objects;
    }
    
    public HashSet<GameObject> getObjectsLineIntersect(Line2f line){
        return root.getObjectLineIntersect(line);
    }
    
    public HashSet<GameObject> getObjectsInRange(int x,int y,int width,int height){
        return root.getObjectsInRange(new AABB(x,y,x+width,y+height));
    }
    
    public void remove(GameObject object){
        root.removeObject(object);
    }
    
    public HashSet<GameObject> getRenderableObjectsInRange(AABB aabb){
        return root.getRenderableObjectsInRange(aabb);
    }
        
    public int getNumOfObjects(){
        return root.getNumOfObjects();
    }
    public int getNumOfNodes(){
        return root.getNumOfONodes();
    }

    protected void clean() {
        root.clean();
        root =null;
    }
    
    
}
