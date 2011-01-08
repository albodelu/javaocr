package net.sourceforge.javaocr.filter;

import net.sourceforge.javaocr.Image;
import net.sourceforge.javaocr.ImageFilter;
import net.sourceforge.javaocr.ocr.PixelImage;

/**
 * computes integral image.   result os stored in  the allocated image
 * to be retrieved.  Basically it is sum of all pixels from top-left part
 * of image relative to current pixel. this filter can work in place, but
 * intendet usage tells us to create and cache destination image
 */
public class IntregralImageFilter implements ImageFilter {

    private Image resultImage;
    private Image empty;

    /**
     * image to be used as result.  size shall match to processed
     * images.  responsibility lies on caller. you are also responsible for format
     * so that values do not overflow
     *
     * @param resultImage
     */
    public IntregralImageFilter(Image resultImage) {
        this.resultImage = resultImage;
        // just an empty row, substitute as previous
        empty = new PixelImage(resultImage.getWidth(), 1);
    }

    /**
     * process image. size of image under process and configured destination
     * image must match  -  it is up to caller to ensure format compatibility
     * (otherwise be prepared to receive lots of garbled images and IOOB exceptions)
     *
     * @param image image to be processed
     */
    public void process(Image image) {
        final int height = image.getHeight();
        int cumulated;

        for (int i = 0; i < height; i++) {

            cumulated = 0;
            // iterate over all the  scans
            Image sourceScan = image.row(i);
            Image destinationScan = resultImage.row(i);
            Image previous = i > 0 ? resultImage.row(i - 1) : empty;

            for (sourceScan.iterateH(0), destinationScan.iterateH(0), previous.iterateH(0); sourceScan.hasNext();) {
                cumulated += sourceScan.next();
                destinationScan.next(cumulated + previous.next());
            }
        }
    }


}
