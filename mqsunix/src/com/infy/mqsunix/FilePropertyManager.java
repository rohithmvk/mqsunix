package com.infy.mqsunix;



import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class FilePropertyManager
{
	/**
	 * PropertyManager constructor is a blank constructor.
	 */

	public FilePropertyManager()
	{
		super();
	}

	 /**************************************************************************
	 Function        : getProperty
	 Description     : getProperty method gets the property value from the 
					property file .These parameter are passed as input to 
					this method
					@param String param is the parameter name whose value 
					is sought. The property name and value 
					@return String Property value of the property passed as 
					input parameter
	 *************************************************************************/
	public static synchronized String getProperty(String param)			
	{
		String strPropVal = null;
		
		URL myClassURL = FilePropertyManager.class.getProtectionDomain().getCodeSource().getLocation();
		//System.out.println(myClassURL.toString());
		int iLoc = myClassURL.toString().indexOf("/bin");
		String strLoc = myClassURL.toString().substring(5,iLoc) + "/data/QueueSettings.properties" ;
		//System.out.println("Class:FilePropertyManager; Fxn:getProperty >Location of Properties File: "+strLoc);
		Properties prop = null;
		FileInputStream stream = null;
		try 
		{
			prop = new Properties();
			stream = new FileInputStream(strLoc); 
			prop.load(stream);
			stream.close();
			strPropVal = prop.getProperty(param);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			System.out.println("Class:FilePropertyManager; Fxn:getProperty > Exception while loading property file");
			//return(null);
		}
		finally{
			try 
			{
				stream.close();
				prop.clear();
			}catch (IOException e) 
			{
				e.printStackTrace();
				System.out.println("Class:FilePropertyManager; Fxn:getProperty > Exception while loading property file");
				//return(null);
			}
		}
			
		if ( strPropVal == null )
		{
			System.out.println("Property[" + param + "] was not found");
		}
		return(strPropVal.trim());
	}
	
	
	
}
/******************************************************************************
End of File       : FilePropertyManager.java
******************************************************************************/