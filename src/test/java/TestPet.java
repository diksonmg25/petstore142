// 0 - nome do pacote

// 1 - bibliotecas

// 2 - Classe

import io.restassured.response.Response; // Classe Resposta do REST-assured

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given; // função given do REST-assured
import static org.hamcrest.Matchers.*;          // Classe de verificadores do Hamcrest

// static - quando executa o programa carrega so uma vez e nao muda

// sendo assim mais rapido

public class TestPet {
    // 2.1 atributos
    static String ct = "application/json"; // content-type
    static String uriPet = "https://petstore.swagger.io/v2/pet";
    // baseUrl + estrutura do endpoint da classe usuario - /user

    static int petId = 189906601;

    // 2.2 funçoes e metodos
    // 2.2.1 funçoes e metodos comuns/uteis

    // Funçao de leitura de Json
    public static String lerArquivoJson(String arquivoJson) throws IOException {
        return new String(Files.readAllBytes(Paths.get(arquivoJson)));
    }

    // 2.2.2 metodos de teste

    @Test
    public void testPostPet() throws IOException {
        // Configura
        
        // carrregar os dados do arquivo json do pet
        // TODO: Criar o metodo Post

        String jsonBody = lerArquivoJson("src/test/resources/json/pet1.json");

        // começa o teste via REST-assured

        given()                                 // Dado que 
         .contentType(ct)                        // o tipo do conteudo e ct
         .log().all()                            // mostre tudo na ida
         .body(jsonBody)                         // envie o corpo da requisiçao

        // Executa
         .when()                                 // Quando -- verbo que voce quer fazer, açao que voce quer fazer
         .post(uriPet)                       // Chamamos o endpoint fazendo post 

        // Valida
         .then()                                 // Entao
         .log().all()                        // Mostre tudo na volta
         .statusCode(200) // Verifique se o status code e 200
         .body("name", is("Snoopy")) // Verifique se o nome e Snoopy
         .body("id", is(petId))            // Verifique o codigo do pet
         .body("category.name", is("cachorro")) // Verifique se e cachorro
         .body("tags[0].name", is("vacinado"))  // Verifique se esta vacinado
        ; // fim do given
       }

       @Test
       public void testGetPet(){
           // Configura
           // Entrada - petId que esta definido no nivel da classe
           // Saidas / Resultados Esperados
           String petName = "Snoopy";
           String categoryName = "cachorro";
           String tagName = "vacinado";

           given()
                .contentType(ct)
                .log().all()
                // quando e get ou delete nao tem body
            // Executa
            .when()  
               .get(uriPet + "/" + petId) // montar o endpoint da URI/<petId>

            // Valida
            .then()
                .log().all()
                .statusCode(200)
                .body("name", is("Snoopy")) // Verifique se o nome e Snoopy
                .body("id", is(petId))            // Verifique o codigo do pet
                .body("category.name", is("cachorro")) // Verifique se e cachorro
                .body("tags[0].name", is("vacinado"))  // Verifique se esta vacinado

           ; // fim do given

       }
}