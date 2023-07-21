# ImageWizHelper
<img src="https://raw.githubusercontent.com/ExtrieveTechnologies/ImageWizHelper/main/ImageWizHelper_v2.gif" alt="img-verification" autoplay>
ImageWizHelper - Imaging SDK from Extrieve

<img class="img-fluid" align="center" src="https://github.com/ExtrieveTechnologies/QuickCapture/blob/main/QuickCapture.png?raw=true" width="30%" alt="img-verification">

## Document Scanning-Capture SDK ANDROID v3
QuickCapture Mobile Scanning SDK Specially designed for native ANDROID from [Extrieve](https://www.extrieve.com/).

> It's not "**just**" a scanning SDK. It's a "**document**" scanning/capture SDK evolved with **Best Quality**, **Highest Possible Compression**, **Image Optimisation**, of output document in mind.

> **Developer-friendly** & **Easy to integration** SDK.

*Choose the **right** version that suits your need* :
- [**QuickCapture v2**](https://github.com/ExtrieveTechnologies/QuickCapture_Android/tree/QuickCapture-V2#document-scanning-capture-sdk-android-v2): Optimized capture functionality, designed to be as compact as possible [~ **2 MB**].
- [**QuickCapture v3**](https://github.com/ExtrieveTechnologies/QuickCapture_Android/tree/QuickCapture-V3#document-scanning-capture-sdk-android-v3): Comprehensive & advanced **AI** functionalities, **comparatively bit** larger size [~ **20 MB**].

> **End of support Notice** :  QuickCapture SDK Android **V1** deprecated by Dec. 2022.For any further updates and support, can use **V2**
> which having no major modifications.But with improved funcionalities,feature additions and fixes.

[Refer here for **V2 documentation** and samples](https://github.com/ExtrieveTechnologies/QuickCapture_Android/tree/QuickCapture-V2#mobile-document-scanning-sdk-android-v2)

### Other available platform options
- [iOS](https://github.com/ExtrieveTechnologies/QuickCapture_IOS)
- [Fultter Plugin](https://pub.dev/packages/quickcapture)
- [Web SDK](https://github.com/ExtrieveTechnologies/QuickCapture_WEB)

Download
--------
You can download the **.aar** library file from GitHub's [releases page](https://github.com/ExtrieveTechnologies/QuickCapture_Android/releases/) and add the file dependency manually into the project/app.


Compatibility
-------------
Currently the DLL API's are available in:
 * **C**
 * **C++**
 * **C#**
 

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

1. **CompressToTiff**	 -	*This function compresses multiple Images to a single tiff files.* 
2. **CompressToTiff**	 -	*This function compresses multiple images to a single PDF file*
3. **CompressToJpeg**	 -	*This function converts a sigle image file to JEPG file* 
4. **AppendToTiffImage** -	*This function appends a single image file over an exisitng Tiff file*

C/C++ usage
-------------
1. **Initialize** - *This function is used to initialize the DLL and validate the license. This function should
    be called once per thread by the application. If DLL is used in a multithreaded context each thread should maintain a different 
    handle.*
     
    ``` C/ C++
    //C/C++
    HANDLE WINAPI Initialize (char *Logpath)
    ```
    **Parameter Name**
    - **Logpath** - *Pass a path with write acccess to the application. Debug logs are created in this path. This is optional parameter.
      Debugging is not required then this can be kept as null.*

2. **Terminate** - *Each initialized handle should be terminated using this function.*

    ```C / C++
    //C/C++
    int WINAPI Terminate(HANDLE ImgWizHlpHandle)
    ```
    
    **Parameter Name**
    - **ImgWizHlpHandle** - *Handle created during initialization*

3. **CompressToTiff** - *This function will take array of input files and will create a single Tif output file. Support input as
   array of JPEG, PNG, bmp, & TIFF. By default DLL will select 200 as the standard DPI and A4 as the page size. It is recommended to
   keep a minimum of 150 as DPi to avoid Quality issues. Formats like DJvu, JBIG2 an data PDF are not supported. If input format is not
   supported it will return false.*
   
   ```C/C++
   //C/C++
   INT32 WINAPI CompressToTIFF(HANDLE ImgWizHlpHandle, char **InputFile, INT32 InputFileCount, char
   *Output_Filename , INT32 option )
   ```
   
   **Parameter Name**
   
   - **ImgWizHlpHandle** - *Handle created using initialization*
   - **InputFile** - *Array on input files. In case of multipage TIFF all pages will be considered as input. This should be with full path.*
   - **InputFileCount** - *Number of files.*
   - **Output_Filename** - *Expected output file name with directory.*
   - **option** - *Following are the possible options: -*
   ```C/C++
   //C/C++
   No_DPI_change = 0 NO
   ResetAllDPI = 1
   ResetZeroDPI = 2
   ```
   *If only compression is to be performed then pass 0 as the option.*
   *If all images has to be resized to the standard page size then use **ResetAllDPI** option*
   *If only mobile captured images to be resized then keep **ResetZeroDPI** as the parameter.*

4. **CompressToPDF** - *This function will take array of input files and create a single PDF output file. Support input as array of JPEG,PNG, bmp, & TIFF. By default DLL will select 200 as the standard DPI & A4 as the page size. It is recommended to keep minimum of 150 DPI to avoid quality issues. Formats like DJvu, JBIG2 and data PDF are not suported. If input format is not supported it will return false.*
   
   ```C/C++
   //C/C++
   int WINAPI CompressToPDF (HANDLE ImgWizHlpHandle, char **InputFile, INT32 InputFileCount, char
   *Output_Filename, int option )
   ```
   
   **Parameter Name**
   
   - **ImgWizHlpHandle** - *Handle created using initialization*
   - **InputFile** - *Array on input files. In case of multipage TIFF all pages will be considered as input. This should be with full path.*
   - **InputFileCount** - *Number of files.*
   - **Output_Filename** - *Expected output file name with directory.*
   - **option** - *Following are the possible options: -*
   ```C/C++
   //C/C++
   No_DPI_change = 0 NO
   ResetAllDPI = 1
   ResetZeroDPI = 2
   ```
   *If only compression is to be performed then pass 0 as the option.*
   *If all images has to be resized to the standard page size then use **ResetAllDPI** option*
   *If only mobile captured images to be resized then keep **ResetZeroDPI** as the parameter.*

5. **CompressToJpeg** - *This function will compress as single input file and create a Jpeg output file.*

   ```C/C++
   //C/C++
   int WINAPI CompressToJpeg(HANDLE ImgWizHlpHandle , char **InputFile , char *Output_Directory, int
   option)
   ```
   
   **Parameter Name**
   
   - **ImgWizHlpHandle** - *Handle created using initialization*
   - **InputFile** - *Array on input files. In case of multipage TIFF all pages will be considered as input. This should be with full path.*
   - **Output_Directory** - *Expected output directory.*
   - **option** - *Following are the possible options: -*
   ```C/C++
   //C/C++
   No_DPI_change = 0 
   ResetAllDPI = 1
   ResetZeroDPI = 2
   ```
   *If only compression is to be performed then pass 0 as the option.*
   *If all images has to be resized to the standard page size then use **ResetAllDPI** option*
   *If only mobile captured images to be resized then keep **ResetZeroDPI** as the parameter.*

6. **AppendToTiff** - This function will append a tiff image over an existing tiff image.

   **Parameter Name**
   
   - **ImgWizHlpHandle** - *Handle created using initialization*
   - **InputFile** - *Array on input files. In case of multipage TIFF all pages will be considered as input. This should be with full path.*
   - **Output_File** - *Expected output file name with directory.*
   - **option** - *Following are the possible options: -*
   ```C/C++
   //C/C++
   No_DPI_change = 0 NO
   ResetAllDPI = 1
   ResetZeroDPI = 2
   ```
   *If only compression is to be performed then pass 0 as the option.*
   *If all images has to be resized to the standard page size then use **ResetAllDPI** option*
   *If only mobile captured images to be resized then keep **ResetZeroDPI** as the parameter.*


