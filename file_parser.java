import java.util.ArrayList;

class FileParser
{
	static final boolean DEBUG_BUILD = true;
	
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
            System.out.println("Usage: TODO");
        }
		
		String prefix = "";
		String out_path = "";
		boolean append = false;
		boolean short_stats = false;
		boolean full_stats = false;
		ArrayList<String> in_files = new ArrayList<String>();
		
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].equals("-p"))
			{
				prefix = args[i+1];
				i++;
				continue;
			}
			if (args[i].equals("-o"))
			{
				out_path = args[i+1];
				i++;
				continue;
			}
			if (args[i].equals("-a"))
			{
				append = true;
				continue;
			}
			if (args[i].equals("-s"))
			{
				short_stats = true;
				continue;
			}
			if (args[i].equals("-f"))
			{
				full_stats = true;
				continue;
			}
			in_files.add(args[i]);
		}
		
		if (DEBUG_BUILD)
		{
			System.out.println("prefix=" + prefix);
			System.out.println("out_path=" + out_path);
			System.out.println("append=" + append);
			System.out.println("short_stats=" + short_stats);
			System.out.println("full_stats=" + full_stats);
			System.out.println("in_files=" + in_files);
		}
	}
}
