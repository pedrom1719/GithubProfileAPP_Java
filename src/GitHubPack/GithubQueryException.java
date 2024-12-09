package GitHubPack;

public class GithubQueryException extends RuntimeException{
    public GithubQueryException(String mensagemErro){
        super(mensagemErro);
    }
}
