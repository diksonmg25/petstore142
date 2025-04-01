// 0 - nome do pacote

// 1 - bibliotecas

// 2 - Classe

import io.restassured.response.Response; // Classe Resposta do REST-assured

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.config;
import static io.restassured.RestAssured.given; // função given do REST-assured
import static org.hamcrest.Matchers.*; // Classe de verificadores do Hamcrest

// static - quando executa o programa carrega so uma vez e nao muda

// sendo assim mais rapido

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
// ordernar as anotações dessa classe

// ativa a ordenação

public class TestPet {
    // 2.1 atributos
    static String ct = "application/json"; // content-type
    static String uriPet = "https://petstore.swagger.io/v2/pet";
    // baseUrl + estrutura do endpoint da classe pet - /pet

    static int petId = 189906601;

    String petName = "Snoopy";
    String categoryName = "cachorro";
    String tagName = "vacinado";
    String[] status = { "available", "sold" };

    // 2.2 funçoes e metodos
    // 2.2.1 funçoes e metodos comuns/uteis

    // Funçao de leitura de Json
    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    // 2.2.2 metodos de teste

    @Test
    @Order(1)
    public void testPostPet() throws IOException {
    // Configura

    // carrregar os dados do arquivo json do pet
    // TODO: Criar o metodo Post

    String jsonBody = lerArquivoJson("src/test/resources/json/pet1.json");

    // começa o teste via REST-assured

    given() // Dado que
        .contentType(ct) // o tipo do conteudo e ct
        .log().all() // mostre tudo na ida
        .body(jsonBody) // envie o corpo da requisiçao

    // Executa
    .when() // Quando -- verbo que voce quer fazer, açao que voce quer fazer
        .post(uriPet) // Chamamos o endpoint fazendo post

    // Valida
    .then() // Entao
        .log().all() // Mostre tudo na volta
        .statusCode(200) // Verifique se o status code e 200
        .body("name", is(petName)) // Verifique se o nome e Snoopy
        .body("id", is(petId)) // Verifique o codigo do pet
        .body("category.name", is(categoryName)) // Verifique se e cachorro
        .body("tags[0].name", is(tagName)) // Verifique se esta vacinado
        ; // fim do given
    }

    @Test
    @Order(2)
    public void testGetPet() {
    // Configura
    // Entrada e saidas definidas no nivel da classe

    given()
        .contentType(ct)
        .log().all()
        .header("", "api_key: " + TestUser.testLogin()) // passa o header junto com o token para autorizar a realizar outras operaçoes
        
        // se e algo que precisa de autorizaçao seja post, get, put ou delete

        // precisa ter o header
        

        // quando e get ou delete nao tem body


        // Executa
        .when()
            .get(uriPet + "/" + petId) // montar o endpoint da URI/<petId>

        // Valida
        .then()
            .log().all()
            .statusCode(200)
            .body("name", is(petName)) // Verifique se o nome e Snoopy
            .body("id", is(petId)) // Verifique o codigo do pet
            .body("category.name", is(categoryName)) // Verifique se e cachorro
            .body("tags[0].name", is(tagName)) // Verifique se esta vacinado

        ; // fim do given
    }

    @Test
    @Order(3)
    public void testPutPet() throws IOException {
    // Configura

    String jsonBody = lerArquivoJson("src/test/resources/json/pet2.json");

    given()
        .contentType(ct)
        .log().all() // log da entrada - requisiçao / log do que vai
        .body(jsonBody)

    // Executa
    .when()
        .put(uriPet)

    // Valida
    .then()
        .log().all() // log da saida - response - log do que volta
        .statusCode(200)
        .body("name", is(petName)) // Verifique se o nome e Snoopy
        .body("id", is(petId)) // Verifique o codigo do pet
        .body("category.name", is(categoryName)) // Verifique se e cachorro
        .body("tags[0].name", is(tagName)) // Verifique se esta vacinado
        .body("status", is(status[1])) // status do pet na loja
        ;

    }

    @Test
    @Order(4)
    public void testDeletePet() {

    // Configura --> Dados de entrada e saida começo da classe

    given()
        .contentType(ct)
        .log().all()

    // Executa

    .when()
        .delete(uriPet + "/" + petId)

    // Valida
        .then()
        .log().all()
        .statusCode(200) // se comunicou e processou
        .body("code", is(200)) // se apagou
        .body("type", is("unknown"))
        .body("message", is(String.valueOf(petId)));
    }

    // Data Driven Testing (DDT) - Teste Direcionado por Dados / Teste com Massa
    // Teste com Json parametrizado


    @ParameterizedTest @Order(5) // faz um teste parametrizado - 5 º a ser executado
    @CsvFileSource(resources = "/csv/petMassa.csv", numLinesToSkip = 1, delimiter = ',')// ler um arquivo csv externo
    // pula uma linha, o separador dele e a virgula 

    public void testPostPetDDT(
        int petId,
        String petName,
        int catId,
        String catName,
        String status1,
        String status2

    ) // fim dos parametros
      
      // inicio do codigo do metodo testPostPetDDT
    {
        // Criar a classe pet para receber os dados do csv

        Pet pet = new Pet(); // classe Pet - objeto pet - instancia a classe Pet

        // referencia dos campos
        // conectando os campos de dados com os campos da estrutura

        Pet.Category category = pet.new Category(); // instancia a subclasse Category
        Pet.Tag[] tags = new Pet.Tag[2]; // instancia subclasse Tag
        tags[0]= pet.new Tag();
        tags[1] = pet.new Tag();

        pet.id = petId; // petId de fora - recebe os dados do csv
        pet.category = category; // associar a pet.category com a subclasse category
        pet.category.id = catId;
        pet.category.name = catName;
        pet.name = petName;
        // pet.photoUrls não precisa ser incluido porque será vazio
        pet.tags = tags; // associa a pet.tags com a subclasse tags
        pet.tags[0].id = 9;
        pet.tags[0].name = "vacinado";
        pet.tags[1].id = 8;
        pet.tags[1].name = "vermifugado";
        pet.status = status1; // status inicial usado no Post = "available"
 
        // Criar um Json para o body a ser enviado a partir da classe Pet e do CSV

        // Json dinamico
        
        Gson gson = new Gson(); // Instancia a classe Gson como o objeto gson

        String jsonBody = gson.toJson(pet);
        
        // Configura
        given()
            .contentType(ct)
            .log().all()
            .body(jsonBody)

        // Executa
        .when()
            .post(uriPet)
        
        // Valida
        .then()
            .log().all()
            .statusCode(200)
            .body("id", is(petId)) // se o id que volta no json na resposta e o petId
            .body("name", is(petName))
            .body("category.id", is(catId)) // se o id que volta no json na resposta e o id da categoria
            .body("category.name", is(catName)) // se o name que volta no json na resposta e o nome da categoria
            .body("status", is(status1)) // status inicial do Post
        ;

    }
}