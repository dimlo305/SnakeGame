package name.rulexec.snake.game;

public class Snake {
    public int x, y;
    public Snake(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public void move(Direction d) {
        switch (d) {
            case LEFT: this.x -= 1; break;
            case RIGHT: this.x += 1; break;
            case UP: this.y -= 1; break;
            case DOWN: this.y += 1; break;
        }
    }
}
