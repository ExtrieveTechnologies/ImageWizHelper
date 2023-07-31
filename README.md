# ImageWizHelper - Imaging SDK from Extrieve
<img src="https://raw.githubusercontent.com/ExtrieveTechnologies/ImageWizHelper/main/ImageWizHelper_v2.gif" alt="img-verification" autoplay>

> **Developer-friendly** & **Easy to integration** SDK.

Download
--------
You can download the SDK file from GitHub's [releases page](https://github.com/ExtrieveTechnologies/QuickCapture_Android/releases/) and add the file dependency manually into the project/app.


Compatibility
-------------
Currently the DLL API's are available in:
 * **C**
 * **C++**
 * **Java**

Currently the **ImageWizHelper DLL** usage is from C, VB and C#

For the Java Interface we use a **ImageWizHelper JNI DLL**
 

C# usage
-------------

A wrapper class called ImageWizHelperAPI is provided which can be used for C#. DLL has to be 
initialized first before use. Following is the sample code for the initialization of the DLL

```C#
//C#
ImageWizHelperAPI obj = new ImageWizHelperAPI();
obj.InitializeWizHelper(local_bin_path, local_error_log_path);
```
This function should be called once per thread by the application. If the application is used in a multithreaded
context then each thread should initialize a different ImageWizHelperAPI object and initialize once before using 
it.

**local_error_log_path** - This path is optional. Pass a path with write access for the application. This will 
create log files which can be used for debugging any issues. If this is not passed then loggin will be disabled.

```C#
//C#
obj.TerminateWizHelper();
```
This object reference will be used for all the imaging functionalities.

## Functionalties

1. **CompressToTiff**	   -	*This function compresses multiple Images to a single tiff file.* 
2. **CompressToTiff**	   -	*This function compresses multiple images to a single PDF file*
3. **CompressToJpeg**	   -	*This function converts a single image file to a JEPG file* 
4. **AppendToTiffImage** -	*This function appends a single image file over an existing Tiff file*

C/C++ Initialization
-------------
**1. Initialize** - *This function is used to initialize the DLL and validate the license. This function should
   be called once per thread by the application. If DLL is used in a multithreaded context each thread should maintain a different 
   handle.*
     
   ``` C/ C++
   //C/C++
   HANDLE WINAPI Initialize (char *Logpath)
   ```
   **Parameter Name**
   - **Logpath** - *Pass a path with write acccess to the application. Debug logs are created in this path. This is optional parameter.
      Debugging is not required then this can be kept as null.*

**2. Terminate** - *Each initialized handle should be terminated using this function.*

   ```C / C++
   //C/C++
   int WINAPI Terminate(HANDLE ImgWizHlpHandle)
   ```
    
   **Parameter Name**
   - **ImgWizHlpHandle** - *Handle created during initialization*

C/C++ & Java usage
------------------

**1. CompressToTiff** - *This function will take array of input files and will create a single Tif output file. Support input as
   array of JPEG, PNG, bmp, & TIFF. By default DLL will select 200 as the standard DPI and A4 as the page size. It is recommended to
   keep a minimum of 150 as DPi to avoid Quality issues. Formats like DJvu, JBIG2 an data PDF are not supported. If input format is not
   supported it will return false.*
   
   ```C / C++
   //C/C++
   INT32 WINAPI CompressToTIFF(HANDLE ImgWizHlpHandle, char **InputFile, INT32 InputFileCount, char *Output_Filename , INT32 option )

   //ImgWizHlpHandle - Handle created using initialization.
   //InputFile       - Array on input files. In case of multipage TIFF all pages will be considered as input. This should be with full path.
   //InputFileCount  - Number of files.
   //Output_Filename - Expected output file name with directory.
   //option          - Following are the possible options.
   
   //No_DPI_change = 0 NO
   //ResetAllDPI = 1
   //ResetZeroDPI = 2
   ```

   ```Java
   //Java
   public int CompressToTIFF(String[] inputFiles, String outputFile, ResetOption resetOption)

   //inputFiles  - Array on input files. In case of multipage TIFF all pages will be considered as input. This should be with full path.
   //outputFile  - Expected output file with directory.
   //resetOption - Following are the possible options: -
   
   //No_DPI_change(0), DPI will not be resetted this case . 
   //ResetAllDPI(1), Every image DPI will be resetted to selected DPI.  Dimension also will be changed according to DPI
   //ResetZeroDPI(2), If DPI is not available then DPI will setted for the image.  Dimension also will be changed according to DPI
   ```

