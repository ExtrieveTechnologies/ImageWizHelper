package ImageHelperTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import com.extrieve.imaging.sdk.ImageWizHelperJNI.ResetOption;

public class Main {
    
    static String InputFile = null;
    static String OutputFile = null;
    static String app_name = null;
    static String lic_data = null;
    static String compression_type = null;
    // function for read license data from license file. 
    static  String readLicData(String licPath) throws Exception {
        
        String mLine;
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        
        try {
            reader = new BufferedReader(new FileReader(licPath));
            while ((mLine = reader.readLine()) != null) {
                builder.append(mLine);
            }
        }catch(Exception ex) {
            throw new RuntimeException("Unable to Read Licence File");
        }
        finally {
            if (reader != null) reader.close();
        }
        
        return (builder.length() > 1) ? builder.toString() : null;
    }
    
    static void DefineUsage()
    {
    	System.out.print("java -jar" + "-I <Input File>" + "-O <Output file>" + "-AN <App Name>" + "[-CT " + "<PDF"+"/TIFF" + "/JPG>]");
    }
    
    //function for read the input file, output file and app_name from command line argument.
    static boolean ReadArguments(String args[])
    {
        int index = 0;
        do
        {
            if (index >= args.length)
                break;

            switch (args[index].toUpperCase())
            {
            case "-I":
                if (index == args.length)
                {
                    return false;
                }
                index++;
                InputFile = args[index];
                break;
            case "-O":
                if (index == args.length)
                {
                    return false;
                }
                index++;
                OutputFile = args[index];
                break;
            case "-AN":
                if(index == args.length)
                {
                    break;
                }
                index++;
                app_name = args[index];
                break;
            case  "-CT":
                if(index == args.length)
                {
                    break;
                }
                index++;
                compression_type = args[index];
                break;
            }
            index++;
        } while (true);
        if(compression_type == null)//default compression type
        {
            compression_type = "TIFF";
        }
        return true;
    }
    
    //function for compress the pdf image file into tiff image file.
    static boolean PdfToTiff(ImageWizHelperJNI objWizHelper) {
    
        int ret;

        if (InputFile == null) 
        {
            throw new RuntimeException("Input file name not provided");
        }

        if (OutputFile == null) 
        {
            throw new RuntimeException("Output file name not provided");
        }
        
        ret=objWizHelper.CompressToTIFF(new String[] { InputFile }, OutputFile, ResetOption.No_DPI_change); //compress Input File to TIFF.
        if (ret != 0)
        {
            throw new RuntimeException(objWizHelper.GetErrorDescription(ret));
        }

        return true;
    }
    
    //function for compress the tiff image file into pdf image file.
    static boolean TiffToPdf(ImageWizHelperJNI objWizHelper) {
    
        int ret;

        if (InputFile == null) 
        {
            throw new RuntimeException("Input file name not provided");
        }

        if (OutputFile == null) 
        {
            throw new RuntimeException("Output file name not provided");
        }
        
        ret=objWizHelper.CompressToPDF(new String[] { InputFile }, OutputFile, ResetOption.No_DPI_change); //compress Input File to PDF.
        if (ret != 0)
        {
            throw new RuntimeException(objWizHelper.GetErrorDescription(ret)); // throw exception based on the error code
        }

        return true;
    }
    
    //function for compress the image file into jpg file.
    static boolean CompressToJpg(ImageWizHelperJNI objWizHelper) {
        
        int ret;

        if (InputFile == null) 
        {
            throw new RuntimeException("Input file name not provided");
        }

        if (OutputFile == null) 
        {
            throw new RuntimeException("Output file name not provided");
        }
        
        ret=objWizHelper.CompressToJPEG(new String[] { InputFile }, OutputFile, ResetOption.No_DPI_change); //compress Input File to PDF.
        if (ret != 0)
        {
            throw new RuntimeException(objWizHelper.GetErrorDescription(ret)); // throw exception based on the error code
        }

        return true;
    }
    
    
    
    public static void main(String args[]) throws Exception {

        try {
            
            boolean ret;
            String Curr_dir = System.getProperty("user.dir");
            String lic_path = Curr_dir + File.separator + "waves_imaging_new" + ".lic"; //license path for read the license file.
            lic_data = readLicData(lic_path); //read license data

            if (!ReadArguments(args))
            {
                throw new RuntimeException("failed to read arguments");
            }
            
            if (app_name == null) throw new RuntimeException("Inavlid app_name");
            if (lic_data == null) throw new RuntimeException("license data not found");
            
            ImageWizHelperJNI ObjImageWizHelperJNI = new ImageWizHelperJNI(Curr_dir, lic_data, app_name);//ObjImageWizHelperJNI is object of ImageWizHelperJNI class.
            switch(compression_type) 
            {
            case "tiff":
            case "tif":
                
            case "TIFF":
            case "TIF":    
                ret=PdfToTiff(ObjImageWizHelperJNI);//called function create PDF to TIFF.
                if(ret==true)
                    System.out.println("Tiff file Successfully Generated.....");
                else {
                    System.out.println("Usage: \n\tjava -jar ImageWizHelper.jar -i 'Input File Path' -o 'Output File Path'");
                }
                break;
                
            case "pdf":
            case "PDF":
                ret=TiffToPdf(ObjImageWizHelperJNI);//called function create tiff to PDF.
                if(ret==true)
                    System.out.println("Pdf file Successfully Generated.....");
                else {
                    System.out.println("Usage: \n\tjava -jar ImageWizHelper.jar -i 'Input File Path' -o 'Output File Path'");
                }
                break;
            case "jpg":
            case "JPG":
                ret=CompressToJpg(ObjImageWizHelperJNI);//called function create image file to JPG file.
                if(ret==true)
                    System.out.println("Jpg file Successfully Generated.....");
                else {
                    System.out.println("Usage: \n\tjava -jar ImageWizHelper.jar -i 'Input File Path' -o 'Output File Path'");
                }
                break;
            }
            
        } catch (Exception Ex) {
            System.out.println(Ex.toString());
            
            DefineUsage();
        }
        finally {
            System.exit(0);
        }

    }
}
