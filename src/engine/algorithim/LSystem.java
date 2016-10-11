package engine.algorithim;


import java.util.HashMap;
import java.util.Map;

/**
 * Created by anarchist on 6/23/16.
 */
public class LSystem {
    private Map<Character, String> rules = new HashMap<>();

    public LSystem() {
        rules.put('L', "L+R+");
        rules.put('R', "-L+R");
    }

    private String rewrite(String str) {
        String result = "";
        for (Character character : rules.keySet()) {
            if (rules.containsKey(character)) {
                result += rules.get(character);
            } else {
                result += character;
            }
        }

        return result;
    }
}
