public class Settings {
    // The width and height of each individual tile
    public static final int TILE_SIZE = 48;

    // The amount of tiles vertically and horizontally in the map
    public static final int GRID_SIZE = 16;

    public static double moveTime(int pieces) {
        return 125 + 375 / Math.pow(pieces, 0.5);
    }
}
