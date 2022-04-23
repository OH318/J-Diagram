package demo.JavaCodeGenerator.roaster;

import java.io.Serializable;
import java.util.Date;

public class PersonPojo extends Date implements Serializable {

	private static final long serialVersionUID = -1L;
	private String firstName;
	private Integer id;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public PersonPojo(java.lang.Integer id) {
		this.id = id;
	}

	private void increaseId(int step) {
		this.id += step;
	}
}