package co.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
	private int userNo;
	private String id;
	private String pw;
	private String name;
	private String addr;
	private String phone;
	private int userType;
	
}
