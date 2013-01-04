package name.rulexec.snake.game;

public class Game {
    private IDisplayMatrix displayMatrix;
    
    private Snake snake;
    private Direction direction;
    
    private float timer = 0;
    
    public Game() {
        this.snake = new Snake(14, 11);
        this.direction = Direction.LEFT;
    }
    
    public void setDisplayMatrix(IDisplayMatrix dm) {
        this.displayMatrix = dm;
    }
    
    public void init() {
        // ���������� �������
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < 28; i++) {
                this.displayMatrix.setPixel(i, j * 21, PixelType.BLOCK);
            }
        }
        
        for (int j = 0; j < 2; j++) {
            for (int i = 1; i < 21; i++) {
                this.displayMatrix.setPixel(j * 27, i, PixelType.BLOCK);
            }
        }
    }
    
    public void changeDirection(Direction d) {
        this.direction = d;
    }
    
    public boolean step(float deltaT) {
        // ���������� � ���������� �������, ������� ������
        this.timer += deltaT;
        
        boolean alive = true;
        
        // ���� ������ ������, ��� 300��
        while (this.timer > 0.3) {
            this.timer -= 0.3;
            
            // �� ������������� ������� ��������
            alive = this.recalc();
        }

        return alive;
    }
    
    private boolean recalc() {
        // ������� ������� �������������� ������
        this.displayMatrix.disablePixel(this.snake.x, this.snake.y);
        // ����������� � ������ �������
        this.snake.move(this.direction);
        // ���������� ������� �� ����� �����
        this.displayMatrix.setPixel(this.snake.x, this.snake.y, PixelType.SNAKE_HEAD);
        
        // ���� ��������� � �������, ����������� ��� ����������
        if (this.snake.x == 0 || this.snake.x == 27 ||
            this.snake.y == 0 || this.snake.y == 21)
        {
            return false;
        } else {
            return true;
        }
    }
}
