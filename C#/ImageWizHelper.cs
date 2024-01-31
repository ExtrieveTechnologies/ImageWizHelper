using System;
using System.Collections.Generic;
using System.Configuration;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading.Tasks;

namespace ImageWizHelperDemo
{
    public enum Layout
    {
        A0,
        A1,
        A2,
        A3,
        A4,
        A5,
        A6,
        A7
    };

    public enum ImageDPI
    {
        DPI_100 = 100,
        DPI_150 = 150,
        DPI_200 = 200,
        DPI_300 = 300,
        DPI_500 = 500,
        DPI_600 = 600
    };

    public enum ResetOption
    {
        No_DPI_change,          // DPI will not be resetted this case . 
        ResetAllDPI,            // Every image DPI will be resetted to selected DPI.  Dimension also will be changed according to DPI
        ResetNonDPI             // If DPI is not available then DPI will setted for the image.  Dimension also will be changed according to DPI
    };

    public enum ImageQuality
    {
        unknown  = -1,
        Photo_Quality = 70,
        Document_Quality = 40,
        Compressed_Document = 20
    };
    
    [StructLayout(LayoutKind.Sequential, Pack = 8, CharSet = CharSet.Ansi)]
    public struct RECT
    {
        public Int32 left;
        public Int32 top;
        public Int32 right;
        public Int32 bottom;
    }

    public enum ConversionType
    {
        CONVERSIONTYPE_NO_CONVERSION = 0,
        CONVERSIONTYPE_CONVERT_TO_BW = 1,
        CONVERSIONTYPE_CONVERT_TO_GREY = 2
    }

    public class ImageWizHelper
    {
        const string DLL_NAME = "ImageWizHelper.dll";

