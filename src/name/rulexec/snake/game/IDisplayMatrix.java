package name.rulexec.snake.game;


public interface IDisplayMatrix {
    public void setPixel(int x, int y, PixelType type);
    public void disablePixel(int x, int y);
}
