package browserAcces;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;

/**
 * clasa pt controlul unei instante flashPlayer
 *
 */
public class FlashPlayer extends PluginBase
{
	private JWebBrowser player;
	private JPanel jComponent;
	
	/**
	 * constructor pt clasa flashPlayer
	 * @param id id-ul acestei instante flashPlayer(unit, setat doar la creare)
	 * @throws ExceptionInInitializerError daca parametrul creator este nul
	 * @throws ExceptionIdIsNotUnique daca id-ul nu este unic in cadrul Managerului(nu ar trebui sa apara dacaeste creat cu ajutorul unui plugin manager)
	 */
	public FlashPlayer(String id, PluginManager creator) throws ExceptionInInitializerError, ExceptionIdIsNotUnique
	{
		NativeInterface.open();
		
		if(creator != null)
		{
			if(creator.idIsUnique(id))
			{		
				instanceId = id;
		
				jComponent = new JPanel(new BorderLayout());
				player = new JWebBrowser();
				
				jComponent.add(player, BorderLayout.CENTER);
				player.setBarsVisible(false);
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
		if(player == null)
		{
			jComponent = new JPanel(new BorderLayout());
			player = new JWebBrowser();
			
			jComponent.add(player, BorderLayout.CENTER);
			player.setBarsVisible(false);
		}
		
		return jComponent;
	}
	
	/**
	 * @param path calea locala care se gaseste resursa care va fi prelucrata de acest plugin
	 */
	@Override
	public void loadContent(String path)
	{
		player.navigate(path);
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
	
	public String getAdressTitle()
	{
		return player.getPageTitle();
	}
	
	/**
	 * callback din pluginManager la apelarea 'clearFlashPlayerRefferences'
	 */
	@Override
	public void onDestroy()
	{
		player.stopLoading();
		
		super.interrupt();
		
		NativeInterface.close();
	}
}
