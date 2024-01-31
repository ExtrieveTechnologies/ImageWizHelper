using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.InteropServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;

namespace ImageWizHelperDemo
{
    public class Program
    {
        public static String InputFile { get; set; }// decalre input file to store the input image path. 
        public static String OutputFile { get; set; }//decalre output file to store the output image path with file name and extension
        public static String lic_data { get; set; }//decalre license data  to store the license data.
        public static String app_name { get; set; }//decalre app name to store the current application name calling the ImageWizHelper DLL.
        public static String compression_type { get; set; }//decalre compression type to store the compression format "TIFF"/"PDF"/"JPG"

        //function for read argument 
        static Boolean ReadArguments(String[] args)
        {
            int index = 0;
            if (args.Length == 0)//**checking have any argument or not
            {
                DefineUsage();
                return false;
            }
            do
            {
                if (index == args.Length)
                    break;

                switch (args[index].ToUpper())
                {
                    case "-I":
                        if (index == args.Length) return false;
                        index++;
                        InputFile = args[index];
                        break;
                    case "-O":
                        if (index == args.Length) return false;
                        index++;
                        OutputFile = args[index];
                        break;
                    case "-AN":
                        if (index == args.Length) return false;
                        index++;
                        app_name = args[index];
                        break;
                    case "-CT":
                        if (index == args.Length) return false;
                        index++;
                        compression_type = args[index];
                        break;
                }
                index++;
            } while (true);
            if (compression_type == null)//default compression type
            {
                compression_type = "TIFF";
            }
            return true;
        }

        //this fuction will call when argument is null.
        static void DefineUsage()
        {
            Console.WriteLine("file.exe" + "-I <Input File>" + "-O <Output file>" + "-AN <App Name>" + "-CT " + "<[PDF" + "/TIFF" + "/JPG]>");//Message of Input format to run the exe.
        }


        //Reading license data for unlock the library
        static IntPtr readLicData(String licPath)
        {
            IntPtr ReadData = IntPtr.Zero;
            try
            {
                if (!File.Exists(licPath)) return IntPtr.Zero;
                String Data = File.ReadAllText(licPath); // reading the license file data using licPath.
                ReadData = Marshal.StringToHGlobalAnsi(Data); // convert the data to IntPtr.
            }
            catch (Exception ex)
            {
                throw new Exception(ex.ToString());
            }
            return ReadData;
        }

        //This Function is used when we choose the Default options.
        static Boolean PDFtoTiff(ImageWizHelper ObjimageWizHelper)
        {
            int len = 1024, ret;
            StringBuilder error_buff = new StringBuilder(len); // declare the varible of string buider for getting error description. 

            if (InputFile == null)// checking input file is null.
            {
                throw new Exception("Input file name not provided");
            }

            if (OutputFile == null) // checking output is null
            {
                throw new Exception("Output file name not provided");
            }
            ret = ObjimageWizHelper.CompressAndAddToTIFF(new String[] { InputFile }, OutputFile, ResetOption.ResetAllDPI); //compress pdf to tiff using argument <string array>(inputfile), string(output file), resetoption(enum defined in the ImageWizHelper Interface).
            if (ret != 0)
            {
                Int32 err = (ObjimageWizHelper.getErrorDescription(ret, error_buff, len));//get error desc from error code(ret),error_buff(stringBuilder) and length of StringBuilder.
                if (!String.IsNullOrEmpty(error_buff.ToString())) // checking error_buff have some value or not.
                {
                    throw new Exception(error_buff.ToString());
                }
            }
            return true;
        }

