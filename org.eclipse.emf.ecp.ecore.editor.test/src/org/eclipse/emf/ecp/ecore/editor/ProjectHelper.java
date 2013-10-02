package org.eclipse.emf.ecp.ecore.editor;

public class ProjectHelper {



	public static String getProjectName(String string) {

		
		String[] split = string.split(".");
		return split[split.length -1];
	}

	public static String getNSPrefix(String string) {

		return string.substring(0,string.lastIndexOf(".")-1);
	}

	public static String getNSURL(String string) {

		String[] split = string.split(".");

		String temp=split[0];
		split[0]=split[1];
		split[1]=temp;
		
		String result = join(split,"/");
				
		result+="http://"+string;
		
		return result;
		
	}
	
	//join(String array,delimiter)
	public static String join(String r[],String d)
	{
	        if (r.length == 0) return "";
	        StringBuilder sb = new StringBuilder();
	        int i;
	        for(i=0;i<r.length-1;i++)
	            sb.append(r[i]+d);
	        return sb.toString()+r[i];
	}

}
