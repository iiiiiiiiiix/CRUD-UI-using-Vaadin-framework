package crud;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.vaadin.annotations.Theme;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
@Theme("valo")
public class VaadinUI extends UI {

	private final StudentRepository repo;
	private final StudentEditor editor;
	private final Grid grid;
	private final Button addNewBtn;
	private final Button editThisBtn;

	@Autowired
	public VaadinUI(StudentRepository repo, StudentEditor editor) {
		this.repo = repo;
		this.editor = editor;
		this.grid = new Grid();
		this.addNewBtn = new Button("Add student");
		this.editThisBtn = new Button("Edit");
	}

	@Override
	protected void init(VaadinRequest request) {

		HorizontalLayout actions = new HorizontalLayout(addNewBtn, editThisBtn);
		HorizontalLayout middleLayout = new HorizontalLayout(grid, editor);
		VerticalLayout groupLayout = new VerticalLayout(actions, middleLayout);
		VerticalLayout mainLayout = new VerticalLayout(groupLayout);

		setContent(mainLayout);

		actions.setMargin(false);
		actions.setSpacing(true);
		middleLayout.setMargin(false);
		middleLayout.setSpacing(true);
		groupLayout.setMargin(false);
		groupLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);

		grid.setHeight(228, Unit.PIXELS);
		grid.setColumns("firstName", "lastName");

		editor.setVisible(false);
		editThisBtn.setEnabled(false);

		//делает недоступной кнопку Edit, если не выделен элемент таблицы
		grid.addSelectionListener(e -> {
			if (e.getSelected().isEmpty()) {
				editThisBtn.setEnabled(false);
			}
			else {
				editThisBtn.setEnabled(true);
			}
		});

		//добавляет студента
		addNewBtn.addClickListener(e -> editor.editStudent(new Student("", "")));

		//редактирует студента
		editThisBtn.addClickListener(e -> editor.editStudent((Student) grid.getSelectedRow()));

		//По нажатию на кнопку editor'а прячет его и наполняет таблицу
		editor.setChangeHandler(() -> {
			editor.setVisible(false);
			listStudents();
		});

		listStudents();

	}

	//наполняет таблицу
	private void listStudents() {
		grid.setContainerDataSource(new BeanItemContainer(Student.class, repo.findAll()));
	}

}