**2. CompressToPDF** - *This function will take an array of input files and create a single PDF output file. Support input as an array of JPEG, PNG, BMP, & TIFF. By 
   default, DLL will select 200 as the standard DPI & A4 as the page size. It is recommended to keep a minimum of 150 DPI to avoid quality issues. Formats like DJvu, 
   JBIG2, and data PDF are not supported. If input format is not supported it will return false.*
   
   ```C / C++
   //C/C++
   int WINAPI CompressToPDF (HANDLE ImgWizHlpHandle, char **InputFile, INT32 InputFileCount, char
   *Output_Filename, int option )
   ```
   ```Java
   //Java
   public int CompressToPDF(String[] inputFiles, String outputFile, ResetOption resetOption) 
   ```
 
   **Parameters**
   
   - **ImgWizHlpHandle** - *Handle created using initialization*
   - **InputFile**       - *Array on input files. In the case of multipage TIFF, all pages will be considered as input. This should be with full path.*
   - **InputFileCount**  - *Number of files.*
   - **Output_Filename** - *Expected output file name with directory.*
   - **option** - *Following are the possible options: -*
   ```C / C++
   //C/C++
   No_DPI_change = 0 NO
   ResetAllDPI = 1
   ResetZeroDPI = 2
   ```
   - *If only compression is to be performed then pass 0 as the option.*
   - *If all images have to be resized to the standard page size then use **ResetAllDPI** option*
   - *If only mobile captured images are to be resized then keep **ResetZeroDPI** as the parameter.*

**3. CompressToJpeg** - *This function will compress as a single input file and create a Jpeg output file.*

   ```C / C++
   //C/C++
   int WINAPI CompressToJpeg(HANDLE ImgWizHlpHandle , char **InputFile , char *Output_Directory, int
   option)
   ```
   ```Java
   //Java
   public int CompressToJPEG(String[] inputFile, String outPutFile, ResetOption resetOption)
   ```

   **Parameters**
   
   - **ImgWizHlpHandle**  - *Handle created using initialization*
   - **InputFile**        - *Array on input files. In the case of multipage TIFF, all pages will be considered as input. This should be with full path.*
   - **Output_Directory** - *Expected output directory.*
   - **option**           - *Following are the possible options: -*
   ```C / C++
   //C/C++
   No_DPI_change = 0 NO
   ResetAllDPI = 1
   ResetZeroDPI = 2
   ```
   *If only compression is to be performed then pass 0 as the option.*
   *If all images have to be resized to the standard page size then use **ResetAllDPI** option*
   *If only mobile captured images are to be resized then keep **ResetZeroDPI** as the parameter.*

**4. AppendToTiff** - *This function will append a tiff image over an existing tiff image.*

   ```C / C++
   //C/C++
   int WINAPI AppendToTiff(HANDLE ImgWizHlpHandle , char *InputFile , char *OutputFile, int option)
   ```
   ```Java
   //Java
   public int AppendToTIFF(String inputFile, String outputFile,ResetOption resetOption)
   ```

   **Parameters**
   
   - **ImgWizHlpHandle** - *Handle created using initialization*
   - **InputFile**       - *Array on input files. In the case of multipage TIFF, all pages will be considered as input. This should be with full path.*
   - **Output_File**     - *Expected output file name with directory.*
   - **option**          - *Following are the possible options: -*
   ```C / C++
   //C/C++
   No_DPI_change = 0 NO
   ResetAllDPI = 1
   ResetZeroDPI = 2
   ```
   - *If only compression is to be performed then pass 0 as the option.*
   - *If all images have to be resized to the standard page size then use **ResetAllDPI** option*
   - *If only mobile captured images are to be resized then keep **ResetZeroDPI** as the parameter.*

**5. OCRBWConversionToTiff** - This function will take single input files and create a Black and White output file. Which will be mainly helpful
   for OCR purposes.The supported input file of type JPEg, BMP, and TIFF. By default, DLL will select 200 as the standard DPI & A4 as the page size.
   It is recommended to keep a minimum of 150 as DPI to avoid Quality issues. Formats like DJvu, JBIG2, and data PDF are not supported. If input format
   is not supported it will return false.

   ```C / C++
   //C/C++
   INT32 OCRBWConversionToTiff(HANDLE ImgWizHlpHandle, char *InputFile, INT32 PageNo, char*Output_Filename , INT32 option )
   ```
   ```Java
   //Java
   public int OcrBWConversionToTiff(String inputFile, int pageNumber, String outputFileName, ResetOption resetOption)
   ```

   **Parameters**
   
   - **ImgWizHlpHandle** - *Handle created using initialization*
   - **InputFile**       - *Single input file Image or PDF*
   - **PageNo**          - *Page Number of the input file. This is mainly required for input file type TIFF and PDF. For other types of images, we can send 0*
   - **Output_Filename** - *Expected output file name with directory.*
   - **option**          - *Following are the possible options: -*
   ```C / C++
   //C/C++
   No_DPI_change = 0 NO
   ResetAllDPI = 1
   ResetZeroDPI = 2
   ```
   - *If only compression is to be performed then pass 0 as the option.*
   - *If all images have to be resized to the standard page size then use **ResetAllDPI** option*
   - *If only mobile captured images are to be resized then keep **ResetZeroDPI** as the parameter.*

