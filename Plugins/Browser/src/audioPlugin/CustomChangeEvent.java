package audioPlugin;

import java.util.List;

import javax.swing.event.ChangeEvent;

public class CustomChangeEvent extends ChangeEvent
{
	private List<String> changedProperties;

	public CustomChangeEvent(Object source, List<String> changedProperties)
	{
		super(source);
		
		this.changedProperties = changedProperties;
	}

	public List<String> getChangedProperties()
	{
		return changedProperties;
	}
}