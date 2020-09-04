package br.ce.wcaquino.tasks.functional;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

public class TasksTest {
	public static DateTimeFormatter task = DateTimeFormatter.ofPattern("A EEEE");
	public static DateTimeFormatter dueDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	public WebDriver acessarAplicacao() throws MalformedURLException {
//		WebDriver driver = new ChromeDriver();
		String ipDaMaquina = null;
		try {
			ipDaMaquina = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		DesiredCapabilities capabilities = DesiredCapabilities.chrome();
		WebDriver driver = new RemoteWebDriver(new URL("http://192.168.99.100:4444/wd/hub"),capabilities);
		driver.navigate().to("http://" + ipDaMaquina + ":8081/tasks/");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        return driver;
	}

	@Test
	public void deveSalvarTarefaComSucesso() throws MalformedURLException {
		LocalDateTime dataTarefa = LocalDateTime.now();
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no botão Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a descrição
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium " + dataTarefa.format(task));

			// Escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dataTarefa.format(dueDate));

			// Clicar no botão Save
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
	public void deveSalvarTarefaComDataFutura() throws MalformedURLException {
		LocalDateTime dataTarefa = LocalDateTime.now().plusDays(1);
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no botão Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a descrição
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium " + dataTarefa.format(task));

			// Escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dataTarefa.format(dueDate));

			// Clicar no botão Save
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
	public void naoDeveSalvarTarefaSemDescricao() throws MalformedURLException {
		LocalDateTime dataTarefa = LocalDateTime.now();
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no botão Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dataTarefa.format(dueDate));

			// Clicar no botão Save
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
	public void naoDeveSalvarTarefaSemData() throws MalformedURLException {
		LocalDateTime dataTarefa = LocalDateTime.now();
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no botão Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a descrição
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium " + dataTarefa.format(task));

			// Clicar no botão Save
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
	public void naoDeveSalvarTarefaComDataPassada() throws MalformedURLException {
		LocalDateTime dataTarefa = LocalDateTime.now().minusDays(1);
		WebDriver driver = acessarAplicacao();

		try {
			// Clicar no botão Add Todo
			driver.findElement(By.id("addTodo")).click();

			// Escrever a descrição
			driver.findElement(By.id("task")).sendKeys("Teste via Selenium " + dataTarefa.format(task));

			// Escrever a data
			driver.findElement(By.id("dueDate")).sendKeys(dataTarefa.format(dueDate));

			// Clicar no botão Save
			driver.findElement(By.id("saveButton")).click();

			// Validar mensagem de Erro
			String mensagem = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Due date must not be in past", mensagem);
		} finally {
			// Fechar o browser
			driver.quit();
		}
	}

	@Test
	public void deveRemoverTarefaComSucesso() throws MalformedURLException {
		WebDriver driver = acessarAplicacao();

		try {
			// Inserir tarefa
			deveSalvarTarefaComSucesso();

			// Clicar no botão Remove
			driver.findElement(By.xpath("//a[contains(text(),'Remove')]")).click();

			// Validar mensagem de Sucesso
			String mensagem = driver.findElement(By.id("message")).getText();
			Assert.assertEquals("Success!", mensagem);
		} finally {
			// Fechar o browser
			driver.quit();
		}
	}
}
