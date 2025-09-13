package dev.hilligans.engine.client.input.key;

import dev.hilligans.engine.data.Tuple;
import it.unimi.dsi.fastutil.ints.Int2CharOpenHashMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyHandler {

    public static Int2CharOpenHashMap mappedKeys = new Int2CharOpenHashMap();
    public static Int2ObjectOpenHashMap<String> namedKeys = new Int2ObjectOpenHashMap<>();
    public static Int2CharOpenHashMap shiftMappedKeys = new Int2CharOpenHashMap();
    public static boolean[] keyPressed = new boolean[350];

    public static ConcurrentLinkedQueue<CharPress> charPresses = new ConcurrentLinkedQueue<>();
    public static ConcurrentLinkedQueue<Tuple<KeyPress, Integer>> keyPresses = new ConcurrentLinkedQueue<>();

    public static void register(KeyPress keyPress, int id) {
        keyPresses.add(new Tuple<>(keyPress,id));
    }

    public static void register(KeyPress keyPress, int... id) {
        for(int id1 : id) {
            keyPresses.add(new Tuple<>(keyPress, id1));
        }
    }

    public static void register(CharPress charPress) {
        charPresses.add(charPress);
    }

    public static void remove(KeyPress keyPress) {
        for(Tuple<KeyPress, Integer> wrapper : keyPresses) {
            if(keyPress == wrapper.getTypeA()) {
                keyPresses.remove(wrapper);
                break;
            }
        }
    }

    public static void remove(CharPress charPress) {
        charPresses.remove(charPress);
    }

    private static void onPress(int id) {
        char key = getChar(id);
        for(CharPress charPress : charPresses) {
            if(key != Character.MIN_VALUE) {
                charPress.onPress(key);
            }
        }

        for(Tuple<KeyPress, Integer> keyPress : keyPresses) {
            if(keyPress.getTypeB() == id) {
                keyPress.getTypeA().onPress();
            }
        }
    }

    private static void onUnPress(int id) {
        char key = getChar(id);
        for(CharPress charPress : charPresses) {
            if(key != Character.MIN_VALUE) {
                charPress.onUnPress(key);
            }
        }

        for(Tuple<KeyPress, Integer> keyPress : keyPresses) {
            if(keyPress.getTypeB() == id) {
                keyPress.getTypeA().onUnPress();
            }
        }
    }

    private static void onRepeat(int id) {
        char key = getChar(id);
        for(CharPress charPress : charPresses) {
            if(key != Character.MIN_VALUE) {
                charPress.onRepeat(key);
            }
        }

        for(Tuple<KeyPress, Integer> keyPress : keyPresses) {
            if(keyPress.getTypeB() == id) {
                keyPress.getTypeA().onRepeat();
            }
        }
    }

    private static char getChar(int id) {
      /*  if(glfwGetKey(ClientMain.getClient().window,GLFW_KEY_LEFT_SHIFT) == GLFW_PRESS || glfwGetKey(ClientMain.getClient().window,GLFW_KEY_RIGHT_SHIFT) == GLFW_PRESS) {
            return shiftMappedKeys.getOrDefault(id,mappedKeys.getOrDefault(id,Character.MIN_VALUE));
        } else {
            return mappedKeys.getOrDefault(id,Character.MIN_VALUE);
        }

       */
        throw new RuntimeException();
    }

    private static void setCallback(long window) {
        GLFW.glfwSetKeyCallback(window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(action == GLFW_PRESS) {
                    keyPressed[key] = true;
                    onPress(key);
                } else if(action == GLFW_RELEASE) {
                    keyPressed[key] = false;
                    onUnPress(key);
                } else {
                    onRepeat(key);
                }
            }
        });
    }

    private static void putKeyAndKey(int id, char key) {
        mappedKeys.put(id,key);
        namedKeys.put(id,key + "");
    }

    private static void putName(int id, String name) {
        namedKeys.put(id,name);
    }

    static {
       // if(ClientMain.getClient().glStarted) {
            //setCallback(ClientMain.getClient().window);
       // } else {
           // Ourcraft.GAME_INSTANCE.EVENT_BUS.register(GLInitEvent.class,KeyHandler::setCallback);
       // }

        mappedKeys.put(32,' ');
        mappedKeys.put(39, '\'');
        mappedKeys.put(44,',');
        mappedKeys.put(45,'-');
        mappedKeys.put(46,'.');
        mappedKeys.put(47,'/');
        mappedKeys.put(48,'0');
        mappedKeys.put(49,'1');
        mappedKeys.put(50,'2');
        mappedKeys.put(51,'3');
        mappedKeys.put(52,'4');
        mappedKeys.put(53,'5');
        mappedKeys.put(54,'6');
        mappedKeys.put(55,'7');
        mappedKeys.put(56,'8');
        mappedKeys.put(57,'9');
        mappedKeys.put(59,';');
        mappedKeys.put(61,'=');
        mappedKeys.put(65,'a');
        mappedKeys.put(66,'b');
        mappedKeys.put(67,'c');
        mappedKeys.put(68,'d');
        mappedKeys.put(69,'e');
        mappedKeys.put(70,'f');
        mappedKeys.put(71,'g');
        mappedKeys.put(72,'h');
        mappedKeys.put(73,'i');
        mappedKeys.put(74,'j');
        mappedKeys.put(75,'k');
        mappedKeys.put(76,'l');
        mappedKeys.put(77,'m');
        mappedKeys.put(78,'n');
        mappedKeys.put(79,'o');
        mappedKeys.put(80,'p');
        mappedKeys.put(81,'q');
        mappedKeys.put(82,'r');
        mappedKeys.put(83,'s');
        mappedKeys.put(84,'t');
        mappedKeys.put(85,'u');
        mappedKeys.put(86,'v');
        mappedKeys.put(87,'w');
        mappedKeys.put(88,'x');
        mappedKeys.put(89,'y');
        mappedKeys.put(90,'z');
        mappedKeys.put(91,'[');
        mappedKeys.put(92,'\\');
        mappedKeys.put(93,']');
        mappedKeys.put(96,'`');

        putKeyAndKey(GLFW.GLFW_KEY_A,'a');


        shiftMappedKeys.put(39, '\"');
        shiftMappedKeys.put(44,'<');
        shiftMappedKeys.put(45,'_');
        shiftMappedKeys.put(46,'>');
        shiftMappedKeys.put(47,'?');
        shiftMappedKeys.put(48,')');
        shiftMappedKeys.put(49,'!');
        shiftMappedKeys.put(50,'@');
        shiftMappedKeys.put(51,'#');
        shiftMappedKeys.put(52,'$');
        shiftMappedKeys.put(53,'%');
        shiftMappedKeys.put(54,'^');
        shiftMappedKeys.put(55,'&');
        shiftMappedKeys.put(56,'*');
        shiftMappedKeys.put(57,'(');
        shiftMappedKeys.put(59,':');
        shiftMappedKeys.put(61,'+');
        shiftMappedKeys.put(91,'{');
        shiftMappedKeys.put(92,'|');
        shiftMappedKeys.put(93,'}');
        shiftMappedKeys.put(96,'~');
    }

    /** Function keys. */
    public static final int
            GLFW_KEY_ESCAPE        = 256,
            GLFW_KEY_ENTER         = 257,
            GLFW_KEY_TAB           = 258,
            GLFW_KEY_BACKSPACE     = 259,
            GLFW_KEY_INSERT        = 260,
            GLFW_KEY_DELETE        = 261,
            GLFW_KEY_RIGHT         = 262,
            GLFW_KEY_LEFT          = 263,
            GLFW_KEY_DOWN          = 264,
            GLFW_KEY_UP            = 265,
            GLFW_KEY_PAGE_UP       = 266,
            GLFW_KEY_PAGE_DOWN     = 267,
            GLFW_KEY_HOME          = 268,
            GLFW_KEY_END           = 269,
            GLFW_KEY_CAPS_LOCK     = 280,
            GLFW_KEY_SCROLL_LOCK   = 281,
            GLFW_KEY_NUM_LOCK      = 282,
            GLFW_KEY_PRINT_SCREEN  = 283,
            GLFW_KEY_PAUSE         = 284,
            GLFW_KEY_F1            = 290,
            GLFW_KEY_F2            = 291,
            GLFW_KEY_F3            = 292,
            GLFW_KEY_F4            = 293,
            GLFW_KEY_F5            = 294,
            GLFW_KEY_F6            = 295,
            GLFW_KEY_F7            = 296,
            GLFW_KEY_F8            = 297,
            GLFW_KEY_F9            = 298,
            GLFW_KEY_F10           = 299,
            GLFW_KEY_F11           = 300,
            GLFW_KEY_F12           = 301,
            GLFW_KEY_F13           = 302,
            GLFW_KEY_F14           = 303,
            GLFW_KEY_F15           = 304,
            GLFW_KEY_F16           = 305,
            GLFW_KEY_F17           = 306,
            GLFW_KEY_F18           = 307,
            GLFW_KEY_F19           = 308,
            GLFW_KEY_F20           = 309,
            GLFW_KEY_F21           = 310,
            GLFW_KEY_F22           = 311,
            GLFW_KEY_F23           = 312,
            GLFW_KEY_F24           = 313,
            GLFW_KEY_F25           = 314,
            GLFW_KEY_KP_0          = 320,
            GLFW_KEY_KP_1          = 321,
            GLFW_KEY_KP_2          = 322,
            GLFW_KEY_KP_3          = 323,
            GLFW_KEY_KP_4          = 324,
            GLFW_KEY_KP_5          = 325,
            GLFW_KEY_KP_6          = 326,
            GLFW_KEY_KP_7          = 327,
            GLFW_KEY_KP_8          = 328,
            GLFW_KEY_KP_9          = 329,
            GLFW_KEY_KP_DECIMAL    = 330,
            GLFW_KEY_KP_DIVIDE     = 331,
            GLFW_KEY_KP_MULTIPLY   = 332,
            GLFW_KEY_KP_SUBTRACT   = 333,
            GLFW_KEY_KP_ADD        = 334,
            GLFW_KEY_KP_ENTER      = 335,
            GLFW_KEY_KP_EQUAL      = 336,
            GLFW_KEY_LEFT_SHIFT    = 340,
            GLFW_KEY_LEFT_CONTROL  = 341,
            GLFW_KEY_LEFT_ALT      = 342,
            GLFW_KEY_LEFT_SUPER    = 343,
            GLFW_KEY_RIGHT_SHIFT   = 344,
            GLFW_KEY_RIGHT_CONTROL = 345,
            GLFW_KEY_RIGHT_ALT     = 346,
            GLFW_KEY_RIGHT_SUPER   = 347,
            GLFW_KEY_MENU          = 348,
            GLFW_KEY_LAST          = GLFW_KEY_MENU;


    public static final int

            GLFW_KEY_WORLD_1       = 161,
            GLFW_KEY_WORLD_2       = 162;
}