**6. SetLogFile** - This function will initiate the log dumping of the dll usage.

   ```C / C++
   //C/C++
   int WINAPI SetLogFile(HANDLE ImgWizHlpHandle , char *error_log_file, int log_level, int *error)
   ```

   **Parameters**

   - **ImgWizHlpHandle** - *Handle created using initialization.*
   - **error_log_file**  - *Error log file name with full path*
   - **log_level**       - *Has 3 log values: -*
                           *0 - App run logging*
                           *1 - App debug logging*
                           *2 - Full Debug logging*
   - **error**           - This will return the error for logging initialization.

**7. SetPageLayout** - *By default DLL will create output as A4 layout. This parameter can be used to change the type of Layout for the output creation. 
   If the input file is smaller than the layout size then the DLL will not increase the size, as this will reduce the quality of the output image.* Also this DLL
   will maintain the aspect ratio of the original Image.

   ```C / C++
   //C/C++
   int WINAPI SetPageLayout(HANDLE ImgWizHlpHandle, LayoutType Page)
   ```
   ```Java
   //Java 
   public int SetPageLayout(Layout layout)
   ```
   
   Following are the different Layout types supported

   ```C / C++
   //C/C++
   typedef enum
   {
    A0,
    A1,
    A2,
    A3,
    A4,
    A5,
    A6,
    A7
   } LayoutType;
   ```
   ```Java
   //Java
   Unknown(-1),
   A0(0),
   A1(1),
   A2(2),
   A3(3),
   A4(4),
   A5(5),
   A6(6),
   A7(7);
   ```
 
**8. GetPageLayout** - *This will return the existing page setup.*
      
  ```C / C++
  //C/C++
  int WINAPI GetPageLayout(HANDLE ImgWizHlpHandle, int *Height, int *Width)
  ```
  ```Java
  //Java
  public Layout GetPageLayouWidth( ) 
  ```
  ```Java
  //Java
  public Layout GetPageLayoutHeight( )
  ```

**9. SetDPI** - *By default DLL will use 200 DPI as the output DPI. This parameter can be used to change the DPI.*
     
  ```C / C++
  //C/C++
  int WINAPI SetDPI(HANDLE ImgWizHlpHandle, DPI dpi)
  ```
  ```Java
  public int SetDPI(ImageDPI imageDPI)
  ```

  **Following are the possible DPI supported**
    
  ```C / C++
  //C/C++
  typedef enum
  {
    DPI_100 = 100,
    DPI_150 = 150,
    DPI_200 = 200,
    DPI_300 = 300,
    DPI_500 = 500,
    DPI_600 = 600
  } DPI;
  ```
  ```Java
  //Java
  Unknown(-1),
  A0(0),
  A1(1),
  A2(2),
  A3(3),
  A4(4),
  A5(5),
  A6(6),
  A7(7); 
  ``` 

**10. GetDPI** - *This function will return the existing DPi setup.*

```C / C++
//C/C++
int WINAPI GetDPI(HANDLE ImgWizHlpHandle, int *dpi)
```
```Java
//Java
public ImageDPI GetDPI()
```

**11. SetImageQuality** - *By default the Quality is set as Document_Quality. This API is used to reset the output Quality*

```C / C++
//C/C++
int WINAPI SetImageQuality(HANDLE ImgWizHlpHandle, ImageQuality Quality)
```
```Java
//Java
public int SetImageQuality(ImageQuality imageQuality)
```
```C / C++
//C/C++
ImageQuality
{
  Photo_Quality = 0;
  Document_Quality = 1;
  Compressed_Document = 2; 
}
```
```Java
//Java
Unknown(-1),
Photo_Quality(0),
Document_Quality(1),
Compressed_Document(2);
```

