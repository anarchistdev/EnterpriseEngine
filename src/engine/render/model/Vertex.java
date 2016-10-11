package engine.render.model;

import engine.math.Vector3f;

import java.util.ArrayList;
import java.util.List;


public class Vertex {

    private static final int NO_INDEX = -1;

    private Vector3f position;
    private int textureIndex = NO_INDEX;
    private int normalIndex = NO_INDEX;
    private Vertex duplicateVertex = null;
    private int index;
    private float length;
    private Vector3f tangent;
    private Vector3f averagedTangent = new Vector3f(0, 0, 0);

    public Vertex(int index, Vector3f position){
        this.index = index;
        this.position = position;
        this.length = position.length();
    }

    public void setTangent(Vector3f tangent){
        this.tangent = tangent;
    }

    public Vector3f getTangent() {
        return tangent;
    }

    //NEW
    public Vertex duplicate(int newIndex){
        Vertex vertex = new Vertex(newIndex, position);
        vertex.tangent = this.tangent;
        return vertex;
    }

//    public void averageTangents(){
//        if(tangents.isEmpty()){
//            return;
//        }
//        for(Vector3f tangent : tangents){
//            Vector3f.add(averagedTangent, tangent, averagedTangent);
//        }
//        averagedTangent.normalise();
//    }

    public Vector3f getAverageTangent(){
        return averagedTangent;
    }

    public int getIndex(){
        return index;
    }

    public float getLength(){
        return length;
    }

    public boolean isSet(){
        return textureIndex!=NO_INDEX && normalIndex!=NO_INDEX;
    }

    public boolean hasSameTextureAndNormal(int textureIndexOther,int normalIndexOther){
        return textureIndexOther==textureIndex && normalIndexOther==normalIndex;
    }

    public void setTextureIndex(int textureIndex){
        this.textureIndex = textureIndex;
    }

    public void setNormalIndex(int normalIndex){
        this.normalIndex = normalIndex;
    }

    public Vector3f getPosition() {
        return position;
    }

    public int getTextureIndex() {
        return textureIndex;
    }

    public int getNormalIndex() {
        return normalIndex;
    }

    public Vertex getDuplicateVertex() {
        return duplicateVertex;
    }

    public void setDuplicateVertex(Vertex duplicateVertex) {
        this.duplicateVertex = duplicateVertex;
    }

}
