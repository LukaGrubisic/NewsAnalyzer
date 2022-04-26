package newsanalyzer.ui;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import newsanalyzer.ctrl.Controller;
import newsapi.enums.Category;
import newsapi.enums.Country;
import newsapi.enums.Endpoint;
import newsapi.exceptionModel.NewsApiException;

public class UserInterface 
{

	private Controller ctrl = new Controller();

	public void getDataFromCtrl1(){
		System.out.println("Get corona news");
		try{
			String s = ctrl.process("corona", Endpoint.EVERYTHING, Country.us, Category.health);
			System.out.println(s);
		}catch (NewsApiException | IOException e){
			System.out.println(e.getMessage());
		}
	}


	public void getDataFromCtrl2(){
		System.out.println("Get business news");
		try {
			String s = ctrl.process("business", Endpoint.TOP_HEADLINES, Country.us, Category.business);
			System.out.println(s);
		}catch (NewsApiException | IOException e ){
			System.out.println(e.getMessage());
		}
	}

	public void getDataFromCtrl3(){
		System.out.println("Get technology news");
		try {
			String s = ctrl.process("Apple", Endpoint.TOP_HEADLINES, Country.us, Category.technology);
			System.out.println(s);
		}catch (NewsApiException | IOException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void getDataForCustomInput() {
		Scanner scanner = new Scanner(System.in);

		System.out.println("Search for your topic: ");
		String topic = scanner.nextLine();

		System.out.println("[headline|all]:");
		String givenEndpoint = scanner.nextLine();

		System.out.println("country code [at,us,en,...] default country is [at]:");
		String givenCountry = scanner.nextLine();

		System.out.println("Category[health,technology,business,sport,entertainment,science]");
		String givenCategory = scanner.nextLine();

		try {
			ctrl.process(topic, getChosenEndpoint(givenEndpoint), getChosenCountry(givenCountry), getChosenCategory(givenCategory));
		} catch (NewsApiException | IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private Endpoint getChosenEndpoint(String inputEndpoint){
		if("headline".equalsIgnoreCase(inputEndpoint))
			return Endpoint.TOP_HEADLINES;
		return Endpoint.EVERYTHING;
	}

	private Country getChosenCountry(String inputCountry){
		var filteredCountry = Arrays.stream(Country.values()).filter(x -> Objects.equals(x.toString(), inputCountry.toLowerCase()));
		var findMatch = filteredCountry.findAny();
		return findMatch.orElse(Country.at);
	}

	private Category getChosenCategory(String inputCategory) {
		var filteredCategory = Arrays.stream(Category.values()).filter(x -> Objects.equals(x.toString(), inputCategory.toLowerCase()));
		return filteredCategory.findAny().orElse(Category.health);
	}


	public void start() {
		Menu<Runnable> menu = new Menu<>("User Interfacx");
		menu.setTitel("Wählen Sie aus:");
		menu.insert("a", "Get corona news", this::getDataFromCtrl1);
		menu.insert("b", "Get business news", this::getDataFromCtrl2);
		menu.insert("c", "Get technology news", this::getDataFromCtrl3);
		menu.insert("d", "Choice User Input:",this::getDataForCustomInput);
		menu.insert("q", "Quit", null);

		Runnable choice;
		while ((choice = menu.exec()) != null) {
			 choice.run();
		}
		System.out.println("Program finished");
	}


    protected String readLine() {
		String value = "\0";
		BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
		try {
			value = inReader.readLine();
        } catch (IOException ignored) {
		}
		return value.trim();
	}

	protected Double readDouble(int lowerlimit, int upperlimit) 	{
		Double number = null;
        while (number == null) {
			String str = this.readLine();
			try {
				number = Double.parseDouble(str);
            } catch (NumberFormatException e) {
                number = null;
				System.out.println("Please enter a valid number:");
				continue;
			}
            if (number < lowerlimit) {
				System.out.println("Please enter a higher number:");
                number = null;
            } else if (number > upperlimit) {
				System.out.println("Please enter a lower number:");
                number = null;
			}
		}
		return number;
	}
}
