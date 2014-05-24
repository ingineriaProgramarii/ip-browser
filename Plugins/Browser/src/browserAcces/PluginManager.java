package browserAcces;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.qoppa.pdf.PDFException;

import audioPlugin.AudioPluginControler;

/**
 * Clasa de baza PluginManager de unde se pot accesa plugin-urile
 * 
 * @author AlexState
 * 
 */
public class PluginManager extends JApplet
{
	private int baseId;

	public SetupLoader dataLoader;

	ArrayList<FlashPlayer> loadedFlashPlayers;
	ArrayList<AudioPlayer> loadedAudioPlugins;
	ArrayList<PdfPlugin> loadedPdfPlugins;

	/**
	 * Constructorul clasei PluginManager
	 */
	public PluginManager()
	{
		dataLoader = new SetupLoader();

		dataLoader.loadData();

		loadedFlashPlayers = new ArrayList<>();
		loadedAudioPlugins = new ArrayList<AudioPlayer>();
		loadedPdfPlugins = new ArrayList<PdfPlugin>();
	}

	/**
	 * creeaza o instanta noua de flashPlayer
	 * 
	 * @return instanta nou creata sau null daca este aruncata vreo exceptie
	 * @throws ExceptionIdIsNotUnique
	 * @throws ExceptionInInitializerError
	 * @throws ExceptionPluginDisabled
	 */
	public FlashPlayer createNewFlashPlayer(String path) throws ExceptionInInitializerError, ExceptionIdIsNotUnique, ExceptionPluginDisabled
	{
		FlashPlayer flashPlayer = null;

		if (dataLoader.getFlashPlayerIsEnabled())
		{
			flashPlayer = new FlashPlayer(getUniqueId(), this);

			loadedFlashPlayers.add(flashPlayer);

			flashPlayer.loadContent(path);
			
			return flashPlayer;
		} else
		{
			throw new ExceptionPluginDisabled();
		}
	}

	/**
	 * 
	 * @param path
	 * @return
	 * @throws ExceptionPluginDisabled
	 * @throws ExceptionIdIsNotUnique
	 */
	public AudioPlayer createNewAudioPlugin(String path)
			throws ExceptionPluginDisabled, ExceptionIdIsNotUnique
	{
		AudioPlayer audio = null;

		if (dataLoader.getAudioPluginIsEnabled())
		{
			audio = new AudioPlayer(getUniqueId(), path, this);

			loadedAudioPlugins.add(audio);
		} else
		{
			throw new ExceptionPluginDisabled();
		}

		return audio;
	}

	public PdfPlugin createNewPdfPlugin(String path) throws FileNotFoundException, ExceptionIdIsNotUnique, PDFException,	ExceptionPluginDisabled
	{
		PdfPlugin pdfPlugin = null;

		if (dataLoader.getPdfPluginIsEnabled())
		{
			pdfPlugin = new PdfPlugin(getUniqueId(), path, this);

			loadedPdfPlugins.add(pdfPlugin);
		} else
		{
			throw new ExceptionPluginDisabled();
		}

		return pdfPlugin;
	}

	/**
	 * sterge referintele din acest pluginManager pt un flashPlayer cu id-ul dat
	 * ca parametru
	 * 
	 * @param instanceId
	 *            id-ul flashPlayerului de 'eliberat'
	 */
	public void clearFlashPlayerRefferences(String instanceId)
	{
		for (int i = 0; i < loadedFlashPlayers.size(); i++)
		{
			if (loadedFlashPlayers.get(i).getInstanceId().equals(instanceId))
			{
				loadedFlashPlayers.get(i).onDestroy();

				loadedFlashPlayers.remove(i);

				return;
			}
		}
	}

	public void clearAudioPluginRefferences(String instanceId)
	{
		for (int i = 0; i < loadedAudioPlugins.size(); i++)
		{
			if (loadedAudioPlugins.get(i).getInstanceId().equals(instanceId))
			{
				loadedAudioPlugins.get(i).onDestroy();

				loadedAudioPlugins.remove(i);

				return;
			}
		}
	}

	public void clearPdfPluginRefferences(String instanceId)
	{
		for (int i = 0; i < loadedPdfPlugins.size(); i++)
		{
			if (loadedPdfPlugins.get(i).getInstanceId().equals(instanceId))
			{
				loadedPdfPlugins.get(i).onDestroy();

				loadedPdfPlugins.remove(i);

				return;
			}
		}
	}

	/**
	 * genereaza un id unic pt a fi folosit in crearea unui plugin nou
	 * 
	 * @return id-ul in format String
	 */
	private String getUniqueId()
	{
		return ++baseId + "";
	}

	/**
	 * testeaza daca un id este unic
	 * 
	 * @param id
	 *            id-ul de testat
	 * @return true daca id-ul nu exista in listele de pluginuri care ruleaza
	 */
	public boolean idIsUnique(String id)
	{
		for (int i = 0; i < loadedFlashPlayers.size(); i++)
		{
			if (loadedFlashPlayers.get(i).getInstanceId().equals(id))
			{
				return false;
			}
		}

		return true;
	}

	public static void main(String[] args)
	{
		final PluginManager maager = new PluginManager();

		maager.dataLoader.setPdfPluginIsEnabled(true);

		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				JFrame frame = new JFrame("");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

				try
				{
					frame.getContentPane().add(maager.createNewFlashPlayer("https://www.youtube.com/v/3fDZXQImlAA").getSwingComponent());
				}
				
				catch (ExceptionInInitializerError | ExceptionIdIsNotUnique
						| ExceptionPluginDisabled e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				frame.setSize(800, 600);
				frame.setLocationByPlatform(true);
				frame.setVisible(true);
				frame.pack();
			}
		});
	}
}