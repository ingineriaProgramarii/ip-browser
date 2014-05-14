package browserAcces;

import javax.swing.JComponent;

import chrriis.dj.nativeswing.swtimpl.components.JFlashPlayer;

/**
 * clasa pt controlul unei instante flashPlayer
 *
 */
public class FlashPlayer extends PluginBase
{
	private JFlashPlayer flashPlayer;
	
	/**
	 * constructor pt clasa flashPlayer
	 * @param id id-ul acestei instante flashPlayer(unit, setat doar la creare)
	 * @throws ExceptionInInitializerError daca parametrul creator este nul
	 * @throws ExceptionIdIsNotUnique daca id-ul nu este unic in cadrul Managerului(nu ar trebui sa apara dacaeste creat cu ajutorul unui plugin manager)
	 */
	public FlashPlayer(String id, PluginManager creator) throws ExceptionInInitializerError, ExceptionIdIsNotUnique
	{
		if(creator != null)
		{
			if(creator.idIsUnique(id))
			{		
				instanceId = id;
		
				flashPlayer = new JFlashPlayer();
			}
			else
			{
				throw new ExceptionIdIsNotUnique();
			}
		}
		else
		{		
			throw new ExceptionInInitializerError();
		}
	}
	
	/**
	 * functie care returneaza instanta flashPlayer asociata acestei (instante)clase
	 * @return componenta de adaugat in interfata swing pt a vedea continutul pluginului
	 */
	@Override
	public JComponent getSwingComponent()
	{
		if(flashPlayer == null)
		{
			flashPlayer = new JFlashPlayer();
		}
		
		return flashPlayer;
	}
	
	/**
	 * @param path calea locala care se gaseste resursa care va fi prelucrata de acest plugin
	 */
	@Override
	public void loadContent(String path)
	{
		flashPlayer.load(path);
	}
	
	/**
	 * returneaza id-ul acestei instante(unic)
	 * @return id-ul ca String
	 */
	@Override
	public String getInstanceId()
	{
		return super.instanceId;
	}
	
	/**
	 * callback din pluginManager la apelarea 'clearFlashPlayerRefferences'
	 */
	@Override
	public void onDestroy()
	{
		flashPlayer.stop();
		
		super.interrupt();
	}
}
