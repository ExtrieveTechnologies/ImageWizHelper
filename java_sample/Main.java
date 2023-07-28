package com.extrieve.imaging.sdk;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

import com.extrieve.imaging.sdk.ImageWizHelperJNI.ImageDPI;
import com.extrieve.imaging.sdk.ImageWizHelperJNI.ImageQuality;
import com.extrieve.imaging.sdk.ImageWizHelperJNI.Layout;
import com.extrieve.imaging.sdk.ImageWizHelperJNI.ResetOption;

public class Main {

	static String input[]=new String[1];
	static String outputFile;

	
	
	
	//This Function is used when we choose the Default options.
	static boolean PDFtoTiff(ImageWizHelperJNI objWizHelper, String args[]) {

		int ret, index;
		String InputFile = null;
		String OutputFile = null;

		// get the required parameter from command line .. 
		index = 0;
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
			}
			index++;
		} while (true);

		if (OutputFile == null || InputFile == null) {
			return false;
		}

		ret=objWizHelper.CompressToTIFF(new String[] { InputFile }, OutputFile,ResetOption.No_DPI_change); //compress Input File to TIFF.
		if (ret != 0)
		{
			throw new RuntimeException(objWizHelper.GetErrorDescription(ret));
			// GetErrorDescription gives the error description for the particular error code generated from "CompressToTIFF" function.
		}

		return true;
	}



	public static void main(String args[]) throws Exception {

		try {

			// The same HANDLE object can be used in the whole program for every function of ImageWizHelper.
			ImageWizHelperJNI HANDLE = new ImageWizHelperJNI();
			
			boolean ret;

			ret=PDFtoTiff(HANDLE,args);//functions create PDF to TIFF using same HANDLE for every function inside it.
			if(ret==true)
				System.out.println("Tiff file Successfully Generated.....");
			else {
				System.out.println("Usage: \n\tjava -jar ImageWizHelper.jar -i 'Input File Path' -o 'Output File Path'");
			}
		} catch (Exception Ex) {
			System.out.println(Ex.toString());
		}
		finally {
			System.exit(0);
		}

	}
}
