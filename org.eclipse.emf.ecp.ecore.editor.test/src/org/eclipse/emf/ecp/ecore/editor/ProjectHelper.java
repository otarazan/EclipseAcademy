package org.eclipse.emf.ecp.ecore.editor;

public class ProjectHelper {

	private static String projectFullName;

	public static String getProjectName() {

		
		String[] split = getProjectFullName().split("\\.");
		
		if(split.length<=0)
			return "";
		
		return split[split.length-1];
	}

	public static String getNSPrefix() {
       
		String projectName=getProjectFullName();
		
		return projectName.substring(0,projectName.lastIndexOf("."));
	}

	public static String getNSURL() {

		String[] split = getProjectFullName().split("\\.");

		String temp=split[0];
		split[0]=split[1];
		split[1]=temp;
		
		String result = join(split,"/");
		StringBuilder mystring= new StringBuilder(result);
		mystring.setCharAt(result.indexOf('/'), '.');		
		result="http://"+mystring;
		
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

	public static String getProjectFullName() {
		return projectFullName;
	}

	public static void setProjectFullName(String projectFullName) {
		ProjectHelper.projectFullName = projectFullName;
	}

	
	

}
