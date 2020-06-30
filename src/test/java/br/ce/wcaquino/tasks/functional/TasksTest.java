package br.ce.wcaquino.tasks.functional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TasksTest {
	public static DateTimeFormatter task = DateTimeFormatter.ofPattern("A EEEE");
	public static DateTimeFormatter dueDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public WebDriver acessarAplicacao() {
		WebDriver driver = new ChromeDriver();
		driver.navigate().to("http://localhost:8081/tasks/");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		return driver;
	}

	@Test
	public void deveSalvarTarefaComSucesso() {
		LocalDateTime dataTarefa = LocalDateTime.now();
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no bot�o Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a descri��o
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium " + dataTarefa.format(task));

			// Escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dataTarefa.format(dueDate));

			// Clicar no bot�o Save
			driver.findElement(By.id("saveButton")).click();

			// Validar mensagem de Sucesso
			String mensagem = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Success!", mensagem);
		} finally {
			// Fechar o browser
			driver.quit();
		}
	}

	@Test
	public void naoDeveSalvarTarefaSemDescricao() {
		LocalDateTime dataTarefa = LocalDateTime.now();
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no bot�o Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dataTarefa.format(dueDate));

			// Clicar no bot�o Save
			driver.findElement(By.id("saveButton")).click();

			// Validar mensagem de Erro
			String mensagem = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Fill the task description", mensagem);
		} finally {
			// Fechar o browser
			driver.quit();
		}
	}

	@Test
	public void naoDeveSalvarTarefaSemData() {
		LocalDateTime dataTarefa = LocalDateTime.now();
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no bot�o Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a descri��o
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium " + dataTarefa.format(task));

			// Clicar no bot�o Save
			driver.findElement(By.id("saveButton")).click();

			// Validar mensagem de Erro
			String mensagem = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Fill the due date", mensagem);
		} finally {
			// Fechar o browser
			driver.quit();
		}
	}

	@Test
	public void naoDeveSalvarTarefaComDataPassada() {
		LocalDateTime dataTarefa = LocalDateTime.now().minusDays(1);
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no bot�o Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a descri��o
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium " + dataTarefa.format(task));

			// Escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dataTarefa.format(dueDate));

			// Clicar no bot�o Save
			driver.findElement(By.id("saveButton")).click();

			// Validar mensagem de Erro
			String mensagem = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Due date must not be in past", mensagem);
		} finally {
			// Fechar o browser
			driver.quit();
		}
	}
}
