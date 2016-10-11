package engine.tree;

import engine.math.Vector2f;
import engine.math.Vector3f;
import org.lwjgl.system.CallbackI;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anarchist on 7/6/16.
 */
public class Tree {
    private Branch root;
    private List<Vector3f> vert;
    private List<Vector3f>  normal;
    private List<Vector2f> uv;
    private List<Vector3f>  twigNormal;
    private List<Vector3f>  twigVert;
    private List<Vector2f> twigUv;
    private List<Vector3f>  face;
    private List<Vector3f>  twigFace;

    private int vertCount;
    private int twigVertCount;
    private int faceCount;
    private int twigFaceCount;

    private TreeProperties properties;

    public Tree(TreeProperties properties) {
        vert = new ArrayList<>();
        normal = new ArrayList<>();
        uv = new ArrayList<>();
        twigNormal = new ArrayList<>();
        twigVert = new ArrayList<>();
        twigUv = new ArrayList<>();
        twigFace = new ArrayList<>();
        face = new ArrayList<>();

        vertCount = 0;
        twigVertCount = 0;
        faceCount = 0;
        twigFaceCount = 0;

        this.properties = properties;
    }

    public void generate() {
        properties.setmRSeed(properties.getSeed());
        Vector3f startHead = new Vector3f(0, properties.getTrunkLength(), 0);
        root = new Branch(startHead, null);
        root.setLength(properties.getInitialBranchLength());
        root.split(properties.getLevels(), properties.getTreeSteps(), properties, 1, 1);

    }

//    private void fixUvs() {
//        int[] badVertTable = new int[vertCount / 2];
//        int i;
//        int badVerts = 0;
//        for (i = 0; i < faceCount; i++) {
//            if (badVertTable[j] == face.get(i).y && uv.get((int)face.get(i).y).x == 0)
//                int found = 0;
//                int j;
//                for (j = 0; j < badVerts; j++) {
//                }
//        }
//    }

    private void calcVertSizes(Branch branch) {
        int segments = properties.getSegments();
        if (branch == null) {
            branch = root;
        }

        if (branch.getParent() == null) {
            vertCount += segments;
        }

        if (branch.getChild0() != null) {
            vertCount += 1 + (segments / 2) - 1 + 1 + (segments / 2) - 1 + (segments / 2) - 1;
            calcVertSizes(branch.getChild0());
            calcVertSizes(branch.getChild1());
        } else {
            vertCount++;
            twigVertCount += 8;
            twigFaceCount += 4;
        }
    }

    private void calcFaceSizes(Branch branch) {
        int segments = properties.getSegments();
        if (branch == null) {
            branch = root;
        }

        if (branch.getParent() == null) {
            faceCount += segments * 2;
        }

        if (branch.getChild0().getRing0() != 0) {
            faceCount += segments * 4;
            calcFaceSizes(branch.getChild0());
            calcFaceSizes(branch.getChild1());
        } else {
            faceCount += segments * 2;
        }
    }

    private void calcNormals() {
        int[] normalCount = new int[vertCount];
        int i;

        for (i = 0; i < faceCount; i++) {
            normalCount[(int)face.get(i).x]++;
            normalCount[(int)face.get(i).y]++;
            normalCount[(int)face.get(i).z]++;

           // Vector3f norm =
        }
    }


}
