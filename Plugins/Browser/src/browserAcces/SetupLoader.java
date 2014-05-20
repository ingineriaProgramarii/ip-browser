package browserAcces;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SetupLoader
{
	private boolean flashPlayerIsEnabled;
	private boolean pdfPluginIsEnabled;
	private boolean audioPluginIsEnabled;
	
	public Boolean getFlashPlayerIsEnabled()
	{
		return flashPlayerIsEnabled;
	}
	public void setFlashPlayerIsEnabled(Boolean flashPlayerIsEnabled)
	{
		this.flashPlayerIsEnabled = flashPlayerIsEnabled;
	}
	public Boolean getPdfPluginIsEnabled()
	{
		return pdfPluginIsEnabled;
	}
	public void setPdfPluginIsEnabled(Boolean pdfPluginIsEnabled)
	{
		this.pdfPluginIsEnabled = pdfPluginIsEnabled;
	}
	public Boolean getAudioPluginIsEnabled()
	{
		return audioPluginIsEnabled;
	}
	public void setAudioPluginIsEnabled(Boolean audioPluginIsEnabled)
	{
		this.audioPluginIsEnabled = audioPluginIsEnabled;
	}
	
	public void loadData()
	{
		FileInputStream stream = null;
		
		try
		{
			stream = new FileInputStream("managerData.txt");
			
			DataInputStream inputStream = new DataInputStream(stream);
			
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			
			String line = bufferedReader.readLine();
			
			setFlashPlayerIsEnabled(getValue("flashPlayer", line));
			
			line = bufferedReader.readLine();
			
			setAudioPluginIsEnabled(getValue("audioPlayer", line));
			
			line = bufferedReader.readLine();
			
			setPdfPluginIsEnabled(getValue("pdfPlugin", line));
			
			inputStream.close();
		} 
		
		catch (FileNotFoundException e)
		{
			e.printStackTrace();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public void saveData()
	{
		try
		{
			File file = new File("managerData.txt");
			
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileWriter stream = new FileWriter(file.getAbsoluteFile());
			
			BufferedWriter writer = new BufferedWriter(stream);	
			
			writer.write("flashPlayer " + (getFlashPlayerIsEnabled() ? 1 : 0));
			
			writer.newLine();
			
			writer.write("audioPlayer " + (getAudioPluginIsEnabled() ? 1 : 0));
			
			writer.newLine();
			
			writer.write("pdfPlugin " + (getPdfPluginIsEnabled() ? 1 : 0));
			
			writer.close();
		} 
		
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	
	Boolean getValue(String option, String input)
	{		
		Pattern pattern = Pattern.compile(option);
		
		Matcher match = pattern.matcher(input);
		
		match.find();
		
		return input.substring(match.end() + 1, input.length()).equals("1");
	}
}
