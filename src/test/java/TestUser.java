import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasLength;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;

public class TestUser {
    static String ct = "aplication/json";
    static String uriUser = "https://petstore.swagger.io/v2/user";
    static String token;

    @Test
    public static String testLogin() { 
         
               // funçao

               // Configura

        String username = "roberto";
        String password = "abfrgv";
        String resultadoEsperado = "logged in user session:";

        Response resposta = (Response) given() // cast -- o que vai receber deve estar no formato de uma resposta
                .contentType(ct)
                .log().all()

                // Executa

                .when()
                .get(uriUser + "/login?username=" + username + "&password=" + password) // endpoint com parametros

                // Valida

                .then()
                .log().all()
                .statusCode(200)
                .body("code", is(200))
                .body("type", is("unknown"))
                .body("message", containsString(resultadoEsperado)) // olhar na frase inteira se contem esse texto -
                                                                    //"logged in user session:"
                .body("message", hasLength(36)) // tamanho do campo message
                .extract()

        ;

        // extraçao

        token = resposta.jsonPath().getString("message").substring(23); // subtring -- pega o numero a partir da posiçao 23 - 13 ultimos caracteres
        System.out.println("Conteudo do token: " + token);

        return token;

    }

}


