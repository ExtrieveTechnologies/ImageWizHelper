package com.extrieve.imaging.sdk;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.nio.file.*;
import java.util.HashMap;

import javax.naming.Context;


public class ImageWizHelperJNI
{
    private final Object lock = new Object();
    private static long handle = 0;

    public enum Layout
    {
        Unknown(-1),
        A0(0),
        A1(1),
        A2(2),
        A3(3),
        A4(4),
        A5(5),
        A6(6),
        A7(7);

        private int value;
        private static HashMap<Object, Object> map = new HashMap<Object, Object>();

        Layout(int value) {
            this.value = value;
        }

        static {
            for (Layout layout : Layout.values()) {
                map.put(layout.value, layout);
            }
        }
        
        public static Layout valueOf(int layout) {
            return (Layout) map.get(layout);
        }

        public int getValue() {
            return value;
        }
    }
        
    
    public enum ImageDPI
    {
        Unknown(-1),
          DPI_100(0),
        DPI_150(1),
        DPI_200(2),
        DPI_300(3),
        DPI_500(4),
        DPI_600(5);
        
        private int value;

        private static HashMap<Object, Object> map = new HashMap<Object, Object>();
        
        ImageDPI(int value) {
            this.value = value;
        }

        static {
            for (ImageDPI imageDPI : ImageDPI.values()) {
                map.put(imageDPI.value, imageDPI);
            }
        }
        
        public static ImageDPI valueOf(int imageDPI) {
            return (ImageDPI) map.get(imageDPI);
        }
        
        public int getValue() {
            return value;
        }
    }

    public enum ConversionType
    {
        Unknown(-1),
        No_Conversion(0),
        Convert_To_BW(1),
        Convert_To_Grey(2);
        
        private int value;
         private static HashMap<Object, Object> map = new HashMap<Object, Object>();

         ConversionType(int value) {
             this.value = value;
         }
         
         static {
            for (ConversionType conversiontype : ConversionType.values()) {
                map.put(conversiontype.value, conversiontype);
            }
        }
        
        public static ConversionType valueOf(int conversiontype) {
            return (ConversionType) map.get(conversiontype);
        }

         public int getValue() {
             return value;
         }
        
    } 

    public enum ResetOption
    {    
        No_DPI_change(0),             // DPI will not be resetted this case . 
        ResetAllDPI(1),              // Every image DPI will be resetted to selected DPI.  Dimension also will be changed according to DPI
        ResetZeroDPI(2);             // If DPI is not available then DPI will setted for the image.  Dimension also will be changed according to DPI
    
        private int value;
        private static HashMap<Object, Object> map = new HashMap<Object, Object>();

            ResetOption(int value) {
            this.value = value;
        }
            
        static {
            for (ResetOption resetOption : ResetOption.values()) {
                map.put(resetOption.value, resetOption);
            }
        }
        
        public static ResetOption valueOf(int resetOption) {
            return (ResetOption) map.get(resetOption);
        }            

        public int getValue() {
            return value;
        }
        
    }

    public enum ImageQuality
    {
        Unknown(-1),
        Photo_Quality(0),
        Document_Quality(1),
        Compressed_Document(2);
        
        private final int value;
        private static HashMap<Object, Object> map = new HashMap<Object, Object>();

        ImageQuality(int value) {
            this.value = value;
        }

        static {
            for (ImageQuality imageQuality : ImageQuality.values()) {
                map.put(imageQuality.value, imageQuality);
            }
        }
        
        public static ImageQuality valueOf(int imageQuality) {
            return (ImageQuality) map.get(imageQuality);
        }
        
        public int getValue() {
            return value;
        }
    }

    String DllName = "ImageWizHelper_jni";
    
