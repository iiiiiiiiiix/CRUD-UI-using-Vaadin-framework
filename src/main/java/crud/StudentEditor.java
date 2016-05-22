package crud;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.data.fieldgroup.BeanFieldGroup;
import com.vaadin.event.ShortcutAction;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.Button;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringComponent
@UIScope
public class StudentEditor extends VerticalLayout {
	
	private static StudentRepository repository;//здесь хранятся данные таблицы

	private Student student;

	TextField firstName = new TextField("First name");
	TextField lastName = new TextField("Last name");
	Button save = new Button("Save");
	Button delete = new Button("Delete");
	CssLayout actions = new CssLayout(save, delete);

	@Autowired
	public StudentEditor(StudentRepository repository) {
		this.repository = repository;

		addComponents(firstName, lastName, actions);

		setSpacing(true);
		actions.setStyleName(ValoTheme.LAYOUT_COMPONENT_GROUP);
		save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		save.addClickListener(e -> repository.save(student));
		delete.addClickListener(e -> repository.delete(student));
		setVisible(false);
	}

	public interface ChangeHandler {

		void onChange();
	}

	public final void editStudent(Student s) {

		final boolean persisted = s.getId() != null;
		if (persisted) {
			student = repository.findOne(s.getId());
			save.setCaption("OK");
			delete.setCaption("Delete");
			delete.setStyleName(ValoTheme.BUTTON_DANGER);
		}
		else {
			student = s;
			save.setCaption("Create");
			delete.setCaption("Cancel");
			delete.setStyleName(ValoTheme.BUTTON_PRIMARY);
		}

		//связывает значение из таблицы с текстфилдом в editor
		BeanFieldGroup.bindFieldsUnbuffered(student, this);

		setVisible(true);

	}

	public void setChangeHandler(ChangeHandler h) {
		save.addClickListener(e -> h.onChange());
		delete.addClickListener(e -> h.onChange());
	}

}