**Following are the recommended qualities:**
- **Photo_Quality**       - *To be used when higher Quality is required.*
- **Document_Quality**    - *Recommended Default quality.*
- **Compressed_Document** - *To be used when higher compression is required. But may degrade the image quality*

**12. GetImageQuality** - *This function will return the existing image quality setup.*

```C / C++
//C/C++
int WINAPI GetImageQuality(HANDLE ImgWizHlpHandle, int *Quality)
```
```Java
public ImageQuality GetImageQuality()
```
    
**13. SetConversion** - *By default DLL will use no conversion for the output file. This parameter can be used to change the conversion.*

```C / C++
//C/C++
int WINAPI SetImageQuality(HANDLE ImgWizHlpHandle, ImageQuality Quality)
```
```Java
//Java
public int SetConversion(ConversionType convertionType)
```

**Following are the possible supported conversion:**

```C / C++
//C/C++
typedef enum
{
  No_Conversion,
  Convert_To_BW,
  Convert_To_Grey
} ConversionType;
```

```Java
//Java
public enum ConversionType
{
  Unknown(-1),
  No_Conversion(0),
  Convert_To_BW(1),
  Convert_To_Grey(2);
}
```

**14. GetConversion** - This function will return the existing conversion setup.
     
```C / C++
//C/C++
int WINAPI GetConversion(HANDLE ImgWizHlpHandle, ConvertionType Conversion)
```

```Java
//Java
public ConversionType GetConversion()
```
    
**15. CompressPagesToTiff_Array** - *This function will take a single input file and take the file's pages in an array to compress those pages in a
single output.*

It will also take an ArrayLenght of pages also.

It is recommended to keep a minimum of 150 as DPI to avoid Quality issues. Formats like DJvu, JBIG2, and data PDF are not supported. If input
format is not supported it will not return to 0.

For successful compression, it will return 0.

```C / C++
//C/C++
INT32 WINAPI CompressPagesToTiff_Array(HANDLE ImgWizHlpHandle, char *InputFile, char *OutputFile,INT32 *PageArray,
INT32 PageArrayCount, BOOL Append, INT32 option)
```

```Java
//Java
public int CompressPagesToTiff_Array(String inputFile, String outputFile, int[] pageArray, boolean append,ResetOption resetOption)
```

**Parameters**

- **ImgWizHlpHandle** - *Handle created using initialization.*
- **InputFile**       - *Single input file Image.*
- **OutputFile**      - *Expected output file name with directory*
- **PageArray**       - *Expected page number in an array to compress.*
- **PageArrayCount**  - *Array length*
- **Append**          - *Boolean value (true/false), which helps to use a single output file while Multiple Compression operations.*
- **option**          - *Following are the possible options: -*
    
```C / C++
//C/C++
No_DPI_change = 0 NO
ResetAllDPI = 1
ResetZeroDPI = 2
```
 
- *If only compression is to be performed then pass 0 as the option.*
- *If all images have to be resized to the standard page size then use **ResetAllDPI** option*
- *If only mobile captured images are to be resized then keep **ResetZeroDPI** as the parameter.*
  

**16. GetErrorDescription** - This method will return the error string for a specific error code.


**Error Description for respective error code return**

  - ERR_INAVLID_EDITOR_HANDLE = **1** // "Invalid Editor Handle"
  - ERR_INVALID_IMAGE_HANDLE = **2** // "Invalid Image Handle"
  - ERR_INVALID_MULTIIMG_HANDLE = **3** // "Invalid MultiImage Handle"
  - ERR_NO_MEMORY = 12 // "Memory is not available"
  - ERR_FILE_OPEN = 13 // "File open error"
  - ERR_FILE_WRITE = 14 // "File writing error"
  - ERR_FILE_ACCESS = 15 // "File access error"
  - ERR_FILE_NOT_FOUND = 16 // "File Not Found"
  - ERR_FILE_ALREADY_EXIST = 17 // "File Already Exists"
  - ERR_INVAL_PARAM = 18 // "Invalid input parameter"
  - ERR_PAGE_NUMBER = 19 // "Invalid Page Number"
  - ERR_FMT_NOT_SUPPORTED =20 // "Invalid input file format"
  - ERR_DATA_PDF = 21 // "Input PDF file is data PDF file"
  - ERR_PDF_READING = 22 // "Error in PDF Reading"
  - ERR_PDF_WRITING = 23 // "Error in PDF writing"
  - ERR_LIMAGING = 24 // "Error from Image Library"
  - ERR_EXIF_FAILED = 25 // "Error in reading exif information"

     
