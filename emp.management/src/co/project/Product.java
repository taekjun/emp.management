package co.project;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product extends User{
	private String pName;
	private int stock;
	private int price;
	private int userNo;
}

