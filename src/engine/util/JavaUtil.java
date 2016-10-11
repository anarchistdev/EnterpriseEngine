package engine.util;

import java.util.List;

/**
 * Created by anarchist on 8/6/16.
 */
public class JavaUtil {
    public static int[] listToArray(List<Integer> list) {
        int[] indicesArray = new int[list.size()];
        for (int i = 0; i < indicesArray.length; i++) {
            indicesArray[i] = list.get(i);
        }
        return indicesArray;
    }
}
