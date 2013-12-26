package geo.working.sheet;

import java.util.ArrayList;
import java.util.List;

public class WorkingSheet {
	private String name;
    private String email;
    private String key;
    
    public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	private List<String> questions = new ArrayList<String>();
  	
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getQuestions() {
		return questions;
	}

	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}
}
