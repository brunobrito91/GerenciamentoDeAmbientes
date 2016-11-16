package converter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.primefaces.component.picklist.PickList;
import org.primefaces.model.DualListModel;

import com.model.Ambiente;

@FacesConverter(value = "ambienteConverter")
public class AmbienteConverter implements Converter {

	@Override
	public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
		if (arg1 instanceof PickList) {
			PickList pickList = (PickList) arg1;
			@SuppressWarnings("unchecked")
			DualListModel<Ambiente> dualListModel = (DualListModel<Ambiente>) pickList.getValue();
			for (Ambiente ambiente : dualListModel.getSource()) {
				if (ambiente.getNome().equals(arg2)) {
					return ambiente;
				}
			}

			for (Ambiente ambiente : dualListModel.getTarget()) {
				if (ambiente.getNome().equals(arg2)) {
					return ambiente;
				}
			}
		}
		return null;
	}

	@Override
	public String getAsString(FacesContext arg0, UIComponent arg1, Object arg2) {
		if (arg2 instanceof Ambiente) {
			Ambiente ambiente = (Ambiente) arg2;
			return ambiente.getNome();
		}

		return null;
	}
}