        //function for compress the tiff file into pdf file.
        static Boolean TifftoPdf(ImageWizHelper ObjimageWizHelper)
        {
            int len = 1024, ret;
            StringBuilder error_buff = new StringBuilder(len); // declare the varible of string buider for getting error description. 

            if (InputFile == null)  // checking input file is null.
            {
                throw new Exception("Input file name not provided");
            }

            if (OutputFile == null) // checking output file is null.
            {
                throw new Exception("Output file name not provided");
            }
            ret = ObjimageWizHelper.CompressAndAddToPDF(new String[] { InputFile }, OutputFile, ResetOption.ResetAllDPI); //compress tiff to pdf using argument <string array>(inputfile), string(output file), resetoption(enum defined in the ImageWizHelper Interface). 
            if (ret != 0)
            {
                Int32 err = (ObjimageWizHelper.getErrorDescription(ret, error_buff, len));//get error desc from error code(ret),error_buff(stringBuilder) and length of StringBuilder.
                if (!String.IsNullOrEmpty(error_buff.ToString())) // checking error_buff have some value or not.
                {
                    throw new Exception(error_buff.ToString());
                }
            }
            return true;
        }

        //function for compress the image file into jpg file.
        static Boolean CompressToJpg(ImageWizHelper ObjimageWizHelper)
        {
            int ret, len = 1024;
            StringBuilder error_buff = new StringBuilder(len); // declare the varible of string buider for getting error description. 

            if (InputFile == null) // checking input file is null.
            {
                throw new Exception("Input file name not provided");
            }

            if (OutputFile == null) // checking input file is null.
            {
                throw new Exception("Output file name not provided");
            }
            ret = ObjimageWizHelper.CompressAndAddToJPG(new String[] { InputFile }, OutputFile, ResetOption.ResetAllDPI); //compress image to jpg file using argument <string array>(inputfile), string(output file), resetoption(enum defined in the ImageWizHelper Interface).
            if (ret != 0)
            {
                Int32 err = (ObjimageWizHelper.getErrorDescription(ret, error_buff, len));//get error desc from error code(ret), error_buff(stringBuilder) and length of StringBuilder.
                if (!String.IsNullOrEmpty(error_buff.ToString())) // checking error_buff have some value or not.
                {
                    throw new Exception(error_buff.ToString());
                }
            }
            return true;
        }


        static void Main(string[] args)
        {
            IntPtr Handle = IntPtr.Zero; // declare handle
            Boolean ret;
            String CurrentPath = AppDomain.CurrentDomain.BaseDirectory; //getting exe directory path
            String licPath = Path.Combine(CurrentPath, "waves_imaging_new" + ".lic"); // path of license data.
            IntPtr lic_data = readLicData(licPath);   // here i calling for read the license data.
            StringBuilder err_desc = new StringBuilder(0);
            ImageWizHelper ObjimageWizHelper = null; //create a object of ImageWizHelper class.
            try
            {
                if (!ReadArguments(args))
                {
                    throw new Exception("Failed to read argument");
                }
                if (String.IsNullOrEmpty(app_name)) throw new Exception("Invalid app name"); // checking app name is not empty.
                if (lic_data == IntPtr.Zero) throw new Exception("license data not found"); // checking license data is not empty.

                //object of constructor ImageWizHelper.
                ObjimageWizHelper = new ImageWizHelper(CurrentPath, lic_data, app_name); // Assigning object of the ImageWizHelper class. This same object will be used for calling every funtionalities of ImageWizHelper.
                switch (compression_type)
                {
                    case "TIFF":
                    case "TIF":
                    case "tif":
                    case "tiff":
                        ret = PDFtoTiff(ObjimageWizHelper); // call the function for compress the pdf to tiff.
                        if (ret) Console.Write("Tiff file Successfully Generated....."); // output type if compression successfull.
                        break;
                    case "PDF":
                    case "pdf":
                        ret = TifftoPdf(ObjimageWizHelper); // call the function for compress the tiff to pdf.
                        if (ret) Console.Write("Pdf file Successfully Generated.....");// output type if compression successfull.
                        break;
                    case "JPG":
                    case "jpg":
                        ret = CompressToJpg(ObjimageWizHelper);  // call the function for compress the image file to jpg file.
                        if (ret) Console.Write("Jpg file Successfully Generated.....");// output type if compression successfull.
                        break;
                }

            }
            catch (Exception ex)
            {
                Console.WriteLine(ex);
                Console.ReadLine();
            }
            finally
            {
                ObjimageWizHelper.TerminateHandle(ObjimageWizHelper.ImageWizHandle);//finally close the image wiz handle
            }
        }
    }
}