    //Constructor of ImageWizHelperJNI 
    public ImageWizHelperJNI(String dll_path, String license_data, String app_name) throws Exception 
    {
        String dllPath = dll_path + File.separator + DllName + ".dll";
        if (!Files.exists(Paths.get(dllPath)))
        {
            throw new RuntimeException(DllName + " library not found");
        }

        try
        {
            System.load(dllPath);

            synchronized (lock) {
                StringBuffer error_info = new StringBuffer("");
                if (!unlockLibrary(license_data, app_name, error_info))
                {
                    throw new Exception(error_info.toString());
                }
            }

            handle = initialize();//We are getting the Handle.
            if (handle == 0)
                throw new RuntimeException("Unable to load imaging library");

            // Register a shutdown hook
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    terminate(handle);
                }
            });
        } catch(UnsatisfiedLinkError ex)
        {
            System.out.println(ex.toString());
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        terminate(handle);
    }

    private static native boolean unlockLibrary(String license_file_content, String app_name,  StringBuffer error_info);

    private static native long initialize();

    private static native int terminate(long hdl);

    private static native void resetToDefault(long hdl);

    private static native int getErrorDescription(int error, StringBuffer desc_buffer, int desc_len);

    private static native int setPageLayout(long hdl, int Layout);
    private static native int getPageLayout(long hdl, IntPtr objHeight, IntPtr objWidth);

    private static native int setDPI(long hdl, int ImageDPI);
    private static native int getDPI(long hdl, IntPtr objDpi);

    private static native int setConversion(long hdl, int ConvertionType);
    private static native int getConversion(long hdl, IntPtr objConversionType);

    private static native int setImageQuality(long hdl,int ImageQuality);
    private static native int getImageQuality(long hdl, IntPtr objQuality);

    private static native int setImageCustomWidth(long hdl, int widthInInches);
    private static native int getImageCustomWidth(long hdl, IntPtr objWidthInInches);

    private static native int setImageCustomHeigth(long hdl, int heightInInches);
    private static native int getImageCustomHeight(long hdl, IntPtr objHeightInInches);

    private static native int enableExifRotation(long hdl);
    private static native int disableExifRotation(long hdl);

    private static native int getPageCountInImageFile(long hdl, String filename, String pwd);

    private static native int compressToTIFF(long hdl, String[] inputFiles, String outputFile, int ResetOption);
    private static native int compressToPDF(long hdl, String[] inputFiles, String outputFile, int ResetOption);
    private static native int compressToJPEG(long hdl, String[] inputFile, String outPutFile, int ResetOption);

    private static native int ocrBWConversionToTiff(long hdl, String inputFile, int pageNumber, String outputFileName, int ResetOption);

    private static native int appendToTIFF(long hdl, String inputFile, String outputFile,int ResetOption);

    private static native int getMaskImage(long hdl, String fileName, String outputFileName, int PageNum, Rect[] MaskPortion, long MaskValue);
    private static native int compressPagesToTiff_Array(long hdl, String inputFile, String outputFile, int[] pageArray, boolean append, int ResetOption);
    private static native int compressPagesToTiff_Range(long hdl, String inputFile, String outputFile, int startPage, int endPage, boolean append, int ResetOption);

    public String GetErrorDescription(int errorCode) 
    {
        StringBuffer error_info = new StringBuffer ("");
        getErrorDescription(errorCode,error_info,255);
        return error_info.toString();
    }

    public  void ResetToDefault() {
        resetToDefault(handle);
    }

    public int SetPageLayout(Layout layout) {
        return setPageLayout(handle, layout.getValue());
    }

    public Layout GetPageLayouWidth( ) {
        IntPtr objWidth, objHeight;

        objWidth = new IntPtr();
        objHeight = new IntPtr();

        int ret = getPageLayout(handle,objHeight,objWidth);

        if (ret != 0) { return Layout.Unknown; }

        return Layout.valueOf(objWidth.value);
    }

    public Layout GetPageLayouHeight( ) {
        IntPtr objWidth, objHeight;

        objWidth = new IntPtr();
        objHeight = new IntPtr();

        int ret = getPageLayout(handle,objHeight,objWidth);

        if (ret != 0) { return Layout.Unknown; }

        return Layout.valueOf(objHeight.value);
    }

    public int SetDPI(ImageDPI imageDPI) {
        return setDPI(handle,imageDPI.getValue());
    }

    public ImageDPI GetDPI() {
        IntPtr objDpi;
        objDpi =new IntPtr();

        int ret=getDPI(handle,objDpi);

        if (ret != 0) { return ImageDPI.Unknown; }

        return ImageDPI.valueOf(objDpi.value);
    }

    public int SetConversion(ConversionType convertionType) {
        return setConversion(handle,convertionType.getValue());
    }

    public ConversionType GetConversion() {
        IntPtr objConversionType;
        objConversionType=new IntPtr();

        int ret=getConversion(handle,objConversionType);

        if (ret != 0) { return ConversionType.Unknown; }

        return ConversionType.valueOf(objConversionType.value);
    }

    public int SetImageQuality(ImageQuality imageQuality) {
        return setImageQuality(handle,imageQuality.getValue());
    }

    public ImageQuality GetImageQuality() {
        IntPtr objquality;
        objquality=new IntPtr();

        int ret=getImageQuality(handle,objquality);

        if (ret != 0) { return ImageQuality.Unknown; }

        return ImageQuality.valueOf(objquality.value);
    }

    public int SetImageCustomWidth(int widthInInches) {
        return setImageCustomWidth(handle,widthInInches);
    }

    public int getImageCustomHeight() {
        IntPtr objHeightInInches;
        objHeightInInches=new IntPtr();

        int ret=getImageCustomHeight(handle,objHeightInInches);

        if(ret!=0) {
            return ret;
        }
        return objHeightInInches.value;
    }

    public int SetImageCustomHeigth(int heightInInches) {
        return setImageCustomHeigth(handle,heightInInches);
    }

    public int GetImageCustomHeight() {
        IntPtr objHeightInInches = new IntPtr();
        int ret=getImageCustomHeight(handle, objHeightInInches);

        if(ret!=0) {
            return ret;
        }
        return objHeightInInches.value;
    }

    public int EnableExifRotation() {
        return enableExifRotation(handle);
    }

    public int DisableExifRotation() {
        return disableExifRotation(handle);
    }

    public int GetPageCountInImageFile(String filename, String pwd) {
        return getPageCountInImageFile(handle,filename,pwd);
    }

    public int CompressToTIFF(String[] inputFiles, String outputFile, ResetOption resetOption) {
        return compressToTIFF( handle, inputFiles,  outputFile,  resetOption.getValue());
    }

    public int CompressToPDF(String[] inputFiles, String outputFile, ResetOption resetOption) {
        return compressToPDF( handle,  inputFiles,  outputFile,  resetOption.getValue());
    }

    public int CompressToJPEG(String[] inputFile, String outPutFile, ResetOption resetOption) {
        return compressToJPEG(handle,  inputFile,  outPutFile,  resetOption.getValue());
    }

    public int OcrBWConversionToTiff(String inputFile, int pageNumber, String outputFileName, ResetOption resetOption) {
        return ocrBWConversionToTiff(handle,  inputFile,  pageNumber,  outputFileName,  resetOption.getValue());
    }

    public int AppendToTIFF(String inputFile, String outputFile,ResetOption resetOption) {
        return appendToTIFF(handle,  inputFile,  outputFile, resetOption.getValue());
    }

    public int GetMaskImage(String fileName, String outputFileName, int PageNum, Rect[] MaskPortion, long MaskValue) {
        return getMaskImage(handle,  fileName,  outputFileName,  PageNum,  MaskPortion,  MaskValue);
    }

    public int CompressPagesToTiff_Array(String inputFile, String outputFile, int[] pageArray, boolean append,ResetOption resetOption) {
        return compressPagesToTiff_Array(handle,  inputFile,  outputFile,  pageArray,  append,  resetOption.getValue());
    }

    public int CompressPagesToTiff_Range(String inputFile, String outputFile, int startPage, int endPage, boolean append, ResetOption resetOption){
        return compressPagesToTiff_Range(handle,  inputFile,  outputFile,  startPage,  endPage,  append,  resetOption.getValue());

    }
}
