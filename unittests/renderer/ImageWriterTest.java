package renderer;

import org.junit.jupiter.api.Test;
import primitives.Color;
import renderer.ImageWriter;

import static java.awt.Color.*;

/**
 * Unit test for {@link ImageWriter} class.
 * This test verifies the image writing functionality by drawing a colored grid.
 * The image consists of a grid of red lines over a blue background.
 */
public class ImageWriterTest {

    /**
     * Test method for {@link ImageWriter#writeToImage(String)}.
     * <p>
     * This test creates an image with a red grid drawn over a blue background.
     * The grid lines are spaced evenly every {@code pixelDim} pixels.
     * Each pixel is colored either red (if it's on a grid line) or blue otherwise.
     * </p>
     * <ul>
     *     <li>The image resolution is 500x800 pixels.</li>
     *     <li>The grid consists of 10 horizontal and 16 vertical sections.</li>
     *     <li>The resulting image is saved with the filename "yellowGrid".</li>
     * </ul>
     */
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
                    } else {
                        imageWriter.writePixel(j, i, blue);
                    }
                }
            } else {
                for (int j = 0; j < nY; j++) {
                    imageWriter.writePixel(j, i, red);
                }
            }
        }
        imageWriter.writeToImage("yellowGrid");
    }
}
