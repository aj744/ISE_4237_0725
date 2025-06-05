package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;
import renderer.ImageWriter;

import static java.awt.Color.*;

public class ImageWriterTest {

    @Test
    public void testWriteImage() {
        final int pixelX = 10;
        final int pixelY = 16;
        final int nX = 500;
        final int nY = 800;
        final int pixelDim = nX / pixelX;
        final Color blue = new Color(BLUE);
        final Color red = new Color(RED);
        ImageWriter imageWriter = new ImageWriter(nY, nX);
        for (int i = 0; i < nX; i++) {
            if (i % pixelDim != 0) {
                for (int j = 0; j < nY; j++) {
                    if (j % pixelDim == 0) {
                        imageWriter.writePixel(j, i, red);
                    }
                    else {
                        imageWriter.writePixel(j, i, blue);
                    }
                }
            }
            else {
                for (int j = 0; j < nY; j++) {
                    imageWriter.writePixel(j, i, red);
                }
            }
        }
        imageWriter.writeToImage("yellowGrid");
    }
}
