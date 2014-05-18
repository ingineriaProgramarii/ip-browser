package audioPlugin;

import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultBoundedRangeModel;

public class CustomBoundRangeModel extends DefaultBoundedRangeModel
{

	public CustomBoundRangeModel()
	{
		
	}

	public CustomBoundRangeModel(int value, int extent, int min, int max)
	{
		super(value, extent, min, max);
	}

	@Override
	public void setRangeProperties(int newValue, int newExtent, int newMin,	int newMax, boolean adjusting)
	{
		int oldMax = getMaximum();
		int oldMin = getMinimum();
		int oldValue = getValue();
		int oldExtent = getExtent();
		
		boolean oldAdjusting = getValueIsAdjusting();
		
		List<String> changedProperties = new ArrayList<>();
		
		if (oldMax != newMax)
		{
			changedProperties.add("maximum");
		}
		
		if (oldValue != newValue)
		{
			changedProperties.add("value");
		}
		
		changeEvent = changedProperties.size() > 0 ? new CustomChangeEvent(this, changedProperties) : null;
		
		super.setRangeProperties(newValue, newExtent, newMin, newMax, adjusting);
	}
}