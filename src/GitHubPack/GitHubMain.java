package GitHubPack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

record GitProfile(String login, String user_view_type, String name, int public_repos, int followers, int following, String created_at){}

public class GitHubMain {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner userInput = new Scanner(System.in);

        System.out.println("\n ----- BUSCANDO PERFIL NO GITHUB ----- \n");

        System.out.println("Busque por nome de usuário: ");
        String userName = userInput.next();

        String endPoint = "https://api.github.com/users/" + userName;

        try{
            HttpClient meuClient = HttpClient.newHttpClient();
            HttpRequest meuRequest = HttpRequest.newBuilder().uri(URI.create(endPoint)).build();

            HttpResponse<String> meuResponse = meuClient.send(meuRequest, HttpResponse.BodyHandlers.ofString());

            int responseStatus = meuResponse.statusCode();

            CheckQuery(userName, responseStatus);

            String jsonResponse = meuResponse.body();

            Gson meuGson = new GsonBuilder().setLenient().create();
            GitProfile meuPerfil = meuGson.fromJson(jsonResponse, GitProfile.class);

            System.out.println("\n --- INFORMAÇÕES: ---");
            System.out.println("Nome de usuário: " + meuPerfil.login() + " (" + meuPerfil.user_view_type() + ")");
            System.out.println("Nome: " + meuPerfil.name());
            System.out.println("Repositórios: " + meuPerfil.public_repos());
            System.out.println("Seguidores: " + meuPerfil.followers());
            System.out.println("Seguindo: " + meuPerfil.following());
            System.out.println("Criado em: " + meuPerfil.created_at());


        } catch (IOException | InterruptedException e) {
            System.out.println("\nERRO: Houve um erro durante a consulta à API do GitHub.");
            e.printStackTrace();

        } catch (GithubQueryException e){
            System.out.println(e.getMessage());

        } finally {
            System.out.println("\nEncerrando aplicação.");

        }
    }

    private static void CheckQuery(String userName, int status){
        if (status == 404) {
            throw new GithubQueryException("\nERRO: Não foi possível encontrar um usuário \"" + userName + "\" na base de dados.");
        }
    }
}
