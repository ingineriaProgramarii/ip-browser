package browserAcces;

import java.util.ArrayList;
import javax.swing.JApplet;

/**
 * Clasa de baza PluginManager de unde se pot accesa plugin-urile 
 * @author AlexState
 *
 */
public class PluginManager extends JApplet
{
	private int baseId;
	
	ArrayList<FlashPlayer> loadedFlashPlayers;
	
/**
 * Constructorul clasei PluginManager
 */
	public PluginManager()
	{
		loadedFlashPlayers = new ArrayList<>();
	}

	/**
	 * creeaza o instanta noua de flashPlayer
	 * @return instanta nou creata sau null daca este aruncata vreo exceptie
	 */
	public FlashPlayer createNewFlashPlayer()
	{
		FlashPlayer flashPlayer = null;
		
		try 
		{
			flashPlayer = new FlashPlayer(getUniqueId(), this);
			
			loadedFlashPlayers.add(flashPlayer);
			
			return flashPlayer;
		} 
		
		catch (ExceptionInInitializerError | ExceptionIdIsNotUnique e)
		{			
			e.printStackTrace();
		}
		
		return flashPlayer;
	}
	
	/**
	 * sterger referintele din acest pluginManager pt un flashPlayer cu id-ul dat ca parametru
	 * @param instanceId id-ul flashPlayerului de 'eliberat'
	 */
	public void clearFlashPlayerRefferences(String instanceId)
	{
		for(int i = 0;i < loadedFlashPlayers.size();i++)
		{
			if(loadedFlashPlayers.get(i).getInstanceId().equals(instanceId))
			{
				loadedFlashPlayers.get(i).onDestroy();
				
				loadedFlashPlayers.remove(i);
				
				return;
			}
		}
	}
	
	/**
 	* Metoda pentru a selecta activarea unui anumit plugin(probabil va fi stearsa, nu insistati cu testarea)
 	* @param pluginType
 	* @param path
 	*/
	public void execPlugin(String pluginType, String path)
	{
			switch (pluginType)
			{
				case "audioPlayer":
				{
					//this.audioP = new AudioPlayer();
	
					//URL f;
					//f = new URL(path);
					//audioP.loadContent(f);
	
					break;
				}
				case "pdfViewer":
				{
					//this.pdfViewer=new PDFViewerApplet();
					//this.pdfViewer.init();
					//this.pdfViewer.start(path);
					break;
				}
				case "flashPlayer":
				{
					
					
					break;
				}

			}
	}
	
	/**
	 * genereaza un id unic pt a fi folosit in crearea unui plugin nou
	 * @return id-ul in format String
	 */
	private String getUniqueId()
	{
		return ++baseId + "";
	}
	
	/**
	 * testeaza daca un id este unic
	 * @param id id-ul de testat
	 * @return true daca id-ul nu exista in listele de pluginuri care ruleaza
	 */
	public boolean idIsUnique(String id)
	{
		for(int i = 0;i < loadedFlashPlayers.size();i++)
		{
			if(loadedFlashPlayers.get(i).getInstanceId().equals(id))
			{
				return false;
			}
		}
		
		return true;
	}

	public static void main(String[] args)
	{
		
	}
}