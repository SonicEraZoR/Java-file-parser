import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;

class FileParser
{
	static final boolean DEBUG_BUILD = true;
	
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println("Usage: FileParser [OPTION]... [FILE]...");
			System.out.println("");
			System.out.println("-p,");
			System.out.println("prefix for the output file");
			System.out.println("");
			System.out.println("-o,");
			System.out.println("output path");
			System.out.println("");
			System.out.println("-a,");
			System.out.println("enable append mode");
			System.out.println("");
			System.out.println("-s,");
			System.out.println("enable short statistics");
			System.out.println("");
			System.out.println("-f,");
			System.out.println("enable full statistics");
        }
		
		String prefix = "";
		String out_path = "";
		boolean append = false;
		boolean short_stats = false;
		boolean full_stats = false;
		List<String> in_files = new ArrayList<>();
		
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
			System.out.println("---------------------------");
			System.out.println("prefix=" + prefix);
			System.out.println("out_path=" + out_path);
			System.out.println("append=" + append);
			System.out.println("short_stats=" + short_stats);
			System.out.println("full_stats=" + full_stats);
			System.out.println("in_files=" + in_files);
			System.out.println("---------------------------");
		}
		
		List<File> in_files_obj = new ArrayList<>();
		for (String in_file : in_files)
		{
			in_files_obj.add(new File(in_file));
		}
		
		List<String> in_lines = new ArrayList<>();
		
		for (File in_file_obj : in_files_obj)
		{
			try (Scanner myReader = new Scanner(in_file_obj))
			{
				while (myReader.hasNextLine())
				{
					String line = myReader.nextLine();
					in_lines.add(line);
				}
			}
			catch (FileNotFoundException e)
			{
				System.out.println("Error reading input files.");
				e.printStackTrace();
			}
		}
		
		if (DEBUG_BUILD)
		{
			System.out.println("---------------------------");
			for (String in_line : in_lines)
				System.out.println(in_line);
			System.out.println("---------------------------");
		}
		
		List<String> strings = new ArrayList<>();
		List<String> integers = new ArrayList<>();
		List<String> floats = new ArrayList<>();
		
		String[] numbers = {"1", "2", "3", "4", "5", "6", "7", "8", "9"};
		String[] decimal_separators = {".", ","};
		
		boolean added_float = false;
		boolean added_integer = false;
		
		for (String line : in_lines)
		{
			for (String decimal_separator : decimal_separators)
			{
				if (line.indexOf(decimal_separator) != -1)
				{
					floats.add(line);
					added_float = true;
					break;
				}
			}
			if (!added_float)
			{
				for (String number : numbers)
				{
					if (line.indexOf(number) != -1)
					{
						integers.add(line);
						added_integer = true;
						break;
					}
				}
			}
			if (!added_float && !added_integer)
			{
				strings.add(line);
			}
			added_float = false;
			added_integer = false;
		}
		
		if (DEBUG_BUILD)
		{
			System.out.println("---------------------------");
			System.out.println("strings=" + strings);
			System.out.println("integers=" + integers);
			System.out.println("floats=" + floats);
			System.out.println("---------------------------");
		}
		
		if (!strings.isEmpty())
		{
			try (FileWriter myWriter = new FileWriter(prefix + "strings.txt", append))
			{
				for (String string : strings)
				{
					myWriter.write(string + "\n");
				}
			}
			catch (IOException e)
			{
				System.out.println("Error writing to a file");
				e.printStackTrace();
			}
		}
		if (!integers.isEmpty())
		{
			try (FileWriter myWriter = new FileWriter(prefix + "integers.txt", append))
			{
				for (String integer : integers)
				{
					myWriter.write(integer + "\n");
				}
			}
			catch (IOException e)
			{
				System.out.println("Error writing to a file");
				e.printStackTrace();
			}
		}
		if (!floats.isEmpty())
		{
			try (FileWriter myWriter = new FileWriter(prefix + "floats.txt", append))
			{
				for (String _float : floats)
				{
					myWriter.write(_float + "\n");
				}
			}
			catch (IOException e)
			{
				System.out.println("Error writing to a file");
				e.printStackTrace();
			}
		}
	}
}
