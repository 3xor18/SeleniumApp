package com.scraping.app.scraping;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Login2 {
	private static Logger log = LoggerFactory.getLogger(Login2.class);
	// Declaracion que Siempre se Debe Hacer Para Trabajar con Selenium
	private WebDriver driver;
	// Variables
	String ususario = "766034764";
	String clave = "Bmorin5100";

	public void encenderRobot() throws InterruptedException {
		// Configuracion de las properties: (por que nos conectaremos con mozzila, ruta
		// absoluta de donde esta el driver)
		System.setProperty("webdriver.gecko.driver", "C:\\Users\\Tinet\\Documents\\driver\\geckodriver.exe");
		driver = new FirefoxDriver();
		// le pasamos la url a conectarnos
		driver.get(
				"https://zeusr.sii.cl/AUT2000/InicioAutenticacion/IngresoRutClave.html?https://www4.sii.cl/consemitidosinternetui/#/defaultInternet");
		// que espere 1 segundo
		Thread.sleep(1000);
		// que seleccione textbox rutcntr, ingresar valos de variable usuario
		driver.findElement(By.id("rutcntr")).sendKeys(ususario);
		// Seleccione texbox clave, ingrese el contenido de variable clave
		driver.findElement(By.id("clave")).sendKeys(clave);
		// click en el boton del formulario(como no tiene id le pasamos el xpath)
		driver.findElement(
				By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Clave'])[1]/following::button[1]"))
				.click();

		// Rut o clave Errados
		if (!driver.findElement(By.xpath(
				"(.//*[normalize-space(text()) and normalize-space(.)='Valores y fechas'])[1]/preceding::div[3]"))
				.isDisplayed() == false) {
			log.info("Error En Credenciales");
			driver.quit();
		} else {
			// rut o clave OK
			Thread.sleep(4000);
			driver.findElement(By.linkText("Factura No Afecta o Exenta Electrónica (34)")).click();

			WebElement table_element = driver.findElement(By.id("table"));
			List<WebElement> tr_collection = table_element.findElements(By.xpath("id('table')/tbody/tr"));
			Integer documentos = tr_collection.size() + 1;
			log.info("Numero de Documentos= " + documentos);

			// Capturar Tabla
			WebElement table = driver.findElement(By.id("table"));
			// esperar que se cargue la data
			Thread.sleep(4000);
			// Capturar las filas de la table
			List<WebElement> allRows = table.findElements(By.tagName("tr"));
			// Iterar en todas las filas encontradas
			for (WebElement row : allRows) {
				// capturar las columnas de la fina
				List<WebElement> cells = row.findElements(By.tagName("td"));
				for (WebElement cell : cells) {
					// si tiene valor, quitamos el boton publicar
					if (cell.getText().length() > 0 && !cell.getText().equals("Publicar")) {
						// si no llega al total de montos
						if (!cell.getText().equals("Total montos")) {
							log.info("content >>   " + cell.getText());
						} else {
							// si llega a total de montos salirse
							break;
						}
					}

				}
			}
		}
	}

	public void desconectarRobot() throws InterruptedException {
		Thread.sleep(14000);
		// Desconectamos el robot(cierre de explorador)
		driver.quit();
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			log.info("Encontrado");
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	public static void main(String[] args) throws InterruptedException {
		// Instanciamos la clase actual
		Login2 login = new Login2();
		// llamamos a la funcion que inicia el robot
		login.encenderRobot();
		// llamamos el robot que desconecta el robot
		login.desconectarRobot();
	}
}
