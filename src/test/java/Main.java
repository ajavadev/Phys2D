import com.github.ajavadev.render.RenderEngine;
import com.github.ajavadev.util.Vector2;

public class Main {
    public static void main(String[] args) {
        try (RenderEngine renderEngine = new RenderEngine(new Vector2(500, 500), "Phys2D")) {
            renderEngine.run();
        }
    }
}
