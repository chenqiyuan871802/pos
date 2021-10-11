package websys;

import java.io.File;
import java.io.IOException;
 
public class gitsvn
{
	public static final String GITSVN = ".keep";
 
	private static void check(File path) throws Exception 
	{
		File[] files = path.listFiles();
		if(files.length == 0)
		{
			System.out.printf("+ %s/.keep\n", path.getAbsolutePath());
			mark(path.getAbsolutePath());
		} 
		else
		{
			if(files.length!=1 || !files[0].getName().equals(GITSVN))
			{
				for (int i=0; i<files.length; i++) 
				{
					if(files[i].getName().equals(GITSVN))
					{
						System.out.printf("- %s\n", files[i].getAbsolutePath());
						files[i].delete();
					}
					
					if (files[i].isDirectory()) 
					{
						try 
						{
							check(files[i]);
						} 
						catch (Exception e) 
						{
							
						}
					}
				}
			}
		}
	}
	
	private static void clear(File path, String target) throws Exception 
	{
		File[] files = path.listFiles();
 
		for (int i=0; i<files.length; i++) 
		{
			if(files[i].getName().equals(target))
			{
				System.out.printf("- %s\n", files[i].getName());
				files[i].delete();
			}
			else if (files[i].isDirectory()) 
			{
				try 
				{
					clear(files[i], target);
				} 
				catch (Exception e) 
				{
					
				}
			}
		}
	}
 
	public static void mark(String path) 
	{
		File file = new File(path + "/" + GITSVN);
		try 
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args)
	{
		String home = System.getProperty("user.dir");
		File file = new File(home);
 
		try 
		{
			if(args.length==2 && args[0].equals("-clean"))
			{
				clear(file, args[1]);
			}
			else
			{
				check(file);
			}
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
}