        [DllImport("ImageWizHelper.dll", SetLastError = true, CharSet = CharSet.Ansi)]
        static extern IntPtr Initialize(String LogFileName);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CharSet = CharSet.Ansi)]
        static extern Int32 Terminate(IntPtr ImageWizHelper);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CharSet = CharSet.Ansi)]
        static extern Int32 GetLastErrorCode();

        [DllImport("ImageWizHelper.dll", SetLastError = true, CharSet = CharSet.Ansi)]
        static extern Int32 GetErrorDescription(Int32 error, StringBuilder desc_buffer, ref Int32 desc_len);//StringBuilder desc_buffer

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        static extern Int32 UnlockWithAppName(IntPtr licData, String app_name, StringBuilder err_buff);
        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        static extern Int32 UnlockWithModuleHandle(IntPtr ImageWizHelper, String Lic_file_name, String err_buff);

        [DllImport("ImageWizHelper.dll", CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 GetPageCountInImageFile(IntPtr ImageWizHelper, string filename, string pwd);

        [DllImport("ImageWizHelper.dll", CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 getImageQuality(IntPtr ImageWizHelper, IntPtr objQuality);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 CompressToTIFF(IntPtr ImageWizHelper, String[] InFileName, Int32 InFileCount, String OutputFileName, ResetOption Option);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 CompressToPDF(IntPtr ImageWizHelper, String[] InFileName, Int32 InFileCount, String OutputFileName, ResetOption Option);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 CompressToJpeg(IntPtr ImageWizHelper, String InFileName, String OutputFileName, ResetOption Option);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 SetDPI(IntPtr ImageWizHelper, ImageDPI Dpi);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 SetImageQuality(IntPtr ImageWizHelper, ImageQuality Quality);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern void EnableExifRotation(IntPtr ImageWizHelper);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern void DisableExifRotation(IntPtr ImageWizHelper);

        [DllImport("ImageWizHelper.dll", SetLastError = true, CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 GetErrorDescription(Int32 ErrorValue, StringBuilder ErrorDesc, Int32 MaxLen);

        [DllImport("ImageWizHelper.dll", CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 GetMaskImage(IntPtr ImageWizHelper, String FileName, String OutputFileName, Int32 PageNum, [MarshalAs(UnmanagedType.LPArray)] RECT[] RectData, Int32 RectCount, Int32 maskValue);

        [DllImport("ImageWizHelper.dll", CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 CompressPagesToTiff_Array(IntPtr ImageWizHelper, String InputFileName, String OutputFileName, Int32[] PageArray, Int32 PageArrayCount, Boolean Append, ResetOption Option);

        [DllImport("ImageWizHelper.dll", CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 CompressPagesToTiff_Range(IntPtr ImageWizHelper, String InputFileName, String OutputFileName, Int32 StartPageNum, Int32 EndPageNum, Boolean Append, ResetOption Option);

        [DllImport("ImageWizHelper.dll", CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 SetConversion(IntPtr ImageWizHelper, Int32 Conversion);

        [DllImport("ImageWizHelper.dll", CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 GetConversion(IntPtr ImageWizHelper, ref Int32 Conversion);

        [DllImport("ImageWizHelper.dll", CallingConvention = CallingConvention.StdCall, CharSet = CharSet.Ansi)]
        private static extern Int32 AppendToTiff(IntPtr ImageWizHelper, String InputFile, String OutPutFile, ResetOption Option);

        //Win API def
        [DllImport("kernel32", SetLastError = true, CharSet = CharSet.Ansi)]
        private static extern IntPtr LoadLibrary([MarshalAs(UnmanagedType.LPStr)]String lpFileName);

        public IntPtr ImageWizHandle { get; set; }

        public String DllName = "ImageWizHelper.dll";
        public ImageWizHelper(String dll_path, IntPtr lic_data, String app_name)
        {
            //Console.WriteLine(CurrentPath);
            String dllPath = Path.Combine(dll_path, DllName);//****Here We Are Taking The Dll Path From Where We Have Plased The Dll. Full Path With Name.
            String LogFileName = Path.Combine(dll_path, "ImageWizHelper_Error.log");
            if (File.Exists(dllPath))
            {
                //****Here We are Loading The Dll****
                if(LoadLibrary(dllPath) == IntPtr.Zero) throw new Exception("Dll isn't Load Successfully|" + dll_path + "|" + DLL_NAME);
                StringBuilder error_info = new StringBuilder(1024);
                if (UnlockLibrary(lic_data, app_name, error_info) != 0)//unlock library using input < License data(IntPtr), application name(string), error_info(stringbuilder)>.
                {
                    if (error_info.Length > 0)
                        throw new Exception(error_info.ToString());
                }
                ImageWizHandle = Initialize(LogFileName); //Initilizing the Handle of ImageWizHelper
                if (ImageWizHandle == IntPtr.Zero) throw new Exception("unable to load memory");
            }
        }
        public void UninitializeHelper()
        {
            if (ImageWizHandle != IntPtr.Zero)
            {
                Terminate(ImageWizHandle);
                ImageWizHandle = IntPtr.Zero;
            }
        }
        public Int32 GetPageCount(string filename, string pwd)
        {
            return GetPageCountInImageFile(ImageWizHandle, filename, pwd);
        }

        public Int32 getErrorDescription(Int32 error, StringBuilder error_buff, Int32 len)
        {
            return GetErrorDescription(error, error_buff, len);
        }
        public Int32 UnlockLibrary(IntPtr licData, String appName, StringBuilder err_buff)
        {
            return UnlockWithAppName(licData, appName, err_buff);
        }
        public Int32 CompressAndAddToTIFF(String[] InFileName, String OutputFileName, ResetOption Option)
        {
            return CompressToTIFF(ImageWizHandle, InFileName, InFileName.Length, OutputFileName, Option);
        }
        public int AppendToTIFF(String inputFile, String outputFile, ResetOption resetOption)
        { 
            return AppendToTiff(ImageWizHandle, inputFile, outputFile, ResetOption.ResetAllDPI);
        }
        public Int32 CompressAndAddToPDF(String[] InFileName, String outputFile, ResetOption Option)
        {
            return CompressToPDF(ImageWizHandle, InFileName, InFileName.Length, outputFile, Option);
        }
        public Int32 CompressAndAddToJPG(String[] InFileName, String outputFile, ResetOption Option)
        {
            return CompressToPDF(ImageWizHandle, InFileName, InFileName.Length, outputFile, Option);
        }
        public Int32 GetMaskedImage(String inputFileName, String outputFile, Int32 PageNum, RECT[] RectData)
        {
            return GetMaskImage(ImageWizHandle, inputFileName, outputFile, PageNum, RectData, RectData.Length, 1);
        }

        public Int32 CompressToTiff_By_PageArray(String inputFileName, String outputFile, Int32[] PageArray, Boolean Append, ResetOption option)
        {
            return CompressPagesToTiff_Array(ImageWizHandle, inputFileName, outputFile, PageArray, PageArray.Length, Append, option);
        }

        public Int32 CompressToTiff_By_PageRange(String inputFileName, String outputFileName, Int32 startPage, Int32 EndPage, Boolean Append, ResetOption option = ResetOption.ResetAllDPI)
        {
            return CompressPagesToTiff_Range(ImageWizHandle, inputFileName, outputFileName, startPage, EndPage, Append, option);
        }

        public Int32 AppentTo_Tiff(String InputFile, String OutputFile, ResetOption option)
        {
            return AppendToTiff(ImageWizHandle, InputFile, OutputFile, option);
        }
        public Int32 SetConversion(ConversionType convertionType)
        {
            return SetConversion(ImageWizHandle, (Int32)convertionType);
        }
        public Int32 SetUpDPI(ImageDPI DPI)
        {
            return SetDPI(ImageWizHandle, DPI);
        }

        public Int32 SetupBPP(Int32 ConvType)
        {
            return SetConversion(ImageWizHandle, ConvType);
        }

        public ImageQuality GetImageQuality()
        {
            IntPtr objquality;
            objquality = new IntPtr();

            int ret = getImageQuality(ImageWizHandle, objquality);

            if (ret != 0) { return ImageQuality.unknown; }

            return ImageQuality.Photo_Quality;

        }
        public Int32 GetConversionType()
        {
            Int32 ConvType = 0;
            GetConversion(ImageWizHandle, ref ConvType);
            return ConvType;
        }

        public Int32 TerminateHandle(IntPtr Handle)
        {
            return Terminate(Handle);
        }
    }
}
