import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.IOException;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.Comparator;
import java.lang.Long;
import java.lang.Float;
import java.lang.NumberFormatException;
import java.util.OptionalDouble;
import java.text.DecimalFormatSymbols;

class FileParser
{
	static final boolean DEBUG_BUILD = true;
	
	public static void main(String[] args)
	{
		if (args.length == 0)
		{
			System.out.println("""
			Usage: FileParser [OPTION]... [FILE]...
			
			-p,
			prefix for the output file
			
			-o,
			output path
			
			-a,
			enable append mode
			
			-s,
			enable short statistics
			
			-f,
			enable full statistics""");
			return;
        }
		
		String prefix = "";
		String out_path = ".";
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
		
		if (in_files.isEmpty())
		{
			System.out.println("No input files were given, can't continue");
			return;
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
		
		for (String line : in_lines)
		{
			if (isStringInteger(line))
			{
				integers.add(line);
				continue;
			}
			else if (isStringFloat(line))
			{
				floats.add(line);
				continue;
			}
			strings.add(line);
		}
		
		if (DEBUG_BUILD)
		{
			System.out.println("---------------------------");
			System.out.println("strings=" + strings);
			System.out.println("integers=" + integers);
			System.out.println("floats=" + floats);
			System.out.println("---------------------------");
		}
		
		writeOutputFile(strings, out_path, prefix, "strings.txt", append, short_stats, full_stats, DataTypes.string);
		writeOutputFile(integers, out_path, prefix, "integers.txt", append, short_stats, full_stats, DataTypes.integer);
		writeOutputFile(floats, out_path, prefix, "floats.txt", append, short_stats, full_stats, DataTypes._float);
	}
	
	static int writeOutputFile(List<String> strings, String out_path, String prefix, String file_name,  boolean append, boolean short_stats, boolean full_stats, DataTypes datatype)
	{
		if (!strings.isEmpty())
		{
			File out_file = new File(new File(out_path), prefix + file_name);
			try (FileWriter myWriter = new FileWriter(out_file, append))
			{
				List<Long> ints = new ArrayList<>();
				List<Double> floats = new ArrayList<>();
				for (String string : strings)
				{
					if (full_stats && (datatype == DataTypes.integer))
					{
						try
						{
							ints.add(Long.parseLong(string));
						}
						catch (NumberFormatException e)
						{
							System.out.println("ERROR: Couldn't convert string to a number: " + string);
							System.out.println("ERROR: Numbers statistics for integers will be incorrect");
						}
					}
					if (full_stats && (datatype == DataTypes._float))
					{
						try
						{
							floats.add(Double.parseDouble(string));
						}
						catch (NumberFormatException e)
						{
							System.out.println("ERROR: Couldn't convert string to a number: " + string);
							System.out.println("ERROR: Numbers statistics for floats will be incorrect");
						}
					}
					myWriter.write(string + "\n");
				}
				if (full_stats && (datatype == DataTypes.string))
				{
					short_stats = false;
					System.out.println("Wrote " + strings.size() + " lines to " + prefix + file_name);
					System.out.println("Shortest string length: " + Collections.min(strings, Comparator.comparing(String::length)).length());
					System.out.println("Longest string length: " + Collections.max(strings, Comparator.comparing(String::length)).length());
				}
				if (full_stats && (datatype == DataTypes.integer))
				{
					short_stats = false;
					System.out.println("Wrote " + strings.size() + " lines to " + prefix + file_name);
					System.out.println("Min number: " + Collections.min(ints));
					System.out.println("Max number: " + Collections.max(ints));
					System.out.println("Sum: " + sumIntsList(ints));
					OptionalDouble average = ints.stream().mapToDouble(a -> a).average();
					System.out.println("Average: " + (average.isPresent() ? average.getAsDouble() : "ERROR: Not able to calculate average"));
				}
				if (full_stats && (datatype == DataTypes._float))
				{
					short_stats = false;
					System.out.println("Wrote " + strings.size() + " lines to " + prefix + file_name);
					System.out.println("Min number: " + Collections.min(floats));
					System.out.println("Max number: " + Collections.max(floats));
					System.out.println("Sum: " + sumFloatsList(floats));
					OptionalDouble average = floats.stream().mapToDouble(a -> a).average();
					System.out.println("Average: " + (average.isPresent() ? average.getAsDouble() : "ERROR: Not able to calculate average"));
				}
				if (short_stats)
					System.out.println("Wrote " + strings.size() + " lines to " + prefix + file_name);
			}
			catch (IOException e)
			{
				System.out.println("Error writing to a file");
				e.printStackTrace();
				return 1;
			}
		}
		return 0;
	}
	
	static boolean isStringInteger(String str)
	{
		DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
		char localeMinusSign = currentLocaleSymbols.getMinusSign();

		if ( !Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign )
			return false;
		
		for (char c : str.substring(1).toCharArray())
		{
			if (!Character.isDigit(c))
				return false;
		}
		return true;
	}
	
	static boolean isStringFloat(String str)
	{
		DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
		char localeMinusSign = currentLocaleSymbols.getMinusSign();

		if (!Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign)
			return false;
		
		boolean isDecimalSeparatorFound = false;
		boolean isExponentSeparatorFound = false;
		
		for (int i = 0; i < str.substring(1).length(); i++)
		{
			if (!Character.isDigit(str.substring(1).charAt(i)))
			{
				if (isDecimalSeparator(str.substring(1).charAt(i)) && !isDecimalSeparatorFound)
				{
					isDecimalSeparatorFound = true;
					continue;
				}
				if (isExponentSeparator(str.substring(1).charAt(i)) && !isExponentSeparatorFound)
				{
					if (!Character.isDigit(str.substring(1).charAt(i+1)) && !isExponentSeparator2(str.substring(1).charAt(i+1)))
						return false;
					else
						i++;
					isExponentSeparatorFound = true;
					continue;
				}
				return false;
			}
		}
		if (!isDecimalSeparatorFound)
			return false;
		
		return true;
	}
	
	static boolean isDecimalSeparator(char c)
	{
		char[] DecimalSeparators = {'.', ','};
		for (char DecimalSeparator : DecimalSeparators)
		{
			if (c == DecimalSeparator)
				return true;
		}
		return false;
	}
	
	static boolean isExponentSeparator(char c)
	{
		char[] ExponentSeparators = {'e', 'E'};
		for (char ExponentSeparator : ExponentSeparators)
		{
			if (c == ExponentSeparator)
				return true;
		}
		return false;
	}
	
	static boolean isExponentSeparator2(char c)
	{
		char[] ExponentSeparators = {'+', '-'};
		for (char ExponentSeparator : ExponentSeparators)
		{
			if (c == ExponentSeparator)
				return true;
		}
		return false;
	}
	
	static Long sumIntsList(List<Long> ints)
	{
		Long res = (long)0;
		for (Long _int : ints)
		{
			res += _int;
		}
		return res;
	}
	
	static Double sumFloatsList(List<Double> floats)
	{
		Double res = 0.0;
		for (Double _float : floats)
		{
			res += _float;
		}
		return res;
	}
	
	static enum DataTypes 
	{
		string,
		integer,
		_float
	}
}
