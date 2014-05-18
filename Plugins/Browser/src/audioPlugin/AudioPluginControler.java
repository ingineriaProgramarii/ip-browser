package audioPlugin;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicSliderUI;

/**
 * plugin pt redarea fisierelor wav
 * @author lawrence
 *
 */
public class AudioPluginControler extends JPanel
{
	JPanel volumePanel;

	JLabel volumeLabel;

	JButton volUp, volDown, playPause;

	JSlider seeker;

	int volume = 75;

	ImageIcon playImage, pauseImage, volUpImage, volDownImage;

	AudioPluginCore core;

	Timer updateSeeker;

	/**
	 * event apelat la apasarea butonului play(din pauza)
	 */
	ActionListener onPlay = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			playPause.removeActionListener(onPlay);
			playPause.addActionListener(onPause);

			playPause.setIcon(pauseImage);

			core.resume();
		}
	};

	/**
	 * event apelat la apasarea butonului resume(din play)
	 */
	ActionListener onPause = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			playPause.removeActionListener(onPause);
			playPause.addActionListener(onPlay);

			playPause.setIcon(playImage);

			core.pause();
		}
	};

	/**
	 * event apelat la apasarea butonului de volume up
	 */
	ActionListener volumeUp = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			volume += 5;

			if (volume > 100)
				volume = 100;

			if(volume < 10)
			{
				volumeLabel.setText(" " + volume);
			}
			else	
			{
				volumeLabel.setText(volume + "");
			}
			
			core.setVolume(volume);
		}
	};

	/**
	 * event apelat la apasarea butonului de volume down
	 */
	ActionListener volumeDown = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			volume -= 5;

			if (volume < 0)
				volume = 0;

			if(volume < 10)
			{
				volumeLabel.setText(" " + volume);
			}
			else	
			{
				volumeLabel.setText(volume + "");
			}
			
			core.setVolume(volume);
		}
	};

	/**
	 * event apelat de un timer pt a updata bara de seek
	 */
	ActionListener timerTrigger = new ActionListener()
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			double percent = ((double) core.getLength()) / core.getMaxLength();

			seeker.setValue((int) (seeker.getMaximum() * percent));
		}
	};

	/**
	 * event apelat la miscarea barei de seek de catre utilizator(si de evntul de la timer)(incompleta)
	 */
	ChangeListener seekChange = new ChangeListener()
	{
		@Override
		public void stateChanged(ChangeEvent e)
		{
	        if (e instanceof CustomChangeEvent) 
	        {
	        	CustomChangeEvent me = (CustomChangeEvent) e;
	        	
	            if (me.getChangedProperties().contains("value")) 
	            {
	            	System.out.println("new value: " + ((BoundedRangeModel) e.getSource()).getValue());
	               
	            	float now = ((BoundedRangeModel)e.getSource()).getValue();
					float total = ((BoundedRangeModel)e.getSource()).getMaximum();

					//core.setPlayedAmount(now / total);
	            }
	        } 
	        else 
	        {
				float now = seeker.getValue();
				float total = seeker.getMaximum();

				//core.setPlayedAmount(now / total);
	        }
		}
	};

	/**
	 * contructor pt pluginul audio
	 * contruieste si interfata vizuala a pluginului
	 * @param path calea catre fisierul pe care acest plugin il va reda
	 */
	public AudioPluginControler(String path)
	{
		BufferedImage imgPlay = null, imgPause = null, imgVUp = null, imgVDn = null;

		Thread exec;

		try
		{
			imgPlay = ImageIO.read(new File(
					"resources/audioPlayer/play_button.png"));
			imgPause = ImageIO.read(new File(
					"resources/audioPlayer/pause_button.png"));
			imgVUp = ImageIO.read(new File(
					"resources/audioPlayer/volume_up.png"));
			imgVDn = ImageIO.read(new File(
					"resources/audioPlayer/volume_down.png"));

			core = new AudioPluginCore(path, volume);

		} catch (IOException e)
		{
			e.printStackTrace();
		}

		playImage = new ImageIcon(imgPlay.getScaledInstance(32, 32,
				Image.SCALE_SMOOTH));
		pauseImage = new ImageIcon(imgPause.getScaledInstance(32, 32,
				Image.SCALE_SMOOTH));
		volUpImage = new ImageIcon(imgVUp.getScaledInstance(32, 19,
				Image.SCALE_SMOOTH));
		volDownImage = new ImageIcon(imgVDn.getScaledInstance(32, 19,
				Image.SCALE_SMOOTH));

		volumePanel = new JPanel();
		volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.Y_AXIS));

		volumeLabel = new JLabel(volume + "");
		volumeLabel.setFont(new Font("Sherif", Font.BOLD, 42));

		playPause = new JButton(pauseImage);

		volUp = new JButton(volUpImage);
		volDown = new JButton(volDownImage);

		volumePanel.add(volUp);
		volumePanel.add(volDown);

		seeker = new JSlider(0, 1000);
		seeker.setModel(new CustomBoundRangeModel( 0, 0, 0, 1000));

		playPause.addActionListener(onPause);
		volUp.addActionListener(volumeUp);
		volDown.addActionListener(volumeDown);
		seeker.getModel().addChangeListener(seekChange);

		super.add(playPause);
		super.add(seeker);
		super.add(volumePanel);
		super.add(volumeLabel);
		
		exec = new Thread(core);

		exec.start();

		updateSeeker = new Timer(50, timerTrigger);

		updateSeeker.start();
		
		super.revalidate();
	}
	
	public void pause()
	{
		core.pause();
	}
	
	public void play()
	{
		core.resume();
	}
}