package com.files;

import com.aspose.pdf.Document;
import com.aspose.pdf.devices.PngDevice;
import com.aspose.pdf.devices.Resolution;

import java.io.File;

public class PdfUtils {
    public static File generatePdfPreview(File pdfFile){
        try {
            com.aspose.pdf.Document pdfDocument = new Document(pdfFile.getAbsolutePath());


// Loop through all the pages of PDF file
            //for (int pageCount = 1; pageCount <= pdfDocument.getPages().size(); pageCount++) {
                // Create stream object to save the output image
            int pageCount = 0;
            File output = new File(pdfFile.getParentFile(), pdfFile.getName().split(".")[0] + "_" + pageCount + ".png");
                java.io.OutputStream imageStream = new java.io.FileOutputStream(output);

                // Create Resolution object
                Resolution resolution = new Resolution(300);
                // Create PngDevice object with particular resolution
                PngDevice pngDevice = new PngDevice(resolution);
                // Convert a particular page and save the image to stream
                pngDevice.process(pdfDocument.getPages().get_Item(pageCount), imageStream);

                // Close the stream
                imageStream.close();
                return output;
            //}

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
