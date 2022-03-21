package sttsoft.com.br.mlkit_test;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Build;
import android.os.ParcelFileDescriptor;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

public class utils {


    public static File getAppDir(Context context) throws Exception {
        PackageManager packageManager;
        String packageName;
        PackageInfo packageInfo;
        File file;

        packageManager = context.getPackageManager();
        packageName = context.getPackageName();

        packageInfo = packageManager.getPackageInfo(packageName, 0);
        file = new File(packageInfo.applicationInfo.dataDir);

        return file;
    }

    public static File getDiretorioCarga(Context context, String nome) throws Exception {
        File file = new File(getAppDir(context), "pdf/" + nome);

        if (!file.exists())
            file.mkdir();

        return file;
    }

    public static byte[] readFileToBytes(String filePath) throws IOException {

        File file = new File(filePath);
        byte[] bytes = new byte[(int) file.length()];

        FileInputStream fis = null;
        try {

            fis = new FileInputStream(file);

            fis.read(bytes);

        } finally {
            if (fis != null) {
                fis.close();
            }
            return bytes;
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static ArrayList<Bitmap> pdfToBitmap(Context context, File pdfFile) {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();

        try {
            PdfRenderer renderer = new PdfRenderer(ParcelFileDescriptor.open(pdfFile, ParcelFileDescriptor.MODE_READ_ONLY));

            Bitmap bitmap;
            final int pageCount = renderer.getPageCount();
            for (int i = 0; i < pageCount; i++) {
                PdfRenderer.Page page = renderer.openPage(i);

                int width = context.getResources().getDisplayMetrics().densityDpi * page.getWidth() / 72 ;
                int height = context.getResources().getDisplayMetrics().densityDpi * page.getHeight() / 72;
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_PRINT);

                bitmaps.add(bitmap);

                // close the page
                page.close();

            }

            // close the renderer
            renderer.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return bitmaps;

    }
}
